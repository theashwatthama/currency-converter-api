package com.preet.currencyconverter.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conversion_history")
@EntityListeners(AuditingEntityListener.class)
public class ConversionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 3)
    private String fromCurrency;

    @Column(nullable = false,length = 3)
    private String toCurrency;

    @Column(nullable = false)
    private double units;

    @Column(nullable = false)
    private double convertedAmount;




    @CreatedDate
    @Column(nullable = false,updatable = false)
    private Instant createdAt;

    @Setter
    @LastModifiedDate
    private Instant updatedAt;




}
