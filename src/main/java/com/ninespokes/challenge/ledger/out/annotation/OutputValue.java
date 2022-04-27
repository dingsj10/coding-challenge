package com.ninespokes.challenge.ledger.out.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OutputValue {

    String value() default "";

}
