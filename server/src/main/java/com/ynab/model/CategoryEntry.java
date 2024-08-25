package com.ynab.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "category_entry")
public class CategoryEntry {
    @EmbeddedId
    private CategoryEntryId id;

    @NonNull
    private BigDecimal amount;

    @MapsId("categoryId")
    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class CategoryEntryId implements Serializable {
        @NonNull
        @Column(name = "category_id")
        private Long categoryId;

        @NonNull
        @Enumerated(EnumType.ORDINAL)
        private Month budgetMonth;

        @NonNull
        private Year budgetYear;
    }
}
