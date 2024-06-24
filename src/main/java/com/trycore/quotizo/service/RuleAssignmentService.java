package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.RuleAssignment;
import com.trycore.quotizo.repository.RuleAssignmentRepository;
import com.trycore.quotizo.service.dto.RuleAssignmentDTO;
import com.trycore.quotizo.service.mapper.RuleAssignmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.trycore.quotizo.domain.RuleAssignment}.
 */
@Service
@Transactional
public class RuleAssignmentService {

    private final Logger log = LoggerFactory.getLogger(RuleAssignmentService.class);

    private final RuleAssignmentRepository ruleAssignmentRepository;

    private final RuleAssignmentMapper ruleAssignmentMapper;

    public RuleAssignmentService(RuleAssignmentRepository ruleAssignmentRepository, RuleAssignmentMapper ruleAssignmentMapper) {
        this.ruleAssignmentRepository = ruleAssignmentRepository;
        this.ruleAssignmentMapper = ruleAssignmentMapper;
    }

    /**
     * Save a ruleAssignment.
     *
     * @param ruleAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public RuleAssignmentDTO save(RuleAssignmentDTO ruleAssignmentDTO) {
        log.debug("Request to save RuleAssignment : {}", ruleAssignmentDTO);
        RuleAssignment ruleAssignment = ruleAssignmentMapper.toEntity(ruleAssignmentDTO);
        ruleAssignment = ruleAssignmentRepository.save(ruleAssignment);
        return ruleAssignmentMapper.toDto(ruleAssignment);
    }

    /**
     * Update a ruleAssignment.
     *
     * @param ruleAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public RuleAssignmentDTO update(RuleAssignmentDTO ruleAssignmentDTO) {
        log.debug("Request to update RuleAssignment : {}", ruleAssignmentDTO);
        RuleAssignment ruleAssignment = ruleAssignmentMapper.toEntity(ruleAssignmentDTO);
        ruleAssignment = ruleAssignmentRepository.save(ruleAssignment);
        return ruleAssignmentMapper.toDto(ruleAssignment);
    }

    /**
     * Partially update a ruleAssignment.
     *
     * @param ruleAssignmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RuleAssignmentDTO> partialUpdate(RuleAssignmentDTO ruleAssignmentDTO) {
        log.debug("Request to partially update RuleAssignment : {}", ruleAssignmentDTO);

        return ruleAssignmentRepository
            .findById(ruleAssignmentDTO.getId())
            .map(existingRuleAssignment -> {
                ruleAssignmentMapper.partialUpdate(existingRuleAssignment, ruleAssignmentDTO);

                return existingRuleAssignment;
            })
            .map(ruleAssignmentRepository::save)
            .map(ruleAssignmentMapper::toDto);
    }

    /**
     * Get one ruleAssignment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RuleAssignmentDTO> findOne(Long id) {
        log.debug("Request to get RuleAssignment : {}", id);
        return ruleAssignmentRepository.findById(id).map(ruleAssignmentMapper::toDto);
    }

    /**
     * Delete the ruleAssignment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RuleAssignment : {}", id);
        ruleAssignmentRepository.deleteById(id);
    }
}
