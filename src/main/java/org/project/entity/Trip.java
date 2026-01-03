package org.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.project.validator.DifferentLocations;
import org.project.validator.ValidTripDates;

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
@DifferentLocations(message = "Start location and end location must be different!")
@ValidTripDates(message = "Arrival date must be the same as or after departure date!")
public class Trip extends BaseEntity {

    @Column(name = "start_location")
    @NotBlank(message = "Start locations must be provided!")
    private String startLoc;

    @Column(name = "end_location")
    @NotBlank(message = "End locations must be provided!")
    private String endLoc;

    @Column(name = "departure_date")
    @NotNull(message = "Departure date must be specified!")
    @FutureOrPresent(message = "Departure date cannot be in the past!")
    private LocalDate departure;

    @Column(name = "arrival_date")
    @NotNull(message = "Arrival date must be specified!")
    @FutureOrPresent(message = "Arrival date cannot be in the past!")
    private LocalDate arrival;

    @Column(name = "price")
    @NotNull(message = "Base price must be specified!")
    @DecimalMin(value = "1.00", message = "There cannot be price 0.00 or less!")
    @Digits(integer = 5, fraction = 2, message = "Price must have up to 5 digits and 2 decimals!")
    private BigDecimal price;

    @Column(name = "is_paid")
    private boolean isPaid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qualification_id")
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private Qualification qualification;

    public BigDecimal calculateFinalPrice() {
        return price;
    }

    public void assignCompany(Company company) {
        this.company = company;
        company.getTrips().add(this);
    }

    public void assignClient(Client client) {
        this.client = client;
        client.getTrips().add(this);
    }

    public void assignEmployee(Employee employee) {
        this.employee = employee;
        employee.getTrips().add(this);
    }

    public void assignVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        vehicle.getTrips().add(this);
    }

    public void assignQualification(Qualification qualification) {
        this.qualification = qualification;
        qualification.getTrips().add(this);
    }
}
