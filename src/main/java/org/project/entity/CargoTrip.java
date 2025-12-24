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
@Table(name = "cargo_trip")
@Getter
@Setter
@ToString(callSuper = true)
@DiscriminatorValue("cargo")
public class CargoTrip extends Trip {
    private BigDecimal weight;

    @Column(name = "percent_increment")
    private BigDecimal percent;
}
