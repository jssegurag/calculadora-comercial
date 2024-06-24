package com.trycore.quotizo.web.rest;

import com.trycore.quotizo.repository.BudgetRepository;
import com.trycore.quotizo.service.BudgetQueryService;
import com.trycore.quotizo.service.BudgetService;
import com.trycore.quotizo.service.criteria.BudgetCriteria;
import com.trycore.quotizo.service.dto.BudgetDTO;
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
 * REST controller for managing {@link com.trycore.quotizo.domain.Budget}.
 */
@RestController
@RequestMapping("/api/budgets")
public class BudgetResource {

    private final Logger log = LoggerFactory.getLogger(BudgetResource.class);

    private static final String ENTITY_NAME = "budget";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BudgetService budgetService;

    private final BudgetRepository budgetRepository;

    private final BudgetQueryService budgetQueryService;

    public BudgetResource(BudgetService budgetService, BudgetRepository budgetRepository, BudgetQueryService budgetQueryService) {
        this.budgetService = budgetService;
        this.budgetRepository = budgetRepository;
        this.budgetQueryService = budgetQueryService;
    }

    /**
     * {@code POST  /budgets} : Create a new budget.
     *
     * @param budgetDTO the budgetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new budgetDTO, or with status {@code 400 (Bad Request)} if the budget has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BudgetDTO> createBudget(@Valid @RequestBody BudgetDTO budgetDTO) throws URISyntaxException {
        log.debug("REST request to save Budget : {}", budgetDTO);
        if (budgetDTO.getId() != null) {
            throw new BadRequestAlertException("A new budget cannot already have an ID", ENTITY_NAME, "idexists");
        }
        budgetDTO = budgetService.save(budgetDTO);
        return ResponseEntity.created(new URI("/api/budgets/" + budgetDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, budgetDTO.getId().toString()))
            .body(budgetDTO);
    }

    /**
     * {@code PUT  /budgets/:id} : Updates an existing budget.
     *
     * @param id the id of the budgetDTO to save.
     * @param budgetDTO the budgetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated budgetDTO,
     * or with status {@code 400 (Bad Request)} if the budgetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the budgetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BudgetDTO> updateBudget(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BudgetDTO budgetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Budget : {}, {}", id, budgetDTO);
        if (budgetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, budgetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!budgetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        budgetDTO = budgetService.update(budgetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, budgetDTO.getId().toString()))
            .body(budgetDTO);
    }

    /**
     * {@code PATCH  /budgets/:id} : Partial updates given fields of an existing budget, field will ignore if it is null
     *
     * @param id the id of the budgetDTO to save.
     * @param budgetDTO the budgetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated budgetDTO,
     * or with status {@code 400 (Bad Request)} if the budgetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the budgetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the budgetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BudgetDTO> partialUpdateBudget(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BudgetDTO budgetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Budget partially : {}, {}", id, budgetDTO);
        if (budgetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, budgetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!budgetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BudgetDTO> result = budgetService.partialUpdate(budgetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, budgetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /budgets} : get all the budgets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of budgets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BudgetDTO>> getAllBudgets(
        BudgetCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Budgets by criteria: {}", criteria);

        Page<BudgetDTO> page = budgetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /budgets/count} : count all the budgets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countBudgets(BudgetCriteria criteria) {
        log.debug("REST request to count Budgets by criteria: {}", criteria);
        return ResponseEntity.ok().body(budgetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /budgets/:id} : get the "id" budget.
     *
     * @param id the id of the budgetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the budgetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BudgetDTO> getBudget(@PathVariable("id") Long id) {
        log.debug("REST request to get Budget : {}", id);
        Optional<BudgetDTO> budgetDTO = budgetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(budgetDTO);
    }

    /**
     * {@code DELETE  /budgets/:id} : delete the "id" budget.
     *
     * @param id the id of the budgetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable("id") Long id) {
        log.debug("REST request to delete Budget : {}", id);
        budgetService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
