package org.project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "client")
@Getter
@Setter
@ToString(callSuper = true)
public class Client extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "resources")
    private BigDecimal resources;

    @ManyToOne
    private Company company;
}
