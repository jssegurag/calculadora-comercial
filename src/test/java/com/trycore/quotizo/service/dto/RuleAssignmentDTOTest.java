package com.trycore.quotizo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RuleAssignmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RuleAssignmentDTO.class);
        RuleAssignmentDTO ruleAssignmentDTO1 = new RuleAssignmentDTO();
        ruleAssignmentDTO1.setId(1L);
        RuleAssignmentDTO ruleAssignmentDTO2 = new RuleAssignmentDTO();
        assertThat(ruleAssignmentDTO1).isNotEqualTo(ruleAssignmentDTO2);
        ruleAssignmentDTO2.setId(ruleAssignmentDTO1.getId());
        assertThat(ruleAssignmentDTO1).isEqualTo(ruleAssignmentDTO2);
        ruleAssignmentDTO2.setId(2L);
        assertThat(ruleAssignmentDTO1).isNotEqualTo(ruleAssignmentDTO2);
        ruleAssignmentDTO1.setId(null);
        assertThat(ruleAssignmentDTO1).isNotEqualTo(ruleAssignmentDTO2);
    }
}
