package com.trycore.quotizo.service.mapper;

import static com.trycore.quotizo.domain.RuleAssignmentAsserts.*;
import static com.trycore.quotizo.domain.RuleAssignmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RuleAssignmentMapperTest {

    private RuleAssignmentMapper ruleAssignmentMapper;

    @BeforeEach
    void setUp() {
        ruleAssignmentMapper = new RuleAssignmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRuleAssignmentSample1();
        var actual = ruleAssignmentMapper.toEntity(ruleAssignmentMapper.toDto(expected));
        assertRuleAssignmentAllPropertiesEquals(expected, actual);
    }
}
