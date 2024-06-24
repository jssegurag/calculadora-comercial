package com.trycore.quotizo.domain;

import static com.trycore.quotizo.domain.BudgetTestSamples.*;
import static com.trycore.quotizo.domain.FinancialParameterTestSamples.*;
import static com.trycore.quotizo.domain.UserRoleTestSamples.*;
import static com.trycore.quotizo.domain.UsersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UsersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Users.class);
        Users users1 = getUsersSample1();
        Users users2 = new Users();
        assertThat(users1).isNotEqualTo(users2);

        users2.setId(users1.getId());
        assertThat(users1).isEqualTo(users2);

        users2 = getUsersSample2();
        assertThat(users1).isNotEqualTo(users2);
    }

    @Test
    void budgetTest() {
        Users users = getUsersRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        users.addBudget(budgetBack);
        assertThat(users.getBudgets()).containsOnly(budgetBack);
        assertThat(budgetBack.getUserOwner()).isEqualTo(users);

        users.removeBudget(budgetBack);
        assertThat(users.getBudgets()).doesNotContain(budgetBack);
        assertThat(budgetBack.getUserOwner()).isNull();

        users.budgets(new HashSet<>(Set.of(budgetBack)));
        assertThat(users.getBudgets()).containsOnly(budgetBack);
        assertThat(budgetBack.getUserOwner()).isEqualTo(users);

        users.setBudgets(new HashSet<>());
        assertThat(users.getBudgets()).doesNotContain(budgetBack);
        assertThat(budgetBack.getUserOwner()).isNull();
    }

    @Test
    void financialParameterTest() {
        Users users = getUsersRandomSampleGenerator();
        FinancialParameter financialParameterBack = getFinancialParameterRandomSampleGenerator();

        users.addFinancialParameter(financialParameterBack);
        assertThat(users.getFinancialParameters()).containsOnly(financialParameterBack);
        assertThat(financialParameterBack.getAdministrator()).isEqualTo(users);

        users.removeFinancialParameter(financialParameterBack);
        assertThat(users.getFinancialParameters()).doesNotContain(financialParameterBack);
        assertThat(financialParameterBack.getAdministrator()).isNull();

        users.financialParameters(new HashSet<>(Set.of(financialParameterBack)));
        assertThat(users.getFinancialParameters()).containsOnly(financialParameterBack);
        assertThat(financialParameterBack.getAdministrator()).isEqualTo(users);

        users.setFinancialParameters(new HashSet<>());
        assertThat(users.getFinancialParameters()).doesNotContain(financialParameterBack);
        assertThat(financialParameterBack.getAdministrator()).isNull();
    }

    @Test
    void userRoleTest() {
        Users users = getUsersRandomSampleGenerator();
        UserRole userRoleBack = getUserRoleRandomSampleGenerator();

        users.addUserRole(userRoleBack);
        assertThat(users.getUserRoles()).containsOnly(userRoleBack);

        users.removeUserRole(userRoleBack);
        assertThat(users.getUserRoles()).doesNotContain(userRoleBack);

        users.userRoles(new HashSet<>(Set.of(userRoleBack)));
        assertThat(users.getUserRoles()).containsOnly(userRoleBack);

        users.setUserRoles(new HashSet<>());
        assertThat(users.getUserRoles()).doesNotContain(userRoleBack);
    }

    @Test
    void budgetAuthorizedTest() {
        Users users = getUsersRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        users.addBudgetAuthorized(budgetBack);
        assertThat(users.getBudgetAuthorizeds()).containsOnly(budgetBack);

        users.removeBudgetAuthorized(budgetBack);
        assertThat(users.getBudgetAuthorizeds()).doesNotContain(budgetBack);

        users.budgetAuthorizeds(new HashSet<>(Set.of(budgetBack)));
        assertThat(users.getBudgetAuthorizeds()).containsOnly(budgetBack);

        users.setBudgetAuthorizeds(new HashSet<>());
        assertThat(users.getBudgetAuthorizeds()).doesNotContain(budgetBack);
    }

    @Test
    void assignedToTest() {
        Users users = getUsersRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        users.addAssignedTo(budgetBack);
        assertThat(users.getAssignedTos()).containsOnly(budgetBack);
        assertThat(budgetBack.getUserAssignedTo()).isEqualTo(users);

        users.removeAssignedTo(budgetBack);
        assertThat(users.getAssignedTos()).doesNotContain(budgetBack);
        assertThat(budgetBack.getUserAssignedTo()).isNull();

        users.assignedTos(new HashSet<>(Set.of(budgetBack)));
        assertThat(users.getAssignedTos()).containsOnly(budgetBack);
        assertThat(budgetBack.getUserAssignedTo()).isEqualTo(users);

        users.setAssignedTos(new HashSet<>());
        assertThat(users.getAssignedTos()).doesNotContain(budgetBack);
        assertThat(budgetBack.getUserAssignedTo()).isNull();
    }

    @Test
    void approvedByTest() {
        Users users = getUsersRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        users.addApprovedBy(budgetBack);
        assertThat(users.getApprovedBies()).containsOnly(budgetBack);
        assertThat(budgetBack.getUserApprovedBy()).isEqualTo(users);

        users.removeApprovedBy(budgetBack);
        assertThat(users.getApprovedBies()).doesNotContain(budgetBack);
        assertThat(budgetBack.getUserApprovedBy()).isNull();

        users.approvedBies(new HashSet<>(Set.of(budgetBack)));
        assertThat(users.getApprovedBies()).containsOnly(budgetBack);
        assertThat(budgetBack.getUserApprovedBy()).isEqualTo(users);

        users.setApprovedBies(new HashSet<>());
        assertThat(users.getApprovedBies()).doesNotContain(budgetBack);
        assertThat(budgetBack.getUserApprovedBy()).isNull();
    }
}
