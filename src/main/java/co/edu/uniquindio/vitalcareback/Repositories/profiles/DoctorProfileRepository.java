package co.edu.uniquindio.vitalcareback.Repositories.profiles;

import co.edu.uniquindio.vitalcareback.Model.profiles.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, UUID> {
    Optional<DoctorProfile> findById(UUID id);
}

