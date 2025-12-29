package org.project.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.project.entity.Trip;

import java.time.LocalDate;

public class ValidTripDatesValidator implements ConstraintValidator<ValidTripDates, Trip> {

    @Override
    public boolean isValid(Trip trip, ConstraintValidatorContext context) {
        if (trip == null) {
            return true;
        }

        LocalDate departure = trip.getDeparture();
        LocalDate arrival = trip.getArrival();

        if (departure == null || arrival == null) {
            return true;
        }

        return !arrival.isBefore(departure);
    }
}
