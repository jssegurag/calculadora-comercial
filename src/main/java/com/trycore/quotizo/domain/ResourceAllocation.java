package com.trycore.quotizo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A ResourceAllocation.
 */
@Entity
@Table(name = "resource_allocation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResourceAllocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "assigned_hours", precision = 21, scale = 2, nullable = false)
    private BigDecimal assignedHours;

    @Column(name = "total_cost", precision = 21, scale = 2)
    private BigDecimal totalCost;

    @Column(name = "units", precision = 21, scale = 2)
    private BigDecimal units;

    @Column(name = "capacity", precision = 21, scale = 2)
    private BigDecimal capacity;

    @Column(name = "planned_hours", precision = 21, scale = 2)
    private BigDecimal plannedHours;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "resourceAllocations",
            "budgetComments",
            "contry",
            "userAssignedTo",
            "userApprovedBy",
            "userOwner",
            "authorizeds",
            "roleAuthorizeds",
        },
        allowSetters = true
    )
    private Budget budget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "position", "resourceAllocations" }, allowSetters = true)
    private Resource resource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "resourceAllocations", "country" }, allowSetters = true)
    private BudgetTemplate budgetTemplate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ResourceAllocation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAssignedHours() {
        return this.assignedHours;
    }

    public ResourceAllocation assignedHours(BigDecimal assignedHours) {
        this.setAssignedHours(assignedHours);
        return this;
    }

    public void setAssignedHours(BigDecimal assignedHours) {
        this.assignedHours = assignedHours;
    }

    public BigDecimal getTotalCost() {
        return this.totalCost;
    }

    public ResourceAllocation totalCost(BigDecimal totalCost) {
        this.setTotalCost(totalCost);
        return this;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getUnits() {
        return this.units;
    }

    public ResourceAllocation units(BigDecimal units) {
        this.setUnits(units);
        return this;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public BigDecimal getCapacity() {
        return this.capacity;
    }

    public ResourceAllocation capacity(BigDecimal capacity) {
        this.setCapacity(capacity);
        return this;
    }

    public void setCapacity(BigDecimal capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPlannedHours() {
        return this.plannedHours;
    }

    public ResourceAllocation plannedHours(BigDecimal plannedHours) {
        this.setPlannedHours(plannedHours);
        return this;
    }

    public void setPlannedHours(BigDecimal plannedHours) {
        this.plannedHours = plannedHours;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ResourceAllocation createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ResourceAllocation createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public ResourceAllocation lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public ResourceAllocation lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Budget getBudget() {
        return this.budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public ResourceAllocation budget(Budget budget) {
        this.setBudget(budget);
        return this;
    }

    public Resource getResource() {
        return this.resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public ResourceAllocation resource(Resource resource) {
        this.setResource(resource);
        return this;
    }

    public BudgetTemplate getBudgetTemplate() {
        return this.budgetTemplate;
    }

    public void setBudgetTemplate(BudgetTemplate budgetTemplate) {
        this.budgetTemplate = budgetTemplate;
    }

    public ResourceAllocation budgetTemplate(BudgetTemplate budgetTemplate) {
        this.setBudgetTemplate(budgetTemplate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceAllocation)) {
            return false;
        }
        return getId() != null && getId().equals(((ResourceAllocation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceAllocation{" +
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
            "}";
    }
}
