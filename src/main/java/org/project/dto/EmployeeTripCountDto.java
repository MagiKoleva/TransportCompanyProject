package org.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class EmployeeTripCountDto {

    private String firstName;
    private String lastName;
    private long tripCount;
}
