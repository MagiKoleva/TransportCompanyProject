package org.project.exceptions;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(BigDecimal available, BigDecimal required) {
        super("Insufficient funds! Available: " + available + ", required: " + required);
    }
}
