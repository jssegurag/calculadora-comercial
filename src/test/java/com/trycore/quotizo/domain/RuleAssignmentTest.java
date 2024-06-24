package com.trycore.quotizo.domain;

import static com.trycore.quotizo.domain.DroolsRuleFileTestSamples.*;
import static com.trycore.quotizo.domain.RuleAssignmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RuleAssignmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RuleAssignment.class);
        RuleAssignment ruleAssignment1 = getRuleAssignmentSample1();
        RuleAssignment ruleAssignment2 = new RuleAssignment();
        assertThat(ruleAssignment1).isNotEqualTo(ruleAssignment2);

        ruleAssignment2.setId(ruleAssignment1.getId());
        assertThat(ruleAssignment1).isEqualTo(ruleAssignment2);

        ruleAssignment2 = getRuleAssignmentSample2();
        assertThat(ruleAssignment1).isNotEqualTo(ruleAssignment2);
    }

    @Test
    void droolsRuleFileTest() {
        RuleAssignment ruleAssignment = getRuleAssignmentRandomSampleGenerator();
        DroolsRuleFile droolsRuleFileBack = getDroolsRuleFileRandomSampleGenerator();

        ruleAssignment.setDroolsRuleFile(droolsRuleFileBack);
        assertThat(ruleAssignment.getDroolsRuleFile()).isEqualTo(droolsRuleFileBack);

        ruleAssignment.droolsRuleFile(null);
        assertThat(ruleAssignment.getDroolsRuleFile()).isNull();
    }
}
