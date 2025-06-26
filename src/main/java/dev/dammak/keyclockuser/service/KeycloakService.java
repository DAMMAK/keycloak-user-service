package dev.dammak.keyclockuser.service;


import dev.dammak.keyclockuser.dto.request.UserRegistrationRequest;
import dev.dammak.keyclockuser.dto.response.AuthResponse;

/**
 * Service interface for Keycloak operations.
 */
public interface KeycloakService {

    String createUser(UserRegistrationRequest request);

    AuthResponse authenticateUser(String email, String password);

    AuthResponse refreshAccessToken(String refreshToken);

    void logout(String refreshToken);

    void deleteUser(String userId);
}