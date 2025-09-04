package co.edu.uniquindio.vitalcareback.Repositories.location;

import co.edu.uniquindio.vitalcareback.Model.location.City;
import co.edu.uniquindio.vitalcareback.Model.location.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CityRepository extends JpaRepository<City, UUID> {

    List<City> findByDepartment(Department department);

    Optional<City> findById(UUID cityId);
}

