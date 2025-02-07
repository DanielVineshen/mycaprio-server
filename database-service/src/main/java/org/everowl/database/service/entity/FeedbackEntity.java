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

@Entity(name = "Feedback")
@Table(name = "`Feedback`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "feedback_id")
    private Integer feedbackId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "store-feedback")
    @JoinColumn(name = "store_id",
            nullable = false,
            referencedColumnName = "store_id",
            foreignKey = @ForeignKey(name = "FK_STORE_FEEDBACK")
    )
    private StoreEntity store;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "contact_no")
    private String contactNo;

    @Column(name = "msg_title", nullable = false)
    private String msgTitle;

    @Column(name = "msg_content", nullable = false, columnDefinition = "TEXT")
    private String msgContent;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;
}
