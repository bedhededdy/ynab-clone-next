package com.ynab.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "users", indexes = @Index(columnList = "email"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank
    @Column(unique = true)
    private String email;

    @NonNull
    @NotBlank
    private String password;

    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.REFRESH}, orphanRemoval = true)
    private Budget lastOpenedBudget;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.REFRESH}, orphanRemoval = true)
    private List<Budget> budgets;
}
