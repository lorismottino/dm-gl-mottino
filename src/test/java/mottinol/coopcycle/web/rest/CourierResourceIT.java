package mottinol.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import mottinol.coopcycle.IntegrationTest;
import mottinol.coopcycle.domain.Courier;
import mottinol.coopcycle.repository.CourierRepository;
import mottinol.coopcycle.repository.search.CourierSearchRepository;
import mottinol.coopcycle.service.dto.CourierDTO;
import mottinol.coopcycle.service.mapper.CourierMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CourierResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CourierResourceIT {

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/couriers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/couriers";

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private CourierMapper courierMapper;

    /**
     * This repository is mocked in the mottinol.coopcycle.repository.search test package.
     *
     * @see mottinol.coopcycle.repository.search.CourierSearchRepositoryMockConfiguration
     */
    @Autowired
    private CourierSearchRepository mockCourierSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourierMockMvc;

    private Courier courier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Courier createEntity(EntityManager em) {
        Courier courier = new Courier().firstname(DEFAULT_FIRSTNAME).lastname(DEFAULT_LASTNAME).phone(DEFAULT_PHONE).email(DEFAULT_EMAIL);
        return courier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Courier createUpdatedEntity(EntityManager em) {
        Courier courier = new Courier().firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME).phone(UPDATED_PHONE).email(UPDATED_EMAIL);
        return courier;
    }

    @BeforeEach
    public void initTest() {
        courier = createEntity(em);
    }

    @Test
    @Transactional
    void createCourier() throws Exception {
        int databaseSizeBeforeCreate = courierRepository.findAll().size();
        // Create the Courier
        CourierDTO courierDTO = courierMapper.toDto(courier);
        restCourierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courierDTO)))
            .andExpect(status().isCreated());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeCreate + 1);
        Courier testCourier = courierList.get(courierList.size() - 1);
        assertThat(testCourier.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testCourier.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testCourier.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCourier.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the Courier in Elasticsearch
        verify(mockCourierSearchRepository, times(1)).save(testCourier);
    }

    @Test
    @Transactional
    void createCourierWithExistingId() throws Exception {
        // Create the Courier with an existing ID
        courier.setId("existing_id");
        CourierDTO courierDTO = courierMapper.toDto(courier);

        int databaseSizeBeforeCreate = courierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeCreate);

        // Validate the Courier in Elasticsearch
        verify(mockCourierSearchRepository, times(0)).save(courier);
    }

    @Test
    @Transactional
    void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = courierRepository.findAll().size();
        // set the field null
        courier.setFirstname(null);

        // Create the Courier, which fails.
        CourierDTO courierDTO = courierMapper.toDto(courier);

        restCourierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courierDTO)))
            .andExpect(status().isBadRequest());

        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = courierRepository.findAll().size();
        // set the field null
        courier.setLastname(null);

        // Create the Courier, which fails.
        CourierDTO courierDTO = courierMapper.toDto(courier);

        restCourierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courierDTO)))
            .andExpect(status().isBadRequest());

        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = courierRepository.findAll().size();
        // set the field null
        courier.setPhone(null);

        // Create the Courier, which fails.
        CourierDTO courierDTO = courierMapper.toDto(courier);

        restCourierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courierDTO)))
            .andExpect(status().isBadRequest());

        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCouriers() throws Exception {
        // Initialize the database
        courier.setId(UUID.randomUUID().toString());
        courierRepository.saveAndFlush(courier);

        // Get all the courierList
        restCourierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courier.getId())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getCourier() throws Exception {
        // Initialize the database
        courier.setId(UUID.randomUUID().toString());
        courierRepository.saveAndFlush(courier);

        // Get the courier
        restCourierMockMvc
            .perform(get(ENTITY_API_URL_ID, courier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courier.getId()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingCourier() throws Exception {
        // Get the courier
        restCourierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCourier() throws Exception {
        // Initialize the database
        courier.setId(UUID.randomUUID().toString());
        courierRepository.saveAndFlush(courier);

        int databaseSizeBeforeUpdate = courierRepository.findAll().size();

        // Update the courier
        Courier updatedCourier = courierRepository.findById(courier.getId()).get();
        // Disconnect from session so that the updates on updatedCourier are not directly saved in db
        em.detach(updatedCourier);
        updatedCourier.firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME).phone(UPDATED_PHONE).email(UPDATED_EMAIL);
        CourierDTO courierDTO = courierMapper.toDto(updatedCourier);

        restCourierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courierDTO))
            )
            .andExpect(status().isOk());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);
        Courier testCourier = courierList.get(courierList.size() - 1);
        assertThat(testCourier.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testCourier.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testCourier.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCourier.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the Courier in Elasticsearch
        verify(mockCourierSearchRepository).save(testCourier);
    }

    @Test
    @Transactional
    void putNonExistingCourier() throws Exception {
        int databaseSizeBeforeUpdate = courierRepository.findAll().size();
        courier.setId(UUID.randomUUID().toString());

        // Create the Courier
        CourierDTO courierDTO = courierMapper.toDto(courier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Courier in Elasticsearch
        verify(mockCourierSearchRepository, times(0)).save(courier);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourier() throws Exception {
        int databaseSizeBeforeUpdate = courierRepository.findAll().size();
        courier.setId(UUID.randomUUID().toString());

        // Create the Courier
        CourierDTO courierDTO = courierMapper.toDto(courier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Courier in Elasticsearch
        verify(mockCourierSearchRepository, times(0)).save(courier);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourier() throws Exception {
        int databaseSizeBeforeUpdate = courierRepository.findAll().size();
        courier.setId(UUID.randomUUID().toString());

        // Create the Courier
        CourierDTO courierDTO = courierMapper.toDto(courier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Courier in Elasticsearch
        verify(mockCourierSearchRepository, times(0)).save(courier);
    }

    @Test
    @Transactional
    void partialUpdateCourierWithPatch() throws Exception {
        // Initialize the database
        courier.setId(UUID.randomUUID().toString());
        courierRepository.saveAndFlush(courier);

        int databaseSizeBeforeUpdate = courierRepository.findAll().size();

        // Update the courier using partial update
        Courier partialUpdatedCourier = new Courier();
        partialUpdatedCourier.setId(courier.getId());

        partialUpdatedCourier.firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME).email(UPDATED_EMAIL);

        restCourierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourier))
            )
            .andExpect(status().isOk());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);
        Courier testCourier = courierList.get(courierList.size() - 1);
        assertThat(testCourier.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testCourier.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testCourier.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCourier.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateCourierWithPatch() throws Exception {
        // Initialize the database
        courier.setId(UUID.randomUUID().toString());
        courierRepository.saveAndFlush(courier);

        int databaseSizeBeforeUpdate = courierRepository.findAll().size();

        // Update the courier using partial update
        Courier partialUpdatedCourier = new Courier();
        partialUpdatedCourier.setId(courier.getId());

        partialUpdatedCourier.firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME).phone(UPDATED_PHONE).email(UPDATED_EMAIL);

        restCourierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourier))
            )
            .andExpect(status().isOk());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);
        Courier testCourier = courierList.get(courierList.size() - 1);
        assertThat(testCourier.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testCourier.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testCourier.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCourier.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingCourier() throws Exception {
        int databaseSizeBeforeUpdate = courierRepository.findAll().size();
        courier.setId(UUID.randomUUID().toString());

        // Create the Courier
        CourierDTO courierDTO = courierMapper.toDto(courier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courierDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Courier in Elasticsearch
        verify(mockCourierSearchRepository, times(0)).save(courier);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourier() throws Exception {
        int databaseSizeBeforeUpdate = courierRepository.findAll().size();
        courier.setId(UUID.randomUUID().toString());

        // Create the Courier
        CourierDTO courierDTO = courierMapper.toDto(courier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Courier in Elasticsearch
        verify(mockCourierSearchRepository, times(0)).save(courier);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourier() throws Exception {
        int databaseSizeBeforeUpdate = courierRepository.findAll().size();
        courier.setId(UUID.randomUUID().toString());

        // Create the Courier
        CourierDTO courierDTO = courierMapper.toDto(courier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourierMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(courierDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Courier in Elasticsearch
        verify(mockCourierSearchRepository, times(0)).save(courier);
    }

    @Test
    @Transactional
    void deleteCourier() throws Exception {
        // Initialize the database
        courier.setId(UUID.randomUUID().toString());
        courierRepository.saveAndFlush(courier);

        int databaseSizeBeforeDelete = courierRepository.findAll().size();

        // Delete the courier
        restCourierMockMvc
            .perform(delete(ENTITY_API_URL_ID, courier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Courier in Elasticsearch
        verify(mockCourierSearchRepository, times(1)).deleteById(courier.getId());
    }

    @Test
    @Transactional
    void searchCourier() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        courier.setId(UUID.randomUUID().toString());
        courierRepository.saveAndFlush(courier);
        when(mockCourierSearchRepository.search("id:" + courier.getId())).thenReturn(Stream.of(courier));

        // Search the courier
        restCourierMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + courier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courier.getId())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }
}
