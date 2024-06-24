package com.trycore.quotizo.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.trycore.quotizo.domain.BudgetTemplate} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BudgetTemplateDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Lob
    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer estimatedDurationDays;

    private Integer durationMonths;

    private BigDecimal monthlyHours;

    private BigDecimal plannedHours;

    private Integer resourceCount;

    private BigDecimal income;

    private BigDecimal otherTaxes;

    private String descriptionOtherTaxes;

    private BigDecimal withholdingTaxes;

    private BigDecimal modAndCifCosts;

    private BigDecimal grossProfit;

    private BigDecimal grossProfitPercentage;

    private BigDecimal grossProfitRule;

    private BigDecimal absorbedFixedCosts;

    private BigDecimal otherExpenses;

    private BigDecimal profitBeforeTax;

    private BigDecimal estimatedTaxes;

    private BigDecimal estimatedNetProfit;

    private BigDecimal netMarginPercentage;

    private BigDecimal netMarginRule;

    private BigDecimal commissionToReceive;

    @NotNull
    private Boolean active;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private CountryDTO country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getEstimatedDurationDays() {
        return estimatedDurationDays;
    }

    public void setEstimatedDurationDays(Integer estimatedDurationDays) {
        this.estimatedDurationDays = estimatedDurationDays;
    }

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public BigDecimal getMonthlyHours() {
        return monthlyHours;
    }

    public void setMonthlyHours(BigDecimal monthlyHours) {
        this.monthlyHours = monthlyHours;
    }

    public BigDecimal getPlannedHours() {
        return plannedHours;
    }

    public void setPlannedHours(BigDecimal plannedHours) {
        this.plannedHours = plannedHours;
    }

    public Integer getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(Integer resourceCount) {
        this.resourceCount = resourceCount;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getOtherTaxes() {
        return otherTaxes;
    }

    public void setOtherTaxes(BigDecimal otherTaxes) {
        this.otherTaxes = otherTaxes;
    }

    public String getDescriptionOtherTaxes() {
        return descriptionOtherTaxes;
    }

    public void setDescriptionOtherTaxes(String descriptionOtherTaxes) {
        this.descriptionOtherTaxes = descriptionOtherTaxes;
    }

    public BigDecimal getWithholdingTaxes() {
        return withholdingTaxes;
    }

    public void setWithholdingTaxes(BigDecimal withholdingTaxes) {
        this.withholdingTaxes = withholdingTaxes;
    }

    public BigDecimal getModAndCifCosts() {
        return modAndCifCosts;
    }

    public void setModAndCifCosts(BigDecimal modAndCifCosts) {
        this.modAndCifCosts = modAndCifCosts;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    public BigDecimal getGrossProfitPercentage() {
        return grossProfitPercentage;
    }

    public void setGrossProfitPercentage(BigDecimal grossProfitPercentage) {
        this.grossProfitPercentage = grossProfitPercentage;
    }

    public BigDecimal getGrossProfitRule() {
        return grossProfitRule;
    }

    public void setGrossProfitRule(BigDecimal grossProfitRule) {
        this.grossProfitRule = grossProfitRule;
    }

    public BigDecimal getAbsorbedFixedCosts() {
        return absorbedFixedCosts;
    }

    public void setAbsorbedFixedCosts(BigDecimal absorbedFixedCosts) {
        this.absorbedFixedCosts = absorbedFixedCosts;
    }

    public BigDecimal getOtherExpenses() {
        return otherExpenses;
    }

    public void setOtherExpenses(BigDecimal otherExpenses) {
        this.otherExpenses = otherExpenses;
    }

    public BigDecimal getProfitBeforeTax() {
        return profitBeforeTax;
    }

    public void setProfitBeforeTax(BigDecimal profitBeforeTax) {
        this.profitBeforeTax = profitBeforeTax;
    }

    public BigDecimal getEstimatedTaxes() {
        return estimatedTaxes;
    }

    public void setEstimatedTaxes(BigDecimal estimatedTaxes) {
        this.estimatedTaxes = estimatedTaxes;
    }

    public BigDecimal getEstimatedNetProfit() {
        return estimatedNetProfit;
    }

    public void setEstimatedNetProfit(BigDecimal estimatedNetProfit) {
        this.estimatedNetProfit = estimatedNetProfit;
    }

    public BigDecimal getNetMarginPercentage() {
        return netMarginPercentage;
    }

    public void setNetMarginPercentage(BigDecimal netMarginPercentage) {
        this.netMarginPercentage = netMarginPercentage;
    }

    public BigDecimal getNetMarginRule() {
        return netMarginRule;
    }

    public void setNetMarginRule(BigDecimal netMarginRule) {
        this.netMarginRule = netMarginRule;
    }

    public BigDecimal getCommissionToReceive() {
        return commissionToReceive;
    }

    public void setCommissionToReceive(BigDecimal commissionToReceive) {
        this.commissionToReceive = commissionToReceive;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BudgetTemplateDTO)) {
            return false;
        }

        BudgetTemplateDTO budgetTemplateDTO = (BudgetTemplateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, budgetTemplateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BudgetTemplateDTO{" +
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
            ", country=" + getCountry() +
            "}";
    }
}
