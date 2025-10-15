package co.edu.uniquindio.vitalcareback.Repositories.auth;

import co.edu.uniquindio.vitalcareback.Model.auth.PasswordResetToken;
import co.edu.uniquindio.vitalcareback.Model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    int deleteByUser(User user);
    Optional<PasswordResetToken> findByUser(User user);
}

