package co.edu.uniquindio.vitalcareback.mapper.location;

import co.edu.uniquindio.vitalcareback.Dto.location.SiteDTO;
import co.edu.uniquindio.vitalcareback.Model.location.Site;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SiteMapper {
    SiteDTO toDTO(Site entity);
    Site toEntity(SiteDTO dto);
}

