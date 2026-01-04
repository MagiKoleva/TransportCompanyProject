package org.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@ToString
public class CompanyTripPaymentReportDto {
    private String companyName;
    private BigDecimal totalFinalPaidSum;
    private BigDecimal totalUnpaidSum;
}
