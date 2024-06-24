package com.trycore.quotizo.domain;

import static com.trycore.quotizo.domain.PositionTestSamples.*;
import static com.trycore.quotizo.domain.ResourceAllocationTestSamples.*;
import static com.trycore.quotizo.domain.ResourceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ResourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resource.class);
        Resource resource1 = getResourceSample1();
        Resource resource2 = new Resource();
        assertThat(resource1).isNotEqualTo(resource2);

        resource2.setId(resource1.getId());
        assertThat(resource1).isEqualTo(resource2);

        resource2 = getResourceSample2();
        assertThat(resource1).isNotEqualTo(resource2);
    }

    @Test
    void positionTest() {
        Resource resource = getResourceRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        resource.setPosition(positionBack);
        assertThat(resource.getPosition()).isEqualTo(positionBack);

        resource.position(null);
        assertThat(resource.getPosition()).isNull();
    }

    @Test
    void resourceAllocationTest() {
        Resource resource = getResourceRandomSampleGenerator();
        ResourceAllocation resourceAllocationBack = getResourceAllocationRandomSampleGenerator();

        resource.addResourceAllocation(resourceAllocationBack);
        assertThat(resource.getResourceAllocations()).containsOnly(resourceAllocationBack);
        assertThat(resourceAllocationBack.getResource()).isEqualTo(resource);

        resource.removeResourceAllocation(resourceAllocationBack);
        assertThat(resource.getResourceAllocations()).doesNotContain(resourceAllocationBack);
        assertThat(resourceAllocationBack.getResource()).isNull();

        resource.resourceAllocations(new HashSet<>(Set.of(resourceAllocationBack)));
        assertThat(resource.getResourceAllocations()).containsOnly(resourceAllocationBack);
        assertThat(resourceAllocationBack.getResource()).isEqualTo(resource);

        resource.setResourceAllocations(new HashSet<>());
        assertThat(resource.getResourceAllocations()).doesNotContain(resourceAllocationBack);
        assertThat(resourceAllocationBack.getResource()).isNull();
    }
}
