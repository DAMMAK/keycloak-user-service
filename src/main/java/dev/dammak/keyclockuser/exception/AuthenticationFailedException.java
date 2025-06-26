package dev.dammak.keyclockuser.exception;

/**
 * Authentication Failed Exception
 *
 * @author Damola Adekoya
 * @version 1.0.0
 */
public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException(String message) {
        super(message);
    }

    public AuthenticationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}