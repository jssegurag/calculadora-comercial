package com.trycore.quotizo.service.mapper;

import com.trycore.quotizo.domain.Budget;
import com.trycore.quotizo.domain.Country;
import com.trycore.quotizo.domain.UserRole;
import com.trycore.quotizo.domain.Users;
import com.trycore.quotizo.service.dto.BudgetDTO;
import com.trycore.quotizo.service.dto.CountryDTO;
import com.trycore.quotizo.service.dto.UserRoleDTO;
import com.trycore.quotizo.service.dto.UsersDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Budget} and its DTO {@link BudgetDTO}.
 */
@Mapper(componentModel = "spring")
public interface BudgetMapper extends EntityMapper<BudgetDTO, Budget> {
    @Mapping(target = "contry", source = "contry", qualifiedByName = "countryId")
    @Mapping(target = "userAssignedTo", source = "userAssignedTo", qualifiedByName = "usersId")
    @Mapping(target = "userApprovedBy", source = "userApprovedBy", qualifiedByName = "usersId")
    @Mapping(target = "userOwner", source = "userOwner", qualifiedByName = "usersId")
    @Mapping(target = "authorizeds", source = "authorizeds", qualifiedByName = "usersIdSet")
    @Mapping(target = "roleAuthorizeds", source = "roleAuthorizeds", qualifiedByName = "userRoleIdSet")
    BudgetDTO toDto(Budget s);

    @Mapping(target = "authorizeds", ignore = true)
    @Mapping(target = "removeAuthorized", ignore = true)
    @Mapping(target = "roleAuthorizeds", ignore = true)
    @Mapping(target = "removeRoleAuthorized", ignore = true)
    Budget toEntity(BudgetDTO budgetDTO);

    @Named("countryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CountryDTO toDtoCountryId(Country country);

    @Named("usersId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsersDTO toDtoUsersId(Users users);

    @Named("usersIdSet")
    default Set<UsersDTO> toDtoUsersIdSet(Set<Users> users) {
        return users.stream().map(this::toDtoUsersId).collect(Collectors.toSet());
    }

    @Named("userRoleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserRoleDTO toDtoUserRoleId(UserRole userRole);

    @Named("userRoleIdSet")
    default Set<UserRoleDTO> toDtoUserRoleIdSet(Set<UserRole> userRole) {
        return userRole.stream().map(this::toDtoUserRoleId).collect(Collectors.toSet());
    }
}
