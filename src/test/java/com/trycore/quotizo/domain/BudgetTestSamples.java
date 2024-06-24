package com.trycore.quotizo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BudgetTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Budget getBudgetSample1() {
        return new Budget()
            .id(1L)
            .name("name1")
            .estimatedDurationDays(1)
            .durationMonths(1)
            .resourceCount(1)
            .descriptionOtherTaxes("descriptionOtherTaxes1")
            .approvalDecision("approvalDecision1")
            .approvalComments("approvalComments1")
            .approvalStatus("approvalStatus1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Budget getBudgetSample2() {
        return new Budget()
            .id(2L)
            .name("name2")
            .estimatedDurationDays(2)
            .durationMonths(2)
            .resourceCount(2)
            .descriptionOtherTaxes("descriptionOtherTaxes2")
            .approvalDecision("approvalDecision2")
            .approvalComments("approvalComments2")
            .approvalStatus("approvalStatus2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Budget getBudgetRandomSampleGenerator() {
        return new Budget()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .estimatedDurationDays(intCount.incrementAndGet())
            .durationMonths(intCount.incrementAndGet())
            .resourceCount(intCount.incrementAndGet())
            .descriptionOtherTaxes(UUID.randomUUID().toString())
            .approvalDecision(UUID.randomUUID().toString())
            .approvalComments(UUID.randomUUID().toString())
            .approvalStatus(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
