package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.dto.SendRequestDTO;
import hr.fer.pi.geoFighter.dto.UserCardDTO;
import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.model.*;
import hr.fer.pi.geoFighter.repository.FightRepository;
import hr.fer.pi.geoFighter.repository.LocationCardRepository;
import hr.fer.pi.geoFighter.repository.UserCardRepository;
import hr.fer.pi.geoFighter.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
@AllArgsConstructor
public class FightService {

    private final UserRepository userRepository;
    private final LocationCardRepository locationCardRepository;
    private final UserCardRepository userCardRepository;
    private final FightRepository fightRepository;

    private final List<SendRequestDTO> requests;
    private final List<SendRequestDTO> startPlaying;
    private static final Map<Long, List<String>> ongoingFight = new HashMap<>();

    private final AuthService authService;

    private final static Map<Long, String> fightIdWinnerMap = new HashMap<>();
    private final static Map<String, List<Long>> playerUsernameListCardsMap = new HashMap<>();

    @Transactional
    public List<UserCardDTO> getUserCardList(String username) throws MalformedURLException {
        List<UserCardDTO> userCards = new ArrayList<>();
        for (ArrayList<String> card : userRepository.findLocationCards(username)) {
            Long id = Long.valueOf(card.get(0));
            String name = card.get(1);
            String description = card.get(2);
            URL photoURL = new URL(card.get(3));
            userCards.add(new UserCardDTO(id, name, description, photoURL));
        }
        return userCards;
    }

    public String getWinnerOfFight(long fightId) {
        return fightIdWinnerMap.getOrDefault(fightId, null);
    }

    public void submitCards(Long[] fightCards) {
        playerUsernameListCardsMap.put(authService.getCurrentUser().getUsername(), Arrays.asList(fightCards));
    }

    public void deleteFight(Long fightId) {
        ongoingFight.remove(fightId);
        fightIdWinnerMap.remove(fightId);
    }

    @Transactional
    public void startFight(Long fightId) {
        var list = ongoingFight.get(fightId);
        String username1 = list.get(0);
        String username2 = list.get(1);

        String username = authService.getCurrentUser().getUsername();

        if (! username1.equals(username) && ! username2.equals(username))
            throw new SpringGeoFighterException("User not in fight!");

        //Nisu jos oba playera poslala svoje karte
        if (! playerUsernameListCardsMap.containsKey(username1) || ! playerUsernameListCardsMap.containsKey(username2))
            return;

        //Jedan je vec odradio borbu pa se preskace drugi put
        if (! fightIdWinnerMap.containsKey(fightId))
            fightIdWinnerMap.put(fightId, fight(new FightObject(username1, username2,
                    playerUsernameListCardsMap.get(username1), playerUsernameListCardsMap.get(username2))));
    }

    /**
     * Računa pobjednika na temelju primljenih karata
     *
     * @param fightObject objekt sa borcima i njihovim kartama
     * @return ime pobjednika, ili prazan string ako je draw
     */
    @Transactional
    public String fight(FightObject fightObject) {
        if (fightObject.getUser1selectedCardIds().size() != 3 ||
                fightObject.getUser2selectedCardIds().size() != 3)
            throw new SpringGeoFighterException("Bad request: users must have 3 selected cards each");

        User user1 = userRepository.findByUsername(fightObject.getUsername1()).orElseThrow(() -> new SpringGeoFighterException("User " + fightObject.getUsername1() + " not in database"));
        User user2 = userRepository.findByUsername(fightObject.getUsername2()).orElseThrow(() -> new SpringGeoFighterException("User " + fightObject.getUsername2() + " not in database"));

        List<UserCard> user1Cards = new ArrayList<>();
        List<UserCard> user2Cards = new ArrayList<>();
        List<UserCardFight> usersCardsFights = new ArrayList<>();

        playerUsernameListCardsMap.remove(fightObject.getUsername1());
        playerUsernameListCardsMap.remove(fightObject.getUsername2());

        // validate cards, check timeouts

        for (Long id : fightObject.getUser1selectedCardIds()) {
            // location card exists
            LocationCard assocCard = locationCardRepository.findById(id).orElseThrow(() -> new SpringGeoFighterException("No card with such ID in database: " + id));

            // user has that card
            UserCard assocUserCard = userCardRepository.findById(new UserCardId(user1.getUserId(), assocCard.getId())).orElseThrow(() -> new SpringGeoFighterException("User " + fightObject.getUsername1() + " doesn't have card with id: " + id));
            // card is not on cooldown
            LocalDateTime cd = assocUserCard.getCooldownEndTime();
            if (cd != null && cd.isAfter(LocalDateTime.now()))
                throw new SpringGeoFighterException("Card is on cooldown! User: " + user1.getUsername() + " Card ID: " + assocCard.getId());

            user1Cards.add(assocUserCard);
        }
        for (Long id : fightObject.getUser2selectedCardIds()) {
            // location card exists
            LocationCard assocCard = locationCardRepository.findById(id).orElseThrow(() -> new SpringGeoFighterException("No card with such ID in database: " + id));

            // user has that card
            UserCard assocUserCard = userCardRepository.findById(new UserCardId(user2.getUserId(), assocCard.getId())).orElseThrow(() -> new SpringGeoFighterException("User " + fightObject.getUsername2() + " doesn't have card with id: " + id));
            // card is not on cooldown
            LocalDateTime cd = assocUserCard.getCooldownEndTime();
            if (cd != null && cd.isAfter(LocalDateTime.now()))
                throw new SpringGeoFighterException("Card is on cooldown! User: " + user2.getUsername() + " Card ID: " + assocCard.getId());

            user2Cards.add(assocUserCard);
        }

        // fight

        User winner = null;
        User loser = null;
        boolean draw = false;

        double user1Score = 0.;
        double user2Score = 0.;
        for (int i = 0; i < 3; i++) {
            user1Score += calculateScore(user1Cards.get(i));
            user2Score += calculateScore(user2Cards.get(i));
        }

        if (Math.abs(user1Score - user2Score) < 0.2) {
            draw = true;
        } else if (user1Score > user2Score) {
            winner = user1;
            loser = user2;
        } else {
            winner = user2;
            loser = user1;
        }

        // update user cards

        for (UserCard uc1 : user1Cards) {
            double cdMultiplier = uc1.getCooldownMultiplier();

            // cool down multiplier++
            uc1.setCooldownMultiplier(cdMultiplier + 1);
            // default cool down 1h, each time used +1h
            uc1.setCooldownEndTime(LocalDateTime.now().plusHours((long) cdMultiplier));
        }
        for (UserCard uc2 : user2Cards) {
            double cdMultiplier = uc2.getCooldownMultiplier();

            uc2.setCooldownMultiplier(cdMultiplier + 1);
            uc2.setCooldownEndTime(LocalDateTime.now().plusHours((long) cdMultiplier));
        }

        // save fight

        Fight fight = new Fight();

        for (UserCard uc : user1Cards)
            usersCardsFights.add(new UserCardFight(user1, uc.getLocationCard(), fight));

        for (UserCard uc : user2Cards)
            usersCardsFights.add(new UserCardFight(user2, uc.getLocationCard(), fight));

        fight.setUserCardAssoc(usersCardsFights);
        fight.setWinner(winner);
        fightRepository.save(fight);

        // update winner

        if (! draw) {
            winner.setWins(winner.getWins() + 1);
            winner.getFightsWon().add(fight);
            loser.setLosses(loser.getLosses() + 1);
            calculateEloScore(winner, loser);
        }

        return draw ? "" : winner.getUsername();
    }

    private double calculateScore(UserCard uc) {
        LocationCard lc = uc.getLocationCard();
        return (lc.getDifficulty() + lc.getUncommonness() + lc.getPopulation())
                / (1 + 0.1 * uc.getCooldownMultiplier());
    }

    public void calculateEloScore(User winner, User loser) {

        float score1;
        float score2;
        int K = 40;
        float expectancyA = 1 / (1 + (float) Math.pow(10.0, (winner.getEloScore() - loser.getEloScore()) / 400f));
        float expectancyB = 1 - expectancyA;

        //calculating score in case of win/lose
        score1 = winner.getEloScore() + K * (1 - expectancyA);
        score2 = loser.getEloScore() - K * expectancyB;

        winner.setEloScore(Math.round(score1));
        loser.setEloScore(Math.round(score2));
    }

    public void sendRequest(SendRequestDTO sendRequestDTO) {
        requests.add(sendRequestDTO);
    }

    public List<String> getRequests(String username) {
        List<String> yourRequests = new ArrayList<>();
        for (SendRequestDTO u : requests) {
            if (u.getUsernameReceiver().equals(username)) {
                yourRequests.add(u.getUsernameSender());
            }
        }
        return yourRequests;
    }

    public void processAnswer(SendRequestDTO answer) {
        List<SendRequestDTO> copy = List.copyOf(requests);
        if (answer.isAnswer()) {
            startPlaying.add(answer);
            for (SendRequestDTO request : copy) {
                if (request.getUsernameSender().equals(answer.getUsernameSender())) {
                    requests.remove(request);
                }
            }
        } else {
            requests.remove(answer);
        }
    }

    public SendRequestDTO getMatches(String username) {
        for (SendRequestDTO match : startPlaying) {
            if (match.getUsernameSender().equals(username)) {
                List<String> players = new ArrayList<>();
                players.add(match.getUsernameSender());
                players.add(match.getUsernameReceiver());
                Long id = AtomicSequenceGenerator.getNext();
                ongoingFight.put(id, players);
                match.setBattleId(id);
                if (match.getBattleId() > 0L) {
                    startPlaying.remove(match);
                }
                return match;
            } else if (match.getUsernameReceiver().equals(username)) {
                if (match.getBattleId() > 0L) {
                    startPlaying.remove(match);
                }
                return match;
            }
        }
        return new SendRequestDTO("", "", false, 0L);
    }

    @AllArgsConstructor
    @Getter
    private static class FightObject {
        String username1;
        String username2;

        List<Long> user1selectedCardIds;
        List<Long> user2selectedCardIds;
    }

    private static class AtomicSequenceGenerator {

        private static final AtomicLong value = new AtomicLong(1);

        public static long getNext() {
            return value.getAndIncrement();
        }
    }
}