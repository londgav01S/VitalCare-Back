package co.edu.uniquindio.vitalcareback.mapper.billing;

import co.edu.uniquindio.vitalcareback.Dto.billing.InvoiceDTO;
import co.edu.uniquindio.vitalcareback.Model.billing.Invoice;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    InvoiceDTO toDTO(Invoice entity);
    Invoice toEntity(InvoiceDTO dto);
    List<InvoiceDTO> toDTOList(List<Invoice> entities);
    List<Invoice> toEntityList(List<InvoiceDTO> dtos);
}

