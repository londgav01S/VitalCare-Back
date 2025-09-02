package co.edu.uniquindio.vitalcareback.Repositories.auth;


import co.edu.uniquindio.vitalcareback.Model.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
