package com.trycore.quotizo.repository;

import com.trycore.quotizo.domain.RuleAssignment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RuleAssignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RuleAssignmentRepository extends JpaRepository<RuleAssignment, Long>, JpaSpecificationExecutor<RuleAssignment> {}
