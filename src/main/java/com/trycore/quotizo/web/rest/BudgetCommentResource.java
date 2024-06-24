package com.trycore.quotizo.web.rest;

import com.trycore.quotizo.repository.BudgetCommentRepository;
import com.trycore.quotizo.service.BudgetCommentQueryService;
import com.trycore.quotizo.service.BudgetCommentService;
import com.trycore.quotizo.service.criteria.BudgetCommentCriteria;
import com.trycore.quotizo.service.dto.BudgetCommentDTO;
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
 * REST controller for managing {@link com.trycore.quotizo.domain.BudgetComment}.
 */
@RestController
@RequestMapping("/api/budget-comments")
public class BudgetCommentResource {

    private final Logger log = LoggerFactory.getLogger(BudgetCommentResource.class);

    private static final String ENTITY_NAME = "budgetComment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BudgetCommentService budgetCommentService;

    private final BudgetCommentRepository budgetCommentRepository;

    private final BudgetCommentQueryService budgetCommentQueryService;

    public BudgetCommentResource(
        BudgetCommentService budgetCommentService,
        BudgetCommentRepository budgetCommentRepository,
        BudgetCommentQueryService budgetCommentQueryService
    ) {
        this.budgetCommentService = budgetCommentService;
        this.budgetCommentRepository = budgetCommentRepository;
        this.budgetCommentQueryService = budgetCommentQueryService;
    }

    /**
     * {@code POST  /budget-comments} : Create a new budgetComment.
     *
     * @param budgetCommentDTO the budgetCommentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new budgetCommentDTO, or with status {@code 400 (Bad Request)} if the budgetComment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BudgetCommentDTO> createBudgetComment(@Valid @RequestBody BudgetCommentDTO budgetCommentDTO)
        throws URISyntaxException {
        log.debug("REST request to save BudgetComment : {}", budgetCommentDTO);
        if (budgetCommentDTO.getId() != null) {
            throw new BadRequestAlertException("A new budgetComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        budgetCommentDTO = budgetCommentService.save(budgetCommentDTO);
        return ResponseEntity.created(new URI("/api/budget-comments/" + budgetCommentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, budgetCommentDTO.getId().toString()))
            .body(budgetCommentDTO);
    }

    /**
     * {@code PUT  /budget-comments/:id} : Updates an existing budgetComment.
     *
     * @param id the id of the budgetCommentDTO to save.
     * @param budgetCommentDTO the budgetCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated budgetCommentDTO,
     * or with status {@code 400 (Bad Request)} if the budgetCommentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the budgetCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BudgetCommentDTO> updateBudgetComment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BudgetCommentDTO budgetCommentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BudgetComment : {}, {}", id, budgetCommentDTO);
        if (budgetCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, budgetCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!budgetCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        budgetCommentDTO = budgetCommentService.update(budgetCommentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, budgetCommentDTO.getId().toString()))
            .body(budgetCommentDTO);
    }

    /**
     * {@code PATCH  /budget-comments/:id} : Partial updates given fields of an existing budgetComment, field will ignore if it is null
     *
     * @param id the id of the budgetCommentDTO to save.
     * @param budgetCommentDTO the budgetCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated budgetCommentDTO,
     * or with status {@code 400 (Bad Request)} if the budgetCommentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the budgetCommentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the budgetCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BudgetCommentDTO> partialUpdateBudgetComment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BudgetCommentDTO budgetCommentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BudgetComment partially : {}, {}", id, budgetCommentDTO);
        if (budgetCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, budgetCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!budgetCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BudgetCommentDTO> result = budgetCommentService.partialUpdate(budgetCommentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, budgetCommentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /budget-comments} : get all the budgetComments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of budgetComments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BudgetCommentDTO>> getAllBudgetComments(
        BudgetCommentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get BudgetComments by criteria: {}", criteria);

        Page<BudgetCommentDTO> page = budgetCommentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /budget-comments/count} : count all the budgetComments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countBudgetComments(BudgetCommentCriteria criteria) {
        log.debug("REST request to count BudgetComments by criteria: {}", criteria);
        return ResponseEntity.ok().body(budgetCommentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /budget-comments/:id} : get the "id" budgetComment.
     *
     * @param id the id of the budgetCommentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the budgetCommentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BudgetCommentDTO> getBudgetComment(@PathVariable("id") Long id) {
        log.debug("REST request to get BudgetComment : {}", id);
        Optional<BudgetCommentDTO> budgetCommentDTO = budgetCommentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(budgetCommentDTO);
    }

    /**
     * {@code DELETE  /budget-comments/:id} : delete the "id" budgetComment.
     *
     * @param id the id of the budgetCommentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudgetComment(@PathVariable("id") Long id) {
        log.debug("REST request to delete BudgetComment : {}", id);
        budgetCommentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
