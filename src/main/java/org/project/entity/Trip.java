package org.project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "trip")
@Getter
@Setter
@ToString(callSuper = true)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dType", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("trip")
public class Trip extends BaseEntity{
    @Column(name = "start_location")
    private String startLoc;

    @Column(name = "end_location")
    private String endLoc;

    @Column(name = "departure_date")
    private LocalDate departure;

    @Column(name = "arrival_date")
    private LocalDate arrival;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "is_paid")
    private boolean isPaid;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Vehicle vehicle;

    @ManyToOne
    private Company company;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Qualification qualification;
}
