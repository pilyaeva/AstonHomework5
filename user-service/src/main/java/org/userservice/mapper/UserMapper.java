package org.userservice.mapper;

import org.userservice.model.UserDtoIn;
import org.userservice.model.UserDtoOut;
import org.userservice.entity.UserEntity;

public class UserMapper {
    public static UserDtoOut toDto(UserEntity entity) {
        if (entity == null) return null;
        return new UserDtoOut(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getAge(),
            entity.getCreatedAt()
        );
    }

    public static UserEntity toEntity(UserDtoIn dto) {
        if (dto == null) return null;
        UserEntity entity = new UserEntity();
        entity.setName(dto.name());
        entity.setEmail(dto.email());
        entity.setAge(dto.age());
        return entity;
    }
}
