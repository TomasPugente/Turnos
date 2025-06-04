package com.grupo12.entities;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled")
    private String enabled;

    @Column(name = "createdAt")
    @CreationTimestamp
    private String createdAt;

    @Column(name = "updatedAt")
    private String updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "User")
    @SuppressWarnings("unused")
    private Set<UserRole> userRoles = new HashSet<UserRole>();

}
