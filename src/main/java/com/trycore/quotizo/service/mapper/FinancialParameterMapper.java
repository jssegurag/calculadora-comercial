package com.trycore.quotizo.service.mapper;

import com.trycore.quotizo.domain.Country;
import com.trycore.quotizo.domain.FinancialParameter;
import com.trycore.quotizo.domain.FinancialParameterType;
import com.trycore.quotizo.domain.UserRole;
import com.trycore.quotizo.domain.Users;
import com.trycore.quotizo.service.dto.CountryDTO;
import com.trycore.quotizo.service.dto.FinancialParameterDTO;
import com.trycore.quotizo.service.dto.FinancialParameterTypeDTO;
import com.trycore.quotizo.service.dto.UserRoleDTO;
import com.trycore.quotizo.service.dto.UsersDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FinancialParameter} and its DTO {@link FinancialParameterDTO}.
 */
@Mapper(componentModel = "spring")
public interface FinancialParameterMapper extends EntityMapper<FinancialParameterDTO, FinancialParameter> {
    @Mapping(target = "financialParameterType", source = "financialParameterType", qualifiedByName = "financialParameterTypeId")
    @Mapping(target = "country", source = "country", qualifiedByName = "countryId")
    @Mapping(target = "administrator", source = "administrator", qualifiedByName = "usersId")
    @Mapping(target = "roleAuthorizeds", source = "roleAuthorizeds", qualifiedByName = "userRoleIdSet")
    FinancialParameterDTO toDto(FinancialParameter s);

    @Mapping(target = "roleAuthorizeds", ignore = true)
    @Mapping(target = "removeRoleAuthorized", ignore = true)
    FinancialParameter toEntity(FinancialParameterDTO financialParameterDTO);

    @Named("financialParameterTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FinancialParameterTypeDTO toDtoFinancialParameterTypeId(FinancialParameterType financialParameterType);

    @Named("countryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CountryDTO toDtoCountryId(Country country);

    @Named("usersId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsersDTO toDtoUsersId(Users users);

    @Named("userRoleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserRoleDTO toDtoUserRoleId(UserRole userRole);

    @Named("userRoleIdSet")
    default Set<UserRoleDTO> toDtoUserRoleIdSet(Set<UserRole> userRole) {
        return userRole.stream().map(this::toDtoUserRoleId).collect(Collectors.toSet());
    }
}
