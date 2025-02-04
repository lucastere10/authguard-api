package br.com.lucascaldas.authguard.api.mapper;

import br.com.lucascaldas.authguard.api.dto.UserResponseDTO;
import br.com.lucascaldas.authguard.domain.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(UserResponseDTO userDto);
    UserResponseDTO toDto(User user);
}
