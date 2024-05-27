package ru.bulkhak.gateway.security;

import org.springframework.context.annotation.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;


public class UserDetailsImpl implements UserDetails {
    private final String userID;
    private final String username;
    private final Set<String> roles;

    public UserDetailsImpl(String userID, String username, Set<String> roles) {
        this.userID = userID;
        this.username = username;
        this.roles = roles;
    }

    public Set<String> getRoles() {
        return this.roles;
    }

    public String getUserId() {
        return this.userID;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
        return roles.stream().filter(role -> role.startsWith("ROLE_")).map(SimpleGrantedAuthority::new).distinct().toList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
