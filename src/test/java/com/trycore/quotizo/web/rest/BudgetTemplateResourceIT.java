package com.trycore.quotizo.web.rest;

import static com.trycore.quotizo.domain.BudgetTemplateAsserts.*;
import static com.trycore.quotizo.web.rest.TestUtil.createUpdateProxyForBean;
import static com.trycore.quotizo.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trycore.quotizo.IntegrationTest;
import com.trycore.quotizo.domain.BudgetTemplate;
import com.trycore.quotizo.domain.Country;
import com.trycore.quotizo.repository.BudgetTemplateRepository;
import com.trycore.quotizo.service.dto.BudgetTemplateDTO;
import com.trycore.quotizo.service.mapper.BudgetTemplateMapper;
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
 * Integration tests for the {@link BudgetTemplateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BudgetTemplateResourceIT {

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

    private static final String ENTITY_API_URL = "/api/budget-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BudgetTemplateRepository budgetTemplateRepository;

    @Autowired
    private BudgetTemplateMapper budgetTemplateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBudgetTemplateMockMvc;

    private BudgetTemplate budgetTemplate;

    private BudgetTemplate insertedBudgetTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BudgetTemplate createEntity(EntityManager em) {
        BudgetTemplate budgetTemplate = new BudgetTemplate()
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
            .active(DEFAULT_ACTIVE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return budgetTemplate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BudgetTemplate createUpdatedEntity(EntityManager em) {
        BudgetTemplate budgetTemplate = new BudgetTemplate()
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
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return budgetTemplate;
    }

    @BeforeEach
    public void initTest() {
        budgetTemplate = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedBudgetTemplate != null) {
            budgetTemplateRepository.delete(insertedBudgetTemplate);
            insertedBudgetTemplate = null;
        }
    }

    @Test
    @Transactional
    void createBudgetTemplate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BudgetTemplate
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);
        var returnedBudgetTemplateDTO = om.readValue(
            restBudgetTemplateMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(budgetTemplateDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BudgetTemplateDTO.class
        );

        // Validate the BudgetTemplate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBudgetTemplate = budgetTemplateMapper.toEntity(returnedBudgetTemplateDTO);
        assertBudgetTemplateUpdatableFieldsEquals(returnedBudgetTemplate, getPersistedBudgetTemplate(returnedBudgetTemplate));

        insertedBudgetTemplate = returnedBudgetTemplate;
    }

    @Test
    @Transactional
    void createBudgetTemplateWithExistingId() throws Exception {
        // Create the BudgetTemplate with an existing ID
        budgetTemplate.setId(1L);
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBudgetTemplateMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetTemplate.setName(null);

        // Create the BudgetTemplate, which fails.
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        restBudgetTemplateMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetTemplate.setActive(null);

        // Create the BudgetTemplate, which fails.
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        restBudgetTemplateMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBudgetTemplates() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList
        restBudgetTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budgetTemplate.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getBudgetTemplate() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get the budgetTemplate
        restBudgetTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, budgetTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(budgetTemplate.getId().intValue()))
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
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getBudgetTemplatesByIdFiltering() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        Long id = budgetTemplate.getId();

        defaultBudgetTemplateFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBudgetTemplateFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBudgetTemplateFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where name equals to
        defaultBudgetTemplateFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where name in
        defaultBudgetTemplateFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where name is not null
        defaultBudgetTemplateFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where name contains
        defaultBudgetTemplateFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where name does not contain
        defaultBudgetTemplateFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where startDate equals to
        defaultBudgetTemplateFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where startDate in
        defaultBudgetTemplateFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where startDate is not null
        defaultBudgetTemplateFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where startDate is greater than or equal to
        defaultBudgetTemplateFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where startDate is less than or equal to
        defaultBudgetTemplateFiltering(
            "startDate.lessThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.lessThanOrEqual=" + SMALLER_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where startDate is less than
        defaultBudgetTemplateFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where startDate is greater than
        defaultBudgetTemplateFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where endDate equals to
        defaultBudgetTemplateFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where endDate in
        defaultBudgetTemplateFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where endDate is not null
        defaultBudgetTemplateFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where endDate is greater than or equal to
        defaultBudgetTemplateFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where endDate is less than or equal to
        defaultBudgetTemplateFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where endDate is less than
        defaultBudgetTemplateFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where endDate is greater than
        defaultBudgetTemplateFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedDurationDaysIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedDurationDays equals to
        defaultBudgetTemplateFiltering(
            "estimatedDurationDays.equals=" + DEFAULT_ESTIMATED_DURATION_DAYS,
            "estimatedDurationDays.equals=" + UPDATED_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedDurationDaysIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedDurationDays in
        defaultBudgetTemplateFiltering(
            "estimatedDurationDays.in=" + DEFAULT_ESTIMATED_DURATION_DAYS + "," + UPDATED_ESTIMATED_DURATION_DAYS,
            "estimatedDurationDays.in=" + UPDATED_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedDurationDaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedDurationDays is not null
        defaultBudgetTemplateFiltering("estimatedDurationDays.specified=true", "estimatedDurationDays.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedDurationDaysIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedDurationDays is greater than or equal to
        defaultBudgetTemplateFiltering(
            "estimatedDurationDays.greaterThanOrEqual=" + DEFAULT_ESTIMATED_DURATION_DAYS,
            "estimatedDurationDays.greaterThanOrEqual=" + UPDATED_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedDurationDaysIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedDurationDays is less than or equal to
        defaultBudgetTemplateFiltering(
            "estimatedDurationDays.lessThanOrEqual=" + DEFAULT_ESTIMATED_DURATION_DAYS,
            "estimatedDurationDays.lessThanOrEqual=" + SMALLER_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedDurationDaysIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedDurationDays is less than
        defaultBudgetTemplateFiltering(
            "estimatedDurationDays.lessThan=" + UPDATED_ESTIMATED_DURATION_DAYS,
            "estimatedDurationDays.lessThan=" + DEFAULT_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedDurationDaysIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedDurationDays is greater than
        defaultBudgetTemplateFiltering(
            "estimatedDurationDays.greaterThan=" + SMALLER_ESTIMATED_DURATION_DAYS,
            "estimatedDurationDays.greaterThan=" + DEFAULT_ESTIMATED_DURATION_DAYS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByDurationMonthsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where durationMonths equals to
        defaultBudgetTemplateFiltering(
            "durationMonths.equals=" + DEFAULT_DURATION_MONTHS,
            "durationMonths.equals=" + UPDATED_DURATION_MONTHS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByDurationMonthsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where durationMonths in
        defaultBudgetTemplateFiltering(
            "durationMonths.in=" + DEFAULT_DURATION_MONTHS + "," + UPDATED_DURATION_MONTHS,
            "durationMonths.in=" + UPDATED_DURATION_MONTHS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByDurationMonthsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where durationMonths is not null
        defaultBudgetTemplateFiltering("durationMonths.specified=true", "durationMonths.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByDurationMonthsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where durationMonths is greater than or equal to
        defaultBudgetTemplateFiltering(
            "durationMonths.greaterThanOrEqual=" + DEFAULT_DURATION_MONTHS,
            "durationMonths.greaterThanOrEqual=" + UPDATED_DURATION_MONTHS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByDurationMonthsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where durationMonths is less than or equal to
        defaultBudgetTemplateFiltering(
            "durationMonths.lessThanOrEqual=" + DEFAULT_DURATION_MONTHS,
            "durationMonths.lessThanOrEqual=" + SMALLER_DURATION_MONTHS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByDurationMonthsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where durationMonths is less than
        defaultBudgetTemplateFiltering(
            "durationMonths.lessThan=" + UPDATED_DURATION_MONTHS,
            "durationMonths.lessThan=" + DEFAULT_DURATION_MONTHS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByDurationMonthsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where durationMonths is greater than
        defaultBudgetTemplateFiltering(
            "durationMonths.greaterThan=" + SMALLER_DURATION_MONTHS,
            "durationMonths.greaterThan=" + DEFAULT_DURATION_MONTHS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByMonthlyHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where monthlyHours equals to
        defaultBudgetTemplateFiltering("monthlyHours.equals=" + DEFAULT_MONTHLY_HOURS, "monthlyHours.equals=" + UPDATED_MONTHLY_HOURS);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByMonthlyHoursIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where monthlyHours in
        defaultBudgetTemplateFiltering(
            "monthlyHours.in=" + DEFAULT_MONTHLY_HOURS + "," + UPDATED_MONTHLY_HOURS,
            "monthlyHours.in=" + UPDATED_MONTHLY_HOURS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByMonthlyHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where monthlyHours is not null
        defaultBudgetTemplateFiltering("monthlyHours.specified=true", "monthlyHours.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByMonthlyHoursIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where monthlyHours is greater than or equal to
        defaultBudgetTemplateFiltering(
            "monthlyHours.greaterThanOrEqual=" + DEFAULT_MONTHLY_HOURS,
            "monthlyHours.greaterThanOrEqual=" + UPDATED_MONTHLY_HOURS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByMonthlyHoursIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where monthlyHours is less than or equal to
        defaultBudgetTemplateFiltering(
            "monthlyHours.lessThanOrEqual=" + DEFAULT_MONTHLY_HOURS,
            "monthlyHours.lessThanOrEqual=" + SMALLER_MONTHLY_HOURS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByMonthlyHoursIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where monthlyHours is less than
        defaultBudgetTemplateFiltering("monthlyHours.lessThan=" + UPDATED_MONTHLY_HOURS, "monthlyHours.lessThan=" + DEFAULT_MONTHLY_HOURS);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByMonthlyHoursIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where monthlyHours is greater than
        defaultBudgetTemplateFiltering(
            "monthlyHours.greaterThan=" + SMALLER_MONTHLY_HOURS,
            "monthlyHours.greaterThan=" + DEFAULT_MONTHLY_HOURS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByPlannedHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where plannedHours equals to
        defaultBudgetTemplateFiltering("plannedHours.equals=" + DEFAULT_PLANNED_HOURS, "plannedHours.equals=" + UPDATED_PLANNED_HOURS);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByPlannedHoursIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where plannedHours in
        defaultBudgetTemplateFiltering(
            "plannedHours.in=" + DEFAULT_PLANNED_HOURS + "," + UPDATED_PLANNED_HOURS,
            "plannedHours.in=" + UPDATED_PLANNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByPlannedHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where plannedHours is not null
        defaultBudgetTemplateFiltering("plannedHours.specified=true", "plannedHours.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByPlannedHoursIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where plannedHours is greater than or equal to
        defaultBudgetTemplateFiltering(
            "plannedHours.greaterThanOrEqual=" + DEFAULT_PLANNED_HOURS,
            "plannedHours.greaterThanOrEqual=" + UPDATED_PLANNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByPlannedHoursIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where plannedHours is less than or equal to
        defaultBudgetTemplateFiltering(
            "plannedHours.lessThanOrEqual=" + DEFAULT_PLANNED_HOURS,
            "plannedHours.lessThanOrEqual=" + SMALLER_PLANNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByPlannedHoursIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where plannedHours is less than
        defaultBudgetTemplateFiltering("plannedHours.lessThan=" + UPDATED_PLANNED_HOURS, "plannedHours.lessThan=" + DEFAULT_PLANNED_HOURS);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByPlannedHoursIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where plannedHours is greater than
        defaultBudgetTemplateFiltering(
            "plannedHours.greaterThan=" + SMALLER_PLANNED_HOURS,
            "plannedHours.greaterThan=" + DEFAULT_PLANNED_HOURS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByResourceCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where resourceCount equals to
        defaultBudgetTemplateFiltering("resourceCount.equals=" + DEFAULT_RESOURCE_COUNT, "resourceCount.equals=" + UPDATED_RESOURCE_COUNT);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByResourceCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where resourceCount in
        defaultBudgetTemplateFiltering(
            "resourceCount.in=" + DEFAULT_RESOURCE_COUNT + "," + UPDATED_RESOURCE_COUNT,
            "resourceCount.in=" + UPDATED_RESOURCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByResourceCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where resourceCount is not null
        defaultBudgetTemplateFiltering("resourceCount.specified=true", "resourceCount.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByResourceCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where resourceCount is greater than or equal to
        defaultBudgetTemplateFiltering(
            "resourceCount.greaterThanOrEqual=" + DEFAULT_RESOURCE_COUNT,
            "resourceCount.greaterThanOrEqual=" + UPDATED_RESOURCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByResourceCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where resourceCount is less than or equal to
        defaultBudgetTemplateFiltering(
            "resourceCount.lessThanOrEqual=" + DEFAULT_RESOURCE_COUNT,
            "resourceCount.lessThanOrEqual=" + SMALLER_RESOURCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByResourceCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where resourceCount is less than
        defaultBudgetTemplateFiltering(
            "resourceCount.lessThan=" + UPDATED_RESOURCE_COUNT,
            "resourceCount.lessThan=" + DEFAULT_RESOURCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByResourceCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where resourceCount is greater than
        defaultBudgetTemplateFiltering(
            "resourceCount.greaterThan=" + SMALLER_RESOURCE_COUNT,
            "resourceCount.greaterThan=" + DEFAULT_RESOURCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByIncomeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where income equals to
        defaultBudgetTemplateFiltering("income.equals=" + DEFAULT_INCOME, "income.equals=" + UPDATED_INCOME);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByIncomeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where income in
        defaultBudgetTemplateFiltering("income.in=" + DEFAULT_INCOME + "," + UPDATED_INCOME, "income.in=" + UPDATED_INCOME);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByIncomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where income is not null
        defaultBudgetTemplateFiltering("income.specified=true", "income.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByIncomeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where income is greater than or equal to
        defaultBudgetTemplateFiltering("income.greaterThanOrEqual=" + DEFAULT_INCOME, "income.greaterThanOrEqual=" + UPDATED_INCOME);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByIncomeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where income is less than or equal to
        defaultBudgetTemplateFiltering("income.lessThanOrEqual=" + DEFAULT_INCOME, "income.lessThanOrEqual=" + SMALLER_INCOME);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByIncomeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where income is less than
        defaultBudgetTemplateFiltering("income.lessThan=" + UPDATED_INCOME, "income.lessThan=" + DEFAULT_INCOME);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByIncomeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where income is greater than
        defaultBudgetTemplateFiltering("income.greaterThan=" + SMALLER_INCOME, "income.greaterThan=" + DEFAULT_INCOME);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByOtherTaxesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where otherTaxes equals to
        defaultBudgetTemplateFiltering("otherTaxes.equals=" + DEFAULT_OTHER_TAXES, "otherTaxes.equals=" + UPDATED_OTHER_TAXES);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByOtherTaxesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where otherTaxes in
        defaultBudgetTemplateFiltering(
            "otherTaxes.in=" + DEFAULT_OTHER_TAXES + "," + UPDATED_OTHER_TAXES,
            "otherTaxes.in=" + UPDATED_OTHER_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByOtherTaxesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where otherTaxes is not null
        defaultBudgetTemplateFiltering("otherTaxes.specified=true", "otherTaxes.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByOtherTaxesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where otherTaxes is greater than or equal to
        defaultBudgetTemplateFiltering(
            "otherTaxes.greaterThanOrEqual=" + DEFAULT_OTHER_TAXES,
            "otherTaxes.greaterThanOrEqual=" + UPDATED_OTHER_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByOtherTaxesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where otherTaxes is less than or equal to
        defaultBudgetTemplateFiltering(
            "otherTaxes.lessThanOrEqual=" + DEFAULT_OTHER_TAXES,
            "otherTaxes.lessThanOrEqual=" + SMALLER_OTHER_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByOtherTaxesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where otherTaxes is less than
        defaultBudgetTemplateFiltering("otherTaxes.lessThan=" + UPDATED_OTHER_TAXES, "otherTaxes.lessThan=" + DEFAULT_OTHER_TAXES);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByOtherTaxesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where otherTaxes is greater than
        defaultBudgetTemplateFiltering("otherTaxes.greaterThan=" + SMALLER_OTHER_TAXES, "otherTaxes.greaterThan=" + DEFAULT_OTHER_TAXES);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByDescriptionOtherTaxesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where descriptionOtherTaxes equals to
        defaultBudgetTemplateFiltering(
            "descriptionOtherTaxes.equals=" + DEFAULT_DESCRIPTION_OTHER_TAXES,
            "descriptionOtherTaxes.equals=" + UPDATED_DESCRIPTION_OTHER_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByDescriptionOtherTaxesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where descriptionOtherTaxes in
        defaultBudgetTemplateFiltering(
            "descriptionOtherTaxes.in=" + DEFAULT_DESCRIPTION_OTHER_TAXES + "," + UPDATED_DESCRIPTION_OTHER_TAXES,
            "descriptionOtherTaxes.in=" + UPDATED_DESCRIPTION_OTHER_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByDescriptionOtherTaxesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where descriptionOtherTaxes is not null
        defaultBudgetTemplateFiltering("descriptionOtherTaxes.specified=true", "descriptionOtherTaxes.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByDescriptionOtherTaxesContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where descriptionOtherTaxes contains
        defaultBudgetTemplateFiltering(
            "descriptionOtherTaxes.contains=" + DEFAULT_DESCRIPTION_OTHER_TAXES,
            "descriptionOtherTaxes.contains=" + UPDATED_DESCRIPTION_OTHER_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByDescriptionOtherTaxesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where descriptionOtherTaxes does not contain
        defaultBudgetTemplateFiltering(
            "descriptionOtherTaxes.doesNotContain=" + UPDATED_DESCRIPTION_OTHER_TAXES,
            "descriptionOtherTaxes.doesNotContain=" + DEFAULT_DESCRIPTION_OTHER_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByWithholdingTaxesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where withholdingTaxes equals to
        defaultBudgetTemplateFiltering(
            "withholdingTaxes.equals=" + DEFAULT_WITHHOLDING_TAXES,
            "withholdingTaxes.equals=" + UPDATED_WITHHOLDING_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByWithholdingTaxesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where withholdingTaxes in
        defaultBudgetTemplateFiltering(
            "withholdingTaxes.in=" + DEFAULT_WITHHOLDING_TAXES + "," + UPDATED_WITHHOLDING_TAXES,
            "withholdingTaxes.in=" + UPDATED_WITHHOLDING_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByWithholdingTaxesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where withholdingTaxes is not null
        defaultBudgetTemplateFiltering("withholdingTaxes.specified=true", "withholdingTaxes.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByWithholdingTaxesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where withholdingTaxes is greater than or equal to
        defaultBudgetTemplateFiltering(
            "withholdingTaxes.greaterThanOrEqual=" + DEFAULT_WITHHOLDING_TAXES,
            "withholdingTaxes.greaterThanOrEqual=" + UPDATED_WITHHOLDING_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByWithholdingTaxesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where withholdingTaxes is less than or equal to
        defaultBudgetTemplateFiltering(
            "withholdingTaxes.lessThanOrEqual=" + DEFAULT_WITHHOLDING_TAXES,
            "withholdingTaxes.lessThanOrEqual=" + SMALLER_WITHHOLDING_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByWithholdingTaxesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where withholdingTaxes is less than
        defaultBudgetTemplateFiltering(
            "withholdingTaxes.lessThan=" + UPDATED_WITHHOLDING_TAXES,
            "withholdingTaxes.lessThan=" + DEFAULT_WITHHOLDING_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByWithholdingTaxesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where withholdingTaxes is greater than
        defaultBudgetTemplateFiltering(
            "withholdingTaxes.greaterThan=" + SMALLER_WITHHOLDING_TAXES,
            "withholdingTaxes.greaterThan=" + DEFAULT_WITHHOLDING_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByModAndCifCostsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where modAndCifCosts equals to
        defaultBudgetTemplateFiltering(
            "modAndCifCosts.equals=" + DEFAULT_MOD_AND_CIF_COSTS,
            "modAndCifCosts.equals=" + UPDATED_MOD_AND_CIF_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByModAndCifCostsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where modAndCifCosts in
        defaultBudgetTemplateFiltering(
            "modAndCifCosts.in=" + DEFAULT_MOD_AND_CIF_COSTS + "," + UPDATED_MOD_AND_CIF_COSTS,
            "modAndCifCosts.in=" + UPDATED_MOD_AND_CIF_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByModAndCifCostsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where modAndCifCosts is not null
        defaultBudgetTemplateFiltering("modAndCifCosts.specified=true", "modAndCifCosts.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByModAndCifCostsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where modAndCifCosts is greater than or equal to
        defaultBudgetTemplateFiltering(
            "modAndCifCosts.greaterThanOrEqual=" + DEFAULT_MOD_AND_CIF_COSTS,
            "modAndCifCosts.greaterThanOrEqual=" + UPDATED_MOD_AND_CIF_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByModAndCifCostsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where modAndCifCosts is less than or equal to
        defaultBudgetTemplateFiltering(
            "modAndCifCosts.lessThanOrEqual=" + DEFAULT_MOD_AND_CIF_COSTS,
            "modAndCifCosts.lessThanOrEqual=" + SMALLER_MOD_AND_CIF_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByModAndCifCostsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where modAndCifCosts is less than
        defaultBudgetTemplateFiltering(
            "modAndCifCosts.lessThan=" + UPDATED_MOD_AND_CIF_COSTS,
            "modAndCifCosts.lessThan=" + DEFAULT_MOD_AND_CIF_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByModAndCifCostsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where modAndCifCosts is greater than
        defaultBudgetTemplateFiltering(
            "modAndCifCosts.greaterThan=" + SMALLER_MOD_AND_CIF_COSTS,
            "modAndCifCosts.greaterThan=" + DEFAULT_MOD_AND_CIF_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfit equals to
        defaultBudgetTemplateFiltering("grossProfit.equals=" + DEFAULT_GROSS_PROFIT, "grossProfit.equals=" + UPDATED_GROSS_PROFIT);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfit in
        defaultBudgetTemplateFiltering(
            "grossProfit.in=" + DEFAULT_GROSS_PROFIT + "," + UPDATED_GROSS_PROFIT,
            "grossProfit.in=" + UPDATED_GROSS_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfit is not null
        defaultBudgetTemplateFiltering("grossProfit.specified=true", "grossProfit.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfit is greater than or equal to
        defaultBudgetTemplateFiltering(
            "grossProfit.greaterThanOrEqual=" + DEFAULT_GROSS_PROFIT,
            "grossProfit.greaterThanOrEqual=" + UPDATED_GROSS_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfit is less than or equal to
        defaultBudgetTemplateFiltering(
            "grossProfit.lessThanOrEqual=" + DEFAULT_GROSS_PROFIT,
            "grossProfit.lessThanOrEqual=" + SMALLER_GROSS_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfit is less than
        defaultBudgetTemplateFiltering("grossProfit.lessThan=" + UPDATED_GROSS_PROFIT, "grossProfit.lessThan=" + DEFAULT_GROSS_PROFIT);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfit is greater than
        defaultBudgetTemplateFiltering(
            "grossProfit.greaterThan=" + SMALLER_GROSS_PROFIT,
            "grossProfit.greaterThan=" + DEFAULT_GROSS_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfitPercentage equals to
        defaultBudgetTemplateFiltering(
            "grossProfitPercentage.equals=" + DEFAULT_GROSS_PROFIT_PERCENTAGE,
            "grossProfitPercentage.equals=" + UPDATED_GROSS_PROFIT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfitPercentage in
        defaultBudgetTemplateFiltering(
            "grossProfitPercentage.in=" + DEFAULT_GROSS_PROFIT_PERCENTAGE + "," + UPDATED_GROSS_PROFIT_PERCENTAGE,
            "grossProfitPercentage.in=" + UPDATED_GROSS_PROFIT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfitPercentage is not null
        defaultBudgetTemplateFiltering("grossProfitPercentage.specified=true", "grossProfitPercentage.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfitPercentage is greater than or equal to
        defaultBudgetTemplateFiltering(
            "grossProfitPercentage.greaterThanOrEqual=" + DEFAULT_GROSS_PROFIT_PERCENTAGE,
            "grossProfitPercentage.greaterThanOrEqual=" + UPDATED_GROSS_PROFIT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfitPercentage is less than or equal to
        defaultBudgetTemplateFiltering(
            "grossProfitPercentage.lessThanOrEqual=" + DEFAULT_GROSS_PROFIT_PERCENTAGE,
            "grossProfitPercentage.lessThanOrEqual=" + SMALLER_GROSS_PROFIT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfitPercentage is less than
        defaultBudgetTemplateFiltering(
            "grossProfitPercentage.lessThan=" + UPDATED_GROSS_PROFIT_PERCENTAGE,
            "grossProfitPercentage.lessThan=" + DEFAULT_GROSS_PROFIT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfitPercentage is greater than
        defaultBudgetTemplateFiltering(
            "grossProfitPercentage.greaterThan=" + SMALLER_GROSS_PROFIT_PERCENTAGE,
            "grossProfitPercentage.greaterThan=" + DEFAULT_GROSS_PROFIT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitRuleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfitRule equals to
        defaultBudgetTemplateFiltering(
            "grossProfitRule.equals=" + DEFAULT_GROSS_PROFIT_RULE,
            "grossProfitRule.equals=" + UPDATED_GROSS_PROFIT_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitRuleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfitRule in
        defaultBudgetTemplateFiltering(
            "grossProfitRule.in=" + DEFAULT_GROSS_PROFIT_RULE + "," + UPDATED_GROSS_PROFIT_RULE,
            "grossProfitRule.in=" + UPDATED_GROSS_PROFIT_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitRuleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfitRule is not null
        defaultBudgetTemplateFiltering("grossProfitRule.specified=true", "grossProfitRule.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitRuleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfitRule is greater than or equal to
        defaultBudgetTemplateFiltering(
            "grossProfitRule.greaterThanOrEqual=" + DEFAULT_GROSS_PROFIT_RULE,
            "grossProfitRule.greaterThanOrEqual=" + UPDATED_GROSS_PROFIT_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitRuleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfitRule is less than or equal to
        defaultBudgetTemplateFiltering(
            "grossProfitRule.lessThanOrEqual=" + DEFAULT_GROSS_PROFIT_RULE,
            "grossProfitRule.lessThanOrEqual=" + SMALLER_GROSS_PROFIT_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitRuleIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfitRule is less than
        defaultBudgetTemplateFiltering(
            "grossProfitRule.lessThan=" + UPDATED_GROSS_PROFIT_RULE,
            "grossProfitRule.lessThan=" + DEFAULT_GROSS_PROFIT_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByGrossProfitRuleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where grossProfitRule is greater than
        defaultBudgetTemplateFiltering(
            "grossProfitRule.greaterThan=" + SMALLER_GROSS_PROFIT_RULE,
            "grossProfitRule.greaterThan=" + DEFAULT_GROSS_PROFIT_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByAbsorbedFixedCostsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where absorbedFixedCosts equals to
        defaultBudgetTemplateFiltering(
            "absorbedFixedCosts.equals=" + DEFAULT_ABSORBED_FIXED_COSTS,
            "absorbedFixedCosts.equals=" + UPDATED_ABSORBED_FIXED_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByAbsorbedFixedCostsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where absorbedFixedCosts in
        defaultBudgetTemplateFiltering(
            "absorbedFixedCosts.in=" + DEFAULT_ABSORBED_FIXED_COSTS + "," + UPDATED_ABSORBED_FIXED_COSTS,
            "absorbedFixedCosts.in=" + UPDATED_ABSORBED_FIXED_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByAbsorbedFixedCostsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where absorbedFixedCosts is not null
        defaultBudgetTemplateFiltering("absorbedFixedCosts.specified=true", "absorbedFixedCosts.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByAbsorbedFixedCostsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where absorbedFixedCosts is greater than or equal to
        defaultBudgetTemplateFiltering(
            "absorbedFixedCosts.greaterThanOrEqual=" + DEFAULT_ABSORBED_FIXED_COSTS,
            "absorbedFixedCosts.greaterThanOrEqual=" + UPDATED_ABSORBED_FIXED_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByAbsorbedFixedCostsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where absorbedFixedCosts is less than or equal to
        defaultBudgetTemplateFiltering(
            "absorbedFixedCosts.lessThanOrEqual=" + DEFAULT_ABSORBED_FIXED_COSTS,
            "absorbedFixedCosts.lessThanOrEqual=" + SMALLER_ABSORBED_FIXED_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByAbsorbedFixedCostsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where absorbedFixedCosts is less than
        defaultBudgetTemplateFiltering(
            "absorbedFixedCosts.lessThan=" + UPDATED_ABSORBED_FIXED_COSTS,
            "absorbedFixedCosts.lessThan=" + DEFAULT_ABSORBED_FIXED_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByAbsorbedFixedCostsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where absorbedFixedCosts is greater than
        defaultBudgetTemplateFiltering(
            "absorbedFixedCosts.greaterThan=" + SMALLER_ABSORBED_FIXED_COSTS,
            "absorbedFixedCosts.greaterThan=" + DEFAULT_ABSORBED_FIXED_COSTS
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByOtherExpensesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where otherExpenses equals to
        defaultBudgetTemplateFiltering("otherExpenses.equals=" + DEFAULT_OTHER_EXPENSES, "otherExpenses.equals=" + UPDATED_OTHER_EXPENSES);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByOtherExpensesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where otherExpenses in
        defaultBudgetTemplateFiltering(
            "otherExpenses.in=" + DEFAULT_OTHER_EXPENSES + "," + UPDATED_OTHER_EXPENSES,
            "otherExpenses.in=" + UPDATED_OTHER_EXPENSES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByOtherExpensesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where otherExpenses is not null
        defaultBudgetTemplateFiltering("otherExpenses.specified=true", "otherExpenses.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByOtherExpensesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where otherExpenses is greater than or equal to
        defaultBudgetTemplateFiltering(
            "otherExpenses.greaterThanOrEqual=" + DEFAULT_OTHER_EXPENSES,
            "otherExpenses.greaterThanOrEqual=" + UPDATED_OTHER_EXPENSES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByOtherExpensesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where otherExpenses is less than or equal to
        defaultBudgetTemplateFiltering(
            "otherExpenses.lessThanOrEqual=" + DEFAULT_OTHER_EXPENSES,
            "otherExpenses.lessThanOrEqual=" + SMALLER_OTHER_EXPENSES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByOtherExpensesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where otherExpenses is less than
        defaultBudgetTemplateFiltering(
            "otherExpenses.lessThan=" + UPDATED_OTHER_EXPENSES,
            "otherExpenses.lessThan=" + DEFAULT_OTHER_EXPENSES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByOtherExpensesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where otherExpenses is greater than
        defaultBudgetTemplateFiltering(
            "otherExpenses.greaterThan=" + SMALLER_OTHER_EXPENSES,
            "otherExpenses.greaterThan=" + DEFAULT_OTHER_EXPENSES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByProfitBeforeTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where profitBeforeTax equals to
        defaultBudgetTemplateFiltering(
            "profitBeforeTax.equals=" + DEFAULT_PROFIT_BEFORE_TAX,
            "profitBeforeTax.equals=" + UPDATED_PROFIT_BEFORE_TAX
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByProfitBeforeTaxIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where profitBeforeTax in
        defaultBudgetTemplateFiltering(
            "profitBeforeTax.in=" + DEFAULT_PROFIT_BEFORE_TAX + "," + UPDATED_PROFIT_BEFORE_TAX,
            "profitBeforeTax.in=" + UPDATED_PROFIT_BEFORE_TAX
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByProfitBeforeTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where profitBeforeTax is not null
        defaultBudgetTemplateFiltering("profitBeforeTax.specified=true", "profitBeforeTax.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByProfitBeforeTaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where profitBeforeTax is greater than or equal to
        defaultBudgetTemplateFiltering(
            "profitBeforeTax.greaterThanOrEqual=" + DEFAULT_PROFIT_BEFORE_TAX,
            "profitBeforeTax.greaterThanOrEqual=" + UPDATED_PROFIT_BEFORE_TAX
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByProfitBeforeTaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where profitBeforeTax is less than or equal to
        defaultBudgetTemplateFiltering(
            "profitBeforeTax.lessThanOrEqual=" + DEFAULT_PROFIT_BEFORE_TAX,
            "profitBeforeTax.lessThanOrEqual=" + SMALLER_PROFIT_BEFORE_TAX
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByProfitBeforeTaxIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where profitBeforeTax is less than
        defaultBudgetTemplateFiltering(
            "profitBeforeTax.lessThan=" + UPDATED_PROFIT_BEFORE_TAX,
            "profitBeforeTax.lessThan=" + DEFAULT_PROFIT_BEFORE_TAX
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByProfitBeforeTaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where profitBeforeTax is greater than
        defaultBudgetTemplateFiltering(
            "profitBeforeTax.greaterThan=" + SMALLER_PROFIT_BEFORE_TAX,
            "profitBeforeTax.greaterThan=" + DEFAULT_PROFIT_BEFORE_TAX
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedTaxesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedTaxes equals to
        defaultBudgetTemplateFiltering(
            "estimatedTaxes.equals=" + DEFAULT_ESTIMATED_TAXES,
            "estimatedTaxes.equals=" + UPDATED_ESTIMATED_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedTaxesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedTaxes in
        defaultBudgetTemplateFiltering(
            "estimatedTaxes.in=" + DEFAULT_ESTIMATED_TAXES + "," + UPDATED_ESTIMATED_TAXES,
            "estimatedTaxes.in=" + UPDATED_ESTIMATED_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedTaxesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedTaxes is not null
        defaultBudgetTemplateFiltering("estimatedTaxes.specified=true", "estimatedTaxes.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedTaxesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedTaxes is greater than or equal to
        defaultBudgetTemplateFiltering(
            "estimatedTaxes.greaterThanOrEqual=" + DEFAULT_ESTIMATED_TAXES,
            "estimatedTaxes.greaterThanOrEqual=" + UPDATED_ESTIMATED_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedTaxesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedTaxes is less than or equal to
        defaultBudgetTemplateFiltering(
            "estimatedTaxes.lessThanOrEqual=" + DEFAULT_ESTIMATED_TAXES,
            "estimatedTaxes.lessThanOrEqual=" + SMALLER_ESTIMATED_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedTaxesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedTaxes is less than
        defaultBudgetTemplateFiltering(
            "estimatedTaxes.lessThan=" + UPDATED_ESTIMATED_TAXES,
            "estimatedTaxes.lessThan=" + DEFAULT_ESTIMATED_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedTaxesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedTaxes is greater than
        defaultBudgetTemplateFiltering(
            "estimatedTaxes.greaterThan=" + SMALLER_ESTIMATED_TAXES,
            "estimatedTaxes.greaterThan=" + DEFAULT_ESTIMATED_TAXES
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedNetProfitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedNetProfit equals to
        defaultBudgetTemplateFiltering(
            "estimatedNetProfit.equals=" + DEFAULT_ESTIMATED_NET_PROFIT,
            "estimatedNetProfit.equals=" + UPDATED_ESTIMATED_NET_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedNetProfitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedNetProfit in
        defaultBudgetTemplateFiltering(
            "estimatedNetProfit.in=" + DEFAULT_ESTIMATED_NET_PROFIT + "," + UPDATED_ESTIMATED_NET_PROFIT,
            "estimatedNetProfit.in=" + UPDATED_ESTIMATED_NET_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedNetProfitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedNetProfit is not null
        defaultBudgetTemplateFiltering("estimatedNetProfit.specified=true", "estimatedNetProfit.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedNetProfitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedNetProfit is greater than or equal to
        defaultBudgetTemplateFiltering(
            "estimatedNetProfit.greaterThanOrEqual=" + DEFAULT_ESTIMATED_NET_PROFIT,
            "estimatedNetProfit.greaterThanOrEqual=" + UPDATED_ESTIMATED_NET_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedNetProfitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedNetProfit is less than or equal to
        defaultBudgetTemplateFiltering(
            "estimatedNetProfit.lessThanOrEqual=" + DEFAULT_ESTIMATED_NET_PROFIT,
            "estimatedNetProfit.lessThanOrEqual=" + SMALLER_ESTIMATED_NET_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedNetProfitIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedNetProfit is less than
        defaultBudgetTemplateFiltering(
            "estimatedNetProfit.lessThan=" + UPDATED_ESTIMATED_NET_PROFIT,
            "estimatedNetProfit.lessThan=" + DEFAULT_ESTIMATED_NET_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByEstimatedNetProfitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where estimatedNetProfit is greater than
        defaultBudgetTemplateFiltering(
            "estimatedNetProfit.greaterThan=" + SMALLER_ESTIMATED_NET_PROFIT,
            "estimatedNetProfit.greaterThan=" + DEFAULT_ESTIMATED_NET_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNetMarginPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where netMarginPercentage equals to
        defaultBudgetTemplateFiltering(
            "netMarginPercentage.equals=" + DEFAULT_NET_MARGIN_PERCENTAGE,
            "netMarginPercentage.equals=" + UPDATED_NET_MARGIN_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNetMarginPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where netMarginPercentage in
        defaultBudgetTemplateFiltering(
            "netMarginPercentage.in=" + DEFAULT_NET_MARGIN_PERCENTAGE + "," + UPDATED_NET_MARGIN_PERCENTAGE,
            "netMarginPercentage.in=" + UPDATED_NET_MARGIN_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNetMarginPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where netMarginPercentage is not null
        defaultBudgetTemplateFiltering("netMarginPercentage.specified=true", "netMarginPercentage.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNetMarginPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where netMarginPercentage is greater than or equal to
        defaultBudgetTemplateFiltering(
            "netMarginPercentage.greaterThanOrEqual=" + DEFAULT_NET_MARGIN_PERCENTAGE,
            "netMarginPercentage.greaterThanOrEqual=" + UPDATED_NET_MARGIN_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNetMarginPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where netMarginPercentage is less than or equal to
        defaultBudgetTemplateFiltering(
            "netMarginPercentage.lessThanOrEqual=" + DEFAULT_NET_MARGIN_PERCENTAGE,
            "netMarginPercentage.lessThanOrEqual=" + SMALLER_NET_MARGIN_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNetMarginPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where netMarginPercentage is less than
        defaultBudgetTemplateFiltering(
            "netMarginPercentage.lessThan=" + UPDATED_NET_MARGIN_PERCENTAGE,
            "netMarginPercentage.lessThan=" + DEFAULT_NET_MARGIN_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNetMarginPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where netMarginPercentage is greater than
        defaultBudgetTemplateFiltering(
            "netMarginPercentage.greaterThan=" + SMALLER_NET_MARGIN_PERCENTAGE,
            "netMarginPercentage.greaterThan=" + DEFAULT_NET_MARGIN_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNetMarginRuleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where netMarginRule equals to
        defaultBudgetTemplateFiltering(
            "netMarginRule.equals=" + DEFAULT_NET_MARGIN_RULE,
            "netMarginRule.equals=" + UPDATED_NET_MARGIN_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNetMarginRuleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where netMarginRule in
        defaultBudgetTemplateFiltering(
            "netMarginRule.in=" + DEFAULT_NET_MARGIN_RULE + "," + UPDATED_NET_MARGIN_RULE,
            "netMarginRule.in=" + UPDATED_NET_MARGIN_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNetMarginRuleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where netMarginRule is not null
        defaultBudgetTemplateFiltering("netMarginRule.specified=true", "netMarginRule.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNetMarginRuleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where netMarginRule is greater than or equal to
        defaultBudgetTemplateFiltering(
            "netMarginRule.greaterThanOrEqual=" + DEFAULT_NET_MARGIN_RULE,
            "netMarginRule.greaterThanOrEqual=" + UPDATED_NET_MARGIN_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNetMarginRuleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where netMarginRule is less than or equal to
        defaultBudgetTemplateFiltering(
            "netMarginRule.lessThanOrEqual=" + DEFAULT_NET_MARGIN_RULE,
            "netMarginRule.lessThanOrEqual=" + SMALLER_NET_MARGIN_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNetMarginRuleIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where netMarginRule is less than
        defaultBudgetTemplateFiltering(
            "netMarginRule.lessThan=" + UPDATED_NET_MARGIN_RULE,
            "netMarginRule.lessThan=" + DEFAULT_NET_MARGIN_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByNetMarginRuleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where netMarginRule is greater than
        defaultBudgetTemplateFiltering(
            "netMarginRule.greaterThan=" + SMALLER_NET_MARGIN_RULE,
            "netMarginRule.greaterThan=" + DEFAULT_NET_MARGIN_RULE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByCommissionToReceiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where commissionToReceive equals to
        defaultBudgetTemplateFiltering(
            "commissionToReceive.equals=" + DEFAULT_COMMISSION_TO_RECEIVE,
            "commissionToReceive.equals=" + UPDATED_COMMISSION_TO_RECEIVE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByCommissionToReceiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where commissionToReceive in
        defaultBudgetTemplateFiltering(
            "commissionToReceive.in=" + DEFAULT_COMMISSION_TO_RECEIVE + "," + UPDATED_COMMISSION_TO_RECEIVE,
            "commissionToReceive.in=" + UPDATED_COMMISSION_TO_RECEIVE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByCommissionToReceiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where commissionToReceive is not null
        defaultBudgetTemplateFiltering("commissionToReceive.specified=true", "commissionToReceive.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByCommissionToReceiveIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where commissionToReceive is greater than or equal to
        defaultBudgetTemplateFiltering(
            "commissionToReceive.greaterThanOrEqual=" + DEFAULT_COMMISSION_TO_RECEIVE,
            "commissionToReceive.greaterThanOrEqual=" + UPDATED_COMMISSION_TO_RECEIVE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByCommissionToReceiveIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where commissionToReceive is less than or equal to
        defaultBudgetTemplateFiltering(
            "commissionToReceive.lessThanOrEqual=" + DEFAULT_COMMISSION_TO_RECEIVE,
            "commissionToReceive.lessThanOrEqual=" + SMALLER_COMMISSION_TO_RECEIVE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByCommissionToReceiveIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where commissionToReceive is less than
        defaultBudgetTemplateFiltering(
            "commissionToReceive.lessThan=" + UPDATED_COMMISSION_TO_RECEIVE,
            "commissionToReceive.lessThan=" + DEFAULT_COMMISSION_TO_RECEIVE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByCommissionToReceiveIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where commissionToReceive is greater than
        defaultBudgetTemplateFiltering(
            "commissionToReceive.greaterThan=" + SMALLER_COMMISSION_TO_RECEIVE,
            "commissionToReceive.greaterThan=" + DEFAULT_COMMISSION_TO_RECEIVE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where active equals to
        defaultBudgetTemplateFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where active in
        defaultBudgetTemplateFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where active is not null
        defaultBudgetTemplateFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where createdBy equals to
        defaultBudgetTemplateFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where createdBy in
        defaultBudgetTemplateFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where createdBy is not null
        defaultBudgetTemplateFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where createdBy contains
        defaultBudgetTemplateFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where createdBy does not contain
        defaultBudgetTemplateFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where createdDate equals to
        defaultBudgetTemplateFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where createdDate in
        defaultBudgetTemplateFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where createdDate is not null
        defaultBudgetTemplateFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where lastModifiedBy equals to
        defaultBudgetTemplateFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where lastModifiedBy in
        defaultBudgetTemplateFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where lastModifiedBy is not null
        defaultBudgetTemplateFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where lastModifiedBy contains
        defaultBudgetTemplateFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where lastModifiedBy does not contain
        defaultBudgetTemplateFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where lastModifiedDate equals to
        defaultBudgetTemplateFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where lastModifiedDate in
        defaultBudgetTemplateFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList where lastModifiedDate is not null
        defaultBudgetTemplateFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBudgetTemplatesByCountryIsEqualToSomething() throws Exception {
        Country country;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            budgetTemplateRepository.saveAndFlush(budgetTemplate);
            country = CountryResourceIT.createEntity(em);
        } else {
            country = TestUtil.findAll(em, Country.class).get(0);
        }
        em.persist(country);
        em.flush();
        budgetTemplate.setCountry(country);
        budgetTemplateRepository.saveAndFlush(budgetTemplate);
        Long countryId = country.getId();
        // Get all the budgetTemplateList where country equals to countryId
        defaultBudgetTemplateShouldBeFound("countryId.equals=" + countryId);

        // Get all the budgetTemplateList where country equals to (countryId + 1)
        defaultBudgetTemplateShouldNotBeFound("countryId.equals=" + (countryId + 1));
    }

    private void defaultBudgetTemplateFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBudgetTemplateShouldBeFound(shouldBeFound);
        defaultBudgetTemplateShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBudgetTemplateShouldBeFound(String filter) throws Exception {
        restBudgetTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budgetTemplate.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restBudgetTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBudgetTemplateShouldNotBeFound(String filter) throws Exception {
        restBudgetTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBudgetTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBudgetTemplate() throws Exception {
        // Get the budgetTemplate
        restBudgetTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBudgetTemplate() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budgetTemplate
        BudgetTemplate updatedBudgetTemplate = budgetTemplateRepository.findById(budgetTemplate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBudgetTemplate are not directly saved in db
        em.detach(updatedBudgetTemplate);
        updatedBudgetTemplate
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
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(updatedBudgetTemplate);

        restBudgetTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, budgetTemplateDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetTemplateDTO))
            )
            .andExpect(status().isOk());

        // Validate the BudgetTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBudgetTemplateToMatchAllProperties(updatedBudgetTemplate);
    }

    @Test
    @Transactional
    void putNonExistingBudgetTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetTemplate.setId(longCount.incrementAndGet());

        // Create the BudgetTemplate
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBudgetTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, budgetTemplateDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBudgetTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetTemplate.setId(longCount.incrementAndGet());

        // Create the BudgetTemplate
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBudgetTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetTemplate.setId(longCount.incrementAndGet());

        // Create the BudgetTemplate
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetTemplateMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetTemplateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BudgetTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBudgetTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budgetTemplate using partial update
        BudgetTemplate partialUpdatedBudgetTemplate = new BudgetTemplate();
        partialUpdatedBudgetTemplate.setId(budgetTemplate.getId());

        partialUpdatedBudgetTemplate
            .description(UPDATED_DESCRIPTION)
            .endDate(UPDATED_END_DATE)
            .durationMonths(UPDATED_DURATION_MONTHS)
            .monthlyHours(UPDATED_MONTHLY_HOURS)
            .income(UPDATED_INCOME)
            .descriptionOtherTaxes(UPDATED_DESCRIPTION_OTHER_TAXES)
            .withholdingTaxes(UPDATED_WITHHOLDING_TAXES)
            .modAndCifCosts(UPDATED_MOD_AND_CIF_COSTS)
            .grossProfit(UPDATED_GROSS_PROFIT)
            .grossProfitRule(UPDATED_GROSS_PROFIT_RULE)
            .absorbedFixedCosts(UPDATED_ABSORBED_FIXED_COSTS)
            .estimatedNetProfit(UPDATED_ESTIMATED_NET_PROFIT)
            .netMarginPercentage(UPDATED_NET_MARGIN_PERCENTAGE)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restBudgetTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBudgetTemplate.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBudgetTemplate))
            )
            .andExpect(status().isOk());

        // Validate the BudgetTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBudgetTemplateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBudgetTemplate, budgetTemplate),
            getPersistedBudgetTemplate(budgetTemplate)
        );
    }

    @Test
    @Transactional
    void fullUpdateBudgetTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budgetTemplate using partial update
        BudgetTemplate partialUpdatedBudgetTemplate = new BudgetTemplate();
        partialUpdatedBudgetTemplate.setId(budgetTemplate.getId());

        partialUpdatedBudgetTemplate
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
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restBudgetTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBudgetTemplate.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBudgetTemplate))
            )
            .andExpect(status().isOk());

        // Validate the BudgetTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBudgetTemplateUpdatableFieldsEquals(partialUpdatedBudgetTemplate, getPersistedBudgetTemplate(partialUpdatedBudgetTemplate));
    }

    @Test
    @Transactional
    void patchNonExistingBudgetTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetTemplate.setId(longCount.incrementAndGet());

        // Create the BudgetTemplate
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBudgetTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, budgetTemplateDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(budgetTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBudgetTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetTemplate.setId(longCount.incrementAndGet());

        // Create the BudgetTemplate
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(budgetTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBudgetTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetTemplate.setId(longCount.incrementAndGet());

        // Create the BudgetTemplate
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(budgetTemplateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BudgetTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBudgetTemplate() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the budgetTemplate
        restBudgetTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, budgetTemplate.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return budgetTemplateRepository.count();
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

    protected BudgetTemplate getPersistedBudgetTemplate(BudgetTemplate budgetTemplate) {
        return budgetTemplateRepository.findById(budgetTemplate.getId()).orElseThrow();
    }

    protected void assertPersistedBudgetTemplateToMatchAllProperties(BudgetTemplate expectedBudgetTemplate) {
        assertBudgetTemplateAllPropertiesEquals(expectedBudgetTemplate, getPersistedBudgetTemplate(expectedBudgetTemplate));
    }

    protected void assertPersistedBudgetTemplateToMatchUpdatableProperties(BudgetTemplate expectedBudgetTemplate) {
        assertBudgetTemplateAllUpdatablePropertiesEquals(expectedBudgetTemplate, getPersistedBudgetTemplate(expectedBudgetTemplate));
    }
}
