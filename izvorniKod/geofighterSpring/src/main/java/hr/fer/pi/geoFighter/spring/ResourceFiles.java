package hr.fer.pi.geoFighter.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author MatejCubek
 * @project pi
 * @created 08/01/2021
 */
@Configuration
public class ResourceFiles {

    @Value("classpath:cards.txt")
    private Resource cards;

    @Bean(name = "cards")
    public String readCards(){
        try(InputStream is = cards.getInputStream()){
            return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
