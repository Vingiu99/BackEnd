package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PazienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paziente.class);
        Paziente paziente1 = new Paziente();
        paziente1.setId(1L);
        Paziente paziente2 = new Paziente();
        paziente2.setId(paziente1.getId());
        assertThat(paziente1).isEqualTo(paziente2);
        paziente2.setId(2L);
        assertThat(paziente1).isNotEqualTo(paziente2);
        paziente1.setId(null);
        assertThat(paziente1).isNotEqualTo(paziente2);
    }
}
