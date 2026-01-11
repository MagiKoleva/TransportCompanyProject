package org.project.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@ToString
public class TripExportDto {

    @NotNull(message = "There cannot be no ID!")
    private long id;

    @NotBlank(message = "Trip type must be provided!")
    private String tripType;

    @NotBlank(message = "Start location must be provided!")
    private String startLoc;

    @NotBlank(message = "End location must be provided!")
    private String endLoc;

    @NotNull(message = "Price must be specified!")
    @DecimalMin(value = "1.00", message = "There cannot be price 0.00 or less!")
    @Digits(integer = 5, fraction = 2, message = "Price must have up to 5 digits and 2 decimals!")
    private BigDecimal finalPrice;

    @NotNull(message = "Departure date must be specified!")
    @FutureOrPresent(message = "Departure date cannot be in the past!")
    private LocalDate departure;

    @NotNull(message = "Arrival date must be specified!")
    @FutureOrPresent(message = "Arrival date cannot be in the past!")
    private LocalDate arrival;

    @NotBlank(message = "Driver name must be provided! Trip cannot be without a driver.")
    private String driverName;

    @NotBlank(message = "Vehicle must be provided! Trip must have a vehicle.")
    private String vehicleType;

    private boolean paid;
}
