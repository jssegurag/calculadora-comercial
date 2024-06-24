package com.trycore.quotizo.domain;

import static com.trycore.quotizo.domain.BudgetTemplateTestSamples.*;
import static com.trycore.quotizo.domain.BudgetTestSamples.*;
import static com.trycore.quotizo.domain.CountryTestSamples.*;
import static com.trycore.quotizo.domain.FinancialParameterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CountryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Country.class);
        Country country1 = getCountrySample1();
        Country country2 = new Country();
        assertThat(country1).isNotEqualTo(country2);

        country2.setId(country1.getId());
        assertThat(country1).isEqualTo(country2);

        country2 = getCountrySample2();
        assertThat(country1).isNotEqualTo(country2);
    }

    @Test
    void financialParameterTest() {
        Country country = getCountryRandomSampleGenerator();
        FinancialParameter financialParameterBack = getFinancialParameterRandomSampleGenerator();

        country.addFinancialParameter(financialParameterBack);
        assertThat(country.getFinancialParameters()).containsOnly(financialParameterBack);
        assertThat(financialParameterBack.getCountry()).isEqualTo(country);

        country.removeFinancialParameter(financialParameterBack);
        assertThat(country.getFinancialParameters()).doesNotContain(financialParameterBack);
        assertThat(financialParameterBack.getCountry()).isNull();

        country.financialParameters(new HashSet<>(Set.of(financialParameterBack)));
        assertThat(country.getFinancialParameters()).containsOnly(financialParameterBack);
        assertThat(financialParameterBack.getCountry()).isEqualTo(country);

        country.setFinancialParameters(new HashSet<>());
        assertThat(country.getFinancialParameters()).doesNotContain(financialParameterBack);
        assertThat(financialParameterBack.getCountry()).isNull();
    }

    @Test
    void budgetTest() {
        Country country = getCountryRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        country.addBudget(budgetBack);
        assertThat(country.getBudgets()).containsOnly(budgetBack);
        assertThat(budgetBack.getContry()).isEqualTo(country);

        country.removeBudget(budgetBack);
        assertThat(country.getBudgets()).doesNotContain(budgetBack);
        assertThat(budgetBack.getContry()).isNull();

        country.budgets(new HashSet<>(Set.of(budgetBack)));
        assertThat(country.getBudgets()).containsOnly(budgetBack);
        assertThat(budgetBack.getContry()).isEqualTo(country);

        country.setBudgets(new HashSet<>());
        assertThat(country.getBudgets()).doesNotContain(budgetBack);
        assertThat(budgetBack.getContry()).isNull();
    }

    @Test
    void budgetTemplateTest() {
        Country country = getCountryRandomSampleGenerator();
        BudgetTemplate budgetTemplateBack = getBudgetTemplateRandomSampleGenerator();

        country.addBudgetTemplate(budgetTemplateBack);
        assertThat(country.getBudgetTemplates()).containsOnly(budgetTemplateBack);
        assertThat(budgetTemplateBack.getCountry()).isEqualTo(country);

        country.removeBudgetTemplate(budgetTemplateBack);
        assertThat(country.getBudgetTemplates()).doesNotContain(budgetTemplateBack);
        assertThat(budgetTemplateBack.getCountry()).isNull();

        country.budgetTemplates(new HashSet<>(Set.of(budgetTemplateBack)));
        assertThat(country.getBudgetTemplates()).containsOnly(budgetTemplateBack);
        assertThat(budgetTemplateBack.getCountry()).isEqualTo(country);

        country.setBudgetTemplates(new HashSet<>());
        assertThat(country.getBudgetTemplates()).doesNotContain(budgetTemplateBack);
        assertThat(budgetTemplateBack.getCountry()).isNull();
    }
}
