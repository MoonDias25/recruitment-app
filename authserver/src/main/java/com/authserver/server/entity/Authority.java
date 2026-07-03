package com.authserver.server.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="authorities")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private String id;

    @Column(name="authority_name")
    private String authorityName;

    @OneToMany(mappedBy ="authority")
    private List<User> users;

    public String getId() {return id;}

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthority(String authorityName) {
        this.authorityName = authorityName;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void setUser(List<User> users) {
        this.users = users;
    }
}
