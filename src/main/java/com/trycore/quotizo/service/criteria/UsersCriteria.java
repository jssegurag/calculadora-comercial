package com.trycore.quotizo.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.trycore.quotizo.domain.Users} entity. This class is used
 * in {@link com.trycore.quotizo.web.rest.UsersResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsersCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter email;

    private StringFilter password;

    private BooleanFilter active;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter budgetId;

    private LongFilter financialParameterId;

    private LongFilter userRoleId;

    private LongFilter budgetAuthorizedId;

    private LongFilter assignedToId;

    private LongFilter approvedById;

    private Boolean distinct;

    public UsersCriteria() {}

    public UsersCriteria(UsersCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.password = other.optionalPassword().map(StringFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.budgetId = other.optionalBudgetId().map(LongFilter::copy).orElse(null);
        this.financialParameterId = other.optionalFinancialParameterId().map(LongFilter::copy).orElse(null);
        this.userRoleId = other.optionalUserRoleId().map(LongFilter::copy).orElse(null);
        this.budgetAuthorizedId = other.optionalBudgetAuthorizedId().map(LongFilter::copy).orElse(null);
        this.assignedToId = other.optionalAssignedToId().map(LongFilter::copy).orElse(null);
        this.approvedById = other.optionalApprovedById().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UsersCriteria copy() {
        return new UsersCriteria(this);
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

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPassword() {
        return password;
    }

    public Optional<StringFilter> optionalPassword() {
        return Optional.ofNullable(password);
    }

    public StringFilter password() {
        if (password == null) {
            setPassword(new StringFilter());
        }
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public Optional<BooleanFilter> optionalActive() {
        return Optional.ofNullable(active);
    }

    public BooleanFilter active() {
        if (active == null) {
            setActive(new BooleanFilter());
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
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

    public LongFilter getUserRoleId() {
        return userRoleId;
    }

    public Optional<LongFilter> optionalUserRoleId() {
        return Optional.ofNullable(userRoleId);
    }

    public LongFilter userRoleId() {
        if (userRoleId == null) {
            setUserRoleId(new LongFilter());
        }
        return userRoleId;
    }

    public void setUserRoleId(LongFilter userRoleId) {
        this.userRoleId = userRoleId;
    }

    public LongFilter getBudgetAuthorizedId() {
        return budgetAuthorizedId;
    }

    public Optional<LongFilter> optionalBudgetAuthorizedId() {
        return Optional.ofNullable(budgetAuthorizedId);
    }

    public LongFilter budgetAuthorizedId() {
        if (budgetAuthorizedId == null) {
            setBudgetAuthorizedId(new LongFilter());
        }
        return budgetAuthorizedId;
    }

    public void setBudgetAuthorizedId(LongFilter budgetAuthorizedId) {
        this.budgetAuthorizedId = budgetAuthorizedId;
    }

    public LongFilter getAssignedToId() {
        return assignedToId;
    }

    public Optional<LongFilter> optionalAssignedToId() {
        return Optional.ofNullable(assignedToId);
    }

    public LongFilter assignedToId() {
        if (assignedToId == null) {
            setAssignedToId(new LongFilter());
        }
        return assignedToId;
    }

    public void setAssignedToId(LongFilter assignedToId) {
        this.assignedToId = assignedToId;
    }

    public LongFilter getApprovedById() {
        return approvedById;
    }

    public Optional<LongFilter> optionalApprovedById() {
        return Optional.ofNullable(approvedById);
    }

    public LongFilter approvedById() {
        if (approvedById == null) {
            setApprovedById(new LongFilter());
        }
        return approvedById;
    }

    public void setApprovedById(LongFilter approvedById) {
        this.approvedById = approvedById;
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
        final UsersCriteria that = (UsersCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(email, that.email) &&
            Objects.equals(password, that.password) &&
            Objects.equals(active, that.active) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(budgetId, that.budgetId) &&
            Objects.equals(financialParameterId, that.financialParameterId) &&
            Objects.equals(userRoleId, that.userRoleId) &&
            Objects.equals(budgetAuthorizedId, that.budgetAuthorizedId) &&
            Objects.equals(assignedToId, that.assignedToId) &&
            Objects.equals(approvedById, that.approvedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            email,
            password,
            active,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            budgetId,
            financialParameterId,
            userRoleId,
            budgetAuthorizedId,
            assignedToId,
            approvedById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsersCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalPassword().map(f -> "password=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalBudgetId().map(f -> "budgetId=" + f + ", ").orElse("") +
            optionalFinancialParameterId().map(f -> "financialParameterId=" + f + ", ").orElse("") +
            optionalUserRoleId().map(f -> "userRoleId=" + f + ", ").orElse("") +
            optionalBudgetAuthorizedId().map(f -> "budgetAuthorizedId=" + f + ", ").orElse("") +
            optionalAssignedToId().map(f -> "assignedToId=" + f + ", ").orElse("") +
            optionalApprovedById().map(f -> "approvedById=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
