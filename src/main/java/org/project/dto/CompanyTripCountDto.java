package org.project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CompanyTripCountDto {

    @NotBlank(message = "Company name cannot be blank!")
    @Size(max = 20, message = "Company name cannot be more than 20 characters!")
    @Pattern(regexp = "^([A-Z]).*", message = "Company name has to start with a capital letter!")
    private String companyName;

    @Min(value = 0, message = "Value cannot be less than 0!")
    private long tripCount;
}
