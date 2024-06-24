package com.trycore.quotizo.domain;

import static com.trycore.quotizo.domain.DroolsRuleFileTestSamples.*;
import static com.trycore.quotizo.domain.RuleAssignmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DroolsRuleFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DroolsRuleFile.class);
        DroolsRuleFile droolsRuleFile1 = getDroolsRuleFileSample1();
        DroolsRuleFile droolsRuleFile2 = new DroolsRuleFile();
        assertThat(droolsRuleFile1).isNotEqualTo(droolsRuleFile2);

        droolsRuleFile2.setId(droolsRuleFile1.getId());
        assertThat(droolsRuleFile1).isEqualTo(droolsRuleFile2);

        droolsRuleFile2 = getDroolsRuleFileSample2();
        assertThat(droolsRuleFile1).isNotEqualTo(droolsRuleFile2);
    }

    @Test
    void ruleAssignmentTest() {
        DroolsRuleFile droolsRuleFile = getDroolsRuleFileRandomSampleGenerator();
        RuleAssignment ruleAssignmentBack = getRuleAssignmentRandomSampleGenerator();

        droolsRuleFile.addRuleAssignment(ruleAssignmentBack);
        assertThat(droolsRuleFile.getRuleAssignments()).containsOnly(ruleAssignmentBack);
        assertThat(ruleAssignmentBack.getDroolsRuleFile()).isEqualTo(droolsRuleFile);

        droolsRuleFile.removeRuleAssignment(ruleAssignmentBack);
        assertThat(droolsRuleFile.getRuleAssignments()).doesNotContain(ruleAssignmentBack);
        assertThat(ruleAssignmentBack.getDroolsRuleFile()).isNull();

        droolsRuleFile.ruleAssignments(new HashSet<>(Set.of(ruleAssignmentBack)));
        assertThat(droolsRuleFile.getRuleAssignments()).containsOnly(ruleAssignmentBack);
        assertThat(ruleAssignmentBack.getDroolsRuleFile()).isEqualTo(droolsRuleFile);

        droolsRuleFile.setRuleAssignments(new HashSet<>());
        assertThat(droolsRuleFile.getRuleAssignments()).doesNotContain(ruleAssignmentBack);
        assertThat(ruleAssignmentBack.getDroolsRuleFile()).isNull();
    }
}
