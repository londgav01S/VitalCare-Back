package co.edu.uniquindio.vitalcareback.mapper.clinical;

import co.edu.uniquindio.vitalcareback.Dto.clinical.MedicalRecordDTO;
import co.edu.uniquindio.vitalcareback.Model.clinical.MedicalRecord;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {
    MedicalRecordDTO toDTO(MedicalRecord entity);
    MedicalRecord toEntity(MedicalRecordDTO dto);
    List<MedicalRecordDTO> toDTOList(List<MedicalRecord> entities);
}

