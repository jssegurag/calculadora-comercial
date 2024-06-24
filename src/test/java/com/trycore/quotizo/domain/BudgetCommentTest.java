package com.trycore.quotizo.domain;

import static com.trycore.quotizo.domain.BudgetCommentTestSamples.*;
import static com.trycore.quotizo.domain.BudgetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BudgetCommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BudgetComment.class);
        BudgetComment budgetComment1 = getBudgetCommentSample1();
        BudgetComment budgetComment2 = new BudgetComment();
        assertThat(budgetComment1).isNotEqualTo(budgetComment2);

        budgetComment2.setId(budgetComment1.getId());
        assertThat(budgetComment1).isEqualTo(budgetComment2);

        budgetComment2 = getBudgetCommentSample2();
        assertThat(budgetComment1).isNotEqualTo(budgetComment2);
    }

    @Test
    void budgetTest() {
        BudgetComment budgetComment = getBudgetCommentRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        budgetComment.setBudget(budgetBack);
        assertThat(budgetComment.getBudget()).isEqualTo(budgetBack);

        budgetComment.budget(null);
        assertThat(budgetComment.getBudget()).isNull();
    }
}
