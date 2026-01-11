package org.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "cargo_trip")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@DiscriminatorValue("cargo")
@SuperBuilder
public class CargoTrip extends Trip {

    @Column(name = "weight")
    @NotNull(message = "Weight is required!")
    @DecimalMin(value = "0.01", message = "Weight must be greater than 0!")
    @DecimalMax(value = "100000.00", message = "Weight should be realistically entered!")
    @Digits(integer = 8, fraction = 2, message = "Weight must have up to 8 digits and 2 decimals!")
    private BigDecimal weight;

    @Column(name = "percent_increment")
    @DecimalMin(value = "0.00", message = "Percentage cannot be less than 0.00%!")
    @DecimalMax(value = "100.00", message = "Percentage cannot be more than 100.00%!")
    @Digits(integer = 3, fraction = 2, message = "Percentage must have up to 3 digits and 2 decimals!")
    private BigDecimal percent;

    @Override
    public BigDecimal calculateFinalPrice() {
        BigDecimal result = getPrice();

        if (this instanceof CargoTrip cargo) {
            BigDecimal increment = cargo.getPrice()
                    .multiply(cargo.getPercent())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            result = result.add(increment);
        }

        return result.setScale(2, RoundingMode.HALF_UP);

//        return getPrice().add(
//                getPrice().multiply(percent).divide(BigDecimal.valueOf(100))
//        );
    }
}
