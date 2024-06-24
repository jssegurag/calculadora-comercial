package com.trycore.quotizo.web.rest;

import com.trycore.quotizo.repository.RuleAssignmentRepository;
import com.trycore.quotizo.service.RuleAssignmentQueryService;
import com.trycore.quotizo.service.RuleAssignmentService;
import com.trycore.quotizo.service.criteria.RuleAssignmentCriteria;
import com.trycore.quotizo.service.dto.RuleAssignmentDTO;
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
 * REST controller for managing {@link com.trycore.quotizo.domain.RuleAssignment}.
 */
@RestController
@RequestMapping("/api/rule-assignments")
public class RuleAssignmentResource {

    private final Logger log = LoggerFactory.getLogger(RuleAssignmentResource.class);

    private static final String ENTITY_NAME = "ruleAssignment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RuleAssignmentService ruleAssignmentService;

    private final RuleAssignmentRepository ruleAssignmentRepository;

    private final RuleAssignmentQueryService ruleAssignmentQueryService;

    public RuleAssignmentResource(
        RuleAssignmentService ruleAssignmentService,
        RuleAssignmentRepository ruleAssignmentRepository,
        RuleAssignmentQueryService ruleAssignmentQueryService
    ) {
        this.ruleAssignmentService = ruleAssignmentService;
        this.ruleAssignmentRepository = ruleAssignmentRepository;
        this.ruleAssignmentQueryService = ruleAssignmentQueryService;
    }

    /**
     * {@code POST  /rule-assignments} : Create a new ruleAssignment.
     *
     * @param ruleAssignmentDTO the ruleAssignmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ruleAssignmentDTO, or with status {@code 400 (Bad Request)} if the ruleAssignment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RuleAssignmentDTO> createRuleAssignment(@Valid @RequestBody RuleAssignmentDTO ruleAssignmentDTO)
        throws URISyntaxException {
        log.debug("REST request to save RuleAssignment : {}", ruleAssignmentDTO);
        if (ruleAssignmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new ruleAssignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ruleAssignmentDTO = ruleAssignmentService.save(ruleAssignmentDTO);
        return ResponseEntity.created(new URI("/api/rule-assignments/" + ruleAssignmentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ruleAssignmentDTO.getId().toString()))
            .body(ruleAssignmentDTO);
    }

    /**
     * {@code PUT  /rule-assignments/:id} : Updates an existing ruleAssignment.
     *
     * @param id the id of the ruleAssignmentDTO to save.
     * @param ruleAssignmentDTO the ruleAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ruleAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the ruleAssignmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ruleAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RuleAssignmentDTO> updateRuleAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RuleAssignmentDTO ruleAssignmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RuleAssignment : {}, {}", id, ruleAssignmentDTO);
        if (ruleAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ruleAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ruleAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ruleAssignmentDTO = ruleAssignmentService.update(ruleAssignmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ruleAssignmentDTO.getId().toString()))
            .body(ruleAssignmentDTO);
    }

    /**
     * {@code PATCH  /rule-assignments/:id} : Partial updates given fields of an existing ruleAssignment, field will ignore if it is null
     *
     * @param id the id of the ruleAssignmentDTO to save.
     * @param ruleAssignmentDTO the ruleAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ruleAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the ruleAssignmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ruleAssignmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ruleAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RuleAssignmentDTO> partialUpdateRuleAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RuleAssignmentDTO ruleAssignmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RuleAssignment partially : {}, {}", id, ruleAssignmentDTO);
        if (ruleAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ruleAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ruleAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RuleAssignmentDTO> result = ruleAssignmentService.partialUpdate(ruleAssignmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ruleAssignmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rule-assignments} : get all the ruleAssignments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ruleAssignments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RuleAssignmentDTO>> getAllRuleAssignments(
        RuleAssignmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get RuleAssignments by criteria: {}", criteria);

        Page<RuleAssignmentDTO> page = ruleAssignmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rule-assignments/count} : count all the ruleAssignments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRuleAssignments(RuleAssignmentCriteria criteria) {
        log.debug("REST request to count RuleAssignments by criteria: {}", criteria);
        return ResponseEntity.ok().body(ruleAssignmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rule-assignments/:id} : get the "id" ruleAssignment.
     *
     * @param id the id of the ruleAssignmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ruleAssignmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RuleAssignmentDTO> getRuleAssignment(@PathVariable("id") Long id) {
        log.debug("REST request to get RuleAssignment : {}", id);
        Optional<RuleAssignmentDTO> ruleAssignmentDTO = ruleAssignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ruleAssignmentDTO);
    }

    /**
     * {@code DELETE  /rule-assignments/:id} : delete the "id" ruleAssignment.
     *
     * @param id the id of the ruleAssignmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRuleAssignment(@PathVariable("id") Long id) {
        log.debug("REST request to delete RuleAssignment : {}", id);
        ruleAssignmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
