package com.trycore.quotizo.repository;

import com.trycore.quotizo.domain.Users;
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
public class UsersRepositoryWithBagRelationshipsImpl implements UsersRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String USERS_PARAMETER = "users";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Users> fetchBagRelationships(Optional<Users> users) {
        return users.map(this::fetchUserRoles).map(this::fetchBudgetAuthorizeds);
    }

    @Override
    public Page<Users> fetchBagRelationships(Page<Users> users) {
        return new PageImpl<>(fetchBagRelationships(users.getContent()), users.getPageable(), users.getTotalElements());
    }

    @Override
    public List<Users> fetchBagRelationships(List<Users> users) {
        return Optional.of(users).map(this::fetchUserRoles).map(this::fetchBudgetAuthorizeds).orElse(Collections.emptyList());
    }

    Users fetchUserRoles(Users result) {
        return entityManager
            .createQuery("select users from Users users left join fetch users.userRoles where users.id = :id", Users.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Users> fetchUserRoles(List<Users> users) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, users.size()).forEach(index -> order.put(users.get(index).getId(), index));
        List<Users> result = entityManager
            .createQuery("select users from Users users left join fetch users.userRoles where users in :users", Users.class)
            .setParameter(USERS_PARAMETER, users)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Users fetchBudgetAuthorizeds(Users result) {
        return entityManager
            .createQuery("select users from Users users left join fetch users.budgetAuthorizeds where users.id = :id", Users.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Users> fetchBudgetAuthorizeds(List<Users> users) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, users.size()).forEach(index -> order.put(users.get(index).getId(), index));
        List<Users> result = entityManager
            .createQuery("select users from Users users left join fetch users.budgetAuthorizeds where users in :users", Users.class)
            .setParameter(USERS_PARAMETER, users)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
