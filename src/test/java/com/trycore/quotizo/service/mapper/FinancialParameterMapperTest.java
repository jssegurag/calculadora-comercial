package com.trycore.quotizo.service.mapper;

import static com.trycore.quotizo.domain.FinancialParameterAsserts.*;
import static com.trycore.quotizo.domain.FinancialParameterTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FinancialParameterMapperTest {

    private FinancialParameterMapper financialParameterMapper;

    @BeforeEach
    void setUp() {
        financialParameterMapper = new FinancialParameterMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFinancialParameterSample1();
        var actual = financialParameterMapper.toEntity(financialParameterMapper.toDto(expected));
        assertFinancialParameterAllPropertiesEquals(expected, actual);
    }
}
