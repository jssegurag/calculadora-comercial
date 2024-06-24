package com.trycore.quotizo.web.rest;

import static com.trycore.quotizo.domain.DroolsRuleFileAsserts.*;
import static com.trycore.quotizo.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trycore.quotizo.IntegrationTest;
import com.trycore.quotizo.domain.DroolsRuleFile;
import com.trycore.quotizo.repository.DroolsRuleFileRepository;
import com.trycore.quotizo.service.dto.DroolsRuleFileDTO;
import com.trycore.quotizo.service.mapper.DroolsRuleFileMapper;
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
 * Integration tests for the {@link DroolsRuleFileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DroolsRuleFileResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_FILE_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/drools-rule-files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DroolsRuleFileRepository droolsRuleFileRepository;

    @Autowired
    private DroolsRuleFileMapper droolsRuleFileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDroolsRuleFileMockMvc;

    private DroolsRuleFile droolsRuleFile;

    private DroolsRuleFile insertedDroolsRuleFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DroolsRuleFile createEntity(EntityManager em) {
        DroolsRuleFile droolsRuleFile = new DroolsRuleFile()
            .fileName(DEFAULT_FILE_NAME)
            .fileContent(DEFAULT_FILE_CONTENT)
            .description(DEFAULT_DESCRIPTION)
            .active(DEFAULT_ACTIVE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return droolsRuleFile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DroolsRuleFile createUpdatedEntity(EntityManager em) {
        DroolsRuleFile droolsRuleFile = new DroolsRuleFile()
            .fileName(UPDATED_FILE_NAME)
            .fileContent(UPDATED_FILE_CONTENT)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return droolsRuleFile;
    }

    @BeforeEach
    public void initTest() {
        droolsRuleFile = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDroolsRuleFile != null) {
            droolsRuleFileRepository.delete(insertedDroolsRuleFile);
            insertedDroolsRuleFile = null;
        }
    }

    @Test
    @Transactional
    void createDroolsRuleFile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DroolsRuleFile
        DroolsRuleFileDTO droolsRuleFileDTO = droolsRuleFileMapper.toDto(droolsRuleFile);
        var returnedDroolsRuleFileDTO = om.readValue(
            restDroolsRuleFileMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(droolsRuleFileDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DroolsRuleFileDTO.class
        );

        // Validate the DroolsRuleFile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDroolsRuleFile = droolsRuleFileMapper.toEntity(returnedDroolsRuleFileDTO);
        assertDroolsRuleFileUpdatableFieldsEquals(returnedDroolsRuleFile, getPersistedDroolsRuleFile(returnedDroolsRuleFile));

        insertedDroolsRuleFile = returnedDroolsRuleFile;
    }

    @Test
    @Transactional
    void createDroolsRuleFileWithExistingId() throws Exception {
        // Create the DroolsRuleFile with an existing ID
        droolsRuleFile.setId(1L);
        DroolsRuleFileDTO droolsRuleFileDTO = droolsRuleFileMapper.toDto(droolsRuleFile);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDroolsRuleFileMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(droolsRuleFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DroolsRuleFile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFileNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        droolsRuleFile.setFileName(null);

        // Create the DroolsRuleFile, which fails.
        DroolsRuleFileDTO droolsRuleFileDTO = droolsRuleFileMapper.toDto(droolsRuleFile);

        restDroolsRuleFileMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(droolsRuleFileDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        droolsRuleFile.setActive(null);

        // Create the DroolsRuleFile, which fails.
        DroolsRuleFileDTO droolsRuleFileDTO = droolsRuleFileMapper.toDto(droolsRuleFile);

        restDroolsRuleFileMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(droolsRuleFileDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDroolsRuleFiles() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList
        restDroolsRuleFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(droolsRuleFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileContent").value(hasItem(DEFAULT_FILE_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getDroolsRuleFile() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get the droolsRuleFile
        restDroolsRuleFileMockMvc
            .perform(get(ENTITY_API_URL_ID, droolsRuleFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(droolsRuleFile.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.fileContent").value(DEFAULT_FILE_CONTENT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getDroolsRuleFilesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        Long id = droolsRuleFile.getId();

        defaultDroolsRuleFileFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDroolsRuleFileFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDroolsRuleFileFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where fileName equals to
        defaultDroolsRuleFileFiltering("fileName.equals=" + DEFAULT_FILE_NAME, "fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where fileName in
        defaultDroolsRuleFileFiltering("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME, "fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where fileName is not null
        defaultDroolsRuleFileFiltering("fileName.specified=true", "fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByFileNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where fileName contains
        defaultDroolsRuleFileFiltering("fileName.contains=" + DEFAULT_FILE_NAME, "fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where fileName does not contain
        defaultDroolsRuleFileFiltering("fileName.doesNotContain=" + UPDATED_FILE_NAME, "fileName.doesNotContain=" + DEFAULT_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where description equals to
        defaultDroolsRuleFileFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where description in
        defaultDroolsRuleFileFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where description is not null
        defaultDroolsRuleFileFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where description contains
        defaultDroolsRuleFileFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where description does not contain
        defaultDroolsRuleFileFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where active equals to
        defaultDroolsRuleFileFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where active in
        defaultDroolsRuleFileFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where active is not null
        defaultDroolsRuleFileFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where createdBy equals to
        defaultDroolsRuleFileFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where createdBy in
        defaultDroolsRuleFileFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where createdBy is not null
        defaultDroolsRuleFileFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where createdBy contains
        defaultDroolsRuleFileFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where createdBy does not contain
        defaultDroolsRuleFileFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where createdDate equals to
        defaultDroolsRuleFileFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where createdDate in
        defaultDroolsRuleFileFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where createdDate is not null
        defaultDroolsRuleFileFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where lastModifiedBy equals to
        defaultDroolsRuleFileFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where lastModifiedBy in
        defaultDroolsRuleFileFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where lastModifiedBy is not null
        defaultDroolsRuleFileFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where lastModifiedBy contains
        defaultDroolsRuleFileFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where lastModifiedBy does not contain
        defaultDroolsRuleFileFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where lastModifiedDate equals to
        defaultDroolsRuleFileFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where lastModifiedDate in
        defaultDroolsRuleFileFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDroolsRuleFilesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        // Get all the droolsRuleFileList where lastModifiedDate is not null
        defaultDroolsRuleFileFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultDroolsRuleFileFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDroolsRuleFileShouldBeFound(shouldBeFound);
        defaultDroolsRuleFileShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDroolsRuleFileShouldBeFound(String filter) throws Exception {
        restDroolsRuleFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(droolsRuleFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileContent").value(hasItem(DEFAULT_FILE_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restDroolsRuleFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDroolsRuleFileShouldNotBeFound(String filter) throws Exception {
        restDroolsRuleFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDroolsRuleFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDroolsRuleFile() throws Exception {
        // Get the droolsRuleFile
        restDroolsRuleFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDroolsRuleFile() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the droolsRuleFile
        DroolsRuleFile updatedDroolsRuleFile = droolsRuleFileRepository.findById(droolsRuleFile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDroolsRuleFile are not directly saved in db
        em.detach(updatedDroolsRuleFile);
        updatedDroolsRuleFile
            .fileName(UPDATED_FILE_NAME)
            .fileContent(UPDATED_FILE_CONTENT)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        DroolsRuleFileDTO droolsRuleFileDTO = droolsRuleFileMapper.toDto(updatedDroolsRuleFile);

        restDroolsRuleFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, droolsRuleFileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(droolsRuleFileDTO))
            )
            .andExpect(status().isOk());

        // Validate the DroolsRuleFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDroolsRuleFileToMatchAllProperties(updatedDroolsRuleFile);
    }

    @Test
    @Transactional
    void putNonExistingDroolsRuleFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        droolsRuleFile.setId(longCount.incrementAndGet());

        // Create the DroolsRuleFile
        DroolsRuleFileDTO droolsRuleFileDTO = droolsRuleFileMapper.toDto(droolsRuleFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDroolsRuleFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, droolsRuleFileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(droolsRuleFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DroolsRuleFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDroolsRuleFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        droolsRuleFile.setId(longCount.incrementAndGet());

        // Create the DroolsRuleFile
        DroolsRuleFileDTO droolsRuleFileDTO = droolsRuleFileMapper.toDto(droolsRuleFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDroolsRuleFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(droolsRuleFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DroolsRuleFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDroolsRuleFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        droolsRuleFile.setId(longCount.incrementAndGet());

        // Create the DroolsRuleFile
        DroolsRuleFileDTO droolsRuleFileDTO = droolsRuleFileMapper.toDto(droolsRuleFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDroolsRuleFileMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(droolsRuleFileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DroolsRuleFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDroolsRuleFileWithPatch() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the droolsRuleFile using partial update
        DroolsRuleFile partialUpdatedDroolsRuleFile = new DroolsRuleFile();
        partialUpdatedDroolsRuleFile.setId(droolsRuleFile.getId());

        partialUpdatedDroolsRuleFile.createdBy(UPDATED_CREATED_BY).createdDate(UPDATED_CREATED_DATE);

        restDroolsRuleFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDroolsRuleFile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDroolsRuleFile))
            )
            .andExpect(status().isOk());

        // Validate the DroolsRuleFile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDroolsRuleFileUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDroolsRuleFile, droolsRuleFile),
            getPersistedDroolsRuleFile(droolsRuleFile)
        );
    }

    @Test
    @Transactional
    void fullUpdateDroolsRuleFileWithPatch() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the droolsRuleFile using partial update
        DroolsRuleFile partialUpdatedDroolsRuleFile = new DroolsRuleFile();
        partialUpdatedDroolsRuleFile.setId(droolsRuleFile.getId());

        partialUpdatedDroolsRuleFile
            .fileName(UPDATED_FILE_NAME)
            .fileContent(UPDATED_FILE_CONTENT)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restDroolsRuleFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDroolsRuleFile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDroolsRuleFile))
            )
            .andExpect(status().isOk());

        // Validate the DroolsRuleFile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDroolsRuleFileUpdatableFieldsEquals(partialUpdatedDroolsRuleFile, getPersistedDroolsRuleFile(partialUpdatedDroolsRuleFile));
    }

    @Test
    @Transactional
    void patchNonExistingDroolsRuleFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        droolsRuleFile.setId(longCount.incrementAndGet());

        // Create the DroolsRuleFile
        DroolsRuleFileDTO droolsRuleFileDTO = droolsRuleFileMapper.toDto(droolsRuleFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDroolsRuleFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, droolsRuleFileDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(droolsRuleFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DroolsRuleFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDroolsRuleFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        droolsRuleFile.setId(longCount.incrementAndGet());

        // Create the DroolsRuleFile
        DroolsRuleFileDTO droolsRuleFileDTO = droolsRuleFileMapper.toDto(droolsRuleFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDroolsRuleFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(droolsRuleFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DroolsRuleFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDroolsRuleFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        droolsRuleFile.setId(longCount.incrementAndGet());

        // Create the DroolsRuleFile
        DroolsRuleFileDTO droolsRuleFileDTO = droolsRuleFileMapper.toDto(droolsRuleFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDroolsRuleFileMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(droolsRuleFileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DroolsRuleFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDroolsRuleFile() throws Exception {
        // Initialize the database
        insertedDroolsRuleFile = droolsRuleFileRepository.saveAndFlush(droolsRuleFile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the droolsRuleFile
        restDroolsRuleFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, droolsRuleFile.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return droolsRuleFileRepository.count();
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

    protected DroolsRuleFile getPersistedDroolsRuleFile(DroolsRuleFile droolsRuleFile) {
        return droolsRuleFileRepository.findById(droolsRuleFile.getId()).orElseThrow();
    }

    protected void assertPersistedDroolsRuleFileToMatchAllProperties(DroolsRuleFile expectedDroolsRuleFile) {
        assertDroolsRuleFileAllPropertiesEquals(expectedDroolsRuleFile, getPersistedDroolsRuleFile(expectedDroolsRuleFile));
    }

    protected void assertPersistedDroolsRuleFileToMatchUpdatableProperties(DroolsRuleFile expectedDroolsRuleFile) {
        assertDroolsRuleFileAllUpdatablePropertiesEquals(expectedDroolsRuleFile, getPersistedDroolsRuleFile(expectedDroolsRuleFile));
    }
}
