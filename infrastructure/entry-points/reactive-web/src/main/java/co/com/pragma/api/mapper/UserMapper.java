package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.CreateUserDTO;
import co.com.pragma.api.dto.UserResponseDTO;
import co.com.pragma.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {DateMapper.class})
public interface UserMapper {

    @Mapping(source = "birthDate", target = "birthDate", qualifiedByName = "stringToLocalDate")
    User toDomain(CreateUserDTO request);

    @Mapping(source = "birthDate", target = "birthDate", qualifiedByName = "localDateToString")
    UserResponseDTO toResponse(User domain);
}
