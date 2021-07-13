package com.myapp.web.rest;

import static com.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Prenotazione;
import com.myapp.repository.PrenotazioneRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PrenotazioneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrenotazioneResourceIT {

    private static final String DEFAULT_CODICE_FISCALE = "AAAAAAAAAA";
    private static final String UPDATED_CODICE_FISCALE = "BBBBBBBBBB";

    private static final String DEFAULT_TESSERA_SANITARIA = "AAAAAAAAAA";
    private static final String UPDATED_TESSERA_SANITARIA = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_COGNOME = "AAAAAAAAAA";
    private static final String UPDATED_COGNOME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_NASCITA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_NASCITA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LUOGO_NASCITA = "AAAAAAAAAA";
    private static final String UPDATED_LUOGO_NASCITA = "BBBBBBBBBB";

    private static final String DEFAULT_LUOGO_RESIDENZA = "AAAAAAAAAA";
    private static final String UPDATED_LUOGO_RESIDENZA = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_LUOGO_VACCINO = "AAAAAAAAAA";
    private static final String UPDATED_LUOGO_VACCINO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_VACCINO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_VACCINO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/prenotaziones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrenotazioneMockMvc;

    private Prenotazione prenotazione;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prenotazione createEntity(EntityManager em) {
        Prenotazione prenotazione = new Prenotazione()
            .codiceFiscale(DEFAULT_CODICE_FISCALE)
            .tesseraSanitaria(DEFAULT_TESSERA_SANITARIA)
            .nome(DEFAULT_NOME)
            .cognome(DEFAULT_COGNOME)
            .dataNascita(DEFAULT_DATA_NASCITA)
            .luogoNascita(DEFAULT_LUOGO_NASCITA)
            .luogoResidenza(DEFAULT_LUOGO_RESIDENZA)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO)
            .luogoVaccino(DEFAULT_LUOGO_VACCINO)
            .dataVaccino(DEFAULT_DATA_VACCINO);
        return prenotazione;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prenotazione createUpdatedEntity(EntityManager em) {
        Prenotazione prenotazione = new Prenotazione()
            .codiceFiscale(UPDATED_CODICE_FISCALE)
            .tesseraSanitaria(UPDATED_TESSERA_SANITARIA)
            .nome(UPDATED_NOME)
            .cognome(UPDATED_COGNOME)
            .dataNascita(UPDATED_DATA_NASCITA)
            .luogoNascita(UPDATED_LUOGO_NASCITA)
            .luogoResidenza(UPDATED_LUOGO_RESIDENZA)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .luogoVaccino(UPDATED_LUOGO_VACCINO)
            .dataVaccino(UPDATED_DATA_VACCINO);
        return prenotazione;
    }

    @BeforeEach
    public void initTest() {
        prenotazione = createEntity(em);
    }

    @Test
    @Transactional
    void createPrenotazione() throws Exception {
        int databaseSizeBeforeCreate = prenotazioneRepository.findAll().size();
        // Create the Prenotazione
        restPrenotazioneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prenotazione)))
            .andExpect(status().isCreated());

        // Validate the Prenotazione in the database
        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeCreate + 1);
        Prenotazione testPrenotazione = prenotazioneList.get(prenotazioneList.size() - 1);
        assertThat(testPrenotazione.getCodiceFiscale()).isEqualTo(DEFAULT_CODICE_FISCALE);
        assertThat(testPrenotazione.getTesseraSanitaria()).isEqualTo(DEFAULT_TESSERA_SANITARIA);
        assertThat(testPrenotazione.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPrenotazione.getCognome()).isEqualTo(DEFAULT_COGNOME);
        assertThat(testPrenotazione.getDataNascita()).isEqualTo(DEFAULT_DATA_NASCITA);
        assertThat(testPrenotazione.getLuogoNascita()).isEqualTo(DEFAULT_LUOGO_NASCITA);
        assertThat(testPrenotazione.getLuogoResidenza()).isEqualTo(DEFAULT_LUOGO_RESIDENZA);
        assertThat(testPrenotazione.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPrenotazione.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testPrenotazione.getLuogoVaccino()).isEqualTo(DEFAULT_LUOGO_VACCINO);
        assertThat(testPrenotazione.getDataVaccino()).isEqualTo(DEFAULT_DATA_VACCINO);
    }

    @Test
    @Transactional
    void createPrenotazioneWithExistingId() throws Exception {
        // Create the Prenotazione with an existing ID
        prenotazione.setId(1L);

        int databaseSizeBeforeCreate = prenotazioneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrenotazioneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prenotazione)))
            .andExpect(status().isBadRequest());

        // Validate the Prenotazione in the database
        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodiceFiscaleIsRequired() throws Exception {
        int databaseSizeBeforeTest = prenotazioneRepository.findAll().size();
        // set the field null
        prenotazione.setCodiceFiscale(null);

        // Create the Prenotazione, which fails.

        restPrenotazioneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prenotazione)))
            .andExpect(status().isBadRequest());

        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTesseraSanitariaIsRequired() throws Exception {
        int databaseSizeBeforeTest = prenotazioneRepository.findAll().size();
        // set the field null
        prenotazione.setTesseraSanitaria(null);

        // Create the Prenotazione, which fails.

        restPrenotazioneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prenotazione)))
            .andExpect(status().isBadRequest());

        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = prenotazioneRepository.findAll().size();
        // set the field null
        prenotazione.setNome(null);

        // Create the Prenotazione, which fails.

        restPrenotazioneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prenotazione)))
            .andExpect(status().isBadRequest());

        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCognomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = prenotazioneRepository.findAll().size();
        // set the field null
        prenotazione.setCognome(null);

        // Create the Prenotazione, which fails.

        restPrenotazioneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prenotazione)))
            .andExpect(status().isBadRequest());

        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataNascitaIsRequired() throws Exception {
        int databaseSizeBeforeTest = prenotazioneRepository.findAll().size();
        // set the field null
        prenotazione.setDataNascita(null);

        // Create the Prenotazione, which fails.

        restPrenotazioneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prenotazione)))
            .andExpect(status().isBadRequest());

        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLuogoNascitaIsRequired() throws Exception {
        int databaseSizeBeforeTest = prenotazioneRepository.findAll().size();
        // set the field null
        prenotazione.setLuogoNascita(null);

        // Create the Prenotazione, which fails.

        restPrenotazioneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prenotazione)))
            .andExpect(status().isBadRequest());

        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLuogoResidenzaIsRequired() throws Exception {
        int databaseSizeBeforeTest = prenotazioneRepository.findAll().size();
        // set the field null
        prenotazione.setLuogoResidenza(null);

        // Create the Prenotazione, which fails.

        restPrenotazioneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prenotazione)))
            .andExpect(status().isBadRequest());

        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = prenotazioneRepository.findAll().size();
        // set the field null
        prenotazione.setEmail(null);

        // Create the Prenotazione, which fails.

        restPrenotazioneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prenotazione)))
            .andExpect(status().isBadRequest());

        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoIsRequired() throws Exception {
        int databaseSizeBeforeTest = prenotazioneRepository.findAll().size();
        // set the field null
        prenotazione.setTelefono(null);

        // Create the Prenotazione, which fails.

        restPrenotazioneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prenotazione)))
            .andExpect(status().isBadRequest());

        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLuogoVaccinoIsRequired() throws Exception {
        int databaseSizeBeforeTest = prenotazioneRepository.findAll().size();
        // set the field null
        prenotazione.setLuogoVaccino(null);

        // Create the Prenotazione, which fails.

        restPrenotazioneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prenotazione)))
            .andExpect(status().isBadRequest());

        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataVaccinoIsRequired() throws Exception {
        int databaseSizeBeforeTest = prenotazioneRepository.findAll().size();
        // set the field null
        prenotazione.setDataVaccino(null);

        // Create the Prenotazione, which fails.

        restPrenotazioneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prenotazione)))
            .andExpect(status().isBadRequest());

        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrenotaziones() throws Exception {
        // Initialize the database
        prenotazioneRepository.saveAndFlush(prenotazione);

        // Get all the prenotazioneList
        restPrenotazioneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prenotazione.getId().intValue())))
            .andExpect(jsonPath("$.[*].codiceFiscale").value(hasItem(DEFAULT_CODICE_FISCALE)))
            .andExpect(jsonPath("$.[*].tesseraSanitaria").value(hasItem(DEFAULT_TESSERA_SANITARIA)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cognome").value(hasItem(DEFAULT_COGNOME)))
            .andExpect(jsonPath("$.[*].dataNascita").value(hasItem(sameInstant(DEFAULT_DATA_NASCITA))))
            .andExpect(jsonPath("$.[*].luogoNascita").value(hasItem(DEFAULT_LUOGO_NASCITA)))
            .andExpect(jsonPath("$.[*].luogoResidenza").value(hasItem(DEFAULT_LUOGO_RESIDENZA)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].luogoVaccino").value(hasItem(DEFAULT_LUOGO_VACCINO)))
            .andExpect(jsonPath("$.[*].dataVaccino").value(hasItem(sameInstant(DEFAULT_DATA_VACCINO))));
    }

    @Test
    @Transactional
    void getPrenotazione() throws Exception {
        // Initialize the database
        prenotazioneRepository.saveAndFlush(prenotazione);

        // Get the prenotazione
        restPrenotazioneMockMvc
            .perform(get(ENTITY_API_URL_ID, prenotazione.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prenotazione.getId().intValue()))
            .andExpect(jsonPath("$.codiceFiscale").value(DEFAULT_CODICE_FISCALE))
            .andExpect(jsonPath("$.tesseraSanitaria").value(DEFAULT_TESSERA_SANITARIA))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cognome").value(DEFAULT_COGNOME))
            .andExpect(jsonPath("$.dataNascita").value(sameInstant(DEFAULT_DATA_NASCITA)))
            .andExpect(jsonPath("$.luogoNascita").value(DEFAULT_LUOGO_NASCITA))
            .andExpect(jsonPath("$.luogoResidenza").value(DEFAULT_LUOGO_RESIDENZA))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.luogoVaccino").value(DEFAULT_LUOGO_VACCINO))
            .andExpect(jsonPath("$.dataVaccino").value(sameInstant(DEFAULT_DATA_VACCINO)));
    }

    @Test
    @Transactional
    void getNonExistingPrenotazione() throws Exception {
        // Get the prenotazione
        restPrenotazioneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrenotazione() throws Exception {
        // Initialize the database
        prenotazioneRepository.saveAndFlush(prenotazione);

        int databaseSizeBeforeUpdate = prenotazioneRepository.findAll().size();

        // Update the prenotazione
        Prenotazione updatedPrenotazione = prenotazioneRepository.findById(prenotazione.getId()).get();
        // Disconnect from session so that the updates on updatedPrenotazione are not directly saved in db
        em.detach(updatedPrenotazione);
        updatedPrenotazione
            .codiceFiscale(UPDATED_CODICE_FISCALE)
            .tesseraSanitaria(UPDATED_TESSERA_SANITARIA)
            .nome(UPDATED_NOME)
            .cognome(UPDATED_COGNOME)
            .dataNascita(UPDATED_DATA_NASCITA)
            .luogoNascita(UPDATED_LUOGO_NASCITA)
            .luogoResidenza(UPDATED_LUOGO_RESIDENZA)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .luogoVaccino(UPDATED_LUOGO_VACCINO)
            .dataVaccino(UPDATED_DATA_VACCINO);

        restPrenotazioneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrenotazione.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrenotazione))
            )
            .andExpect(status().isOk());

        // Validate the Prenotazione in the database
        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeUpdate);
        Prenotazione testPrenotazione = prenotazioneList.get(prenotazioneList.size() - 1);
        assertThat(testPrenotazione.getCodiceFiscale()).isEqualTo(UPDATED_CODICE_FISCALE);
        assertThat(testPrenotazione.getTesseraSanitaria()).isEqualTo(UPDATED_TESSERA_SANITARIA);
        assertThat(testPrenotazione.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPrenotazione.getCognome()).isEqualTo(UPDATED_COGNOME);
        assertThat(testPrenotazione.getDataNascita()).isEqualTo(UPDATED_DATA_NASCITA);
        assertThat(testPrenotazione.getLuogoNascita()).isEqualTo(UPDATED_LUOGO_NASCITA);
        assertThat(testPrenotazione.getLuogoResidenza()).isEqualTo(UPDATED_LUOGO_RESIDENZA);
        assertThat(testPrenotazione.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPrenotazione.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testPrenotazione.getLuogoVaccino()).isEqualTo(UPDATED_LUOGO_VACCINO);
        assertThat(testPrenotazione.getDataVaccino()).isEqualTo(UPDATED_DATA_VACCINO);
    }

    @Test
    @Transactional
    void putNonExistingPrenotazione() throws Exception {
        int databaseSizeBeforeUpdate = prenotazioneRepository.findAll().size();
        prenotazione.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrenotazioneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prenotazione.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prenotazione))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prenotazione in the database
        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrenotazione() throws Exception {
        int databaseSizeBeforeUpdate = prenotazioneRepository.findAll().size();
        prenotazione.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrenotazioneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prenotazione))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prenotazione in the database
        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrenotazione() throws Exception {
        int databaseSizeBeforeUpdate = prenotazioneRepository.findAll().size();
        prenotazione.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrenotazioneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prenotazione)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prenotazione in the database
        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrenotazioneWithPatch() throws Exception {
        // Initialize the database
        prenotazioneRepository.saveAndFlush(prenotazione);

        int databaseSizeBeforeUpdate = prenotazioneRepository.findAll().size();

        // Update the prenotazione using partial update
        Prenotazione partialUpdatedPrenotazione = new Prenotazione();
        partialUpdatedPrenotazione.setId(prenotazione.getId());

        partialUpdatedPrenotazione
            .nome(UPDATED_NOME)
            .luogoResidenza(UPDATED_LUOGO_RESIDENZA)
            .email(UPDATED_EMAIL)
            .luogoVaccino(UPDATED_LUOGO_VACCINO);

        restPrenotazioneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrenotazione.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrenotazione))
            )
            .andExpect(status().isOk());

        // Validate the Prenotazione in the database
        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeUpdate);
        Prenotazione testPrenotazione = prenotazioneList.get(prenotazioneList.size() - 1);
        assertThat(testPrenotazione.getCodiceFiscale()).isEqualTo(DEFAULT_CODICE_FISCALE);
        assertThat(testPrenotazione.getTesseraSanitaria()).isEqualTo(DEFAULT_TESSERA_SANITARIA);
        assertThat(testPrenotazione.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPrenotazione.getCognome()).isEqualTo(DEFAULT_COGNOME);
        assertThat(testPrenotazione.getDataNascita()).isEqualTo(DEFAULT_DATA_NASCITA);
        assertThat(testPrenotazione.getLuogoNascita()).isEqualTo(DEFAULT_LUOGO_NASCITA);
        assertThat(testPrenotazione.getLuogoResidenza()).isEqualTo(UPDATED_LUOGO_RESIDENZA);
        assertThat(testPrenotazione.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPrenotazione.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testPrenotazione.getLuogoVaccino()).isEqualTo(UPDATED_LUOGO_VACCINO);
        assertThat(testPrenotazione.getDataVaccino()).isEqualTo(DEFAULT_DATA_VACCINO);
    }

    @Test
    @Transactional
    void fullUpdatePrenotazioneWithPatch() throws Exception {
        // Initialize the database
        prenotazioneRepository.saveAndFlush(prenotazione);

        int databaseSizeBeforeUpdate = prenotazioneRepository.findAll().size();

        // Update the prenotazione using partial update
        Prenotazione partialUpdatedPrenotazione = new Prenotazione();
        partialUpdatedPrenotazione.setId(prenotazione.getId());

        partialUpdatedPrenotazione
            .codiceFiscale(UPDATED_CODICE_FISCALE)
            .tesseraSanitaria(UPDATED_TESSERA_SANITARIA)
            .nome(UPDATED_NOME)
            .cognome(UPDATED_COGNOME)
            .dataNascita(UPDATED_DATA_NASCITA)
            .luogoNascita(UPDATED_LUOGO_NASCITA)
            .luogoResidenza(UPDATED_LUOGO_RESIDENZA)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .luogoVaccino(UPDATED_LUOGO_VACCINO)
            .dataVaccino(UPDATED_DATA_VACCINO);

        restPrenotazioneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrenotazione.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrenotazione))
            )
            .andExpect(status().isOk());

        // Validate the Prenotazione in the database
        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeUpdate);
        Prenotazione testPrenotazione = prenotazioneList.get(prenotazioneList.size() - 1);
        assertThat(testPrenotazione.getCodiceFiscale()).isEqualTo(UPDATED_CODICE_FISCALE);
        assertThat(testPrenotazione.getTesseraSanitaria()).isEqualTo(UPDATED_TESSERA_SANITARIA);
        assertThat(testPrenotazione.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPrenotazione.getCognome()).isEqualTo(UPDATED_COGNOME);
        assertThat(testPrenotazione.getDataNascita()).isEqualTo(UPDATED_DATA_NASCITA);
        assertThat(testPrenotazione.getLuogoNascita()).isEqualTo(UPDATED_LUOGO_NASCITA);
        assertThat(testPrenotazione.getLuogoResidenza()).isEqualTo(UPDATED_LUOGO_RESIDENZA);
        assertThat(testPrenotazione.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPrenotazione.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testPrenotazione.getLuogoVaccino()).isEqualTo(UPDATED_LUOGO_VACCINO);
        assertThat(testPrenotazione.getDataVaccino()).isEqualTo(UPDATED_DATA_VACCINO);
    }

    @Test
    @Transactional
    void patchNonExistingPrenotazione() throws Exception {
        int databaseSizeBeforeUpdate = prenotazioneRepository.findAll().size();
        prenotazione.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrenotazioneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prenotazione.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prenotazione))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prenotazione in the database
        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrenotazione() throws Exception {
        int databaseSizeBeforeUpdate = prenotazioneRepository.findAll().size();
        prenotazione.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrenotazioneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prenotazione))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prenotazione in the database
        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrenotazione() throws Exception {
        int databaseSizeBeforeUpdate = prenotazioneRepository.findAll().size();
        prenotazione.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrenotazioneMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(prenotazione))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prenotazione in the database
        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrenotazione() throws Exception {
        // Initialize the database
        prenotazioneRepository.saveAndFlush(prenotazione);

        int databaseSizeBeforeDelete = prenotazioneRepository.findAll().size();

        // Delete the prenotazione
        restPrenotazioneMockMvc
            .perform(delete(ENTITY_API_URL_ID, prenotazione.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Prenotazione> prenotazioneList = prenotazioneRepository.findAll();
        assertThat(prenotazioneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
