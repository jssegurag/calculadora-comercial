package com.trycore.quotizo.service.mapper;

import static com.trycore.quotizo.domain.ResourceAllocationAsserts.*;
import static com.trycore.quotizo.domain.ResourceAllocationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResourceAllocationMapperTest {

    private ResourceAllocationMapper resourceAllocationMapper;

    @BeforeEach
    void setUp() {
        resourceAllocationMapper = new ResourceAllocationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getResourceAllocationSample1();
        var actual = resourceAllocationMapper.toEntity(resourceAllocationMapper.toDto(expected));
        assertResourceAllocationAllPropertiesEquals(expected, actual);
    }
}
