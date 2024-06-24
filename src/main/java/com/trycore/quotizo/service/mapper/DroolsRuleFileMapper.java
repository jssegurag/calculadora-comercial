package com.trycore.quotizo.service.mapper;

import com.trycore.quotizo.domain.DroolsRuleFile;
import com.trycore.quotizo.service.dto.DroolsRuleFileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DroolsRuleFile} and its DTO {@link DroolsRuleFileDTO}.
 */
@Mapper(componentModel = "spring")
public interface DroolsRuleFileMapper extends EntityMapper<DroolsRuleFileDTO, DroolsRuleFile> {}
