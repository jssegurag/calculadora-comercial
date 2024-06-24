package com.trycore.quotizo.web.rest;

import static com.trycore.quotizo.domain.RuleAssignmentAsserts.*;
import static com.trycore.quotizo.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trycore.quotizo.IntegrationTest;
import com.trycore.quotizo.domain.DroolsRuleFile;
import com.trycore.quotizo.domain.RuleAssignment;
import com.trycore.quotizo.repository.RuleAssignmentRepository;
import com.trycore.quotizo.service.dto.RuleAssignmentDTO;
import com.trycore.quotizo.service.mapper.RuleAssignmentMapper;
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
 * Integration tests for the {@link RuleAssignmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RuleAssignmentResourceIT {

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_ENTITY_ID = 1L;
    private static final Long UPDATED_ENTITY_ID = 2L;
    private static final Long SMALLER_ENTITY_ID = 1L - 1L;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/rule-assignments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RuleAssignmentRepository ruleAssignmentRepository;

    @Autowired
    private RuleAssignmentMapper ruleAssignmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRuleAssignmentMockMvc;

    private RuleAssignment ruleAssignment;

    private RuleAssignment insertedRuleAssignment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RuleAssignment createEntity(EntityManager em) {
        RuleAssignment ruleAssignment = new RuleAssignment()
            .entityName(DEFAULT_ENTITY_NAME)
            .entityId(DEFAULT_ENTITY_ID)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return ruleAssignment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RuleAssignment createUpdatedEntity(EntityManager em) {
        RuleAssignment ruleAssignment = new RuleAssignment()
            .entityName(UPDATED_ENTITY_NAME)
            .entityId(UPDATED_ENTITY_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return ruleAssignment;
    }

    @BeforeEach
    public void initTest() {
        ruleAssignment = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedRuleAssignment != null) {
            ruleAssignmentRepository.delete(insertedRuleAssignment);
            insertedRuleAssignment = null;
        }
    }

    @Test
    @Transactional
    void createRuleAssignment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RuleAssignment
        RuleAssignmentDTO ruleAssignmentDTO = ruleAssignmentMapper.toDto(ruleAssignment);
        var returnedRuleAssignmentDTO = om.readValue(
            restRuleAssignmentMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(ruleAssignmentDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RuleAssignmentDTO.class
        );

        // Validate the RuleAssignment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRuleAssignment = ruleAssignmentMapper.toEntity(returnedRuleAssignmentDTO);
        assertRuleAssignmentUpdatableFieldsEquals(returnedRuleAssignment, getPersistedRuleAssignment(returnedRuleAssignment));

        insertedRuleAssignment = returnedRuleAssignment;
    }

    @Test
    @Transactional
    void createRuleAssignmentWithExistingId() throws Exception {
        // Create the RuleAssignment with an existing ID
        ruleAssignment.setId(1L);
        RuleAssignmentDTO ruleAssignmentDTO = ruleAssignmentMapper.toDto(ruleAssignment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRuleAssignmentMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ruleAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RuleAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEntityNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ruleAssignment.setEntityName(null);

        // Create the RuleAssignment, which fails.
        RuleAssignmentDTO ruleAssignmentDTO = ruleAssignmentMapper.toDto(ruleAssignment);

        restRuleAssignmentMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ruleAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntityIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ruleAssignment.setEntityId(null);

        // Create the RuleAssignment, which fails.
        RuleAssignmentDTO ruleAssignmentDTO = ruleAssignmentMapper.toDto(ruleAssignment);

        restRuleAssignmentMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ruleAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRuleAssignments() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList
        restRuleAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ruleAssignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getRuleAssignment() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get the ruleAssignment
        restRuleAssignmentMockMvc
            .perform(get(ENTITY_API_URL_ID, ruleAssignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ruleAssignment.getId().intValue()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getRuleAssignmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        Long id = ruleAssignment.getId();

        defaultRuleAssignmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRuleAssignmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRuleAssignmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where entityName equals to
        defaultRuleAssignmentFiltering("entityName.equals=" + DEFAULT_ENTITY_NAME, "entityName.equals=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where entityName in
        defaultRuleAssignmentFiltering(
            "entityName.in=" + DEFAULT_ENTITY_NAME + "," + UPDATED_ENTITY_NAME,
            "entityName.in=" + UPDATED_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where entityName is not null
        defaultRuleAssignmentFiltering("entityName.specified=true", "entityName.specified=false");
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByEntityNameContainsSomething() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where entityName contains
        defaultRuleAssignmentFiltering("entityName.contains=" + DEFAULT_ENTITY_NAME, "entityName.contains=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByEntityNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where entityName does not contain
        defaultRuleAssignmentFiltering(
            "entityName.doesNotContain=" + UPDATED_ENTITY_NAME,
            "entityName.doesNotContain=" + DEFAULT_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByEntityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where entityId equals to
        defaultRuleAssignmentFiltering("entityId.equals=" + DEFAULT_ENTITY_ID, "entityId.equals=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByEntityIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where entityId in
        defaultRuleAssignmentFiltering("entityId.in=" + DEFAULT_ENTITY_ID + "," + UPDATED_ENTITY_ID, "entityId.in=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByEntityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where entityId is not null
        defaultRuleAssignmentFiltering("entityId.specified=true", "entityId.specified=false");
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByEntityIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where entityId is greater than or equal to
        defaultRuleAssignmentFiltering(
            "entityId.greaterThanOrEqual=" + DEFAULT_ENTITY_ID,
            "entityId.greaterThanOrEqual=" + UPDATED_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByEntityIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where entityId is less than or equal to
        defaultRuleAssignmentFiltering("entityId.lessThanOrEqual=" + DEFAULT_ENTITY_ID, "entityId.lessThanOrEqual=" + SMALLER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByEntityIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where entityId is less than
        defaultRuleAssignmentFiltering("entityId.lessThan=" + UPDATED_ENTITY_ID, "entityId.lessThan=" + DEFAULT_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByEntityIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where entityId is greater than
        defaultRuleAssignmentFiltering("entityId.greaterThan=" + SMALLER_ENTITY_ID, "entityId.greaterThan=" + DEFAULT_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where createdBy equals to
        defaultRuleAssignmentFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where createdBy in
        defaultRuleAssignmentFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where createdBy is not null
        defaultRuleAssignmentFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where createdBy contains
        defaultRuleAssignmentFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where createdBy does not contain
        defaultRuleAssignmentFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where createdDate equals to
        defaultRuleAssignmentFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where createdDate in
        defaultRuleAssignmentFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where createdDate is not null
        defaultRuleAssignmentFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where lastModifiedBy equals to
        defaultRuleAssignmentFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where lastModifiedBy in
        defaultRuleAssignmentFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where lastModifiedBy is not null
        defaultRuleAssignmentFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where lastModifiedBy contains
        defaultRuleAssignmentFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where lastModifiedBy does not contain
        defaultRuleAssignmentFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where lastModifiedDate equals to
        defaultRuleAssignmentFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where lastModifiedDate in
        defaultRuleAssignmentFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        // Get all the ruleAssignmentList where lastModifiedDate is not null
        defaultRuleAssignmentFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRuleAssignmentsByDroolsRuleFileIsEqualToSomething() throws Exception {
        DroolsRuleFile droolsRuleFile;
        if (TestUtil.findAll(em, DroolsRuleFile.class).isEmpty()) {
            ruleAssignmentRepository.saveAndFlush(ruleAssignment);
            droolsRuleFile = DroolsRuleFileResourceIT.createEntity(em);
        } else {
            droolsRuleFile = TestUtil.findAll(em, DroolsRuleFile.class).get(0);
        }
        em.persist(droolsRuleFile);
        em.flush();
        ruleAssignment.setDroolsRuleFile(droolsRuleFile);
        ruleAssignmentRepository.saveAndFlush(ruleAssignment);
        Long droolsRuleFileId = droolsRuleFile.getId();
        // Get all the ruleAssignmentList where droolsRuleFile equals to droolsRuleFileId
        defaultRuleAssignmentShouldBeFound("droolsRuleFileId.equals=" + droolsRuleFileId);

        // Get all the ruleAssignmentList where droolsRuleFile equals to (droolsRuleFileId + 1)
        defaultRuleAssignmentShouldNotBeFound("droolsRuleFileId.equals=" + (droolsRuleFileId + 1));
    }

    private void defaultRuleAssignmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRuleAssignmentShouldBeFound(shouldBeFound);
        defaultRuleAssignmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRuleAssignmentShouldBeFound(String filter) throws Exception {
        restRuleAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ruleAssignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restRuleAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRuleAssignmentShouldNotBeFound(String filter) throws Exception {
        restRuleAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRuleAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRuleAssignment() throws Exception {
        // Get the ruleAssignment
        restRuleAssignmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRuleAssignment() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ruleAssignment
        RuleAssignment updatedRuleAssignment = ruleAssignmentRepository.findById(ruleAssignment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRuleAssignment are not directly saved in db
        em.detach(updatedRuleAssignment);
        updatedRuleAssignment
            .entityName(UPDATED_ENTITY_NAME)
            .entityId(UPDATED_ENTITY_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        RuleAssignmentDTO ruleAssignmentDTO = ruleAssignmentMapper.toDto(updatedRuleAssignment);

        restRuleAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ruleAssignmentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ruleAssignmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the RuleAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRuleAssignmentToMatchAllProperties(updatedRuleAssignment);
    }

    @Test
    @Transactional
    void putNonExistingRuleAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ruleAssignment.setId(longCount.incrementAndGet());

        // Create the RuleAssignment
        RuleAssignmentDTO ruleAssignmentDTO = ruleAssignmentMapper.toDto(ruleAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRuleAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ruleAssignmentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ruleAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RuleAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRuleAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ruleAssignment.setId(longCount.incrementAndGet());

        // Create the RuleAssignment
        RuleAssignmentDTO ruleAssignmentDTO = ruleAssignmentMapper.toDto(ruleAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRuleAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ruleAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RuleAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRuleAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ruleAssignment.setId(longCount.incrementAndGet());

        // Create the RuleAssignment
        RuleAssignmentDTO ruleAssignmentDTO = ruleAssignmentMapper.toDto(ruleAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRuleAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ruleAssignmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RuleAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRuleAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ruleAssignment using partial update
        RuleAssignment partialUpdatedRuleAssignment = new RuleAssignment();
        partialUpdatedRuleAssignment.setId(ruleAssignment.getId());

        partialUpdatedRuleAssignment
            .entityName(UPDATED_ENTITY_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restRuleAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRuleAssignment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRuleAssignment))
            )
            .andExpect(status().isOk());

        // Validate the RuleAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRuleAssignmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRuleAssignment, ruleAssignment),
            getPersistedRuleAssignment(ruleAssignment)
        );
    }

    @Test
    @Transactional
    void fullUpdateRuleAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ruleAssignment using partial update
        RuleAssignment partialUpdatedRuleAssignment = new RuleAssignment();
        partialUpdatedRuleAssignment.setId(ruleAssignment.getId());

        partialUpdatedRuleAssignment
            .entityName(UPDATED_ENTITY_NAME)
            .entityId(UPDATED_ENTITY_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restRuleAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRuleAssignment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRuleAssignment))
            )
            .andExpect(status().isOk());

        // Validate the RuleAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRuleAssignmentUpdatableFieldsEquals(partialUpdatedRuleAssignment, getPersistedRuleAssignment(partialUpdatedRuleAssignment));
    }

    @Test
    @Transactional
    void patchNonExistingRuleAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ruleAssignment.setId(longCount.incrementAndGet());

        // Create the RuleAssignment
        RuleAssignmentDTO ruleAssignmentDTO = ruleAssignmentMapper.toDto(ruleAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRuleAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ruleAssignmentDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ruleAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RuleAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRuleAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ruleAssignment.setId(longCount.incrementAndGet());

        // Create the RuleAssignment
        RuleAssignmentDTO ruleAssignmentDTO = ruleAssignmentMapper.toDto(ruleAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRuleAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ruleAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RuleAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRuleAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ruleAssignment.setId(longCount.incrementAndGet());

        // Create the RuleAssignment
        RuleAssignmentDTO ruleAssignmentDTO = ruleAssignmentMapper.toDto(ruleAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRuleAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ruleAssignmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RuleAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRuleAssignment() throws Exception {
        // Initialize the database
        insertedRuleAssignment = ruleAssignmentRepository.saveAndFlush(ruleAssignment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ruleAssignment
        restRuleAssignmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, ruleAssignment.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ruleAssignmentRepository.count();
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

    protected RuleAssignment getPersistedRuleAssignment(RuleAssignment ruleAssignment) {
        return ruleAssignmentRepository.findById(ruleAssignment.getId()).orElseThrow();
    }

    protected void assertPersistedRuleAssignmentToMatchAllProperties(RuleAssignment expectedRuleAssignment) {
        assertRuleAssignmentAllPropertiesEquals(expectedRuleAssignment, getPersistedRuleAssignment(expectedRuleAssignment));
    }

    protected void assertPersistedRuleAssignmentToMatchUpdatableProperties(RuleAssignment expectedRuleAssignment) {
        assertRuleAssignmentAllUpdatablePropertiesEquals(expectedRuleAssignment, getPersistedRuleAssignment(expectedRuleAssignment));
    }
}
