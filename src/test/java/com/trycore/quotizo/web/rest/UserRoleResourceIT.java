package com.trycore.quotizo.web.rest;

import static com.trycore.quotizo.domain.UserRoleAsserts.*;
import static com.trycore.quotizo.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trycore.quotizo.IntegrationTest;
import com.trycore.quotizo.domain.Budget;
import com.trycore.quotizo.domain.FinancialParameter;
import com.trycore.quotizo.domain.Permission;
import com.trycore.quotizo.domain.UserRole;
import com.trycore.quotizo.domain.Users;
import com.trycore.quotizo.repository.UserRoleRepository;
import com.trycore.quotizo.service.UserRoleService;
import com.trycore.quotizo.service.dto.UserRoleDTO;
import com.trycore.quotizo.service.mapper.UserRoleMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserRoleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserRoleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserRoleRepository userRoleRepositoryMock;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Mock
    private UserRoleService userRoleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserRoleMockMvc;

    private UserRole userRole;

    private UserRole insertedUserRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserRole createEntity(EntityManager em) {
        UserRole userRole = new UserRole()
            .name(DEFAULT_NAME)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return userRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserRole createUpdatedEntity(EntityManager em) {
        UserRole userRole = new UserRole()
            .name(UPDATED_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return userRole;
    }

    @BeforeEach
    public void initTest() {
        userRole = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserRole != null) {
            userRoleRepository.delete(insertedUserRole);
            insertedUserRole = null;
        }
    }

    @Test
    @Transactional
    void createUserRole() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserRole
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);
        var returnedUserRoleDTO = om.readValue(
            restUserRoleMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userRoleDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserRoleDTO.class
        );

        // Validate the UserRole in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserRole = userRoleMapper.toEntity(returnedUserRoleDTO);
        assertUserRoleUpdatableFieldsEquals(returnedUserRole, getPersistedUserRole(returnedUserRole));

        insertedUserRole = returnedUserRole;
    }

    @Test
    @Transactional
    void createUserRoleWithExistingId() throws Exception {
        // Create the UserRole with an existing ID
        userRole.setId(1L);
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserRoleMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userRoleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserRole in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userRole.setName(null);

        // Create the UserRole, which fails.
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        restUserRoleMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userRoleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserRoles() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList
        restUserRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserRolesWithEagerRelationshipsIsEnabled() throws Exception {
        when(userRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userRoleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserRolesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userRoleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserRole() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get the userRole
        restUserRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, userRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userRole.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getUserRolesByIdFiltering() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        Long id = userRole.getId();

        defaultUserRoleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUserRoleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUserRoleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserRolesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where name equals to
        defaultUserRoleFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUserRolesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where name in
        defaultUserRoleFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUserRolesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where name is not null
        defaultUserRoleFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRolesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where name contains
        defaultUserRoleFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUserRolesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where name does not contain
        defaultUserRoleFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllUserRolesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where createdBy equals to
        defaultUserRoleFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUserRolesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where createdBy in
        defaultUserRoleFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUserRolesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where createdBy is not null
        defaultUserRoleFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRolesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where createdBy contains
        defaultUserRoleFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUserRolesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where createdBy does not contain
        defaultUserRoleFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUserRolesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where createdDate equals to
        defaultUserRoleFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllUserRolesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where createdDate in
        defaultUserRoleFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllUserRolesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where createdDate is not null
        defaultUserRoleFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRolesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where lastModifiedBy equals to
        defaultUserRoleFiltering("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUserRolesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where lastModifiedBy in
        defaultUserRoleFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllUserRolesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where lastModifiedBy is not null
        defaultUserRoleFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRolesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where lastModifiedBy contains
        defaultUserRoleFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllUserRolesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where lastModifiedBy does not contain
        defaultUserRoleFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllUserRolesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where lastModifiedDate equals to
        defaultUserRoleFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllUserRolesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where lastModifiedDate in
        defaultUserRoleFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllUserRolesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList where lastModifiedDate is not null
        defaultUserRoleFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRolesByPermissionIsEqualToSomething() throws Exception {
        Permission permission;
        if (TestUtil.findAll(em, Permission.class).isEmpty()) {
            userRoleRepository.saveAndFlush(userRole);
            permission = PermissionResourceIT.createEntity(em);
        } else {
            permission = TestUtil.findAll(em, Permission.class).get(0);
        }
        em.persist(permission);
        em.flush();
        userRole.addPermission(permission);
        userRoleRepository.saveAndFlush(userRole);
        Long permissionId = permission.getId();
        // Get all the userRoleList where permission equals to permissionId
        defaultUserRoleShouldBeFound("permissionId.equals=" + permissionId);

        // Get all the userRoleList where permission equals to (permissionId + 1)
        defaultUserRoleShouldNotBeFound("permissionId.equals=" + (permissionId + 1));
    }

    @Test
    @Transactional
    void getAllUserRolesByBudgetIsEqualToSomething() throws Exception {
        Budget budget;
        if (TestUtil.findAll(em, Budget.class).isEmpty()) {
            userRoleRepository.saveAndFlush(userRole);
            budget = BudgetResourceIT.createEntity(em);
        } else {
            budget = TestUtil.findAll(em, Budget.class).get(0);
        }
        em.persist(budget);
        em.flush();
        userRole.addBudget(budget);
        userRoleRepository.saveAndFlush(userRole);
        Long budgetId = budget.getId();
        // Get all the userRoleList where budget equals to budgetId
        defaultUserRoleShouldBeFound("budgetId.equals=" + budgetId);

        // Get all the userRoleList where budget equals to (budgetId + 1)
        defaultUserRoleShouldNotBeFound("budgetId.equals=" + (budgetId + 1));
    }

    @Test
    @Transactional
    void getAllUserRolesByFinancialParameterIsEqualToSomething() throws Exception {
        FinancialParameter financialParameter;
        if (TestUtil.findAll(em, FinancialParameter.class).isEmpty()) {
            userRoleRepository.saveAndFlush(userRole);
            financialParameter = FinancialParameterResourceIT.createEntity(em);
        } else {
            financialParameter = TestUtil.findAll(em, FinancialParameter.class).get(0);
        }
        em.persist(financialParameter);
        em.flush();
        userRole.addFinancialParameter(financialParameter);
        userRoleRepository.saveAndFlush(userRole);
        Long financialParameterId = financialParameter.getId();
        // Get all the userRoleList where financialParameter equals to financialParameterId
        defaultUserRoleShouldBeFound("financialParameterId.equals=" + financialParameterId);

        // Get all the userRoleList where financialParameter equals to (financialParameterId + 1)
        defaultUserRoleShouldNotBeFound("financialParameterId.equals=" + (financialParameterId + 1));
    }

    @Test
    @Transactional
    void getAllUserRolesByUsersIsEqualToSomething() throws Exception {
        Users users;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            userRoleRepository.saveAndFlush(userRole);
            users = UsersResourceIT.createEntity(em);
        } else {
            users = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(users);
        em.flush();
        userRole.addUsers(users);
        userRoleRepository.saveAndFlush(userRole);
        Long usersId = users.getId();
        // Get all the userRoleList where users equals to usersId
        defaultUserRoleShouldBeFound("usersId.equals=" + usersId);

        // Get all the userRoleList where users equals to (usersId + 1)
        defaultUserRoleShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    private void defaultUserRoleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUserRoleShouldBeFound(shouldBeFound);
        defaultUserRoleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserRoleShouldBeFound(String filter) throws Exception {
        restUserRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restUserRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserRoleShouldNotBeFound(String filter) throws Exception {
        restUserRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserRole() throws Exception {
        // Get the userRole
        restUserRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserRole() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userRole
        UserRole updatedUserRole = userRoleRepository.findById(userRole.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserRole are not directly saved in db
        em.detach(updatedUserRole);
        updatedUserRole
            .name(UPDATED_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(updatedUserRole);

        restUserRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userRoleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userRoleDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserRoleToMatchAllProperties(updatedUserRole);
    }

    @Test
    @Transactional
    void putNonExistingUserRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRole.setId(longCount.incrementAndGet());

        // Create the UserRole
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userRoleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRole.setId(longCount.incrementAndGet());

        // Create the UserRole
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRole.setId(longCount.incrementAndGet());

        // Create the UserRole
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userRoleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserRoleWithPatch() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userRole using partial update
        UserRole partialUpdatedUserRole = new UserRole();
        partialUpdatedUserRole.setId(userRole.getId());

        partialUpdatedUserRole.name(UPDATED_NAME).createdBy(UPDATED_CREATED_BY);

        restUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserRole.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserRole))
            )
            .andExpect(status().isOk());

        // Validate the UserRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserRoleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUserRole, userRole), getPersistedUserRole(userRole));
    }

    @Test
    @Transactional
    void fullUpdateUserRoleWithPatch() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userRole using partial update
        UserRole partialUpdatedUserRole = new UserRole();
        partialUpdatedUserRole.setId(userRole.getId());

        partialUpdatedUserRole
            .name(UPDATED_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserRole.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserRole))
            )
            .andExpect(status().isOk());

        // Validate the UserRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserRoleUpdatableFieldsEquals(partialUpdatedUserRole, getPersistedUserRole(partialUpdatedUserRole));
    }

    @Test
    @Transactional
    void patchNonExistingUserRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRole.setId(longCount.incrementAndGet());

        // Create the UserRole
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userRoleDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRole.setId(longCount.incrementAndGet());

        // Create the UserRole
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRole.setId(longCount.incrementAndGet());

        // Create the UserRole
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRoleMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userRoleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserRole() throws Exception {
        // Initialize the database
        insertedUserRole = userRoleRepository.saveAndFlush(userRole);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userRole
        restUserRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, userRole.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userRoleRepository.count();
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

    protected UserRole getPersistedUserRole(UserRole userRole) {
        return userRoleRepository.findById(userRole.getId()).orElseThrow();
    }

    protected void assertPersistedUserRoleToMatchAllProperties(UserRole expectedUserRole) {
        assertUserRoleAllPropertiesEquals(expectedUserRole, getPersistedUserRole(expectedUserRole));
    }

    protected void assertPersistedUserRoleToMatchUpdatableProperties(UserRole expectedUserRole) {
        assertUserRoleAllUpdatablePropertiesEquals(expectedUserRole, getPersistedUserRole(expectedUserRole));
    }
}
