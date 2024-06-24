package com.trycore.quotizo.service.mapper;

import static com.trycore.quotizo.domain.FinancialParameterTypeAsserts.*;
import static com.trycore.quotizo.domain.FinancialParameterTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FinancialParameterTypeMapperTest {

    private FinancialParameterTypeMapper financialParameterTypeMapper;

    @BeforeEach
    void setUp() {
        financialParameterTypeMapper = new FinancialParameterTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFinancialParameterTypeSample1();
        var actual = financialParameterTypeMapper.toEntity(financialParameterTypeMapper.toDto(expected));
        assertFinancialParameterTypeAllPropertiesEquals(expected, actual);
    }
}
