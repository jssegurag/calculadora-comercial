package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.*; // for static metamodels
import com.trycore.quotizo.domain.UserRole;
import com.trycore.quotizo.repository.UserRoleRepository;
import com.trycore.quotizo.service.criteria.UserRoleCriteria;
import com.trycore.quotizo.service.dto.UserRoleDTO;
import com.trycore.quotizo.service.mapper.UserRoleMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link UserRole} entities in the database.
 * The main input is a {@link UserRoleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link UserRoleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserRoleQueryService extends QueryService<UserRole> {

    private final Logger log = LoggerFactory.getLogger(UserRoleQueryService.class);

    private final UserRoleRepository userRoleRepository;

    private final UserRoleMapper userRoleMapper;

    public UserRoleQueryService(UserRoleRepository userRoleRepository, UserRoleMapper userRoleMapper) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleMapper = userRoleMapper;
    }

    /**
     * Return a {@link Page} of {@link UserRoleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserRoleDTO> findByCriteria(UserRoleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserRole> specification = createSpecification(criteria);
        return userRoleRepository.fetchBagRelationships(userRoleRepository.findAll(specification, page)).map(userRoleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserRoleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserRole> specification = createSpecification(criteria);
        return userRoleRepository.count(specification);
    }

    /**
     * Function to convert {@link UserRoleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserRole> createSpecification(UserRoleCriteria criteria) {
        Specification<UserRole> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserRole_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), UserRole_.name));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), UserRole_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), UserRole_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), UserRole_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), UserRole_.lastModifiedDate));
            }
            if (criteria.getPermissionId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getPermissionId(),
                        root -> root.join(UserRole_.permissions, JoinType.LEFT).get(Permission_.id)
                    )
                );
            }
            if (criteria.getBudgetId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getBudgetId(), root -> root.join(UserRole_.budgets, JoinType.LEFT).get(Budget_.id))
                );
            }
            if (criteria.getFinancialParameterId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getFinancialParameterId(),
                        root -> root.join(UserRole_.financialParameters, JoinType.LEFT).get(FinancialParameter_.id)
                    )
                );
            }
            if (criteria.getUsersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUsersId(), root -> root.join(UserRole_.users, JoinType.LEFT).get(Users_.id))
                );
            }
        }
        return specification;
    }
}
