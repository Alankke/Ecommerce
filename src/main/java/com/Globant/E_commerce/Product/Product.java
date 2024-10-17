package com.Globant.E_commerce.Product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Product name cannot be null or empty")
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Product description cannot be null or empty")
    private String description;

    @Column(nullable = false)
    @NotNull(message = "Product price must be greater than zero")
    @Positive(message = "Product price must be greater than zero")
    private double price;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Invalid product type")
    private Type type;
}

