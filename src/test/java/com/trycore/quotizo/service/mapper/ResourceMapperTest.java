package com.trycore.quotizo.service.mapper;

import static com.trycore.quotizo.domain.ResourceAsserts.*;
import static com.trycore.quotizo.domain.ResourceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResourceMapperTest {

    private ResourceMapper resourceMapper;

    @BeforeEach
    void setUp() {
        resourceMapper = new ResourceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getResourceSample1();
        var actual = resourceMapper.toEntity(resourceMapper.toDto(expected));
        assertResourceAllPropertiesEquals(expected, actual);
    }
}