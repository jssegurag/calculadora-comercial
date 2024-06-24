package com.trycore.quotizo.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.trycore.quotizo.domain.ResourceAllocation} entity. This class is used
 * in {@link com.trycore.quotizo.web.rest.ResourceAllocationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /resource-allocations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResourceAllocationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter assignedHours;

    private BigDecimalFilter totalCost;

    private BigDecimalFilter units;

    private BigDecimalFilter capacity;

    private BigDecimalFilter plannedHours;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter budgetId;

    private LongFilter resourceId;

    private LongFilter budgetTemplateId;

    private Boolean distinct;

    public ResourceAllocationCriteria() {}

    public ResourceAllocationCriteria(ResourceAllocationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.assignedHours = other.optionalAssignedHours().map(BigDecimalFilter::copy).orElse(null);
        this.totalCost = other.optionalTotalCost().map(BigDecimalFilter::copy).orElse(null);
        this.units = other.optionalUnits().map(BigDecimalFilter::copy).orElse(null);
        this.capacity = other.optionalCapacity().map(BigDecimalFilter::copy).orElse(null);
        this.plannedHours = other.optionalPlannedHours().map(BigDecimalFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.budgetId = other.optionalBudgetId().map(LongFilter::copy).orElse(null);
        this.resourceId = other.optionalResourceId().map(LongFilter::copy).orElse(null);
        this.budgetTemplateId = other.optionalBudgetTemplateId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ResourceAllocationCriteria copy() {
        return new ResourceAllocationCriteria(this);
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

    public BigDecimalFilter getAssignedHours() {
        return assignedHours;
    }

    public Optional<BigDecimalFilter> optionalAssignedHours() {
        return Optional.ofNullable(assignedHours);
    }

    public BigDecimalFilter assignedHours() {
        if (assignedHours == null) {
            setAssignedHours(new BigDecimalFilter());
        }
        return assignedHours;
    }

    public void setAssignedHours(BigDecimalFilter assignedHours) {
        this.assignedHours = assignedHours;
    }

    public BigDecimalFilter getTotalCost() {
        return totalCost;
    }

    public Optional<BigDecimalFilter> optionalTotalCost() {
        return Optional.ofNullable(totalCost);
    }

    public BigDecimalFilter totalCost() {
        if (totalCost == null) {
            setTotalCost(new BigDecimalFilter());
        }
        return totalCost;
    }

    public void setTotalCost(BigDecimalFilter totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimalFilter getUnits() {
        return units;
    }

    public Optional<BigDecimalFilter> optionalUnits() {
        return Optional.ofNullable(units);
    }

    public BigDecimalFilter units() {
        if (units == null) {
            setUnits(new BigDecimalFilter());
        }
        return units;
    }

    public void setUnits(BigDecimalFilter units) {
        this.units = units;
    }

    public BigDecimalFilter getCapacity() {
        return capacity;
    }

    public Optional<BigDecimalFilter> optionalCapacity() {
        return Optional.ofNullable(capacity);
    }

    public BigDecimalFilter capacity() {
        if (capacity == null) {
            setCapacity(new BigDecimalFilter());
        }
        return capacity;
    }

    public void setCapacity(BigDecimalFilter capacity) {
        this.capacity = capacity;
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

    public LongFilter getBudgetId() {
        return budgetId;
    }

    public Optional<LongFilter> optionalBudgetId() {
        return Optional.ofNullable(budgetId);
    }

    public LongFilter budgetId() {
        if (budgetId == null) {
            setBudgetId(new LongFilter());
        }
        return budgetId;
    }

    public void setBudgetId(LongFilter budgetId) {
        this.budgetId = budgetId;
    }

    public LongFilter getResourceId() {
        return resourceId;
    }

    public Optional<LongFilter> optionalResourceId() {
        return Optional.ofNullable(resourceId);
    }

    public LongFilter resourceId() {
        if (resourceId == null) {
            setResourceId(new LongFilter());
        }
        return resourceId;
    }

    public void setResourceId(LongFilter resourceId) {
        this.resourceId = resourceId;
    }

    public LongFilter getBudgetTemplateId() {
        return budgetTemplateId;
    }

    public Optional<LongFilter> optionalBudgetTemplateId() {
        return Optional.ofNullable(budgetTemplateId);
    }

    public LongFilter budgetTemplateId() {
        if (budgetTemplateId == null) {
            setBudgetTemplateId(new LongFilter());
        }
        return budgetTemplateId;
    }

    public void setBudgetTemplateId(LongFilter budgetTemplateId) {
        this.budgetTemplateId = budgetTemplateId;
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
        final ResourceAllocationCriteria that = (ResourceAllocationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assignedHours, that.assignedHours) &&
            Objects.equals(totalCost, that.totalCost) &&
            Objects.equals(units, that.units) &&
            Objects.equals(capacity, that.capacity) &&
            Objects.equals(plannedHours, that.plannedHours) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(budgetId, that.budgetId) &&
            Objects.equals(resourceId, that.resourceId) &&
            Objects.equals(budgetTemplateId, that.budgetTemplateId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            assignedHours,
            totalCost,
            units,
            capacity,
            plannedHours,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            budgetId,
            resourceId,
            budgetTemplateId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceAllocationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAssignedHours().map(f -> "assignedHours=" + f + ", ").orElse("") +
            optionalTotalCost().map(f -> "totalCost=" + f + ", ").orElse("") +
            optionalUnits().map(f -> "units=" + f + ", ").orElse("") +
            optionalCapacity().map(f -> "capacity=" + f + ", ").orElse("") +
            optionalPlannedHours().map(f -> "plannedHours=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalBudgetId().map(f -> "budgetId=" + f + ", ").orElse("") +
            optionalResourceId().map(f -> "resourceId=" + f + ", ").orElse("") +
            optionalBudgetTemplateId().map(f -> "budgetTemplateId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
