package com.trycore.quotizo.service.mapper;

import static com.trycore.quotizo.domain.BudgetCommentAsserts.*;
import static com.trycore.quotizo.domain.BudgetCommentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BudgetCommentMapperTest {

    private BudgetCommentMapper budgetCommentMapper;

    @BeforeEach
    void setUp() {
        budgetCommentMapper = new BudgetCommentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBudgetCommentSample1();
        var actual = budgetCommentMapper.toEntity(budgetCommentMapper.toDto(expected));
        assertBudgetCommentAllPropertiesEquals(expected, actual);
    }
}
