package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrenotazioneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prenotazione.class);
        Prenotazione prenotazione1 = new Prenotazione();
        prenotazione1.setId(1L);
        Prenotazione prenotazione2 = new Prenotazione();
        prenotazione2.setId(prenotazione1.getId());
        assertThat(prenotazione1).isEqualTo(prenotazione2);
        prenotazione2.setId(2L);
        assertThat(prenotazione1).isNotEqualTo(prenotazione2);
        prenotazione1.setId(null);
        assertThat(prenotazione1).isNotEqualTo(prenotazione2);
    }
}
