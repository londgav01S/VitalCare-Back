package co.edu.uniquindio.vitalcareback.Model.auth;

import co.edu.uniquindio.vitalcareback.Model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog extends BaseEntity {

    @Column(nullable = false)
    private String action;

    private String ipAddress;

    private String userAgent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

