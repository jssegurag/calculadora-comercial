package com.trycore.quotizo.web.rest;

import static com.trycore.quotizo.domain.ResourceAsserts.*;
import static com.trycore.quotizo.web.rest.TestUtil.createUpdateProxyForBean;
import static com.trycore.quotizo.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trycore.quotizo.IntegrationTest;
import com.trycore.quotizo.domain.Position;
import com.trycore.quotizo.domain.Resource;
import com.trycore.quotizo.repository.ResourceRepository;
import com.trycore.quotizo.service.dto.ResourceDTO;
import com.trycore.quotizo.service.mapper.ResourceMapper;
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
 * Integration tests for the {@link ResourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResourceResourceIT {

    private static final BigDecimal DEFAULT_SALARY = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALARY = new BigDecimal(2);
    private static final BigDecimal SMALLER_SALARY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_HOURLY_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_HOURLY_RATE = new BigDecimal(2);
    private static final BigDecimal SMALLER_HOURLY_RATE = new BigDecimal(1 - 1);

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

    private static final String ENTITY_API_URL = "/api/resources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResourceMockMvc;

    private Resource resource;

    private Resource insertedResource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resource createEntity(EntityManager em) {
        Resource resource = new Resource()
            .salary(DEFAULT_SALARY)
            .hourlyRate(DEFAULT_HOURLY_RATE)
            .active(DEFAULT_ACTIVE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return resource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resource createUpdatedEntity(EntityManager em) {
        Resource resource = new Resource()
            .salary(UPDATED_SALARY)
            .hourlyRate(UPDATED_HOURLY_RATE)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return resource;
    }

    @BeforeEach
    public void initTest() {
        resource = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedResource != null) {
            resourceRepository.delete(insertedResource);
            insertedResource = null;
        }
    }

    @Test
    @Transactional
    void createResource() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);
        var returnedResourceDTO = om.readValue(
            restResourceMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resourceDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ResourceDTO.class
        );

        // Validate the Resource in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedResource = resourceMapper.toEntity(returnedResourceDTO);
        assertResourceUpdatableFieldsEquals(returnedResource, getPersistedResource(returnedResource));

        insertedResource = returnedResource;
    }

    @Test
    @Transactional
    void createResourceWithExistingId() throws Exception {
        // Create the Resource with an existing ID
        resource.setId(1L);
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resourceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSalaryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resource.setSalary(null);

        // Create the Resource, which fails.
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        restResourceMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resourceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resource.setActive(null);

        // Create the Resource, which fails.
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        restResourceMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resourceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllResources() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList
        restResourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resource.getId().intValue())))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(sameNumber(DEFAULT_SALARY))))
            .andExpect(jsonPath("$.[*].hourlyRate").value(hasItem(sameNumber(DEFAULT_HOURLY_RATE))))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getResource() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get the resource
        restResourceMockMvc
            .perform(get(ENTITY_API_URL_ID, resource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resource.getId().intValue()))
            .andExpect(jsonPath("$.salary").value(sameNumber(DEFAULT_SALARY)))
            .andExpect(jsonPath("$.hourlyRate").value(sameNumber(DEFAULT_HOURLY_RATE)))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getResourcesByIdFiltering() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        Long id = resource.getId();

        defaultResourceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultResourceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultResourceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllResourcesBySalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where salary equals to
        defaultResourceFiltering("salary.equals=" + DEFAULT_SALARY, "salary.equals=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllResourcesBySalaryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where salary in
        defaultResourceFiltering("salary.in=" + DEFAULT_SALARY + "," + UPDATED_SALARY, "salary.in=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllResourcesBySalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where salary is not null
        defaultResourceFiltering("salary.specified=true", "salary.specified=false");
    }

    @Test
    @Transactional
    void getAllResourcesBySalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where salary is greater than or equal to
        defaultResourceFiltering("salary.greaterThanOrEqual=" + DEFAULT_SALARY, "salary.greaterThanOrEqual=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllResourcesBySalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where salary is less than or equal to
        defaultResourceFiltering("salary.lessThanOrEqual=" + DEFAULT_SALARY, "salary.lessThanOrEqual=" + SMALLER_SALARY);
    }

    @Test
    @Transactional
    void getAllResourcesBySalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where salary is less than
        defaultResourceFiltering("salary.lessThan=" + UPDATED_SALARY, "salary.lessThan=" + DEFAULT_SALARY);
    }

    @Test
    @Transactional
    void getAllResourcesBySalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where salary is greater than
        defaultResourceFiltering("salary.greaterThan=" + SMALLER_SALARY, "salary.greaterThan=" + DEFAULT_SALARY);
    }

    @Test
    @Transactional
    void getAllResourcesByHourlyRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where hourlyRate equals to
        defaultResourceFiltering("hourlyRate.equals=" + DEFAULT_HOURLY_RATE, "hourlyRate.equals=" + UPDATED_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllResourcesByHourlyRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where hourlyRate in
        defaultResourceFiltering(
            "hourlyRate.in=" + DEFAULT_HOURLY_RATE + "," + UPDATED_HOURLY_RATE,
            "hourlyRate.in=" + UPDATED_HOURLY_RATE
        );
    }

    @Test
    @Transactional
    void getAllResourcesByHourlyRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where hourlyRate is not null
        defaultResourceFiltering("hourlyRate.specified=true", "hourlyRate.specified=false");
    }

    @Test
    @Transactional
    void getAllResourcesByHourlyRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where hourlyRate is greater than or equal to
        defaultResourceFiltering(
            "hourlyRate.greaterThanOrEqual=" + DEFAULT_HOURLY_RATE,
            "hourlyRate.greaterThanOrEqual=" + UPDATED_HOURLY_RATE
        );
    }

    @Test
    @Transactional
    void getAllResourcesByHourlyRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where hourlyRate is less than or equal to
        defaultResourceFiltering("hourlyRate.lessThanOrEqual=" + DEFAULT_HOURLY_RATE, "hourlyRate.lessThanOrEqual=" + SMALLER_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllResourcesByHourlyRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where hourlyRate is less than
        defaultResourceFiltering("hourlyRate.lessThan=" + UPDATED_HOURLY_RATE, "hourlyRate.lessThan=" + DEFAULT_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllResourcesByHourlyRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where hourlyRate is greater than
        defaultResourceFiltering("hourlyRate.greaterThan=" + SMALLER_HOURLY_RATE, "hourlyRate.greaterThan=" + DEFAULT_HOURLY_RATE);
    }

    @Test
    @Transactional
    void getAllResourcesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where active equals to
        defaultResourceFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllResourcesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where active in
        defaultResourceFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllResourcesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where active is not null
        defaultResourceFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllResourcesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where createdBy equals to
        defaultResourceFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllResourcesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where createdBy in
        defaultResourceFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllResourcesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where createdBy is not null
        defaultResourceFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllResourcesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where createdBy contains
        defaultResourceFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllResourcesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where createdBy does not contain
        defaultResourceFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllResourcesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where createdDate equals to
        defaultResourceFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllResourcesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where createdDate in
        defaultResourceFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllResourcesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where createdDate is not null
        defaultResourceFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllResourcesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where lastModifiedBy equals to
        defaultResourceFiltering("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllResourcesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where lastModifiedBy in
        defaultResourceFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllResourcesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where lastModifiedBy is not null
        defaultResourceFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllResourcesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where lastModifiedBy contains
        defaultResourceFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllResourcesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where lastModifiedBy does not contain
        defaultResourceFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllResourcesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where lastModifiedDate equals to
        defaultResourceFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllResourcesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where lastModifiedDate in
        defaultResourceFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllResourcesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        // Get all the resourceList where lastModifiedDate is not null
        defaultResourceFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllResourcesByPositionIsEqualToSomething() throws Exception {
        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            resourceRepository.saveAndFlush(resource);
            position = PositionResourceIT.createEntity(em);
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }
        em.persist(position);
        em.flush();
        resource.setPosition(position);
        resourceRepository.saveAndFlush(resource);
        Long positionId = position.getId();
        // Get all the resourceList where position equals to positionId
        defaultResourceShouldBeFound("positionId.equals=" + positionId);

        // Get all the resourceList where position equals to (positionId + 1)
        defaultResourceShouldNotBeFound("positionId.equals=" + (positionId + 1));
    }

    private void defaultResourceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultResourceShouldBeFound(shouldBeFound);
        defaultResourceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultResourceShouldBeFound(String filter) throws Exception {
        restResourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resource.getId().intValue())))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(sameNumber(DEFAULT_SALARY))))
            .andExpect(jsonPath("$.[*].hourlyRate").value(hasItem(sameNumber(DEFAULT_HOURLY_RATE))))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restResourceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultResourceShouldNotBeFound(String filter) throws Exception {
        restResourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restResourceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingResource() throws Exception {
        // Get the resource
        restResourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResource() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resource
        Resource updatedResource = resourceRepository.findById(resource.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedResource are not directly saved in db
        em.detach(updatedResource);
        updatedResource
            .salary(UPDATED_SALARY)
            .hourlyRate(UPDATED_HOURLY_RATE)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ResourceDTO resourceDTO = resourceMapper.toDto(updatedResource);

        restResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resourceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resourceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Resource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResourceToMatchAllProperties(updatedResource);
    }

    @Test
    @Transactional
    void putNonExistingResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resource.setId(longCount.incrementAndGet());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resourceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resource.setId(longCount.incrementAndGet());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resource.setId(longCount.incrementAndGet());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resourceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResourceWithPatch() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resource using partial update
        Resource partialUpdatedResource = new Resource();
        partialUpdatedResource.setId(resource.getId());

        partialUpdatedResource.createdBy(UPDATED_CREATED_BY).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResource.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResource))
            )
            .andExpect(status().isOk());

        // Validate the Resource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResourceUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedResource, resource), getPersistedResource(resource));
    }

    @Test
    @Transactional
    void fullUpdateResourceWithPatch() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resource using partial update
        Resource partialUpdatedResource = new Resource();
        partialUpdatedResource.setId(resource.getId());

        partialUpdatedResource
            .salary(UPDATED_SALARY)
            .hourlyRate(UPDATED_HOURLY_RATE)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResource.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResource))
            )
            .andExpect(status().isOk());

        // Validate the Resource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResourceUpdatableFieldsEquals(partialUpdatedResource, getPersistedResource(partialUpdatedResource));
    }

    @Test
    @Transactional
    void patchNonExistingResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resource.setId(longCount.incrementAndGet());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resourceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resource.setId(longCount.incrementAndGet());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resource.setId(longCount.incrementAndGet());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(resourceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResource() throws Exception {
        // Initialize the database
        insertedResource = resourceRepository.saveAndFlush(resource);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the resource
        restResourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, resource.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return resourceRepository.count();
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

    protected Resource getPersistedResource(Resource resource) {
        return resourceRepository.findById(resource.getId()).orElseThrow();
    }

    protected void assertPersistedResourceToMatchAllProperties(Resource expectedResource) {
        assertResourceAllPropertiesEquals(expectedResource, getPersistedResource(expectedResource));
    }

    protected void assertPersistedResourceToMatchUpdatableProperties(Resource expectedResource) {
        assertResourceAllUpdatablePropertiesEquals(expectedResource, getPersistedResource(expectedResource));
    }
}
