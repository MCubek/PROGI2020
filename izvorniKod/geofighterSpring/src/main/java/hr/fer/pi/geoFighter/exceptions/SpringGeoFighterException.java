package hr.fer.pi.geoFighter.exceptions;

import org.springframework.mail.MailException;

public class SpringGeoFighterException extends RuntimeException {
    private static final long serialVersionUID = 6411390069462815083L;

    public SpringGeoFighterException(String s, MailException e) {
        super(s, e);
    }

    public SpringGeoFighterException(String s) {
        super(s);
    }
}
