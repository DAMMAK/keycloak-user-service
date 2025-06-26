package dev.dammak.keyclockuser.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Response DTO for user information.
 */
@Data
public class UserResponse {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePictureUrl;
    private boolean enabled;
    private boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}