package co.edu.uniquindio.vitalcareback.Repositories.profiles;

import co.edu.uniquindio.vitalcareback.Model.profiles.StaffProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffProfileRepository extends JpaRepository<StaffProfile, UUID> {
    Optional<StaffProfile> findById(UUID id);
}

