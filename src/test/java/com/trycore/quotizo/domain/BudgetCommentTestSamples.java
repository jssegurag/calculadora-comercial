package com.trycore.quotizo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BudgetCommentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BudgetComment getBudgetCommentSample1() {
        return new BudgetComment().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static BudgetComment getBudgetCommentSample2() {
        return new BudgetComment().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static BudgetComment getBudgetCommentRandomSampleGenerator() {
        return new BudgetComment()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
