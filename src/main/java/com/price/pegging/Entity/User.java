package com.price.pegging.Entity;

//import lombok.Data;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="USER",schema="pricepegging")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

    @Column(name="active")
    private int active;

    @Column(name="email")
    private String email;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "name")
    private String name;

    @Column(name="password")
    private String password;

    @Column(name="createon")
    private LocalDateTime createon;

    @Column(name="contenttype")
    private String contenttype;
    @Column(name="filename")
    private String filename;

    @Column(name="profile_picture")
    private String profilePicture;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserRole> userRoles;

    @PrePersist
    public void prePersist() {
        this.createon = LocalDateTime.now();
    }


}
