package co.edu.uniquindio.vitalcareback.Repositories.auth;

import co.edu.uniquindio.vitalcareback.Model.auth.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}

