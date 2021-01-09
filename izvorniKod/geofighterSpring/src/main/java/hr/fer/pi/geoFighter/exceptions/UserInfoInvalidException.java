package hr.fer.pi.geoFighter.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Error")
public class UserInfoInvalidException extends RuntimeException {
    private static final long serialVersionUID = 7066346125092106988L;

    public UserInfoInvalidException() {
    }

    public UserInfoInvalidException(String message) {
        super(message);
    }
}
