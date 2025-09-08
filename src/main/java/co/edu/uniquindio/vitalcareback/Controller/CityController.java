package co.edu.uniquindio.vitalcareback.Controller;

import co.edu.uniquindio.vitalcareback.Dto.location.CityDTO;
import co.edu.uniquindio.vitalcareback.Services.location.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CityController
 *
 * Controlador REST encargado de manejar operaciones relacionadas con las ciudades.
 * Proporciona endpoints para consultar la información de ciudades disponibles en el sistema.
 */
@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    /**
     * Servicio que contiene la lógica de negocio para las ciudades.
     */
    private final CityService cityService;

    /**
     * Endpoint para obtener todas las ciudades registradas en el sistema.
     *
     * @return ResponseEntity con la lista de CityDTO
     */
    @GetMapping
    public ResponseEntity<List<CityDTO>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }
}
