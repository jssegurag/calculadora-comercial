package com.trycore.quotizo.web.rest;

import static com.trycore.quotizo.domain.FinancialParameterTypeAsserts.*;
import static com.trycore.quotizo.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trycore.quotizo.IntegrationTest;
import com.trycore.quotizo.domain.FinancialParameterType;
import com.trycore.quotizo.repository.FinancialParameterTypeRepository;
import com.trycore.quotizo.service.dto.FinancialParameterTypeDTO;
import com.trycore.quotizo.service.mapper.FinancialParameterTypeMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link FinancialParameterTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FinancialParameterTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/financial-parameter-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FinancialParameterTypeRepository financialParameterTypeRepository;

    @Autowired
    private FinancialParameterTypeMapper financialParameterTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFinancialParameterTypeMockMvc;

    private FinancialParameterType financialParameterType;

    private FinancialParameterType insertedFinancialParameterType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinancialParameterType createEntity(EntityManager em) {
        FinancialParameterType financialParameterType = new FinancialParameterType()
            .name(DEFAULT_NAME)
            .active(DEFAULT_ACTIVE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return financialParameterType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinancialParameterType createUpdatedEntity(EntityManager em) {
        FinancialParameterType financialParameterType = new FinancialParameterType()
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return financialParameterType;
    }

    @BeforeEach
    public void initTest() {
        financialParameterType = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedFinancialParameterType != null) {
            financialParameterTypeRepository.delete(insertedFinancialParameterType);
            insertedFinancialParameterType = null;
        }
    }

    @Test
    @Transactional
    void createFinancialParameterType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FinancialParameterType
        FinancialParameterTypeDTO financialParameterTypeDTO = financialParameterTypeMapper.toDto(financialParameterType);
        var returnedFinancialParameterTypeDTO = om.readValue(
            restFinancialParameterTypeMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(financialParameterTypeDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FinancialParameterTypeDTO.class
        );

        // Validate the FinancialParameterType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFinancialParameterType = financialParameterTypeMapper.toEntity(returnedFinancialParameterTypeDTO);
        assertFinancialParameterTypeUpdatableFieldsEquals(
            returnedFinancialParameterType,
            getPersistedFinancialParameterType(returnedFinancialParameterType)
        );

        insertedFinancialParameterType = returnedFinancialParameterType;
    }

    @Test
    @Transactional
    void createFinancialParameterTypeWithExistingId() throws Exception {
        // Create the FinancialParameterType with an existing ID
        financialParameterType.setId(1L);
        FinancialParameterTypeDTO financialParameterTypeDTO = financialParameterTypeMapper.toDto(financialParameterType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinancialParameterTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialParameterTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialParameterType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financialParameterType.setName(null);

        // Create the FinancialParameterType, which fails.
        FinancialParameterTypeDTO financialParameterTypeDTO = financialParameterTypeMapper.toDto(financialParameterType);

        restFinancialParameterTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialParameterTypeDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financialParameterType.setActive(null);

        // Create the FinancialParameterType, which fails.
        FinancialParameterTypeDTO financialParameterTypeDTO = financialParameterTypeMapper.toDto(financialParameterType);

        restFinancialParameterTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialParameterTypeDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypes() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList
        restFinancialParameterTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(financialParameterType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getFinancialParameterType() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get the financialParameterType
        restFinancialParameterTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, financialParameterType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(financialParameterType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getFinancialParameterTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        Long id = financialParameterType.getId();

        defaultFinancialParameterTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFinancialParameterTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFinancialParameterTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where name equals to
        defaultFinancialParameterTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where name in
        defaultFinancialParameterTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where name is not null
        defaultFinancialParameterTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where name contains
        defaultFinancialParameterTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where name does not contain
        defaultFinancialParameterTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where active equals to
        defaultFinancialParameterTypeFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where active in
        defaultFinancialParameterTypeFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where active is not null
        defaultFinancialParameterTypeFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where createdBy equals to
        defaultFinancialParameterTypeFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where createdBy in
        defaultFinancialParameterTypeFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where createdBy is not null
        defaultFinancialParameterTypeFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where createdBy contains
        defaultFinancialParameterTypeFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where createdBy does not contain
        defaultFinancialParameterTypeFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where createdDate equals to
        defaultFinancialParameterTypeFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where createdDate in
        defaultFinancialParameterTypeFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where createdDate is not null
        defaultFinancialParameterTypeFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where lastModifiedBy equals to
        defaultFinancialParameterTypeFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where lastModifiedBy in
        defaultFinancialParameterTypeFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where lastModifiedBy is not null
        defaultFinancialParameterTypeFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where lastModifiedBy contains
        defaultFinancialParameterTypeFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where lastModifiedBy does not contain
        defaultFinancialParameterTypeFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where lastModifiedDate equals to
        defaultFinancialParameterTypeFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where lastModifiedDate in
        defaultFinancialParameterTypeFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFinancialParameterTypesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        // Get all the financialParameterTypeList where lastModifiedDate is not null
        defaultFinancialParameterTypeFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultFinancialParameterTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFinancialParameterTypeShouldBeFound(shouldBeFound);
        defaultFinancialParameterTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFinancialParameterTypeShouldBeFound(String filter) throws Exception {
        restFinancialParameterTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(financialParameterType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restFinancialParameterTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFinancialParameterTypeShouldNotBeFound(String filter) throws Exception {
        restFinancialParameterTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFinancialParameterTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFinancialParameterType() throws Exception {
        // Get the financialParameterType
        restFinancialParameterTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFinancialParameterType() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financialParameterType
        FinancialParameterType updatedFinancialParameterType = financialParameterTypeRepository
            .findById(financialParameterType.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedFinancialParameterType are not directly saved in db
        em.detach(updatedFinancialParameterType);
        updatedFinancialParameterType
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        FinancialParameterTypeDTO financialParameterTypeDTO = financialParameterTypeMapper.toDto(updatedFinancialParameterType);

        restFinancialParameterTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, financialParameterTypeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialParameterTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the FinancialParameterType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFinancialParameterTypeToMatchAllProperties(updatedFinancialParameterType);
    }

    @Test
    @Transactional
    void putNonExistingFinancialParameterType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialParameterType.setId(longCount.incrementAndGet());

        // Create the FinancialParameterType
        FinancialParameterTypeDTO financialParameterTypeDTO = financialParameterTypeMapper.toDto(financialParameterType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinancialParameterTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, financialParameterTypeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialParameterTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialParameterType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFinancialParameterType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialParameterType.setId(longCount.incrementAndGet());

        // Create the FinancialParameterType
        FinancialParameterTypeDTO financialParameterTypeDTO = financialParameterTypeMapper.toDto(financialParameterType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialParameterTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialParameterTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialParameterType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFinancialParameterType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialParameterType.setId(longCount.incrementAndGet());

        // Create the FinancialParameterType
        FinancialParameterTypeDTO financialParameterTypeDTO = financialParameterTypeMapper.toDto(financialParameterType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialParameterTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialParameterTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinancialParameterType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFinancialParameterTypeWithPatch() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financialParameterType using partial update
        FinancialParameterType partialUpdatedFinancialParameterType = new FinancialParameterType();
        partialUpdatedFinancialParameterType.setId(financialParameterType.getId());

        partialUpdatedFinancialParameterType
            .name(UPDATED_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restFinancialParameterTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinancialParameterType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFinancialParameterType))
            )
            .andExpect(status().isOk());

        // Validate the FinancialParameterType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFinancialParameterTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFinancialParameterType, financialParameterType),
            getPersistedFinancialParameterType(financialParameterType)
        );
    }

    @Test
    @Transactional
    void fullUpdateFinancialParameterTypeWithPatch() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financialParameterType using partial update
        FinancialParameterType partialUpdatedFinancialParameterType = new FinancialParameterType();
        partialUpdatedFinancialParameterType.setId(financialParameterType.getId());

        partialUpdatedFinancialParameterType
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restFinancialParameterTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinancialParameterType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFinancialParameterType))
            )
            .andExpect(status().isOk());

        // Validate the FinancialParameterType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFinancialParameterTypeUpdatableFieldsEquals(
            partialUpdatedFinancialParameterType,
            getPersistedFinancialParameterType(partialUpdatedFinancialParameterType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFinancialParameterType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialParameterType.setId(longCount.incrementAndGet());

        // Create the FinancialParameterType
        FinancialParameterTypeDTO financialParameterTypeDTO = financialParameterTypeMapper.toDto(financialParameterType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinancialParameterTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, financialParameterTypeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(financialParameterTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialParameterType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFinancialParameterType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialParameterType.setId(longCount.incrementAndGet());

        // Create the FinancialParameterType
        FinancialParameterTypeDTO financialParameterTypeDTO = financialParameterTypeMapper.toDto(financialParameterType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialParameterTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(financialParameterTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialParameterType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFinancialParameterType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialParameterType.setId(longCount.incrementAndGet());

        // Create the FinancialParameterType
        FinancialParameterTypeDTO financialParameterTypeDTO = financialParameterTypeMapper.toDto(financialParameterType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialParameterTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(financialParameterTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinancialParameterType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFinancialParameterType() throws Exception {
        // Initialize the database
        insertedFinancialParameterType = financialParameterTypeRepository.saveAndFlush(financialParameterType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the financialParameterType
        restFinancialParameterTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, financialParameterType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return financialParameterTypeRepository.count();
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

    protected FinancialParameterType getPersistedFinancialParameterType(FinancialParameterType financialParameterType) {
        return financialParameterTypeRepository.findById(financialParameterType.getId()).orElseThrow();
    }

    protected void assertPersistedFinancialParameterTypeToMatchAllProperties(FinancialParameterType expectedFinancialParameterType) {
        assertFinancialParameterTypeAllPropertiesEquals(
            expectedFinancialParameterType,
            getPersistedFinancialParameterType(expectedFinancialParameterType)
        );
    }

    protected void assertPersistedFinancialParameterTypeToMatchUpdatableProperties(FinancialParameterType expectedFinancialParameterType) {
        assertFinancialParameterTypeAllUpdatablePropertiesEquals(
            expectedFinancialParameterType,
            getPersistedFinancialParameterType(expectedFinancialParameterType)
        );
    }
}
