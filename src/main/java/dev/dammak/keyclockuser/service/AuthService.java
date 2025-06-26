package dev.dammak.keyclockuser.service;


import dev.dammak.keyclockuser.dto.request.UserLoginRequest;
import dev.dammak.keyclockuser.dto.request.UserRegistrationRequest;
import dev.dammak.keyclockuser.dto.response.AuthResponse;

/**
 * Service interface for authentication operations.
 */
public interface AuthService {

    AuthResponse register(UserRegistrationRequest request);

    AuthResponse login(UserLoginRequest request);

    AuthResponse refreshToken(String refreshToken);

    void logout(String refreshToken);
}