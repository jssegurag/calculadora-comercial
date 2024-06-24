package com.trycore.quotizo.domain;

import static com.trycore.quotizo.domain.FinancialParameterTestSamples.*;
import static com.trycore.quotizo.domain.FinancialParameterTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FinancialParameterTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinancialParameterType.class);
        FinancialParameterType financialParameterType1 = getFinancialParameterTypeSample1();
        FinancialParameterType financialParameterType2 = new FinancialParameterType();
        assertThat(financialParameterType1).isNotEqualTo(financialParameterType2);

        financialParameterType2.setId(financialParameterType1.getId());
        assertThat(financialParameterType1).isEqualTo(financialParameterType2);

        financialParameterType2 = getFinancialParameterTypeSample2();
        assertThat(financialParameterType1).isNotEqualTo(financialParameterType2);
    }

    @Test
    void financialParameterTest() {
        FinancialParameterType financialParameterType = getFinancialParameterTypeRandomSampleGenerator();
        FinancialParameter financialParameterBack = getFinancialParameterRandomSampleGenerator();

        financialParameterType.addFinancialParameter(financialParameterBack);
        assertThat(financialParameterType.getFinancialParameters()).containsOnly(financialParameterBack);
        assertThat(financialParameterBack.getFinancialParameterType()).isEqualTo(financialParameterType);

        financialParameterType.removeFinancialParameter(financialParameterBack);
        assertThat(financialParameterType.getFinancialParameters()).doesNotContain(financialParameterBack);
        assertThat(financialParameterBack.getFinancialParameterType()).isNull();

        financialParameterType.financialParameters(new HashSet<>(Set.of(financialParameterBack)));
        assertThat(financialParameterType.getFinancialParameters()).containsOnly(financialParameterBack);
        assertThat(financialParameterBack.getFinancialParameterType()).isEqualTo(financialParameterType);

        financialParameterType.setFinancialParameters(new HashSet<>());
        assertThat(financialParameterType.getFinancialParameters()).doesNotContain(financialParameterBack);
        assertThat(financialParameterBack.getFinancialParameterType()).isNull();
    }
}
