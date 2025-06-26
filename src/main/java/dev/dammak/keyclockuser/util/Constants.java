package dev.dammak.keyclockuser.util;

/**
 * Application constants.
 */
public final class Constants {

    private Constants() {
        // Private constructor to prevent instantiation
    }

    public static final class Security {
        public static final String BEARER_PREFIX = "Bearer ";
        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String ROLE_PREFIX = "ROLE_";
        public static final String ADMIN_ROLE = "ADMIN";
        public static final String USER_ROLE = "USER";
    }

    public static final class Api {
        public static final String API_VERSION = "/api/v1";
        public static final String USERS_PATH = API_VERSION + "/users";
        public static final String AUTH_PATH = API_VERSION + "/auth";
        public static final String PROFILE_PATH = API_VERSION + "/profile";
    }

    public static final class Messages {
        public static final String USER_NOT_FOUND = "User not found";
        public static final String USER_ALREADY_EXISTS = "User already exists";
        public static final String AUTHENTICATION_FAILED = "Authentication failed";
        public static final String ACCESS_DENIED = "Access denied";
        public static final String VALIDATION_FAILED = "Validation failed";
    }
}