package co.edu.uniquindio.vitalcareback.Services.location;

import co.edu.uniquindio.vitalcareback.Dto.location.SiteDTO;
import co.edu.uniquindio.vitalcareback.Model.location.Site;
import co.edu.uniquindio.vitalcareback.Repositories.location.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SiteService {

    private final SiteRepository siteRepository;

    public List<SiteDTO> getAllSites() {
        List<Site> sites = siteRepository.findAll();
        return sites.stream()
                .map(site -> SiteDTO.builder()
                        .id(site.getId())
                        .name(site.getName())
                        .build())
                .collect(Collectors.toList());
    }
}

