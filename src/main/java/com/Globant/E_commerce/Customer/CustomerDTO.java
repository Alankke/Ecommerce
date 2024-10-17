package com.Globant.E_commerce.Customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    @Schema(description = "Unique identifier for the customer", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "First name of the customer", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Name cannot be null or empty")
    private String name;

    @Schema(description = "Last name of the customer", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Last name cannot be null or empty")
    private String lastName;

    @Schema(description = "Email address of the customer", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email(message = "Invalid email address")
    @NotBlank(message = "Email cannot be null or empty")
    private String email;

    @Schema(description = "Phone number of the customer", example = "+123456789", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Phone cannot be null or empty")
    private String phone;

    @Schema(description = "Birth date of the customer", example = "1990-05-15", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Birth date must be provided")
    @Past(message = "Birth date must be a past date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}