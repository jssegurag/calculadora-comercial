package com.trycore.quotizo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BudgetTemplateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BudgetTemplate getBudgetTemplateSample1() {
        return new BudgetTemplate()
            .id(1L)
            .name("name1")
            .estimatedDurationDays(1)
            .durationMonths(1)
            .resourceCount(1)
            .descriptionOtherTaxes("descriptionOtherTaxes1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static BudgetTemplate getBudgetTemplateSample2() {
        return new BudgetTemplate()
            .id(2L)
            .name("name2")
            .estimatedDurationDays(2)
            .durationMonths(2)
            .resourceCount(2)
            .descriptionOtherTaxes("descriptionOtherTaxes2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static BudgetTemplate getBudgetTemplateRandomSampleGenerator() {
        return new BudgetTemplate()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .estimatedDurationDays(intCount.incrementAndGet())
            .durationMonths(intCount.incrementAndGet())
            .resourceCount(intCount.incrementAndGet())
            .descriptionOtherTaxes(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
