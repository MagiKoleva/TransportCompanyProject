package org.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.project.exceptions.EntityAlreadyConnectedException;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "company")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Builder
public class Company extends BaseEntity {

    @Column(name = "name")
    @NotBlank(message = "Company name cannot be blank!")
    @Size(max = 20, message = "Company name cannot be more than 20 characters!")
    @Pattern(regexp = "^([A-Z]).*", message = "Company name has to start with a capital letter!")
    private String name;

    @Column(name = "address")
    @NotBlank(message = "Address must not be empty!")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters!")
    private String address;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<Client> clients = new HashSet<>();

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<Vehicle> vehicles = new HashSet<>();

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private Set<Trip> trips = new HashSet<>();

    public void addVehicle(Vehicle vehicle) {
        if (vehicle.getCompany() != null && vehicle.getCompany() != this) {
            throw new EntityAlreadyConnectedException("Vehicle" , vehicle.getId(), "Company", this.getId());
        }

        vehicles.add(vehicle);
        vehicle.setCompany(this);
    }

    public void hireEmployee(Employee employee) {
        employees.add(employee);
        employee.setCompany(this);
    }
}
