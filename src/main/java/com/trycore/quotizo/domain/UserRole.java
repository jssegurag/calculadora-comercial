package com.trycore.quotizo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A UserRole.
 */
@Entity
@Table(name = "user_role")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_user_role__permission",
        joinColumns = @JoinColumn(name = "user_role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @JsonIgnoreProperties(value = { "permissions" }, allowSetters = true)
    private Set<Permission> permissions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_user_role__budget",
        joinColumns = @JoinColumn(name = "user_role_id"),
        inverseJoinColumns = @JoinColumn(name = "budget_id")
    )
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
    private Set<Budget> budgets = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_user_role__financial_parameter",
        joinColumns = @JoinColumn(name = "user_role_id"),
        inverseJoinColumns = @JoinColumn(name = "financial_parameter_id")
    )
    @JsonIgnoreProperties(value = { "financialParameterType", "country", "administrator", "roleAuthorizeds" }, allowSetters = true)
    private Set<FinancialParameter> financialParameters = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "userRoles")
    @JsonIgnoreProperties(
        value = { "budgets", "financialParameters", "userRoles", "budgetAuthorizeds", "assignedTos", "approvedBies" },
        allowSetters = true
    )
    private Set<Users> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserRole id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public UserRole name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UserRole createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public UserRole createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public UserRole lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public UserRole lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<Permission> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public UserRole permissions(Set<Permission> permissions) {
        this.setPermissions(permissions);
        return this;
    }

    public UserRole addPermission(Permission permission) {
        this.permissions.add(permission);
        return this;
    }

    public UserRole removePermission(Permission permission) {
        this.permissions.remove(permission);
        return this;
    }

    public Set<Budget> getBudgets() {
        return this.budgets;
    }

    public void setBudgets(Set<Budget> budgets) {
        this.budgets = budgets;
    }

    public UserRole budgets(Set<Budget> budgets) {
        this.setBudgets(budgets);
        return this;
    }

    public UserRole addBudget(Budget budget) {
        this.budgets.add(budget);
        return this;
    }

    public UserRole removeBudget(Budget budget) {
        this.budgets.remove(budget);
        return this;
    }

    public Set<FinancialParameter> getFinancialParameters() {
        return this.financialParameters;
    }

    public void setFinancialParameters(Set<FinancialParameter> financialParameters) {
        this.financialParameters = financialParameters;
    }

    public UserRole financialParameters(Set<FinancialParameter> financialParameters) {
        this.setFinancialParameters(financialParameters);
        return this;
    }

    public UserRole addFinancialParameter(FinancialParameter financialParameter) {
        this.financialParameters.add(financialParameter);
        return this;
    }

    public UserRole removeFinancialParameter(FinancialParameter financialParameter) {
        this.financialParameters.remove(financialParameter);
        return this;
    }

    public Set<Users> getUsers() {
        return this.users;
    }

    public void setUsers(Set<Users> users) {
        if (this.users != null) {
            this.users.forEach(i -> i.removeUserRole(this));
        }
        if (users != null) {
            users.forEach(i -> i.addUserRole(this));
        }
        this.users = users;
    }

    public UserRole users(Set<Users> users) {
        this.setUsers(users);
        return this;
    }

    public UserRole addUsers(Users users) {
        this.users.add(users);
        users.getUserRoles().add(this);
        return this;
    }

    public UserRole removeUsers(Users users) {
        this.users.remove(users);
        users.getUserRoles().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRole)) {
            return false;
        }
        return getId() != null && getId().equals(((UserRole) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserRole{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
