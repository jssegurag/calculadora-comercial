package com.trycore.quotizo.web.rest;

import static com.trycore.quotizo.domain.ResourceAllocationAsserts.*;
import static com.trycore.quotizo.web.rest.TestUtil.createUpdateProxyForBean;
import static com.trycore.quotizo.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trycore.quotizo.IntegrationTest;
import com.trycore.quotizo.domain.Budget;
import com.trycore.quotizo.domain.BudgetTemplate;
import com.trycore.quotizo.domain.Resource;
import com.trycore.quotizo.domain.ResourceAllocation;
import com.trycore.quotizo.repository.ResourceAllocationRepository;
import com.trycore.quotizo.service.dto.ResourceAllocationDTO;
import com.trycore.quotizo.service.mapper.ResourceAllocationMapper;
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
 * Integration tests for the {@link ResourceAllocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResourceAllocationResourceIT {

    private static final BigDecimal DEFAULT_ASSIGNED_HOURS = new BigDecimal(1);
    private static final BigDecimal UPDATED_ASSIGNED_HOURS = new BigDecimal(2);
    private static final BigDecimal SMALLER_ASSIGNED_HOURS = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_COST = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_UNITS = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNITS = new BigDecimal(2);
    private static final BigDecimal SMALLER_UNITS = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CAPACITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_CAPACITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_CAPACITY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PLANNED_HOURS = new BigDecimal(1);
    private static final BigDecimal UPDATED_PLANNED_HOURS = new BigDecimal(2);
    private static final BigDecimal SMALLER_PLANNED_HOURS = new BigDecimal(1 - 1);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/resource-allocations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResourceAllocationRepository resourceAllocationRepository;

    @Autowired
    private ResourceAllocationMapper resourceAllocationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResourceAllocationMockMvc;

    private ResourceAllocation resourceAllocation;

    private ResourceAllocation insertedResourceAllocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceAllocation createEntity(EntityManager em) {
        ResourceAllocation resourceAllocation = new ResourceAllocation()
            .assignedHours(DEFAULT_ASSIGNED_HOURS)
            .totalCost(DEFAULT_TOTAL_COST)
            .units(DEFAULT_UNITS)
            .capacity(DEFAULT_CAPACITY)
            .plannedHours(DEFAULT_PLANNED_HOURS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return resourceAllocation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceAllocation createUpdatedEntity(EntityManager em) {
        ResourceAllocation resourceAllocation = new ResourceAllocation()
            .assignedHours(UPDATED_ASSIGNED_HOURS)
            .totalCost(UPDATED_TOTAL_COST)
            .units(UPDATED_UNITS)
            .capacity(UPDATED_CAPACITY)
            .plannedHours(UPDATED_PLANNED_HOURS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return resourceAllocation;
    }

    @BeforeEach
    public void initTest() {
        resourceAllocation = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedResourceAllocation != null) {
            resourceAllocationRepository.delete(insertedResourceAllocation);
            insertedResourceAllocation = null;
        }
    }

    @Test
    @Transactional
    void createResourceAllocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ResourceAllocation
        ResourceAllocationDTO resourceAllocationDTO = resourceAllocationMapper.toDto(resourceAllocation);
        var returnedResourceAllocationDTO = om.readValue(
            restResourceAllocationMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(resourceAllocationDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ResourceAllocationDTO.class
        );

        // Validate the ResourceAllocation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedResourceAllocation = resourceAllocationMapper.toEntity(returnedResourceAllocationDTO);
        assertResourceAllocationUpdatableFieldsEquals(
            returnedResourceAllocation,
            getPersistedResourceAllocation(returnedResourceAllocation)
        );

        insertedResourceAllocation = returnedResourceAllocation;
    }

    @Test
    @Transactional
    void createResourceAllocationWithExistingId() throws Exception {
        // Create the ResourceAllocation with an existing ID
        resourceAllocation.setId(1L);
        ResourceAllocationDTO resourceAllocationDTO = resourceAllocationMapper.toDto(resourceAllocation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceAllocationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resourceAllocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAssignedHoursIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resourceAllocation.setAssignedHours(null);

        // Create the ResourceAllocation, which fails.
        ResourceAllocationDTO resourceAllocationDTO = resourceAllocationMapper.toDto(resourceAllocation);

        restResourceAllocationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resourceAllocationDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllResourceAllocations() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList
        restResourceAllocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceAllocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].assignedHours").value(hasItem(sameNumber(DEFAULT_ASSIGNED_HOURS))))
            .andExpect(jsonPath("$.[*].totalCost").value(hasItem(sameNumber(DEFAULT_TOTAL_COST))))
            .andExpect(jsonPath("$.[*].units").value(hasItem(sameNumber(DEFAULT_UNITS))))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(sameNumber(DEFAULT_CAPACITY))))
            .andExpect(jsonPath("$.[*].plannedHours").value(hasItem(sameNumber(DEFAULT_PLANNED_HOURS))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getResourceAllocation() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get the resourceAllocation
        restResourceAllocationMockMvc
            .perform(get(ENTITY_API_URL_ID, resourceAllocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resourceAllocation.getId().intValue()))
            .andExpect(jsonPath("$.assignedHours").value(sameNumber(DEFAULT_ASSIGNED_HOURS)))
            .andExpect(jsonPath("$.totalCost").value(sameNumber(DEFAULT_TOTAL_COST)))
            .andExpect(jsonPath("$.units").value(sameNumber(DEFAULT_UNITS)))
            .andExpect(jsonPath("$.capacity").value(sameNumber(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.plannedHours").value(sameNumber(DEFAULT_PLANNED_HOURS)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getResourceAllocationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        Long id = resourceAllocation.getId();

        defaultResourceAllocationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultResourceAllocationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultResourceAllocationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByAssignedHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where assignedHours equals to
        defaultResourceAllocationFiltering(
            "assignedHours.equals=" + DEFAULT_ASSIGNED_HOURS,
            "assignedHours.equals=" + UPDATED_ASSIGNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByAssignedHoursIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where assignedHours in
        defaultResourceAllocationFiltering(
            "assignedHours.in=" + DEFAULT_ASSIGNED_HOURS + "," + UPDATED_ASSIGNED_HOURS,
            "assignedHours.in=" + UPDATED_ASSIGNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByAssignedHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where assignedHours is not null
        defaultResourceAllocationFiltering("assignedHours.specified=true", "assignedHours.specified=false");
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByAssignedHoursIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where assignedHours is greater than or equal to
        defaultResourceAllocationFiltering(
            "assignedHours.greaterThanOrEqual=" + DEFAULT_ASSIGNED_HOURS,
            "assignedHours.greaterThanOrEqual=" + UPDATED_ASSIGNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByAssignedHoursIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where assignedHours is less than or equal to
        defaultResourceAllocationFiltering(
            "assignedHours.lessThanOrEqual=" + DEFAULT_ASSIGNED_HOURS,
            "assignedHours.lessThanOrEqual=" + SMALLER_ASSIGNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByAssignedHoursIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where assignedHours is less than
        defaultResourceAllocationFiltering(
            "assignedHours.lessThan=" + UPDATED_ASSIGNED_HOURS,
            "assignedHours.lessThan=" + DEFAULT_ASSIGNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByAssignedHoursIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where assignedHours is greater than
        defaultResourceAllocationFiltering(
            "assignedHours.greaterThan=" + SMALLER_ASSIGNED_HOURS,
            "assignedHours.greaterThan=" + DEFAULT_ASSIGNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByTotalCostIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where totalCost equals to
        defaultResourceAllocationFiltering("totalCost.equals=" + DEFAULT_TOTAL_COST, "totalCost.equals=" + UPDATED_TOTAL_COST);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByTotalCostIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where totalCost in
        defaultResourceAllocationFiltering(
            "totalCost.in=" + DEFAULT_TOTAL_COST + "," + UPDATED_TOTAL_COST,
            "totalCost.in=" + UPDATED_TOTAL_COST
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByTotalCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where totalCost is not null
        defaultResourceAllocationFiltering("totalCost.specified=true", "totalCost.specified=false");
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByTotalCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where totalCost is greater than or equal to
        defaultResourceAllocationFiltering(
            "totalCost.greaterThanOrEqual=" + DEFAULT_TOTAL_COST,
            "totalCost.greaterThanOrEqual=" + UPDATED_TOTAL_COST
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByTotalCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where totalCost is less than or equal to
        defaultResourceAllocationFiltering(
            "totalCost.lessThanOrEqual=" + DEFAULT_TOTAL_COST,
            "totalCost.lessThanOrEqual=" + SMALLER_TOTAL_COST
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByTotalCostIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where totalCost is less than
        defaultResourceAllocationFiltering("totalCost.lessThan=" + UPDATED_TOTAL_COST, "totalCost.lessThan=" + DEFAULT_TOTAL_COST);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByTotalCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where totalCost is greater than
        defaultResourceAllocationFiltering("totalCost.greaterThan=" + SMALLER_TOTAL_COST, "totalCost.greaterThan=" + DEFAULT_TOTAL_COST);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByUnitsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where units equals to
        defaultResourceAllocationFiltering("units.equals=" + DEFAULT_UNITS, "units.equals=" + UPDATED_UNITS);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByUnitsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where units in
        defaultResourceAllocationFiltering("units.in=" + DEFAULT_UNITS + "," + UPDATED_UNITS, "units.in=" + UPDATED_UNITS);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByUnitsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where units is not null
        defaultResourceAllocationFiltering("units.specified=true", "units.specified=false");
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByUnitsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where units is greater than or equal to
        defaultResourceAllocationFiltering("units.greaterThanOrEqual=" + DEFAULT_UNITS, "units.greaterThanOrEqual=" + UPDATED_UNITS);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByUnitsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where units is less than or equal to
        defaultResourceAllocationFiltering("units.lessThanOrEqual=" + DEFAULT_UNITS, "units.lessThanOrEqual=" + SMALLER_UNITS);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByUnitsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where units is less than
        defaultResourceAllocationFiltering("units.lessThan=" + UPDATED_UNITS, "units.lessThan=" + DEFAULT_UNITS);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByUnitsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where units is greater than
        defaultResourceAllocationFiltering("units.greaterThan=" + SMALLER_UNITS, "units.greaterThan=" + DEFAULT_UNITS);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByCapacityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where capacity equals to
        defaultResourceAllocationFiltering("capacity.equals=" + DEFAULT_CAPACITY, "capacity.equals=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByCapacityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where capacity in
        defaultResourceAllocationFiltering("capacity.in=" + DEFAULT_CAPACITY + "," + UPDATED_CAPACITY, "capacity.in=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByCapacityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where capacity is not null
        defaultResourceAllocationFiltering("capacity.specified=true", "capacity.specified=false");
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByCapacityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where capacity is greater than or equal to
        defaultResourceAllocationFiltering(
            "capacity.greaterThanOrEqual=" + DEFAULT_CAPACITY,
            "capacity.greaterThanOrEqual=" + UPDATED_CAPACITY
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByCapacityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where capacity is less than or equal to
        defaultResourceAllocationFiltering("capacity.lessThanOrEqual=" + DEFAULT_CAPACITY, "capacity.lessThanOrEqual=" + SMALLER_CAPACITY);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByCapacityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where capacity is less than
        defaultResourceAllocationFiltering("capacity.lessThan=" + UPDATED_CAPACITY, "capacity.lessThan=" + DEFAULT_CAPACITY);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByCapacityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where capacity is greater than
        defaultResourceAllocationFiltering("capacity.greaterThan=" + SMALLER_CAPACITY, "capacity.greaterThan=" + DEFAULT_CAPACITY);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByPlannedHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where plannedHours equals to
        defaultResourceAllocationFiltering("plannedHours.equals=" + DEFAULT_PLANNED_HOURS, "plannedHours.equals=" + UPDATED_PLANNED_HOURS);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByPlannedHoursIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where plannedHours in
        defaultResourceAllocationFiltering(
            "plannedHours.in=" + DEFAULT_PLANNED_HOURS + "," + UPDATED_PLANNED_HOURS,
            "plannedHours.in=" + UPDATED_PLANNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByPlannedHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where plannedHours is not null
        defaultResourceAllocationFiltering("plannedHours.specified=true", "plannedHours.specified=false");
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByPlannedHoursIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where plannedHours is greater than or equal to
        defaultResourceAllocationFiltering(
            "plannedHours.greaterThanOrEqual=" + DEFAULT_PLANNED_HOURS,
            "plannedHours.greaterThanOrEqual=" + UPDATED_PLANNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByPlannedHoursIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where plannedHours is less than or equal to
        defaultResourceAllocationFiltering(
            "plannedHours.lessThanOrEqual=" + DEFAULT_PLANNED_HOURS,
            "plannedHours.lessThanOrEqual=" + SMALLER_PLANNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByPlannedHoursIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where plannedHours is less than
        defaultResourceAllocationFiltering(
            "plannedHours.lessThan=" + UPDATED_PLANNED_HOURS,
            "plannedHours.lessThan=" + DEFAULT_PLANNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByPlannedHoursIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where plannedHours is greater than
        defaultResourceAllocationFiltering(
            "plannedHours.greaterThan=" + SMALLER_PLANNED_HOURS,
            "plannedHours.greaterThan=" + DEFAULT_PLANNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where createdBy equals to
        defaultResourceAllocationFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where createdBy in
        defaultResourceAllocationFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where createdBy is not null
        defaultResourceAllocationFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where createdBy contains
        defaultResourceAllocationFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where createdBy does not contain
        defaultResourceAllocationFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where createdDate equals to
        defaultResourceAllocationFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where createdDate in
        defaultResourceAllocationFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where createdDate is not null
        defaultResourceAllocationFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where lastModifiedBy equals to
        defaultResourceAllocationFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where lastModifiedBy in
        defaultResourceAllocationFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where lastModifiedBy is not null
        defaultResourceAllocationFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where lastModifiedBy contains
        defaultResourceAllocationFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where lastModifiedBy does not contain
        defaultResourceAllocationFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where lastModifiedDate equals to
        defaultResourceAllocationFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where lastModifiedDate in
        defaultResourceAllocationFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        // Get all the resourceAllocationList where lastModifiedDate is not null
        defaultResourceAllocationFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByBudgetIsEqualToSomething() throws Exception {
        Budget budget;
        if (TestUtil.findAll(em, Budget.class).isEmpty()) {
            resourceAllocationRepository.saveAndFlush(resourceAllocation);
            budget = BudgetResourceIT.createEntity(em);
        } else {
            budget = TestUtil.findAll(em, Budget.class).get(0);
        }
        em.persist(budget);
        em.flush();
        resourceAllocation.setBudget(budget);
        resourceAllocationRepository.saveAndFlush(resourceAllocation);
        Long budgetId = budget.getId();
        // Get all the resourceAllocationList where budget equals to budgetId
        defaultResourceAllocationShouldBeFound("budgetId.equals=" + budgetId);

        // Get all the resourceAllocationList where budget equals to (budgetId + 1)
        defaultResourceAllocationShouldNotBeFound("budgetId.equals=" + (budgetId + 1));
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByResourceIsEqualToSomething() throws Exception {
        Resource resource;
        if (TestUtil.findAll(em, Resource.class).isEmpty()) {
            resourceAllocationRepository.saveAndFlush(resourceAllocation);
            resource = ResourceResourceIT.createEntity(em);
        } else {
            resource = TestUtil.findAll(em, Resource.class).get(0);
        }
        em.persist(resource);
        em.flush();
        resourceAllocation.setResource(resource);
        resourceAllocationRepository.saveAndFlush(resourceAllocation);
        Long resourceId = resource.getId();
        // Get all the resourceAllocationList where resource equals to resourceId
        defaultResourceAllocationShouldBeFound("resourceId.equals=" + resourceId);

        // Get all the resourceAllocationList where resource equals to (resourceId + 1)
        defaultResourceAllocationShouldNotBeFound("resourceId.equals=" + (resourceId + 1));
    }

    @Test
    @Transactional
    void getAllResourceAllocationsByBudgetTemplateIsEqualToSomething() throws Exception {
        BudgetTemplate budgetTemplate;
        if (TestUtil.findAll(em, BudgetTemplate.class).isEmpty()) {
            resourceAllocationRepository.saveAndFlush(resourceAllocation);
            budgetTemplate = BudgetTemplateResourceIT.createEntity(em);
        } else {
            budgetTemplate = TestUtil.findAll(em, BudgetTemplate.class).get(0);
        }
        em.persist(budgetTemplate);
        em.flush();
        resourceAllocation.setBudgetTemplate(budgetTemplate);
        resourceAllocationRepository.saveAndFlush(resourceAllocation);
        Long budgetTemplateId = budgetTemplate.getId();
        // Get all the resourceAllocationList where budgetTemplate equals to budgetTemplateId
        defaultResourceAllocationShouldBeFound("budgetTemplateId.equals=" + budgetTemplateId);

        // Get all the resourceAllocationList where budgetTemplate equals to (budgetTemplateId + 1)
        defaultResourceAllocationShouldNotBeFound("budgetTemplateId.equals=" + (budgetTemplateId + 1));
    }

    private void defaultResourceAllocationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultResourceAllocationShouldBeFound(shouldBeFound);
        defaultResourceAllocationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultResourceAllocationShouldBeFound(String filter) throws Exception {
        restResourceAllocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceAllocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].assignedHours").value(hasItem(sameNumber(DEFAULT_ASSIGNED_HOURS))))
            .andExpect(jsonPath("$.[*].totalCost").value(hasItem(sameNumber(DEFAULT_TOTAL_COST))))
            .andExpect(jsonPath("$.[*].units").value(hasItem(sameNumber(DEFAULT_UNITS))))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(sameNumber(DEFAULT_CAPACITY))))
            .andExpect(jsonPath("$.[*].plannedHours").value(hasItem(sameNumber(DEFAULT_PLANNED_HOURS))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restResourceAllocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultResourceAllocationShouldNotBeFound(String filter) throws Exception {
        restResourceAllocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restResourceAllocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingResourceAllocation() throws Exception {
        // Get the resourceAllocation
        restResourceAllocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResourceAllocation() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resourceAllocation
        ResourceAllocation updatedResourceAllocation = resourceAllocationRepository.findById(resourceAllocation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedResourceAllocation are not directly saved in db
        em.detach(updatedResourceAllocation);
        updatedResourceAllocation
            .assignedHours(UPDATED_ASSIGNED_HOURS)
            .totalCost(UPDATED_TOTAL_COST)
            .units(UPDATED_UNITS)
            .capacity(UPDATED_CAPACITY)
            .plannedHours(UPDATED_PLANNED_HOURS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ResourceAllocationDTO resourceAllocationDTO = resourceAllocationMapper.toDto(updatedResourceAllocation);

        restResourceAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resourceAllocationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resourceAllocationDTO))
            )
            .andExpect(status().isOk());

        // Validate the ResourceAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResourceAllocationToMatchAllProperties(updatedResourceAllocation);
    }

    @Test
    @Transactional
    void putNonExistingResourceAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceAllocation.setId(longCount.incrementAndGet());

        // Create the ResourceAllocation
        ResourceAllocationDTO resourceAllocationDTO = resourceAllocationMapper.toDto(resourceAllocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resourceAllocationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resourceAllocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResourceAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceAllocation.setId(longCount.incrementAndGet());

        // Create the ResourceAllocation
        ResourceAllocationDTO resourceAllocationDTO = resourceAllocationMapper.toDto(resourceAllocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resourceAllocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResourceAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceAllocation.setId(longCount.incrementAndGet());

        // Create the ResourceAllocation
        ResourceAllocationDTO resourceAllocationDTO = resourceAllocationMapper.toDto(resourceAllocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceAllocationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resourceAllocationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResourceAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResourceAllocationWithPatch() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resourceAllocation using partial update
        ResourceAllocation partialUpdatedResourceAllocation = new ResourceAllocation();
        partialUpdatedResourceAllocation.setId(resourceAllocation.getId());

        partialUpdatedResourceAllocation
            .units(UPDATED_UNITS)
            .plannedHours(UPDATED_PLANNED_HOURS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restResourceAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResourceAllocation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResourceAllocation))
            )
            .andExpect(status().isOk());

        // Validate the ResourceAllocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResourceAllocationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedResourceAllocation, resourceAllocation),
            getPersistedResourceAllocation(resourceAllocation)
        );
    }

    @Test
    @Transactional
    void fullUpdateResourceAllocationWithPatch() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resourceAllocation using partial update
        ResourceAllocation partialUpdatedResourceAllocation = new ResourceAllocation();
        partialUpdatedResourceAllocation.setId(resourceAllocation.getId());

        partialUpdatedResourceAllocation
            .assignedHours(UPDATED_ASSIGNED_HOURS)
            .totalCost(UPDATED_TOTAL_COST)
            .units(UPDATED_UNITS)
            .capacity(UPDATED_CAPACITY)
            .plannedHours(UPDATED_PLANNED_HOURS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restResourceAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResourceAllocation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResourceAllocation))
            )
            .andExpect(status().isOk());

        // Validate the ResourceAllocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResourceAllocationUpdatableFieldsEquals(
            partialUpdatedResourceAllocation,
            getPersistedResourceAllocation(partialUpdatedResourceAllocation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingResourceAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceAllocation.setId(longCount.incrementAndGet());

        // Create the ResourceAllocation
        ResourceAllocationDTO resourceAllocationDTO = resourceAllocationMapper.toDto(resourceAllocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resourceAllocationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resourceAllocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResourceAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceAllocation.setId(longCount.incrementAndGet());

        // Create the ResourceAllocation
        ResourceAllocationDTO resourceAllocationDTO = resourceAllocationMapper.toDto(resourceAllocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resourceAllocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResourceAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceAllocation.setId(longCount.incrementAndGet());

        // Create the ResourceAllocation
        ResourceAllocationDTO resourceAllocationDTO = resourceAllocationMapper.toDto(resourceAllocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resourceAllocationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResourceAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResourceAllocation() throws Exception {
        // Initialize the database
        insertedResourceAllocation = resourceAllocationRepository.saveAndFlush(resourceAllocation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the resourceAllocation
        restResourceAllocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, resourceAllocation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return resourceAllocationRepository.count();
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

    protected ResourceAllocation getPersistedResourceAllocation(ResourceAllocation resourceAllocation) {
        return resourceAllocationRepository.findById(resourceAllocation.getId()).orElseThrow();
    }

    protected void assertPersistedResourceAllocationToMatchAllProperties(ResourceAllocation expectedResourceAllocation) {
        assertResourceAllocationAllPropertiesEquals(expectedResourceAllocation, getPersistedResourceAllocation(expectedResourceAllocation));
    }

    protected void assertPersistedResourceAllocationToMatchUpdatableProperties(ResourceAllocation expectedResourceAllocation) {
        assertResourceAllocationAllUpdatablePropertiesEquals(
            expectedResourceAllocation,
            getPersistedResourceAllocation(expectedResourceAllocation)
        );
    }
}
