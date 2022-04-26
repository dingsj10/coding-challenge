package com.ninespokes.challenge.ledger.out;

import com.ninespokes.challenge.ledger.out.annotation.CurrencyAdvice;
import com.ninespokes.challenge.ledger.out.annotation.OutputValue;
import com.ninespokes.challenge.ledger.out.annotation.PercentageAdvice;
import com.ninespokes.challenge.ledger.out.model.LedgerSummary;
import com.ninespokes.challenge.ledger.out.model.LedgerSummarySerializer;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.math.BigDecimal;

public class LedgerAnnotationTest {

    class LedgerAnnotationTestBase {
        @Override
        public String toString() {
            return LedgerSummarySerializer.serialise(this, this.getClass());
        }
    }



    @Setter
    class LedgerAnnotationTest_CurrencyCNYPrefix extends LedgerAnnotationTestBase {
        @CurrencyAdvice(prefix = "¥")
        @OutputValue
        BigDecimal value;
    }

    @Test
    public void test_CurrencyCNYPrefix() {
        LedgerAnnotationTest_CurrencyCNYPrefix data = new LedgerAnnotationTest_CurrencyCNYPrefix();
        assert "value: null".equals(data.toString());

        data.setValue(new BigDecimal("123"));
        assert "value: ¥123".equals(data.toString());

        data.setValue(new BigDecimal("-123"));
        assert "value: -¥123".equals(data.toString());
    }



    @Setter
    class LedgerAnnotationTest_CurrencyGBPSuffix extends LedgerAnnotationTestBase {
        @CurrencyAdvice(prefix = "", suffix = "£")
        @OutputValue
        BigDecimal value;
    }

    @Test
    public void test_CurrencyGBPSuffix() {
        LedgerAnnotationTest_CurrencyGBPSuffix data = new LedgerAnnotationTest_CurrencyGBPSuffix();
        assert "value: null".equals(data.toString());

        data.setValue(new BigDecimal("123"));
        assert "value: 123£".equals(data.toString());

        data.setValue(new BigDecimal("-123"));
        assert "value: -123£".equals(data.toString());
    }



    @Setter
    class LedgerAnnotationTest_CurrencyCNYPrefixHashSuffix extends LedgerAnnotationTestBase {
        @CurrencyAdvice(prefix = "¥", suffix = "#")
        @OutputValue
        BigDecimal value;
    }

    @Test
    public void test_CurrencyCNYPrefixHashSuffix() {
        LedgerAnnotationTest_CurrencyCNYPrefixHashSuffix data = new LedgerAnnotationTest_CurrencyCNYPrefixHashSuffix();
        assert "value: null".equals(data.toString());

        data.setValue(new BigDecimal("123"));
        assert "value: ¥123#".equals(data.toString());

        data.setValue(new BigDecimal("-123"));
        assert "value: -¥123#".equals(data.toString());
    }



    @Setter
    class LedgerAnnotationTest_CurrencyCNYPrefixHashSuffixPrefixAheadOfNegative extends LedgerAnnotationTestBase {
        @CurrencyAdvice(prefix = "¥", suffix = "#", prefixAheadNegative = true)
        @OutputValue
        BigDecimal value;
    }

    @Test
    public void test_CurrencyCNYPrefixHashSuffixPrefixAheadOfNegative() {
        LedgerAnnotationTest_CurrencyCNYPrefixHashSuffixPrefixAheadOfNegative data = new LedgerAnnotationTest_CurrencyCNYPrefixHashSuffixPrefixAheadOfNegative();
        assert "value: null".equals(data.toString());

        data.setValue(new BigDecimal("123"));
        assert "value: ¥123#".equals(data.toString());

        data.setValue(new BigDecimal("-123"));
        assert "value: ¥-123#".equals(data.toString());
    }



    @Setter
    class LedgerAnnotationTest_CurrencyRoundDecimalFixedRoundDown extends LedgerAnnotationTestBase {
        @CurrencyAdvice(decimal = 3, fixed = true, round = BigDecimal.ROUND_DOWN)
        @OutputValue
        BigDecimal value;
    }

    @Test
    public void test_CurrencyRoundDecimalFixedRoundDown() {
        LedgerAnnotationTest_CurrencyRoundDecimalFixedRoundDown data = new LedgerAnnotationTest_CurrencyRoundDecimalFixedRoundDown();
        data.setValue(new BigDecimal("0.12345678"));
        assert "value: $0.123".equals(data.toString());

        data.setValue(new BigDecimal("12.345678"));
        assert "value: $12.345".equals(data.toString());

        data.setValue(new BigDecimal("123.45"));
        assert "value: $123.450".equals(data.toString());

        data.setValue(new BigDecimal("0.12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        assert "value: $0.123".equals(data.toString());

        data.setValue(new BigDecimal("0"));
        assert "value: $0.000".equals(data.toString());

        data.setValue(null);
        assert "value: null".equals(data.toString());

        data.setValue(new BigDecimal("-12.345678"));
        assert "value: -$12.345".equals(data.toString());

        data.setValue(new BigDecimal("-0.12345678"));
        assert "value: -$0.123".equals(data.toString());

        data.setValue(new BigDecimal("-0.12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        assert "value: -$0.123".equals(data.toString());

        data.setValue(new BigDecimal("-0.12"));
        assert "value: -$0.120".equals(data.toString());

        data.setValue(new BigDecimal(Double.MIN_VALUE));
        assert "value: $0.000".equals(data.toString());

    }



    @Setter
    class LedgerAnnotationTest_CurrencyRoundDecimalUnfixedRoundDown extends LedgerAnnotationTestBase {
        @CurrencyAdvice(decimal = 3, fixed = false, round = BigDecimal.ROUND_DOWN)
        @OutputValue
        BigDecimal value;
    }

    @Test
    public void test_CurrencyRoundDecimalUnfixedRoundDown() {
        LedgerAnnotationTest_CurrencyRoundDecimalUnfixedRoundDown data = new LedgerAnnotationTest_CurrencyRoundDecimalUnfixedRoundDown();
        data.setValue(new BigDecimal("0.12345678"));
        assert "value: $0.123".equals(data.toString());

        data.setValue(new BigDecimal("12.345678"));
        assert "value: $12.345".equals(data.toString());

        data.setValue(new BigDecimal("123.45"));
        assert "value: $123.45".equals(data.toString());

        data.setValue(new BigDecimal("0.12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        assert "value: $0.123".equals(data.toString());

        data.setValue(new BigDecimal("0"));
        assert "value: $0".equals(data.toString());

        data.setValue(null);
        assert "value: null".equals(data.toString());

        data.setValue(new BigDecimal("-12.345678"));
        assert "value: -$12.345".equals(data.toString());

        data.setValue(new BigDecimal("-0.12345678"));
        assert "value: -$0.123".equals(data.toString());

        data.setValue(new BigDecimal("-0.12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        assert "value: -$0.123".equals(data.toString());

        data.setValue(new BigDecimal("-0.12"));
        assert "value: -$0.12".equals(data.toString());

        data.setValue(new BigDecimal(Double.MIN_VALUE));
        assert "value: $0".equals(data.toString());

    }



    @Setter
    class LedgerAnnotationTest_CurrencyNegativeRoundUpPositiveRoundDown extends LedgerAnnotationTestBase {
        @CurrencyAdvice(decimal = 3, fixed = true, round = BigDecimal.ROUND_DOWN, roundNegative = BigDecimal.ROUND_UP)
        @OutputValue
        BigDecimal value;
    }

    @Test
    public void test_CurrencyNegativeRoundUpPositiveRoundDown() {
        LedgerAnnotationTest_CurrencyNegativeRoundUpPositiveRoundDown data = new LedgerAnnotationTest_CurrencyNegativeRoundUpPositiveRoundDown();
        data.setValue(new BigDecimal("0.12345678"));
        assert "value: $0.123".equals(data.toString());

        data.setValue(new BigDecimal("12.345678"));
        assert "value: $12.345".equals(data.toString());

        data.setValue(new BigDecimal("123.45"));
        assert "value: $123.450".equals(data.toString());

        data.setValue(new BigDecimal("0.12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        assert "value: $0.123".equals(data.toString());

        data.setValue(new BigDecimal("0"));
        assert "value: $0.000".equals(data.toString());

        data.setValue(null);
        assert "value: null".equals(data.toString());

        data.setValue(new BigDecimal("-12.345678"));
        assert "value: -$12.346".equals(data.toString());

        data.setValue(new BigDecimal("-0.12345678"));
        assert "value: -$0.124".equals(data.toString());

        data.setValue(new BigDecimal("-0.12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        assert "value: -$0.124".equals(data.toString());

        data.setValue(new BigDecimal("-0.12"));
        assert "value: -$0.120".equals(data.toString());

        data.setValue(new BigDecimal(Double.MIN_VALUE));
        assert "value: $0.000".equals(data.toString());

    }



    @Setter
    class LedgerAnnotationTest_CurrencyRoundHalfEven extends LedgerAnnotationTestBase {
        @CurrencyAdvice(decimal = 3, fixed = true, round = BigDecimal.ROUND_HALF_EVEN)
        @OutputValue
        BigDecimal value;
    }

    @Test
    public void test_CurrencyRoundHalfEven() {
        LedgerAnnotationTest_CurrencyRoundHalfEven data = new LedgerAnnotationTest_CurrencyRoundHalfEven();
        data.setValue(new BigDecimal("0.1234"));
        assert "value: $0.123".equals(data.toString());

        data.setValue(new BigDecimal("0.1236"));
        assert "value: $0.124".equals(data.toString());

        data.setValue(new BigDecimal("0.1285"));
        assert "value: $0.128".equals(data.toString());

        data.setValue(new BigDecimal("0.1215"));
        assert "value: $0.122".equals(data.toString());

        data.setValue(new BigDecimal("-0.1285"));
        assert "value: -$0.128".equals(data.toString());

        data.setValue(new BigDecimal("-0.1215"));
        assert "value: -$0.122".equals(data.toString());
    }



    @Setter
    class LedgerAnnotationTest_PercentagePerMill extends LedgerAnnotationTestBase {
        @PercentageAdvice(suffix = "‱", coefficient = 1000, fixed = true, decimal = 3, round = BigDecimal.ROUND_DOWN)
        @OutputValue
        BigDecimal value;
    }

    @Test
    public void test_PercentagePerMill() {
        LedgerAnnotationTest_PercentagePerMill data = new LedgerAnnotationTest_PercentagePerMill();
        assert "value: null".equals(data.toString());

        data.setValue(new BigDecimal("0.1234567"));
        assert "value: 123.456‱".equals(data.toString());
    }



    @Setter
    class LedgerAnnotationTest_PercentagePPM extends LedgerAnnotationTestBase {
        @PercentageAdvice(suffix = "ppm", coefficient = 1000000l, fixed = true, decimal = 3, round = BigDecimal.ROUND_DOWN)
        @OutputValue
        BigDecimal value;
    }

    @Test
    public void test_PercentagePPM() {
        LedgerAnnotationTest_PercentagePPM data = new LedgerAnnotationTest_PercentagePPM();
        assert "value: null".equals(data.toString());

        data.setValue(new BigDecimal("0.12345678901234567890"));
        assert "value: 123456.789ppm".equals(data.toString());
    }



    @Setter
    class LedgerAnnotationTest_PercentagePrefix extends LedgerAnnotationTestBase {
        @PercentageAdvice(prefix="%", suffix = "", decimal = 1)
        @OutputValue
        BigDecimal value;
    }

    @Test
    public void test_PercentagePrefix() {
        LedgerAnnotationTest_PercentagePrefix data = new LedgerAnnotationTest_PercentagePrefix();
        assert "value: null".equals(data.toString());

        data.setValue(new BigDecimal("0.12345678901234567890"));
        assert "value: %12.3".equals(data.toString());

        data.setValue(new BigDecimal("-0.12345678901234567890"));
        assert "value: -%12.3".equals(data.toString());
    }



    @Setter
    class LedgerAnnotationTest_PercentagePrefixAheadOfNegative extends LedgerAnnotationTestBase {
        @PercentageAdvice(prefix="%", suffix = "", prefixAheadNegative = true, decimal = 1)
        @OutputValue
        BigDecimal value;
    }

    @Test
    public void test_PercentagePrefixAheadOfNegative() {
        LedgerAnnotationTest_PercentagePrefixAheadOfNegative data = new LedgerAnnotationTest_PercentagePrefixAheadOfNegative();
        assert "value: null".equals(data.toString());

        data.setValue(new BigDecimal("0.12345678901234567890"));
        assert "value: %12.3".equals(data.toString());

        data.setValue(new BigDecimal("-0.12345678901234567890"));
        assert "value: %-12.3".equals(data.toString());
    }



    @Setter
    class LedgerAnnotationTest_PercentageRoundDecimalFixedRoundDown extends LedgerAnnotationTestBase {
        @PercentageAdvice(decimal = 3, fixed = true, round = BigDecimal.ROUND_DOWN)
        @OutputValue
        BigDecimal value;
    }

    @Test
    public void test_PercentageRoundDecimalFixedRoundDown() {
        LedgerAnnotationTest_PercentageRoundDecimalFixedRoundDown data = new LedgerAnnotationTest_PercentageRoundDecimalFixedRoundDown();
        data.setValue(new BigDecimal("0.12345678"));
        assert "value: 12.345%".equals(data.toString());

        data.setValue(new BigDecimal("12.345678"));
        assert "value: 1234.567%".equals(data.toString());

        data.setValue(new BigDecimal("123.45"));
        assert "value: 12345.000%".equals(data.toString());

        data.setValue(new BigDecimal("0.12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        assert "value: 12.345%".equals(data.toString());

        data.setValue(new BigDecimal("0"));
        assert "value: 0.000%".equals(data.toString());

        data.setValue(null);
        assert "value: null".equals(data.toString());

        data.setValue(new BigDecimal("-12.345678"));
        assert "value: -1234.567%".equals(data.toString());

        data.setValue(new BigDecimal("-0.12345678"));
        assert "value: -12.345%".equals(data.toString());

        data.setValue(new BigDecimal("-0.12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        assert "value: -12.345%".equals(data.toString());

        data.setValue(new BigDecimal("-0.12"));
        assert "value: -12.000%".equals(data.toString());

        data.setValue(new BigDecimal(Double.MIN_VALUE));
        assert "value: 0.000%".equals(data.toString());

    }



    @Setter
    class LedgerAnnotationTest_PercentageRoundDecimalUnfixedRoundDown extends LedgerAnnotationTestBase {
        @PercentageAdvice(decimal = 3, fixed = false, round = BigDecimal.ROUND_DOWN)
        @OutputValue
        BigDecimal value;
    }

    @Test
    public void test_PercentageRoundDecimalUnfixedRoundDown() {
        LedgerAnnotationTest_PercentageRoundDecimalUnfixedRoundDown data = new LedgerAnnotationTest_PercentageRoundDecimalUnfixedRoundDown();
        data.setValue(new BigDecimal("0.12345678"));
        assert "value: 12.345%".equals(data.toString());

        data.setValue(new BigDecimal("12.345678"));
        assert "value: 1234.567%".equals(data.toString());

        data.setValue(new BigDecimal("123.456"));
        assert "value: 12345.6%".equals(data.toString());

        data.setValue(new BigDecimal("0.12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        assert "value: 12.345%".equals(data.toString());

        data.setValue(new BigDecimal("0"));
        assert "value: 0%".equals(data.toString());

        data.setValue(null);
        assert "value: null".equals(data.toString());

        data.setValue(new BigDecimal("-12.3456"));
        assert "value: -1234.56%".equals(data.toString());

        data.setValue(new BigDecimal("-0.12345678"));
        assert "value: -12.345%".equals(data.toString());

        data.setValue(new BigDecimal("-0.12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        assert "value: -12.345%".equals(data.toString());

        data.setValue(new BigDecimal("-0.12"));
        assert "value: -12%".equals(data.toString());

        data.setValue(new BigDecimal(Double.MIN_VALUE));
        assert "value: 0%".equals(data.toString());

    }



    @Setter
    class LedgerAnnotationTest_PercentageNegativeRoundUpPositiveRoundDown extends LedgerAnnotationTestBase {
        @PercentageAdvice(decimal = 3, fixed = true, round = BigDecimal.ROUND_DOWN, roundNegative = BigDecimal.ROUND_UP)
        @OutputValue
        BigDecimal value;
    }

    @Test
    public void test_PercentageNegativeRoundUpPositiveRoundDown() {
        LedgerAnnotationTest_PercentageNegativeRoundUpPositiveRoundDown data = new LedgerAnnotationTest_PercentageNegativeRoundUpPositiveRoundDown();
        data.setValue(new BigDecimal("0.12345678"));
        assert "value: 12.345%".equals(data.toString());

        data.setValue(new BigDecimal("12.345678"));
        assert "value: 1234.567%".equals(data.toString());

        data.setValue(new BigDecimal("123.456"));
        assert "value: 12345.600%".equals(data.toString());

        data.setValue(new BigDecimal("0.12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        assert "value: 12.345%".equals(data.toString());

        data.setValue(new BigDecimal("0"));
        assert "value: 0.000%".equals(data.toString());

        data.setValue(null);
        assert "value: null".equals(data.toString());

        data.setValue(new BigDecimal("-12.3456"));
        assert "value: -1234.560%".equals(data.toString());

        data.setValue(new BigDecimal("-0.12345678"));
        assert "value: -12.346%".equals(data.toString());

        data.setValue(new BigDecimal("-0.12345378901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        assert "value: -12.346%".equals(data.toString());

        data.setValue(new BigDecimal("-0.12"));
        assert "value: -12.000%".equals(data.toString());

        data.setValue(new BigDecimal(Double.MIN_VALUE));
        assert "value: 0.000%".equals(data.toString());

    }



    @Setter
    class LedgerAnnotationTest_PercentageRoundHalfEven extends LedgerAnnotationTestBase {
        @PercentageAdvice(decimal = 3, fixed = true, round = BigDecimal.ROUND_HALF_EVEN)
        @OutputValue
        BigDecimal value;
    }

    @Test
    public void test_PercentageRoundHalfEven() {
        LedgerAnnotationTest_PercentageRoundHalfEven data = new LedgerAnnotationTest_PercentageRoundHalfEven();
        data.setValue(new BigDecimal("0.111234"));
        assert "value: 11.123%".equals(data.toString());

        data.setValue(new BigDecimal("0.111236"));
        assert "value: 11.124%".equals(data.toString());

        data.setValue(new BigDecimal("0.111285"));
        assert "value: 11.128%".equals(data.toString());

        data.setValue(new BigDecimal("0.111215"));
        assert "value: 11.122%".equals(data.toString());

        data.setValue(new BigDecimal("-0.111285"));
        assert "value: -11.128%".equals(data.toString());

        data.setValue(new BigDecimal("-0.111215"));
        assert "value: -11.122%".equals(data.toString());
    }

}
