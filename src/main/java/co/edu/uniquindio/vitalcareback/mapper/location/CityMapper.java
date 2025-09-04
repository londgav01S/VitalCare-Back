package co.edu.uniquindio.vitalcareback.mapper.location;


import co.edu.uniquindio.vitalcareback.Dto.location.CityDTO;
import co.edu.uniquindio.vitalcareback.Model.location.City;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CityMapper {
    CityDTO toDTO(City entity);
    City toEntity(CityDTO dto);
    List<CityDTO> toDTOList(List<City> entities);
    List<City> toEntityList(List<CityDTO> dtos);
}

