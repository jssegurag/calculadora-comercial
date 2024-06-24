package com.trycore.quotizo.web.rest;

import com.trycore.quotizo.repository.UsersRepository;
import com.trycore.quotizo.service.UsersQueryService;
import com.trycore.quotizo.service.UsersService;
import com.trycore.quotizo.service.criteria.UsersCriteria;
import com.trycore.quotizo.service.dto.UsersDTO;
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
 * REST controller for managing {@link com.trycore.quotizo.domain.Users}.
 */
@RestController
@RequestMapping("/api/users")
public class UsersResource {

    private final Logger log = LoggerFactory.getLogger(UsersResource.class);

    private static final String ENTITY_NAME = "users";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UsersService usersService;

    private final UsersRepository usersRepository;

    private final UsersQueryService usersQueryService;

    public UsersResource(UsersService usersService, UsersRepository usersRepository, UsersQueryService usersQueryService) {
        this.usersService = usersService;
        this.usersRepository = usersRepository;
        this.usersQueryService = usersQueryService;
    }

    /**
     * {@code POST  /users} : Create a new users.
     *
     * @param usersDTO the usersDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new usersDTO, or with status {@code 400 (Bad Request)} if the users has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UsersDTO> createUsers(@Valid @RequestBody UsersDTO usersDTO) throws URISyntaxException {
        log.debug("REST request to save Users : {}", usersDTO);
        if (usersDTO.getId() != null) {
            throw new BadRequestAlertException("A new users cannot already have an ID", ENTITY_NAME, "idexists");
        }
        usersDTO = usersService.save(usersDTO);
        return ResponseEntity.created(new URI("/api/users/" + usersDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, usersDTO.getId().toString()))
            .body(usersDTO);
    }

    /**
     * {@code PUT  /users/:id} : Updates an existing users.
     *
     * @param id the id of the usersDTO to save.
     * @param usersDTO the usersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usersDTO,
     * or with status {@code 400 (Bad Request)} if the usersDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the usersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsersDTO> updateUsers(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UsersDTO usersDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Users : {}, {}", id, usersDTO);
        if (usersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        usersDTO = usersService.update(usersDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usersDTO.getId().toString()))
            .body(usersDTO);
    }

    /**
     * {@code PATCH  /users/:id} : Partial updates given fields of an existing users, field will ignore if it is null
     *
     * @param id the id of the usersDTO to save.
     * @param usersDTO the usersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usersDTO,
     * or with status {@code 400 (Bad Request)} if the usersDTO is not valid,
     * or with status {@code 404 (Not Found)} if the usersDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the usersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UsersDTO> partialUpdateUsers(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UsersDTO usersDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Users partially : {}, {}", id, usersDTO);
        if (usersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UsersDTO> result = usersService.partialUpdate(usersDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usersDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /users} : get all the users.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of users in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UsersDTO>> getAllUsers(
        UsersCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Users by criteria: {}", criteria);

        Page<UsersDTO> page = usersQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /users/count} : count all the users.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUsers(UsersCriteria criteria) {
        log.debug("REST request to count Users by criteria: {}", criteria);
        return ResponseEntity.ok().body(usersQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /users/:id} : get the "id" users.
     *
     * @param id the id of the usersDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the usersDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsersDTO> getUsers(@PathVariable("id") Long id) {
        log.debug("REST request to get Users : {}", id);
        Optional<UsersDTO> usersDTO = usersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(usersDTO);
    }

    /**
     * {@code DELETE  /users/:id} : delete the "id" users.
     *
     * @param id the id of the usersDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsers(@PathVariable("id") Long id) {
        log.debug("REST request to delete Users : {}", id);
        usersService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
