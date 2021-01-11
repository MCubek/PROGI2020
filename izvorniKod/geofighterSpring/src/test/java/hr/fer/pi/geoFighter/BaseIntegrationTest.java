package hr.fer.pi.geoFighter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

@SpringBootTest
public class BaseIntegrationTest {

    @Autowired
    protected ObjectMapper objectMapper;

    protected static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @BeforeAll
    static void test() {
        Assertions.assertDoesNotThrow(() -> {
        });
    }

    protected String getJsonObject(Object o) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

        return ow.writeValueAsString(o);
    }
}
