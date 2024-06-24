package com.trycore.quotizo.service.mapper;

import com.trycore.quotizo.domain.Budget;
import com.trycore.quotizo.domain.UserRole;
import com.trycore.quotizo.domain.Users;
import com.trycore.quotizo.service.dto.BudgetDTO;
import com.trycore.quotizo.service.dto.UserRoleDTO;
import com.trycore.quotizo.service.dto.UsersDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Users} and its DTO {@link UsersDTO}.
 */
@Mapper(componentModel = "spring")
public interface UsersMapper extends EntityMapper<UsersDTO, Users> {
    @Mapping(target = "userRoles", source = "userRoles", qualifiedByName = "userRoleIdSet")
    @Mapping(target = "budgetAuthorizeds", source = "budgetAuthorizeds", qualifiedByName = "budgetIdSet")
    UsersDTO toDto(Users s);

    @Mapping(target = "removeUserRole", ignore = true)
    @Mapping(target = "removeBudgetAuthorized", ignore = true)
    Users toEntity(UsersDTO usersDTO);

    @Named("budgetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BudgetDTO toDtoBudgetId(Budget budget);

    @Named("budgetIdSet")
    default Set<BudgetDTO> toDtoBudgetIdSet(Set<Budget> budget) {
        return budget.stream().map(this::toDtoBudgetId).collect(Collectors.toSet());
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
