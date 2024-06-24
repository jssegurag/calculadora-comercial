package com.trycore.quotizo.repository;

import com.trycore.quotizo.domain.BudgetComment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BudgetComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BudgetCommentRepository extends JpaRepository<BudgetComment, Long>, JpaSpecificationExecutor<BudgetComment> {}
