package org.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "passenger_trip")
@Getter
@Setter
@ToString(callSuper = true)
@DiscriminatorValue("passenger")
public class PassengerTrip extends Trip{
    @Column(name = "passenger_number")
    private int number;

    @Column(name = "price_per_person")
    private BigDecimal pricePerPerson;

    @Column(name = "max_number_until_increment")
    private int maxNumber;
}
