package mottinol.coopcycle.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import mottinol.coopcycle.domain.Courier;
import mottinol.coopcycle.repository.CourierRepository;
import mottinol.coopcycle.repository.search.CourierSearchRepository;
import mottinol.coopcycle.service.dto.CourierDTO;
import mottinol.coopcycle.service.mapper.CourierMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Courier}.
 */
@Service
@Transactional
public class CourierService {

    private final Logger log = LoggerFactory.getLogger(CourierService.class);

    private final CourierRepository courierRepository;

    private final CourierMapper courierMapper;

    private final CourierSearchRepository courierSearchRepository;

    public CourierService(
        CourierRepository courierRepository,
        CourierMapper courierMapper,
        CourierSearchRepository courierSearchRepository
    ) {
        this.courierRepository = courierRepository;
        this.courierMapper = courierMapper;
        this.courierSearchRepository = courierSearchRepository;
    }

    /**
     * Save a courier.
     *
     * @param courierDTO the entity to save.
     * @return the persisted entity.
     */
    public CourierDTO save(CourierDTO courierDTO) {
        log.debug("Request to save Courier : {}", courierDTO);
        Courier courier = courierMapper.toEntity(courierDTO);
        courier = courierRepository.save(courier);
        CourierDTO result = courierMapper.toDto(courier);
        courierSearchRepository.save(courier);
        return result;
    }

    /**
     * Update a courier.
     *
     * @param courierDTO the entity to save.
     * @return the persisted entity.
     */
    public CourierDTO update(CourierDTO courierDTO) {
        log.debug("Request to save Courier : {}", courierDTO);
        Courier courier = courierMapper.toEntity(courierDTO);
        courier.setIsPersisted();
        courier = courierRepository.save(courier);
        CourierDTO result = courierMapper.toDto(courier);
        courierSearchRepository.save(courier);
        return result;
    }

    /**
     * Partially update a courier.
     *
     * @param courierDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CourierDTO> partialUpdate(CourierDTO courierDTO) {
        log.debug("Request to partially update Courier : {}", courierDTO);

        return courierRepository
            .findById(courierDTO.getId())
            .map(existingCourier -> {
                courierMapper.partialUpdate(existingCourier, courierDTO);

                return existingCourier;
            })
            .map(courierRepository::save)
            .map(savedCourier -> {
                courierSearchRepository.save(savedCourier);

                return savedCourier;
            })
            .map(courierMapper::toDto);
    }

    /**
     * Get all the couriers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CourierDTO> findAll() {
        log.debug("Request to get all Couriers");
        return courierRepository.findAll().stream().map(courierMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one courier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CourierDTO> findOne(String id) {
        log.debug("Request to get Courier : {}", id);
        return courierRepository.findById(id).map(courierMapper::toDto);
    }

    /**
     * Delete the courier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Courier : {}", id);
        courierRepository.deleteById(id);
        courierSearchRepository.deleteById(id);
    }

    /**
     * Search for the courier corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CourierDTO> search(String query) {
        log.debug("Request to search Couriers for query {}", query);
        return StreamSupport
            .stream(courierSearchRepository.search(query).spliterator(), false)
            .map(courierMapper::toDto)
            .collect(Collectors.toList());
    }
}
