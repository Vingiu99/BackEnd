package com.myapp.web.rest;

import static com.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Paziente;
import com.myapp.repository.PazienteRepository;
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
 * Integration tests for the {@link PazienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PazienteResourceIT {

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

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pazientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PazienteRepository pazienteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPazienteMockMvc;

    private Paziente paziente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paziente createEntity(EntityManager em) {
        Paziente paziente = new Paziente()
            .codiceFiscale(DEFAULT_CODICE_FISCALE)
            .tesseraSanitaria(DEFAULT_TESSERA_SANITARIA)
            .nome(DEFAULT_NOME)
            .cognome(DEFAULT_COGNOME)
            .dataNascita(DEFAULT_DATA_NASCITA)
            .luogoNascita(DEFAULT_LUOGO_NASCITA)
            .luogoResidenza(DEFAULT_LUOGO_RESIDENZA)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO)
            .password(DEFAULT_PASSWORD);
        return paziente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paziente createUpdatedEntity(EntityManager em) {
        Paziente paziente = new Paziente()
            .codiceFiscale(UPDATED_CODICE_FISCALE)
            .tesseraSanitaria(UPDATED_TESSERA_SANITARIA)
            .nome(UPDATED_NOME)
            .cognome(UPDATED_COGNOME)
            .dataNascita(UPDATED_DATA_NASCITA)
            .luogoNascita(UPDATED_LUOGO_NASCITA)
            .luogoResidenza(UPDATED_LUOGO_RESIDENZA)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .password(UPDATED_PASSWORD);
        return paziente;
    }

    @BeforeEach
    public void initTest() {
        paziente = createEntity(em);
    }

    @Test
    @Transactional
    void createPaziente() throws Exception {
        int databaseSizeBeforeCreate = pazienteRepository.findAll().size();
        // Create the Paziente
        restPazienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paziente)))
            .andExpect(status().isCreated());

        // Validate the Paziente in the database
        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeCreate + 1);
        Paziente testPaziente = pazienteList.get(pazienteList.size() - 1);
        assertThat(testPaziente.getCodiceFiscale()).isEqualTo(DEFAULT_CODICE_FISCALE);
        assertThat(testPaziente.getTesseraSanitaria()).isEqualTo(DEFAULT_TESSERA_SANITARIA);
        assertThat(testPaziente.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPaziente.getCognome()).isEqualTo(DEFAULT_COGNOME);
        assertThat(testPaziente.getDataNascita()).isEqualTo(DEFAULT_DATA_NASCITA);
        assertThat(testPaziente.getLuogoNascita()).isEqualTo(DEFAULT_LUOGO_NASCITA);
        assertThat(testPaziente.getLuogoResidenza()).isEqualTo(DEFAULT_LUOGO_RESIDENZA);
        assertThat(testPaziente.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPaziente.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testPaziente.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void createPazienteWithExistingId() throws Exception {
        // Create the Paziente with an existing ID
        paziente.setId(1L);

        int databaseSizeBeforeCreate = pazienteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPazienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paziente)))
            .andExpect(status().isBadRequest());

        // Validate the Paziente in the database
        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodiceFiscaleIsRequired() throws Exception {
        int databaseSizeBeforeTest = pazienteRepository.findAll().size();
        // set the field null
        paziente.setCodiceFiscale(null);

        // Create the Paziente, which fails.

        restPazienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paziente)))
            .andExpect(status().isBadRequest());

        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTesseraSanitariaIsRequired() throws Exception {
        int databaseSizeBeforeTest = pazienteRepository.findAll().size();
        // set the field null
        paziente.setTesseraSanitaria(null);

        // Create the Paziente, which fails.

        restPazienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paziente)))
            .andExpect(status().isBadRequest());

        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pazienteRepository.findAll().size();
        // set the field null
        paziente.setNome(null);

        // Create the Paziente, which fails.

        restPazienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paziente)))
            .andExpect(status().isBadRequest());

        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCognomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pazienteRepository.findAll().size();
        // set the field null
        paziente.setCognome(null);

        // Create the Paziente, which fails.

        restPazienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paziente)))
            .andExpect(status().isBadRequest());

        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataNascitaIsRequired() throws Exception {
        int databaseSizeBeforeTest = pazienteRepository.findAll().size();
        // set the field null
        paziente.setDataNascita(null);

        // Create the Paziente, which fails.

        restPazienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paziente)))
            .andExpect(status().isBadRequest());

        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLuogoNascitaIsRequired() throws Exception {
        int databaseSizeBeforeTest = pazienteRepository.findAll().size();
        // set the field null
        paziente.setLuogoNascita(null);

        // Create the Paziente, which fails.

        restPazienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paziente)))
            .andExpect(status().isBadRequest());

        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLuogoResidenzaIsRequired() throws Exception {
        int databaseSizeBeforeTest = pazienteRepository.findAll().size();
        // set the field null
        paziente.setLuogoResidenza(null);

        // Create the Paziente, which fails.

        restPazienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paziente)))
            .andExpect(status().isBadRequest());

        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = pazienteRepository.findAll().size();
        // set the field null
        paziente.setEmail(null);

        // Create the Paziente, which fails.

        restPazienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paziente)))
            .andExpect(status().isBadRequest());

        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pazienteRepository.findAll().size();
        // set the field null
        paziente.setTelefono(null);

        // Create the Paziente, which fails.

        restPazienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paziente)))
            .andExpect(status().isBadRequest());

        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = pazienteRepository.findAll().size();
        // set the field null
        paziente.setPassword(null);

        // Create the Paziente, which fails.

        restPazienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paziente)))
            .andExpect(status().isBadRequest());

        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPazientes() throws Exception {
        // Initialize the database
        pazienteRepository.saveAndFlush(paziente);

        // Get all the pazienteList
        restPazienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paziente.getId().intValue())))
            .andExpect(jsonPath("$.[*].codiceFiscale").value(hasItem(DEFAULT_CODICE_FISCALE)))
            .andExpect(jsonPath("$.[*].tesseraSanitaria").value(hasItem(DEFAULT_TESSERA_SANITARIA)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cognome").value(hasItem(DEFAULT_COGNOME)))
            .andExpect(jsonPath("$.[*].dataNascita").value(hasItem(sameInstant(DEFAULT_DATA_NASCITA))))
            .andExpect(jsonPath("$.[*].luogoNascita").value(hasItem(DEFAULT_LUOGO_NASCITA)))
            .andExpect(jsonPath("$.[*].luogoResidenza").value(hasItem(DEFAULT_LUOGO_RESIDENZA)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    void getPaziente() throws Exception {
        // Initialize the database
        pazienteRepository.saveAndFlush(paziente);

        // Get the paziente
        restPazienteMockMvc
            .perform(get(ENTITY_API_URL_ID, paziente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paziente.getId().intValue()))
            .andExpect(jsonPath("$.codiceFiscale").value(DEFAULT_CODICE_FISCALE))
            .andExpect(jsonPath("$.tesseraSanitaria").value(DEFAULT_TESSERA_SANITARIA))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cognome").value(DEFAULT_COGNOME))
            .andExpect(jsonPath("$.dataNascita").value(sameInstant(DEFAULT_DATA_NASCITA)))
            .andExpect(jsonPath("$.luogoNascita").value(DEFAULT_LUOGO_NASCITA))
            .andExpect(jsonPath("$.luogoResidenza").value(DEFAULT_LUOGO_RESIDENZA))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getNonExistingPaziente() throws Exception {
        // Get the paziente
        restPazienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPaziente() throws Exception {
        // Initialize the database
        pazienteRepository.saveAndFlush(paziente);

        int databaseSizeBeforeUpdate = pazienteRepository.findAll().size();

        // Update the paziente
        Paziente updatedPaziente = pazienteRepository.findById(paziente.getId()).get();
        // Disconnect from session so that the updates on updatedPaziente are not directly saved in db
        em.detach(updatedPaziente);
        updatedPaziente
            .codiceFiscale(UPDATED_CODICE_FISCALE)
            .tesseraSanitaria(UPDATED_TESSERA_SANITARIA)
            .nome(UPDATED_NOME)
            .cognome(UPDATED_COGNOME)
            .dataNascita(UPDATED_DATA_NASCITA)
            .luogoNascita(UPDATED_LUOGO_NASCITA)
            .luogoResidenza(UPDATED_LUOGO_RESIDENZA)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .password(UPDATED_PASSWORD);

        restPazienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaziente.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPaziente))
            )
            .andExpect(status().isOk());

        // Validate the Paziente in the database
        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeUpdate);
        Paziente testPaziente = pazienteList.get(pazienteList.size() - 1);
        assertThat(testPaziente.getCodiceFiscale()).isEqualTo(UPDATED_CODICE_FISCALE);
        assertThat(testPaziente.getTesseraSanitaria()).isEqualTo(UPDATED_TESSERA_SANITARIA);
        assertThat(testPaziente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPaziente.getCognome()).isEqualTo(UPDATED_COGNOME);
        assertThat(testPaziente.getDataNascita()).isEqualTo(UPDATED_DATA_NASCITA);
        assertThat(testPaziente.getLuogoNascita()).isEqualTo(UPDATED_LUOGO_NASCITA);
        assertThat(testPaziente.getLuogoResidenza()).isEqualTo(UPDATED_LUOGO_RESIDENZA);
        assertThat(testPaziente.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPaziente.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testPaziente.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void putNonExistingPaziente() throws Exception {
        int databaseSizeBeforeUpdate = pazienteRepository.findAll().size();
        paziente.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPazienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paziente.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paziente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paziente in the database
        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaziente() throws Exception {
        int databaseSizeBeforeUpdate = pazienteRepository.findAll().size();
        paziente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPazienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paziente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paziente in the database
        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaziente() throws Exception {
        int databaseSizeBeforeUpdate = pazienteRepository.findAll().size();
        paziente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPazienteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paziente)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paziente in the database
        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePazienteWithPatch() throws Exception {
        // Initialize the database
        pazienteRepository.saveAndFlush(paziente);

        int databaseSizeBeforeUpdate = pazienteRepository.findAll().size();

        // Update the paziente using partial update
        Paziente partialUpdatedPaziente = new Paziente();
        partialUpdatedPaziente.setId(paziente.getId());

        partialUpdatedPaziente
            .tesseraSanitaria(UPDATED_TESSERA_SANITARIA)
            .dataNascita(UPDATED_DATA_NASCITA)
            .luogoNascita(UPDATED_LUOGO_NASCITA)
            .luogoResidenza(UPDATED_LUOGO_RESIDENZA)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD);

        restPazienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaziente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaziente))
            )
            .andExpect(status().isOk());

        // Validate the Paziente in the database
        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeUpdate);
        Paziente testPaziente = pazienteList.get(pazienteList.size() - 1);
        assertThat(testPaziente.getCodiceFiscale()).isEqualTo(DEFAULT_CODICE_FISCALE);
        assertThat(testPaziente.getTesseraSanitaria()).isEqualTo(UPDATED_TESSERA_SANITARIA);
        assertThat(testPaziente.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPaziente.getCognome()).isEqualTo(DEFAULT_COGNOME);
        assertThat(testPaziente.getDataNascita()).isEqualTo(UPDATED_DATA_NASCITA);
        assertThat(testPaziente.getLuogoNascita()).isEqualTo(UPDATED_LUOGO_NASCITA);
        assertThat(testPaziente.getLuogoResidenza()).isEqualTo(UPDATED_LUOGO_RESIDENZA);
        assertThat(testPaziente.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPaziente.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testPaziente.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void fullUpdatePazienteWithPatch() throws Exception {
        // Initialize the database
        pazienteRepository.saveAndFlush(paziente);

        int databaseSizeBeforeUpdate = pazienteRepository.findAll().size();

        // Update the paziente using partial update
        Paziente partialUpdatedPaziente = new Paziente();
        partialUpdatedPaziente.setId(paziente.getId());

        partialUpdatedPaziente
            .codiceFiscale(UPDATED_CODICE_FISCALE)
            .tesseraSanitaria(UPDATED_TESSERA_SANITARIA)
            .nome(UPDATED_NOME)
            .cognome(UPDATED_COGNOME)
            .dataNascita(UPDATED_DATA_NASCITA)
            .luogoNascita(UPDATED_LUOGO_NASCITA)
            .luogoResidenza(UPDATED_LUOGO_RESIDENZA)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .password(UPDATED_PASSWORD);

        restPazienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaziente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaziente))
            )
            .andExpect(status().isOk());

        // Validate the Paziente in the database
        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeUpdate);
        Paziente testPaziente = pazienteList.get(pazienteList.size() - 1);
        assertThat(testPaziente.getCodiceFiscale()).isEqualTo(UPDATED_CODICE_FISCALE);
        assertThat(testPaziente.getTesseraSanitaria()).isEqualTo(UPDATED_TESSERA_SANITARIA);
        assertThat(testPaziente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPaziente.getCognome()).isEqualTo(UPDATED_COGNOME);
        assertThat(testPaziente.getDataNascita()).isEqualTo(UPDATED_DATA_NASCITA);
        assertThat(testPaziente.getLuogoNascita()).isEqualTo(UPDATED_LUOGO_NASCITA);
        assertThat(testPaziente.getLuogoResidenza()).isEqualTo(UPDATED_LUOGO_RESIDENZA);
        assertThat(testPaziente.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPaziente.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testPaziente.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void patchNonExistingPaziente() throws Exception {
        int databaseSizeBeforeUpdate = pazienteRepository.findAll().size();
        paziente.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPazienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paziente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paziente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paziente in the database
        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaziente() throws Exception {
        int databaseSizeBeforeUpdate = pazienteRepository.findAll().size();
        paziente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPazienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paziente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paziente in the database
        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaziente() throws Exception {
        int databaseSizeBeforeUpdate = pazienteRepository.findAll().size();
        paziente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPazienteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paziente)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paziente in the database
        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaziente() throws Exception {
        // Initialize the database
        pazienteRepository.saveAndFlush(paziente);

        int databaseSizeBeforeDelete = pazienteRepository.findAll().size();

        // Delete the paziente
        restPazienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, paziente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Paziente> pazienteList = pazienteRepository.findAll();
        assertThat(pazienteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
