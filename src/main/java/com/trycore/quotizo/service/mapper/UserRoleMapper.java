package com.trycore.quotizo.service.mapper;

import com.trycore.quotizo.domain.Budget;
import com.trycore.quotizo.domain.FinancialParameter;
import com.trycore.quotizo.domain.Permission;
import com.trycore.quotizo.domain.UserRole;
import com.trycore.quotizo.domain.Users;
import com.trycore.quotizo.service.dto.BudgetDTO;
import com.trycore.quotizo.service.dto.FinancialParameterDTO;
import com.trycore.quotizo.service.dto.PermissionDTO;
import com.trycore.quotizo.service.dto.UserRoleDTO;
import com.trycore.quotizo.service.dto.UsersDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserRole} and its DTO {@link UserRoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserRoleMapper extends EntityMapper<UserRoleDTO, UserRole> {
    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "permissionIdSet")
    @Mapping(target = "budgets", source = "budgets", qualifiedByName = "budgetIdSet")
    @Mapping(target = "financialParameters", source = "financialParameters", qualifiedByName = "financialParameterIdSet")
    @Mapping(target = "users", source = "users", qualifiedByName = "usersIdSet")
    UserRoleDTO toDto(UserRole s);

    @Mapping(target = "removePermission", ignore = true)
    @Mapping(target = "removeBudget", ignore = true)
    @Mapping(target = "removeFinancialParameter", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "removeUsers", ignore = true)
    UserRole toEntity(UserRoleDTO userRoleDTO);

    @Named("permissionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PermissionDTO toDtoPermissionId(Permission permission);

    @Named("permissionIdSet")
    default Set<PermissionDTO> toDtoPermissionIdSet(Set<Permission> permission) {
        return permission.stream().map(this::toDtoPermissionId).collect(Collectors.toSet());
    }

    @Named("budgetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BudgetDTO toDtoBudgetId(Budget budget);

    @Named("budgetIdSet")
    default Set<BudgetDTO> toDtoBudgetIdSet(Set<Budget> budget) {
        return budget.stream().map(this::toDtoBudgetId).collect(Collectors.toSet());
    }

    @Named("financialParameterId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FinancialParameterDTO toDtoFinancialParameterId(FinancialParameter financialParameter);

    @Named("financialParameterIdSet")
    default Set<FinancialParameterDTO> toDtoFinancialParameterIdSet(Set<FinancialParameter> financialParameter) {
        return financialParameter.stream().map(this::toDtoFinancialParameterId).collect(Collectors.toSet());
    }

    @Named("usersId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsersDTO toDtoUsersId(Users users);

    @Named("usersIdSet")
    default Set<UsersDTO> toDtoUsersIdSet(Set<Users> users) {
        return users.stream().map(this::toDtoUsersId).collect(Collectors.toSet());
    }
}
