package com.trycore.quotizo.domain;

import static com.trycore.quotizo.domain.PositionTestSamples.*;
import static com.trycore.quotizo.domain.ResourceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trycore.quotizo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PositionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Position.class);
        Position position1 = getPositionSample1();
        Position position2 = new Position();
        assertThat(position1).isNotEqualTo(position2);

        position2.setId(position1.getId());
        assertThat(position1).isEqualTo(position2);

        position2 = getPositionSample2();
        assertThat(position1).isNotEqualTo(position2);
    }

    @Test
    void resourceTest() {
        Position position = getPositionRandomSampleGenerator();
        Resource resourceBack = getResourceRandomSampleGenerator();

        position.setResource(resourceBack);
        assertThat(position.getResource()).isEqualTo(resourceBack);
        assertThat(resourceBack.getPosition()).isEqualTo(position);

        position.resource(null);
        assertThat(position.getResource()).isNull();
        assertThat(resourceBack.getPosition()).isNull();
    }
}
