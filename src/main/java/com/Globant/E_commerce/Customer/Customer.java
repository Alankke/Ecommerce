package com.Globant.E_commerce.Customer;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name cannot be null or empty")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Last name cannot be null or empty")
    private String lastName;

    @Column(nullable = false, unique = true)
    @Email(message = "Invalid email address")
    @NotBlank(message = "Email cannot be null or empty")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Phone cannot be null or empty")
    private String phone;

    @Column(nullable = false)
    @Past(message = "Birth date must be a past date")
    private LocalDate birthDate;
}