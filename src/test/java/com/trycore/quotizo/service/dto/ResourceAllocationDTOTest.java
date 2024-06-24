package com.trycore.quotizo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResourceAllocationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceAllocationDTO.class);
        ResourceAllocationDTO resourceAllocationDTO1 = new ResourceAllocationDTO();
        resourceAllocationDTO1.setId(1L);
        ResourceAllocationDTO resourceAllocationDTO2 = new ResourceAllocationDTO();
        assertThat(resourceAllocationDTO1).isNotEqualTo(resourceAllocationDTO2);
        resourceAllocationDTO2.setId(resourceAllocationDTO1.getId());
        assertThat(resourceAllocationDTO1).isEqualTo(resourceAllocationDTO2);
        resourceAllocationDTO2.setId(2L);
        assertThat(resourceAllocationDTO1).isNotEqualTo(resourceAllocationDTO2);
        resourceAllocationDTO1.setId(null);
        assertThat(resourceAllocationDTO1).isNotEqualTo(resourceAllocationDTO2);
    }
}
