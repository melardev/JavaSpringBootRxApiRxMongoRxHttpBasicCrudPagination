package com.melardev.spring.rest.entities;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Role extends TimeStampedDocument {
    public String name;

    public Role() {
    }

    public Role(String roleName) {
        this.name = roleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == Role.class && ((Role) obj).getName().equalsIgnoreCase(name);
    }
}
