package org.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employee")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Builder
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
    @JoinColumn(name = "company_id", nullable = true)
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_has_qualification",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "qualification_id")
    )
    @ToString.Exclude
    @Builder.Default
    private Set<Qualification> qualifications = new HashSet<>();

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private Set<Trip> trips = new HashSet<>();
}
