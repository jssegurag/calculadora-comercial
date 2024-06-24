package com.trycore.quotizo.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.trycore.quotizo.domain.UserRole} entity. This class is used
 * in {@link com.trycore.quotizo.web.rest.UserRoleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-roles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserRoleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter permissionId;

    private LongFilter budgetId;

    private LongFilter financialParameterId;

    private LongFilter usersId;

    private Boolean distinct;

    public UserRoleCriteria() {}

    public UserRoleCriteria(UserRoleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.permissionId = other.optionalPermissionId().map(LongFilter::copy).orElse(null);
        this.budgetId = other.optionalBudgetId().map(LongFilter::copy).orElse(null);
        this.financialParameterId = other.optionalFinancialParameterId().map(LongFilter::copy).orElse(null);
        this.usersId = other.optionalUsersId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UserRoleCriteria copy() {
        return new UserRoleCriteria(this);
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

    public LongFilter getPermissionId() {
        return permissionId;
    }

    public Optional<LongFilter> optionalPermissionId() {
        return Optional.ofNullable(permissionId);
    }

    public LongFilter permissionId() {
        if (permissionId == null) {
            setPermissionId(new LongFilter());
        }
        return permissionId;
    }

    public void setPermissionId(LongFilter permissionId) {
        this.permissionId = permissionId;
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

    public LongFilter getFinancialParameterId() {
        return financialParameterId;
    }

    public Optional<LongFilter> optionalFinancialParameterId() {
        return Optional.ofNullable(financialParameterId);
    }

    public LongFilter financialParameterId() {
        if (financialParameterId == null) {
            setFinancialParameterId(new LongFilter());
        }
        return financialParameterId;
    }

    public void setFinancialParameterId(LongFilter financialParameterId) {
        this.financialParameterId = financialParameterId;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public Optional<LongFilter> optionalUsersId() {
        return Optional.ofNullable(usersId);
    }

    public LongFilter usersId() {
        if (usersId == null) {
            setUsersId(new LongFilter());
        }
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
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
        final UserRoleCriteria that = (UserRoleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(permissionId, that.permissionId) &&
            Objects.equals(budgetId, that.budgetId) &&
            Objects.equals(financialParameterId, that.financialParameterId) &&
            Objects.equals(usersId, that.usersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            permissionId,
            budgetId,
            financialParameterId,
            usersId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserRoleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalPermissionId().map(f -> "permissionId=" + f + ", ").orElse("") +
            optionalBudgetId().map(f -> "budgetId=" + f + ", ").orElse("") +
            optionalFinancialParameterId().map(f -> "financialParameterId=" + f + ", ").orElse("") +
            optionalUsersId().map(f -> "usersId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
