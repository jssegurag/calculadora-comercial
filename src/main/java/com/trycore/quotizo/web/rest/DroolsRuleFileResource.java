package com.trycore.quotizo.web.rest;

import com.trycore.quotizo.repository.DroolsRuleFileRepository;
import com.trycore.quotizo.service.DroolsRuleFileQueryService;
import com.trycore.quotizo.service.DroolsRuleFileService;
import com.trycore.quotizo.service.criteria.DroolsRuleFileCriteria;
import com.trycore.quotizo.service.dto.DroolsRuleFileDTO;
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
 * REST controller for managing {@link com.trycore.quotizo.domain.DroolsRuleFile}.
 */
@RestController
@RequestMapping("/api/drools-rule-files")
public class DroolsRuleFileResource {

    private final Logger log = LoggerFactory.getLogger(DroolsRuleFileResource.class);

    private static final String ENTITY_NAME = "droolsRuleFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DroolsRuleFileService droolsRuleFileService;

    private final DroolsRuleFileRepository droolsRuleFileRepository;

    private final DroolsRuleFileQueryService droolsRuleFileQueryService;

    public DroolsRuleFileResource(
        DroolsRuleFileService droolsRuleFileService,
        DroolsRuleFileRepository droolsRuleFileRepository,
        DroolsRuleFileQueryService droolsRuleFileQueryService
    ) {
        this.droolsRuleFileService = droolsRuleFileService;
        this.droolsRuleFileRepository = droolsRuleFileRepository;
        this.droolsRuleFileQueryService = droolsRuleFileQueryService;
    }

    /**
     * {@code POST  /drools-rule-files} : Create a new droolsRuleFile.
     *
     * @param droolsRuleFileDTO the droolsRuleFileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new droolsRuleFileDTO, or with status {@code 400 (Bad Request)} if the droolsRuleFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DroolsRuleFileDTO> createDroolsRuleFile(@Valid @RequestBody DroolsRuleFileDTO droolsRuleFileDTO)
        throws URISyntaxException {
        log.debug("REST request to save DroolsRuleFile : {}", droolsRuleFileDTO);
        if (droolsRuleFileDTO.getId() != null) {
            throw new BadRequestAlertException("A new droolsRuleFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        droolsRuleFileDTO = droolsRuleFileService.save(droolsRuleFileDTO);
        return ResponseEntity.created(new URI("/api/drools-rule-files/" + droolsRuleFileDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, droolsRuleFileDTO.getId().toString()))
            .body(droolsRuleFileDTO);
    }

    /**
     * {@code PUT  /drools-rule-files/:id} : Updates an existing droolsRuleFile.
     *
     * @param id the id of the droolsRuleFileDTO to save.
     * @param droolsRuleFileDTO the droolsRuleFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated droolsRuleFileDTO,
     * or with status {@code 400 (Bad Request)} if the droolsRuleFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the droolsRuleFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DroolsRuleFileDTO> updateDroolsRuleFile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DroolsRuleFileDTO droolsRuleFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DroolsRuleFile : {}, {}", id, droolsRuleFileDTO);
        if (droolsRuleFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, droolsRuleFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!droolsRuleFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        droolsRuleFileDTO = droolsRuleFileService.update(droolsRuleFileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, droolsRuleFileDTO.getId().toString()))
            .body(droolsRuleFileDTO);
    }

    /**
     * {@code PATCH  /drools-rule-files/:id} : Partial updates given fields of an existing droolsRuleFile, field will ignore if it is null
     *
     * @param id the id of the droolsRuleFileDTO to save.
     * @param droolsRuleFileDTO the droolsRuleFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated droolsRuleFileDTO,
     * or with status {@code 400 (Bad Request)} if the droolsRuleFileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the droolsRuleFileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the droolsRuleFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DroolsRuleFileDTO> partialUpdateDroolsRuleFile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DroolsRuleFileDTO droolsRuleFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DroolsRuleFile partially : {}, {}", id, droolsRuleFileDTO);
        if (droolsRuleFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, droolsRuleFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!droolsRuleFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DroolsRuleFileDTO> result = droolsRuleFileService.partialUpdate(droolsRuleFileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, droolsRuleFileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /drools-rule-files} : get all the droolsRuleFiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of droolsRuleFiles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DroolsRuleFileDTO>> getAllDroolsRuleFiles(
        DroolsRuleFileCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DroolsRuleFiles by criteria: {}", criteria);

        Page<DroolsRuleFileDTO> page = droolsRuleFileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /drools-rule-files/count} : count all the droolsRuleFiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDroolsRuleFiles(DroolsRuleFileCriteria criteria) {
        log.debug("REST request to count DroolsRuleFiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(droolsRuleFileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /drools-rule-files/:id} : get the "id" droolsRuleFile.
     *
     * @param id the id of the droolsRuleFileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the droolsRuleFileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DroolsRuleFileDTO> getDroolsRuleFile(@PathVariable("id") Long id) {
        log.debug("REST request to get DroolsRuleFile : {}", id);
        Optional<DroolsRuleFileDTO> droolsRuleFileDTO = droolsRuleFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(droolsRuleFileDTO);
    }

    /**
     * {@code DELETE  /drools-rule-files/:id} : delete the "id" droolsRuleFile.
     *
     * @param id the id of the droolsRuleFileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDroolsRuleFile(@PathVariable("id") Long id) {
        log.debug("REST request to delete DroolsRuleFile : {}", id);
        droolsRuleFileService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
