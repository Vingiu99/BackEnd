package com.myapp.web.rest;

import static com.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Operatore;
import com.myapp.repository.OperatoreRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OperatoreResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OperatoreResourceIT {

    private static final String DEFAULT_ID_ASL_OPERATORE = "AAAAAAAAAA";
    private static final String UPDATED_ID_ASL_OPERATORE = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_COGNOME = "AAAAAAAAAA";
    private static final String UPDATED_COGNOME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_NASCITA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_NASCITA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LUOGO_NASCITA = "AAAAAAAAAA";
    private static final String UPDATED_LUOGO_NASCITA = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/operatores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OperatoreRepository operatoreRepository;

    @Mock
    private OperatoreRepository operatoreRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperatoreMockMvc;

    private Operatore operatore;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operatore createEntity(EntityManager em) {
        Operatore operatore = new Operatore()
            .idAslOperatore(DEFAULT_ID_ASL_OPERATORE)
            .nome(DEFAULT_NOME)
            .cognome(DEFAULT_COGNOME)
            .dataNascita(DEFAULT_DATA_NASCITA)
            .luogoNascita(DEFAULT_LUOGO_NASCITA)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO)
            .password(DEFAULT_PASSWORD);
        return operatore;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operatore createUpdatedEntity(EntityManager em) {
        Operatore operatore = new Operatore()
            .idAslOperatore(UPDATED_ID_ASL_OPERATORE)
            .nome(UPDATED_NOME)
            .cognome(UPDATED_COGNOME)
            .dataNascita(UPDATED_DATA_NASCITA)
            .luogoNascita(UPDATED_LUOGO_NASCITA)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .password(UPDATED_PASSWORD);
        return operatore;
    }

    @BeforeEach
    public void initTest() {
        operatore = createEntity(em);
    }

    @Test
    @Transactional
    void createOperatore() throws Exception {
        int databaseSizeBeforeCreate = operatoreRepository.findAll().size();
        // Create the Operatore
        restOperatoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatore)))
            .andExpect(status().isCreated());

        // Validate the Operatore in the database
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeCreate + 1);
        Operatore testOperatore = operatoreList.get(operatoreList.size() - 1);
        assertThat(testOperatore.getIdAslOperatore()).isEqualTo(DEFAULT_ID_ASL_OPERATORE);
        assertThat(testOperatore.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testOperatore.getCognome()).isEqualTo(DEFAULT_COGNOME);
        assertThat(testOperatore.getDataNascita()).isEqualTo(DEFAULT_DATA_NASCITA);
        assertThat(testOperatore.getLuogoNascita()).isEqualTo(DEFAULT_LUOGO_NASCITA);
        assertThat(testOperatore.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOperatore.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testOperatore.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void createOperatoreWithExistingId() throws Exception {
        // Create the Operatore with an existing ID
        operatore.setId(1L);

        int databaseSizeBeforeCreate = operatoreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperatoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatore)))
            .andExpect(status().isBadRequest());

        // Validate the Operatore in the database
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdAslOperatoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatoreRepository.findAll().size();
        // set the field null
        operatore.setIdAslOperatore(null);

        // Create the Operatore, which fails.

        restOperatoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatore)))
            .andExpect(status().isBadRequest());

        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatoreRepository.findAll().size();
        // set the field null
        operatore.setNome(null);

        // Create the Operatore, which fails.

        restOperatoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatore)))
            .andExpect(status().isBadRequest());

        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCognomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatoreRepository.findAll().size();
        // set the field null
        operatore.setCognome(null);

        // Create the Operatore, which fails.

        restOperatoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatore)))
            .andExpect(status().isBadRequest());

        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataNascitaIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatoreRepository.findAll().size();
        // set the field null
        operatore.setDataNascita(null);

        // Create the Operatore, which fails.

        restOperatoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatore)))
            .andExpect(status().isBadRequest());

        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLuogoNascitaIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatoreRepository.findAll().size();
        // set the field null
        operatore.setLuogoNascita(null);

        // Create the Operatore, which fails.

        restOperatoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatore)))
            .andExpect(status().isBadRequest());

        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatoreRepository.findAll().size();
        // set the field null
        operatore.setEmail(null);

        // Create the Operatore, which fails.

        restOperatoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatore)))
            .andExpect(status().isBadRequest());

        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatoreRepository.findAll().size();
        // set the field null
        operatore.setTelefono(null);

        // Create the Operatore, which fails.

        restOperatoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatore)))
            .andExpect(status().isBadRequest());

        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatoreRepository.findAll().size();
        // set the field null
        operatore.setPassword(null);

        // Create the Operatore, which fails.

        restOperatoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatore)))
            .andExpect(status().isBadRequest());

        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOperatores() throws Exception {
        // Initialize the database
        operatoreRepository.saveAndFlush(operatore);

        // Get all the operatoreList
        restOperatoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operatore.getId().intValue())))
            .andExpect(jsonPath("$.[*].idAslOperatore").value(hasItem(DEFAULT_ID_ASL_OPERATORE)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cognome").value(hasItem(DEFAULT_COGNOME)))
            .andExpect(jsonPath("$.[*].dataNascita").value(hasItem(sameInstant(DEFAULT_DATA_NASCITA))))
            .andExpect(jsonPath("$.[*].luogoNascita").value(hasItem(DEFAULT_LUOGO_NASCITA)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOperatoresWithEagerRelationshipsIsEnabled() throws Exception {
        when(operatoreRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOperatoreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(operatoreRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOperatoresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(operatoreRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOperatoreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(operatoreRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getOperatore() throws Exception {
        // Initialize the database
        operatoreRepository.saveAndFlush(operatore);

        // Get the operatore
        restOperatoreMockMvc
            .perform(get(ENTITY_API_URL_ID, operatore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operatore.getId().intValue()))
            .andExpect(jsonPath("$.idAslOperatore").value(DEFAULT_ID_ASL_OPERATORE))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cognome").value(DEFAULT_COGNOME))
            .andExpect(jsonPath("$.dataNascita").value(sameInstant(DEFAULT_DATA_NASCITA)))
            .andExpect(jsonPath("$.luogoNascita").value(DEFAULT_LUOGO_NASCITA))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getNonExistingOperatore() throws Exception {
        // Get the operatore
        restOperatoreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOperatore() throws Exception {
        // Initialize the database
        operatoreRepository.saveAndFlush(operatore);

        int databaseSizeBeforeUpdate = operatoreRepository.findAll().size();

        // Update the operatore
        Operatore updatedOperatore = operatoreRepository.findById(operatore.getId()).get();
        // Disconnect from session so that the updates on updatedOperatore are not directly saved in db
        em.detach(updatedOperatore);
        updatedOperatore
            .idAslOperatore(UPDATED_ID_ASL_OPERATORE)
            .nome(UPDATED_NOME)
            .cognome(UPDATED_COGNOME)
            .dataNascita(UPDATED_DATA_NASCITA)
            .luogoNascita(UPDATED_LUOGO_NASCITA)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .password(UPDATED_PASSWORD);

        restOperatoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOperatore.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOperatore))
            )
            .andExpect(status().isOk());

        // Validate the Operatore in the database
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeUpdate);
        Operatore testOperatore = operatoreList.get(operatoreList.size() - 1);
        assertThat(testOperatore.getIdAslOperatore()).isEqualTo(UPDATED_ID_ASL_OPERATORE);
        assertThat(testOperatore.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testOperatore.getCognome()).isEqualTo(UPDATED_COGNOME);
        assertThat(testOperatore.getDataNascita()).isEqualTo(UPDATED_DATA_NASCITA);
        assertThat(testOperatore.getLuogoNascita()).isEqualTo(UPDATED_LUOGO_NASCITA);
        assertThat(testOperatore.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOperatore.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testOperatore.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void putNonExistingOperatore() throws Exception {
        int databaseSizeBeforeUpdate = operatoreRepository.findAll().size();
        operatore.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperatoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operatore.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operatore))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operatore in the database
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperatore() throws Exception {
        int databaseSizeBeforeUpdate = operatoreRepository.findAll().size();
        operatore.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operatore))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operatore in the database
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperatore() throws Exception {
        int databaseSizeBeforeUpdate = operatoreRepository.findAll().size();
        operatore.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatoreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatore)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operatore in the database
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperatoreWithPatch() throws Exception {
        // Initialize the database
        operatoreRepository.saveAndFlush(operatore);

        int databaseSizeBeforeUpdate = operatoreRepository.findAll().size();

        // Update the operatore using partial update
        Operatore partialUpdatedOperatore = new Operatore();
        partialUpdatedOperatore.setId(operatore.getId());

        partialUpdatedOperatore.cognome(UPDATED_COGNOME).password(UPDATED_PASSWORD);

        restOperatoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperatore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperatore))
            )
            .andExpect(status().isOk());

        // Validate the Operatore in the database
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeUpdate);
        Operatore testOperatore = operatoreList.get(operatoreList.size() - 1);
        assertThat(testOperatore.getIdAslOperatore()).isEqualTo(DEFAULT_ID_ASL_OPERATORE);
        assertThat(testOperatore.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testOperatore.getCognome()).isEqualTo(UPDATED_COGNOME);
        assertThat(testOperatore.getDataNascita()).isEqualTo(DEFAULT_DATA_NASCITA);
        assertThat(testOperatore.getLuogoNascita()).isEqualTo(DEFAULT_LUOGO_NASCITA);
        assertThat(testOperatore.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOperatore.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testOperatore.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void fullUpdateOperatoreWithPatch() throws Exception {
        // Initialize the database
        operatoreRepository.saveAndFlush(operatore);

        int databaseSizeBeforeUpdate = operatoreRepository.findAll().size();

        // Update the operatore using partial update
        Operatore partialUpdatedOperatore = new Operatore();
        partialUpdatedOperatore.setId(operatore.getId());

        partialUpdatedOperatore
            .idAslOperatore(UPDATED_ID_ASL_OPERATORE)
            .nome(UPDATED_NOME)
            .cognome(UPDATED_COGNOME)
            .dataNascita(UPDATED_DATA_NASCITA)
            .luogoNascita(UPDATED_LUOGO_NASCITA)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .password(UPDATED_PASSWORD);

        restOperatoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperatore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperatore))
            )
            .andExpect(status().isOk());

        // Validate the Operatore in the database
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeUpdate);
        Operatore testOperatore = operatoreList.get(operatoreList.size() - 1);
        assertThat(testOperatore.getIdAslOperatore()).isEqualTo(UPDATED_ID_ASL_OPERATORE);
        assertThat(testOperatore.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testOperatore.getCognome()).isEqualTo(UPDATED_COGNOME);
        assertThat(testOperatore.getDataNascita()).isEqualTo(UPDATED_DATA_NASCITA);
        assertThat(testOperatore.getLuogoNascita()).isEqualTo(UPDATED_LUOGO_NASCITA);
        assertThat(testOperatore.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOperatore.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testOperatore.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void patchNonExistingOperatore() throws Exception {
        int databaseSizeBeforeUpdate = operatoreRepository.findAll().size();
        operatore.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperatoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operatore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operatore))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operatore in the database
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperatore() throws Exception {
        int databaseSizeBeforeUpdate = operatoreRepository.findAll().size();
        operatore.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operatore))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operatore in the database
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperatore() throws Exception {
        int databaseSizeBeforeUpdate = operatoreRepository.findAll().size();
        operatore.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatoreMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(operatore))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operatore in the database
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperatore() throws Exception {
        // Initialize the database
        operatoreRepository.saveAndFlush(operatore);

        int databaseSizeBeforeDelete = operatoreRepository.findAll().size();

        // Delete the operatore
        restOperatoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, operatore.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
