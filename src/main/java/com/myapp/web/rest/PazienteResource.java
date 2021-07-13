package com.myapp.web.rest;

import com.myapp.domain.Paziente;
import com.myapp.repository.PazienteRepository;
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
 * REST controller for managing {@link com.myapp.domain.Paziente}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PazienteResource {

    private final Logger log = LoggerFactory.getLogger(PazienteResource.class);

    private static final String ENTITY_NAME = "paziente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PazienteRepository pazienteRepository;

    public PazienteResource(PazienteRepository pazienteRepository) {
        this.pazienteRepository = pazienteRepository;
    }

    /**
     * {@code POST  /pazientes} : Create a new paziente.
     *
     * @param paziente the paziente to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paziente, or with status {@code 400 (Bad Request)} if the paziente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pazientes")
    public ResponseEntity<Paziente> createPaziente(@Valid @RequestBody Paziente paziente) throws URISyntaxException {
        log.debug("REST request to save Paziente : {}", paziente);
        if (paziente.getId() != null) {
            throw new BadRequestAlertException("A new paziente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Paziente result = pazienteRepository.save(paziente);
        return ResponseEntity
            .created(new URI("/api/pazientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pazientes/:id} : Updates an existing paziente.
     *
     * @param id the id of the paziente to save.
     * @param paziente the paziente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paziente,
     * or with status {@code 400 (Bad Request)} if the paziente is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paziente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pazientes/{id}")
    public ResponseEntity<Paziente> updatePaziente(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Paziente paziente
    ) throws URISyntaxException {
        log.debug("REST request to update Paziente : {}, {}", id, paziente);
        if (paziente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paziente.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pazienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Paziente result = pazienteRepository.save(paziente);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paziente.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pazientes/:id} : Partial updates given fields of an existing paziente, field will ignore if it is null
     *
     * @param id the id of the paziente to save.
     * @param paziente the paziente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paziente,
     * or with status {@code 400 (Bad Request)} if the paziente is not valid,
     * or with status {@code 404 (Not Found)} if the paziente is not found,
     * or with status {@code 500 (Internal Server Error)} if the paziente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pazientes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Paziente> partialUpdatePaziente(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Paziente paziente
    ) throws URISyntaxException {
        log.debug("REST request to partial update Paziente partially : {}, {}", id, paziente);
        if (paziente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paziente.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pazienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Paziente> result = pazienteRepository
            .findById(paziente.getId())
            .map(
                existingPaziente -> {
                    if (paziente.getCodiceFiscale() != null) {
                        existingPaziente.setCodiceFiscale(paziente.getCodiceFiscale());
                    }
                    if (paziente.getTesseraSanitaria() != null) {
                        existingPaziente.setTesseraSanitaria(paziente.getTesseraSanitaria());
                    }
                    if (paziente.getNome() != null) {
                        existingPaziente.setNome(paziente.getNome());
                    }
                    if (paziente.getCognome() != null) {
                        existingPaziente.setCognome(paziente.getCognome());
                    }
                    if (paziente.getDataNascita() != null) {
                        existingPaziente.setDataNascita(paziente.getDataNascita());
                    }
                    if (paziente.getLuogoNascita() != null) {
                        existingPaziente.setLuogoNascita(paziente.getLuogoNascita());
                    }
                    if (paziente.getLuogoResidenza() != null) {
                        existingPaziente.setLuogoResidenza(paziente.getLuogoResidenza());
                    }
                    if (paziente.getEmail() != null) {
                        existingPaziente.setEmail(paziente.getEmail());
                    }
                    if (paziente.getTelefono() != null) {
                        existingPaziente.setTelefono(paziente.getTelefono());
                    }
                    if (paziente.getPassword() != null) {
                        existingPaziente.setPassword(paziente.getPassword());
                    }

                    return existingPaziente;
                }
            )
            .map(pazienteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paziente.getId().toString())
        );
    }

    /**
     * {@code GET  /pazientes} : get all the pazientes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pazientes in body.
     */
    @GetMapping("/pazientes")
    public List<Paziente> getAllPazientes() {
        log.debug("REST request to get all Pazientes");
        return pazienteRepository.findAll();
    }

    /**
     * {@code GET  /pazientes/:id} : get the "id" paziente.
     *
     * @param id the id of the paziente to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paziente, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pazientes/{id}")
    public ResponseEntity<Paziente> getPaziente(@PathVariable Long id) {
        log.debug("REST request to get Paziente : {}", id);
        Optional<Paziente> paziente = pazienteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(paziente);
    }

    /**
     * {@code DELETE  /pazientes/:id} : delete the "id" paziente.
     *
     * @param id the id of the paziente to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pazientes/{id}")
    public ResponseEntity<Void> deletePaziente(@PathVariable Long id) {
        log.debug("REST request to delete Paziente : {}", id);
        pazienteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
