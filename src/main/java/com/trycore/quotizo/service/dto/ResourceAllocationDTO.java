package com.trycore.quotizo.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.trycore.quotizo.domain.ResourceAllocation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResourceAllocationDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal assignedHours;

    private BigDecimal totalCost;

    private BigDecimal units;

    private BigDecimal capacity;

    private BigDecimal plannedHours;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private BudgetDTO budget;

    private ResourceDTO resource;

    private BudgetTemplateDTO budgetTemplate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAssignedHours() {
        return assignedHours;
    }

    public void setAssignedHours(BigDecimal assignedHours) {
        this.assignedHours = assignedHours;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getUnits() {
        return units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public BigDecimal getCapacity() {
        return capacity;
    }

    public void setCapacity(BigDecimal capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPlannedHours() {
        return plannedHours;
    }

    public void setPlannedHours(BigDecimal plannedHours) {
        this.plannedHours = plannedHours;
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

    public BudgetDTO getBudget() {
        return budget;
    }

    public void setBudget(BudgetDTO budget) {
        this.budget = budget;
    }

    public ResourceDTO getResource() {
        return resource;
    }

    public void setResource(ResourceDTO resource) {
        this.resource = resource;
    }

    public BudgetTemplateDTO getBudgetTemplate() {
        return budgetTemplate;
    }

    public void setBudgetTemplate(BudgetTemplateDTO budgetTemplate) {
        this.budgetTemplate = budgetTemplate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceAllocationDTO)) {
            return false;
        }

        ResourceAllocationDTO resourceAllocationDTO = (ResourceAllocationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resourceAllocationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceAllocationDTO{" +
            "id=" + getId() +
            ", assignedHours=" + getAssignedHours() +
            ", totalCost=" + getTotalCost() +
            ", units=" + getUnits() +
            ", capacity=" + getCapacity() +
            ", plannedHours=" + getPlannedHours() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", budget=" + getBudget() +
            ", resource=" + getResource() +
            ", budgetTemplate=" + getBudgetTemplate() +
            "}";
    }
}
