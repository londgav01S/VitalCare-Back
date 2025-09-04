package co.edu.uniquindio.vitalcareback.Repositories.location;

import co.edu.uniquindio.vitalcareback.Model.location.City;
import co.edu.uniquindio.vitalcareback.Model.location.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SiteRepository extends JpaRepository<Site, UUID> {

    List<Site> findByCity(City city);
}

