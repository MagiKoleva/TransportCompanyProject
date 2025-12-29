package org.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "qualification")
@Getter
@Setter
@ToString(callSuper = true)
public class Qualification extends BaseEntity {

    @NotBlank(message = "Qualification must be specified!")
    private String name;

    @ManyToMany(mappedBy = "qualifications", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Employee> employees;

    @OneToMany(mappedBy = "qualification",  fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Trip> trips;
}
