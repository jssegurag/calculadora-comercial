package com.trycore.quotizo.repository;

import com.trycore.quotizo.domain.ResourceAllocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ResourceAllocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceAllocationRepository
    extends JpaRepository<ResourceAllocation, Long>, JpaSpecificationExecutor<ResourceAllocation> {}
