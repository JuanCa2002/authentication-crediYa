package co.com.pragma.api.user.mapper;

import co.com.pragma.api.mapper.PasswordMapper;
import co.com.pragma.api.user.dto.CreateUserDTO;
import co.com.pragma.api.user.dto.UserResponseDTO;
import co.com.pragma.api.mapper.DateMapper;
import co.com.pragma.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {DateMapper.class, PasswordMapper.class})
public interface UserMapper {

    @Mapping(source = "birthDate", target = "birthDate", qualifiedByName = "stringToLocalDate")
    @Mapping(source = "password", target = "password", qualifiedByName = "passwordEncode")
    User toDomain(CreateUserDTO request);

    @Mapping(source = "birthDate", target = "birthDate", qualifiedByName = "localDateToString")
    UserResponseDTO toResponse(User domain);
}
