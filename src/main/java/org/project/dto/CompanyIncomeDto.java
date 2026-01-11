package org.project.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@ToString
public class CompanyIncomeDto {

    @NotBlank(message = "Company name cannot be blank!")
    @Size(max = 20, message = "Company name cannot be more than 20 characters!")
    @Pattern(regexp = "^([A-Z]).*", message = "Company name has to start with a capital letter!")
    private String name;

    @DecimalMin(value = "1.00", message = "There cannot be price 0.00 or less!")
    @Digits(integer = 5, fraction = 2, message = "Price must have up to 5 digits and 2 decimals!")
    private BigDecimal income;
}
