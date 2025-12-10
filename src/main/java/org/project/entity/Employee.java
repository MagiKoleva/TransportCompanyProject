package org.project.entity;

import jakarta.persistence.*;
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
    private String fname;

    @Column(name = "last_name")
    private String lname;

    @Column(name = "salary")
    private BigDecimal salary;

    @ManyToOne
    private Company company;

    @ManyToMany
    private Set<Qualification> qualifications;

    @OneToMany(mappedBy = "employee")
    private Set<Trip> trips;
}
