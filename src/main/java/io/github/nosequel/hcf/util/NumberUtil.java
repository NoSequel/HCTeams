package io.github.nosequel.hcf.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtil {

    /**
     * Round a double to a rounded double
     *
     * @param d the double which has to be rounded
     * @param digits the amount of digits
     * @return the double
     */
    public static double round(double d, int digits) {
        if(digits < 0) {
            throw new IllegalArgumentException("Digit must be greater or equal to 0");
        }

        BigDecimal decimal = BigDecimal.valueOf(d);
        decimal = decimal.setScale(digits, RoundingMode.HALF_UP);

        return decimal.doubleValue();
    }
}