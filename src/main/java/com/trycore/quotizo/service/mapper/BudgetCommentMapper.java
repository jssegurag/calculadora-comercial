package com.trycore.quotizo.service.mapper;

import com.trycore.quotizo.domain.Budget;
import com.trycore.quotizo.domain.BudgetComment;
import com.trycore.quotizo.service.dto.BudgetCommentDTO;
import com.trycore.quotizo.service.dto.BudgetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BudgetComment} and its DTO {@link BudgetCommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface BudgetCommentMapper extends EntityMapper<BudgetCommentDTO, BudgetComment> {
    @Mapping(target = "budget", source = "budget", qualifiedByName = "budgetId")
    BudgetCommentDTO toDto(BudgetComment s);

    @Named("budgetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BudgetDTO toDtoBudgetId(Budget budget);
}
