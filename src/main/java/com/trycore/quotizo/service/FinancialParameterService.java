package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.FinancialParameter;
import com.trycore.quotizo.repository.FinancialParameterRepository;
import com.trycore.quotizo.service.dto.FinancialParameterDTO;
import com.trycore.quotizo.service.mapper.FinancialParameterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.trycore.quotizo.domain.FinancialParameter}.
 */
@Service
@Transactional
public class FinancialParameterService {

    private final Logger log = LoggerFactory.getLogger(FinancialParameterService.class);

    private final FinancialParameterRepository financialParameterRepository;

    private final FinancialParameterMapper financialParameterMapper;

    public FinancialParameterService(
        FinancialParameterRepository financialParameterRepository,
        FinancialParameterMapper financialParameterMapper
    ) {
        this.financialParameterRepository = financialParameterRepository;
        this.financialParameterMapper = financialParameterMapper;
    }

    /**
     * Save a financialParameter.
     *
     * @param financialParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public FinancialParameterDTO save(FinancialParameterDTO financialParameterDTO) {
        log.debug("Request to save FinancialParameter : {}", financialParameterDTO);
        FinancialParameter financialParameter = financialParameterMapper.toEntity(financialParameterDTO);
        financialParameter = financialParameterRepository.save(financialParameter);
        return financialParameterMapper.toDto(financialParameter);
    }

    /**
     * Update a financialParameter.
     *
     * @param financialParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public FinancialParameterDTO update(FinancialParameterDTO financialParameterDTO) {
        log.debug("Request to update FinancialParameter : {}", financialParameterDTO);
        FinancialParameter financialParameter = financialParameterMapper.toEntity(financialParameterDTO);
        financialParameter = financialParameterRepository.save(financialParameter);
        return financialParameterMapper.toDto(financialParameter);
    }

    /**
     * Partially update a financialParameter.
     *
     * @param financialParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FinancialParameterDTO> partialUpdate(FinancialParameterDTO financialParameterDTO) {
        log.debug("Request to partially update FinancialParameter : {}", financialParameterDTO);

        return financialParameterRepository
            .findById(financialParameterDTO.getId())
            .map(existingFinancialParameter -> {
                financialParameterMapper.partialUpdate(existingFinancialParameter, financialParameterDTO);

                return existingFinancialParameter;
            })
            .map(financialParameterRepository::save)
            .map(financialParameterMapper::toDto);
    }

    /**
     * Get one financialParameter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FinancialParameterDTO> findOne(Long id) {
        log.debug("Request to get FinancialParameter : {}", id);
        return financialParameterRepository.findById(id).map(financialParameterMapper::toDto);
    }

    /**
     * Delete the financialParameter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FinancialParameter : {}", id);
        financialParameterRepository.deleteById(id);
    }
}
