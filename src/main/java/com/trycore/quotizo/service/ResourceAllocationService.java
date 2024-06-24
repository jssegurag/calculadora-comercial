package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.ResourceAllocation;
import com.trycore.quotizo.repository.ResourceAllocationRepository;
import com.trycore.quotizo.service.dto.ResourceAllocationDTO;
import com.trycore.quotizo.service.mapper.ResourceAllocationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.trycore.quotizo.domain.ResourceAllocation}.
 */
@Service
@Transactional
public class ResourceAllocationService {

    private final Logger log = LoggerFactory.getLogger(ResourceAllocationService.class);

    private final ResourceAllocationRepository resourceAllocationRepository;

    private final ResourceAllocationMapper resourceAllocationMapper;

    public ResourceAllocationService(
        ResourceAllocationRepository resourceAllocationRepository,
        ResourceAllocationMapper resourceAllocationMapper
    ) {
        this.resourceAllocationRepository = resourceAllocationRepository;
        this.resourceAllocationMapper = resourceAllocationMapper;
    }

    /**
     * Save a resourceAllocation.
     *
     * @param resourceAllocationDTO the entity to save.
     * @return the persisted entity.
     */
    public ResourceAllocationDTO save(ResourceAllocationDTO resourceAllocationDTO) {
        log.debug("Request to save ResourceAllocation : {}", resourceAllocationDTO);
        ResourceAllocation resourceAllocation = resourceAllocationMapper.toEntity(resourceAllocationDTO);
        resourceAllocation = resourceAllocationRepository.save(resourceAllocation);
        return resourceAllocationMapper.toDto(resourceAllocation);
    }

    /**
     * Update a resourceAllocation.
     *
     * @param resourceAllocationDTO the entity to save.
     * @return the persisted entity.
     */
    public ResourceAllocationDTO update(ResourceAllocationDTO resourceAllocationDTO) {
        log.debug("Request to update ResourceAllocation : {}", resourceAllocationDTO);
        ResourceAllocation resourceAllocation = resourceAllocationMapper.toEntity(resourceAllocationDTO);
        resourceAllocation = resourceAllocationRepository.save(resourceAllocation);
        return resourceAllocationMapper.toDto(resourceAllocation);
    }

    /**
     * Partially update a resourceAllocation.
     *
     * @param resourceAllocationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ResourceAllocationDTO> partialUpdate(ResourceAllocationDTO resourceAllocationDTO) {
        log.debug("Request to partially update ResourceAllocation : {}", resourceAllocationDTO);

        return resourceAllocationRepository
            .findById(resourceAllocationDTO.getId())
            .map(existingResourceAllocation -> {
                resourceAllocationMapper.partialUpdate(existingResourceAllocation, resourceAllocationDTO);

                return existingResourceAllocation;
            })
            .map(resourceAllocationRepository::save)
            .map(resourceAllocationMapper::toDto);
    }

    /**
     * Get one resourceAllocation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ResourceAllocationDTO> findOne(Long id) {
        log.debug("Request to get ResourceAllocation : {}", id);
        return resourceAllocationRepository.findById(id).map(resourceAllocationMapper::toDto);
    }

    /**
     * Delete the resourceAllocation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ResourceAllocation : {}", id);
        resourceAllocationRepository.deleteById(id);
    }
}
