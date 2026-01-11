package org.project.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@ToString
public class EmployeeIncomeReportDto {

    @NotBlank(message = "Employee first name cannot be blank!")
    @Pattern(regexp = "^([A-Z]).*", message = "Employee first name has to start with a capital letter!")
    private String firstName;

    @NotBlank(message = "Employee first name cannot be blank!")
    @Pattern(regexp = "^([A-Z]).*", message = "Employee first name has to start with a capital letter!")
    private String lastName;

    @DecimalMin(value = "1.00", message = "There cannot be price 0.00 or less!")
    @Digits(integer = 5, fraction = 2, message = "Price must have up to 5 digits and 2 decimals!")
    private BigDecimal totalPaidIncome;

    @DecimalMin(value = "1.00", message = "There cannot be price 0.00 or less!")
    @Digits(integer = 5, fraction = 2, message = "Price must have up to 5 digits and 2 decimals!")
    private BigDecimal totalUnpaidIncome;
}
