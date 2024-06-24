package com.trycore.quotizo.repository;

import com.trycore.quotizo.domain.FinancialParameterType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FinancialParameterType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinancialParameterTypeRepository
    extends JpaRepository<FinancialParameterType, Long>, JpaSpecificationExecutor<FinancialParameterType> {}
