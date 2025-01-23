package org.everowl.mycaprio.database.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity(name = "Catalogue")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CatalogueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catalogue_id")
    private Integer catalogueId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "store-attachment")
    @JoinColumn(name = "store_id",
            nullable = false,
            referencedColumnName = "store_id",
            foreignKey = @ForeignKey(name = "FK_STORE_ATTACHMENT")
    )
    private StoreEntity store;

    @Column(name = "catalogue_name", nullable = false)
    private String catalogueName;

    @Column(name = "catalogue_desc", nullable = false)
    private String catalogueDesc;

    @Column(name = "points_required", nullable = false)
    private Integer pointsRequired;

    @Column(name = "attachment_name")
    private String attachmentName;

    @Column(name = "attachment_path")
    private String attachmentPath;

    @Column(name = "attachment_size", columnDefinition = "BIGINT")
    private Integer attachmentSize;

    @Column(name = "is_available", nullable = false, columnDefinition = "BOOLEAN")
    private Boolean isAvailable = false;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;
}
