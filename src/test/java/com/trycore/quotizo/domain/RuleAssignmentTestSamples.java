package com.trycore.quotizo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RuleAssignmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RuleAssignment getRuleAssignmentSample1() {
        return new RuleAssignment().id(1L).entityName("entityName1").entityId(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static RuleAssignment getRuleAssignmentSample2() {
        return new RuleAssignment().id(2L).entityName("entityName2").entityId(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static RuleAssignment getRuleAssignmentRandomSampleGenerator() {
        return new RuleAssignment()
            .id(longCount.incrementAndGet())
            .entityName(UUID.randomUUID().toString())
            .entityId(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
