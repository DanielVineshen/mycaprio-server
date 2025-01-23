package org.everowl.mycaprio.shared.service;

import lombok.Getter;
import org.hibernate.usertype.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private final org.everowl.mycaprio.shared.enums.UserType userType;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(BaseUser user, UserType userType) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.userType = userType;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userType));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}