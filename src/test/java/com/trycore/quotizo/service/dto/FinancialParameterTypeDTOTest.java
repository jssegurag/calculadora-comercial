package com.trycore.quotizo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FinancialParameterTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinancialParameterTypeDTO.class);
        FinancialParameterTypeDTO financialParameterTypeDTO1 = new FinancialParameterTypeDTO();
        financialParameterTypeDTO1.setId(1L);
        FinancialParameterTypeDTO financialParameterTypeDTO2 = new FinancialParameterTypeDTO();
        assertThat(financialParameterTypeDTO1).isNotEqualTo(financialParameterTypeDTO2);
        financialParameterTypeDTO2.setId(financialParameterTypeDTO1.getId());
        assertThat(financialParameterTypeDTO1).isEqualTo(financialParameterTypeDTO2);
        financialParameterTypeDTO2.setId(2L);
        assertThat(financialParameterTypeDTO1).isNotEqualTo(financialParameterTypeDTO2);
        financialParameterTypeDTO1.setId(null);
        assertThat(financialParameterTypeDTO1).isNotEqualTo(financialParameterTypeDTO2);
    }
}
