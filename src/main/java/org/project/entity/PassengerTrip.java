package org.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "passenger_trip")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@DiscriminatorValue("passenger")
public class PassengerTrip extends Trip {

    @Column(name = "passenger_number")
    @NotNull(message = "Passenger number is required!")
    @DecimalMin(value = "1", message = "Passenger number cannot be less than 1!")
    private int number;

    @Column(name = "price_per_person")
    @DecimalMin(value = "0.01", message = "Price cannot be 0.00 or less!")
    @Digits(integer = 5, fraction = 2, message = "Price must have up to 5 digits and 2 decimals!")
    private BigDecimal pricePerPerson;

    @Column(name = "max_number_until_increment")
    @NotNull(message = "Number until increment must be provided!")
    private int maxNumber;

    @Override
    public BigDecimal calculateFinalPrice() {
        BigDecimal result;

        if (number <= maxNumber) {
            result = getPrice();
        } else {
            result = getPrice().add(
                    pricePerPerson.multiply(BigDecimal.valueOf(number - maxNumber)));
        }

        return result.setScale(2, RoundingMode.HALF_UP);


//        if (number > maxNumber) {
//            return getPrice().add(
//                    pricePerPerson.multiply(BigDecimal.valueOf(number - maxNumber))
//            );
//        }
//        return getPrice();
    }
}
