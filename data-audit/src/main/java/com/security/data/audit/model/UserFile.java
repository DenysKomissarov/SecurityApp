package com.security.data.audit.model;

import javax.persistence.*;

@Entity
@Table(name = "files", uniqueConstraints = { @UniqueConstraint(columnNames = {"fileName"})})
public class UserFile extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private User owner;

    private String fileName;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
