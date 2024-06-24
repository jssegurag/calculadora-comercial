package com.trycore.quotizo.service.mapper;

import static com.trycore.quotizo.domain.UsersAsserts.*;
import static com.trycore.quotizo.domain.UsersTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UsersMapperTest {

    private UsersMapper usersMapper;

    @BeforeEach
    void setUp() {
        usersMapper = new UsersMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUsersSample1();
        var actual = usersMapper.toEntity(usersMapper.toDto(expected));
        assertUsersAllPropertiesEquals(expected, actual);
    }
}
