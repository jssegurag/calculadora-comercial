package com.trycore.quotizo.repository;

import com.trycore.quotizo.domain.BudgetTemplate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BudgetTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BudgetTemplateRepository extends JpaRepository<BudgetTemplate, Long>, JpaSpecificationExecutor<BudgetTemplate> {}
