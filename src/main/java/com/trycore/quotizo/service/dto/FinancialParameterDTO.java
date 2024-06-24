package com.trycore.quotizo.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.trycore.quotizo.domain.FinancialParameter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FinancialParameterDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal value;

    @NotNull
    private Boolean active;

    @NotNull
    private Boolean mandatory;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private FinancialParameterTypeDTO financialParameterType;

    private CountryDTO country;

    private UsersDTO administrator;

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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
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

    public FinancialParameterTypeDTO getFinancialParameterType() {
        return financialParameterType;
    }

    public void setFinancialParameterType(FinancialParameterTypeDTO financialParameterType) {
        this.financialParameterType = financialParameterType;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    public UsersDTO getAdministrator() {
        return administrator;
    }

    public void setAdministrator(UsersDTO administrator) {
        this.administrator = administrator;
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
        if (!(o instanceof FinancialParameterDTO)) {
            return false;
        }

        FinancialParameterDTO financialParameterDTO = (FinancialParameterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, financialParameterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinancialParameterDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", value=" + getValue() +
            ", active='" + getActive() + "'" +
            ", mandatory='" + getMandatory() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", financialParameterType=" + getFinancialParameterType() +
            ", country=" + getCountry() +
            ", administrator=" + getAdministrator() +
            ", roleAuthorizeds=" + getRoleAuthorizeds() +
            "}";
    }
}
