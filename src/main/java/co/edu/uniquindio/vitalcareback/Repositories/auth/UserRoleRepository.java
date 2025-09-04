package co.edu.uniquindio.vitalcareback.Repositories.auth;


import co.edu.uniquindio.vitalcareback.Model.auth.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}

