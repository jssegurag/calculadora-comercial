package com.trycore.quotizo.web.rest;

import static com.trycore.quotizo.domain.BudgetAsserts.*;
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
import com.trycore.quotizo.domain.Country;
import com.trycore.quotizo.domain.UserRole;
import com.trycore.quotizo.domain.Users;
import com.trycore.quotizo.repository.BudgetRepository;
import com.trycore.quotizo.service.dto.BudgetDTO;
import com.trycore.quotizo.service.mapper.BudgetMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link BudgetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BudgetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_ESTIMATED_DURATION_DAYS = 1;
    private static final Integer UPDATED_ESTIMATED_DURATION_DAYS = 2;
    private static final Integer SMALLER_ESTIMATED_DURATION_DAYS = 1 - 1;

    private static final Integer DEFAULT_DURATION_MONTHS = 1;
    private static final Integer UPDATED_DURATION_MONTHS = 2;
    private static final Integer SMALLER_DURATION_MONTHS = 1 - 1;

    private static final BigDecimal DEFAULT_MONTHLY_HOURS = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTHLY_HOURS = new BigDecimal(2);
    private static final BigDecimal SMALLER_MONTHLY_HOURS = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PLANNED_HOURS = new BigDecimal(1);
    private static final BigDecimal UPDATED_PLANNED_HOURS = new BigDecimal(2);
    private static final BigDecimal SMALLER_PLANNED_HOURS = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_RESOURCE_COUNT = 1;
    private static final Integer UPDATED_RESOURCE_COUNT = 2;
    private static final Integer SMALLER_RESOURCE_COUNT = 1 - 1;

    private static final BigDecimal DEFAULT_INCOME = new BigDecimal(1);
    private static final BigDecimal UPDATED_INCOME = new BigDecimal(2);
    private static final BigDecimal SMALLER_INCOME = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_OTHER_TAXES = new BigDecimal(1);
    private static final BigDecimal UPDATED_OTHER_TAXES = new BigDecimal(2);
    private static final BigDecimal SMALLER_OTHER_TAXES = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRIPTION_OTHER_TAXES = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_OTHER_TAXES = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_WITHHOLDING_TAXES = new BigDecimal(1);
    private static final BigDecimal UPDATED_WITHHOLDING_TAXES = new BigDecimal(2);
    private static final BigDecimal SMALLER_WITHHOLDING_TAXES = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_MOD_AND_CIF_COSTS = new BigDecimal(1);
    private static final BigDecimal UPDATED_MOD_AND_CIF_COSTS = new BigDecimal(2);
    private static final BigDecimal SMALLER_MOD_AND_CIF_COSTS = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_GROSS_PROFIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_GROSS_PROFIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_GROSS_PROFIT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_GROSS_PROFIT_PERCENTAGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_GROSS_PROFIT_PERCENTAGE = new BigDecimal(2);
    private static final BigDecimal SMALLER_GROSS_PROFIT_PERCENTAGE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_GROSS_PROFIT_RULE = new BigDecimal(1);
    private static final BigDecimal UPDATED_GROSS_PROFIT_RULE = new BigDecimal(2);
    private static final BigDecimal SMALLER_GROSS_PROFIT_RULE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ABSORBED_FIXED_COSTS = new BigDecimal(1);
    private static final BigDecimal UPDATED_ABSORBED_FIXED_COSTS = new BigDecimal(2);
    private static final BigDecimal SMALLER_ABSORBED_FIXED_COSTS = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_OTHER_EXPENSES = new BigDecimal(1);
    private static final BigDecimal UPDATED_OTHER_EXPENSES = new BigDecimal(2);
    private static final BigDecimal SMALLER_OTHER_EXPENSES = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PROFIT_BEFORE_TAX = new BigDecimal(1);
    private static final BigDecimal UPDATED_PROFIT_BEFORE_TAX = new BigDecimal(2);
    private static final BigDecimal SMALLER_PROFIT_BEFORE_TAX = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ESTIMATED_TAXES = new BigDecimal(1);
    private static final BigDecimal UPDATED_ESTIMATED_TAXES = new BigDecimal(2);
    private static final BigDecimal SMALLER_ESTIMATED_TAXES = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ESTIMATED_NET_PROFIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_ESTIMATED_NET_PROFIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_ESTIMATED_NET_PROFIT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_NET_MARGIN_PERCENTAGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_NET_MARGIN_PERCENTAGE = new BigDecimal(2);
    private static final BigDecimal SMALLER_NET_MARGIN_PERCENTAGE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_NET_MARGIN_RULE = new BigDecimal(1);
    private static final BigDecimal UPDATED_NET_MARGIN_RULE = new BigDecimal(2);
    private static final BigDecimal SMALLER_NET_MARGIN_RULE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_COMMISSION_TO_RECEIVE = new BigDecimal(1);
    private static final BigDecimal UPDATED_COMMISSION_TO_RECEIVE = new BigDecimal(2);
    private static final BigDecimal SMALLER_COMMISSION_TO_RECEIVE = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_NEEDS_APPROVAL = false;
    private static final Boolean UPDATED_NEEDS_APPROVAL = true;

    private static final String DEFAULT_APPROVAL_DECISION = "AAAAAAAAAA";
    private static final String UPDATED_APPROVAL_DECISION = "BBBBBBBBBB";

    private static final Instant DEFAULT_APPROVAL_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_APPROVAL_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_APPROVAL_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_APPROVAL_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_APPROVAL_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_APPROVAL_COMMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_APPROVAL_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_APPROVAL_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/budgets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetMapper budgetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBudgetMockMvc;

    private Budget budget;

    private Budget insertedBudget;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Budget createEntity(EntityManager em) {
        Budget budget = new Budget()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .estimatedDurationDays(DEFAULT_ESTIMATED_DURATION_DAYS)
            .durationMonths(DEFAULT_DURATION_MONTHS)
            .monthlyHours(DEFAULT_MONTHLY_HOURS)
            .plannedHours(DEFAULT_PLANNED_HOURS)
            .resourceCount(DEFAULT_RESOURCE_COUNT)
            .income(DEFAULT_INCOME)
            .otherTaxes(DEFAULT_OTHER_TAXES)
            .descriptionOtherTaxes(DEFAULT_DESCRIPTION_OTHER_TAXES)
            .withholdingTaxes(DEFAULT_WITHHOLDING_TAXES)
            .modAndCifCosts(DEFAULT_MOD_AND_CIF_COSTS)
            .grossProfit(DEFAULT_GROSS_PROFIT)
            .grossProfitPercentage(DEFAULT_GROSS_PROFIT_PERCENTAGE)
            .grossProfitRule(DEFAULT_GROSS_PROFIT_RULE)
            .absorbedFixedCosts(DEFAULT_ABSORBED_FIXED_COSTS)
            .otherExpenses(DEFAULT_OTHER_EXPENSES)
            .profitBeforeTax(DEFAULT_PROFIT_BEFORE_TAX)
            .estimatedTaxes(DEFAULT_ESTIMATED_TAXES)
            .estimatedNetProfit(DEFAULT_ESTIMATED_NET_PROFIT)
            .netMarginPercentage(DEFAULT_NET_MARGIN_PERCENTAGE)
            .netMarginRule(DEFAULT_NET_MARGIN_RULE)
            .commissionToReceive(DEFAULT_COMMISSION_TO_RECEIVE)
            .needsApproval(DEFAULT_NEEDS_APPROVAL)
            .approvalDecision(DEFAULT_APPROVAL_DECISION)
            .approvalDate(DEFAULT_APPROVAL_DATE)
            .approvalTime(DEFAULT_APPROVAL_TIME)
            .approvalComments(DEFAULT_APPROVAL_COMMENTS)
            .approvalStatus(DEFAULT_APPROVAL_STATUS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return budget;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Budget createUpdatedEntity(EntityManager em) {
        Budget budget = new Budget()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .estimatedDurationDays(UPDATED_ESTIMATED_DURATION_DAYS)
            .durationMonths(UPDATED_DURATION_MONTHS)
            .monthlyHours(UPDATED_MONTHLY_HOURS)
            .plannedHours(UPDATED_PLANNED_HOURS)
            .resourceCount(UPDATED_RESOURCE_COUNT)
            .income(UPDATED_INCOME)
            .otherTaxes(UPDATED_OTHER_TAXES)
            .descriptionOtherTaxes(UPDATED_DESCRIPTION_OTHER_TAXES)
            .withholdingTaxes(UPDATED_WITHHOLDING_TAXES)
            .modAndCifCosts(UPDATED_MOD_AND_CIF_COSTS)
            .grossProfit(UPDATED_GROSS_PROFIT)
            .grossProfitPercentage(UPDATED_GROSS_PROFIT_PERCENTAGE)
            .grossProfitRule(UPDATED_GROSS_PROFIT_RULE)
            .absorbedFixedCosts(UPDATED_ABSORBED_FIXED_COSTS)
            .otherExpenses(UPDATED_OTHER_EXPENSES)
            .profitBeforeTax(UPDATED_PROFIT_BEFORE_TAX)
            .estimatedTaxes(UPDATED_ESTIMATED_TAXES)
            .estimatedNetProfit(UPDATED_ESTIMATED_NET_PROFIT)
            .netMarginPercentage(UPDATED_NET_MARGIN_PERCENTAGE)
            .netMarginRule(UPDATED_NET_MARGIN_RULE)
            .commissionToReceive(UPDATED_COMMISSION_TO_RECEIVE)
            .needsApproval(UPDATED_NEEDS_APPROVAL)
            .approvalDecision(UPDATED_APPROVAL_DECISION)
            .approvalDate(UPDATED_APPROVAL_DATE)
            .approvalTime(UPDATED_APPROVAL_TIME)
            .approvalComments(UPDATED_APPROVAL_COMMENTS)
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return budget;
    }

    @BeforeEach
    public void initTest() {
        budget = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedBudget != null) {
            budgetRepository.delete(insertedBudget);
            insertedBudget = null;
        }
    }

    @Test
    @Transactional
    void createBudget() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);
        var returnedBudgetDTO = om.readValue(
            restBudgetMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BudgetDTO.class
        );

        // Validate the Budget in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBudget = budgetMapper.toEntity(returnedBudgetDTO);
        assertBudgetUpdatableFieldsEquals(returnedBudget, getPersistedBudget(returnedBudget));

        insertedBudget = returnedBudget;
    }

    @Test
    @Transactional
    void createBudgetWithExistingId() throws Exception {
        // Create the Budget with an existing ID
        budget.setId(1L);
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBudgetMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Budget in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budget.setName(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBudgets() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList
        restBudgetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budget.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimatedDurationDays").value(hasItem(DEFAULT_ESTIMATED_DURATION_DAYS)))
            .andExpect(jsonPath("$.[*].durationMonths").value(hasItem(DEFAULT_DURATION_MONTHS)))
            .andExpect(jsonPath("$.[*].monthlyHours").value(hasItem(sameNumber(DEFAULT_MONTHLY_HOURS))))
            .andExpect(jsonPath("$.[*].plannedHours").value(hasItem(sameNumber(DEFAULT_PLANNED_HOURS))))
            .andExpect(jsonPath("$.[*].resourceCount").value(hasItem(DEFAULT_RESOURCE_COUNT)))
            .andExpect(jsonPath("$.[*].income").value(hasItem(sameNumber(DEFAULT_INCOME))))
            .andExpect(jsonPath("$.[*].otherTaxes").value(hasItem(sameNumber(DEFAULT_OTHER_TAXES))))
            .andExpect(jsonPath("$.[*].descriptionOtherTaxes").value(hasItem(DEFAULT_DESCRIPTION_OTHER_TAXES)))
            .andExpect(jsonPath("$.[*].withholdingTaxes").value(hasItem(sameNumber(DEFAULT_WITHHOLDING_TAXES))))
            .andExpect(jsonPath("$.[*].modAndCifCosts").value(hasItem(sameNumber(DEFAULT_MOD_AND_CIF_COSTS))))
            .andExpect(jsonPath("$.[*].grossProfit").value(hasItem(sameNumber(DEFAULT_GROSS_PROFIT))))
            .andExpect(jsonPath("$.[*].grossProfitPercentage").value(hasItem(sameNumber(DEFAULT_GROSS_PROFIT_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].grossProfitRule").value(hasItem(sameNumber(DEFAULT_GROSS_PROFIT_RULE))))
            .andExpect(jsonPath("$.[*].absorbedFixedCosts").value(hasItem(sameNumber(DEFAULT_ABSORBED_FIXED_COSTS))))
            .andExpect(jsonPath("$.[*].otherExpenses").value(hasItem(sameNumber(DEFAULT_OTHER_EXPENSES))))
            .andExpect(jsonPath("$.[*].profitBeforeTax").value(hasItem(sameNumber(DEFAULT_PROFIT_BEFORE_TAX))))
            .andExpect(jsonPath("$.[*].estimatedTaxes").value(hasItem(sameNumber(DEFAULT_ESTIMATED_TAXES))))
            .andExpect(jsonPath("$.[*].estimatedNetProfit").value(hasItem(sameNumber(DEFAULT_ESTIMATED_NET_PROFIT))))
            .andExpect(jsonPath("$.[*].netMarginPercentage").value(hasItem(sameNumber(DEFAULT_NET_MARGIN_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].netMarginRule").value(hasItem(sameNumber(DEFAULT_NET_MARGIN_RULE))))
            .andExpect(jsonPath("$.[*].commissionToReceive").value(hasItem(sameNumber(DEFAULT_COMMISSION_TO_RECEIVE))))
            .andExpect(jsonPath("$.[*].needsApproval").value(hasItem(DEFAULT_NEEDS_APPROVAL.booleanValue())))
            .andExpect(jsonPath("$.[*].approvalDecision").value(hasItem(DEFAULT_APPROVAL_DECISION)))
            .andExpect(jsonPath("$.[*].approvalDate").value(hasItem(DEFAULT_APPROVAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].approvalTime").value(hasItem(DEFAULT_APPROVAL_TIME.toString())))
            .andExpect(jsonPath("$.[*].approvalComments").value(hasItem(DEFAULT_APPROVAL_COMMENTS)))
            .andExpect(jsonPath("$.[*].approvalStatus").value(hasItem(DEFAULT_APPROVAL_STATUS)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getBudget() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get the budget
        restBudgetMockMvc
            .perform(get(ENTITY_API_URL_ID, budget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(budget.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.estimatedDurationDays").value(DEFAULT_ESTIMATED_DURATION_DAYS))
            .andExpect(jsonPath("$.durationMonths").value(DEFAULT_DURATION_MONTHS))
            .andExpect(jsonPath("$.monthlyHours").value(sameNumber(DEFAULT_MONTHLY_HOURS)))
            .andExpect(jsonPath("$.plannedHours").value(sameNumber(DEFAULT_PLANNED_HOURS)))
            .andExpect(jsonPath("$.resourceCount").value(DEFAULT_RESOURCE_COUNT))
            .andExpect(jsonPath("$.income").value(sameNumber(DEFAULT_INCOME)))
            .andExpect(jsonPath("$.otherTaxes").value(sameNumber(DEFAULT_OTHER_TAXES)))
            .andExpect(jsonPath("$.descriptionOtherTaxes").value(DEFAULT_DESCRIPTION_OTHER_TAXES))
            .andExpect(jsonPath("$.withholdingTaxes").value(sameNumber(DEFAULT_WITHHOLDING_TAXES)))
            .andExpect(jsonPath("$.modAndCifCosts").value(sameNumber(DEFAULT_MOD_AND_CIF_COSTS)))
            .andExpect(jsonPath("$.grossProfit").value(sameNumber(DEFAULT_GROSS_PROFIT)))
            .andExpect(jsonPath("$.grossProfitPercentage").value(sameNumber(DEFAULT_GROSS_PROFIT_PERCENTAGE)))
            .andExpect(jsonPath("$.grossProfitRule").value(sameNumber(DEFAULT_GROSS_PROFIT_RULE)))
            .andExpect(jsonPath("$.absorbedFixedCosts").value(sameNumber(DEFAULT_ABSORBED_FIXED_COSTS)))
            .andExpect(jsonPath("$.otherExpenses").value(sameNumber(DEFAULT_OTHER_EXPENSES)))
            .andExpect(jsonPath("$.profitBeforeTax").value(sameNumber(DEFAULT_PROFIT_BEFORE_TAX)))
            .andExpect(jsonPath("$.estimatedTaxes").value(sameNumber(DEFAULT_ESTIMATED_TAXES)))
            .andExpect(jsonPath("$.estimatedNetProfit").value(sameNumber(DEFAULT_ESTIMATED_NET_PROFIT)))
            .andExpect(jsonPath("$.netMarginPercentage").value(sameNumber(DEFAULT_NET_MARGIN_PERCENTAGE)))
            .andExpect(jsonPath("$.netMarginRule").value(sameNumber(DEFAULT_NET_MARGIN_RULE)))
            .andExpect(jsonPath("$.commissionToReceive").value(sameNumber(DEFAULT_COMMISSION_TO_RECEIVE)))
            .andExpect(jsonPath("$.needsApproval").value(DEFAULT_NEEDS_APPROVAL.booleanValue()))
            .andExpect(jsonPath("$.approvalDecision").value(DEFAULT_APPROVAL_DECISION))
            .andExpect(jsonPath("$.approvalDate").value(DEFAULT_APPROVAL_DATE.toString()))
            .andExpect(jsonPath("$.approvalTime").value(DEFAULT_APPROVAL_TIME.toString()))
            .andExpect(jsonPath("$.approvalComments").value(DEFAULT_APPROVAL_COMMENTS))
            .andExpect(jsonPath("$.approvalStatus").value(DEFAULT_APPROVAL_STATUS))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getBudgetsByIdFiltering() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        Long id = budget.getId();

        defaultBudgetFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBudgetFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBudgetFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBudgetsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where name equals to
        defaultBudgetFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBudgetsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where name in
        defaultBudgetFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBudgetsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where name is not null
        defaultBudgetFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where name contains
        defaultBudgetFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBudgetsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where name does not contain
        defaultBudgetFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllBudgetsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where startDate equals to
        defaultBudgetFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where startDate in
        defaultBudgetFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where startDate is not null
        defaultBudgetFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where startDate is greater than or equal to
        defaultBudgetFiltering("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE, "startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where startDate is less than or equal to
        defaultBudgetFiltering("startDate.lessThanOrEqual=" + DEFAULT_START_DATE, "startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where startDate is less than
        defaultBudgetFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where startDate is greater than
        defaultBudgetFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where endDate equals to
        defaultBudgetFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where endDate in
        defaultBudgetFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where endDate is not null
        defaultBudgetFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where endDate is greater than or equal to
        defaultBudgetFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where endDate is less than or equal to
        defaultBudgetFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where endDate is less than
        defaultBudgetFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where endDate is greater than
        defaultBudgetFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedDurationDaysIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedDurationDays equals to
        defaultBudgetFiltering(
            "estimatedDurationDays.equals=" + DEFAULT_ESTIMATED_DURATION_DAYS,
            "estimatedDurationDays.equals=" + UPDATED_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedDurationDaysIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedDurationDays in
        defaultBudgetFiltering(
            "estimatedDurationDays.in=" + DEFAULT_ESTIMATED_DURATION_DAYS + "," + UPDATED_ESTIMATED_DURATION_DAYS,
            "estimatedDurationDays.in=" + UPDATED_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedDurationDaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedDurationDays is not null
        defaultBudgetFiltering("estimatedDurationDays.specified=true", "estimatedDurationDays.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedDurationDaysIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedDurationDays is greater than or equal to
        defaultBudgetFiltering(
            "estimatedDurationDays.greaterThanOrEqual=" + DEFAULT_ESTIMATED_DURATION_DAYS,
            "estimatedDurationDays.greaterThanOrEqual=" + UPDATED_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedDurationDaysIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedDurationDays is less than or equal to
        defaultBudgetFiltering(
            "estimatedDurationDays.lessThanOrEqual=" + DEFAULT_ESTIMATED_DURATION_DAYS,
            "estimatedDurationDays.lessThanOrEqual=" + SMALLER_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedDurationDaysIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedDurationDays is less than
        defaultBudgetFiltering(
            "estimatedDurationDays.lessThan=" + UPDATED_ESTIMATED_DURATION_DAYS,
            "estimatedDurationDays.lessThan=" + DEFAULT_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedDurationDaysIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedDurationDays is greater than
        defaultBudgetFiltering(
            "estimatedDurationDays.greaterThan=" + SMALLER_ESTIMATED_DURATION_DAYS,
            "estimatedDurationDays.greaterThan=" + DEFAULT_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByDurationMonthsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where durationMonths equals to
        defaultBudgetFiltering("durationMonths.equals=" + DEFAULT_DURATION_MONTHS, "durationMonths.equals=" + UPDATED_DURATION_MONTHS);
    }

    @Test
    @Transactional
    void getAllBudgetsByDurationMonthsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where durationMonths in
        defaultBudgetFiltering(
            "durationMonths.in=" + DEFAULT_DURATION_MONTHS + "," + UPDATED_DURATION_MONTHS,
            "durationMonths.in=" + UPDATED_DURATION_MONTHS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByDurationMonthsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where durationMonths is not null
        defaultBudgetFiltering("durationMonths.specified=true", "durationMonths.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByDurationMonthsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where durationMonths is greater than or equal to
        defaultBudgetFiltering(
            "durationMonths.greaterThanOrEqual=" + DEFAULT_DURATION_MONTHS,
            "durationMonths.greaterThanOrEqual=" + UPDATED_DURATION_MONTHS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByDurationMonthsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where durationMonths is less than or equal to
        defaultBudgetFiltering(
            "durationMonths.lessThanOrEqual=" + DEFAULT_DURATION_MONTHS,
            "durationMonths.lessThanOrEqual=" + SMALLER_DURATION_MONTHS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByDurationMonthsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where durationMonths is less than
        defaultBudgetFiltering("durationMonths.lessThan=" + UPDATED_DURATION_MONTHS, "durationMonths.lessThan=" + DEFAULT_DURATION_MONTHS);
    }

    @Test
    @Transactional
    void getAllBudgetsByDurationMonthsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where durationMonths is greater than
        defaultBudgetFiltering(
            "durationMonths.greaterThan=" + SMALLER_DURATION_MONTHS,
            "durationMonths.greaterThan=" + DEFAULT_DURATION_MONTHS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByMonthlyHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where monthlyHours equals to
        defaultBudgetFiltering("monthlyHours.equals=" + DEFAULT_MONTHLY_HOURS, "monthlyHours.equals=" + UPDATED_MONTHLY_HOURS);
    }

    @Test
    @Transactional
    void getAllBudgetsByMonthlyHoursIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where monthlyHours in
        defaultBudgetFiltering(
            "monthlyHours.in=" + DEFAULT_MONTHLY_HOURS + "," + UPDATED_MONTHLY_HOURS,
            "monthlyHours.in=" + UPDATED_MONTHLY_HOURS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByMonthlyHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where monthlyHours is not null
        defaultBudgetFiltering("monthlyHours.specified=true", "monthlyHours.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByMonthlyHoursIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where monthlyHours is greater than or equal to
        defaultBudgetFiltering(
            "monthlyHours.greaterThanOrEqual=" + DEFAULT_MONTHLY_HOURS,
            "monthlyHours.greaterThanOrEqual=" + UPDATED_MONTHLY_HOURS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByMonthlyHoursIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where monthlyHours is less than or equal to
        defaultBudgetFiltering(
            "monthlyHours.lessThanOrEqual=" + DEFAULT_MONTHLY_HOURS,
            "monthlyHours.lessThanOrEqual=" + SMALLER_MONTHLY_HOURS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByMonthlyHoursIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where monthlyHours is less than
        defaultBudgetFiltering("monthlyHours.lessThan=" + UPDATED_MONTHLY_HOURS, "monthlyHours.lessThan=" + DEFAULT_MONTHLY_HOURS);
    }

    @Test
    @Transactional
    void getAllBudgetsByMonthlyHoursIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where monthlyHours is greater than
        defaultBudgetFiltering("monthlyHours.greaterThan=" + SMALLER_MONTHLY_HOURS, "monthlyHours.greaterThan=" + DEFAULT_MONTHLY_HOURS);
    }

    @Test
    @Transactional
    void getAllBudgetsByPlannedHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where plannedHours equals to
        defaultBudgetFiltering("plannedHours.equals=" + DEFAULT_PLANNED_HOURS, "plannedHours.equals=" + UPDATED_PLANNED_HOURS);
    }

    @Test
    @Transactional
    void getAllBudgetsByPlannedHoursIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where plannedHours in
        defaultBudgetFiltering(
            "plannedHours.in=" + DEFAULT_PLANNED_HOURS + "," + UPDATED_PLANNED_HOURS,
            "plannedHours.in=" + UPDATED_PLANNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByPlannedHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where plannedHours is not null
        defaultBudgetFiltering("plannedHours.specified=true", "plannedHours.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByPlannedHoursIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where plannedHours is greater than or equal to
        defaultBudgetFiltering(
            "plannedHours.greaterThanOrEqual=" + DEFAULT_PLANNED_HOURS,
            "plannedHours.greaterThanOrEqual=" + UPDATED_PLANNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByPlannedHoursIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where plannedHours is less than or equal to
        defaultBudgetFiltering(
            "plannedHours.lessThanOrEqual=" + DEFAULT_PLANNED_HOURS,
            "plannedHours.lessThanOrEqual=" + SMALLER_PLANNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByPlannedHoursIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where plannedHours is less than
        defaultBudgetFiltering("plannedHours.lessThan=" + UPDATED_PLANNED_HOURS, "plannedHours.lessThan=" + DEFAULT_PLANNED_HOURS);
    }

    @Test
    @Transactional
    void getAllBudgetsByPlannedHoursIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where plannedHours is greater than
        defaultBudgetFiltering("plannedHours.greaterThan=" + SMALLER_PLANNED_HOURS, "plannedHours.greaterThan=" + DEFAULT_PLANNED_HOURS);
    }

    @Test
    @Transactional
    void getAllBudgetsByResourceCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where resourceCount equals to
        defaultBudgetFiltering("resourceCount.equals=" + DEFAULT_RESOURCE_COUNT, "resourceCount.equals=" + UPDATED_RESOURCE_COUNT);
    }

    @Test
    @Transactional
    void getAllBudgetsByResourceCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where resourceCount in
        defaultBudgetFiltering(
            "resourceCount.in=" + DEFAULT_RESOURCE_COUNT + "," + UPDATED_RESOURCE_COUNT,
            "resourceCount.in=" + UPDATED_RESOURCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByResourceCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where resourceCount is not null
        defaultBudgetFiltering("resourceCount.specified=true", "resourceCount.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByResourceCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where resourceCount is greater than or equal to
        defaultBudgetFiltering(
            "resourceCount.greaterThanOrEqual=" + DEFAULT_RESOURCE_COUNT,
            "resourceCount.greaterThanOrEqual=" + UPDATED_RESOURCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByResourceCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where resourceCount is less than or equal to
        defaultBudgetFiltering(
            "resourceCount.lessThanOrEqual=" + DEFAULT_RESOURCE_COUNT,
            "resourceCount.lessThanOrEqual=" + SMALLER_RESOURCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByResourceCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where resourceCount is less than
        defaultBudgetFiltering("resourceCount.lessThan=" + UPDATED_RESOURCE_COUNT, "resourceCount.lessThan=" + DEFAULT_RESOURCE_COUNT);
    }

    @Test
    @Transactional
    void getAllBudgetsByResourceCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where resourceCount is greater than
        defaultBudgetFiltering(
            "resourceCount.greaterThan=" + SMALLER_RESOURCE_COUNT,
            "resourceCount.greaterThan=" + DEFAULT_RESOURCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByIncomeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where income equals to
        defaultBudgetFiltering("income.equals=" + DEFAULT_INCOME, "income.equals=" + UPDATED_INCOME);
    }

    @Test
    @Transactional
    void getAllBudgetsByIncomeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where income in
        defaultBudgetFiltering("income.in=" + DEFAULT_INCOME + "," + UPDATED_INCOME, "income.in=" + UPDATED_INCOME);
    }

    @Test
    @Transactional
    void getAllBudgetsByIncomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where income is not null
        defaultBudgetFiltering("income.specified=true", "income.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByIncomeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where income is greater than or equal to
        defaultBudgetFiltering("income.greaterThanOrEqual=" + DEFAULT_INCOME, "income.greaterThanOrEqual=" + UPDATED_INCOME);
    }

    @Test
    @Transactional
    void getAllBudgetsByIncomeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where income is less than or equal to
        defaultBudgetFiltering("income.lessThanOrEqual=" + DEFAULT_INCOME, "income.lessThanOrEqual=" + SMALLER_INCOME);
    }

    @Test
    @Transactional
    void getAllBudgetsByIncomeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where income is less than
        defaultBudgetFiltering("income.lessThan=" + UPDATED_INCOME, "income.lessThan=" + DEFAULT_INCOME);
    }

    @Test
    @Transactional
    void getAllBudgetsByIncomeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where income is greater than
        defaultBudgetFiltering("income.greaterThan=" + SMALLER_INCOME, "income.greaterThan=" + DEFAULT_INCOME);
    }

    @Test
    @Transactional
    void getAllBudgetsByOtherTaxesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where otherTaxes equals to
        defaultBudgetFiltering("otherTaxes.equals=" + DEFAULT_OTHER_TAXES, "otherTaxes.equals=" + UPDATED_OTHER_TAXES);
    }

    @Test
    @Transactional
    void getAllBudgetsByOtherTaxesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where otherTaxes in
        defaultBudgetFiltering("otherTaxes.in=" + DEFAULT_OTHER_TAXES + "," + UPDATED_OTHER_TAXES, "otherTaxes.in=" + UPDATED_OTHER_TAXES);
    }

    @Test
    @Transactional
    void getAllBudgetsByOtherTaxesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where otherTaxes is not null
        defaultBudgetFiltering("otherTaxes.specified=true", "otherTaxes.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByOtherTaxesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where otherTaxes is greater than or equal to
        defaultBudgetFiltering(
            "otherTaxes.greaterThanOrEqual=" + DEFAULT_OTHER_TAXES,
            "otherTaxes.greaterThanOrEqual=" + UPDATED_OTHER_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByOtherTaxesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where otherTaxes is less than or equal to
        defaultBudgetFiltering("otherTaxes.lessThanOrEqual=" + DEFAULT_OTHER_TAXES, "otherTaxes.lessThanOrEqual=" + SMALLER_OTHER_TAXES);
    }

    @Test
    @Transactional
    void getAllBudgetsByOtherTaxesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where otherTaxes is less than
        defaultBudgetFiltering("otherTaxes.lessThan=" + UPDATED_OTHER_TAXES, "otherTaxes.lessThan=" + DEFAULT_OTHER_TAXES);
    }

    @Test
    @Transactional
    void getAllBudgetsByOtherTaxesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where otherTaxes is greater than
        defaultBudgetFiltering("otherTaxes.greaterThan=" + SMALLER_OTHER_TAXES, "otherTaxes.greaterThan=" + DEFAULT_OTHER_TAXES);
    }

    @Test
    @Transactional
    void getAllBudgetsByDescriptionOtherTaxesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where descriptionOtherTaxes equals to
        defaultBudgetFiltering(
            "descriptionOtherTaxes.equals=" + DEFAULT_DESCRIPTION_OTHER_TAXES,
            "descriptionOtherTaxes.equals=" + UPDATED_DESCRIPTION_OTHER_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByDescriptionOtherTaxesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where descriptionOtherTaxes in
        defaultBudgetFiltering(
            "descriptionOtherTaxes.in=" + DEFAULT_DESCRIPTION_OTHER_TAXES + "," + UPDATED_DESCRIPTION_OTHER_TAXES,
            "descriptionOtherTaxes.in=" + UPDATED_DESCRIPTION_OTHER_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByDescriptionOtherTaxesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where descriptionOtherTaxes is not null
        defaultBudgetFiltering("descriptionOtherTaxes.specified=true", "descriptionOtherTaxes.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByDescriptionOtherTaxesContainsSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where descriptionOtherTaxes contains
        defaultBudgetFiltering(
            "descriptionOtherTaxes.contains=" + DEFAULT_DESCRIPTION_OTHER_TAXES,
            "descriptionOtherTaxes.contains=" + UPDATED_DESCRIPTION_OTHER_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByDescriptionOtherTaxesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where descriptionOtherTaxes does not contain
        defaultBudgetFiltering(
            "descriptionOtherTaxes.doesNotContain=" + UPDATED_DESCRIPTION_OTHER_TAXES,
            "descriptionOtherTaxes.doesNotContain=" + DEFAULT_DESCRIPTION_OTHER_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByWithholdingTaxesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where withholdingTaxes equals to
        defaultBudgetFiltering(
            "withholdingTaxes.equals=" + DEFAULT_WITHHOLDING_TAXES,
            "withholdingTaxes.equals=" + UPDATED_WITHHOLDING_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByWithholdingTaxesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where withholdingTaxes in
        defaultBudgetFiltering(
            "withholdingTaxes.in=" + DEFAULT_WITHHOLDING_TAXES + "," + UPDATED_WITHHOLDING_TAXES,
            "withholdingTaxes.in=" + UPDATED_WITHHOLDING_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByWithholdingTaxesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where withholdingTaxes is not null
        defaultBudgetFiltering("withholdingTaxes.specified=true", "withholdingTaxes.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByWithholdingTaxesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where withholdingTaxes is greater than or equal to
        defaultBudgetFiltering(
            "withholdingTaxes.greaterThanOrEqual=" + DEFAULT_WITHHOLDING_TAXES,
            "withholdingTaxes.greaterThanOrEqual=" + UPDATED_WITHHOLDING_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByWithholdingTaxesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where withholdingTaxes is less than or equal to
        defaultBudgetFiltering(
            "withholdingTaxes.lessThanOrEqual=" + DEFAULT_WITHHOLDING_TAXES,
            "withholdingTaxes.lessThanOrEqual=" + SMALLER_WITHHOLDING_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByWithholdingTaxesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where withholdingTaxes is less than
        defaultBudgetFiltering(
            "withholdingTaxes.lessThan=" + UPDATED_WITHHOLDING_TAXES,
            "withholdingTaxes.lessThan=" + DEFAULT_WITHHOLDING_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByWithholdingTaxesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where withholdingTaxes is greater than
        defaultBudgetFiltering(
            "withholdingTaxes.greaterThan=" + SMALLER_WITHHOLDING_TAXES,
            "withholdingTaxes.greaterThan=" + DEFAULT_WITHHOLDING_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByModAndCifCostsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where modAndCifCosts equals to
        defaultBudgetFiltering("modAndCifCosts.equals=" + DEFAULT_MOD_AND_CIF_COSTS, "modAndCifCosts.equals=" + UPDATED_MOD_AND_CIF_COSTS);
    }

    @Test
    @Transactional
    void getAllBudgetsByModAndCifCostsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where modAndCifCosts in
        defaultBudgetFiltering(
            "modAndCifCosts.in=" + DEFAULT_MOD_AND_CIF_COSTS + "," + UPDATED_MOD_AND_CIF_COSTS,
            "modAndCifCosts.in=" + UPDATED_MOD_AND_CIF_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByModAndCifCostsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where modAndCifCosts is not null
        defaultBudgetFiltering("modAndCifCosts.specified=true", "modAndCifCosts.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByModAndCifCostsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where modAndCifCosts is greater than or equal to
        defaultBudgetFiltering(
            "modAndCifCosts.greaterThanOrEqual=" + DEFAULT_MOD_AND_CIF_COSTS,
            "modAndCifCosts.greaterThanOrEqual=" + UPDATED_MOD_AND_CIF_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByModAndCifCostsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where modAndCifCosts is less than or equal to
        defaultBudgetFiltering(
            "modAndCifCosts.lessThanOrEqual=" + DEFAULT_MOD_AND_CIF_COSTS,
            "modAndCifCosts.lessThanOrEqual=" + SMALLER_MOD_AND_CIF_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByModAndCifCostsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where modAndCifCosts is less than
        defaultBudgetFiltering(
            "modAndCifCosts.lessThan=" + UPDATED_MOD_AND_CIF_COSTS,
            "modAndCifCosts.lessThan=" + DEFAULT_MOD_AND_CIF_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByModAndCifCostsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where modAndCifCosts is greater than
        defaultBudgetFiltering(
            "modAndCifCosts.greaterThan=" + SMALLER_MOD_AND_CIF_COSTS,
            "modAndCifCosts.greaterThan=" + DEFAULT_MOD_AND_CIF_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfit equals to
        defaultBudgetFiltering("grossProfit.equals=" + DEFAULT_GROSS_PROFIT, "grossProfit.equals=" + UPDATED_GROSS_PROFIT);
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfit in
        defaultBudgetFiltering(
            "grossProfit.in=" + DEFAULT_GROSS_PROFIT + "," + UPDATED_GROSS_PROFIT,
            "grossProfit.in=" + UPDATED_GROSS_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfit is not null
        defaultBudgetFiltering("grossProfit.specified=true", "grossProfit.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfit is greater than or equal to
        defaultBudgetFiltering(
            "grossProfit.greaterThanOrEqual=" + DEFAULT_GROSS_PROFIT,
            "grossProfit.greaterThanOrEqual=" + UPDATED_GROSS_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfit is less than or equal to
        defaultBudgetFiltering(
            "grossProfit.lessThanOrEqual=" + DEFAULT_GROSS_PROFIT,
            "grossProfit.lessThanOrEqual=" + SMALLER_GROSS_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfit is less than
        defaultBudgetFiltering("grossProfit.lessThan=" + UPDATED_GROSS_PROFIT, "grossProfit.lessThan=" + DEFAULT_GROSS_PROFIT);
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfit is greater than
        defaultBudgetFiltering("grossProfit.greaterThan=" + SMALLER_GROSS_PROFIT, "grossProfit.greaterThan=" + DEFAULT_GROSS_PROFIT);
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfitPercentage equals to
        defaultBudgetFiltering(
            "grossProfitPercentage.equals=" + DEFAULT_GROSS_PROFIT_PERCENTAGE,
            "grossProfitPercentage.equals=" + UPDATED_GROSS_PROFIT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfitPercentage in
        defaultBudgetFiltering(
            "grossProfitPercentage.in=" + DEFAULT_GROSS_PROFIT_PERCENTAGE + "," + UPDATED_GROSS_PROFIT_PERCENTAGE,
            "grossProfitPercentage.in=" + UPDATED_GROSS_PROFIT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfitPercentage is not null
        defaultBudgetFiltering("grossProfitPercentage.specified=true", "grossProfitPercentage.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfitPercentage is greater than or equal to
        defaultBudgetFiltering(
            "grossProfitPercentage.greaterThanOrEqual=" + DEFAULT_GROSS_PROFIT_PERCENTAGE,
            "grossProfitPercentage.greaterThanOrEqual=" + UPDATED_GROSS_PROFIT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfitPercentage is less than or equal to
        defaultBudgetFiltering(
            "grossProfitPercentage.lessThanOrEqual=" + DEFAULT_GROSS_PROFIT_PERCENTAGE,
            "grossProfitPercentage.lessThanOrEqual=" + SMALLER_GROSS_PROFIT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfitPercentage is less than
        defaultBudgetFiltering(
            "grossProfitPercentage.lessThan=" + UPDATED_GROSS_PROFIT_PERCENTAGE,
            "grossProfitPercentage.lessThan=" + DEFAULT_GROSS_PROFIT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfitPercentage is greater than
        defaultBudgetFiltering(
            "grossProfitPercentage.greaterThan=" + SMALLER_GROSS_PROFIT_PERCENTAGE,
            "grossProfitPercentage.greaterThan=" + DEFAULT_GROSS_PROFIT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitRuleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfitRule equals to
        defaultBudgetFiltering(
            "grossProfitRule.equals=" + DEFAULT_GROSS_PROFIT_RULE,
            "grossProfitRule.equals=" + UPDATED_GROSS_PROFIT_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitRuleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfitRule in
        defaultBudgetFiltering(
            "grossProfitRule.in=" + DEFAULT_GROSS_PROFIT_RULE + "," + UPDATED_GROSS_PROFIT_RULE,
            "grossProfitRule.in=" + UPDATED_GROSS_PROFIT_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitRuleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfitRule is not null
        defaultBudgetFiltering("grossProfitRule.specified=true", "grossProfitRule.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitRuleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfitRule is greater than or equal to
        defaultBudgetFiltering(
            "grossProfitRule.greaterThanOrEqual=" + DEFAULT_GROSS_PROFIT_RULE,
            "grossProfitRule.greaterThanOrEqual=" + UPDATED_GROSS_PROFIT_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitRuleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfitRule is less than or equal to
        defaultBudgetFiltering(
            "grossProfitRule.lessThanOrEqual=" + DEFAULT_GROSS_PROFIT_RULE,
            "grossProfitRule.lessThanOrEqual=" + SMALLER_GROSS_PROFIT_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitRuleIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfitRule is less than
        defaultBudgetFiltering(
            "grossProfitRule.lessThan=" + UPDATED_GROSS_PROFIT_RULE,
            "grossProfitRule.lessThan=" + DEFAULT_GROSS_PROFIT_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByGrossProfitRuleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where grossProfitRule is greater than
        defaultBudgetFiltering(
            "grossProfitRule.greaterThan=" + SMALLER_GROSS_PROFIT_RULE,
            "grossProfitRule.greaterThan=" + DEFAULT_GROSS_PROFIT_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByAbsorbedFixedCostsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where absorbedFixedCosts equals to
        defaultBudgetFiltering(
            "absorbedFixedCosts.equals=" + DEFAULT_ABSORBED_FIXED_COSTS,
            "absorbedFixedCosts.equals=" + UPDATED_ABSORBED_FIXED_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByAbsorbedFixedCostsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where absorbedFixedCosts in
        defaultBudgetFiltering(
            "absorbedFixedCosts.in=" + DEFAULT_ABSORBED_FIXED_COSTS + "," + UPDATED_ABSORBED_FIXED_COSTS,
            "absorbedFixedCosts.in=" + UPDATED_ABSORBED_FIXED_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByAbsorbedFixedCostsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where absorbedFixedCosts is not null
        defaultBudgetFiltering("absorbedFixedCosts.specified=true", "absorbedFixedCosts.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByAbsorbedFixedCostsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where absorbedFixedCosts is greater than or equal to
        defaultBudgetFiltering(
            "absorbedFixedCosts.greaterThanOrEqual=" + DEFAULT_ABSORBED_FIXED_COSTS,
            "absorbedFixedCosts.greaterThanOrEqual=" + UPDATED_ABSORBED_FIXED_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByAbsorbedFixedCostsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where absorbedFixedCosts is less than or equal to
        defaultBudgetFiltering(
            "absorbedFixedCosts.lessThanOrEqual=" + DEFAULT_ABSORBED_FIXED_COSTS,
            "absorbedFixedCosts.lessThanOrEqual=" + SMALLER_ABSORBED_FIXED_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByAbsorbedFixedCostsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where absorbedFixedCosts is less than
        defaultBudgetFiltering(
            "absorbedFixedCosts.lessThan=" + UPDATED_ABSORBED_FIXED_COSTS,
            "absorbedFixedCosts.lessThan=" + DEFAULT_ABSORBED_FIXED_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByAbsorbedFixedCostsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where absorbedFixedCosts is greater than
        defaultBudgetFiltering(
            "absorbedFixedCosts.greaterThan=" + SMALLER_ABSORBED_FIXED_COSTS,
            "absorbedFixedCosts.greaterThan=" + DEFAULT_ABSORBED_FIXED_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByOtherExpensesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where otherExpenses equals to
        defaultBudgetFiltering("otherExpenses.equals=" + DEFAULT_OTHER_EXPENSES, "otherExpenses.equals=" + UPDATED_OTHER_EXPENSES);
    }

    @Test
    @Transactional
    void getAllBudgetsByOtherExpensesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where otherExpenses in
        defaultBudgetFiltering(
            "otherExpenses.in=" + DEFAULT_OTHER_EXPENSES + "," + UPDATED_OTHER_EXPENSES,
            "otherExpenses.in=" + UPDATED_OTHER_EXPENSES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByOtherExpensesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where otherExpenses is not null
        defaultBudgetFiltering("otherExpenses.specified=true", "otherExpenses.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByOtherExpensesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where otherExpenses is greater than or equal to
        defaultBudgetFiltering(
            "otherExpenses.greaterThanOrEqual=" + DEFAULT_OTHER_EXPENSES,
            "otherExpenses.greaterThanOrEqual=" + UPDATED_OTHER_EXPENSES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByOtherExpensesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where otherExpenses is less than or equal to
        defaultBudgetFiltering(
            "otherExpenses.lessThanOrEqual=" + DEFAULT_OTHER_EXPENSES,
            "otherExpenses.lessThanOrEqual=" + SMALLER_OTHER_EXPENSES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByOtherExpensesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where otherExpenses is less than
        defaultBudgetFiltering("otherExpenses.lessThan=" + UPDATED_OTHER_EXPENSES, "otherExpenses.lessThan=" + DEFAULT_OTHER_EXPENSES);
    }

    @Test
    @Transactional
    void getAllBudgetsByOtherExpensesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where otherExpenses is greater than
        defaultBudgetFiltering(
            "otherExpenses.greaterThan=" + SMALLER_OTHER_EXPENSES,
            "otherExpenses.greaterThan=" + DEFAULT_OTHER_EXPENSES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByProfitBeforeTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where profitBeforeTax equals to
        defaultBudgetFiltering(
            "profitBeforeTax.equals=" + DEFAULT_PROFIT_BEFORE_TAX,
            "profitBeforeTax.equals=" + UPDATED_PROFIT_BEFORE_TAX
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByProfitBeforeTaxIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where profitBeforeTax in
        defaultBudgetFiltering(
            "profitBeforeTax.in=" + DEFAULT_PROFIT_BEFORE_TAX + "," + UPDATED_PROFIT_BEFORE_TAX,
            "profitBeforeTax.in=" + UPDATED_PROFIT_BEFORE_TAX
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByProfitBeforeTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where profitBeforeTax is not null
        defaultBudgetFiltering("profitBeforeTax.specified=true", "profitBeforeTax.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByProfitBeforeTaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where profitBeforeTax is greater than or equal to
        defaultBudgetFiltering(
            "profitBeforeTax.greaterThanOrEqual=" + DEFAULT_PROFIT_BEFORE_TAX,
            "profitBeforeTax.greaterThanOrEqual=" + UPDATED_PROFIT_BEFORE_TAX
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByProfitBeforeTaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where profitBeforeTax is less than or equal to
        defaultBudgetFiltering(
            "profitBeforeTax.lessThanOrEqual=" + DEFAULT_PROFIT_BEFORE_TAX,
            "profitBeforeTax.lessThanOrEqual=" + SMALLER_PROFIT_BEFORE_TAX
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByProfitBeforeTaxIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where profitBeforeTax is less than
        defaultBudgetFiltering(
            "profitBeforeTax.lessThan=" + UPDATED_PROFIT_BEFORE_TAX,
            "profitBeforeTax.lessThan=" + DEFAULT_PROFIT_BEFORE_TAX
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByProfitBeforeTaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where profitBeforeTax is greater than
        defaultBudgetFiltering(
            "profitBeforeTax.greaterThan=" + SMALLER_PROFIT_BEFORE_TAX,
            "profitBeforeTax.greaterThan=" + DEFAULT_PROFIT_BEFORE_TAX
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedTaxesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedTaxes equals to
        defaultBudgetFiltering("estimatedTaxes.equals=" + DEFAULT_ESTIMATED_TAXES, "estimatedTaxes.equals=" + UPDATED_ESTIMATED_TAXES);
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedTaxesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedTaxes in
        defaultBudgetFiltering(
            "estimatedTaxes.in=" + DEFAULT_ESTIMATED_TAXES + "," + UPDATED_ESTIMATED_TAXES,
            "estimatedTaxes.in=" + UPDATED_ESTIMATED_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedTaxesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedTaxes is not null
        defaultBudgetFiltering("estimatedTaxes.specified=true", "estimatedTaxes.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedTaxesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedTaxes is greater than or equal to
        defaultBudgetFiltering(
            "estimatedTaxes.greaterThanOrEqual=" + DEFAULT_ESTIMATED_TAXES,
            "estimatedTaxes.greaterThanOrEqual=" + UPDATED_ESTIMATED_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedTaxesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedTaxes is less than or equal to
        defaultBudgetFiltering(
            "estimatedTaxes.lessThanOrEqual=" + DEFAULT_ESTIMATED_TAXES,
            "estimatedTaxes.lessThanOrEqual=" + SMALLER_ESTIMATED_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedTaxesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedTaxes is less than
        defaultBudgetFiltering("estimatedTaxes.lessThan=" + UPDATED_ESTIMATED_TAXES, "estimatedTaxes.lessThan=" + DEFAULT_ESTIMATED_TAXES);
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedTaxesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedTaxes is greater than
        defaultBudgetFiltering(
            "estimatedTaxes.greaterThan=" + SMALLER_ESTIMATED_TAXES,
            "estimatedTaxes.greaterThan=" + DEFAULT_ESTIMATED_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedNetProfitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedNetProfit equals to
        defaultBudgetFiltering(
            "estimatedNetProfit.equals=" + DEFAULT_ESTIMATED_NET_PROFIT,
            "estimatedNetProfit.equals=" + UPDATED_ESTIMATED_NET_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedNetProfitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedNetProfit in
        defaultBudgetFiltering(
            "estimatedNetProfit.in=" + DEFAULT_ESTIMATED_NET_PROFIT + "," + UPDATED_ESTIMATED_NET_PROFIT,
            "estimatedNetProfit.in=" + UPDATED_ESTIMATED_NET_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedNetProfitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedNetProfit is not null
        defaultBudgetFiltering("estimatedNetProfit.specified=true", "estimatedNetProfit.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedNetProfitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedNetProfit is greater than or equal to
        defaultBudgetFiltering(
            "estimatedNetProfit.greaterThanOrEqual=" + DEFAULT_ESTIMATED_NET_PROFIT,
            "estimatedNetProfit.greaterThanOrEqual=" + UPDATED_ESTIMATED_NET_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedNetProfitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedNetProfit is less than or equal to
        defaultBudgetFiltering(
            "estimatedNetProfit.lessThanOrEqual=" + DEFAULT_ESTIMATED_NET_PROFIT,
            "estimatedNetProfit.lessThanOrEqual=" + SMALLER_ESTIMATED_NET_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedNetProfitIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedNetProfit is less than
        defaultBudgetFiltering(
            "estimatedNetProfit.lessThan=" + UPDATED_ESTIMATED_NET_PROFIT,
            "estimatedNetProfit.lessThan=" + DEFAULT_ESTIMATED_NET_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByEstimatedNetProfitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where estimatedNetProfit is greater than
        defaultBudgetFiltering(
            "estimatedNetProfit.greaterThan=" + SMALLER_ESTIMATED_NET_PROFIT,
            "estimatedNetProfit.greaterThan=" + DEFAULT_ESTIMATED_NET_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByNetMarginPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where netMarginPercentage equals to
        defaultBudgetFiltering(
            "netMarginPercentage.equals=" + DEFAULT_NET_MARGIN_PERCENTAGE,
            "netMarginPercentage.equals=" + UPDATED_NET_MARGIN_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByNetMarginPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where netMarginPercentage in
        defaultBudgetFiltering(
            "netMarginPercentage.in=" + DEFAULT_NET_MARGIN_PERCENTAGE + "," + UPDATED_NET_MARGIN_PERCENTAGE,
            "netMarginPercentage.in=" + UPDATED_NET_MARGIN_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByNetMarginPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where netMarginPercentage is not null
        defaultBudgetFiltering("netMarginPercentage.specified=true", "netMarginPercentage.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByNetMarginPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where netMarginPercentage is greater than or equal to
        defaultBudgetFiltering(
            "netMarginPercentage.greaterThanOrEqual=" + DEFAULT_NET_MARGIN_PERCENTAGE,
            "netMarginPercentage.greaterThanOrEqual=" + UPDATED_NET_MARGIN_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByNetMarginPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where netMarginPercentage is less than or equal to
        defaultBudgetFiltering(
            "netMarginPercentage.lessThanOrEqual=" + DEFAULT_NET_MARGIN_PERCENTAGE,
            "netMarginPercentage.lessThanOrEqual=" + SMALLER_NET_MARGIN_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByNetMarginPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where netMarginPercentage is less than
        defaultBudgetFiltering(
            "netMarginPercentage.lessThan=" + UPDATED_NET_MARGIN_PERCENTAGE,
            "netMarginPercentage.lessThan=" + DEFAULT_NET_MARGIN_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByNetMarginPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where netMarginPercentage is greater than
        defaultBudgetFiltering(
            "netMarginPercentage.greaterThan=" + SMALLER_NET_MARGIN_PERCENTAGE,
            "netMarginPercentage.greaterThan=" + DEFAULT_NET_MARGIN_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByNetMarginRuleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where netMarginRule equals to
        defaultBudgetFiltering("netMarginRule.equals=" + DEFAULT_NET_MARGIN_RULE, "netMarginRule.equals=" + UPDATED_NET_MARGIN_RULE);
    }

    @Test
    @Transactional
    void getAllBudgetsByNetMarginRuleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where netMarginRule in
        defaultBudgetFiltering(
            "netMarginRule.in=" + DEFAULT_NET_MARGIN_RULE + "," + UPDATED_NET_MARGIN_RULE,
            "netMarginRule.in=" + UPDATED_NET_MARGIN_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByNetMarginRuleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where netMarginRule is not null
        defaultBudgetFiltering("netMarginRule.specified=true", "netMarginRule.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByNetMarginRuleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where netMarginRule is greater than or equal to
        defaultBudgetFiltering(
            "netMarginRule.greaterThanOrEqual=" + DEFAULT_NET_MARGIN_RULE,
            "netMarginRule.greaterThanOrEqual=" + UPDATED_NET_MARGIN_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByNetMarginRuleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where netMarginRule is less than or equal to
        defaultBudgetFiltering(
            "netMarginRule.lessThanOrEqual=" + DEFAULT_NET_MARGIN_RULE,
            "netMarginRule.lessThanOrEqual=" + SMALLER_NET_MARGIN_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByNetMarginRuleIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where netMarginRule is less than
        defaultBudgetFiltering("netMarginRule.lessThan=" + UPDATED_NET_MARGIN_RULE, "netMarginRule.lessThan=" + DEFAULT_NET_MARGIN_RULE);
    }

    @Test
    @Transactional
    void getAllBudgetsByNetMarginRuleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where netMarginRule is greater than
        defaultBudgetFiltering(
            "netMarginRule.greaterThan=" + SMALLER_NET_MARGIN_RULE,
            "netMarginRule.greaterThan=" + DEFAULT_NET_MARGIN_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByCommissionToReceiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where commissionToReceive equals to
        defaultBudgetFiltering(
            "commissionToReceive.equals=" + DEFAULT_COMMISSION_TO_RECEIVE,
            "commissionToReceive.equals=" + UPDATED_COMMISSION_TO_RECEIVE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByCommissionToReceiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where commissionToReceive in
        defaultBudgetFiltering(
            "commissionToReceive.in=" + DEFAULT_COMMISSION_TO_RECEIVE + "," + UPDATED_COMMISSION_TO_RECEIVE,
            "commissionToReceive.in=" + UPDATED_COMMISSION_TO_RECEIVE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByCommissionToReceiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where commissionToReceive is not null
        defaultBudgetFiltering("commissionToReceive.specified=true", "commissionToReceive.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByCommissionToReceiveIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where commissionToReceive is greater than or equal to
        defaultBudgetFiltering(
            "commissionToReceive.greaterThanOrEqual=" + DEFAULT_COMMISSION_TO_RECEIVE,
            "commissionToReceive.greaterThanOrEqual=" + UPDATED_COMMISSION_TO_RECEIVE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByCommissionToReceiveIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where commissionToReceive is less than or equal to
        defaultBudgetFiltering(
            "commissionToReceive.lessThanOrEqual=" + DEFAULT_COMMISSION_TO_RECEIVE,
            "commissionToReceive.lessThanOrEqual=" + SMALLER_COMMISSION_TO_RECEIVE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByCommissionToReceiveIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where commissionToReceive is less than
        defaultBudgetFiltering(
            "commissionToReceive.lessThan=" + UPDATED_COMMISSION_TO_RECEIVE,
            "commissionToReceive.lessThan=" + DEFAULT_COMMISSION_TO_RECEIVE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByCommissionToReceiveIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where commissionToReceive is greater than
        defaultBudgetFiltering(
            "commissionToReceive.greaterThan=" + SMALLER_COMMISSION_TO_RECEIVE,
            "commissionToReceive.greaterThan=" + DEFAULT_COMMISSION_TO_RECEIVE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByNeedsApprovalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where needsApproval equals to
        defaultBudgetFiltering("needsApproval.equals=" + DEFAULT_NEEDS_APPROVAL, "needsApproval.equals=" + UPDATED_NEEDS_APPROVAL);
    }

    @Test
    @Transactional
    void getAllBudgetsByNeedsApprovalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where needsApproval in
        defaultBudgetFiltering(
            "needsApproval.in=" + DEFAULT_NEEDS_APPROVAL + "," + UPDATED_NEEDS_APPROVAL,
            "needsApproval.in=" + UPDATED_NEEDS_APPROVAL
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByNeedsApprovalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where needsApproval is not null
        defaultBudgetFiltering("needsApproval.specified=true", "needsApproval.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalDecisionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalDecision equals to
        defaultBudgetFiltering(
            "approvalDecision.equals=" + DEFAULT_APPROVAL_DECISION,
            "approvalDecision.equals=" + UPDATED_APPROVAL_DECISION
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalDecisionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalDecision in
        defaultBudgetFiltering(
            "approvalDecision.in=" + DEFAULT_APPROVAL_DECISION + "," + UPDATED_APPROVAL_DECISION,
            "approvalDecision.in=" + UPDATED_APPROVAL_DECISION
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalDecisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalDecision is not null
        defaultBudgetFiltering("approvalDecision.specified=true", "approvalDecision.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalDecisionContainsSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalDecision contains
        defaultBudgetFiltering(
            "approvalDecision.contains=" + DEFAULT_APPROVAL_DECISION,
            "approvalDecision.contains=" + UPDATED_APPROVAL_DECISION
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalDecisionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalDecision does not contain
        defaultBudgetFiltering(
            "approvalDecision.doesNotContain=" + UPDATED_APPROVAL_DECISION,
            "approvalDecision.doesNotContain=" + DEFAULT_APPROVAL_DECISION
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalDate equals to
        defaultBudgetFiltering("approvalDate.equals=" + DEFAULT_APPROVAL_DATE, "approvalDate.equals=" + UPDATED_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalDate in
        defaultBudgetFiltering(
            "approvalDate.in=" + DEFAULT_APPROVAL_DATE + "," + UPDATED_APPROVAL_DATE,
            "approvalDate.in=" + UPDATED_APPROVAL_DATE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalDate is not null
        defaultBudgetFiltering("approvalDate.specified=true", "approvalDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalTime equals to
        defaultBudgetFiltering("approvalTime.equals=" + DEFAULT_APPROVAL_TIME, "approvalTime.equals=" + UPDATED_APPROVAL_TIME);
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalTime in
        defaultBudgetFiltering(
            "approvalTime.in=" + DEFAULT_APPROVAL_TIME + "," + UPDATED_APPROVAL_TIME,
            "approvalTime.in=" + UPDATED_APPROVAL_TIME
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalTime is not null
        defaultBudgetFiltering("approvalTime.specified=true", "approvalTime.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalComments equals to
        defaultBudgetFiltering(
            "approvalComments.equals=" + DEFAULT_APPROVAL_COMMENTS,
            "approvalComments.equals=" + UPDATED_APPROVAL_COMMENTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalComments in
        defaultBudgetFiltering(
            "approvalComments.in=" + DEFAULT_APPROVAL_COMMENTS + "," + UPDATED_APPROVAL_COMMENTS,
            "approvalComments.in=" + UPDATED_APPROVAL_COMMENTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalComments is not null
        defaultBudgetFiltering("approvalComments.specified=true", "approvalComments.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalCommentsContainsSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalComments contains
        defaultBudgetFiltering(
            "approvalComments.contains=" + DEFAULT_APPROVAL_COMMENTS,
            "approvalComments.contains=" + UPDATED_APPROVAL_COMMENTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalComments does not contain
        defaultBudgetFiltering(
            "approvalComments.doesNotContain=" + UPDATED_APPROVAL_COMMENTS,
            "approvalComments.doesNotContain=" + DEFAULT_APPROVAL_COMMENTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalStatus equals to
        defaultBudgetFiltering("approvalStatus.equals=" + DEFAULT_APPROVAL_STATUS, "approvalStatus.equals=" + UPDATED_APPROVAL_STATUS);
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalStatus in
        defaultBudgetFiltering(
            "approvalStatus.in=" + DEFAULT_APPROVAL_STATUS + "," + UPDATED_APPROVAL_STATUS,
            "approvalStatus.in=" + UPDATED_APPROVAL_STATUS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalStatus is not null
        defaultBudgetFiltering("approvalStatus.specified=true", "approvalStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalStatus contains
        defaultBudgetFiltering("approvalStatus.contains=" + DEFAULT_APPROVAL_STATUS, "approvalStatus.contains=" + UPDATED_APPROVAL_STATUS);
    }

    @Test
    @Transactional
    void getAllBudgetsByApprovalStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where approvalStatus does not contain
        defaultBudgetFiltering(
            "approvalStatus.doesNotContain=" + UPDATED_APPROVAL_STATUS,
            "approvalStatus.doesNotContain=" + DEFAULT_APPROVAL_STATUS
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where createdBy equals to
        defaultBudgetFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBudgetsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where createdBy in
        defaultBudgetFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBudgetsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where createdBy is not null
        defaultBudgetFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where createdBy contains
        defaultBudgetFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBudgetsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where createdBy does not contain
        defaultBudgetFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBudgetsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where createdDate equals to
        defaultBudgetFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where createdDate in
        defaultBudgetFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where createdDate is not null
        defaultBudgetFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where lastModifiedBy equals to
        defaultBudgetFiltering("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllBudgetsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where lastModifiedBy in
        defaultBudgetFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where lastModifiedBy is not null
        defaultBudgetFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where lastModifiedBy contains
        defaultBudgetFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where lastModifiedBy does not contain
        defaultBudgetFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where lastModifiedDate equals to
        defaultBudgetFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where lastModifiedDate in
        defaultBudgetFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllBudgetsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList where lastModifiedDate is not null
        defaultBudgetFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetsByContryIsEqualToSomething() throws Exception {
        Country contry;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            budgetRepository.saveAndFlush(budget);
            contry = CountryResourceIT.createEntity(em);
        } else {
            contry = TestUtil.findAll(em, Country.class).get(0);
        }
        em.persist(contry);
        em.flush();
        budget.setContry(contry);
        budgetRepository.saveAndFlush(budget);
        Long contryId = contry.getId();
        // Get all the budgetList where contry equals to contryId
        defaultBudgetShouldBeFound("contryId.equals=" + contryId);

        // Get all the budgetList where contry equals to (contryId + 1)
        defaultBudgetShouldNotBeFound("contryId.equals=" + (contryId + 1));
    }

    @Test
    @Transactional
    void getAllBudgetsByUserAssignedToIsEqualToSomething() throws Exception {
        Users userAssignedTo;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            budgetRepository.saveAndFlush(budget);
            userAssignedTo = UsersResourceIT.createEntity(em);
        } else {
            userAssignedTo = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(userAssignedTo);
        em.flush();
        budget.setUserAssignedTo(userAssignedTo);
        budgetRepository.saveAndFlush(budget);
        Long userAssignedToId = userAssignedTo.getId();
        // Get all the budgetList where userAssignedTo equals to userAssignedToId
        defaultBudgetShouldBeFound("userAssignedToId.equals=" + userAssignedToId);

        // Get all the budgetList where userAssignedTo equals to (userAssignedToId + 1)
        defaultBudgetShouldNotBeFound("userAssignedToId.equals=" + (userAssignedToId + 1));
    }

    @Test
    @Transactional
    void getAllBudgetsByUserApprovedByIsEqualToSomething() throws Exception {
        Users userApprovedBy;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            budgetRepository.saveAndFlush(budget);
            userApprovedBy = UsersResourceIT.createEntity(em);
        } else {
            userApprovedBy = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(userApprovedBy);
        em.flush();
        budget.setUserApprovedBy(userApprovedBy);
        budgetRepository.saveAndFlush(budget);
        Long userApprovedById = userApprovedBy.getId();
        // Get all the budgetList where userApprovedBy equals to userApprovedById
        defaultBudgetShouldBeFound("userApprovedById.equals=" + userApprovedById);

        // Get all the budgetList where userApprovedBy equals to (userApprovedById + 1)
        defaultBudgetShouldNotBeFound("userApprovedById.equals=" + (userApprovedById + 1));
    }

    @Test
    @Transactional
    void getAllBudgetsByUserOwnerIsEqualToSomething() throws Exception {
        Users userOwner;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            budgetRepository.saveAndFlush(budget);
            userOwner = UsersResourceIT.createEntity(em);
        } else {
            userOwner = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(userOwner);
        em.flush();
        budget.setUserOwner(userOwner);
        budgetRepository.saveAndFlush(budget);
        Long userOwnerId = userOwner.getId();
        // Get all the budgetList where userOwner equals to userOwnerId
        defaultBudgetShouldBeFound("userOwnerId.equals=" + userOwnerId);

        // Get all the budgetList where userOwner equals to (userOwnerId + 1)
        defaultBudgetShouldNotBeFound("userOwnerId.equals=" + (userOwnerId + 1));
    }

    @Test
    @Transactional
    void getAllBudgetsByAuthorizedIsEqualToSomething() throws Exception {
        Users authorized;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            budgetRepository.saveAndFlush(budget);
            authorized = UsersResourceIT.createEntity(em);
        } else {
            authorized = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(authorized);
        em.flush();
        budget.addAuthorized(authorized);
        budgetRepository.saveAndFlush(budget);
        Long authorizedId = authorized.getId();
        // Get all the budgetList where authorized equals to authorizedId
        defaultBudgetShouldBeFound("authorizedId.equals=" + authorizedId);

        // Get all the budgetList where authorized equals to (authorizedId + 1)
        defaultBudgetShouldNotBeFound("authorizedId.equals=" + (authorizedId + 1));
    }

    @Test
    @Transactional
    void getAllBudgetsByRoleAuthorizedIsEqualToSomething() throws Exception {
        UserRole roleAuthorized;
        if (TestUtil.findAll(em, UserRole.class).isEmpty()) {
            budgetRepository.saveAndFlush(budget);
            roleAuthorized = UserRoleResourceIT.createEntity(em);
        } else {
            roleAuthorized = TestUtil.findAll(em, UserRole.class).get(0);
        }
        em.persist(roleAuthorized);
        em.flush();
        budget.addRoleAuthorized(roleAuthorized);
        budgetRepository.saveAndFlush(budget);
        Long roleAuthorizedId = roleAuthorized.getId();
        // Get all the budgetList where roleAuthorized equals to roleAuthorizedId
        defaultBudgetShouldBeFound("roleAuthorizedId.equals=" + roleAuthorizedId);

        // Get all the budgetList where roleAuthorized equals to (roleAuthorizedId + 1)
        defaultBudgetShouldNotBeFound("roleAuthorizedId.equals=" + (roleAuthorizedId + 1));
    }

    private void defaultBudgetFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBudgetShouldBeFound(shouldBeFound);
        defaultBudgetShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBudgetShouldBeFound(String filter) throws Exception {
        restBudgetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budget.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimatedDurationDays").value(hasItem(DEFAULT_ESTIMATED_DURATION_DAYS)))
            .andExpect(jsonPath("$.[*].durationMonths").value(hasItem(DEFAULT_DURATION_MONTHS)))
            .andExpect(jsonPath("$.[*].monthlyHours").value(hasItem(sameNumber(DEFAULT_MONTHLY_HOURS))))
            .andExpect(jsonPath("$.[*].plannedHours").value(hasItem(sameNumber(DEFAULT_PLANNED_HOURS))))
            .andExpect(jsonPath("$.[*].resourceCount").value(hasItem(DEFAULT_RESOURCE_COUNT)))
            .andExpect(jsonPath("$.[*].income").value(hasItem(sameNumber(DEFAULT_INCOME))))
            .andExpect(jsonPath("$.[*].otherTaxes").value(hasItem(sameNumber(DEFAULT_OTHER_TAXES))))
            .andExpect(jsonPath("$.[*].descriptionOtherTaxes").value(hasItem(DEFAULT_DESCRIPTION_OTHER_TAXES)))
            .andExpect(jsonPath("$.[*].withholdingTaxes").value(hasItem(sameNumber(DEFAULT_WITHHOLDING_TAXES))))
            .andExpect(jsonPath("$.[*].modAndCifCosts").value(hasItem(sameNumber(DEFAULT_MOD_AND_CIF_COSTS))))
            .andExpect(jsonPath("$.[*].grossProfit").value(hasItem(sameNumber(DEFAULT_GROSS_PROFIT))))
            .andExpect(jsonPath("$.[*].grossProfitPercentage").value(hasItem(sameNumber(DEFAULT_GROSS_PROFIT_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].grossProfitRule").value(hasItem(sameNumber(DEFAULT_GROSS_PROFIT_RULE))))
            .andExpect(jsonPath("$.[*].absorbedFixedCosts").value(hasItem(sameNumber(DEFAULT_ABSORBED_FIXED_COSTS))))
            .andExpect(jsonPath("$.[*].otherExpenses").value(hasItem(sameNumber(DEFAULT_OTHER_EXPENSES))))
            .andExpect(jsonPath("$.[*].profitBeforeTax").value(hasItem(sameNumber(DEFAULT_PROFIT_BEFORE_TAX))))
            .andExpect(jsonPath("$.[*].estimatedTaxes").value(hasItem(sameNumber(DEFAULT_ESTIMATED_TAXES))))
            .andExpect(jsonPath("$.[*].estimatedNetProfit").value(hasItem(sameNumber(DEFAULT_ESTIMATED_NET_PROFIT))))
            .andExpect(jsonPath("$.[*].netMarginPercentage").value(hasItem(sameNumber(DEFAULT_NET_MARGIN_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].netMarginRule").value(hasItem(sameNumber(DEFAULT_NET_MARGIN_RULE))))
            .andExpect(jsonPath("$.[*].commissionToReceive").value(hasItem(sameNumber(DEFAULT_COMMISSION_TO_RECEIVE))))
            .andExpect(jsonPath("$.[*].needsApproval").value(hasItem(DEFAULT_NEEDS_APPROVAL.booleanValue())))
            .andExpect(jsonPath("$.[*].approvalDecision").value(hasItem(DEFAULT_APPROVAL_DECISION)))
            .andExpect(jsonPath("$.[*].approvalDate").value(hasItem(DEFAULT_APPROVAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].approvalTime").value(hasItem(DEFAULT_APPROVAL_TIME.toString())))
            .andExpect(jsonPath("$.[*].approvalComments").value(hasItem(DEFAULT_APPROVAL_COMMENTS)))
            .andExpect(jsonPath("$.[*].approvalStatus").value(hasItem(DEFAULT_APPROVAL_STATUS)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restBudgetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBudgetShouldNotBeFound(String filter) throws Exception {
        restBudgetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBudgetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBudget() throws Exception {
        // Get the budget
        restBudgetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBudget() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budget
        Budget updatedBudget = budgetRepository.findById(budget.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBudget are not directly saved in db
        em.detach(updatedBudget);
        updatedBudget
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .estimatedDurationDays(UPDATED_ESTIMATED_DURATION_DAYS)
            .durationMonths(UPDATED_DURATION_MONTHS)
            .monthlyHours(UPDATED_MONTHLY_HOURS)
            .plannedHours(UPDATED_PLANNED_HOURS)
            .resourceCount(UPDATED_RESOURCE_COUNT)
            .income(UPDATED_INCOME)
            .otherTaxes(UPDATED_OTHER_TAXES)
            .descriptionOtherTaxes(UPDATED_DESCRIPTION_OTHER_TAXES)
            .withholdingTaxes(UPDATED_WITHHOLDING_TAXES)
            .modAndCifCosts(UPDATED_MOD_AND_CIF_COSTS)
            .grossProfit(UPDATED_GROSS_PROFIT)
            .grossProfitPercentage(UPDATED_GROSS_PROFIT_PERCENTAGE)
            .grossProfitRule(UPDATED_GROSS_PROFIT_RULE)
            .absorbedFixedCosts(UPDATED_ABSORBED_FIXED_COSTS)
            .otherExpenses(UPDATED_OTHER_EXPENSES)
            .profitBeforeTax(UPDATED_PROFIT_BEFORE_TAX)
            .estimatedTaxes(UPDATED_ESTIMATED_TAXES)
            .estimatedNetProfit(UPDATED_ESTIMATED_NET_PROFIT)
            .netMarginPercentage(UPDATED_NET_MARGIN_PERCENTAGE)
            .netMarginRule(UPDATED_NET_MARGIN_RULE)
            .commissionToReceive(UPDATED_COMMISSION_TO_RECEIVE)
            .needsApproval(UPDATED_NEEDS_APPROVAL)
            .approvalDecision(UPDATED_APPROVAL_DECISION)
            .approvalDate(UPDATED_APPROVAL_DATE)
            .approvalTime(UPDATED_APPROVAL_TIME)
            .approvalComments(UPDATED_APPROVAL_COMMENTS)
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        BudgetDTO budgetDTO = budgetMapper.toDto(updatedBudget);

        restBudgetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, budgetDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetDTO))
            )
            .andExpect(status().isOk());

        // Validate the Budget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBudgetToMatchAllProperties(updatedBudget);
    }

    @Test
    @Transactional
    void putNonExistingBudget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBudgetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, budgetDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Budget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBudget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Budget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBudget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Budget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBudgetWithPatch() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budget using partial update
        Budget partialUpdatedBudget = new Budget();
        partialUpdatedBudget.setId(budget.getId());

        partialUpdatedBudget
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .estimatedDurationDays(UPDATED_ESTIMATED_DURATION_DAYS)
            .monthlyHours(UPDATED_MONTHLY_HOURS)
            .plannedHours(UPDATED_PLANNED_HOURS)
            .withholdingTaxes(UPDATED_WITHHOLDING_TAXES)
            .profitBeforeTax(UPDATED_PROFIT_BEFORE_TAX)
            .netMarginPercentage(UPDATED_NET_MARGIN_PERCENTAGE)
            .netMarginRule(UPDATED_NET_MARGIN_RULE)
            .commissionToReceive(UPDATED_COMMISSION_TO_RECEIVE)
            .needsApproval(UPDATED_NEEDS_APPROVAL)
            .approvalDate(UPDATED_APPROVAL_DATE)
            .approvalTime(UPDATED_APPROVAL_TIME)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restBudgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBudget.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBudget))
            )
            .andExpect(status().isOk());

        // Validate the Budget in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBudgetUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBudget, budget), getPersistedBudget(budget));
    }

    @Test
    @Transactional
    void fullUpdateBudgetWithPatch() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budget using partial update
        Budget partialUpdatedBudget = new Budget();
        partialUpdatedBudget.setId(budget.getId());

        partialUpdatedBudget
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .estimatedDurationDays(UPDATED_ESTIMATED_DURATION_DAYS)
            .durationMonths(UPDATED_DURATION_MONTHS)
            .monthlyHours(UPDATED_MONTHLY_HOURS)
            .plannedHours(UPDATED_PLANNED_HOURS)
            .resourceCount(UPDATED_RESOURCE_COUNT)
            .income(UPDATED_INCOME)
            .otherTaxes(UPDATED_OTHER_TAXES)
            .descriptionOtherTaxes(UPDATED_DESCRIPTION_OTHER_TAXES)
            .withholdingTaxes(UPDATED_WITHHOLDING_TAXES)
            .modAndCifCosts(UPDATED_MOD_AND_CIF_COSTS)
            .grossProfit(UPDATED_GROSS_PROFIT)
            .grossProfitPercentage(UPDATED_GROSS_PROFIT_PERCENTAGE)
            .grossProfitRule(UPDATED_GROSS_PROFIT_RULE)
            .absorbedFixedCosts(UPDATED_ABSORBED_FIXED_COSTS)
            .otherExpenses(UPDATED_OTHER_EXPENSES)
            .profitBeforeTax(UPDATED_PROFIT_BEFORE_TAX)
            .estimatedTaxes(UPDATED_ESTIMATED_TAXES)
            .estimatedNetProfit(UPDATED_ESTIMATED_NET_PROFIT)
            .netMarginPercentage(UPDATED_NET_MARGIN_PERCENTAGE)
            .netMarginRule(UPDATED_NET_MARGIN_RULE)
            .commissionToReceive(UPDATED_COMMISSION_TO_RECEIVE)
            .needsApproval(UPDATED_NEEDS_APPROVAL)
            .approvalDecision(UPDATED_APPROVAL_DECISION)
            .approvalDate(UPDATED_APPROVAL_DATE)
            .approvalTime(UPDATED_APPROVAL_TIME)
            .approvalComments(UPDATED_APPROVAL_COMMENTS)
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restBudgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBudget.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBudget))
            )
            .andExpect(status().isOk());

        // Validate the Budget in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBudgetUpdatableFieldsEquals(partialUpdatedBudget, getPersistedBudget(partialUpdatedBudget));
    }

    @Test
    @Transactional
    void patchNonExistingBudget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBudgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, budgetDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(budgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Budget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBudget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(budgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Budget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBudget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(budgetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Budget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBudget() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the budget
        restBudgetMockMvc
            .perform(delete(ENTITY_API_URL_ID, budget.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return budgetRepository.count();
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

    protected Budget getPersistedBudget(Budget budget) {
        return budgetRepository.findById(budget.getId()).orElseThrow();
    }

    protected void assertPersistedBudgetToMatchAllProperties(Budget expectedBudget) {
        assertBudgetAllPropertiesEquals(expectedBudget, getPersistedBudget(expectedBudget));
    }

    protected void assertPersistedBudgetToMatchUpdatableProperties(Budget expectedBudget) {
        assertBudgetAllUpdatablePropertiesEquals(expectedBudget, getPersistedBudget(expectedBudget));
    }
}
