package com.backend.backend.mapper;

import com.backend.backend.dto.UserProfileDTO;
import com.backend.backend.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    UserProfileDTO toDto(UserProfile userProfile);
}
