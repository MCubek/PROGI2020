package hr.fer.pi.geoFighter.exceptions;

import org.springframework.mail.MailException;

public class SpringGeoFighterException extends RuntimeException {
    public SpringGeoFighterException(String s, MailException e) {
        super(s, e);
    }

    public SpringGeoFighterException(String s) {
        super(s);
    }
}
