package com.trycore.quotizo.service.mapper;

import static com.trycore.quotizo.domain.BudgetTemplateAsserts.*;
import static com.trycore.quotizo.domain.BudgetTemplateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BudgetTemplateMapperTest {

    private BudgetTemplateMapper budgetTemplateMapper;

    @BeforeEach
    void setUp() {
        budgetTemplateMapper = new BudgetTemplateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBudgetTemplateSample1();
        var actual = budgetTemplateMapper.toEntity(budgetTemplateMapper.toDto(expected));
        assertBudgetTemplateAllPropertiesEquals(expected, actual);
    }
}
