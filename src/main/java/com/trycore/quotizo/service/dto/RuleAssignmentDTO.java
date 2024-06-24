package com.trycore.quotizo.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.trycore.quotizo.domain.RuleAssignment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RuleAssignmentDTO implements Serializable {

    private Long id;

    @NotNull
    private String entityName;

    @NotNull
    private Long entityId;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private DroolsRuleFileDTO droolsRuleFile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
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

    public DroolsRuleFileDTO getDroolsRuleFile() {
        return droolsRuleFile;
    }

    public void setDroolsRuleFile(DroolsRuleFileDTO droolsRuleFile) {
        this.droolsRuleFile = droolsRuleFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RuleAssignmentDTO)) {
            return false;
        }

        RuleAssignmentDTO ruleAssignmentDTO = (RuleAssignmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ruleAssignmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RuleAssignmentDTO{" +
            "id=" + getId() +
            ", entityName='" + getEntityName() + "'" +
            ", entityId=" + getEntityId() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", droolsRuleFile=" + getDroolsRuleFile() +
            "}";
    }
}
