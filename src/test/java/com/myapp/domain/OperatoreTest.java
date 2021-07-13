package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OperatoreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operatore.class);
        Operatore operatore1 = new Operatore();
        operatore1.setId(1L);
        Operatore operatore2 = new Operatore();
        operatore2.setId(operatore1.getId());
        assertThat(operatore1).isEqualTo(operatore2);
        operatore2.setId(2L);
        assertThat(operatore1).isNotEqualTo(operatore2);
        operatore1.setId(null);
        assertThat(operatore1).isNotEqualTo(operatore2);
    }
}
