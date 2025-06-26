package dev.dammak.keyclockuser.service;


import dev.dammak.keyclockuser.dto.request.UserUpdateRequest;
import dev.dammak.keyclockuser.dto.response.UserResponse;
import dev.dammak.keyclockuser.entity.User;
import dev.dammak.keyclockuser.entity.UserProfile;
import dev.dammak.keyclockuser.exception.UserNotFoundException;
import dev.dammak.keyclockuser.mapper.UserMapper;
import dev.dammak.keyclockuser.repository.UserProfileRepository;
import dev.dammak.keyclockuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of UserService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        log.debug("Fetching all users with pagination: {}", pageable);
        return userRepository.findAllActiveUsers(pageable)
                .map(userMapper::toUserResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(String userId) {
        log.debug("Fetching user by ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        log.debug("Fetching user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        log.debug("Updating user: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        userMapper.updateUserFromRequest(request, user);

        if (user.getProfile() == null) {
            UserProfile profile = new UserProfile();
            profile.setUser(user);
            user.setProfile(profile);
        }

        if (request.getProfilePictureUrl() != null) {
            user.getProfile().setProfilePictureUrl(request.getProfilePictureUrl());
        }

        User savedUser = userRepository.save(user);
        log.debug("User updated successfully: {}", userId);
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public void deleteUser(String userId) {
        log.debug("Deleting user: {}", userId);
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        userProfileRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
        log.debug("User deleted successfully: {}", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUserProfile(Authentication authentication) {
        String userId = extractUserIdFromAuthentication(authentication);
        return getUserById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCurrentUser(String userId) {
        // This method would be called from security expressions
        // Implementation depends on how you want to check current user context
        return true; // Simplified implementation
    }

    @Override
    public void syncUserFromKeycloak(String userId, String email, String firstName, String lastName) {
        log.debug("Syncing user from Keycloak: {}", userId);

        User user = userRepository.findById(userId).orElse(new User());
        user.setId(userId);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);

        userRepository.save(user);
        log.debug("User synced from Keycloak: {}", userId);
    }

    private String extractUserIdFromAuthentication(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("sub");
        }
        return authentication.getName();
    }
}