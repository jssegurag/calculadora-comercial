package com.trycore.quotizo.web.rest;

import static com.trycore.quotizo.domain.PositionAsserts.*;
import static com.trycore.quotizo.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trycore.quotizo.IntegrationTest;
import com.trycore.quotizo.domain.Position;
import com.trycore.quotizo.repository.PositionRepository;
import com.trycore.quotizo.service.dto.PositionDTO;
import com.trycore.quotizo.service.mapper.PositionMapper;
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
 * Integration tests for the {@link PositionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PositionResourceIT {

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

    private static final String ENTITY_API_URL = "/api/positions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPositionMockMvc;

    private Position position;

    private Position insertedPosition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Position createEntity(EntityManager em) {
        Position position = new Position()
            .name(DEFAULT_NAME)
            .active(DEFAULT_ACTIVE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return position;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Position createUpdatedEntity(EntityManager em) {
        Position position = new Position()
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return position;
    }

    @BeforeEach
    public void initTest() {
        position = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPosition != null) {
            positionRepository.delete(insertedPosition);
            insertedPosition = null;
        }
    }

    @Test
    @Transactional
    void createPosition() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);
        var returnedPositionDTO = om.readValue(
            restPositionMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PositionDTO.class
        );

        // Validate the Position in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPosition = positionMapper.toEntity(returnedPositionDTO);
        assertPositionUpdatableFieldsEquals(returnedPosition, getPersistedPosition(returnedPosition));

        insertedPosition = returnedPosition;
    }

    @Test
    @Transactional
    void createPositionWithExistingId() throws Exception {
        // Create the Position with an existing ID
        position.setId(1L);
        PositionDTO positionDTO = positionMapper.toDto(position);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        position.setName(null);

        // Create the Position, which fails.
        PositionDTO positionDTO = positionMapper.toDto(position);

        restPositionMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        position.setActive(null);

        // Create the Position, which fails.
        PositionDTO positionDTO = positionMapper.toDto(position);

        restPositionMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPositions() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(position.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getPosition() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get the position
        restPositionMockMvc
            .perform(get(ENTITY_API_URL_ID, position.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(position.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getPositionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        Long id = position.getId();

        defaultPositionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPositionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPositionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPositionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where name equals to
        defaultPositionFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPositionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where name in
        defaultPositionFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPositionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where name is not null
        defaultPositionFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where name contains
        defaultPositionFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPositionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where name does not contain
        defaultPositionFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPositionsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where active equals to
        defaultPositionFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPositionsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where active in
        defaultPositionFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPositionsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where active is not null
        defaultPositionFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where createdBy equals to
        defaultPositionFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllPositionsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where createdBy in
        defaultPositionFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllPositionsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where createdBy is not null
        defaultPositionFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where createdBy contains
        defaultPositionFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllPositionsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where createdBy does not contain
        defaultPositionFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllPositionsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where createdDate equals to
        defaultPositionFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllPositionsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where createdDate in
        defaultPositionFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where createdDate is not null
        defaultPositionFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where lastModifiedBy equals to
        defaultPositionFiltering("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPositionsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where lastModifiedBy in
        defaultPositionFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllPositionsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where lastModifiedBy is not null
        defaultPositionFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where lastModifiedBy contains
        defaultPositionFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllPositionsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where lastModifiedBy does not contain
        defaultPositionFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllPositionsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where lastModifiedDate equals to
        defaultPositionFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where lastModifiedDate in
        defaultPositionFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPositionsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        // Get all the positionList where lastModifiedDate is not null
        defaultPositionFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultPositionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPositionShouldBeFound(shouldBeFound);
        defaultPositionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPositionShouldBeFound(String filter) throws Exception {
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(position.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPositionShouldNotBeFound(String filter) throws Exception {
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPosition() throws Exception {
        // Get the position
        restPositionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPosition() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the position
        Position updatedPosition = positionRepository.findById(position.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPosition are not directly saved in db
        em.detach(updatedPosition);
        updatedPosition
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        PositionDTO positionDTO = positionMapper.toDto(updatedPosition);

        restPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPositionToMatchAllProperties(updatedPosition);
    }

    @Test
    @Transactional
    void putNonExistingPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePositionWithPatch() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the position using partial update
        Position partialUpdatedPosition = new Position();
        partialUpdatedPosition.setId(position.getId());

        partialUpdatedPosition.active(UPDATED_ACTIVE).lastModifiedBy(UPDATED_LAST_MODIFIED_BY).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPosition.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPosition))
            )
            .andExpect(status().isOk());

        // Validate the Position in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPosition, position), getPersistedPosition(position));
    }

    @Test
    @Transactional
    void fullUpdatePositionWithPatch() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the position using partial update
        Position partialUpdatedPosition = new Position();
        partialUpdatedPosition.setId(position.getId());

        partialUpdatedPosition
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPosition.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPosition))
            )
            .andExpect(status().isOk());

        // Validate the Position in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionUpdatableFieldsEquals(partialUpdatedPosition, getPersistedPosition(partialUpdatedPosition));
    }

    @Test
    @Transactional
    void patchNonExistingPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, positionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePosition() throws Exception {
        // Initialize the database
        insertedPosition = positionRepository.saveAndFlush(position);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the position
        restPositionMockMvc
            .perform(delete(ENTITY_API_URL_ID, position.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return positionRepository.count();
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

    protected Position getPersistedPosition(Position position) {
        return positionRepository.findById(position.getId()).orElseThrow();
    }

    protected void assertPersistedPositionToMatchAllProperties(Position expectedPosition) {
        assertPositionAllPropertiesEquals(expectedPosition, getPersistedPosition(expectedPosition));
    }

    protected void assertPersistedPositionToMatchUpdatableProperties(Position expectedPosition) {
        assertPositionAllUpdatablePropertiesEquals(expectedPosition, getPersistedPosition(expectedPosition));
    }
}
