package co.edu.uniquindio.vitalcareback.Services.location;

import co.edu.uniquindio.vitalcareback.Dto.location.CityDTO;
import co.edu.uniquindio.vitalcareback.Repositories.location.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CityService
 *
 * Servicio encargado de la gestión de ciudades en el sistema.
 * Permite obtener la lista de todas las ciudades disponibles.
 */
@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    /**
     * Obtiene todas las ciudades registradas en la base de datos.
     *
     * @return Lista de CityDTO con el ID y nombre de cada ciudad
     */
    public List<CityDTO> getAllCities() {
        return cityRepository.findAll()
                .stream()
                .map(city -> new CityDTO(city.getId(), city.getName()))
                .toList();
    }
}
