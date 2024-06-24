package com.trycore.quotizo.service.mapper;

import static com.trycore.quotizo.domain.PermissionAsserts.*;
import static com.trycore.quotizo.domain.PermissionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PermissionMapperTest {

    private PermissionMapper permissionMapper;

    @BeforeEach
    void setUp() {
        permissionMapper = new PermissionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPermissionSample1();
        var actual = permissionMapper.toEntity(permissionMapper.toDto(expected));
        assertPermissionAllPropertiesEquals(expected, actual);
    }
}
