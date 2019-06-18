package com.melardev.spring.rest.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document
public class User extends TimeStampedDocument {
    private String username;
    private String password;
    private Set<Role> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void addRole(Role role) {
        if (roles == null)
            roles = new HashSet<>();

        this.roles.add(role);
    }
}
