package com.trycore.quotizo.repository;

import com.trycore.quotizo.domain.FinancialParameter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FinancialParameter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinancialParameterRepository
    extends JpaRepository<FinancialParameter, Long>, JpaSpecificationExecutor<FinancialParameter> {}
