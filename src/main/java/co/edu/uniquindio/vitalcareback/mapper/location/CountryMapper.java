package co.edu.uniquindio.vitalcareback.mapper.location;

import co.edu.uniquindio.vitalcareback.Dto.location.CountryDTO;
import co.edu.uniquindio.vitalcareback.Model.location.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    CountryDTO toDTO(Country entity);
    Country toEntity(CountryDTO dto);
}

