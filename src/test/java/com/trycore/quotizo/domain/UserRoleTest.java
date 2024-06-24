package com.trycore.quotizo.domain;

import static com.trycore.quotizo.domain.BudgetTestSamples.*;
import static com.trycore.quotizo.domain.FinancialParameterTestSamples.*;
import static com.trycore.quotizo.domain.PermissionTestSamples.*;
import static com.trycore.quotizo.domain.UserRoleTestSamples.*;
import static com.trycore.quotizo.domain.UsersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRole.class);
        UserRole userRole1 = getUserRoleSample1();
        UserRole userRole2 = new UserRole();
        assertThat(userRole1).isNotEqualTo(userRole2);

        userRole2.setId(userRole1.getId());
        assertThat(userRole1).isEqualTo(userRole2);

        userRole2 = getUserRoleSample2();
        assertThat(userRole1).isNotEqualTo(userRole2);
    }

    @Test
    void permissionTest() {
        UserRole userRole = getUserRoleRandomSampleGenerator();
        Permission permissionBack = getPermissionRandomSampleGenerator();

        userRole.addPermission(permissionBack);
        assertThat(userRole.getPermissions()).containsOnly(permissionBack);

        userRole.removePermission(permissionBack);
        assertThat(userRole.getPermissions()).doesNotContain(permissionBack);

        userRole.permissions(new HashSet<>(Set.of(permissionBack)));
        assertThat(userRole.getPermissions()).containsOnly(permissionBack);

        userRole.setPermissions(new HashSet<>());
        assertThat(userRole.getPermissions()).doesNotContain(permissionBack);
    }

    @Test
    void budgetTest() {
        UserRole userRole = getUserRoleRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        userRole.addBudget(budgetBack);
        assertThat(userRole.getBudgets()).containsOnly(budgetBack);

        userRole.removeBudget(budgetBack);
        assertThat(userRole.getBudgets()).doesNotContain(budgetBack);

        userRole.budgets(new HashSet<>(Set.of(budgetBack)));
        assertThat(userRole.getBudgets()).containsOnly(budgetBack);

        userRole.setBudgets(new HashSet<>());
        assertThat(userRole.getBudgets()).doesNotContain(budgetBack);
    }

    @Test
    void financialParameterTest() {
        UserRole userRole = getUserRoleRandomSampleGenerator();
        FinancialParameter financialParameterBack = getFinancialParameterRandomSampleGenerator();

        userRole.addFinancialParameter(financialParameterBack);
        assertThat(userRole.getFinancialParameters()).containsOnly(financialParameterBack);

        userRole.removeFinancialParameter(financialParameterBack);
        assertThat(userRole.getFinancialParameters()).doesNotContain(financialParameterBack);

        userRole.financialParameters(new HashSet<>(Set.of(financialParameterBack)));
        assertThat(userRole.getFinancialParameters()).containsOnly(financialParameterBack);

        userRole.setFinancialParameters(new HashSet<>());
        assertThat(userRole.getFinancialParameters()).doesNotContain(financialParameterBack);
    }

    @Test
    void usersTest() {
        UserRole userRole = getUserRoleRandomSampleGenerator();
        Users usersBack = getUsersRandomSampleGenerator();

        userRole.addUsers(usersBack);
        assertThat(userRole.getUsers()).containsOnly(usersBack);
        assertThat(usersBack.getUserRoles()).containsOnly(userRole);

        userRole.removeUsers(usersBack);
        assertThat(userRole.getUsers()).doesNotContain(usersBack);
        assertThat(usersBack.getUserRoles()).doesNotContain(userRole);

        userRole.users(new HashSet<>(Set.of(usersBack)));
        assertThat(userRole.getUsers()).containsOnly(usersBack);
        assertThat(usersBack.getUserRoles()).containsOnly(userRole);

        userRole.setUsers(new HashSet<>());
        assertThat(userRole.getUsers()).doesNotContain(usersBack);
        assertThat(usersBack.getUserRoles()).doesNotContain(userRole);
    }
}
