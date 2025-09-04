package co.edu.uniquindio.vitalcareback.mapper.billing;

import co.edu.uniquindio.vitalcareback.Dto.billing.PaymentDTO;
import co.edu.uniquindio.vitalcareback.Model.billing.Payment;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDTO toDTO(Payment entity);
    Payment toEntity(PaymentDTO dto);
    List<PaymentDTO> toDTOList(List<Payment> entities);
    List<Payment> toEntityList(List<PaymentDTO> dtos);
}

