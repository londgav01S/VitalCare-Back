package co.edu.uniquindio.vitalcareback.Dto.location;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityDTO {
    private UUID id;
    private String name;
    private UUID departmentId;
}

