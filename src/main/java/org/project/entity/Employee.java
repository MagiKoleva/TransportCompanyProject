package org.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "employee")
@Getter
@Setter
@ToString(callSuper = true)
public class Employee extends BaseEntity {

    @Column(name = "first_name")
    @NotBlank(message = "Employee first name cannot be blank!")
    @Pattern(regexp = "^([A-Z]).*", message = "Employee first name has to start with a capital letter!")
    private String fname;

    @Column(name = "last_name")
    @NotBlank(message = "Employee last name cannot be blank!")
    @Pattern(regexp = "^([A-Z]).*", message = "Employee last name has to start with a capital letter!")
    private String lname;

    @Column(name = "salary")
    @NotNull(message = "Employee must have a salary!")
    @DecimalMin(value = "100.00", message = "Salary cannot be less than 100.00!")
    @Digits(integer = 5, fraction = 2, message = "Salary must have up to 5 digits and 2 decimals!")
    private BigDecimal salary;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Qualification> qualifications;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Trip> trips;
}
