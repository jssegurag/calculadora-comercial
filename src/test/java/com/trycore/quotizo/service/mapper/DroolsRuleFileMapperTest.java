package com.trycore.quotizo.service.mapper;

import static com.trycore.quotizo.domain.DroolsRuleFileAsserts.*;
import static com.trycore.quotizo.domain.DroolsRuleFileTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DroolsRuleFileMapperTest {

    private DroolsRuleFileMapper droolsRuleFileMapper;

    @BeforeEach
    void setUp() {
        droolsRuleFileMapper = new DroolsRuleFileMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDroolsRuleFileSample1();
        var actual = droolsRuleFileMapper.toEntity(droolsRuleFileMapper.toDto(expected));
        assertDroolsRuleFileAllPropertiesEquals(expected, actual);
    }
}
