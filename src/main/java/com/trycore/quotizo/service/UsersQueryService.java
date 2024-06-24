package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.*; // for static metamodels
import com.trycore.quotizo.domain.Users;
import com.trycore.quotizo.repository.UsersRepository;
import com.trycore.quotizo.service.criteria.UsersCriteria;
import com.trycore.quotizo.service.dto.UsersDTO;
import com.trycore.quotizo.service.mapper.UsersMapper;
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
 * Service for executing complex queries for {@link Users} entities in the database.
 * The main input is a {@link UsersCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link UsersDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UsersQueryService extends QueryService<Users> {

    private final Logger log = LoggerFactory.getLogger(UsersQueryService.class);

    private final UsersRepository usersRepository;

    private final UsersMapper usersMapper;

    public UsersQueryService(UsersRepository usersRepository, UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
    }

    /**
     * Return a {@link Page} of {@link UsersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UsersDTO> findByCriteria(UsersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Users> specification = createSpecification(criteria);
        return usersRepository.fetchBagRelationships(usersRepository.findAll(specification, page)).map(usersMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UsersCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Users> specification = createSpecification(criteria);
        return usersRepository.count(specification);
    }

    /**
     * Function to convert {@link UsersCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Users> createSpecification(UsersCriteria criteria) {
        Specification<Users> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Users_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Users_.name));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Users_.email));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), Users_.password));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Users_.active));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Users_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Users_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Users_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Users_.lastModifiedDate));
            }
            if (criteria.getBudgetId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getBudgetId(), root -> root.join(Users_.budgets, JoinType.LEFT).get(Budget_.id))
                );
            }
            if (criteria.getFinancialParameterId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getFinancialParameterId(),
                        root -> root.join(Users_.financialParameters, JoinType.LEFT).get(FinancialParameter_.id)
                    )
                );
            }
            if (criteria.getUserRoleId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserRoleId(), root -> root.join(Users_.userRoles, JoinType.LEFT).get(UserRole_.id))
                );
            }
            if (criteria.getBudgetAuthorizedId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getBudgetAuthorizedId(),
                        root -> root.join(Users_.budgetAuthorizeds, JoinType.LEFT).get(Budget_.id)
                    )
                );
            }
            if (criteria.getAssignedToId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAssignedToId(), root -> root.join(Users_.assignedTos, JoinType.LEFT).get(Budget_.id))
                );
            }
            if (criteria.getApprovedById() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApprovedById(), root -> root.join(Users_.approvedBies, JoinType.LEFT).get(Budget_.id))
                );
            }
        }
        return specification;
    }
}
