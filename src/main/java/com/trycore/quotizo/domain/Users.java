package com.trycore.quotizo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Users.
 */
@Entity
@Table(name = "users")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userOwner")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "administrator")
    @JsonIgnoreProperties(value = { "financialParameterType", "country", "administrator", "roleAuthorizeds" }, allowSetters = true)
    private Set<FinancialParameter> financialParameters = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_users__user_role",
        joinColumns = @JoinColumn(name = "users_id"),
        inverseJoinColumns = @JoinColumn(name = "user_role_id")
    )
    @JsonIgnoreProperties(value = { "permissions", "budgets", "financialParameters", "users" }, allowSetters = true)
    private Set<UserRole> userRoles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_users__budget_authorized",
        joinColumns = @JoinColumn(name = "users_id"),
        inverseJoinColumns = @JoinColumn(name = "budget_authorized_id")
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
    private Set<Budget> budgetAuthorizeds = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userAssignedTo")
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
    private Set<Budget> assignedTos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userApprovedBy")
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
    private Set<Budget> approvedBies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Users id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Users name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public Users email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public Users password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Users active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Users createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Users createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Users lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Users lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<Budget> getBudgets() {
        return this.budgets;
    }

    public void setBudgets(Set<Budget> budgets) {
        if (this.budgets != null) {
            this.budgets.forEach(i -> i.setUserOwner(null));
        }
        if (budgets != null) {
            budgets.forEach(i -> i.setUserOwner(this));
        }
        this.budgets = budgets;
    }

    public Users budgets(Set<Budget> budgets) {
        this.setBudgets(budgets);
        return this;
    }

    public Users addBudget(Budget budget) {
        this.budgets.add(budget);
        budget.setUserOwner(this);
        return this;
    }

    public Users removeBudget(Budget budget) {
        this.budgets.remove(budget);
        budget.setUserOwner(null);
        return this;
    }

    public Set<FinancialParameter> getFinancialParameters() {
        return this.financialParameters;
    }

    public void setFinancialParameters(Set<FinancialParameter> financialParameters) {
        if (this.financialParameters != null) {
            this.financialParameters.forEach(i -> i.setAdministrator(null));
        }
        if (financialParameters != null) {
            financialParameters.forEach(i -> i.setAdministrator(this));
        }
        this.financialParameters = financialParameters;
    }

    public Users financialParameters(Set<FinancialParameter> financialParameters) {
        this.setFinancialParameters(financialParameters);
        return this;
    }

    public Users addFinancialParameter(FinancialParameter financialParameter) {
        this.financialParameters.add(financialParameter);
        financialParameter.setAdministrator(this);
        return this;
    }

    public Users removeFinancialParameter(FinancialParameter financialParameter) {
        this.financialParameters.remove(financialParameter);
        financialParameter.setAdministrator(null);
        return this;
    }

    public Set<UserRole> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public Users userRoles(Set<UserRole> userRoles) {
        this.setUserRoles(userRoles);
        return this;
    }

    public Users addUserRole(UserRole userRole) {
        this.userRoles.add(userRole);
        return this;
    }

    public Users removeUserRole(UserRole userRole) {
        this.userRoles.remove(userRole);
        return this;
    }

    public Set<Budget> getBudgetAuthorizeds() {
        return this.budgetAuthorizeds;
    }

    public void setBudgetAuthorizeds(Set<Budget> budgets) {
        this.budgetAuthorizeds = budgets;
    }

    public Users budgetAuthorizeds(Set<Budget> budgets) {
        this.setBudgetAuthorizeds(budgets);
        return this;
    }

    public Users addBudgetAuthorized(Budget budget) {
        this.budgetAuthorizeds.add(budget);
        return this;
    }

    public Users removeBudgetAuthorized(Budget budget) {
        this.budgetAuthorizeds.remove(budget);
        return this;
    }

    public Set<Budget> getAssignedTos() {
        return this.assignedTos;
    }

    public void setAssignedTos(Set<Budget> budgets) {
        if (this.assignedTos != null) {
            this.assignedTos.forEach(i -> i.setUserAssignedTo(null));
        }
        if (budgets != null) {
            budgets.forEach(i -> i.setUserAssignedTo(this));
        }
        this.assignedTos = budgets;
    }

    public Users assignedTos(Set<Budget> budgets) {
        this.setAssignedTos(budgets);
        return this;
    }

    public Users addAssignedTo(Budget budget) {
        this.assignedTos.add(budget);
        budget.setUserAssignedTo(this);
        return this;
    }

    public Users removeAssignedTo(Budget budget) {
        this.assignedTos.remove(budget);
        budget.setUserAssignedTo(null);
        return this;
    }

    public Set<Budget> getApprovedBies() {
        return this.approvedBies;
    }

    public void setApprovedBies(Set<Budget> budgets) {
        if (this.approvedBies != null) {
            this.approvedBies.forEach(i -> i.setUserApprovedBy(null));
        }
        if (budgets != null) {
            budgets.forEach(i -> i.setUserApprovedBy(this));
        }
        this.approvedBies = budgets;
    }

    public Users approvedBies(Set<Budget> budgets) {
        this.setApprovedBies(budgets);
        return this;
    }

    public Users addApprovedBy(Budget budget) {
        this.approvedBies.add(budget);
        budget.setUserApprovedBy(this);
        return this;
    }

    public Users removeApprovedBy(Budget budget) {
        this.approvedBies.remove(budget);
        budget.setUserApprovedBy(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Users)) {
            return false;
        }
        return getId() != null && getId().equals(((Users) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Users{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", active='" + getActive() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
