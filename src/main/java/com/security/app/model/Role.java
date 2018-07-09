package com.security.app.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class Role implements Serializable {

    private Set<RoleName> role;

    public Role() {

        role = new HashSet<>();
        role.add(RoleName.ROLE_USER);
    }


    public Role(Set<RoleName> name) {
        this.role = name;
    }

   public Set<RoleName> getName() {
        return role;
    }

    public void setName(Set<RoleName> name) {
        this.role = name;
    }

}
