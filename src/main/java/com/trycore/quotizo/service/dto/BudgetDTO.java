package com.trycore.quotizo.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.trycore.quotizo.domain.Budget} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BudgetDTO implements Serializable {

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

    private Boolean needsApproval;

    private String approvalDecision;

    private Instant approvalDate;

    private Instant approvalTime;

    private String approvalComments;

    private String approvalStatus;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private CountryDTO contry;

    private UsersDTO userAssignedTo;

    private UsersDTO userApprovedBy;

    private UsersDTO userOwner;

    private Set<UsersDTO> authorizeds = new HashSet<>();

    private Set<UserRoleDTO> roleAuthorizeds = new HashSet<>();

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

    public Boolean getNeedsApproval() {
        return needsApproval;
    }

    public void setNeedsApproval(Boolean needsApproval) {
        this.needsApproval = needsApproval;
    }

    public String getApprovalDecision() {
        return approvalDecision;
    }

    public void setApprovalDecision(String approvalDecision) {
        this.approvalDecision = approvalDecision;
    }

    public Instant getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Instant approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Instant getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(Instant approvalTime) {
        this.approvalTime = approvalTime;
    }

    public String getApprovalComments() {
        return approvalComments;
    }

    public void setApprovalComments(String approvalComments) {
        this.approvalComments = approvalComments;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
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

    public CountryDTO getContry() {
        return contry;
    }

    public void setContry(CountryDTO contry) {
        this.contry = contry;
    }

    public UsersDTO getUserAssignedTo() {
        return userAssignedTo;
    }

    public void setUserAssignedTo(UsersDTO userAssignedTo) {
        this.userAssignedTo = userAssignedTo;
    }

    public UsersDTO getUserApprovedBy() {
        return userApprovedBy;
    }

    public void setUserApprovedBy(UsersDTO userApprovedBy) {
        this.userApprovedBy = userApprovedBy;
    }

    public UsersDTO getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(UsersDTO userOwner) {
        this.userOwner = userOwner;
    }

    public Set<UsersDTO> getAuthorizeds() {
        return authorizeds;
    }

    public void setAuthorizeds(Set<UsersDTO> authorizeds) {
        this.authorizeds = authorizeds;
    }

    public Set<UserRoleDTO> getRoleAuthorizeds() {
        return roleAuthorizeds;
    }

    public void setRoleAuthorizeds(Set<UserRoleDTO> roleAuthorizeds) {
        this.roleAuthorizeds = roleAuthorizeds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BudgetDTO)) {
            return false;
        }

        BudgetDTO budgetDTO = (BudgetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, budgetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BudgetDTO{" +
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
            ", contry=" + getContry() +
            ", userAssignedTo=" + getUserAssignedTo() +
            ", userApprovedBy=" + getUserApprovedBy() +
            ", userOwner=" + getUserOwner() +
            ", authorizeds=" + getAuthorizeds() +
            ", roleAuthorizeds=" + getRoleAuthorizeds() +
            "}";
    }
}
