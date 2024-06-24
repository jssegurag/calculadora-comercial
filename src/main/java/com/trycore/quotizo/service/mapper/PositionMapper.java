package com.trycore.quotizo.service.mapper;

import com.trycore.quotizo.domain.Position;
import com.trycore.quotizo.service.dto.PositionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Position} and its DTO {@link PositionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PositionMapper extends EntityMapper<PositionDTO, Position> {}
