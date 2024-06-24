package com.trycore.quotizo.domain;

import static com.trycore.quotizo.domain.BudgetTemplateTestSamples.*;
import static com.trycore.quotizo.domain.BudgetTestSamples.*;
import static com.trycore.quotizo.domain.ResourceAllocationTestSamples.*;
import static com.trycore.quotizo.domain.ResourceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResourceAllocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceAllocation.class);
        ResourceAllocation resourceAllocation1 = getResourceAllocationSample1();
        ResourceAllocation resourceAllocation2 = new ResourceAllocation();
        assertThat(resourceAllocation1).isNotEqualTo(resourceAllocation2);

        resourceAllocation2.setId(resourceAllocation1.getId());
        assertThat(resourceAllocation1).isEqualTo(resourceAllocation2);

        resourceAllocation2 = getResourceAllocationSample2();
        assertThat(resourceAllocation1).isNotEqualTo(resourceAllocation2);
    }

    @Test
    void budgetTest() {
        ResourceAllocation resourceAllocation = getResourceAllocationRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        resourceAllocation.setBudget(budgetBack);
        assertThat(resourceAllocation.getBudget()).isEqualTo(budgetBack);

        resourceAllocation.budget(null);
        assertThat(resourceAllocation.getBudget()).isNull();
    }

    @Test
    void resourceTest() {
        ResourceAllocation resourceAllocation = getResourceAllocationRandomSampleGenerator();
        Resource resourceBack = getResourceRandomSampleGenerator();

        resourceAllocation.setResource(resourceBack);
        assertThat(resourceAllocation.getResource()).isEqualTo(resourceBack);

        resourceAllocation.resource(null);
        assertThat(resourceAllocation.getResource()).isNull();
    }

    @Test
    void budgetTemplateTest() {
        ResourceAllocation resourceAllocation = getResourceAllocationRandomSampleGenerator();
        BudgetTemplate budgetTemplateBack = getBudgetTemplateRandomSampleGenerator();

        resourceAllocation.setBudgetTemplate(budgetTemplateBack);
        assertThat(resourceAllocation.getBudgetTemplate()).isEqualTo(budgetTemplateBack);

        resourceAllocation.budgetTemplate(null);
        assertThat(resourceAllocation.getBudgetTemplate()).isNull();
    }
}
