package co.edu.uniquindio.vitalcareback.Repositories.auth;


import co.edu.uniquindio.vitalcareback.Model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);


    boolean existsByEmail(String email);
}

