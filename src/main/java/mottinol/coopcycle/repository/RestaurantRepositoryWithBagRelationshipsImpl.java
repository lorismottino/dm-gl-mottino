package mottinol.coopcycle.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mottinol.coopcycle.domain.Restaurant;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class RestaurantRepositoryWithBagRelationshipsImpl implements RestaurantRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Restaurant> fetchBagRelationships(Optional<Restaurant> restaurant) {
        return restaurant.map(this::fetchCooperatives);
    }

    @Override
    public Page<Restaurant> fetchBagRelationships(Page<Restaurant> restaurants) {
        return new PageImpl<>(fetchBagRelationships(restaurants.getContent()), restaurants.getPageable(), restaurants.getTotalElements());
    }

    @Override
    public List<Restaurant> fetchBagRelationships(List<Restaurant> restaurants) {
        return Optional.of(restaurants).map(this::fetchCooperatives).orElse(Collections.emptyList());
    }

    Restaurant fetchCooperatives(Restaurant result) {
        return entityManager
            .createQuery(
                "select restaurant from Restaurant restaurant left join fetch restaurant.cooperatives where restaurant is :restaurant",
                Restaurant.class
            )
            .setParameter("restaurant", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Restaurant> fetchCooperatives(List<Restaurant> restaurants) {
        return entityManager
            .createQuery(
                "select distinct restaurant from Restaurant restaurant left join fetch restaurant.cooperatives where restaurant in :restaurants",
                Restaurant.class
            )
            .setParameter("restaurants", restaurants)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
