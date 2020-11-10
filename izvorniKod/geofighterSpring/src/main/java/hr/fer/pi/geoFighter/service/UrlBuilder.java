package hr.fer.pi.geoFighter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author MatejCubek
 * @project pi
 * @created 10/11/2020
 */
@Component
public class UrlBuilder {

    @Value("${server.developer.address}")
    private String address;

    public String appendHost(String postfix) {
        return address + postfix;
    }
}
