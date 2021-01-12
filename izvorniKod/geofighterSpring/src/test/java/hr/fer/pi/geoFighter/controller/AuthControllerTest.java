package hr.fer.pi.geoFighter.controller;

import hr.fer.pi.geoFighter.BaseIntegrationTest;
import hr.fer.pi.geoFighter.dto.AuthenticationResponse;
import hr.fer.pi.geoFighter.dto.CartographerRegisterRequest;
import hr.fer.pi.geoFighter.dto.LoginRequest;
import hr.fer.pi.geoFighter.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public class AuthControllerTest extends BaseIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private String token;

    @BeforeEach
    void setUpFixture() {
        mockMvc = webAppContextSetup(context).build();
    }

    @Test
    public void signup_allOk_success() throws Exception {

        mockMvc.perform(
                post("/api/auth/signup")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(getJsonObject(new RegisterRequest(
                                "newuser@user.com",
                                "newuser",
                                "password",
                                "https://i.ytimg.com/vi/-AVloqiXdk0/maxresdefault.jpg"))))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void signup_usernameTaken_throwException() throws Exception {

        mockMvc.perform(
                post("/api/auth/signup")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(getJsonObject(new RegisterRequest(
                                "newuser@user.com",
                                "admin",
                                "password",
                                "https://i.ytimg.com/vi/-AVloqiXdk0/maxresdefault.jpg"))
                        ))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void signup_badImageUrl_throwException() throws Exception {

        var a = mockMvc.perform(
                post("/api/auth/signup")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(getJsonObject(new RegisterRequest(
                                "newuser@user.com",
                                "newuser",
                                "password",
                                "https://wrongImageThatDoesntExist.jpg"))
                        ))
                .andExpect(status().is4xxClientError())
        .andReturn();

        System.out.println(a.getResponse());
    }

    @Test
    public void signupCartographer_allOk_success() throws Exception {
        if(token == null) token = login();

        mockMvc.perform(
                post("/api/auth/cartographer/apply")
                        .header("Authorization", token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(getJsonObject(new CartographerRegisterRequest(
                                "HR3824840089434722222",
                                "https://i.ytimg.com/vi/-AVloqiXdk0/maxresdefault.jpg"))))
                .andExpect(status().isOk());

    }

    @Test
    public void signupCartographer_badImageUrl_throwException() throws Exception {
        if(token == null) token = login();

        var a = mockMvc.perform(
                post("/api/auth/cartographer/apply")
                        .header("Authorization", token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(getJsonObject(new CartographerRegisterRequest(
                                "HR3824840089434722222",
                                "https://wrongImageThatDoesntExist.jpg"))
                        ))
                .andExpect(status().is4xxClientError())
                .andReturn();

        System.out.println(a.getResponse());
    }

    @Test
    public void signupCartographer_badIBAN_throwException() throws Exception {
        if(token == null) token = login();

        var a = mockMvc.perform(
                post("/api/auth/cartographer/apply")
                        .header("Authorization", token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(getJsonObject(new CartographerRegisterRequest(
                                "HR382484008943",
                                "https://wrongImageThatDoesntExist.jpg"))
                        ))
                .andExpect(status().is4xxClientError())
                .andReturn();

        System.out.println(a.getResponse());
    }

    @Test
    public void login_allOk_success() throws Exception {

        LoginRequest loginRequest = new LoginRequest("admin", "admin");

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(getJsonObject(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$.authorizationToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andReturn();

    }

    @Test
    public void login_invalidCredentials_throwException() {

        try {
            mockMvc.perform(
                    post("/api/auth/login")
                            .contentType(APPLICATION_JSON_UTF8)
                            .content(getJsonObject(new LoginRequest(
                                    "admin",
                                    "wrong"))
                            ));

            fail("Nothing was thrown");
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(BadCredentialsException.class);
        }

        try {
            mockMvc.perform(
                    post("/api/auth/login")
                            .contentType(APPLICATION_JSON_UTF8)
                            .content(getJsonObject(new LoginRequest(
                                    "wrong",
                                    "admin"))
                            ));

            fail("Nothing was thrown");
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(BadCredentialsException.class);
        }
    }

    private String login() throws Exception {
        return objectMapper.readValue(
                mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(getJsonObject(new LoginRequest(
                                "admin",
                                "admin"))
                        ))
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                AuthenticationResponse.class).getAuthorizationToken();
    }
}
