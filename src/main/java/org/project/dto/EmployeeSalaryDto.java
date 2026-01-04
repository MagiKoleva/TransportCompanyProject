package org.project.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeSalaryDto {

    @NotBlank(message = "Employee first name cannot be blank!")
    @Pattern(regexp = "^([A-Z]).*", message = "Employee first name has to start with a capital letter!")
    private String firstName;

    @NotBlank(message = "Employee last name cannot be blank!")
    @Pattern(regexp = "^([A-Z]).*", message = "Employee last name has to start with a capital letter!")
    private String lastName;

    @NotNull(message = "Employee must have a salary!")
    @DecimalMin(value = "100.00", message = "Salary cannot be less than 100.00!")
    @Digits(integer = 5, fraction = 2, message = "Salary must have up to 5 digits and 2 decimals!")
    private BigDecimal salary;
}
