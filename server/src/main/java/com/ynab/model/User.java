package com.ynab.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "users", indexes = @Index(columnList = "email"))
public class User implements UserDetails {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // Needed for UserDetails
    public String getUsername() {
        return email;
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
