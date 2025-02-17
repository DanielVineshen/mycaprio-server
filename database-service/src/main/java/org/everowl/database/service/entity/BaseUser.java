package org.everowl.database.service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseUser {
    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;
}