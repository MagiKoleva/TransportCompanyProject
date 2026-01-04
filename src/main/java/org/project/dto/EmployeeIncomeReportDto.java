package org.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@ToString
public class EmployeeIncomeReportDto {

    private String firstName;
    private String lastName;
    private BigDecimal totalPaidIncome;
    private BigDecimal totalUnpaidIncome;
}
