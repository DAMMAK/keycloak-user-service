package dev.dammak.keyclockuser.exception;

/**
 * User Not Found Exception
 *
 * @author Damola Adekoya
 * @version 1.0.0
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}