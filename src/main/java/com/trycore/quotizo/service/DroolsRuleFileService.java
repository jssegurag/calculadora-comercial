package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.DroolsRuleFile;
import com.trycore.quotizo.repository.DroolsRuleFileRepository;
import com.trycore.quotizo.service.dto.DroolsRuleFileDTO;
import com.trycore.quotizo.service.mapper.DroolsRuleFileMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.trycore.quotizo.domain.DroolsRuleFile}.
 */
@Service
@Transactional
public class DroolsRuleFileService {

    private final Logger log = LoggerFactory.getLogger(DroolsRuleFileService.class);

    private final DroolsRuleFileRepository droolsRuleFileRepository;

    private final DroolsRuleFileMapper droolsRuleFileMapper;

    public DroolsRuleFileService(DroolsRuleFileRepository droolsRuleFileRepository, DroolsRuleFileMapper droolsRuleFileMapper) {
        this.droolsRuleFileRepository = droolsRuleFileRepository;
        this.droolsRuleFileMapper = droolsRuleFileMapper;
    }

    /**
     * Save a droolsRuleFile.
     *
     * @param droolsRuleFileDTO the entity to save.
     * @return the persisted entity.
     */
    public DroolsRuleFileDTO save(DroolsRuleFileDTO droolsRuleFileDTO) {
        log.debug("Request to save DroolsRuleFile : {}", droolsRuleFileDTO);
        DroolsRuleFile droolsRuleFile = droolsRuleFileMapper.toEntity(droolsRuleFileDTO);
        droolsRuleFile = droolsRuleFileRepository.save(droolsRuleFile);
        return droolsRuleFileMapper.toDto(droolsRuleFile);
    }

    /**
     * Update a droolsRuleFile.
     *
     * @param droolsRuleFileDTO the entity to save.
     * @return the persisted entity.
     */
    public DroolsRuleFileDTO update(DroolsRuleFileDTO droolsRuleFileDTO) {
        log.debug("Request to update DroolsRuleFile : {}", droolsRuleFileDTO);
        DroolsRuleFile droolsRuleFile = droolsRuleFileMapper.toEntity(droolsRuleFileDTO);
        droolsRuleFile = droolsRuleFileRepository.save(droolsRuleFile);
        return droolsRuleFileMapper.toDto(droolsRuleFile);
    }

    /**
     * Partially update a droolsRuleFile.
     *
     * @param droolsRuleFileDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DroolsRuleFileDTO> partialUpdate(DroolsRuleFileDTO droolsRuleFileDTO) {
        log.debug("Request to partially update DroolsRuleFile : {}", droolsRuleFileDTO);

        return droolsRuleFileRepository
            .findById(droolsRuleFileDTO.getId())
            .map(existingDroolsRuleFile -> {
                droolsRuleFileMapper.partialUpdate(existingDroolsRuleFile, droolsRuleFileDTO);

                return existingDroolsRuleFile;
            })
            .map(droolsRuleFileRepository::save)
            .map(droolsRuleFileMapper::toDto);
    }

    /**
     * Get one droolsRuleFile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DroolsRuleFileDTO> findOne(Long id) {
        log.debug("Request to get DroolsRuleFile : {}", id);
        return droolsRuleFileRepository.findById(id).map(droolsRuleFileMapper::toDto);
    }

    /**
     * Delete the droolsRuleFile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DroolsRuleFile : {}", id);
        droolsRuleFileRepository.deleteById(id);
    }
}
