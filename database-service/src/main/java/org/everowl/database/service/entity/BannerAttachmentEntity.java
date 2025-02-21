package org.everowl.database.service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity(name = "Banner_Attachment")
@Table(name = "`Banner_Attachment`", uniqueConstraints = {
        @UniqueConstraint(name = "UK_Banner_Attachment_attachment_name", columnNames = "attachment_name"),
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BannerAttachmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Integer attachmentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "store-attachment")
    @JoinColumn(name = "store_id",
            nullable = false,
            referencedColumnName = "store_id",
            foreignKey = @ForeignKey(name = "FK_STORE_ATTACHMENT")
    )
    private StoreEntity store;

    @Column(name = "attachment_name", nullable = false)
    private String attachmentName;

    @Column(name = "attachment_path", nullable = false)
    private String attachmentPath;

    @Column(name = "attachment_size", nullable = false, columnDefinition = "BIGINT")
    private Integer attachmentSize;

    @Column(name = "attachment_type", nullable = false)
    private String attachmentType;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;
}
