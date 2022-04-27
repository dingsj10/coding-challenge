package com.ninespokes.challenge.ledger.out.annotation;

import java.lang.annotation.*;
import java.math.BigDecimal;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PercentageAdvice {

    /**
     * Number of decimals to remain
     */
    int decimal() default 1;

    /**
     * Fix number of decimals, e.g. Value to be serialised is 12.3, decimal=2
     * true  - 12.30
     * false - 12.3
     */
    boolean fixed() default true;

    /**
     * Round type for non negative values, default ROUND_HALF_EVEN as of being used in most financial systems.
     */
    int round() default BigDecimal.ROUND_HALF_EVEN;

    /**
     * Special round type for negative values
     */
    int roundNegative() default -1;

    /**
     * Percentage prefix, e.g. %, ‰... please remember to set a corresponding coefficient
     */
    String prefix() default "";

    /**
     * Percentage suffix, e.g. %, ‰... please remember to set a corresponding coefficient
     */
    String suffix() default "%";

    /**
     * Coefficient to match suffix, support numbers of 10^x only
     */
    long coefficient() default 100;

    /**
     * For number like -0.01
     * true   %-1.0
     * false  -%1.0
     */
    boolean prefixAheadNegative() default false;

}
