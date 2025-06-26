package dev.dammak.keyclockuser.exception;
/**
 * User Already Exists Exception
 *
 * @author Damola Adekoya
 * @version 1.0.0
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}