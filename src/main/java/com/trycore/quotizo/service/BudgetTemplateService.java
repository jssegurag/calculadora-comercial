package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.BudgetTemplate;
import com.trycore.quotizo.repository.BudgetTemplateRepository;
import com.trycore.quotizo.service.dto.BudgetTemplateDTO;
import com.trycore.quotizo.service.mapper.BudgetTemplateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.trycore.quotizo.domain.BudgetTemplate}.
 */
@Service
@Transactional
public class BudgetTemplateService {

    private final Logger log = LoggerFactory.getLogger(BudgetTemplateService.class);

    private final BudgetTemplateRepository budgetTemplateRepository;

    private final BudgetTemplateMapper budgetTemplateMapper;

    public BudgetTemplateService(BudgetTemplateRepository budgetTemplateRepository, BudgetTemplateMapper budgetTemplateMapper) {
        this.budgetTemplateRepository = budgetTemplateRepository;
        this.budgetTemplateMapper = budgetTemplateMapper;
    }

    /**
     * Save a budgetTemplate.
     *
     * @param budgetTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    public BudgetTemplateDTO save(BudgetTemplateDTO budgetTemplateDTO) {
        log.debug("Request to save BudgetTemplate : {}", budgetTemplateDTO);
        BudgetTemplate budgetTemplate = budgetTemplateMapper.toEntity(budgetTemplateDTO);
        budgetTemplate = budgetTemplateRepository.save(budgetTemplate);
        return budgetTemplateMapper.toDto(budgetTemplate);
    }

    /**
     * Update a budgetTemplate.
     *
     * @param budgetTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    public BudgetTemplateDTO update(BudgetTemplateDTO budgetTemplateDTO) {
        log.debug("Request to update BudgetTemplate : {}", budgetTemplateDTO);
        BudgetTemplate budgetTemplate = budgetTemplateMapper.toEntity(budgetTemplateDTO);
        budgetTemplate = budgetTemplateRepository.save(budgetTemplate);
        return budgetTemplateMapper.toDto(budgetTemplate);
    }

    /**
     * Partially update a budgetTemplate.
     *
     * @param budgetTemplateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BudgetTemplateDTO> partialUpdate(BudgetTemplateDTO budgetTemplateDTO) {
        log.debug("Request to partially update BudgetTemplate : {}", budgetTemplateDTO);

        return budgetTemplateRepository
            .findById(budgetTemplateDTO.getId())
            .map(existingBudgetTemplate -> {
                budgetTemplateMapper.partialUpdate(existingBudgetTemplate, budgetTemplateDTO);

                return existingBudgetTemplate;
            })
            .map(budgetTemplateRepository::save)
            .map(budgetTemplateMapper::toDto);
    }

    /**
     * Get one budgetTemplate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BudgetTemplateDTO> findOne(Long id) {
        log.debug("Request to get BudgetTemplate : {}", id);
        return budgetTemplateRepository.findById(id).map(budgetTemplateMapper::toDto);
    }

    /**
     * Delete the budgetTemplate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BudgetTemplate : {}", id);
        budgetTemplateRepository.deleteById(id);
    }
}
