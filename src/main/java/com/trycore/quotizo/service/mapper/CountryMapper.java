package com.trycore.quotizo.service.mapper;

import com.trycore.quotizo.domain.Country;
import com.trycore.quotizo.service.dto.CountryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Country} and its DTO {@link CountryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {}
