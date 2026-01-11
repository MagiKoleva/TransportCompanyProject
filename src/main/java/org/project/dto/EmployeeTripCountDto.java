package org.project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class EmployeeTripCountDto {

    @NotBlank(message = "Employee first name cannot be blank!")
    @Pattern(regexp = "^([A-Z]).*", message = "Employee first name has to start with a capital letter!")
    private String firstName;

    @NotBlank(message = "Employee first name cannot be blank!")
    @Pattern(regexp = "^([A-Z]).*", message = "Employee first name has to start with a capital letter!")
    private String lastName;

    @Min(value = 0, message = "Value cannot be less than 0!")
    private long tripCount;
}
