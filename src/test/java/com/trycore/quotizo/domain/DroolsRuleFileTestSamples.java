package com.trycore.quotizo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DroolsRuleFileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DroolsRuleFile getDroolsRuleFileSample1() {
        return new DroolsRuleFile()
            .id(1L)
            .fileName("fileName1")
            .description("description1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static DroolsRuleFile getDroolsRuleFileSample2() {
        return new DroolsRuleFile()
            .id(2L)
            .fileName("fileName2")
            .description("description2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static DroolsRuleFile getDroolsRuleFileRandomSampleGenerator() {
        return new DroolsRuleFile()
            .id(longCount.incrementAndGet())
            .fileName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
