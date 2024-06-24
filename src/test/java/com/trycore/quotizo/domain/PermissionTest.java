package com.trycore.quotizo.domain;

import static com.trycore.quotizo.domain.PermissionTestSamples.*;
import static com.trycore.quotizo.domain.UserRoleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PermissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Permission.class);
        Permission permission1 = getPermissionSample1();
        Permission permission2 = new Permission();
        assertThat(permission1).isNotEqualTo(permission2);

        permission2.setId(permission1.getId());
        assertThat(permission1).isEqualTo(permission2);

        permission2 = getPermissionSample2();
        assertThat(permission1).isNotEqualTo(permission2);
    }

    @Test
    void permissionsTest() {
        Permission permission = getPermissionRandomSampleGenerator();
        UserRole userRoleBack = getUserRoleRandomSampleGenerator();

        permission.addPermissions(userRoleBack);
        assertThat(permission.getPermissions()).containsOnly(userRoleBack);
        assertThat(userRoleBack.getPermissions()).containsOnly(permission);

        permission.removePermissions(userRoleBack);
        assertThat(permission.getPermissions()).doesNotContain(userRoleBack);
        assertThat(userRoleBack.getPermissions()).doesNotContain(permission);

        permission.permissions(new HashSet<>(Set.of(userRoleBack)));
        assertThat(permission.getPermissions()).containsOnly(userRoleBack);
        assertThat(userRoleBack.getPermissions()).containsOnly(permission);

        permission.setPermissions(new HashSet<>());
        assertThat(permission.getPermissions()).doesNotContain(userRoleBack);
        assertThat(userRoleBack.getPermissions()).doesNotContain(permission);
    }
}
