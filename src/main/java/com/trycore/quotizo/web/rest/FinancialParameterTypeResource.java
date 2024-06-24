package com.trycore.quotizo.web.rest;

import com.trycore.quotizo.repository.FinancialParameterTypeRepository;
import com.trycore.quotizo.service.FinancialParameterTypeQueryService;
import com.trycore.quotizo.service.FinancialParameterTypeService;
import com.trycore.quotizo.service.criteria.FinancialParameterTypeCriteria;
import com.trycore.quotizo.service.dto.FinancialParameterTypeDTO;
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
 * REST controller for managing {@link com.trycore.quotizo.domain.FinancialParameterType}.
 */
@RestController
@RequestMapping("/api/financial-parameter-types")
public class FinancialParameterTypeResource {

    private final Logger log = LoggerFactory.getLogger(FinancialParameterTypeResource.class);

    private static final String ENTITY_NAME = "financialParameterType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FinancialParameterTypeService financialParameterTypeService;

    private final FinancialParameterTypeRepository financialParameterTypeRepository;

    private final FinancialParameterTypeQueryService financialParameterTypeQueryService;

    public FinancialParameterTypeResource(
        FinancialParameterTypeService financialParameterTypeService,
        FinancialParameterTypeRepository financialParameterTypeRepository,
        FinancialParameterTypeQueryService financialParameterTypeQueryService
    ) {
        this.financialParameterTypeService = financialParameterTypeService;
        this.financialParameterTypeRepository = financialParameterTypeRepository;
        this.financialParameterTypeQueryService = financialParameterTypeQueryService;
    }

    /**
     * {@code POST  /financial-parameter-types} : Create a new financialParameterType.
     *
     * @param financialParameterTypeDTO the financialParameterTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new financialParameterTypeDTO, or with status {@code 400 (Bad Request)} if the financialParameterType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FinancialParameterTypeDTO> createFinancialParameterType(
        @Valid @RequestBody FinancialParameterTypeDTO financialParameterTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FinancialParameterType : {}", financialParameterTypeDTO);
        if (financialParameterTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new financialParameterType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        financialParameterTypeDTO = financialParameterTypeService.save(financialParameterTypeDTO);
        return ResponseEntity.created(new URI("/api/financial-parameter-types/" + financialParameterTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, financialParameterTypeDTO.getId().toString()))
            .body(financialParameterTypeDTO);
    }

    /**
     * {@code PUT  /financial-parameter-types/:id} : Updates an existing financialParameterType.
     *
     * @param id the id of the financialParameterTypeDTO to save.
     * @param financialParameterTypeDTO the financialParameterTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financialParameterTypeDTO,
     * or with status {@code 400 (Bad Request)} if the financialParameterTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the financialParameterTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FinancialParameterTypeDTO> updateFinancialParameterType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FinancialParameterTypeDTO financialParameterTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FinancialParameterType : {}, {}", id, financialParameterTypeDTO);
        if (financialParameterTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financialParameterTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financialParameterTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        financialParameterTypeDTO = financialParameterTypeService.update(financialParameterTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, financialParameterTypeDTO.getId().toString()))
            .body(financialParameterTypeDTO);
    }

    /**
     * {@code PATCH  /financial-parameter-types/:id} : Partial updates given fields of an existing financialParameterType, field will ignore if it is null
     *
     * @param id the id of the financialParameterTypeDTO to save.
     * @param financialParameterTypeDTO the financialParameterTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financialParameterTypeDTO,
     * or with status {@code 400 (Bad Request)} if the financialParameterTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the financialParameterTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the financialParameterTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FinancialParameterTypeDTO> partialUpdateFinancialParameterType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FinancialParameterTypeDTO financialParameterTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FinancialParameterType partially : {}, {}", id, financialParameterTypeDTO);
        if (financialParameterTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financialParameterTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financialParameterTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FinancialParameterTypeDTO> result = financialParameterTypeService.partialUpdate(financialParameterTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, financialParameterTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /financial-parameter-types} : get all the financialParameterTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of financialParameterTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FinancialParameterTypeDTO>> getAllFinancialParameterTypes(
        FinancialParameterTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get FinancialParameterTypes by criteria: {}", criteria);

        Page<FinancialParameterTypeDTO> page = financialParameterTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /financial-parameter-types/count} : count all the financialParameterTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFinancialParameterTypes(FinancialParameterTypeCriteria criteria) {
        log.debug("REST request to count FinancialParameterTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(financialParameterTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /financial-parameter-types/:id} : get the "id" financialParameterType.
     *
     * @param id the id of the financialParameterTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the financialParameterTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FinancialParameterTypeDTO> getFinancialParameterType(@PathVariable("id") Long id) {
        log.debug("REST request to get FinancialParameterType : {}", id);
        Optional<FinancialParameterTypeDTO> financialParameterTypeDTO = financialParameterTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(financialParameterTypeDTO);
    }

    /**
     * {@code DELETE  /financial-parameter-types/:id} : delete the "id" financialParameterType.
     *
     * @param id the id of the financialParameterTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancialParameterType(@PathVariable("id") Long id) {
        log.debug("REST request to delete FinancialParameterType : {}", id);
        financialParameterTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
