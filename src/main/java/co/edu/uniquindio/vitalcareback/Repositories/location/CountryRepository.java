package co.edu.uniquindio.vitalcareback.Repositories.location;

import co.edu.uniquindio.vitalcareback.Model.location.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}

