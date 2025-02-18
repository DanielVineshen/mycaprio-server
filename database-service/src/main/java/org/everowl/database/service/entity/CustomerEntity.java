package org.everowl.database.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity(name = "Customer")
@Table(name = "`Customer`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("CUSTOMER")
public class CustomerEntity extends BaseUser {
    @Id
    @Column(name = "cust_id", length = 9)
    private String custId;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "sms_code", length = 6)
    private String smsCode;

    @Column(name = "sms_attempt", nullable = false)
    private Integer smsAttempt;

    @Column(name = "sms_last_datetime", length = 14)
    private String smsLastDatetime;

    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth", length = 8)
    private String dateOfBirth;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "customer-storeCustomer")
    private List<StoreCustomerEntity> storeCustomers;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date  updatedAt;
}
