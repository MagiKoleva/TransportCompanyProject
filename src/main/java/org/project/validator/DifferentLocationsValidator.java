package org.project.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.project.entity.Trip;

public class DifferentLocationsValidator implements ConstraintValidator<DifferentLocations, Trip> {

    @Override
    public boolean isValid(Trip trip, ConstraintValidatorContext context) {
        if (trip == null) {
            return true;
        }

        String start = trip.getStartLoc();
        String end = trip.getEndLoc();

        if (start == null || end == null) {
            return true;
        }

        return !start.equalsIgnoreCase(end);
    }
}
