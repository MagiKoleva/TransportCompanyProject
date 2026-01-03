package org.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@ToString
public class CompanyIncomeDto {
    private String name;
    private BigDecimal income;
}
