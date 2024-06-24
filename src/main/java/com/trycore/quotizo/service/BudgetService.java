package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.Budget;
import com.trycore.quotizo.repository.BudgetRepository;
import com.trycore.quotizo.service.dto.BudgetDTO;
import com.trycore.quotizo.service.mapper.BudgetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.trycore.quotizo.domain.Budget}.
 */
@Service
@Transactional
public class BudgetService {

    private final Logger log = LoggerFactory.getLogger(BudgetService.class);

    private final BudgetRepository budgetRepository;

    private final BudgetMapper budgetMapper;

    public BudgetService(BudgetRepository budgetRepository, BudgetMapper budgetMapper) {
        this.budgetRepository = budgetRepository;
        this.budgetMapper = budgetMapper;
    }

    /**
     * Save a budget.
     *
     * @param budgetDTO the entity to save.
     * @return the persisted entity.
     */
    public BudgetDTO save(BudgetDTO budgetDTO) {
        log.debug("Request to save Budget : {}", budgetDTO);
        Budget budget = budgetMapper.toEntity(budgetDTO);
        budget = budgetRepository.save(budget);
        return budgetMapper.toDto(budget);
    }

    /**
     * Update a budget.
     *
     * @param budgetDTO the entity to save.
     * @return the persisted entity.
     */
    public BudgetDTO update(BudgetDTO budgetDTO) {
        log.debug("Request to update Budget : {}", budgetDTO);
        Budget budget = budgetMapper.toEntity(budgetDTO);
        budget = budgetRepository.save(budget);
        return budgetMapper.toDto(budget);
    }

    /**
     * Partially update a budget.
     *
     * @param budgetDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BudgetDTO> partialUpdate(BudgetDTO budgetDTO) {
        log.debug("Request to partially update Budget : {}", budgetDTO);

        return budgetRepository
            .findById(budgetDTO.getId())
            .map(existingBudget -> {
                budgetMapper.partialUpdate(existingBudget, budgetDTO);

                return existingBudget;
            })
            .map(budgetRepository::save)
            .map(budgetMapper::toDto);
    }

    /**
     * Get one budget by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BudgetDTO> findOne(Long id) {
        log.debug("Request to get Budget : {}", id);
        return budgetRepository.findById(id).map(budgetMapper::toDto);
    }

    /**
     * Delete the budget by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Budget : {}", id);
        budgetRepository.deleteById(id);
    }
}
