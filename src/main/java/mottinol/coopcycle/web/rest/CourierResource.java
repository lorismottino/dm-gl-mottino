package mottinol.coopcycle.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import mottinol.coopcycle.repository.CourierRepository;
import mottinol.coopcycle.service.CourierService;
import mottinol.coopcycle.service.dto.CourierDTO;
import mottinol.coopcycle.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link mottinol.coopcycle.domain.Courier}.
 */
@RestController
@RequestMapping("/api")
public class CourierResource {

    private final Logger log = LoggerFactory.getLogger(CourierResource.class);

    private static final String ENTITY_NAME = "courier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourierService courierService;

    private final CourierRepository courierRepository;

    public CourierResource(CourierService courierService, CourierRepository courierRepository) {
        this.courierService = courierService;
        this.courierRepository = courierRepository;
    }

    /**
     * {@code POST  /couriers} : Create a new courier.
     *
     * @param courierDTO the courierDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courierDTO, or with status {@code 400 (Bad Request)} if the courier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/couriers")
    public ResponseEntity<CourierDTO> createCourier(@Valid @RequestBody CourierDTO courierDTO) throws URISyntaxException {
        log.debug("REST request to save Courier : {}", courierDTO);
        if (courierDTO.getId() != null) {
            throw new BadRequestAlertException("A new courier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourierDTO result = courierService.save(courierDTO);
        return ResponseEntity
            .created(new URI("/api/couriers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /couriers/:id} : Updates an existing courier.
     *
     * @param id the id of the courierDTO to save.
     * @param courierDTO the courierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courierDTO,
     * or with status {@code 400 (Bad Request)} if the courierDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/couriers/{id}")
    public ResponseEntity<CourierDTO> updateCourier(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody CourierDTO courierDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Courier : {}, {}", id, courierDTO);
        if (courierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CourierDTO result = courierService.update(courierDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courierDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /couriers/:id} : Partial updates given fields of an existing courier, field will ignore if it is null
     *
     * @param id the id of the courierDTO to save.
     * @param courierDTO the courierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courierDTO,
     * or with status {@code 400 (Bad Request)} if the courierDTO is not valid,
     * or with status {@code 404 (Not Found)} if the courierDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the courierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/couriers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CourierDTO> partialUpdateCourier(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody CourierDTO courierDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Courier partially : {}, {}", id, courierDTO);
        if (courierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourierDTO> result = courierService.partialUpdate(courierDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courierDTO.getId())
        );
    }

    /**
     * {@code GET  /couriers} : get all the couriers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of couriers in body.
     */
    @GetMapping("/couriers")
    public List<CourierDTO> getAllCouriers() {
        log.debug("REST request to get all Couriers");
        return courierService.findAll();
    }

    /**
     * {@code GET  /couriers/:id} : get the "id" courier.
     *
     * @param id the id of the courierDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courierDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/couriers/{id}")
    public ResponseEntity<CourierDTO> getCourier(@PathVariable String id) {
        log.debug("REST request to get Courier : {}", id);
        Optional<CourierDTO> courierDTO = courierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courierDTO);
    }

    /**
     * {@code DELETE  /couriers/:id} : delete the "id" courier.
     *
     * @param id the id of the courierDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/couriers/{id}")
    public ResponseEntity<Void> deleteCourier(@PathVariable String id) {
        log.debug("REST request to delete Courier : {}", id);
        courierService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/couriers?query=:query} : search for the courier corresponding
     * to the query.
     *
     * @param query the query of the courier search.
     * @return the result of the search.
     */
    @GetMapping("/_search/couriers")
    public List<CourierDTO> searchCouriers(@RequestParam String query) {
        log.debug("REST request to search Couriers for query {}", query);
        return courierService.search(query);
    }
}
