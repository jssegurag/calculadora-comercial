package com.trycore.quotizo.service.mapper;

import com.trycore.quotizo.domain.DroolsRuleFile;
import com.trycore.quotizo.domain.RuleAssignment;
import com.trycore.quotizo.service.dto.DroolsRuleFileDTO;
import com.trycore.quotizo.service.dto.RuleAssignmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RuleAssignment} and its DTO {@link RuleAssignmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface RuleAssignmentMapper extends EntityMapper<RuleAssignmentDTO, RuleAssignment> {
    @Mapping(target = "droolsRuleFile", source = "droolsRuleFile", qualifiedByName = "droolsRuleFileId")
    RuleAssignmentDTO toDto(RuleAssignment s);

    @Named("droolsRuleFileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DroolsRuleFileDTO toDtoDroolsRuleFileId(DroolsRuleFile droolsRuleFile);
}
