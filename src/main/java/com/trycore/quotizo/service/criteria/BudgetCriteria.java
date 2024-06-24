package com.trycore.quotizo.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.trycore.quotizo.domain.Budget} entity. This class is used
 * in {@link com.trycore.quotizo.web.rest.BudgetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /budgets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BudgetCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private IntegerFilter estimatedDurationDays;

    private IntegerFilter durationMonths;

    private BigDecimalFilter monthlyHours;

    private BigDecimalFilter plannedHours;

    private IntegerFilter resourceCount;

    private BigDecimalFilter income;

    private BigDecimalFilter otherTaxes;

    private StringFilter descriptionOtherTaxes;

    private BigDecimalFilter withholdingTaxes;

    private BigDecimalFilter modAndCifCosts;

    private BigDecimalFilter grossProfit;

    private BigDecimalFilter grossProfitPercentage;

    private BigDecimalFilter grossProfitRule;

    private BigDecimalFilter absorbedFixedCosts;

    private BigDecimalFilter otherExpenses;

    private BigDecimalFilter profitBeforeTax;

    private BigDecimalFilter estimatedTaxes;

    private BigDecimalFilter estimatedNetProfit;

    private BigDecimalFilter netMarginPercentage;

    private BigDecimalFilter netMarginRule;

    private BigDecimalFilter commissionToReceive;

    private BooleanFilter needsApproval;

    private StringFilter approvalDecision;

    private InstantFilter approvalDate;

    private InstantFilter approvalTime;

    private StringFilter approvalComments;

    private StringFilter approvalStatus;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter resourceAllocationId;

    private LongFilter budgetCommentId;

    private LongFilter contryId;

    private LongFilter userAssignedToId;

    private LongFilter userApprovedById;

    private LongFilter userOwnerId;

    private LongFilter authorizedId;

    private LongFilter roleAuthorizedId;

    private Boolean distinct;

    public BudgetCriteria() {}

    public BudgetCriteria(BudgetCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.estimatedDurationDays = other.optionalEstimatedDurationDays().map(IntegerFilter::copy).orElse(null);
        this.durationMonths = other.optionalDurationMonths().map(IntegerFilter::copy).orElse(null);
        this.monthlyHours = other.optionalMonthlyHours().map(BigDecimalFilter::copy).orElse(null);
        this.plannedHours = other.optionalPlannedHours().map(BigDecimalFilter::copy).orElse(null);
        this.resourceCount = other.optionalResourceCount().map(IntegerFilter::copy).orElse(null);
        this.income = other.optionalIncome().map(BigDecimalFilter::copy).orElse(null);
        this.otherTaxes = other.optionalOtherTaxes().map(BigDecimalFilter::copy).orElse(null);
        this.descriptionOtherTaxes = other.optionalDescriptionOtherTaxes().map(StringFilter::copy).orElse(null);
        this.withholdingTaxes = other.optionalWithholdingTaxes().map(BigDecimalFilter::copy).orElse(null);
        this.modAndCifCosts = other.optionalModAndCifCosts().map(BigDecimalFilter::copy).orElse(null);
        this.grossProfit = other.optionalGrossProfit().map(BigDecimalFilter::copy).orElse(null);
        this.grossProfitPercentage = other.optionalGrossProfitPercentage().map(BigDecimalFilter::copy).orElse(null);
        this.grossProfitRule = other.optionalGrossProfitRule().map(BigDecimalFilter::copy).orElse(null);
        this.absorbedFixedCosts = other.optionalAbsorbedFixedCosts().map(BigDecimalFilter::copy).orElse(null);
        this.otherExpenses = other.optionalOtherExpenses().map(BigDecimalFilter::copy).orElse(null);
        this.profitBeforeTax = other.optionalProfitBeforeTax().map(BigDecimalFilter::copy).orElse(null);
        this.estimatedTaxes = other.optionalEstimatedTaxes().map(BigDecimalFilter::copy).orElse(null);
        this.estimatedNetProfit = other.optionalEstimatedNetProfit().map(BigDecimalFilter::copy).orElse(null);
        this.netMarginPercentage = other.optionalNetMarginPercentage().map(BigDecimalFilter::copy).orElse(null);
        this.netMarginRule = other.optionalNetMarginRule().map(BigDecimalFilter::copy).orElse(null);
        this.commissionToReceive = other.optionalCommissionToReceive().map(BigDecimalFilter::copy).orElse(null);
        this.needsApproval = other.optionalNeedsApproval().map(BooleanFilter::copy).orElse(null);
        this.approvalDecision = other.optionalApprovalDecision().map(StringFilter::copy).orElse(null);
        this.approvalDate = other.optionalApprovalDate().map(InstantFilter::copy).orElse(null);
        this.approvalTime = other.optionalApprovalTime().map(InstantFilter::copy).orElse(null);
        this.approvalComments = other.optionalApprovalComments().map(StringFilter::copy).orElse(null);
        this.approvalStatus = other.optionalApprovalStatus().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.resourceAllocationId = other.optionalResourceAllocationId().map(LongFilter::copy).orElse(null);
        this.budgetCommentId = other.optionalBudgetCommentId().map(LongFilter::copy).orElse(null);
        this.contryId = other.optionalContryId().map(LongFilter::copy).orElse(null);
        this.userAssignedToId = other.optionalUserAssignedToId().map(LongFilter::copy).orElse(null);
        this.userApprovedById = other.optionalUserApprovedById().map(LongFilter::copy).orElse(null);
        this.userOwnerId = other.optionalUserOwnerId().map(LongFilter::copy).orElse(null);
        this.authorizedId = other.optionalAuthorizedId().map(LongFilter::copy).orElse(null);
        this.roleAuthorizedId = other.optionalRoleAuthorizedId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BudgetCriteria copy() {
        return new BudgetCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public Optional<LocalDateFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            setStartDate(new LocalDateFilter());
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public Optional<LocalDateFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            setEndDate(new LocalDateFilter());
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public IntegerFilter getEstimatedDurationDays() {
        return estimatedDurationDays;
    }

    public Optional<IntegerFilter> optionalEstimatedDurationDays() {
        return Optional.ofNullable(estimatedDurationDays);
    }

    public IntegerFilter estimatedDurationDays() {
        if (estimatedDurationDays == null) {
            setEstimatedDurationDays(new IntegerFilter());
        }
        return estimatedDurationDays;
    }

    public void setEstimatedDurationDays(IntegerFilter estimatedDurationDays) {
        this.estimatedDurationDays = estimatedDurationDays;
    }

    public IntegerFilter getDurationMonths() {
        return durationMonths;
    }

    public Optional<IntegerFilter> optionalDurationMonths() {
        return Optional.ofNullable(durationMonths);
    }

    public IntegerFilter durationMonths() {
        if (durationMonths == null) {
            setDurationMonths(new IntegerFilter());
        }
        return durationMonths;
    }

    public void setDurationMonths(IntegerFilter durationMonths) {
        this.durationMonths = durationMonths;
    }

    public BigDecimalFilter getMonthlyHours() {
        return monthlyHours;
    }

    public Optional<BigDecimalFilter> optionalMonthlyHours() {
        return Optional.ofNullable(monthlyHours);
    }

    public BigDecimalFilter monthlyHours() {
        if (monthlyHours == null) {
            setMonthlyHours(new BigDecimalFilter());
        }
        return monthlyHours;
    }

    public void setMonthlyHours(BigDecimalFilter monthlyHours) {
        this.monthlyHours = monthlyHours;
    }

    public BigDecimalFilter getPlannedHours() {
        return plannedHours;
    }

    public Optional<BigDecimalFilter> optionalPlannedHours() {
        return Optional.ofNullable(plannedHours);
    }

    public BigDecimalFilter plannedHours() {
        if (plannedHours == null) {
            setPlannedHours(new BigDecimalFilter());
        }
        return plannedHours;
    }

    public void setPlannedHours(BigDecimalFilter plannedHours) {
        this.plannedHours = plannedHours;
    }

    public IntegerFilter getResourceCount() {
        return resourceCount;
    }

    public Optional<IntegerFilter> optionalResourceCount() {
        return Optional.ofNullable(resourceCount);
    }

    public IntegerFilter resourceCount() {
        if (resourceCount == null) {
            setResourceCount(new IntegerFilter());
        }
        return resourceCount;
    }

    public void setResourceCount(IntegerFilter resourceCount) {
        this.resourceCount = resourceCount;
    }

    public BigDecimalFilter getIncome() {
        return income;
    }

    public Optional<BigDecimalFilter> optionalIncome() {
        return Optional.ofNullable(income);
    }

    public BigDecimalFilter income() {
        if (income == null) {
            setIncome(new BigDecimalFilter());
        }
        return income;
    }

    public void setIncome(BigDecimalFilter income) {
        this.income = income;
    }

    public BigDecimalFilter getOtherTaxes() {
        return otherTaxes;
    }

    public Optional<BigDecimalFilter> optionalOtherTaxes() {
        return Optional.ofNullable(otherTaxes);
    }

    public BigDecimalFilter otherTaxes() {
        if (otherTaxes == null) {
            setOtherTaxes(new BigDecimalFilter());
        }
        return otherTaxes;
    }

    public void setOtherTaxes(BigDecimalFilter otherTaxes) {
        this.otherTaxes = otherTaxes;
    }

    public StringFilter getDescriptionOtherTaxes() {
        return descriptionOtherTaxes;
    }

    public Optional<StringFilter> optionalDescriptionOtherTaxes() {
        return Optional.ofNullable(descriptionOtherTaxes);
    }

    public StringFilter descriptionOtherTaxes() {
        if (descriptionOtherTaxes == null) {
            setDescriptionOtherTaxes(new StringFilter());
        }
        return descriptionOtherTaxes;
    }

    public void setDescriptionOtherTaxes(StringFilter descriptionOtherTaxes) {
        this.descriptionOtherTaxes = descriptionOtherTaxes;
    }

    public BigDecimalFilter getWithholdingTaxes() {
        return withholdingTaxes;
    }

    public Optional<BigDecimalFilter> optionalWithholdingTaxes() {
        return Optional.ofNullable(withholdingTaxes);
    }

    public BigDecimalFilter withholdingTaxes() {
        if (withholdingTaxes == null) {
            setWithholdingTaxes(new BigDecimalFilter());
        }
        return withholdingTaxes;
    }

    public void setWithholdingTaxes(BigDecimalFilter withholdingTaxes) {
        this.withholdingTaxes = withholdingTaxes;
    }

    public BigDecimalFilter getModAndCifCosts() {
        return modAndCifCosts;
    }

    public Optional<BigDecimalFilter> optionalModAndCifCosts() {
        return Optional.ofNullable(modAndCifCosts);
    }

    public BigDecimalFilter modAndCifCosts() {
        if (modAndCifCosts == null) {
            setModAndCifCosts(new BigDecimalFilter());
        }
        return modAndCifCosts;
    }

    public void setModAndCifCosts(BigDecimalFilter modAndCifCosts) {
        this.modAndCifCosts = modAndCifCosts;
    }

    public BigDecimalFilter getGrossProfit() {
        return grossProfit;
    }

    public Optional<BigDecimalFilter> optionalGrossProfit() {
        return Optional.ofNullable(grossProfit);
    }

    public BigDecimalFilter grossProfit() {
        if (grossProfit == null) {
            setGrossProfit(new BigDecimalFilter());
        }
        return grossProfit;
    }

    public void setGrossProfit(BigDecimalFilter grossProfit) {
        this.grossProfit = grossProfit;
    }

    public BigDecimalFilter getGrossProfitPercentage() {
        return grossProfitPercentage;
    }

    public Optional<BigDecimalFilter> optionalGrossProfitPercentage() {
        return Optional.ofNullable(grossProfitPercentage);
    }

    public BigDecimalFilter grossProfitPercentage() {
        if (grossProfitPercentage == null) {
            setGrossProfitPercentage(new BigDecimalFilter());
        }
        return grossProfitPercentage;
    }

    public void setGrossProfitPercentage(BigDecimalFilter grossProfitPercentage) {
        this.grossProfitPercentage = grossProfitPercentage;
    }

    public BigDecimalFilter getGrossProfitRule() {
        return grossProfitRule;
    }

    public Optional<BigDecimalFilter> optionalGrossProfitRule() {
        return Optional.ofNullable(grossProfitRule);
    }

    public BigDecimalFilter grossProfitRule() {
        if (grossProfitRule == null) {
            setGrossProfitRule(new BigDecimalFilter());
        }
        return grossProfitRule;
    }

    public void setGrossProfitRule(BigDecimalFilter grossProfitRule) {
        this.grossProfitRule = grossProfitRule;
    }

    public BigDecimalFilter getAbsorbedFixedCosts() {
        return absorbedFixedCosts;
    }

    public Optional<BigDecimalFilter> optionalAbsorbedFixedCosts() {
        return Optional.ofNullable(absorbedFixedCosts);
    }

    public BigDecimalFilter absorbedFixedCosts() {
        if (absorbedFixedCosts == null) {
            setAbsorbedFixedCosts(new BigDecimalFilter());
        }
        return absorbedFixedCosts;
    }

    public void setAbsorbedFixedCosts(BigDecimalFilter absorbedFixedCosts) {
        this.absorbedFixedCosts = absorbedFixedCosts;
    }

    public BigDecimalFilter getOtherExpenses() {
        return otherExpenses;
    }

    public Optional<BigDecimalFilter> optionalOtherExpenses() {
        return Optional.ofNullable(otherExpenses);
    }

    public BigDecimalFilter otherExpenses() {
        if (otherExpenses == null) {
            setOtherExpenses(new BigDecimalFilter());
        }
        return otherExpenses;
    }

    public void setOtherExpenses(BigDecimalFilter otherExpenses) {
        this.otherExpenses = otherExpenses;
    }

    public BigDecimalFilter getProfitBeforeTax() {
        return profitBeforeTax;
    }

    public Optional<BigDecimalFilter> optionalProfitBeforeTax() {
        return Optional.ofNullable(profitBeforeTax);
    }

    public BigDecimalFilter profitBeforeTax() {
        if (profitBeforeTax == null) {
            setProfitBeforeTax(new BigDecimalFilter());
        }
        return profitBeforeTax;
    }

    public void setProfitBeforeTax(BigDecimalFilter profitBeforeTax) {
        this.profitBeforeTax = profitBeforeTax;
    }

    public BigDecimalFilter getEstimatedTaxes() {
        return estimatedTaxes;
    }

    public Optional<BigDecimalFilter> optionalEstimatedTaxes() {
        return Optional.ofNullable(estimatedTaxes);
    }

    public BigDecimalFilter estimatedTaxes() {
        if (estimatedTaxes == null) {
            setEstimatedTaxes(new BigDecimalFilter());
        }
        return estimatedTaxes;
    }

    public void setEstimatedTaxes(BigDecimalFilter estimatedTaxes) {
        this.estimatedTaxes = estimatedTaxes;
    }

    public BigDecimalFilter getEstimatedNetProfit() {
        return estimatedNetProfit;
    }

    public Optional<BigDecimalFilter> optionalEstimatedNetProfit() {
        return Optional.ofNullable(estimatedNetProfit);
    }

    public BigDecimalFilter estimatedNetProfit() {
        if (estimatedNetProfit == null) {
            setEstimatedNetProfit(new BigDecimalFilter());
        }
        return estimatedNetProfit;
    }

    public void setEstimatedNetProfit(BigDecimalFilter estimatedNetProfit) {
        this.estimatedNetProfit = estimatedNetProfit;
    }

    public BigDecimalFilter getNetMarginPercentage() {
        return netMarginPercentage;
    }

    public Optional<BigDecimalFilter> optionalNetMarginPercentage() {
        return Optional.ofNullable(netMarginPercentage);
    }

    public BigDecimalFilter netMarginPercentage() {
        if (netMarginPercentage == null) {
            setNetMarginPercentage(new BigDecimalFilter());
        }
        return netMarginPercentage;
    }

    public void setNetMarginPercentage(BigDecimalFilter netMarginPercentage) {
        this.netMarginPercentage = netMarginPercentage;
    }

    public BigDecimalFilter getNetMarginRule() {
        return netMarginRule;
    }

    public Optional<BigDecimalFilter> optionalNetMarginRule() {
        return Optional.ofNullable(netMarginRule);
    }

    public BigDecimalFilter netMarginRule() {
        if (netMarginRule == null) {
            setNetMarginRule(new BigDecimalFilter());
        }
        return netMarginRule;
    }

    public void setNetMarginRule(BigDecimalFilter netMarginRule) {
        this.netMarginRule = netMarginRule;
    }

    public BigDecimalFilter getCommissionToReceive() {
        return commissionToReceive;
    }

    public Optional<BigDecimalFilter> optionalCommissionToReceive() {
        return Optional.ofNullable(commissionToReceive);
    }

    public BigDecimalFilter commissionToReceive() {
        if (commissionToReceive == null) {
            setCommissionToReceive(new BigDecimalFilter());
        }
        return commissionToReceive;
    }

    public void setCommissionToReceive(BigDecimalFilter commissionToReceive) {
        this.commissionToReceive = commissionToReceive;
    }

    public BooleanFilter getNeedsApproval() {
        return needsApproval;
    }

    public Optional<BooleanFilter> optionalNeedsApproval() {
        return Optional.ofNullable(needsApproval);
    }

    public BooleanFilter needsApproval() {
        if (needsApproval == null) {
            setNeedsApproval(new BooleanFilter());
        }
        return needsApproval;
    }

    public void setNeedsApproval(BooleanFilter needsApproval) {
        this.needsApproval = needsApproval;
    }

    public StringFilter getApprovalDecision() {
        return approvalDecision;
    }

    public Optional<StringFilter> optionalApprovalDecision() {
        return Optional.ofNullable(approvalDecision);
    }

    public StringFilter approvalDecision() {
        if (approvalDecision == null) {
            setApprovalDecision(new StringFilter());
        }
        return approvalDecision;
    }

    public void setApprovalDecision(StringFilter approvalDecision) {
        this.approvalDecision = approvalDecision;
    }

    public InstantFilter getApprovalDate() {
        return approvalDate;
    }

    public Optional<InstantFilter> optionalApprovalDate() {
        return Optional.ofNullable(approvalDate);
    }

    public InstantFilter approvalDate() {
        if (approvalDate == null) {
            setApprovalDate(new InstantFilter());
        }
        return approvalDate;
    }

    public void setApprovalDate(InstantFilter approvalDate) {
        this.approvalDate = approvalDate;
    }

    public InstantFilter getApprovalTime() {
        return approvalTime;
    }

    public Optional<InstantFilter> optionalApprovalTime() {
        return Optional.ofNullable(approvalTime);
    }

    public InstantFilter approvalTime() {
        if (approvalTime == null) {
            setApprovalTime(new InstantFilter());
        }
        return approvalTime;
    }

    public void setApprovalTime(InstantFilter approvalTime) {
        this.approvalTime = approvalTime;
    }

    public StringFilter getApprovalComments() {
        return approvalComments;
    }

    public Optional<StringFilter> optionalApprovalComments() {
        return Optional.ofNullable(approvalComments);
    }

    public StringFilter approvalComments() {
        if (approvalComments == null) {
            setApprovalComments(new StringFilter());
        }
        return approvalComments;
    }

    public void setApprovalComments(StringFilter approvalComments) {
        this.approvalComments = approvalComments;
    }

    public StringFilter getApprovalStatus() {
        return approvalStatus;
    }

    public Optional<StringFilter> optionalApprovalStatus() {
        return Optional.ofNullable(approvalStatus);
    }

    public StringFilter approvalStatus() {
        if (approvalStatus == null) {
            setApprovalStatus(new StringFilter());
        }
        return approvalStatus;
    }

    public void setApprovalStatus(StringFilter approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Optional<StringFilter> optionalLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            setLastModifiedBy(new StringFilter());
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getResourceAllocationId() {
        return resourceAllocationId;
    }

    public Optional<LongFilter> optionalResourceAllocationId() {
        return Optional.ofNullable(resourceAllocationId);
    }

    public LongFilter resourceAllocationId() {
        if (resourceAllocationId == null) {
            setResourceAllocationId(new LongFilter());
        }
        return resourceAllocationId;
    }

    public void setResourceAllocationId(LongFilter resourceAllocationId) {
        this.resourceAllocationId = resourceAllocationId;
    }

    public LongFilter getBudgetCommentId() {
        return budgetCommentId;
    }

    public Optional<LongFilter> optionalBudgetCommentId() {
        return Optional.ofNullable(budgetCommentId);
    }

    public LongFilter budgetCommentId() {
        if (budgetCommentId == null) {
            setBudgetCommentId(new LongFilter());
        }
        return budgetCommentId;
    }

    public void setBudgetCommentId(LongFilter budgetCommentId) {
        this.budgetCommentId = budgetCommentId;
    }

    public LongFilter getContryId() {
        return contryId;
    }

    public Optional<LongFilter> optionalContryId() {
        return Optional.ofNullable(contryId);
    }

    public LongFilter contryId() {
        if (contryId == null) {
            setContryId(new LongFilter());
        }
        return contryId;
    }

    public void setContryId(LongFilter contryId) {
        this.contryId = contryId;
    }

    public LongFilter getUserAssignedToId() {
        return userAssignedToId;
    }

    public Optional<LongFilter> optionalUserAssignedToId() {
        return Optional.ofNullable(userAssignedToId);
    }

    public LongFilter userAssignedToId() {
        if (userAssignedToId == null) {
            setUserAssignedToId(new LongFilter());
        }
        return userAssignedToId;
    }

    public void setUserAssignedToId(LongFilter userAssignedToId) {
        this.userAssignedToId = userAssignedToId;
    }

    public LongFilter getUserApprovedById() {
        return userApprovedById;
    }

    public Optional<LongFilter> optionalUserApprovedById() {
        return Optional.ofNullable(userApprovedById);
    }

    public LongFilter userApprovedById() {
        if (userApprovedById == null) {
            setUserApprovedById(new LongFilter());
        }
        return userApprovedById;
    }

    public void setUserApprovedById(LongFilter userApprovedById) {
        this.userApprovedById = userApprovedById;
    }

    public LongFilter getUserOwnerId() {
        return userOwnerId;
    }

    public Optional<LongFilter> optionalUserOwnerId() {
        return Optional.ofNullable(userOwnerId);
    }

    public LongFilter userOwnerId() {
        if (userOwnerId == null) {
            setUserOwnerId(new LongFilter());
        }
        return userOwnerId;
    }

    public void setUserOwnerId(LongFilter userOwnerId) {
        this.userOwnerId = userOwnerId;
    }

    public LongFilter getAuthorizedId() {
        return authorizedId;
    }

    public Optional<LongFilter> optionalAuthorizedId() {
        return Optional.ofNullable(authorizedId);
    }

    public LongFilter authorizedId() {
        if (authorizedId == null) {
            setAuthorizedId(new LongFilter());
        }
        return authorizedId;
    }

    public void setAuthorizedId(LongFilter authorizedId) {
        this.authorizedId = authorizedId;
    }

    public LongFilter getRoleAuthorizedId() {
        return roleAuthorizedId;
    }

    public Optional<LongFilter> optionalRoleAuthorizedId() {
        return Optional.ofNullable(roleAuthorizedId);
    }

    public LongFilter roleAuthorizedId() {
        if (roleAuthorizedId == null) {
            setRoleAuthorizedId(new LongFilter());
        }
        return roleAuthorizedId;
    }

    public void setRoleAuthorizedId(LongFilter roleAuthorizedId) {
        this.roleAuthorizedId = roleAuthorizedId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BudgetCriteria that = (BudgetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(estimatedDurationDays, that.estimatedDurationDays) &&
            Objects.equals(durationMonths, that.durationMonths) &&
            Objects.equals(monthlyHours, that.monthlyHours) &&
            Objects.equals(plannedHours, that.plannedHours) &&
            Objects.equals(resourceCount, that.resourceCount) &&
            Objects.equals(income, that.income) &&
            Objects.equals(otherTaxes, that.otherTaxes) &&
            Objects.equals(descriptionOtherTaxes, that.descriptionOtherTaxes) &&
            Objects.equals(withholdingTaxes, that.withholdingTaxes) &&
            Objects.equals(modAndCifCosts, that.modAndCifCosts) &&
            Objects.equals(grossProfit, that.grossProfit) &&
            Objects.equals(grossProfitPercentage, that.grossProfitPercentage) &&
            Objects.equals(grossProfitRule, that.grossProfitRule) &&
            Objects.equals(absorbedFixedCosts, that.absorbedFixedCosts) &&
            Objects.equals(otherExpenses, that.otherExpenses) &&
            Objects.equals(profitBeforeTax, that.profitBeforeTax) &&
            Objects.equals(estimatedTaxes, that.estimatedTaxes) &&
            Objects.equals(estimatedNetProfit, that.estimatedNetProfit) &&
            Objects.equals(netMarginPercentage, that.netMarginPercentage) &&
            Objects.equals(netMarginRule, that.netMarginRule) &&
            Objects.equals(commissionToReceive, that.commissionToReceive) &&
            Objects.equals(needsApproval, that.needsApproval) &&
            Objects.equals(approvalDecision, that.approvalDecision) &&
            Objects.equals(approvalDate, that.approvalDate) &&
            Objects.equals(approvalTime, that.approvalTime) &&
            Objects.equals(approvalComments, that.approvalComments) &&
            Objects.equals(approvalStatus, that.approvalStatus) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(resourceAllocationId, that.resourceAllocationId) &&
            Objects.equals(budgetCommentId, that.budgetCommentId) &&
            Objects.equals(contryId, that.contryId) &&
            Objects.equals(userAssignedToId, that.userAssignedToId) &&
            Objects.equals(userApprovedById, that.userApprovedById) &&
            Objects.equals(userOwnerId, that.userOwnerId) &&
            Objects.equals(authorizedId, that.authorizedId) &&
            Objects.equals(roleAuthorizedId, that.roleAuthorizedId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            startDate,
            endDate,
            estimatedDurationDays,
            durationMonths,
            monthlyHours,
            plannedHours,
            resourceCount,
            income,
            otherTaxes,
            descriptionOtherTaxes,
            withholdingTaxes,
            modAndCifCosts,
            grossProfit,
            grossProfitPercentage,
            grossProfitRule,
            absorbedFixedCosts,
            otherExpenses,
            profitBeforeTax,
            estimatedTaxes,
            estimatedNetProfit,
            netMarginPercentage,
            netMarginRule,
            commissionToReceive,
            needsApproval,
            approvalDecision,
            approvalDate,
            approvalTime,
            approvalComments,
            approvalStatus,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            resourceAllocationId,
            budgetCommentId,
            contryId,
            userAssignedToId,
            userApprovedById,
            userOwnerId,
            authorizedId,
            roleAuthorizedId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BudgetCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalEstimatedDurationDays().map(f -> "estimatedDurationDays=" + f + ", ").orElse("") +
            optionalDurationMonths().map(f -> "durationMonths=" + f + ", ").orElse("") +
            optionalMonthlyHours().map(f -> "monthlyHours=" + f + ", ").orElse("") +
            optionalPlannedHours().map(f -> "plannedHours=" + f + ", ").orElse("") +
            optionalResourceCount().map(f -> "resourceCount=" + f + ", ").orElse("") +
            optionalIncome().map(f -> "income=" + f + ", ").orElse("") +
            optionalOtherTaxes().map(f -> "otherTaxes=" + f + ", ").orElse("") +
            optionalDescriptionOtherTaxes().map(f -> "descriptionOtherTaxes=" + f + ", ").orElse("") +
            optionalWithholdingTaxes().map(f -> "withholdingTaxes=" + f + ", ").orElse("") +
            optionalModAndCifCosts().map(f -> "modAndCifCosts=" + f + ", ").orElse("") +
            optionalGrossProfit().map(f -> "grossProfit=" + f + ", ").orElse("") +
            optionalGrossProfitPercentage().map(f -> "grossProfitPercentage=" + f + ", ").orElse("") +
            optionalGrossProfitRule().map(f -> "grossProfitRule=" + f + ", ").orElse("") +
            optionalAbsorbedFixedCosts().map(f -> "absorbedFixedCosts=" + f + ", ").orElse("") +
            optionalOtherExpenses().map(f -> "otherExpenses=" + f + ", ").orElse("") +
            optionalProfitBeforeTax().map(f -> "profitBeforeTax=" + f + ", ").orElse("") +
            optionalEstimatedTaxes().map(f -> "estimatedTaxes=" + f + ", ").orElse("") +
            optionalEstimatedNetProfit().map(f -> "estimatedNetProfit=" + f + ", ").orElse("") +
            optionalNetMarginPercentage().map(f -> "netMarginPercentage=" + f + ", ").orElse("") +
            optionalNetMarginRule().map(f -> "netMarginRule=" + f + ", ").orElse("") +
            optionalCommissionToReceive().map(f -> "commissionToReceive=" + f + ", ").orElse("") +
            optionalNeedsApproval().map(f -> "needsApproval=" + f + ", ").orElse("") +
            optionalApprovalDecision().map(f -> "approvalDecision=" + f + ", ").orElse("") +
            optionalApprovalDate().map(f -> "approvalDate=" + f + ", ").orElse("") +
            optionalApprovalTime().map(f -> "approvalTime=" + f + ", ").orElse("") +
            optionalApprovalComments().map(f -> "approvalComments=" + f + ", ").orElse("") +
            optionalApprovalStatus().map(f -> "approvalStatus=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalResourceAllocationId().map(f -> "resourceAllocationId=" + f + ", ").orElse("") +
            optionalBudgetCommentId().map(f -> "budgetCommentId=" + f + ", ").orElse("") +
            optionalContryId().map(f -> "contryId=" + f + ", ").orElse("") +
            optionalUserAssignedToId().map(f -> "userAssignedToId=" + f + ", ").orElse("") +
            optionalUserApprovedById().map(f -> "userApprovedById=" + f + ", ").orElse("") +
            optionalUserOwnerId().map(f -> "userOwnerId=" + f + ", ").orElse("") +
            optionalAuthorizedId().map(f -> "authorizedId=" + f + ", ").orElse("") +
            optionalRoleAuthorizedId().map(f -> "roleAuthorizedId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
