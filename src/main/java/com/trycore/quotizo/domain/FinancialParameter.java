package com.trycore.quotizo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A FinancialParameter.
 */
@Entity
@Table(name = "financial_parameter")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FinancialParameter implements Serializable {

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
    @Column(name = "value", precision = 21, scale = 2, nullable = false)
    private BigDecimal value;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "mandatory", nullable = false)
    private Boolean mandatory;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "financialParameters" }, allowSetters = true)
    private FinancialParameterType financialParameterType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "financialParameters", "budgets", "budgetTemplates" }, allowSetters = true)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "budgets", "financialParameters", "userRoles", "budgetAuthorizeds", "assignedTos", "approvedBies" },
        allowSetters = true
    )
    private Users administrator;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "financialParameters")
    @JsonIgnoreProperties(value = { "permissions", "budgets", "financialParameters", "users" }, allowSetters = true)
    private Set<UserRole> roleAuthorizeds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FinancialParameter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public FinancialParameter name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public FinancialParameter value(BigDecimal value) {
        this.setValue(value);
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Boolean getActive() {
        return this.active;
    }

    public FinancialParameter active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getMandatory() {
        return this.mandatory;
    }

    public FinancialParameter mandatory(Boolean mandatory) {
        this.setMandatory(mandatory);
        return this;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public FinancialParameter createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public FinancialParameter createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public FinancialParameter lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public FinancialParameter lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public FinancialParameterType getFinancialParameterType() {
        return this.financialParameterType;
    }

    public void setFinancialParameterType(FinancialParameterType financialParameterType) {
        this.financialParameterType = financialParameterType;
    }

    public FinancialParameter financialParameterType(FinancialParameterType financialParameterType) {
        this.setFinancialParameterType(financialParameterType);
        return this;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public FinancialParameter country(Country country) {
        this.setCountry(country);
        return this;
    }

    public Users getAdministrator() {
        return this.administrator;
    }

    public void setAdministrator(Users users) {
        this.administrator = users;
    }

    public FinancialParameter administrator(Users users) {
        this.setAdministrator(users);
        return this;
    }

    public Set<UserRole> getRoleAuthorizeds() {
        return this.roleAuthorizeds;
    }

    public void setRoleAuthorizeds(Set<UserRole> userRoles) {
        if (this.roleAuthorizeds != null) {
            this.roleAuthorizeds.forEach(i -> i.removeFinancialParameter(this));
        }
        if (userRoles != null) {
            userRoles.forEach(i -> i.addFinancialParameter(this));
        }
        this.roleAuthorizeds = userRoles;
    }

    public FinancialParameter roleAuthorizeds(Set<UserRole> userRoles) {
        this.setRoleAuthorizeds(userRoles);
        return this;
    }

    public FinancialParameter addRoleAuthorized(UserRole userRole) {
        this.roleAuthorizeds.add(userRole);
        userRole.getFinancialParameters().add(this);
        return this;
    }

    public FinancialParameter removeRoleAuthorized(UserRole userRole) {
        this.roleAuthorizeds.remove(userRole);
        userRole.getFinancialParameters().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinancialParameter)) {
            return false;
        }
        return getId() != null && getId().equals(((FinancialParameter) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinancialParameter{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", value=" + getValue() +
            ", active='" + getActive() + "'" +
            ", mandatory='" + getMandatory() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
