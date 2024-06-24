package com.trycore.quotizo.repository;

import com.trycore.quotizo.domain.DroolsRuleFile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DroolsRuleFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DroolsRuleFileRepository extends JpaRepository<DroolsRuleFile, Long>, JpaSpecificationExecutor<DroolsRuleFile> {}
