package com.myapp.repository;

import com.myapp.domain.Operatore;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Operatore entity.
 */
@Repository
public interface OperatoreRepository extends JpaRepository<Operatore, Long> {
    @Query(
        value = "select distinct operatore from Operatore operatore left join fetch operatore.prenotaziones",
        countQuery = "select count(distinct operatore) from Operatore operatore"
    )
    Page<Operatore> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct operatore from Operatore operatore left join fetch operatore.prenotaziones")
    List<Operatore> findAllWithEagerRelationships();

    @Query("select operatore from Operatore operatore left join fetch operatore.prenotaziones where operatore.id =:id")
    Optional<Operatore> findOneWithEagerRelationships(@Param("id") Long id);
}
