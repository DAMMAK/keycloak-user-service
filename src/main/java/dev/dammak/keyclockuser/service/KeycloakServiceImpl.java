package dev.dammak.keyclockuser.service;

import dev.dammak.keyclockuser.config.KeycloakConfig;
import dev.dammak.keyclockuser.dto.request.UserRegistrationRequest;
import dev.dammak.keyclockuser.dto.response.AuthResponse;
import dev.dammak.keyclockuser.exception.AuthenticationFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

/**
 * Implementation of KeycloakService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {

    private final Keycloak keycloakAdminClient;
    private final KeycloakConfig keycloakConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String createUser(UserRegistrationRequest request) {
        log.debug("Creating user in Keycloak: {}", request.getEmail());

        try {
            RealmResource realmResource = keycloakAdminClient.realm(keycloakConfig.getKeycloakRealm());
            UsersResource usersResource = realmResource.users();

            UserRepresentation user = new UserRepresentation();
            user.setUsername(request.getEmail());
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEnabled(true);
            user.setEmailVerified(false);

            // Set password
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(request.getPassword());
            credential.setTemporary(false);
            user.setCredentials(Collections.singletonList(credential));

            var response = usersResource.create(user);

            if (response.getStatus() == 201) {
                String locationHeader = response.getHeaderString("Location");
                String userId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
                log.debug("User created successfully in Keycloak with ID: {}", userId);
                return userId;
            } else {
                throw new AuthenticationFailedException("Failed to create user in Keycloak");
            }

        } catch (Exception e) {
            log.error("Error creating user in Keycloak", e);
            throw new AuthenticationFailedException("Failed to create user: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse authenticateUser(String email, String password) {
        log.debug("Authenticating user in Keycloak: {}", email);

        try {
            String tokenUrl = keycloakConfig.getKeycloakServerUrl() + "/realms/" + keycloakConfig.getKeycloakRealm() + "/protocol/openid-connect/token";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "password");
            body.add("client_id", keycloakConfig.getClientId());
            body.add("client_secret", keycloakConfig.getClientSecret());
            body.add("username", email);
            body.add("password", password);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> tokenResponse = response.getBody();

                AuthResponse authResponse = new AuthResponse();
                authResponse.setAccessToken((String) tokenResponse.get("access_token"));
                authResponse.setRefreshToken((String) tokenResponse.get("refresh_token"));
                authResponse.setTokenType("Bearer");
                authResponse.setExpiresIn(((Integer) tokenResponse.get("expires_in")).longValue());

                log.debug("User authentication successful in Keycloak: {}", email);
                return authResponse;
            } else {
                throw new AuthenticationFailedException("Authentication failed");
            }

        } catch (Exception e) {
            log.error("Error authenticating user in Keycloak", e);
            throw new AuthenticationFailedException("Authentication failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse refreshAccessToken(String refreshToken) {
        log.debug("Refreshing access token in Keycloak");

        try {
            String tokenUrl = keycloakConfig.getKeycloakServerUrl() + "/realms/" + keycloakConfig.getKeycloakRealm() + "/protocol/openid-connect/token";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "refresh_token");
            body.add("client_id", keycloakConfig.getClientId());
            body.add("client_secret", keycloakConfig.getClientSecret());
            body.add("refresh_token", refreshToken);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> tokenResponse = response.getBody();

                AuthResponse authResponse = new AuthResponse();
                authResponse.setAccessToken((String) tokenResponse.get("access_token"));
                authResponse.setRefreshToken((String) tokenResponse.get("refresh_token"));
                authResponse.setTokenType("Bearer");
                authResponse.setExpiresIn(((Integer) tokenResponse.get("expires_in")).longValue());

                log.debug("Access token refreshed successfully");
                return authResponse;
            } else {
                throw new AuthenticationFailedException("Token refresh failed");
            }

        } catch (Exception e) {
            log.error("Error refreshing access token", e);
            throw new AuthenticationFailedException("Token refresh failed: " + e.getMessage());
        }
    }

    @Override
    public void logout(String refreshToken) {
        log.debug("Logging out user in Keycloak");

        try {
            String logoutUrl = keycloakConfig.getKeycloakServerUrl() + "/realms/" + keycloakConfig.getKeycloakRealm() + "/protocol/openid-connect/logout";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("client_id", keycloakConfig.getClientId());
            body.add("client_secret", keycloakConfig.getClientSecret());
            body.add("refresh_token", refreshToken);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            restTemplate.exchange(logoutUrl, HttpMethod.POST, request, Void.class);
            log.debug("User logout successful in Keycloak");

        } catch (Exception e) {
            log.error("Error logging out user in Keycloak", e);
            throw new AuthenticationFailedException("Logout failed: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(String userId) {
        log.debug("Deleting user in Keycloak: {}", userId);

        try {
            RealmResource realmResource = keycloakAdminClient.realm(keycloakConfig.getKeycloakRealm());
            UsersResource usersResource = realmResource.users();

            var response = usersResource.delete(userId);
            if (response.getStatus() == 204) {
                log.debug("User deleted successfully in Keycloak: {}", userId);
            } else {
                throw new AuthenticationFailedException("Failed to delete user in Keycloak");
            }

        } catch (Exception e) {
            log.error("Error deleting user in Keycloak", e);
            throw new AuthenticationFailedException("Failed to delete user: " + e.getMessage());
        }
    }
}