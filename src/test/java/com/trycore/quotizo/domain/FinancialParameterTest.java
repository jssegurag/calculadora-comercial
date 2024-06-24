package com.trycore.quotizo.domain;

import static com.trycore.quotizo.domain.CountryTestSamples.*;
import static com.trycore.quotizo.domain.FinancialParameterTestSamples.*;
import static com.trycore.quotizo.domain.FinancialParameterTypeTestSamples.*;
import static com.trycore.quotizo.domain.UserRoleTestSamples.*;
import static com.trycore.quotizo.domain.UsersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FinancialParameterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinancialParameter.class);
        FinancialParameter financialParameter1 = getFinancialParameterSample1();
        FinancialParameter financialParameter2 = new FinancialParameter();
        assertThat(financialParameter1).isNotEqualTo(financialParameter2);

        financialParameter2.setId(financialParameter1.getId());
        assertThat(financialParameter1).isEqualTo(financialParameter2);

        financialParameter2 = getFinancialParameterSample2();
        assertThat(financialParameter1).isNotEqualTo(financialParameter2);
    }

    @Test
    void financialParameterTypeTest() {
        FinancialParameter financialParameter = getFinancialParameterRandomSampleGenerator();
        FinancialParameterType financialParameterTypeBack = getFinancialParameterTypeRandomSampleGenerator();

        financialParameter.setFinancialParameterType(financialParameterTypeBack);
        assertThat(financialParameter.getFinancialParameterType()).isEqualTo(financialParameterTypeBack);

        financialParameter.financialParameterType(null);
        assertThat(financialParameter.getFinancialParameterType()).isNull();
    }

    @Test
    void countryTest() {
        FinancialParameter financialParameter = getFinancialParameterRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        financialParameter.setCountry(countryBack);
        assertThat(financialParameter.getCountry()).isEqualTo(countryBack);

        financialParameter.country(null);
        assertThat(financialParameter.getCountry()).isNull();
    }

    @Test
    void administratorTest() {
        FinancialParameter financialParameter = getFinancialParameterRandomSampleGenerator();
        Users usersBack = getUsersRandomSampleGenerator();

        financialParameter.setAdministrator(usersBack);
        assertThat(financialParameter.getAdministrator()).isEqualTo(usersBack);

        financialParameter.administrator(null);
        assertThat(financialParameter.getAdministrator()).isNull();
    }

    @Test
    void roleAuthorizedTest() {
        FinancialParameter financialParameter = getFinancialParameterRandomSampleGenerator();
        UserRole userRoleBack = getUserRoleRandomSampleGenerator();

        financialParameter.addRoleAuthorized(userRoleBack);
        assertThat(financialParameter.getRoleAuthorizeds()).containsOnly(userRoleBack);
        assertThat(userRoleBack.getFinancialParameters()).containsOnly(financialParameter);

        financialParameter.removeRoleAuthorized(userRoleBack);
        assertThat(financialParameter.getRoleAuthorizeds()).doesNotContain(userRoleBack);
        assertThat(userRoleBack.getFinancialParameters()).doesNotContain(financialParameter);

        financialParameter.roleAuthorizeds(new HashSet<>(Set.of(userRoleBack)));
        assertThat(financialParameter.getRoleAuthorizeds()).containsOnly(userRoleBack);
        assertThat(userRoleBack.getFinancialParameters()).containsOnly(financialParameter);

        financialParameter.setRoleAuthorizeds(new HashSet<>());
        assertThat(financialParameter.getRoleAuthorizeds()).doesNotContain(userRoleBack);
        assertThat(userRoleBack.getFinancialParameters()).doesNotContain(financialParameter);
    }
}
