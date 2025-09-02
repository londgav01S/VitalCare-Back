package co.edu.uniquindio.vitalcareback.Repositories.auth;

import co.edu.uniquindio.vitalcareback.Model.auth.RefreshToken;
import co.edu.uniquindio.vitalcareback.Model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    int deleteByUser(User user);
}

