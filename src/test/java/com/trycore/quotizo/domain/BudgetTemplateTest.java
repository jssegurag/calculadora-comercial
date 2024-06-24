package com.trycore.quotizo.domain;

import static com.trycore.quotizo.domain.BudgetTemplateTestSamples.*;
import static com.trycore.quotizo.domain.CountryTestSamples.*;
import static com.trycore.quotizo.domain.ResourceAllocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BudgetTemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BudgetTemplate.class);
        BudgetTemplate budgetTemplate1 = getBudgetTemplateSample1();
        BudgetTemplate budgetTemplate2 = new BudgetTemplate();
        assertThat(budgetTemplate1).isNotEqualTo(budgetTemplate2);

        budgetTemplate2.setId(budgetTemplate1.getId());
        assertThat(budgetTemplate1).isEqualTo(budgetTemplate2);

        budgetTemplate2 = getBudgetTemplateSample2();
        assertThat(budgetTemplate1).isNotEqualTo(budgetTemplate2);
    }

    @Test
    void resourceAllocationTest() {
        BudgetTemplate budgetTemplate = getBudgetTemplateRandomSampleGenerator();
        ResourceAllocation resourceAllocationBack = getResourceAllocationRandomSampleGenerator();

        budgetTemplate.addResourceAllocation(resourceAllocationBack);
        assertThat(budgetTemplate.getResourceAllocations()).containsOnly(resourceAllocationBack);
        assertThat(resourceAllocationBack.getBudgetTemplate()).isEqualTo(budgetTemplate);

        budgetTemplate.removeResourceAllocation(resourceAllocationBack);
        assertThat(budgetTemplate.getResourceAllocations()).doesNotContain(resourceAllocationBack);
        assertThat(resourceAllocationBack.getBudgetTemplate()).isNull();

        budgetTemplate.resourceAllocations(new HashSet<>(Set.of(resourceAllocationBack)));
        assertThat(budgetTemplate.getResourceAllocations()).containsOnly(resourceAllocationBack);
        assertThat(resourceAllocationBack.getBudgetTemplate()).isEqualTo(budgetTemplate);

        budgetTemplate.setResourceAllocations(new HashSet<>());
        assertThat(budgetTemplate.getResourceAllocations()).doesNotContain(resourceAllocationBack);
        assertThat(resourceAllocationBack.getBudgetTemplate()).isNull();
    }

    @Test
    void countryTest() {
        BudgetTemplate budgetTemplate = getBudgetTemplateRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        budgetTemplate.setCountry(countryBack);
        assertThat(budgetTemplate.getCountry()).isEqualTo(countryBack);

        budgetTemplate.country(null);
        assertThat(budgetTemplate.getCountry()).isNull();
    }
}
