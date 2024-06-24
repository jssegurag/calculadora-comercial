package com.trycore.quotizo.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.trycore.quotizo.domain.UserRole} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserRoleDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<PermissionDTO> permissions = new HashSet<>();

    private Set<BudgetDTO> budgets = new HashSet<>();

    private Set<FinancialParameterDTO> financialParameters = new HashSet<>();

    private Set<UsersDTO> users = new HashSet<>();

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

    public Set<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionDTO> permissions) {
        this.permissions = permissions;
    }

    public Set<BudgetDTO> getBudgets() {
        return budgets;
    }

    public void setBudgets(Set<BudgetDTO> budgets) {
        this.budgets = budgets;
    }

    public Set<FinancialParameterDTO> getFinancialParameters() {
        return financialParameters;
    }

    public void setFinancialParameters(Set<FinancialParameterDTO> financialParameters) {
        this.financialParameters = financialParameters;
    }

    public Set<UsersDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UsersDTO> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRoleDTO)) {
            return false;
        }

        UserRoleDTO userRoleDTO = (UserRoleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userRoleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserRoleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", permissions=" + getPermissions() +
            ", budgets=" + getBudgets() +
            ", financialParameters=" + getFinancialParameters() +
            ", users=" + getUsers() +
            "}";
    }
}
