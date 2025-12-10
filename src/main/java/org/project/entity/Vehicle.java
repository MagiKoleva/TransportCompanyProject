package org.project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
@ToString(callSuper = true)
public class Vehicle extends BaseEntity {
    @Column(name = "license_plate")
    private long licensePlate;

    @Column(name = "type")
    private String type;

    @Column(name = "capacity")
    private BigDecimal capacity;

    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "vehicle")
    private Set<Trip> trips;
}
