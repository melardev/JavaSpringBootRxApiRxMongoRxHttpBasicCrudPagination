package com.melardev.spring.rest.models;

import com.melardev.spring.rest.entities.Role;
import com.melardev.spring.rest.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class SecurityUser implements UserDetails {

    private final String username;
    private final String password;
    private final Set<Role> role;

    public SecurityUser(User userDb) {
        this.username = userDb.getUsername();
        this.password = userDb.getPassword();
        this.role = userDb.getRoles();
        // For a complex app you would take the roles, locked, credential expired
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Iterator<Role> roleIterator = role.iterator();
        List<GrantedAuthority> roles = new ArrayList<>();
        while (roleIterator.hasNext()) {
            roles.add(new SimpleGrantedAuthority(roleIterator.next().getName()));
        }
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
