package com.trycore.quotizo.service.mapper;

import com.trycore.quotizo.domain.Permission;
import com.trycore.quotizo.domain.UserRole;
import com.trycore.quotizo.service.dto.PermissionDTO;
import com.trycore.quotizo.service.dto.UserRoleDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Permission} and its DTO {@link PermissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {
    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "userRoleIdSet")
    PermissionDTO toDto(Permission s);

    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "removePermissions", ignore = true)
    Permission toEntity(PermissionDTO permissionDTO);

    @Named("userRoleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserRoleDTO toDtoUserRoleId(UserRole userRole);

    @Named("userRoleIdSet")
    default Set<UserRoleDTO> toDtoUserRoleIdSet(Set<UserRole> userRole) {
        return userRole.stream().map(this::toDtoUserRoleId).collect(Collectors.toSet());
    }
}
