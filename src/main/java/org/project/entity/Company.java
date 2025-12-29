package org.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    private Set<Client> clients;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Vehicle> vehicles;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Employee> employees;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Trip> trips;
}
