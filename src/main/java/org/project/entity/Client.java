package org.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "client")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Builder
public class Client extends BaseEntity {

    @Column(name = "name")
    @NotBlank(message = "Client name cannot be blank!")
    @Pattern(regexp = "^([A-Z]).*", message = "Client name has to start with a capital letter!")
    private String name;

    @Column(name = "resources")
    @NotNull(message = "Client must have resources!")
    @DecimalMin(value = "0.00", message = "Client resources cannot be less than 0.00!")
    @Digits(integer = 5, fraction = 2, message = "The resources must have up to 5 digits and 2 decimals!")
    private BigDecimal resources;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = true)
    private Company company;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Trip> trips;

    //helper method to add the client to the company's Set of clients
    public void assignCompany(Company company) {
        this.company = company;
        company.getClients().add(this);
    }
}
