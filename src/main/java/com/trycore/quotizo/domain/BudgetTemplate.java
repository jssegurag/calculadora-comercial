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
 * A BudgetTemplate.
 */
@Entity
@Table(name = "budget_template")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BudgetTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
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

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "budgetTemplate")
    @JsonIgnoreProperties(value = { "budget", "resource", "budgetTemplate" }, allowSetters = true)
    private Set<ResourceAllocation> resourceAllocations = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "financialParameters", "budgets", "budgetTemplates" }, allowSetters = true)
    private Country country;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BudgetTemplate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public BudgetTemplate name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public BudgetTemplate description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public BudgetTemplate startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public BudgetTemplate endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getEstimatedDurationDays() {
        return this.estimatedDurationDays;
    }

    public BudgetTemplate estimatedDurationDays(Integer estimatedDurationDays) {
        this.setEstimatedDurationDays(estimatedDurationDays);
        return this;
    }

    public void setEstimatedDurationDays(Integer estimatedDurationDays) {
        this.estimatedDurationDays = estimatedDurationDays;
    }

    public Integer getDurationMonths() {
        return this.durationMonths;
    }

    public BudgetTemplate durationMonths(Integer durationMonths) {
        this.setDurationMonths(durationMonths);
        return this;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public BigDecimal getMonthlyHours() {
        return this.monthlyHours;
    }

    public BudgetTemplate monthlyHours(BigDecimal monthlyHours) {
        this.setMonthlyHours(monthlyHours);
        return this;
    }

    public void setMonthlyHours(BigDecimal monthlyHours) {
        this.monthlyHours = monthlyHours;
    }

    public BigDecimal getPlannedHours() {
        return this.plannedHours;
    }

    public BudgetTemplate plannedHours(BigDecimal plannedHours) {
        this.setPlannedHours(plannedHours);
        return this;
    }

    public void setPlannedHours(BigDecimal plannedHours) {
        this.plannedHours = plannedHours;
    }

    public Integer getResourceCount() {
        return this.resourceCount;
    }

    public BudgetTemplate resourceCount(Integer resourceCount) {
        this.setResourceCount(resourceCount);
        return this;
    }

    public void setResourceCount(Integer resourceCount) {
        this.resourceCount = resourceCount;
    }

    public BigDecimal getIncome() {
        return this.income;
    }

    public BudgetTemplate income(BigDecimal income) {
        this.setIncome(income);
        return this;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getOtherTaxes() {
        return this.otherTaxes;
    }

    public BudgetTemplate otherTaxes(BigDecimal otherTaxes) {
        this.setOtherTaxes(otherTaxes);
        return this;
    }

    public void setOtherTaxes(BigDecimal otherTaxes) {
        this.otherTaxes = otherTaxes;
    }

    public String getDescriptionOtherTaxes() {
        return this.descriptionOtherTaxes;
    }

    public BudgetTemplate descriptionOtherTaxes(String descriptionOtherTaxes) {
        this.setDescriptionOtherTaxes(descriptionOtherTaxes);
        return this;
    }

    public void setDescriptionOtherTaxes(String descriptionOtherTaxes) {
        this.descriptionOtherTaxes = descriptionOtherTaxes;
    }

    public BigDecimal getWithholdingTaxes() {
        return this.withholdingTaxes;
    }

    public BudgetTemplate withholdingTaxes(BigDecimal withholdingTaxes) {
        this.setWithholdingTaxes(withholdingTaxes);
        return this;
    }

    public void setWithholdingTaxes(BigDecimal withholdingTaxes) {
        this.withholdingTaxes = withholdingTaxes;
    }

    public BigDecimal getModAndCifCosts() {
        return this.modAndCifCosts;
    }

    public BudgetTemplate modAndCifCosts(BigDecimal modAndCifCosts) {
        this.setModAndCifCosts(modAndCifCosts);
        return this;
    }

    public void setModAndCifCosts(BigDecimal modAndCifCosts) {
        this.modAndCifCosts = modAndCifCosts;
    }

    public BigDecimal getGrossProfit() {
        return this.grossProfit;
    }

    public BudgetTemplate grossProfit(BigDecimal grossProfit) {
        this.setGrossProfit(grossProfit);
        return this;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    public BigDecimal getGrossProfitPercentage() {
        return this.grossProfitPercentage;
    }

    public BudgetTemplate grossProfitPercentage(BigDecimal grossProfitPercentage) {
        this.setGrossProfitPercentage(grossProfitPercentage);
        return this;
    }

    public void setGrossProfitPercentage(BigDecimal grossProfitPercentage) {
        this.grossProfitPercentage = grossProfitPercentage;
    }

    public BigDecimal getGrossProfitRule() {
        return this.grossProfitRule;
    }

    public BudgetTemplate grossProfitRule(BigDecimal grossProfitRule) {
        this.setGrossProfitRule(grossProfitRule);
        return this;
    }

    public void setGrossProfitRule(BigDecimal grossProfitRule) {
        this.grossProfitRule = grossProfitRule;
    }

    public BigDecimal getAbsorbedFixedCosts() {
        return this.absorbedFixedCosts;
    }

    public BudgetTemplate absorbedFixedCosts(BigDecimal absorbedFixedCosts) {
        this.setAbsorbedFixedCosts(absorbedFixedCosts);
        return this;
    }

    public void setAbsorbedFixedCosts(BigDecimal absorbedFixedCosts) {
        this.absorbedFixedCosts = absorbedFixedCosts;
    }

    public BigDecimal getOtherExpenses() {
        return this.otherExpenses;
    }

    public BudgetTemplate otherExpenses(BigDecimal otherExpenses) {
        this.setOtherExpenses(otherExpenses);
        return this;
    }

    public void setOtherExpenses(BigDecimal otherExpenses) {
        this.otherExpenses = otherExpenses;
    }

    public BigDecimal getProfitBeforeTax() {
        return this.profitBeforeTax;
    }

    public BudgetTemplate profitBeforeTax(BigDecimal profitBeforeTax) {
        this.setProfitBeforeTax(profitBeforeTax);
        return this;
    }

    public void setProfitBeforeTax(BigDecimal profitBeforeTax) {
        this.profitBeforeTax = profitBeforeTax;
    }

    public BigDecimal getEstimatedTaxes() {
        return this.estimatedTaxes;
    }

    public BudgetTemplate estimatedTaxes(BigDecimal estimatedTaxes) {
        this.setEstimatedTaxes(estimatedTaxes);
        return this;
    }

    public void setEstimatedTaxes(BigDecimal estimatedTaxes) {
        this.estimatedTaxes = estimatedTaxes;
    }

    public BigDecimal getEstimatedNetProfit() {
        return this.estimatedNetProfit;
    }

    public BudgetTemplate estimatedNetProfit(BigDecimal estimatedNetProfit) {
        this.setEstimatedNetProfit(estimatedNetProfit);
        return this;
    }

    public void setEstimatedNetProfit(BigDecimal estimatedNetProfit) {
        this.estimatedNetProfit = estimatedNetProfit;
    }

    public BigDecimal getNetMarginPercentage() {
        return this.netMarginPercentage;
    }

    public BudgetTemplate netMarginPercentage(BigDecimal netMarginPercentage) {
        this.setNetMarginPercentage(netMarginPercentage);
        return this;
    }

    public void setNetMarginPercentage(BigDecimal netMarginPercentage) {
        this.netMarginPercentage = netMarginPercentage;
    }

    public BigDecimal getNetMarginRule() {
        return this.netMarginRule;
    }

    public BudgetTemplate netMarginRule(BigDecimal netMarginRule) {
        this.setNetMarginRule(netMarginRule);
        return this;
    }

    public void setNetMarginRule(BigDecimal netMarginRule) {
        this.netMarginRule = netMarginRule;
    }

    public BigDecimal getCommissionToReceive() {
        return this.commissionToReceive;
    }

    public BudgetTemplate commissionToReceive(BigDecimal commissionToReceive) {
        this.setCommissionToReceive(commissionToReceive);
        return this;
    }

    public void setCommissionToReceive(BigDecimal commissionToReceive) {
        this.commissionToReceive = commissionToReceive;
    }

    public Boolean getActive() {
        return this.active;
    }

    public BudgetTemplate active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public BudgetTemplate createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public BudgetTemplate createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public BudgetTemplate lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public BudgetTemplate lastModifiedDate(Instant lastModifiedDate) {
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
            this.resourceAllocations.forEach(i -> i.setBudgetTemplate(null));
        }
        if (resourceAllocations != null) {
            resourceAllocations.forEach(i -> i.setBudgetTemplate(this));
        }
        this.resourceAllocations = resourceAllocations;
    }

    public BudgetTemplate resourceAllocations(Set<ResourceAllocation> resourceAllocations) {
        this.setResourceAllocations(resourceAllocations);
        return this;
    }

    public BudgetTemplate addResourceAllocation(ResourceAllocation resourceAllocation) {
        this.resourceAllocations.add(resourceAllocation);
        resourceAllocation.setBudgetTemplate(this);
        return this;
    }

    public BudgetTemplate removeResourceAllocation(ResourceAllocation resourceAllocation) {
        this.resourceAllocations.remove(resourceAllocation);
        resourceAllocation.setBudgetTemplate(null);
        return this;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public BudgetTemplate country(Country country) {
        this.setCountry(country);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BudgetTemplate)) {
            return false;
        }
        return getId() != null && getId().equals(((BudgetTemplate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BudgetTemplate{" +
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
            ", active='" + getActive() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
