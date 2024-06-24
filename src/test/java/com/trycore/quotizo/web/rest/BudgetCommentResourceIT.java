package com.trycore.quotizo.web.rest;

import static com.trycore.quotizo.domain.BudgetCommentAsserts.*;
import static com.trycore.quotizo.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trycore.quotizo.IntegrationTest;
import com.trycore.quotizo.domain.Budget;
import com.trycore.quotizo.domain.BudgetComment;
import com.trycore.quotizo.repository.BudgetCommentRepository;
import com.trycore.quotizo.service.dto.BudgetCommentDTO;
import com.trycore.quotizo.service.mapper.BudgetCommentMapper;
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
 * Integration tests for the {@link BudgetCommentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BudgetCommentResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/budget-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BudgetCommentRepository budgetCommentRepository;

    @Autowired
    private BudgetCommentMapper budgetCommentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBudgetCommentMockMvc;

    private BudgetComment budgetComment;

    private BudgetComment insertedBudgetComment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BudgetComment createEntity(EntityManager em) {
        BudgetComment budgetComment = new BudgetComment()
            .content(DEFAULT_CONTENT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return budgetComment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BudgetComment createUpdatedEntity(EntityManager em) {
        BudgetComment budgetComment = new BudgetComment()
            .content(UPDATED_CONTENT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return budgetComment;
    }

    @BeforeEach
    public void initTest() {
        budgetComment = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedBudgetComment != null) {
            budgetCommentRepository.delete(insertedBudgetComment);
            insertedBudgetComment = null;
        }
    }

    @Test
    @Transactional
    void createBudgetComment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BudgetComment
        BudgetCommentDTO budgetCommentDTO = budgetCommentMapper.toDto(budgetComment);
        var returnedBudgetCommentDTO = om.readValue(
            restBudgetCommentMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(budgetCommentDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BudgetCommentDTO.class
        );

        // Validate the BudgetComment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBudgetComment = budgetCommentMapper.toEntity(returnedBudgetCommentDTO);
        assertBudgetCommentUpdatableFieldsEquals(returnedBudgetComment, getPersistedBudgetComment(returnedBudgetComment));

        insertedBudgetComment = returnedBudgetComment;
    }

    @Test
    @Transactional
    void createBudgetCommentWithExistingId() throws Exception {
        // Create the BudgetComment with an existing ID
        budgetComment.setId(1L);
        BudgetCommentDTO budgetCommentDTO = budgetCommentMapper.toDto(budgetComment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBudgetCommentMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetComment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBudgetComments() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get all the budgetCommentList
        restBudgetCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budgetComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getBudgetComment() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get the budgetComment
        restBudgetCommentMockMvc
            .perform(get(ENTITY_API_URL_ID, budgetComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(budgetComment.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getBudgetCommentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        Long id = budgetComment.getId();

        defaultBudgetCommentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBudgetCommentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBudgetCommentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBudgetCommentsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get all the budgetCommentList where createdBy equals to
        defaultBudgetCommentFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBudgetCommentsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get all the budgetCommentList where createdBy in
        defaultBudgetCommentFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllBudgetCommentsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get all the budgetCommentList where createdBy is not null
        defaultBudgetCommentFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetCommentsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get all the budgetCommentList where createdBy contains
        defaultBudgetCommentFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBudgetCommentsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get all the budgetCommentList where createdBy does not contain
        defaultBudgetCommentFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBudgetCommentsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get all the budgetCommentList where createdDate equals to
        defaultBudgetCommentFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetCommentsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get all the budgetCommentList where createdDate in
        defaultBudgetCommentFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllBudgetCommentsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get all the budgetCommentList where createdDate is not null
        defaultBudgetCommentFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetCommentsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get all the budgetCommentList where lastModifiedBy equals to
        defaultBudgetCommentFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllBudgetCommentsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get all the budgetCommentList where lastModifiedBy in
        defaultBudgetCommentFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllBudgetCommentsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get all the budgetCommentList where lastModifiedBy is not null
        defaultBudgetCommentFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetCommentsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get all the budgetCommentList where lastModifiedBy contains
        defaultBudgetCommentFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllBudgetCommentsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get all the budgetCommentList where lastModifiedBy does not contain
        defaultBudgetCommentFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllBudgetCommentsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get all the budgetCommentList where lastModifiedDate equals to
        defaultBudgetCommentFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllBudgetCommentsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get all the budgetCommentList where lastModifiedDate in
        defaultBudgetCommentFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllBudgetCommentsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        // Get all the budgetCommentList where lastModifiedDate is not null
        defaultBudgetCommentFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetCommentsByBudgetIsEqualToSomething() throws Exception {
        Budget budget;
        if (TestUtil.findAll(em, Budget.class).isEmpty()) {
            budgetCommentRepository.saveAndFlush(budgetComment);
            budget = BudgetResourceIT.createEntity(em);
        } else {
            budget = TestUtil.findAll(em, Budget.class).get(0);
        }
        em.persist(budget);
        em.flush();
        budgetComment.setBudget(budget);
        budgetCommentRepository.saveAndFlush(budgetComment);
        Long budgetId = budget.getId();
        // Get all the budgetCommentList where budget equals to budgetId
        defaultBudgetCommentShouldBeFound("budgetId.equals=" + budgetId);

        // Get all the budgetCommentList where budget equals to (budgetId + 1)
        defaultBudgetCommentShouldNotBeFound("budgetId.equals=" + (budgetId + 1));
    }

    private void defaultBudgetCommentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBudgetCommentShouldBeFound(shouldBeFound);
        defaultBudgetCommentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBudgetCommentShouldBeFound(String filter) throws Exception {
        restBudgetCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budgetComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restBudgetCommentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBudgetCommentShouldNotBeFound(String filter) throws Exception {
        restBudgetCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBudgetCommentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBudgetComment() throws Exception {
        // Get the budgetComment
        restBudgetCommentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBudgetComment() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budgetComment
        BudgetComment updatedBudgetComment = budgetCommentRepository.findById(budgetComment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBudgetComment are not directly saved in db
        em.detach(updatedBudgetComment);
        updatedBudgetComment
            .content(UPDATED_CONTENT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        BudgetCommentDTO budgetCommentDTO = budgetCommentMapper.toDto(updatedBudgetComment);

        restBudgetCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, budgetCommentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetCommentDTO))
            )
            .andExpect(status().isOk());

        // Validate the BudgetComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBudgetCommentToMatchAllProperties(updatedBudgetComment);
    }

    @Test
    @Transactional
    void putNonExistingBudgetComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetComment.setId(longCount.incrementAndGet());

        // Create the BudgetComment
        BudgetCommentDTO budgetCommentDTO = budgetCommentMapper.toDto(budgetComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBudgetCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, budgetCommentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBudgetComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetComment.setId(longCount.incrementAndGet());

        // Create the BudgetComment
        BudgetCommentDTO budgetCommentDTO = budgetCommentMapper.toDto(budgetComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBudgetComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetComment.setId(longCount.incrementAndGet());

        // Create the BudgetComment
        BudgetCommentDTO budgetCommentDTO = budgetCommentMapper.toDto(budgetComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetCommentMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetCommentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BudgetComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBudgetCommentWithPatch() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budgetComment using partial update
        BudgetComment partialUpdatedBudgetComment = new BudgetComment();
        partialUpdatedBudgetComment.setId(budgetComment.getId());

        partialUpdatedBudgetComment.lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restBudgetCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBudgetComment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBudgetComment))
            )
            .andExpect(status().isOk());

        // Validate the BudgetComment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBudgetCommentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBudgetComment, budgetComment),
            getPersistedBudgetComment(budgetComment)
        );
    }

    @Test
    @Transactional
    void fullUpdateBudgetCommentWithPatch() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budgetComment using partial update
        BudgetComment partialUpdatedBudgetComment = new BudgetComment();
        partialUpdatedBudgetComment.setId(budgetComment.getId());

        partialUpdatedBudgetComment
            .content(UPDATED_CONTENT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restBudgetCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBudgetComment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBudgetComment))
            )
            .andExpect(status().isOk());

        // Validate the BudgetComment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBudgetCommentUpdatableFieldsEquals(partialUpdatedBudgetComment, getPersistedBudgetComment(partialUpdatedBudgetComment));
    }

    @Test
    @Transactional
    void patchNonExistingBudgetComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetComment.setId(longCount.incrementAndGet());

        // Create the BudgetComment
        BudgetCommentDTO budgetCommentDTO = budgetCommentMapper.toDto(budgetComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBudgetCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, budgetCommentDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(budgetCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBudgetComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetComment.setId(longCount.incrementAndGet());

        // Create the BudgetComment
        BudgetCommentDTO budgetCommentDTO = budgetCommentMapper.toDto(budgetComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(budgetCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBudgetComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetComment.setId(longCount.incrementAndGet());

        // Create the BudgetComment
        BudgetCommentDTO budgetCommentDTO = budgetCommentMapper.toDto(budgetComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetCommentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(budgetCommentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BudgetComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBudgetComment() throws Exception {
        // Initialize the database
        insertedBudgetComment = budgetCommentRepository.saveAndFlush(budgetComment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the budgetComment
        restBudgetCommentMockMvc
            .perform(delete(ENTITY_API_URL_ID, budgetComment.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return budgetCommentRepository.count();
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

    protected BudgetComment getPersistedBudgetComment(BudgetComment budgetComment) {
        return budgetCommentRepository.findById(budgetComment.getId()).orElseThrow();
    }

    protected void assertPersistedBudgetCommentToMatchAllProperties(BudgetComment expectedBudgetComment) {
        assertBudgetCommentAllPropertiesEquals(expectedBudgetComment, getPersistedBudgetComment(expectedBudgetComment));
    }

    protected void assertPersistedBudgetCommentToMatchUpdatableProperties(BudgetComment expectedBudgetComment) {
        assertBudgetCommentAllUpdatablePropertiesEquals(expectedBudgetComment, getPersistedBudgetComment(expectedBudgetComment));
    }
}
