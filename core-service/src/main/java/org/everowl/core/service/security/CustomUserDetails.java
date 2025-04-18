package org.everowl.core.service.security;

import lombok.Getter;
import org.everowl.database.service.entity.BaseUser;
import org.everowl.shared.service.enums.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private final String fullName;
    private final UserType userType;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(BaseUser user, UserType userType) {
        this.username = user.getLoginId();
        this.password = user.getPassword();
        this.userType = userType;
        this.fullName = user.getFullName();
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