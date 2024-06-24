package com.trycore.quotizo.service.mapper;

import com.trycore.quotizo.domain.FinancialParameterType;
import com.trycore.quotizo.service.dto.FinancialParameterTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FinancialParameterType} and its DTO {@link FinancialParameterTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface FinancialParameterTypeMapper extends EntityMapper<FinancialParameterTypeDTO, FinancialParameterType> {}
