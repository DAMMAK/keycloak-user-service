package dev.dammak.keyclockuser.service;

import dev.dammak.keyclockuser.dto.request.UserUpdateRequest;
import dev.dammak.keyclockuser.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

/**
 * Service interface for user management operations.
 */
public interface UserService {

    Page<UserResponse> getAllUsers(Pageable pageable);

    UserResponse getUserById(String userId);

    UserResponse getUserByEmail(String email);

    UserResponse updateUser(String userId, UserUpdateRequest request);

    void deleteUser(String userId);

    UserResponse getCurrentUserProfile(Authentication authentication);

    boolean isCurrentUser(String userId);

    void syncUserFromKeycloak(String userId, String email, String firstName, String lastName);
}