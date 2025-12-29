package org.project.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.project.entity.Vehicle;
import org.project.entity.VehicleType;

import java.math.BigDecimal;

public class VehicleCapacityValidator implements ConstraintValidator<ValidVehicleCapacity, Vehicle> {

    @Override
    public boolean isValid(Vehicle vehicle, ConstraintValidatorContext context) {
        if (vehicle == null) {
            return true;
        }

        BigDecimal capacity = vehicle.getCapacity();
        VehicleType type = vehicle.getType();

        if (capacity == null || type == null) {
            return true;
        }

        if (type == VehicleType.CAR || type == VehicleType.BUS) {
            return capacity.stripTrailingZeros().scale() <= 0;
        }

        return true;
    }
}
