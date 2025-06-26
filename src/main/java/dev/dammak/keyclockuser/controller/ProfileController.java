package dev.dammak.keyclockuser.controller;


import dev.dammak.keyclockuser.dto.response.ApiResponse;
import dev.dammak.keyclockuser.dto.response.UserResponse;
import dev.dammak.keyclockuser.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for user profile operations.
 */
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Profile", description = "User profile operations")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get current user profile", description = "Retrieve current authenticated user's profile")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUserProfile(Authentication authentication) {
        log.debug("Fetching profile for user: {}", authentication.getName());
        UserResponse user = userService.getCurrentUserProfile(authentication);
        return ResponseEntity.ok(ApiResponse.success(user, "Profile retrieved successfully"));
    }
}