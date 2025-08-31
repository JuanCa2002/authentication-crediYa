package co.com.pragma.api.role.mapper;

import co.com.pragma.api.mapper.DateMapper;
import co.com.pragma.api.role.dto.CreateRoleDTO;
import co.com.pragma.api.role.dto.RoleResponseDTO;
import co.com.pragma.model.role.Role;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {DateMapper.class})
public interface RoleMapper {

    Role toDomain(CreateRoleDTO request);

    RoleResponseDTO toResponse(Role domain);
}
