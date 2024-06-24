package com.trycore.quotizo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PositionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Position getPositionSample1() {
        return new Position().id(1L).name("name1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static Position getPositionSample2() {
        return new Position().id(2L).name("name2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static Position getPositionRandomSampleGenerator() {
        return new Position()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}