package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.BudgetComment;
import com.trycore.quotizo.repository.BudgetCommentRepository;
import com.trycore.quotizo.service.dto.BudgetCommentDTO;
import com.trycore.quotizo.service.mapper.BudgetCommentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.trycore.quotizo.domain.BudgetComment}.
 */
@Service
@Transactional
public class BudgetCommentService {

    private final Logger log = LoggerFactory.getLogger(BudgetCommentService.class);

    private final BudgetCommentRepository budgetCommentRepository;

    private final BudgetCommentMapper budgetCommentMapper;

    public BudgetCommentService(BudgetCommentRepository budgetCommentRepository, BudgetCommentMapper budgetCommentMapper) {
        this.budgetCommentRepository = budgetCommentRepository;
        this.budgetCommentMapper = budgetCommentMapper;
    }

    /**
     * Save a budgetComment.
     *
     * @param budgetCommentDTO the entity to save.
     * @return the persisted entity.
     */
    public BudgetCommentDTO save(BudgetCommentDTO budgetCommentDTO) {
        log.debug("Request to save BudgetComment : {}", budgetCommentDTO);
        BudgetComment budgetComment = budgetCommentMapper.toEntity(budgetCommentDTO);
        budgetComment = budgetCommentRepository.save(budgetComment);
        return budgetCommentMapper.toDto(budgetComment);
    }

    /**
     * Update a budgetComment.
     *
     * @param budgetCommentDTO the entity to save.
     * @return the persisted entity.
     */
    public BudgetCommentDTO update(BudgetCommentDTO budgetCommentDTO) {
        log.debug("Request to update BudgetComment : {}", budgetCommentDTO);
        BudgetComment budgetComment = budgetCommentMapper.toEntity(budgetCommentDTO);
        budgetComment = budgetCommentRepository.save(budgetComment);
        return budgetCommentMapper.toDto(budgetComment);
    }

    /**
     * Partially update a budgetComment.
     *
     * @param budgetCommentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BudgetCommentDTO> partialUpdate(BudgetCommentDTO budgetCommentDTO) {
        log.debug("Request to partially update BudgetComment : {}", budgetCommentDTO);

        return budgetCommentRepository
            .findById(budgetCommentDTO.getId())
            .map(existingBudgetComment -> {
                budgetCommentMapper.partialUpdate(existingBudgetComment, budgetCommentDTO);

                return existingBudgetComment;
            })
            .map(budgetCommentRepository::save)
            .map(budgetCommentMapper::toDto);
    }

    /**
     * Get one budgetComment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BudgetCommentDTO> findOne(Long id) {
        log.debug("Request to get BudgetComment : {}", id);
        return budgetCommentRepository.findById(id).map(budgetCommentMapper::toDto);
    }

    /**
     * Delete the budgetComment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BudgetComment : {}", id);
        budgetCommentRepository.deleteById(id);
    }
}
