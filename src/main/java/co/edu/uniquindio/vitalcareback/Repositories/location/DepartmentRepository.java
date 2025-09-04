package co.edu.uniquindio.vitalcareback.Repositories.location;

import co.edu.uniquindio.vitalcareback.Model.location.Country;
import co.edu.uniquindio.vitalcareback.Model.location.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    List<Department> findByCountry(Country country);
}

