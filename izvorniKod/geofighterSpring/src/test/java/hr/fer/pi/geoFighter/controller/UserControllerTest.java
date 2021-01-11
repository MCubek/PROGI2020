package hr.fer.pi.geoFighter.controller;

import hr.fer.pi.geoFighter.BaseIntegrationTest;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.repository.UserRepository;
import org.apiguardian.api.API;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author MatejCubek
 * @project pi
 * @created 11/01/2021
 */
class UserControllerTest extends BaseIntegrationTest {

    private static final String API_PATH_USERCONTROLLER = "/api/user";

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setupFixture() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void userProfile() throws Exception {
        User user = userRepository.findByUsername("user").get();

        var response = mockMvc.perform(
                get(API_PATH_USERCONTROLLER + "/userProfile/" + user.getUsername()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<String> responseObject = objectMapper.readValue(response.getContentAsString(), List.class);

        Assertions.assertEquals(user.getUsername(), responseObject.get(0));
        Assertions.assertEquals(user.getEloScore().toString(), responseObject.get(1));
    }
}