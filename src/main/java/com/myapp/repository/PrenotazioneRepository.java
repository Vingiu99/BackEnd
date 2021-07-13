package com.myapp.repository;

import com.myapp.domain.Prenotazione;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Prenotazione entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {}
