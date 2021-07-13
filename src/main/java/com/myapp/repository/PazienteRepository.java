package com.myapp.repository;

import com.myapp.domain.Paziente;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Paziente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PazienteRepository extends JpaRepository<Paziente, Long> {}
