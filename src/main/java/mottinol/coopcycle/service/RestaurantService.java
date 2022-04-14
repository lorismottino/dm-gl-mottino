package mottinol.coopcycle.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import mottinol.coopcycle.domain.Restaurant;
import mottinol.coopcycle.repository.RestaurantRepository;
import mottinol.coopcycle.repository.search.RestaurantSearchRepository;
import mottinol.coopcycle.service.dto.RestaurantDTO;
import mottinol.coopcycle.service.mapper.RestaurantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Restaurant}.
 */
@Service
@Transactional
public class RestaurantService {

    private final Logger log = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository restaurantRepository;

    private final RestaurantMapper restaurantMapper;

    private final RestaurantSearchRepository restaurantSearchRepository;

    public RestaurantService(
        RestaurantRepository restaurantRepository,
        RestaurantMapper restaurantMapper,
        RestaurantSearchRepository restaurantSearchRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
        this.restaurantSearchRepository = restaurantSearchRepository;
    }

    /**
     * Save a restaurant.
     *
     * @param restaurantDTO the entity to save.
     * @return the persisted entity.
     */
    public RestaurantDTO save(RestaurantDTO restaurantDTO) {
        log.debug("Request to save Restaurant : {}", restaurantDTO);
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDTO);
        restaurant = restaurantRepository.save(restaurant);
        RestaurantDTO result = restaurantMapper.toDto(restaurant);
        restaurantSearchRepository.save(restaurant);
        return result;
    }

    /**
     * Update a restaurant.
     *
     * @param restaurantDTO the entity to save.
     * @return the persisted entity.
     */
    public RestaurantDTO update(RestaurantDTO restaurantDTO) {
        log.debug("Request to save Restaurant : {}", restaurantDTO);
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDTO);
        restaurant.setIsPersisted();
        restaurant = restaurantRepository.save(restaurant);
        RestaurantDTO result = restaurantMapper.toDto(restaurant);
        restaurantSearchRepository.save(restaurant);
        return result;
    }

    /**
     * Partially update a restaurant.
     *
     * @param restaurantDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RestaurantDTO> partialUpdate(RestaurantDTO restaurantDTO) {
        log.debug("Request to partially update Restaurant : {}", restaurantDTO);

        return restaurantRepository
            .findById(restaurantDTO.getId())
            .map(existingRestaurant -> {
                restaurantMapper.partialUpdate(existingRestaurant, restaurantDTO);

                return existingRestaurant;
            })
            .map(restaurantRepository::save)
            .map(savedRestaurant -> {
                restaurantSearchRepository.save(savedRestaurant);

                return savedRestaurant;
            })
            .map(restaurantMapper::toDto);
    }

    /**
     * Get all the restaurants.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RestaurantDTO> findAll() {
        log.debug("Request to get all Restaurants");
        return restaurantRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(restaurantMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the restaurants with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<RestaurantDTO> findAllWithEagerRelationships(Pageable pageable) {
        return restaurantRepository.findAllWithEagerRelationships(pageable).map(restaurantMapper::toDto);
    }

    /**
     * Get one restaurant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RestaurantDTO> findOne(String id) {
        log.debug("Request to get Restaurant : {}", id);
        return restaurantRepository.findOneWithEagerRelationships(id).map(restaurantMapper::toDto);
    }

    /**
     * Delete the restaurant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Restaurant : {}", id);
        restaurantRepository.deleteById(id);
        restaurantSearchRepository.deleteById(id);
    }

    /**
     * Search for the restaurant corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RestaurantDTO> search(String query) {
        log.debug("Request to search Restaurants for query {}", query);
        return StreamSupport
            .stream(restaurantSearchRepository.search(query).spliterator(), false)
            .map(restaurantMapper::toDto)
            .collect(Collectors.toList());
    }
}
