package com.trycore.quotizo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A DroolsRuleFile.
 */
@Entity
@Table(name = "drools_rule_file")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DroolsRuleFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Lob
    @Column(name = "file_content", nullable = false)
    private String fileContent;

    @Column(name = "description")
    private String description;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "droolsRuleFile")
    @JsonIgnoreProperties(value = { "droolsRuleFile" }, allowSetters = true)
    private Set<RuleAssignment> ruleAssignments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DroolsRuleFile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public DroolsRuleFile fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContent() {
        return this.fileContent;
    }

    public DroolsRuleFile fileContent(String fileContent) {
        this.setFileContent(fileContent);
        return this;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getDescription() {
        return this.description;
    }

    public DroolsRuleFile description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return this.active;
    }

    public DroolsRuleFile active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public DroolsRuleFile createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public DroolsRuleFile createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public DroolsRuleFile lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public DroolsRuleFile lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<RuleAssignment> getRuleAssignments() {
        return this.ruleAssignments;
    }

    public void setRuleAssignments(Set<RuleAssignment> ruleAssignments) {
        if (this.ruleAssignments != null) {
            this.ruleAssignments.forEach(i -> i.setDroolsRuleFile(null));
        }
        if (ruleAssignments != null) {
            ruleAssignments.forEach(i -> i.setDroolsRuleFile(this));
        }
        this.ruleAssignments = ruleAssignments;
    }

    public DroolsRuleFile ruleAssignments(Set<RuleAssignment> ruleAssignments) {
        this.setRuleAssignments(ruleAssignments);
        return this;
    }

    public DroolsRuleFile addRuleAssignment(RuleAssignment ruleAssignment) {
        this.ruleAssignments.add(ruleAssignment);
        ruleAssignment.setDroolsRuleFile(this);
        return this;
    }

    public DroolsRuleFile removeRuleAssignment(RuleAssignment ruleAssignment) {
        this.ruleAssignments.remove(ruleAssignment);
        ruleAssignment.setDroolsRuleFile(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DroolsRuleFile)) {
            return false;
        }
        return getId() != null && getId().equals(((DroolsRuleFile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DroolsRuleFile{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", fileContent='" + getFileContent() + "'" +
            ", description='" + getDescription() + "'" +
            ", active='" + getActive() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
