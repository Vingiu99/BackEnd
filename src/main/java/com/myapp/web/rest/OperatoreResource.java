package com.myapp.web.rest;

import com.myapp.domain.Operatore;
import com.myapp.repository.OperatoreRepository;
import com.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myapp.domain.Operatore}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OperatoreResource {

    private final Logger log = LoggerFactory.getLogger(OperatoreResource.class);

    private static final String ENTITY_NAME = "operatore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OperatoreRepository operatoreRepository;

    public OperatoreResource(OperatoreRepository operatoreRepository) {
        this.operatoreRepository = operatoreRepository;
    }

    /**
     * {@code POST  /operatores} : Create a new operatore.
     *
     * @param operatore the operatore to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new operatore, or with status {@code 400 (Bad Request)} if the operatore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/operatores")
    public ResponseEntity<Operatore> createOperatore(@Valid @RequestBody Operatore operatore) throws URISyntaxException {
        log.debug("REST request to save Operatore : {}", operatore);
        if (operatore.getId() != null) {
            throw new BadRequestAlertException("A new operatore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Operatore result = operatoreRepository.save(operatore);
        return ResponseEntity
            .created(new URI("/api/operatores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /operatores/:id} : Updates an existing operatore.
     *
     * @param id the id of the operatore to save.
     * @param operatore the operatore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operatore,
     * or with status {@code 400 (Bad Request)} if the operatore is not valid,
     * or with status {@code 500 (Internal Server Error)} if the operatore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/operatores/{id}")
    public ResponseEntity<Operatore> updateOperatore(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Operatore operatore
    ) throws URISyntaxException {
        log.debug("REST request to update Operatore : {}, {}", id, operatore);
        if (operatore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operatore.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operatoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Operatore result = operatoreRepository.save(operatore);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operatore.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /operatores/:id} : Partial updates given fields of an existing operatore, field will ignore if it is null
     *
     * @param id the id of the operatore to save.
     * @param operatore the operatore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operatore,
     * or with status {@code 400 (Bad Request)} if the operatore is not valid,
     * or with status {@code 404 (Not Found)} if the operatore is not found,
     * or with status {@code 500 (Internal Server Error)} if the operatore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/operatores/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Operatore> partialUpdateOperatore(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Operatore operatore
    ) throws URISyntaxException {
        log.debug("REST request to partial update Operatore partially : {}, {}", id, operatore);
        if (operatore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operatore.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operatoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Operatore> result = operatoreRepository
            .findById(operatore.getId())
            .map(
                existingOperatore -> {
                    if (operatore.getIdAslOperatore() != null) {
                        existingOperatore.setIdAslOperatore(operatore.getIdAslOperatore());
                    }
                    if (operatore.getNome() != null) {
                        existingOperatore.setNome(operatore.getNome());
                    }
                    if (operatore.getCognome() != null) {
                        existingOperatore.setCognome(operatore.getCognome());
                    }
                    if (operatore.getDataNascita() != null) {
                        existingOperatore.setDataNascita(operatore.getDataNascita());
                    }
                    if (operatore.getLuogoNascita() != null) {
                        existingOperatore.setLuogoNascita(operatore.getLuogoNascita());
                    }
                    if (operatore.getEmail() != null) {
                        existingOperatore.setEmail(operatore.getEmail());
                    }
                    if (operatore.getTelefono() != null) {
                        existingOperatore.setTelefono(operatore.getTelefono());
                    }
                    if (operatore.getPassword() != null) {
                        existingOperatore.setPassword(operatore.getPassword());
                    }

                    return existingOperatore;
                }
            )
            .map(operatoreRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operatore.getId().toString())
        );
    }

    /**
     * {@code GET  /operatores} : get all the operatores.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of operatores in body.
     */
    @GetMapping("/operatores")
    public List<Operatore> getAllOperatores(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Operatores");
        return operatoreRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /operatores/:id} : get the "id" operatore.
     *
     * @param id the id of the operatore to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the operatore, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/operatores/{id}")
    public ResponseEntity<Operatore> getOperatore(@PathVariable Long id) {
        log.debug("REST request to get Operatore : {}", id);
        Optional<Operatore> operatore = operatoreRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(operatore);
    }

    /**
     * {@code DELETE  /operatores/:id} : delete the "id" operatore.
     *
     * @param id the id of the operatore to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/operatores/{id}")
    public ResponseEntity<Void> deleteOperatore(@PathVariable Long id) {
        log.debug("REST request to delete Operatore : {}", id);
        operatoreRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
