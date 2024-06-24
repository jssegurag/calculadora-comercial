package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.UserRole;
import com.trycore.quotizo.repository.UserRoleRepository;
import com.trycore.quotizo.service.dto.UserRoleDTO;
import com.trycore.quotizo.service.mapper.UserRoleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.trycore.quotizo.domain.UserRole}.
 */
@Service
@Transactional
public class UserRoleService {

    private final Logger log = LoggerFactory.getLogger(UserRoleService.class);

    private final UserRoleRepository userRoleRepository;

    private final UserRoleMapper userRoleMapper;

    public UserRoleService(UserRoleRepository userRoleRepository, UserRoleMapper userRoleMapper) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleMapper = userRoleMapper;
    }

    /**
     * Save a userRole.
     *
     * @param userRoleDTO the entity to save.
     * @return the persisted entity.
     */
    public UserRoleDTO save(UserRoleDTO userRoleDTO) {
        log.debug("Request to save UserRole : {}", userRoleDTO);
        UserRole userRole = userRoleMapper.toEntity(userRoleDTO);
        userRole = userRoleRepository.save(userRole);
        return userRoleMapper.toDto(userRole);
    }

    /**
     * Update a userRole.
     *
     * @param userRoleDTO the entity to save.
     * @return the persisted entity.
     */
    public UserRoleDTO update(UserRoleDTO userRoleDTO) {
        log.debug("Request to update UserRole : {}", userRoleDTO);
        UserRole userRole = userRoleMapper.toEntity(userRoleDTO);
        userRole = userRoleRepository.save(userRole);
        return userRoleMapper.toDto(userRole);
    }

    /**
     * Partially update a userRole.
     *
     * @param userRoleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserRoleDTO> partialUpdate(UserRoleDTO userRoleDTO) {
        log.debug("Request to partially update UserRole : {}", userRoleDTO);

        return userRoleRepository
            .findById(userRoleDTO.getId())
            .map(existingUserRole -> {
                userRoleMapper.partialUpdate(existingUserRole, userRoleDTO);

                return existingUserRole;
            })
            .map(userRoleRepository::save)
            .map(userRoleMapper::toDto);
    }

    /**
     * Get all the userRoles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserRoleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userRoleRepository.findAllWithEagerRelationships(pageable).map(userRoleMapper::toDto);
    }

    /**
     * Get one userRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserRoleDTO> findOne(Long id) {
        log.debug("Request to get UserRole : {}", id);
        return userRoleRepository.findOneWithEagerRelationships(id).map(userRoleMapper::toDto);
    }

    /**
     * Delete the userRole by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserRole : {}", id);
        userRoleRepository.deleteById(id);
    }
}
