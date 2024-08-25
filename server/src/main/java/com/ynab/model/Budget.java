package com.ynab.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Month;
import java.time.Year;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "budget", indexes = @Index(columnList = "name"))
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // *ECP FIXME: BUDGET NAME SHOULD BE UNIQUE FOR THE USER
    //             MAY HAVE TO ENFORCE THIS MANUALLY
    @NonNull
    @NotBlank
    private String name;

    @NonNull
    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private Month lastAccessedMonth;

    private Year lastAccessedYear;

    @OneToMany(mappedBy = "budget", cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.REFRESH}, orphanRemoval = true)
    private List<CategoryGroup> categoryGroups;

}
