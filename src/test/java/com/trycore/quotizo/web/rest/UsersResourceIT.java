package com.trycore.quotizo.web.rest;

import static com.trycore.quotizo.domain.UsersAsserts.*;
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
import com.trycore.quotizo.domain.UserRole;
import com.trycore.quotizo.domain.Users;
import com.trycore.quotizo.repository.UsersRepository;
import com.trycore.quotizo.service.UsersService;
import com.trycore.quotizo.service.dto.UsersDTO;
import com.trycore.quotizo.service.mapper.UsersMapper;
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
 * Integration tests for the {@link UsersResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UsersResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UsersRepository usersRepository;

    @Mock
    private UsersRepository usersRepositoryMock;

    @Autowired
    private UsersMapper usersMapper;

    @Mock
    private UsersService usersServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsersMockMvc;

    private Users users;

    private Users insertedUsers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Users createEntity(EntityManager em) {
        Users users = new Users()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD)
            .active(DEFAULT_ACTIVE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return users;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Users createUpdatedEntity(EntityManager em) {
        Users users = new Users()
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return users;
    }

    @BeforeEach
    public void initTest() {
        users = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUsers != null) {
            usersRepository.delete(insertedUsers);
            insertedUsers = null;
        }
    }

    @Test
    @Transactional
    void createUsers() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);
        var returnedUsersDTO = om.readValue(
            restUsersMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usersDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UsersDTO.class
        );

        // Validate the Users in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUsers = usersMapper.toEntity(returnedUsersDTO);
        assertUsersUpdatableFieldsEquals(returnedUsers, getPersistedUsers(returnedUsers));

        insertedUsers = returnedUsers;
    }

    @Test
    @Transactional
    void createUsersWithExistingId() throws Exception {
        // Create the Users with an existing ID
        users.setId(1L);
        UsersDTO usersDTO = usersMapper.toDto(users);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsersMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        users.setName(null);

        // Create the Users, which fails.
        UsersDTO usersDTO = usersMapper.toDto(users);

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usersDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        users.setEmail(null);

        // Create the Users, which fails.
        UsersDTO usersDTO = usersMapper.toDto(users);

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usersDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        users.setPassword(null);

        // Create the Users, which fails.
        UsersDTO usersDTO = usersMapper.toDto(users);

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usersDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        users.setActive(null);

        // Create the Users, which fails.
        UsersDTO usersDTO = usersMapper.toDto(users);

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usersDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsers() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(users.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(usersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUsersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(usersServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(usersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUsersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(usersRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUsers() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get the users
        restUsersMockMvc
            .perform(get(ENTITY_API_URL_ID, users.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(users.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getUsersByIdFiltering() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        Long id = users.getId();

        defaultUsersFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUsersFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUsersFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where name equals to
        defaultUsersFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where name in
        defaultUsersFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where name is not null
        defaultUsersFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where name contains
        defaultUsersFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where name does not contain
        defaultUsersFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where email equals to
        defaultUsersFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where email in
        defaultUsersFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where email is not null
        defaultUsersFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where email contains
        defaultUsersFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where email does not contain
        defaultUsersFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where password equals to
        defaultUsersFiltering("password.equals=" + DEFAULT_PASSWORD, "password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where password in
        defaultUsersFiltering("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD, "password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where password is not null
        defaultUsersFiltering("password.specified=true", "password.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByPasswordContainsSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where password contains
        defaultUsersFiltering("password.contains=" + DEFAULT_PASSWORD, "password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where password does not contain
        defaultUsersFiltering("password.doesNotContain=" + UPDATED_PASSWORD, "password.doesNotContain=" + DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where active equals to
        defaultUsersFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllUsersByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where active in
        defaultUsersFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllUsersByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where active is not null
        defaultUsersFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where createdBy equals to
        defaultUsersFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where createdBy in
        defaultUsersFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where createdBy is not null
        defaultUsersFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where createdBy contains
        defaultUsersFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where createdBy does not contain
        defaultUsersFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where createdDate equals to
        defaultUsersFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where createdDate in
        defaultUsersFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllUsersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where createdDate is not null
        defaultUsersFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModifiedBy equals to
        defaultUsersFiltering("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModifiedBy in
        defaultUsersFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModifiedBy is not null
        defaultUsersFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModifiedBy contains
        defaultUsersFiltering("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModifiedBy does not contain
        defaultUsersFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModifiedDate equals to
        defaultUsersFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModifiedDate in
        defaultUsersFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllUsersByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList where lastModifiedDate is not null
        defaultUsersFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByUserRoleIsEqualToSomething() throws Exception {
        UserRole userRole;
        if (TestUtil.findAll(em, UserRole.class).isEmpty()) {
            usersRepository.saveAndFlush(users);
            userRole = UserRoleResourceIT.createEntity(em);
        } else {
            userRole = TestUtil.findAll(em, UserRole.class).get(0);
        }
        em.persist(userRole);
        em.flush();
        users.addUserRole(userRole);
        usersRepository.saveAndFlush(users);
        Long userRoleId = userRole.getId();
        // Get all the usersList where userRole equals to userRoleId
        defaultUsersShouldBeFound("userRoleId.equals=" + userRoleId);

        // Get all the usersList where userRole equals to (userRoleId + 1)
        defaultUsersShouldNotBeFound("userRoleId.equals=" + (userRoleId + 1));
    }

    @Test
    @Transactional
    void getAllUsersByBudgetAuthorizedIsEqualToSomething() throws Exception {
        Budget budgetAuthorized;
        if (TestUtil.findAll(em, Budget.class).isEmpty()) {
            usersRepository.saveAndFlush(users);
            budgetAuthorized = BudgetResourceIT.createEntity(em);
        } else {
            budgetAuthorized = TestUtil.findAll(em, Budget.class).get(0);
        }
        em.persist(budgetAuthorized);
        em.flush();
        users.addBudgetAuthorized(budgetAuthorized);
        usersRepository.saveAndFlush(users);
        Long budgetAuthorizedId = budgetAuthorized.getId();
        // Get all the usersList where budgetAuthorized equals to budgetAuthorizedId
        defaultUsersShouldBeFound("budgetAuthorizedId.equals=" + budgetAuthorizedId);

        // Get all the usersList where budgetAuthorized equals to (budgetAuthorizedId + 1)
        defaultUsersShouldNotBeFound("budgetAuthorizedId.equals=" + (budgetAuthorizedId + 1));
    }

    private void defaultUsersFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUsersShouldBeFound(shouldBeFound);
        defaultUsersShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsersShouldBeFound(String filter) throws Exception {
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(users.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsersShouldNotBeFound(String filter) throws Exception {
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsers() throws Exception {
        // Get the users
        restUsersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUsers() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the users
        Users updatedUsers = usersRepository.findById(users.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUsers are not directly saved in db
        em.detach(updatedUsers);
        updatedUsers
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        UsersDTO usersDTO = usersMapper.toDto(updatedUsers);

        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usersDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(usersDTO))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUsersToMatchAllProperties(updatedUsers);
    }

    @Test
    @Transactional
    void putNonExistingUsers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        users.setId(longCount.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usersDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        users.setId(longCount.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        users.setId(longCount.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usersDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Users in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsersWithPatch() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the users using partial update
        Users partialUpdatedUsers = new Users();
        partialUpdatedUsers.setId(users.getId());

        partialUpdatedUsers.name(UPDATED_NAME).email(UPDATED_EMAIL).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsers.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUsers))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUsersUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUsers, users), getPersistedUsers(users));
    }

    @Test
    @Transactional
    void fullUpdateUsersWithPatch() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the users using partial update
        Users partialUpdatedUsers = new Users();
        partialUpdatedUsers.setId(users.getId());

        partialUpdatedUsers
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsers.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUsers))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUsersUpdatableFieldsEquals(partialUpdatedUsers, getPersistedUsers(partialUpdatedUsers));
    }

    @Test
    @Transactional
    void patchNonExistingUsers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        users.setId(longCount.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usersDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        users.setId(longCount.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        users.setId(longCount.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(usersDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Users in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsers() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the users
        restUsersMockMvc
            .perform(delete(ENTITY_API_URL_ID, users.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return usersRepository.count();
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

    protected Users getPersistedUsers(Users users) {
        return usersRepository.findById(users.getId()).orElseThrow();
    }

    protected void assertPersistedUsersToMatchAllProperties(Users expectedUsers) {
        assertUsersAllPropertiesEquals(expectedUsers, getPersistedUsers(expectedUsers));
    }

    protected void assertPersistedUsersToMatchUpdatableProperties(Users expectedUsers) {
        assertUsersAllUpdatablePropertiesEquals(expectedUsers, getPersistedUsers(expectedUsers));
    }
}
