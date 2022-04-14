package mottinol.coopcycle.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mottinol.coopcycle.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Courier.class);
        Courier courier1 = new Courier();
        courier1.setId("id1");
        Courier courier2 = new Courier();
        courier2.setId(courier1.getId());
        assertThat(courier1).isEqualTo(courier2);
        courier2.setId("id2");
        assertThat(courier1).isNotEqualTo(courier2);
        courier1.setId(null);
        assertThat(courier1).isNotEqualTo(courier2);
    }
}
