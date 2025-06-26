package dev.dammak.keyclockuser.mapper;


import dev.dammak.keyclockuser.dto.request.UserUpdateRequest;
import dev.dammak.keyclockuser.dto.response.UserResponse;
import dev.dammak.keyclockuser.entity.User;
import org.mapstruct.*;

/**
 * MapStruct mapper for User entity and DTOs.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "profilePictureUrl", source = "profile.profilePictureUrl")
    UserResponse toUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "profile", ignore = true)
    void updateUserFromRequest(UserUpdateRequest request, @MappingTarget User user);
}