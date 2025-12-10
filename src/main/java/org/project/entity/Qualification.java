package org.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    private String name;

    @ManyToMany(mappedBy = "qualifications")
    private Set<Employee> employees;

    @OneToMany(mappedBy = "qualification")
    private Set<Trip> trips;
}
