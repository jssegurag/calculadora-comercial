package com.trycore.quotizo.repository;

import com.trycore.quotizo.domain.UserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class UserRoleRepositoryWithBagRelationshipsImpl implements UserRoleRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String USERROLES_PARAMETER = "userRoles";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserRole> fetchBagRelationships(Optional<UserRole> userRole) {
        return userRole.map(this::fetchPermissions).map(this::fetchBudgets).map(this::fetchFinancialParameters);
    }

    @Override
    public Page<UserRole> fetchBagRelationships(Page<UserRole> userRoles) {
        return new PageImpl<>(fetchBagRelationships(userRoles.getContent()), userRoles.getPageable(), userRoles.getTotalElements());
    }

    @Override
    public List<UserRole> fetchBagRelationships(List<UserRole> userRoles) {
        return Optional.of(userRoles)
            .map(this::fetchPermissions)
            .map(this::fetchBudgets)
            .map(this::fetchFinancialParameters)
            .orElse(Collections.emptyList());
    }

    UserRole fetchPermissions(UserRole result) {
        return entityManager
            .createQuery(
                "select userRole from UserRole userRole left join fetch userRole.permissions where userRole.id = :id",
                UserRole.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<UserRole> fetchPermissions(List<UserRole> userRoles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, userRoles.size()).forEach(index -> order.put(userRoles.get(index).getId(), index));
        List<UserRole> result = entityManager
            .createQuery(
                "select userRole from UserRole userRole left join fetch userRole.permissions where userRole in :userRoles",
                UserRole.class
            )
            .setParameter(USERROLES_PARAMETER, userRoles)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    UserRole fetchBudgets(UserRole result) {
        return entityManager
            .createQuery("select userRole from UserRole userRole left join fetch userRole.budgets where userRole.id = :id", UserRole.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<UserRole> fetchBudgets(List<UserRole> userRoles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, userRoles.size()).forEach(index -> order.put(userRoles.get(index).getId(), index));
        List<UserRole> result = entityManager
            .createQuery(
                "select userRole from UserRole userRole left join fetch userRole.budgets where userRole in :userRoles",
                UserRole.class
            )
            .setParameter(USERROLES_PARAMETER, userRoles)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    UserRole fetchFinancialParameters(UserRole result) {
        return entityManager
            .createQuery(
                "select userRole from UserRole userRole left join fetch userRole.financialParameters where userRole.id = :id",
                UserRole.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<UserRole> fetchFinancialParameters(List<UserRole> userRoles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, userRoles.size()).forEach(index -> order.put(userRoles.get(index).getId(), index));
        List<UserRole> result = entityManager
            .createQuery(
                "select userRole from UserRole userRole left join fetch userRole.financialParameters where userRole in :userRoles",
                UserRole.class
            )
            .setParameter(USERROLES_PARAMETER, userRoles)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
