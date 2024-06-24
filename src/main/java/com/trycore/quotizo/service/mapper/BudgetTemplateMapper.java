package com.trycore.quotizo.service.mapper;

import com.trycore.quotizo.domain.BudgetTemplate;
import com.trycore.quotizo.domain.Country;
import com.trycore.quotizo.service.dto.BudgetTemplateDTO;
import com.trycore.quotizo.service.dto.CountryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BudgetTemplate} and its DTO {@link BudgetTemplateDTO}.
 */
@Mapper(componentModel = "spring")
public interface BudgetTemplateMapper extends EntityMapper<BudgetTemplateDTO, BudgetTemplate> {
    @Mapping(target = "country", source = "country", qualifiedByName = "countryId")
    BudgetTemplateDTO toDto(BudgetTemplate s);

    @Named("countryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CountryDTO toDtoCountryId(Country country);
}
