package com.trycore.quotizo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DroolsRuleFileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DroolsRuleFileDTO.class);
        DroolsRuleFileDTO droolsRuleFileDTO1 = new DroolsRuleFileDTO();
        droolsRuleFileDTO1.setId(1L);
        DroolsRuleFileDTO droolsRuleFileDTO2 = new DroolsRuleFileDTO();
        assertThat(droolsRuleFileDTO1).isNotEqualTo(droolsRuleFileDTO2);
        droolsRuleFileDTO2.setId(droolsRuleFileDTO1.getId());
        assertThat(droolsRuleFileDTO1).isEqualTo(droolsRuleFileDTO2);
        droolsRuleFileDTO2.setId(2L);
        assertThat(droolsRuleFileDTO1).isNotEqualTo(droolsRuleFileDTO2);
        droolsRuleFileDTO1.setId(null);
        assertThat(droolsRuleFileDTO1).isNotEqualTo(droolsRuleFileDTO2);
    }
}
