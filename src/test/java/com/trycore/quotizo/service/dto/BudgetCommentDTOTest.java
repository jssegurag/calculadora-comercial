package com.trycore.quotizo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BudgetCommentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BudgetCommentDTO.class);
        BudgetCommentDTO budgetCommentDTO1 = new BudgetCommentDTO();
        budgetCommentDTO1.setId(1L);
        BudgetCommentDTO budgetCommentDTO2 = new BudgetCommentDTO();
        assertThat(budgetCommentDTO1).isNotEqualTo(budgetCommentDTO2);
        budgetCommentDTO2.setId(budgetCommentDTO1.getId());
        assertThat(budgetCommentDTO1).isEqualTo(budgetCommentDTO2);
        budgetCommentDTO2.setId(2L);
        assertThat(budgetCommentDTO1).isNotEqualTo(budgetCommentDTO2);
        budgetCommentDTO1.setId(null);
        assertThat(budgetCommentDTO1).isNotEqualTo(budgetCommentDTO2);
    }
}
