package com.trycore.quotizo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Budget.
 */
@Entity
@Table(name = "budget")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Budget implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "estimated_duration_days")
    private Integer estimatedDurationDays;

    @Column(name = "duration_months")
    private Integer durationMonths;

    @Column(name = "monthly_hours", precision = 21, scale = 2)
    private BigDecimal monthlyHours;

    @Column(name = "planned_hours", precision = 21, scale = 2)
    private BigDecimal plannedHours;

    @Column(name = "resource_count")
    private Integer resourceCount;

    @Column(name = "income", precision = 21, scale = 2)
    private BigDecimal income;

    @Column(name = "other_taxes", precision = 21, scale = 2)
    private BigDecimal otherTaxes;

    @Column(name = "description_other_taxes")
    private String descriptionOtherTaxes;

    @Column(name = "withholding_taxes", precision = 21, scale = 2)
    private BigDecimal withholdingTaxes;

    @Column(name = "mod_and_cif_costs", precision = 21, scale = 2)
    private BigDecimal modAndCifCosts;

    @Column(name = "gross_profit", precision = 21, scale = 2)
    private BigDecimal grossProfit;

    @Column(name = "gross_profit_percentage", precision = 21, scale = 2)
    private BigDecimal grossProfitPercentage;

    @Column(name = "gross_profit_rule", precision = 21, scale = 2)
    private BigDecimal grossProfitRule;

    @Column(name = "absorbed_fixed_costs", precision = 21, scale = 2)
    private BigDecimal absorbedFixedCosts;

    @Column(name = "other_expenses", precision = 21, scale = 2)
    private BigDecimal otherExpenses;

    @Column(name = "profit_before_tax", precision = 21, scale = 2)
    private BigDecimal profitBeforeTax;

    @Column(name = "estimated_taxes", precision = 21, scale = 2)
    private BigDecimal estimatedTaxes;

    @Column(name = "estimated_net_profit", precision = 21, scale = 2)
    private BigDecimal estimatedNetProfit;

    @Column(name = "net_margin_percentage", precision = 21, scale = 2)
    private BigDecimal netMarginPercentage;

    @Column(name = "net_margin_rule", precision = 21, scale = 2)
    private BigDecimal netMarginRule;

    @Column(name = "commission_to_receive", precision = 21, scale = 2)
    private BigDecimal commissionToReceive;

    @Column(name = "needs_approval")
    private Boolean needsApproval;

    @Column(name = "approval_decision")
    private String approvalDecision;

    @Column(name = "approval_date")
    private Instant approvalDate;

    @Column(name = "approval_time")
    private Instant approvalTime;

    @Column(name = "approval_comments")
    private String approvalComments;

    @Column(name = "approval_status")
    private String approvalStatus;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "budget")
    @JsonIgnoreProperties(value = { "budget", "resource", "budgetTemplate" }, allowSetters = true)
    private Set<ResourceAllocation> resourceAllocations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "budget")
    @JsonIgnoreProperties(value = { "budget" }, allowSetters = true)
    private Set<BudgetComment> budgetComments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "financialParameters", "budgets", "budgetTemplates" }, allowSetters = true)
    private Country contry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "budgets", "financialParameters", "userRoles", "budgetAuthorizeds", "assignedTos", "approvedBies" },
        allowSetters = true
    )
    private Users userAssignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "budgets", "financialParameters", "userRoles", "budgetAuthorizeds", "assignedTos", "approvedBies" },
        allowSetters = true
    )
    private Users userApprovedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "budgets", "financialParameters", "userRoles", "budgetAuthorizeds", "assignedTos", "approvedBies" },
        allowSetters = true
    )
    private Users userOwner;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "budgetAuthorizeds")
    @JsonIgnoreProperties(
        value = { "budgets", "financialParameters", "userRoles", "budgetAuthorizeds", "assignedTos", "approvedBies" },
        allowSetters = true
    )
    private Set<Users> authorizeds = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "budgets")
    @JsonIgnoreProperties(value = { "permissions", "budgets", "financialParameters", "users" }, allowSetters = true)
    private Set<UserRole> roleAuthorizeds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Budget id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Budget name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Budget description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Budget startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Budget endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getEstimatedDurationDays() {
        return this.estimatedDurationDays;
    }

    public Budget estimatedDurationDays(Integer estimatedDurationDays) {
        this.setEstimatedDurationDays(estimatedDurationDays);
        return this;
    }

    public void setEstimatedDurationDays(Integer estimatedDurationDays) {
        this.estimatedDurationDays = estimatedDurationDays;
    }

    public Integer getDurationMonths() {
        return this.durationMonths;
    }

    public Budget durationMonths(Integer durationMonths) {
        this.setDurationMonths(durationMonths);
        return this;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public BigDecimal getMonthlyHours() {
        return this.monthlyHours;
    }

    public Budget monthlyHours(BigDecimal monthlyHours) {
        this.setMonthlyHours(monthlyHours);
        return this;
    }

    public void setMonthlyHours(BigDecimal monthlyHours) {
        this.monthlyHours = monthlyHours;
    }

    public BigDecimal getPlannedHours() {
        return this.plannedHours;
    }

    public Budget plannedHours(BigDecimal plannedHours) {
        this.setPlannedHours(plannedHours);
        return this;
    }

    public void setPlannedHours(BigDecimal plannedHours) {
        this.plannedHours = plannedHours;
    }

    public Integer getResourceCount() {
        return this.resourceCount;
    }

    public Budget resourceCount(Integer resourceCount) {
        this.setResourceCount(resourceCount);
        return this;
    }

    public void setResourceCount(Integer resourceCount) {
        this.resourceCount = resourceCount;
    }

    public BigDecimal getIncome() {
        return this.income;
    }

    public Budget income(BigDecimal income) {
        this.setIncome(income);
        return this;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getOtherTaxes() {
        return this.otherTaxes;
    }

    public Budget otherTaxes(BigDecimal otherTaxes) {
        this.setOtherTaxes(otherTaxes);
        return this;
    }

    public void setOtherTaxes(BigDecimal otherTaxes) {
        this.otherTaxes = otherTaxes;
    }

    public String getDescriptionOtherTaxes() {
        return this.descriptionOtherTaxes;
    }

    public Budget descriptionOtherTaxes(String descriptionOtherTaxes) {
        this.setDescriptionOtherTaxes(descriptionOtherTaxes);
        return this;
    }

    public void setDescriptionOtherTaxes(String descriptionOtherTaxes) {
        this.descriptionOtherTaxes = descriptionOtherTaxes;
    }

    public BigDecimal getWithholdingTaxes() {
        return this.withholdingTaxes;
    }

    public Budget withholdingTaxes(BigDecimal withholdingTaxes) {
        this.setWithholdingTaxes(withholdingTaxes);
        return this;
    }

    public void setWithholdingTaxes(BigDecimal withholdingTaxes) {
        this.withholdingTaxes = withholdingTaxes;
    }

    public BigDecimal getModAndCifCosts() {
        return this.modAndCifCosts;
    }

    public Budget modAndCifCosts(BigDecimal modAndCifCosts) {
        this.setModAndCifCosts(modAndCifCosts);
        return this;
    }

    public void setModAndCifCosts(BigDecimal modAndCifCosts) {
        this.modAndCifCosts = modAndCifCosts;
    }

    public BigDecimal getGrossProfit() {
        return this.grossProfit;
    }

    public Budget grossProfit(BigDecimal grossProfit) {
        this.setGrossProfit(grossProfit);
        return this;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    public BigDecimal getGrossProfitPercentage() {
        return this.grossProfitPercentage;
    }

    public Budget grossProfitPercentage(BigDecimal grossProfitPercentage) {
        this.setGrossProfitPercentage(grossProfitPercentage);
        return this;
    }

    public void setGrossProfitPercentage(BigDecimal grossProfitPercentage) {
        this.grossProfitPercentage = grossProfitPercentage;
    }

    public BigDecimal getGrossProfitRule() {
        return this.grossProfitRule;
    }

    public Budget grossProfitRule(BigDecimal grossProfitRule) {
        this.setGrossProfitRule(grossProfitRule);
        return this;
    }

    public void setGrossProfitRule(BigDecimal grossProfitRule) {
        this.grossProfitRule = grossProfitRule;
    }

    public BigDecimal getAbsorbedFixedCosts() {
        return this.absorbedFixedCosts;
    }

    public Budget absorbedFixedCosts(BigDecimal absorbedFixedCosts) {
        this.setAbsorbedFixedCosts(absorbedFixedCosts);
        return this;
    }

    public void setAbsorbedFixedCosts(BigDecimal absorbedFixedCosts) {
        this.absorbedFixedCosts = absorbedFixedCosts;
    }

    public BigDecimal getOtherExpenses() {
        return this.otherExpenses;
    }

    public Budget otherExpenses(BigDecimal otherExpenses) {
        this.setOtherExpenses(otherExpenses);
        return this;
    }

    public void setOtherExpenses(BigDecimal otherExpenses) {
        this.otherExpenses = otherExpenses;
    }

    public BigDecimal getProfitBeforeTax() {
        return this.profitBeforeTax;
    }

    public Budget profitBeforeTax(BigDecimal profitBeforeTax) {
        this.setProfitBeforeTax(profitBeforeTax);
        return this;
    }

    public void setProfitBeforeTax(BigDecimal profitBeforeTax) {
        this.profitBeforeTax = profitBeforeTax;
    }

    public BigDecimal getEstimatedTaxes() {
        return this.estimatedTaxes;
    }

    public Budget estimatedTaxes(BigDecimal estimatedTaxes) {
        this.setEstimatedTaxes(estimatedTaxes);
        return this;
    }

    public void setEstimatedTaxes(BigDecimal estimatedTaxes) {
        this.estimatedTaxes = estimatedTaxes;
    }

    public BigDecimal getEstimatedNetProfit() {
        return this.estimatedNetProfit;
    }

    public Budget estimatedNetProfit(BigDecimal estimatedNetProfit) {
        this.setEstimatedNetProfit(estimatedNetProfit);
        return this;
    }

    public void setEstimatedNetProfit(BigDecimal estimatedNetProfit) {
        this.estimatedNetProfit = estimatedNetProfit;
    }

    public BigDecimal getNetMarginPercentage() {
        return this.netMarginPercentage;
    }

    public Budget netMarginPercentage(BigDecimal netMarginPercentage) {
        this.setNetMarginPercentage(netMarginPercentage);
        return this;
    }

    public void setNetMarginPercentage(BigDecimal netMarginPercentage) {
        this.netMarginPercentage = netMarginPercentage;
    }

    public BigDecimal getNetMarginRule() {
        return this.netMarginRule;
    }

    public Budget netMarginRule(BigDecimal netMarginRule) {
        this.setNetMarginRule(netMarginRule);
        return this;
    }

    public void setNetMarginRule(BigDecimal netMarginRule) {
        this.netMarginRule = netMarginRule;
    }

    public BigDecimal getCommissionToReceive() {
        return this.commissionToReceive;
    }

    public Budget commissionToReceive(BigDecimal commissionToReceive) {
        this.setCommissionToReceive(commissionToReceive);
        return this;
    }

    public void setCommissionToReceive(BigDecimal commissionToReceive) {
        this.commissionToReceive = commissionToReceive;
    }

    public Boolean getNeedsApproval() {
        return this.needsApproval;
    }

    public Budget needsApproval(Boolean needsApproval) {
        this.setNeedsApproval(needsApproval);
        return this;
    }

    public void setNeedsApproval(Boolean needsApproval) {
        this.needsApproval = needsApproval;
    }

    public String getApprovalDecision() {
        return this.approvalDecision;
    }

    public Budget approvalDecision(String approvalDecision) {
        this.setApprovalDecision(approvalDecision);
        return this;
    }

    public void setApprovalDecision(String approvalDecision) {
        this.approvalDecision = approvalDecision;
    }

    public Instant getApprovalDate() {
        return this.approvalDate;
    }

    public Budget approvalDate(Instant approvalDate) {
        this.setApprovalDate(approvalDate);
        return this;
    }

    public void setApprovalDate(Instant approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Instant getApprovalTime() {
        return this.approvalTime;
    }

    public Budget approvalTime(Instant approvalTime) {
        this.setApprovalTime(approvalTime);
        return this;
    }

    public void setApprovalTime(Instant approvalTime) {
        this.approvalTime = approvalTime;
    }

    public String getApprovalComments() {
        return this.approvalComments;
    }

    public Budget approvalComments(String approvalComments) {
        this.setApprovalComments(approvalComments);
        return this;
    }

    public void setApprovalComments(String approvalComments) {
        this.approvalComments = approvalComments;
    }

    public String getApprovalStatus() {
        return this.approvalStatus;
    }

    public Budget approvalStatus(String approvalStatus) {
        this.setApprovalStatus(approvalStatus);
        return this;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Budget createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Budget createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Budget lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Budget lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<ResourceAllocation> getResourceAllocations() {
        return this.resourceAllocations;
    }

    public void setResourceAllocations(Set<ResourceAllocation> resourceAllocations) {
        if (this.resourceAllocations != null) {
            this.resourceAllocations.forEach(i -> i.setBudget(null));
        }
        if (resourceAllocations != null) {
            resourceAllocations.forEach(i -> i.setBudget(this));
        }
        this.resourceAllocations = resourceAllocations;
    }

    public Budget resourceAllocations(Set<ResourceAllocation> resourceAllocations) {
        this.setResourceAllocations(resourceAllocations);
        return this;
    }

    public Budget addResourceAllocation(ResourceAllocation resourceAllocation) {
        this.resourceAllocations.add(resourceAllocation);
        resourceAllocation.setBudget(this);
        return this;
    }

    public Budget removeResourceAllocation(ResourceAllocation resourceAllocation) {
        this.resourceAllocations.remove(resourceAllocation);
        resourceAllocation.setBudget(null);
        return this;
    }

    public Set<BudgetComment> getBudgetComments() {
        return this.budgetComments;
    }

    public void setBudgetComments(Set<BudgetComment> budgetComments) {
        if (this.budgetComments != null) {
            this.budgetComments.forEach(i -> i.setBudget(null));
        }
        if (budgetComments != null) {
            budgetComments.forEach(i -> i.setBudget(this));
        }
        this.budgetComments = budgetComments;
    }

    public Budget budgetComments(Set<BudgetComment> budgetComments) {
        this.setBudgetComments(budgetComments);
        return this;
    }

    public Budget addBudgetComment(BudgetComment budgetComment) {
        this.budgetComments.add(budgetComment);
        budgetComment.setBudget(this);
        return this;
    }

    public Budget removeBudgetComment(BudgetComment budgetComment) {
        this.budgetComments.remove(budgetComment);
        budgetComment.setBudget(null);
        return this;
    }

    public Country getContry() {
        return this.contry;
    }

    public void setContry(Country country) {
        this.contry = country;
    }

    public Budget contry(Country country) {
        this.setContry(country);
        return this;
    }

    public Users getUserAssignedTo() {
        return this.userAssignedTo;
    }

    public void setUserAssignedTo(Users users) {
        this.userAssignedTo = users;
    }

    public Budget userAssignedTo(Users users) {
        this.setUserAssignedTo(users);
        return this;
    }

    public Users getUserApprovedBy() {
        return this.userApprovedBy;
    }

    public void setUserApprovedBy(Users users) {
        this.userApprovedBy = users;
    }

    public Budget userApprovedBy(Users users) {
        this.setUserApprovedBy(users);
        return this;
    }

    public Users getUserOwner() {
        return this.userOwner;
    }

    public void setUserOwner(Users users) {
        this.userOwner = users;
    }

    public Budget userOwner(Users users) {
        this.setUserOwner(users);
        return this;
    }

    public Set<Users> getAuthorizeds() {
        return this.authorizeds;
    }

    public void setAuthorizeds(Set<Users> users) {
        if (this.authorizeds != null) {
            this.authorizeds.forEach(i -> i.removeBudgetAuthorized(this));
        }
        if (users != null) {
            users.forEach(i -> i.addBudgetAuthorized(this));
        }
        this.authorizeds = users;
    }

    public Budget authorizeds(Set<Users> users) {
        this.setAuthorizeds(users);
        return this;
    }

    public Budget addAuthorized(Users users) {
        this.authorizeds.add(users);
        users.getBudgetAuthorizeds().add(this);
        return this;
    }

    public Budget removeAuthorized(Users users) {
        this.authorizeds.remove(users);
        users.getBudgetAuthorizeds().remove(this);
        return this;
    }

    public Set<UserRole> getRoleAuthorizeds() {
        return this.roleAuthorizeds;
    }

    public void setRoleAuthorizeds(Set<UserRole> userRoles) {
        if (this.roleAuthorizeds != null) {
            this.roleAuthorizeds.forEach(i -> i.removeBudget(this));
        }
        if (userRoles != null) {
            userRoles.forEach(i -> i.addBudget(this));
        }
        this.roleAuthorizeds = userRoles;
    }

    public Budget roleAuthorizeds(Set<UserRole> userRoles) {
        this.setRoleAuthorizeds(userRoles);
        return this;
    }

    public Budget addRoleAuthorized(UserRole userRole) {
        this.roleAuthorizeds.add(userRole);
        userRole.getBudgets().add(this);
        return this;
    }

    public Budget removeRoleAuthorized(UserRole userRole) {
        this.roleAuthorizeds.remove(userRole);
        userRole.getBudgets().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Budget)) {
            return false;
        }
        return getId() != null && getId().equals(((Budget) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Budget{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", estimatedDurationDays=" + getEstimatedDurationDays() +
            ", durationMonths=" + getDurationMonths() +
            ", monthlyHours=" + getMonthlyHours() +
            ", plannedHours=" + getPlannedHours() +
            ", resourceCount=" + getResourceCount() +
            ", income=" + getIncome() +
            ", otherTaxes=" + getOtherTaxes() +
            ", descriptionOtherTaxes='" + getDescriptionOtherTaxes() + "'" +
            ", withholdingTaxes=" + getWithholdingTaxes() +
            ", modAndCifCosts=" + getModAndCifCosts() +
            ", grossProfit=" + getGrossProfit() +
            ", grossProfitPercentage=" + getGrossProfitPercentage() +
            ", grossProfitRule=" + getGrossProfitRule() +
            ", absorbedFixedCosts=" + getAbsorbedFixedCosts() +
            ", otherExpenses=" + getOtherExpenses() +
            ", profitBeforeTax=" + getProfitBeforeTax() +
            ", estimatedTaxes=" + getEstimatedTaxes() +
            ", estimatedNetProfit=" + getEstimatedNetProfit() +
            ", netMarginPercentage=" + getNetMarginPercentage() +
            ", netMarginRule=" + getNetMarginRule() +
            ", commissionToReceive=" + getCommissionToReceive() +
            ", needsApproval='" + getNeedsApproval() + "'" +
            ", approvalDecision='" + getApprovalDecision() + "'" +
            ", approvalDate='" + getApprovalDate() + "'" +
            ", approvalTime='" + getApprovalTime() + "'" +
            ", approvalComments='" + getApprovalComments() + "'" +
            ", approvalStatus='" + getApprovalStatus() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
