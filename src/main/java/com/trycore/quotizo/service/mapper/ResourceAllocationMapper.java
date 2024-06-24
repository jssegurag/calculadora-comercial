package com.trycore.quotizo.service.mapper;

import com.trycore.quotizo.domain.Budget;
import com.trycore.quotizo.domain.BudgetTemplate;
import com.trycore.quotizo.domain.Resource;
import com.trycore.quotizo.domain.ResourceAllocation;
import com.trycore.quotizo.service.dto.BudgetDTO;
import com.trycore.quotizo.service.dto.BudgetTemplateDTO;
import com.trycore.quotizo.service.dto.ResourceAllocationDTO;
import com.trycore.quotizo.service.dto.ResourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResourceAllocation} and its DTO {@link ResourceAllocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResourceAllocationMapper extends EntityMapper<ResourceAllocationDTO, ResourceAllocation> {
    @Mapping(target = "budget", source = "budget", qualifiedByName = "budgetId")
    @Mapping(target = "resource", source = "resource", qualifiedByName = "resourceId")
    @Mapping(target = "budgetTemplate", source = "budgetTemplate", qualifiedByName = "budgetTemplateId")
    ResourceAllocationDTO toDto(ResourceAllocation s);

    @Named("budgetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BudgetDTO toDtoBudgetId(Budget budget);

    @Named("resourceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ResourceDTO toDtoResourceId(Resource resource);

    @Named("budgetTemplateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BudgetTemplateDTO toDtoBudgetTemplateId(BudgetTemplate budgetTemplate);
}
