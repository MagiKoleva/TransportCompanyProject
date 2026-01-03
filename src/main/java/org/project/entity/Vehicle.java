package org.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.project.validator.ValidVehicleCapacity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vehicle")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@ValidVehicleCapacity
public class Vehicle extends BaseEntity {

    @Column(name = "license_plate")
    @NotBlank(message = "License plate must be provided!")
    @Pattern(regexp = "[A-Z]{2}\\d{4}[A-Z]{2}",
            message = "Value must be the following format: AB1234CD!")
    private String licensePlate;

    @NotBlank(message = "Vehicle type must be provided!")
    @Enumerated(EnumType.STRING)
    private VehicleType type;

    @Column(name = "capacity")
    @NotNull(message = "Vehicle capacity must be provided!")
    @Digits(integer = 5, fraction = 2, message = "Capacity must have up to 5 digits and 2 decimals!")
    private BigDecimal capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private Set<Trip> trips = new HashSet<>();
}
