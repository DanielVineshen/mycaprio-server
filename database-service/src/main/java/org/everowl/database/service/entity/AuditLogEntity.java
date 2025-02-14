package org.everowl.database.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity(name = "Audit_Log")
@Table(name = "`Audit_Log`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "audit_log_id")
    private Integer auditLogId;

    @Column(name = "login_id", nullable = false)
    private String loginId;

    @Column(name = "performed_by")
    private String performedBy;

    @Column(name = "authority_level", nullable = false)
    private String authorityLevel;

    @Column(name = "before_changed", columnDefinition = "text")
    private String beforeChanged;

    @Column(name = "after_changed", columnDefinition = "text")
    private String afterChanged;

    @Column(name = "log_type", nullable = false)
    private String logType;

    @Column(name = "log_type_desc")
    private String logTypeDesc;

    @Column(name = "log_action", nullable = false)
    private String logAction;

    @Column(name = "log_desc")
    private String logDesc;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;

}
