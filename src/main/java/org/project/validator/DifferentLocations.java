package org.project.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DifferentLocationsValidator.class)
public @interface DifferentLocations {

    String message() default "Invalid location(s)!";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
