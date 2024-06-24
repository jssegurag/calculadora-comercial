package com.trycore.quotizo.web.rest;

import com.trycore.quotizo.repository.FinancialParameterRepository;
import com.trycore.quotizo.service.FinancialParameterQueryService;
import com.trycore.quotizo.service.FinancialParameterService;
import com.trycore.quotizo.service.criteria.FinancialParameterCriteria;
import com.trycore.quotizo.service.dto.FinancialParameterDTO;
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
 * REST controller for managing {@link com.trycore.quotizo.domain.FinancialParameter}.
 */
@RestController
@RequestMapping("/api/financial-parameters")
public class FinancialParameterResource {

    private final Logger log = LoggerFactory.getLogger(FinancialParameterResource.class);

    private static final String ENTITY_NAME = "financialParameter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FinancialParameterService financialParameterService;

    private final FinancialParameterRepository financialParameterRepository;

    private final FinancialParameterQueryService financialParameterQueryService;

    public FinancialParameterResource(
        FinancialParameterService financialParameterService,
        FinancialParameterRepository financialParameterRepository,
        FinancialParameterQueryService financialParameterQueryService
    ) {
        this.financialParameterService = financialParameterService;
        this.financialParameterRepository = financialParameterRepository;
        this.financialParameterQueryService = financialParameterQueryService;
    }

    /**
     * {@code POST  /financial-parameters} : Create a new financialParameter.
     *
     * @param financialParameterDTO the financialParameterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new financialParameterDTO, or with status {@code 400 (Bad Request)} if the financialParameter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FinancialParameterDTO> createFinancialParameter(@Valid @RequestBody FinancialParameterDTO financialParameterDTO)
        throws URISyntaxException {
        log.debug("REST request to save FinancialParameter : {}", financialParameterDTO);
        if (financialParameterDTO.getId() != null) {
            throw new BadRequestAlertException("A new financialParameter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        financialParameterDTO = financialParameterService.save(financialParameterDTO);
        return ResponseEntity.created(new URI("/api/financial-parameters/" + financialParameterDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, financialParameterDTO.getId().toString()))
            .body(financialParameterDTO);
    }

    /**
     * {@code PUT  /financial-parameters/:id} : Updates an existing financialParameter.
     *
     * @param id the id of the financialParameterDTO to save.
     * @param financialParameterDTO the financialParameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financialParameterDTO,
     * or with status {@code 400 (Bad Request)} if the financialParameterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the financialParameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FinancialParameterDTO> updateFinancialParameter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FinancialParameterDTO financialParameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FinancialParameter : {}, {}", id, financialParameterDTO);
        if (financialParameterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financialParameterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financialParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        financialParameterDTO = financialParameterService.update(financialParameterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, financialParameterDTO.getId().toString()))
            .body(financialParameterDTO);
    }

    /**
     * {@code PATCH  /financial-parameters/:id} : Partial updates given fields of an existing financialParameter, field will ignore if it is null
     *
     * @param id the id of the financialParameterDTO to save.
     * @param financialParameterDTO the financialParameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financialParameterDTO,
     * or with status {@code 400 (Bad Request)} if the financialParameterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the financialParameterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the financialParameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FinancialParameterDTO> partialUpdateFinancialParameter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FinancialParameterDTO financialParameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FinancialParameter partially : {}, {}", id, financialParameterDTO);
        if (financialParameterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financialParameterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financialParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FinancialParameterDTO> result = financialParameterService.partialUpdate(financialParameterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, financialParameterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /financial-parameters} : get all the financialParameters.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of financialParameters in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FinancialParameterDTO>> getAllFinancialParameters(
        FinancialParameterCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get FinancialParameters by criteria: {}", criteria);

        Page<FinancialParameterDTO> page = financialParameterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /financial-parameters/count} : count all the financialParameters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFinancialParameters(FinancialParameterCriteria criteria) {
        log.debug("REST request to count FinancialParameters by criteria: {}", criteria);
        return ResponseEntity.ok().body(financialParameterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /financial-parameters/:id} : get the "id" financialParameter.
     *
     * @param id the id of the financialParameterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the financialParameterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FinancialParameterDTO> getFinancialParameter(@PathVariable("id") Long id) {
        log.debug("REST request to get FinancialParameter : {}", id);
        Optional<FinancialParameterDTO> financialParameterDTO = financialParameterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(financialParameterDTO);
    }

    /**
     * {@code DELETE  /financial-parameters/:id} : delete the "id" financialParameter.
     *
     * @param id the id of the financialParameterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancialParameter(@PathVariable("id") Long id) {
        log.debug("REST request to delete FinancialParameter : {}", id);
        financialParameterService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
