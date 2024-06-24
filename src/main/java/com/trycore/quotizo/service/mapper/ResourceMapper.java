package com.trycore.quotizo.service.mapper;

import com.trycore.quotizo.domain.Position;
import com.trycore.quotizo.domain.Resource;
import com.trycore.quotizo.service.dto.PositionDTO;
import com.trycore.quotizo.service.dto.ResourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Resource} and its DTO {@link ResourceDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResourceMapper extends EntityMapper<ResourceDTO, Resource> {
    @Mapping(target = "position", source = "position", qualifiedByName = "positionId")
    ResourceDTO toDto(Resource s);

    @Named("positionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PositionDTO toDtoPositionId(Position position);
}
