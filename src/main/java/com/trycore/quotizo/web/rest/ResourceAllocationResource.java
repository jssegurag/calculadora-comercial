package com.trycore.quotizo.web.rest;

import com.trycore.quotizo.repository.ResourceAllocationRepository;
import com.trycore.quotizo.service.ResourceAllocationQueryService;
import com.trycore.quotizo.service.ResourceAllocationService;
import com.trycore.quotizo.service.criteria.ResourceAllocationCriteria;
import com.trycore.quotizo.service.dto.ResourceAllocationDTO;
import com.trycore.quotizo.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.trycore.quotizo.domain.ResourceAllocation}.
 */
@RestController
@RequestMapping("/api/resource-allocations")
public class ResourceAllocationResource {

    private final Logger log = LoggerFactory.getLogger(ResourceAllocationResource.class);

    private static final String ENTITY_NAME = "resourceAllocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourceAllocationService resourceAllocationService;

    private final ResourceAllocationRepository resourceAllocationRepository;

    private final ResourceAllocationQueryService resourceAllocationQueryService;

    public ResourceAllocationResource(
        ResourceAllocationService resourceAllocationService,
        ResourceAllocationRepository resourceAllocationRepository,
        ResourceAllocationQueryService resourceAllocationQueryService
    ) {
        this.resourceAllocationService = resourceAllocationService;
        this.resourceAllocationRepository = resourceAllocationRepository;
        this.resourceAllocationQueryService = resourceAllocationQueryService;
    }

    /**
     * {@code POST  /resource-allocations} : Create a new resourceAllocation.
     *
     * @param resourceAllocationDTO the resourceAllocationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceAllocationDTO, or with status {@code 400 (Bad Request)} if the resourceAllocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ResourceAllocationDTO> createResourceAllocation(@Valid @RequestBody ResourceAllocationDTO resourceAllocationDTO)
        throws URISyntaxException {
        log.debug("REST request to save ResourceAllocation : {}", resourceAllocationDTO);
        if (resourceAllocationDTO.getId() != null) {
            throw new BadRequestAlertException("A new resourceAllocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        resourceAllocationDTO = resourceAllocationService.save(resourceAllocationDTO);
        return ResponseEntity.created(new URI("/api/resource-allocations/" + resourceAllocationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, resourceAllocationDTO.getId().toString()))
            .body(resourceAllocationDTO);
    }

    /**
     * {@code PUT  /resource-allocations/:id} : Updates an existing resourceAllocation.
     *
     * @param id the id of the resourceAllocationDTO to save.
     * @param resourceAllocationDTO the resourceAllocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceAllocationDTO,
     * or with status {@code 400 (Bad Request)} if the resourceAllocationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceAllocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResourceAllocationDTO> updateResourceAllocation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ResourceAllocationDTO resourceAllocationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ResourceAllocation : {}, {}", id, resourceAllocationDTO);
        if (resourceAllocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceAllocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resourceAllocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        resourceAllocationDTO = resourceAllocationService.update(resourceAllocationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceAllocationDTO.getId().toString()))
            .body(resourceAllocationDTO);
    }

    /**
     * {@code PATCH  /resource-allocations/:id} : Partial updates given fields of an existing resourceAllocation, field will ignore if it is null
     *
     * @param id the id of the resourceAllocationDTO to save.
     * @param resourceAllocationDTO the resourceAllocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceAllocationDTO,
     * or with status {@code 400 (Bad Request)} if the resourceAllocationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the resourceAllocationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the resourceAllocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ResourceAllocationDTO> partialUpdateResourceAllocation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ResourceAllocationDTO resourceAllocationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ResourceAllocation partially : {}, {}", id, resourceAllocationDTO);
        if (resourceAllocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceAllocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resourceAllocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResourceAllocationDTO> result = resourceAllocationService.partialUpdate(resourceAllocationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceAllocationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /resource-allocations} : get all the resourceAllocations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceAllocations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ResourceAllocationDTO>> getAllResourceAllocations(
        ResourceAllocationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ResourceAllocations by criteria: {}", criteria);

        Page<ResourceAllocationDTO> page = resourceAllocationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /resource-allocations/count} : count all the resourceAllocations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countResourceAllocations(ResourceAllocationCriteria criteria) {
        log.debug("REST request to count ResourceAllocations by criteria: {}", criteria);
        return ResponseEntity.ok().body(resourceAllocationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /resource-allocations/:id} : get the "id" resourceAllocation.
     *
     * @param id the id of the resourceAllocationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceAllocationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResourceAllocationDTO> getResourceAllocation(@PathVariable("id") Long id) {
        log.debug("REST request to get ResourceAllocation : {}", id);
        Optional<ResourceAllocationDTO> resourceAllocationDTO = resourceAllocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resourceAllocationDTO);
    }

    /**
     * {@code DELETE  /resource-allocations/:id} : delete the "id" resourceAllocation.
     *
     * @param id the id of the resourceAllocationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResourceAllocation(@PathVariable("id") Long id) {
        log.debug("REST request to delete ResourceAllocation : {}", id);
        resourceAllocationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
