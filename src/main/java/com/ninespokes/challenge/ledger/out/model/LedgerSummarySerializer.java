package com.ninespokes.challenge.ledger.out.model;

import com.ninespokes.challenge.exception.InvalidValueException;
import com.ninespokes.challenge.ledger.out.annotation.CurrencyAdvice;
import com.ninespokes.challenge.ledger.out.annotation.OutputValue;
import com.ninespokes.challenge.ledger.out.annotation.PercentageAdvice;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Setter
@Slf4j
public class LedgerSummarySerializer {

    public static String serialise(Object instance, Class clazz) {
        List<String> items = new ArrayList<>();
        Field[] allFields = clazz.getDeclaredFields();
        for (Field field : allFields) {
            OutputValue outputValue = field.getAnnotation(OutputValue.class);
            if (outputValue == null) {
                continue;
            }
            String item = (StringUtils.isEmpty(outputValue.value()) ? field.getName() : outputValue.value())
                    + ": " + serialiseFieldValue(field, instance);
            items.add(item);
        }
        return StringUtils.join(items, "\n");
    }

    public static Object serialiseFieldValue(Field field, Object instance) {
        Object object = null;
        try {
            field.setAccessible(true);
            object = field.get(instance);
        } catch (IllegalAccessException e) {
            log.error("Failed to get value of field {}", field.getName());
            return null;
        }

        // for non big decimal or null values, return directly.
        if (field.getGenericType() != BigDecimal.class || object == null || !(object instanceof BigDecimal)) {
            return object;
        }

        // for big decimal values
        BigDecimal value = (BigDecimal) object;
        boolean negative = value.compareTo(BigDecimal.ZERO) < 0;

        CurrencyAdvice currencyAdvice = null;
        PercentageAdvice percentageAdvice = null;

        if ((currencyAdvice = field.getAnnotation(CurrencyAdvice.class)) != null) {
            // render as currency if Currency annotation is added.
            int roundType = negative && currencyAdvice.roundNegative() != -1 ? currencyAdvice.roundNegative() : currencyAdvice.round();
            BigDecimal currencyValue = roundType == BigDecimal.ROUND_UNNECESSARY ? value : value.setScale(currencyAdvice.decimal(), roundType);
            return serialiseCurrency(currencyAdvice, currencyValue);
        } else if ((percentageAdvice = field.getAnnotation(PercentageAdvice.class)) != null) {
            // render as percentage if Percentage annotation is added.
            int roundType = negative && percentageAdvice.roundNegative() != -1 ? percentageAdvice.roundNegative() : percentageAdvice.round();
            BigDecimal percentageValue = value.multiply(new BigDecimal(percentageAdvice.coefficient()));
            percentageValue = roundType == BigDecimal.ROUND_UNNECESSARY ? percentageValue : percentageValue.setScale(percentageAdvice.decimal(), roundType);
            return serialisePercentage(percentageAdvice, percentageValue);
        } else {
            // render as regular BigDecimal
            return value.toPlainString();
        }
    }

    /**
     * Build formatter as per settings in {@link CurrencyAdvice}, like "#,###.0"
     * Then format BigDecimal to String
     * Note for negative values, we should leave the '-' ahead of the currency symbol
     * @param currencyAdvice settings about how currency should be serialised.
     * @param value          the currency value to be formatted.
     * @return               formatted currency value as String
     */
    private static String serialiseCurrency(CurrencyAdvice currencyAdvice, BigDecimal value) {
        if (value == null) {
            return null;
        }
        if (currencyAdvice.decimal() < 0) {
            log.error("Invalid decimal({}) for generating currency format", currencyAdvice.decimal());
            throw new InvalidValueException("Invalid decimal for generating currency format: " + currencyAdvice.decimal());
        }

        String pattern = currencyAdvice.decimal() > 0 ? "#,##0." : "#,###";
        for (int i = 0; i< currencyAdvice.decimal() ; i++) {
            pattern += currencyAdvice.fixed() ? "0" : "#";
        }

        return (!currencyAdvice.prefixAheadNegative() ? (value.compareTo(BigDecimal.ZERO) < 0 ? "-" : "") : "")
                + currencyAdvice.prefix()
                + (currencyAdvice.prefixAheadNegative() ? (value.compareTo(BigDecimal.ZERO) < 0 ? "-" : "") : "")
                + new DecimalFormat(pattern).format(value.abs())
                + currencyAdvice.suffix();
    }

    /**
     *
     * Build formatter as per settings in {@link PercentageAdvice}, like "#.0"
     * Then format BigDecimal to String
     * @param percentageAdvice percentage annotation where the settings are
     * @param value            the decimal value to be formatted.
     * @return                 formatted percentage value as String
     */
    private static String serialisePercentage(PercentageAdvice percentageAdvice, BigDecimal value) {
        if (value == null) {
            return null;
        }
        if (percentageAdvice.decimal() < 0) {
            log.error("Invalid decimal({}) for generating currency format", percentageAdvice.decimal());
            throw new InvalidValueException("Invalid decimal for generating percentage format: " + percentageAdvice.decimal());
        }

        String pattern = percentageAdvice.decimal() > 0 ? "0." : "0";
        for (int i = 0; i< percentageAdvice.decimal() ; i++) {
            pattern += percentageAdvice.fixed() ? "0" : "#";
        }

        return (!percentageAdvice.prefixAheadNegative() ? (value.compareTo(BigDecimal.ZERO) < 0 ? "-" : "") : "")
                + percentageAdvice.prefix()
                + (percentageAdvice.prefixAheadNegative() ? (value.compareTo(BigDecimal.ZERO) < 0 ? "-" : "") : "")
                + new DecimalFormat(pattern).format(value.abs())
                + percentageAdvice.suffix();
    }

    public static boolean isDivideOrMultiplyResultNegative(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) {
            return false;
        }
        return (a.compareTo(BigDecimal.ZERO) > 0 && b.compareTo(BigDecimal.ZERO) < 0)
                || (a.compareTo(BigDecimal.ZERO) < 0 && b.compareTo(BigDecimal.ZERO) > 0);
    }

    public static PercentageAdvice getPercentageAdviceOfFieldInClass(String fieldName, Class clazz) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            PercentageAdvice advice = field.getAnnotation(PercentageAdvice.class);
            if (advice == null) {
                log.error("No percentage advice could be found for field {}", fieldName);
                throw new InvalidValueException("No percentage advice could be found for field: " + fieldName);
            }
            return advice;
        } catch (NoSuchFieldException e) {
            log.error("No such field with name {}", fieldName);
            throw new InvalidValueException("No such field with name: " + fieldName);
        }
    }

    public static CurrencyAdvice getCurrencyAdviceOfFieldInClass(String fieldName, Class clazz) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            CurrencyAdvice advice = field.getAnnotation(CurrencyAdvice.class);
            if (advice == null) {
                log.error("No currency advice could be found for field {}", fieldName);
                throw new InvalidValueException("No currency advice could be found for field: " + fieldName);
            }
            return advice;
        } catch (NoSuchFieldException e) {
            log.error("No such field with name {}", fieldName);
            throw new InvalidValueException("No such field with name: " + fieldName);
        }
    }
}
