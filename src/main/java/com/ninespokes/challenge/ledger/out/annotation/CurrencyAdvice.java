package com.ninespokes.challenge.ledger.out.annotation;

import java.lang.annotation.*;
import java.math.BigDecimal;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CurrencyAdvice {

    /**
     * Number of decimals to remain
     */
    int decimal() default 0;

    /**
     * Fix number of decimals, e.g. Value to be serialised is 12.3, decimal=2
     * true  - 12.30
     * false - 12.3
     */
    boolean fixed() default true;

    /**
     * Round type for non negative values, default ROUND_DOWN to wipe decimals
     */
    int round() default BigDecimal.ROUND_DOWN;

    /**
     * Special round type for negative values
     */
    int roundNegative() default -1;

    /**
     * Currency prefix, e.g. $, ¥, €, £,...
     */
    String prefix() default "$";

    /**
     * Currency suffix
     */
    String suffix() default "";

    /**
     * For number like -1
     * true   $-1
     * false  -$1
     */
    boolean prefixAheadNegative() default false;

}
