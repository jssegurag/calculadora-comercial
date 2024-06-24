package com.trycore.quotizo.web.rest;

import static com.trycore.quotizo.domain.FinancialParameterAsserts.*;
import static com.trycore.quotizo.web.rest.TestUtil.createUpdateProxyForBean;
import static com.trycore.quotizo.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trycore.quotizo.IntegrationTest;
import com.trycore.quotizo.domain.Country;
import com.trycore.quotizo.domain.FinancialParameter;
import com.trycore.quotizo.domain.FinancialParameterType;
import com.trycore.quotizo.domain.UserRole;
import com.trycore.quotizo.domain.Users;
import com.trycore.quotizo.repository.FinancialParameterRepository;
import com.trycore.quotizo.service.dto.FinancialParameterDTO;
import com.trycore.quotizo.service.mapper.FinancialParameterMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FinancialParameterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FinancialParameterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALUE = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_MANDATORY = false;
    private static final Boolean UPDATED_MANDATORY = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/financial-parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FinancialParameterRepository financialParameterRepository;

    @Autowired
    private FinancialParameterMapper financialParameterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFinancialParameterMockMvc;

    private FinancialParameter financialParameter;

    private FinancialParameter insertedFinancialParameter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinancialParameter createEntity(EntityManager em) {
        FinancialParameter financialParameter = new FinancialParameter()
            .name(DEFAULT_NAME)
            .value(DEFAULT_VALUE)
            .active(DEFAULT_ACTIVE)
            .mandatory(DEFAULT_MANDATORY)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return financialParameter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinancialParameter createUpdatedEntity(EntityManager em) {
        FinancialParameter financialParameter = new FinancialParameter()
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .active(UPDATED_ACTIVE)
            .mandatory(UPDATED_MANDATORY)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return financialParameter;
    }

    @BeforeEach
    public void initTest() {
        financialParameter = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedFinancialParameter != null) {
            financialParameterRepository.delete(insertedFinancialParameter);
            insertedFinancialParameter = null;
        }
    }

    @Test
    @Transactional
    void createFinancialParameter() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FinancialParameter
        FinancialParameterDTO financialParameterDTO = financialParameterMapper.toDto(financialParameter);
        var returnedFinancialParameterDTO = om.readValue(
            restFinancialParameterMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(financialParameterDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FinancialParameterDTO.class
        );

        // Validate the FinancialParameter in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFinancialParameter = financialParameterMapper.toEntity(returnedFinancialParameterDTO);
        assertFinancialParameterUpdatableFieldsEquals(
            returnedFinancialParameter,
            getPersistedFinancialParameter(returnedFinancialParameter)
        );

        insertedFinancialParameter = returnedFinancialParameter;
    }

    @Test
    @Transactional
    void createFinancialParameterWithExistingId() throws Exception {
        // Create the FinancialParameter with an existing ID
        financialParameter.setId(1L);
        FinancialParameterDTO financialParameterDTO = financialParameterMapper.toDto(financialParameter);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinancialParameterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialParameterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financialParameter.setName(null);

        // Create the FinancialParameter, which fails.
        FinancialParameterDTO financialParameterDTO = financialParameterMapper.toDto(financialParameter);

        restFinancialParameterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialParameterDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financialParameter.setValue(null);

        // Create the FinancialParameter, which fails.
        FinancialParameterDTO financialParameterDTO = financialParameterMapper.toDto(financialParameter);

        restFinancialParameterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialParameterDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financialParameter.setActive(null);

        // Create the FinancialParameter, which fails.
        FinancialParameterDTO financialParameterDTO = financialParameterMapper.toDto(financialParameter);

        restFinancialParameterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialParameterDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMandatoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financialParameter.setMandatory(null);

        // Create the FinancialParameter, which fails.
        FinancialParameterDTO financialParameterDTO = financialParameterMapper.toDto(financialParameter);

        restFinancialParameterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialParameterDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFinancialParameters() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList
        restFinancialParameterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(financialParameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].mandatory").value(hasItem(DEFAULT_MANDATORY.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getFinancialParameter() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get the financialParameter
        restFinancialParameterMockMvc
            .perform(get(ENTITY_API_URL_ID, financialParameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(financialParameter.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(sameNumber(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.mandatory").value(DEFAULT_MANDATORY.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getFinancialParametersByIdFiltering() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        Long id = financialParameter.getId();

        defaultFinancialParameterFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFinancialParameterFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFinancialParameterFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFinancialParametersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where name equals to
        defaultFinancialParameterFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFinancialParametersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where name in
        defaultFinancialParameterFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFinancialParametersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where name is not null
        defaultFinancialParameterFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllFinancialParametersByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where name contains
        defaultFinancialParameterFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFinancialParametersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where name does not contain
        defaultFinancialParameterFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllFinancialParametersByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where value equals to
        defaultFinancialParameterFiltering("value.equals=" + DEFAULT_VALUE, "value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllFinancialParametersByValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where value in
        defaultFinancialParameterFiltering("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE, "value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllFinancialParametersByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where value is not null
        defaultFinancialParameterFiltering("value.specified=true", "value.specified=false");
    }

    @Test
    @Transactional
    void getAllFinancialParametersByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where value is greater than or equal to
        defaultFinancialParameterFiltering("value.greaterThanOrEqual=" + DEFAULT_VALUE, "value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllFinancialParametersByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where value is less than or equal to
        defaultFinancialParameterFiltering("value.lessThanOrEqual=" + DEFAULT_VALUE, "value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllFinancialParametersByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where value is less than
        defaultFinancialParameterFiltering("value.lessThan=" + UPDATED_VALUE, "value.lessThan=" + DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void getAllFinancialParametersByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where value is greater than
        defaultFinancialParameterFiltering("value.greaterThan=" + SMALLER_VALUE, "value.greaterThan=" + DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void getAllFinancialParametersByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where active equals to
        defaultFinancialParameterFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllFinancialParametersByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where active in
        defaultFinancialParameterFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllFinancialParametersByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where active is not null
        defaultFinancialParameterFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllFinancialParametersByMandatoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where mandatory equals to
        defaultFinancialParameterFiltering("mandatory.equals=" + DEFAULT_MANDATORY, "mandatory.equals=" + UPDATED_MANDATORY);
    }

    @Test
    @Transactional
    void getAllFinancialParametersByMandatoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where mandatory in
        defaultFinancialParameterFiltering(
            "mandatory.in=" + DEFAULT_MANDATORY + "," + UPDATED_MANDATORY,
            "mandatory.in=" + UPDATED_MANDATORY
        );
    }

    @Test
    @Transactional
    void getAllFinancialParametersByMandatoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where mandatory is not null
        defaultFinancialParameterFiltering("mandatory.specified=true", "mandatory.specified=false");
    }

    @Test
    @Transactional
    void getAllFinancialParametersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where createdBy equals to
        defaultFinancialParameterFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFinancialParametersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where createdBy in
        defaultFinancialParameterFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllFinancialParametersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where createdBy is not null
        defaultFinancialParameterFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllFinancialParametersByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where createdBy contains
        defaultFinancialParameterFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFinancialParametersByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where createdBy does not contain
        defaultFinancialParameterFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllFinancialParametersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where createdDate equals to
        defaultFinancialParameterFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllFinancialParametersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where createdDate in
        defaultFinancialParameterFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFinancialParametersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where createdDate is not null
        defaultFinancialParameterFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFinancialParametersByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where lastModifiedBy equals to
        defaultFinancialParameterFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFinancialParametersByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where lastModifiedBy in
        defaultFinancialParameterFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFinancialParametersByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where lastModifiedBy is not null
        defaultFinancialParameterFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllFinancialParametersByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where lastModifiedBy contains
        defaultFinancialParameterFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFinancialParametersByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where lastModifiedBy does not contain
        defaultFinancialParameterFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFinancialParametersByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where lastModifiedDate equals to
        defaultFinancialParameterFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFinancialParametersByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where lastModifiedDate in
        defaultFinancialParameterFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFinancialParametersByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        // Get all the financialParameterList where lastModifiedDate is not null
        defaultFinancialParameterFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFinancialParametersByFinancialParameterTypeIsEqualToSomething() throws Exception {
        FinancialParameterType financialParameterType;
        if (TestUtil.findAll(em, FinancialParameterType.class).isEmpty()) {
            financialParameterRepository.saveAndFlush(financialParameter);
            financialParameterType = FinancialParameterTypeResourceIT.createEntity(em);
        } else {
            financialParameterType = TestUtil.findAll(em, FinancialParameterType.class).get(0);
        }
        em.persist(financialParameterType);
        em.flush();
        financialParameter.setFinancialParameterType(financialParameterType);
        financialParameterRepository.saveAndFlush(financialParameter);
        Long financialParameterTypeId = financialParameterType.getId();
        // Get all the financialParameterList where financialParameterType equals to financialParameterTypeId
        defaultFinancialParameterShouldBeFound("financialParameterTypeId.equals=" + financialParameterTypeId);

        // Get all the financialParameterList where financialParameterType equals to (financialParameterTypeId + 1)
        defaultFinancialParameterShouldNotBeFound("financialParameterTypeId.equals=" + (financialParameterTypeId + 1));
    }

    @Test
    @Transactional
    void getAllFinancialParametersByCountryIsEqualToSomething() throws Exception {
        Country country;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            financialParameterRepository.saveAndFlush(financialParameter);
            country = CountryResourceIT.createEntity(em);
        } else {
            country = TestUtil.findAll(em, Country.class).get(0);
        }
        em.persist(country);
        em.flush();
        financialParameter.setCountry(country);
        financialParameterRepository.saveAndFlush(financialParameter);
        Long countryId = country.getId();
        // Get all the financialParameterList where country equals to countryId
        defaultFinancialParameterShouldBeFound("countryId.equals=" + countryId);

        // Get all the financialParameterList where country equals to (countryId + 1)
        defaultFinancialParameterShouldNotBeFound("countryId.equals=" + (countryId + 1));
    }

    @Test
    @Transactional
    void getAllFinancialParametersByAdministratorIsEqualToSomething() throws Exception {
        Users administrator;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            financialParameterRepository.saveAndFlush(financialParameter);
            administrator = UsersResourceIT.createEntity(em);
        } else {
            administrator = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(administrator);
        em.flush();
        financialParameter.setAdministrator(administrator);
        financialParameterRepository.saveAndFlush(financialParameter);
        Long administratorId = administrator.getId();
        // Get all the financialParameterList where administrator equals to administratorId
        defaultFinancialParameterShouldBeFound("administratorId.equals=" + administratorId);

        // Get all the financialParameterList where administrator equals to (administratorId + 1)
        defaultFinancialParameterShouldNotBeFound("administratorId.equals=" + (administratorId + 1));
    }

    @Test
    @Transactional
    void getAllFinancialParametersByRoleAuthorizedIsEqualToSomething() throws Exception {
        UserRole roleAuthorized;
        if (TestUtil.findAll(em, UserRole.class).isEmpty()) {
            financialParameterRepository.saveAndFlush(financialParameter);
            roleAuthorized = UserRoleResourceIT.createEntity(em);
        } else {
            roleAuthorized = TestUtil.findAll(em, UserRole.class).get(0);
        }
        em.persist(roleAuthorized);
        em.flush();
        financialParameter.addRoleAuthorized(roleAuthorized);
        financialParameterRepository.saveAndFlush(financialParameter);
        Long roleAuthorizedId = roleAuthorized.getId();
        // Get all the financialParameterList where roleAuthorized equals to roleAuthorizedId
        defaultFinancialParameterShouldBeFound("roleAuthorizedId.equals=" + roleAuthorizedId);

        // Get all the financialParameterList where roleAuthorized equals to (roleAuthorizedId + 1)
        defaultFinancialParameterShouldNotBeFound("roleAuthorizedId.equals=" + (roleAuthorizedId + 1));
    }

    private void defaultFinancialParameterFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFinancialParameterShouldBeFound(shouldBeFound);
        defaultFinancialParameterShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFinancialParameterShouldBeFound(String filter) throws Exception {
        restFinancialParameterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(financialParameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].mandatory").value(hasItem(DEFAULT_MANDATORY.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restFinancialParameterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFinancialParameterShouldNotBeFound(String filter) throws Exception {
        restFinancialParameterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFinancialParameterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFinancialParameter() throws Exception {
        // Get the financialParameter
        restFinancialParameterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFinancialParameter() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financialParameter
        FinancialParameter updatedFinancialParameter = financialParameterRepository.findById(financialParameter.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFinancialParameter are not directly saved in db
        em.detach(updatedFinancialParameter);
        updatedFinancialParameter
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .active(UPDATED_ACTIVE)
            .mandatory(UPDATED_MANDATORY)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        FinancialParameterDTO financialParameterDTO = financialParameterMapper.toDto(updatedFinancialParameter);

        restFinancialParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, financialParameterDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialParameterDTO))
            )
            .andExpect(status().isOk());

        // Validate the FinancialParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFinancialParameterToMatchAllProperties(updatedFinancialParameter);
    }

    @Test
    @Transactional
    void putNonExistingFinancialParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialParameter.setId(longCount.incrementAndGet());

        // Create the FinancialParameter
        FinancialParameterDTO financialParameterDTO = financialParameterMapper.toDto(financialParameter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinancialParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, financialParameterDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialParameterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFinancialParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialParameter.setId(longCount.incrementAndGet());

        // Create the FinancialParameter
        FinancialParameterDTO financialParameterDTO = financialParameterMapper.toDto(financialParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialParameterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFinancialParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialParameter.setId(longCount.incrementAndGet());

        // Create the FinancialParameter
        FinancialParameterDTO financialParameterDTO = financialParameterMapper.toDto(financialParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialParameterMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialParameterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinancialParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFinancialParameterWithPatch() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financialParameter using partial update
        FinancialParameter partialUpdatedFinancialParameter = new FinancialParameter();
        partialUpdatedFinancialParameter.setId(financialParameter.getId());

        partialUpdatedFinancialParameter.value(UPDATED_VALUE).active(UPDATED_ACTIVE).createdBy(UPDATED_CREATED_BY);

        restFinancialParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinancialParameter.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFinancialParameter))
            )
            .andExpect(status().isOk());

        // Validate the FinancialParameter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFinancialParameterUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFinancialParameter, financialParameter),
            getPersistedFinancialParameter(financialParameter)
        );
    }

    @Test
    @Transactional
    void fullUpdateFinancialParameterWithPatch() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financialParameter using partial update
        FinancialParameter partialUpdatedFinancialParameter = new FinancialParameter();
        partialUpdatedFinancialParameter.setId(financialParameter.getId());

        partialUpdatedFinancialParameter
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .active(UPDATED_ACTIVE)
            .mandatory(UPDATED_MANDATORY)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restFinancialParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinancialParameter.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFinancialParameter))
            )
            .andExpect(status().isOk());

        // Validate the FinancialParameter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFinancialParameterUpdatableFieldsEquals(
            partialUpdatedFinancialParameter,
            getPersistedFinancialParameter(partialUpdatedFinancialParameter)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFinancialParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialParameter.setId(longCount.incrementAndGet());

        // Create the FinancialParameter
        FinancialParameterDTO financialParameterDTO = financialParameterMapper.toDto(financialParameter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinancialParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, financialParameterDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(financialParameterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFinancialParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialParameter.setId(longCount.incrementAndGet());

        // Create the FinancialParameter
        FinancialParameterDTO financialParameterDTO = financialParameterMapper.toDto(financialParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(financialParameterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFinancialParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialParameter.setId(longCount.incrementAndGet());

        // Create the FinancialParameter
        FinancialParameterDTO financialParameterDTO = financialParameterMapper.toDto(financialParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialParameterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(financialParameterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinancialParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFinancialParameter() throws Exception {
        // Initialize the database
        insertedFinancialParameter = financialParameterRepository.saveAndFlush(financialParameter);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the financialParameter
        restFinancialParameterMockMvc
            .perform(delete(ENTITY_API_URL_ID, financialParameter.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return financialParameterRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected FinancialParameter getPersistedFinancialParameter(FinancialParameter financialParameter) {
        return financialParameterRepository.findById(financialParameter.getId()).orElseThrow();
    }

    protected void assertPersistedFinancialParameterToMatchAllProperties(FinancialParameter expectedFinancialParameter) {
        assertFinancialParameterAllPropertiesEquals(expectedFinancialParameter, getPersistedFinancialParameter(expectedFinancialParameter));
    }

    protected void assertPersistedFinancialParameterToMatchUpdatableProperties(FinancialParameter expectedFinancialParameter) {
        assertFinancialParameterAllUpdatablePropertiesEquals(
            expectedFinancialParameter,
            getPersistedFinancialParameter(expectedFinancialParameter)
        );
    }
}
