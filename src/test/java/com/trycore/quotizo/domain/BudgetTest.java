package com.trycore.quotizo.domain;

import static com.trycore.quotizo.domain.BudgetCommentTestSamples.*;
import static com.trycore.quotizo.domain.BudgetTestSamples.*;
import static com.trycore.quotizo.domain.CountryTestSamples.*;
import static com.trycore.quotizo.domain.ResourceAllocationTestSamples.*;
import static com.trycore.quotizo.domain.UserRoleTestSamples.*;
import static com.trycore.quotizo.domain.UsersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BudgetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Budget.class);
        Budget budget1 = getBudgetSample1();
        Budget budget2 = new Budget();
        assertThat(budget1).isNotEqualTo(budget2);

        budget2.setId(budget1.getId());
        assertThat(budget1).isEqualTo(budget2);

        budget2 = getBudgetSample2();
        assertThat(budget1).isNotEqualTo(budget2);
    }

    @Test
    void resourceAllocationTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        ResourceAllocation resourceAllocationBack = getResourceAllocationRandomSampleGenerator();

        budget.addResourceAllocation(resourceAllocationBack);
        assertThat(budget.getResourceAllocations()).containsOnly(resourceAllocationBack);
        assertThat(resourceAllocationBack.getBudget()).isEqualTo(budget);

        budget.removeResourceAllocation(resourceAllocationBack);
        assertThat(budget.getResourceAllocations()).doesNotContain(resourceAllocationBack);
        assertThat(resourceAllocationBack.getBudget()).isNull();

        budget.resourceAllocations(new HashSet<>(Set.of(resourceAllocationBack)));
        assertThat(budget.getResourceAllocations()).containsOnly(resourceAllocationBack);
        assertThat(resourceAllocationBack.getBudget()).isEqualTo(budget);

        budget.setResourceAllocations(new HashSet<>());
        assertThat(budget.getResourceAllocations()).doesNotContain(resourceAllocationBack);
        assertThat(resourceAllocationBack.getBudget()).isNull();
    }

    @Test
    void budgetCommentTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        BudgetComment budgetCommentBack = getBudgetCommentRandomSampleGenerator();

        budget.addBudgetComment(budgetCommentBack);
        assertThat(budget.getBudgetComments()).containsOnly(budgetCommentBack);
        assertThat(budgetCommentBack.getBudget()).isEqualTo(budget);

        budget.removeBudgetComment(budgetCommentBack);
        assertThat(budget.getBudgetComments()).doesNotContain(budgetCommentBack);
        assertThat(budgetCommentBack.getBudget()).isNull();

        budget.budgetComments(new HashSet<>(Set.of(budgetCommentBack)));
        assertThat(budget.getBudgetComments()).containsOnly(budgetCommentBack);
        assertThat(budgetCommentBack.getBudget()).isEqualTo(budget);

        budget.setBudgetComments(new HashSet<>());
        assertThat(budget.getBudgetComments()).doesNotContain(budgetCommentBack);
        assertThat(budgetCommentBack.getBudget()).isNull();
    }

    @Test
    void contryTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        budget.setContry(countryBack);
        assertThat(budget.getContry()).isEqualTo(countryBack);

        budget.contry(null);
        assertThat(budget.getContry()).isNull();
    }

    @Test
    void userAssignedToTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        Users usersBack = getUsersRandomSampleGenerator();

        budget.setUserAssignedTo(usersBack);
        assertThat(budget.getUserAssignedTo()).isEqualTo(usersBack);

        budget.userAssignedTo(null);
        assertThat(budget.getUserAssignedTo()).isNull();
    }

    @Test
    void userApprovedByTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        Users usersBack = getUsersRandomSampleGenerator();

        budget.setUserApprovedBy(usersBack);
        assertThat(budget.getUserApprovedBy()).isEqualTo(usersBack);

        budget.userApprovedBy(null);
        assertThat(budget.getUserApprovedBy()).isNull();
    }

    @Test
    void userOwnerTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        Users usersBack = getUsersRandomSampleGenerator();

        budget.setUserOwner(usersBack);
        assertThat(budget.getUserOwner()).isEqualTo(usersBack);

        budget.userOwner(null);
        assertThat(budget.getUserOwner()).isNull();
    }

    @Test
    void authorizedTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        Users usersBack = getUsersRandomSampleGenerator();

        budget.addAuthorized(usersBack);
        assertThat(budget.getAuthorizeds()).containsOnly(usersBack);
        assertThat(usersBack.getBudgetAuthorizeds()).containsOnly(budget);

        budget.removeAuthorized(usersBack);
        assertThat(budget.getAuthorizeds()).doesNotContain(usersBack);
        assertThat(usersBack.getBudgetAuthorizeds()).doesNotContain(budget);

        budget.authorizeds(new HashSet<>(Set.of(usersBack)));
        assertThat(budget.getAuthorizeds()).containsOnly(usersBack);
        assertThat(usersBack.getBudgetAuthorizeds()).containsOnly(budget);

        budget.setAuthorizeds(new HashSet<>());
        assertThat(budget.getAuthorizeds()).doesNotContain(usersBack);
        assertThat(usersBack.getBudgetAuthorizeds()).doesNotContain(budget);
    }

    @Test
    void roleAuthorizedTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        UserRole userRoleBack = getUserRoleRandomSampleGenerator();

        budget.addRoleAuthorized(userRoleBack);
        assertThat(budget.getRoleAuthorizeds()).containsOnly(userRoleBack);
        assertThat(userRoleBack.getBudgets()).containsOnly(budget);

        budget.removeRoleAuthorized(userRoleBack);
        assertThat(budget.getRoleAuthorizeds()).doesNotContain(userRoleBack);
        assertThat(userRoleBack.getBudgets()).doesNotContain(budget);

        budget.roleAuthorizeds(new HashSet<>(Set.of(userRoleBack)));
        assertThat(budget.getRoleAuthorizeds()).containsOnly(userRoleBack);
        assertThat(userRoleBack.getBudgets()).containsOnly(budget);

        budget.setRoleAuthorizeds(new HashSet<>());
        assertThat(budget.getRoleAuthorizeds()).doesNotContain(userRoleBack);
        assertThat(userRoleBack.getBudgets()).doesNotContain(budget);
    }
}
