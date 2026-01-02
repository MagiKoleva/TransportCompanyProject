package org.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "qualification")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Builder
public class Qualification extends BaseEntity {

    @NotBlank(message = "Qualification must be specified!")
    private String name;

    @ManyToMany(mappedBy = "qualifications", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "qualification",  fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Trip> trips;
}
