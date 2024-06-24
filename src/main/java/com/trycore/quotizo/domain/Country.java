package com.trycore.quotizo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    @JsonIgnoreProperties(value = { "financialParameterType", "country", "administrator", "roleAuthorizeds" }, allowSetters = true)
    private Set<FinancialParameter> financialParameters = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contry")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    @JsonIgnoreProperties(value = { "resourceAllocations", "country" }, allowSetters = true)
    private Set<BudgetTemplate> budgetTemplates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Country id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Country name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Country active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Country createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Country createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Country lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Country lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<FinancialParameter> getFinancialParameters() {
        return this.financialParameters;
    }

    public void setFinancialParameters(Set<FinancialParameter> financialParameters) {
        if (this.financialParameters != null) {
            this.financialParameters.forEach(i -> i.setCountry(null));
        }
        if (financialParameters != null) {
            financialParameters.forEach(i -> i.setCountry(this));
        }
        this.financialParameters = financialParameters;
    }

    public Country financialParameters(Set<FinancialParameter> financialParameters) {
        this.setFinancialParameters(financialParameters);
        return this;
    }

    public Country addFinancialParameter(FinancialParameter financialParameter) {
        this.financialParameters.add(financialParameter);
        financialParameter.setCountry(this);
        return this;
    }

    public Country removeFinancialParameter(FinancialParameter financialParameter) {
        this.financialParameters.remove(financialParameter);
        financialParameter.setCountry(null);
        return this;
    }

    public Set<Budget> getBudgets() {
        return this.budgets;
    }

    public void setBudgets(Set<Budget> budgets) {
        if (this.budgets != null) {
            this.budgets.forEach(i -> i.setContry(null));
        }
        if (budgets != null) {
            budgets.forEach(i -> i.setContry(this));
        }
        this.budgets = budgets;
    }

    public Country budgets(Set<Budget> budgets) {
        this.setBudgets(budgets);
        return this;
    }

    public Country addBudget(Budget budget) {
        this.budgets.add(budget);
        budget.setContry(this);
        return this;
    }

    public Country removeBudget(Budget budget) {
        this.budgets.remove(budget);
        budget.setContry(null);
        return this;
    }

    public Set<BudgetTemplate> getBudgetTemplates() {
        return this.budgetTemplates;
    }

    public void setBudgetTemplates(Set<BudgetTemplate> budgetTemplates) {
        if (this.budgetTemplates != null) {
            this.budgetTemplates.forEach(i -> i.setCountry(null));
        }
        if (budgetTemplates != null) {
            budgetTemplates.forEach(i -> i.setCountry(this));
        }
        this.budgetTemplates = budgetTemplates;
    }

    public Country budgetTemplates(Set<BudgetTemplate> budgetTemplates) {
        this.setBudgetTemplates(budgetTemplates);
        return this;
    }

    public Country addBudgetTemplate(BudgetTemplate budgetTemplate) {
        this.budgetTemplates.add(budgetTemplate);
        budgetTemplate.setCountry(this);
        return this;
    }

    public Country removeBudgetTemplate(BudgetTemplate budgetTemplate) {
        this.budgetTemplates.remove(budgetTemplate);
        budgetTemplate.setCountry(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return getId() != null && getId().equals(((Country) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Country{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", active='" + getActive() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
