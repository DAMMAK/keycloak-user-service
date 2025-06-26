package dev.dammak.keyclockuser.service;

import dev.dammak.keyclockuser.dto.request.UserLoginRequest;
import dev.dammak.keyclockuser.dto.request.UserRegistrationRequest;
import dev.dammak.keyclockuser.dto.response.AuthResponse;
import dev.dammak.keyclockuser.dto.response.UserResponse;
import dev.dammak.keyclockuser.exception.AuthenticationFailedException;
import dev.dammak.keyclockuser.exception.UserAlreadyExistsException;
import dev.dammak.keyclockuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of AuthService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final KeycloakService keycloakService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public AuthResponse register(UserRegistrationRequest request) {
        log.debug("Registering user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email: " + request.getEmail());
        }

        try {
            // Create user in Keycloak
            String userId = keycloakService.createUser(request);
            log.debug("User created in Keycloak with ID: {}", userId);

            // Sync user to local database
            userService.syncUserFromKeycloak(userId, request.getEmail(),
                    request.getFirstName(), request.getLastName());

            // Get access token
            AuthResponse authResponse = keycloakService.authenticateUser(request.getEmail(), request.getPassword());

            // Get user details for response
            UserResponse userResponse = userService.getUserById(userId);
            authResponse.setUser(userResponse);

            log.debug("User registration completed successfully for: {}", request.getEmail());
            return authResponse;

        } catch (Exception e) {
            log.error("User registration failed for email: {}", request.getEmail(), e);
            throw new AuthenticationFailedException("Registration failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse login(UserLoginRequest request) {
        log.debug("Authenticating user with email: {}", request.getEmail());

        try {
            AuthResponse authResponse = keycloakService.authenticateUser(request.getEmail(), request.getPassword());

            // Get user details for response
            UserResponse userResponse = userService.getUserByEmail(request.getEmail());
            authResponse.setUser(userResponse);

            log.debug("User authentication successful for: {}", request.getEmail());
            return authResponse;

        } catch (Exception e) {
            log.error("User authentication failed for email: {}", request.getEmail(), e);
            throw new AuthenticationFailedException("Authentication failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        log.debug("Refreshing access token");

        try {
            return keycloakService.refreshAccessToken(refreshToken);
        } catch (Exception e) {
            log.error("Token refresh failed", e);
            throw new AuthenticationFailedException("Token refresh failed: " + e.getMessage());
        }
    }

    @Override
    public void logout(String refreshToken) {
        log.debug("Logging out user");

        try {
            keycloakService.logout(refreshToken);
            log.debug("User logout successful");
        } catch (Exception e) {
            log.error("User logout failed", e);
            throw new AuthenticationFailedException("Logout failed: " + e.getMessage());
        }
    }
}