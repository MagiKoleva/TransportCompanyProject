package org.project.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VehicleCapacityValidator.class)
public @interface ValidVehicleCapacity {

    String message() default "Invalid capacity for vehicle type!";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
