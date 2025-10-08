package co.edu.uniquindio.vitalcareback.Controller.Location;

import co.edu.uniquindio.vitalcareback.Dto.location.SiteDTO;
import co.edu.uniquindio.vitalcareback.Services.location.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sites")
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;

    @GetMapping("/all")
    public ResponseEntity<List<SiteDTO>> getAllSites() {
        return ResponseEntity.ok(siteService.getAllSites());
    }
}

