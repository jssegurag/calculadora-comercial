package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.FinancialParameterType;
import com.trycore.quotizo.repository.FinancialParameterTypeRepository;
import com.trycore.quotizo.service.dto.FinancialParameterTypeDTO;
import com.trycore.quotizo.service.mapper.FinancialParameterTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.trycore.quotizo.domain.FinancialParameterType}.
 */
@Service
@Transactional
public class FinancialParameterTypeService {

    private final Logger log = LoggerFactory.getLogger(FinancialParameterTypeService.class);

    private final FinancialParameterTypeRepository financialParameterTypeRepository;

    private final FinancialParameterTypeMapper financialParameterTypeMapper;

    public FinancialParameterTypeService(
        FinancialParameterTypeRepository financialParameterTypeRepository,
        FinancialParameterTypeMapper financialParameterTypeMapper
    ) {
        this.financialParameterTypeRepository = financialParameterTypeRepository;
        this.financialParameterTypeMapper = financialParameterTypeMapper;
    }

    /**
     * Save a financialParameterType.
     *
     * @param financialParameterTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public FinancialParameterTypeDTO save(FinancialParameterTypeDTO financialParameterTypeDTO) {
        log.debug("Request to save FinancialParameterType : {}", financialParameterTypeDTO);
        FinancialParameterType financialParameterType = financialParameterTypeMapper.toEntity(financialParameterTypeDTO);
        financialParameterType = financialParameterTypeRepository.save(financialParameterType);
        return financialParameterTypeMapper.toDto(financialParameterType);
    }

    /**
     * Update a financialParameterType.
     *
     * @param financialParameterTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public FinancialParameterTypeDTO update(FinancialParameterTypeDTO financialParameterTypeDTO) {
        log.debug("Request to update FinancialParameterType : {}", financialParameterTypeDTO);
        FinancialParameterType financialParameterType = financialParameterTypeMapper.toEntity(financialParameterTypeDTO);
        financialParameterType = financialParameterTypeRepository.save(financialParameterType);
        return financialParameterTypeMapper.toDto(financialParameterType);
    }

    /**
     * Partially update a financialParameterType.
     *
     * @param financialParameterTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FinancialParameterTypeDTO> partialUpdate(FinancialParameterTypeDTO financialParameterTypeDTO) {
        log.debug("Request to partially update FinancialParameterType : {}", financialParameterTypeDTO);

        return financialParameterTypeRepository
            .findById(financialParameterTypeDTO.getId())
            .map(existingFinancialParameterType -> {
                financialParameterTypeMapper.partialUpdate(existingFinancialParameterType, financialParameterTypeDTO);

                return existingFinancialParameterType;
            })
            .map(financialParameterTypeRepository::save)
            .map(financialParameterTypeMapper::toDto);
    }

    /**
     * Get one financialParameterType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FinancialParameterTypeDTO> findOne(Long id) {
        log.debug("Request to get FinancialParameterType : {}", id);
        return financialParameterTypeRepository.findById(id).map(financialParameterTypeMapper::toDto);
    }

    /**
     * Delete the financialParameterType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FinancialParameterType : {}", id);
        financialParameterTypeRepository.deleteById(id);
    }
}
