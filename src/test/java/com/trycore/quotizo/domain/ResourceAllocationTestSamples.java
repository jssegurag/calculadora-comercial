package com.trycore.quotizo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ResourceAllocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ResourceAllocation getResourceAllocationSample1() {
        return new ResourceAllocation().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static ResourceAllocation getResourceAllocationSample2() {
        return new ResourceAllocation().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static ResourceAllocation getResourceAllocationRandomSampleGenerator() {
        return new ResourceAllocation()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
