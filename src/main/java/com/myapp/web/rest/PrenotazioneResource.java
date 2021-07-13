package com.myapp.web.rest;

import com.myapp.domain.Prenotazione;
import com.myapp.repository.PrenotazioneRepository;
import com.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.myapp.domain.Prenotazione}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PrenotazioneResource {

    private final Logger log = LoggerFactory.getLogger(PrenotazioneResource.class);

    private static final String ENTITY_NAME = "prenotazione";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrenotazioneRepository prenotazioneRepository;

    public PrenotazioneResource(PrenotazioneRepository prenotazioneRepository) {
        this.prenotazioneRepository = prenotazioneRepository;
    }

    /**
     * {@code POST  /prenotaziones} : Create a new prenotazione.
     *
     * @param prenotazione the prenotazione to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prenotazione, or with status {@code 400 (Bad Request)} if the prenotazione has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prenotaziones")
    public ResponseEntity<Prenotazione> createPrenotazione(@Valid @RequestBody Prenotazione prenotazione) throws URISyntaxException {
        log.debug("REST request to save Prenotazione : {}", prenotazione);
        if (prenotazione.getId() != null) {
            throw new BadRequestAlertException("A new prenotazione cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Prenotazione result = prenotazioneRepository.save(prenotazione);
        return ResponseEntity
            .created(new URI("/api/prenotaziones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prenotaziones/:id} : Updates an existing prenotazione.
     *
     * @param id the id of the prenotazione to save.
     * @param prenotazione the prenotazione to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prenotazione,
     * or with status {@code 400 (Bad Request)} if the prenotazione is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prenotazione couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prenotaziones/{id}")
    public ResponseEntity<Prenotazione> updatePrenotazione(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Prenotazione prenotazione
    ) throws URISyntaxException {
        log.debug("REST request to update Prenotazione : {}, {}", id, prenotazione);
        if (prenotazione.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prenotazione.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prenotazioneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Prenotazione result = prenotazioneRepository.save(prenotazione);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prenotazione.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prenotaziones/:id} : Partial updates given fields of an existing prenotazione, field will ignore if it is null
     *
     * @param id the id of the prenotazione to save.
     * @param prenotazione the prenotazione to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prenotazione,
     * or with status {@code 400 (Bad Request)} if the prenotazione is not valid,
     * or with status {@code 404 (Not Found)} if the prenotazione is not found,
     * or with status {@code 500 (Internal Server Error)} if the prenotazione couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prenotaziones/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Prenotazione> partialUpdatePrenotazione(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Prenotazione prenotazione
    ) throws URISyntaxException {
        log.debug("REST request to partial update Prenotazione partially : {}, {}", id, prenotazione);
        if (prenotazione.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prenotazione.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prenotazioneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Prenotazione> result = prenotazioneRepository
            .findById(prenotazione.getId())
            .map(
                existingPrenotazione -> {
                    if (prenotazione.getCodiceFiscale() != null) {
                        existingPrenotazione.setCodiceFiscale(prenotazione.getCodiceFiscale());
                    }
                    if (prenotazione.getTesseraSanitaria() != null) {
                        existingPrenotazione.setTesseraSanitaria(prenotazione.getTesseraSanitaria());
                    }
                    if (prenotazione.getNome() != null) {
                        existingPrenotazione.setNome(prenotazione.getNome());
                    }
                    if (prenotazione.getCognome() != null) {
                        existingPrenotazione.setCognome(prenotazione.getCognome());
                    }
                    if (prenotazione.getDataNascita() != null) {
                        existingPrenotazione.setDataNascita(prenotazione.getDataNascita());
                    }
                    if (prenotazione.getLuogoNascita() != null) {
                        existingPrenotazione.setLuogoNascita(prenotazione.getLuogoNascita());
                    }
                    if (prenotazione.getLuogoResidenza() != null) {
                        existingPrenotazione.setLuogoResidenza(prenotazione.getLuogoResidenza());
                    }
                    if (prenotazione.getEmail() != null) {
                        existingPrenotazione.setEmail(prenotazione.getEmail());
                    }
                    if (prenotazione.getTelefono() != null) {
                        existingPrenotazione.setTelefono(prenotazione.getTelefono());
                    }
                    if (prenotazione.getLuogoVaccino() != null) {
                        existingPrenotazione.setLuogoVaccino(prenotazione.getLuogoVaccino());
                    }
                    if (prenotazione.getDataVaccino() != null) {
                        existingPrenotazione.setDataVaccino(prenotazione.getDataVaccino());
                    }

                    return existingPrenotazione;
                }
            )
            .map(prenotazioneRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prenotazione.getId().toString())
        );
    }

    /**
     * {@code GET  /prenotaziones} : get all the prenotaziones.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prenotaziones in body.
     */
    @GetMapping("/prenotaziones")
    public List<Prenotazione> getAllPrenotaziones(@RequestParam(required = false) String filter) {
        if ("paziente-is-null".equals(filter)) {
            log.debug("REST request to get all Prenotaziones where paziente is null");
            return StreamSupport
                .stream(prenotazioneRepository.findAll().spliterator(), false)
                .filter(prenotazione -> prenotazione.getPaziente() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Prenotaziones");
        return prenotazioneRepository.findAll();
    }

    /**
     * {@code GET  /prenotaziones/:id} : get the "id" prenotazione.
     *
     * @param id the id of the prenotazione to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prenotazione, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prenotaziones/{id}")
    public ResponseEntity<Prenotazione> getPrenotazione(@PathVariable Long id) {
        log.debug("REST request to get Prenotazione : {}", id);
        Optional<Prenotazione> prenotazione = prenotazioneRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(prenotazione);
    }

    /**
     * {@code DELETE  /prenotaziones/:id} : delete the "id" prenotazione.
     *
     * @param id the id of the prenotazione to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prenotaziones/{id}")
    public ResponseEntity<Void> deletePrenotazione(@PathVariable Long id) {
        log.debug("REST request to delete Prenotazione : {}", id);
        prenotazioneRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
