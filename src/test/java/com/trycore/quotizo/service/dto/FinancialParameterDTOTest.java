package com.trycore.quotizo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FinancialParameterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinancialParameterDTO.class);
        FinancialParameterDTO financialParameterDTO1 = new FinancialParameterDTO();
        financialParameterDTO1.setId(1L);
        FinancialParameterDTO financialParameterDTO2 = new FinancialParameterDTO();
        assertThat(financialParameterDTO1).isNotEqualTo(financialParameterDTO2);
        financialParameterDTO2.setId(financialParameterDTO1.getId());
        assertThat(financialParameterDTO1).isEqualTo(financialParameterDTO2);
        financialParameterDTO2.setId(2L);
        assertThat(financialParameterDTO1).isNotEqualTo(financialParameterDTO2);
        financialParameterDTO1.setId(null);
        assertThat(financialParameterDTO1).isNotEqualTo(financialParameterDTO2);
    }
}
