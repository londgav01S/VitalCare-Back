package co.edu.uniquindio.vitalcareback.Services.location;

import co.edu.uniquindio.vitalcareback.Dto.location.CityDTO;
import co.edu.uniquindio.vitalcareback.Repositories.location.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public List<CityDTO> getAllCities() {
        return cityRepository.findAll()
                .stream()
                .map(city -> new CityDTO(city.getId(), city.getName()))
                .toList();
    }
}

