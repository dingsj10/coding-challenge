package com.ninespokes.challenge.ledger.out;

import com.ninespokes.challenge.ledger.out.model.LedgerSummary;
import org.junit.Test;

import java.math.BigDecimal;

public class LedgerCalculatorTest {

    private static final BigDecimal testDataPositive = new BigDecimal("100000000000");
    private static final BigDecimal testDataNegative = new BigDecimal("-100000000000");
    @Test
    public void test_Revenue() {
        LedgerSummary result = new LedgerSummary(true);
        for (int i = 0; i < 100; i++) {
            result.addRevenue(testDataPositive);
        }
        assert testDataPositive.multiply(new BigDecimal(100)).compareTo(result.getRevenue()) == 0;
    }

    @Test
    public void test_Expenses() {
        LedgerSummary result = new LedgerSummary(true);
        for (int i = 0; i < 100; i++) {
            result.addExpenses(testDataPositive);
        }
        assert testDataPositive.multiply(new BigDecimal(100)).compareTo(result.getExpenses()) == 0;
    }

    @Test
    public void test_GrossProfitMarginRealTime() {
        LedgerSummary data = new LedgerSummary(true);
        for (int i = 0; i < 100; i++) {
            if (i % 4 == 0)
                data.addGrossProfit(testDataPositive);
            if (i % 3 == 0)
                data.addRevenue(testDataPositive);
        }
        assert new BigDecimal((100 - 1) / 4 + 1)
                .divide(new BigDecimal((100 - 1) / 3 + 1), 3, BigDecimal.ROUND_HALF_EVEN)
                .compareTo(data.getGrossProfitMargin()) == 0;
    }

    @Test
    public void test_GrossProfitMarginFinally() {
        LedgerSummary data = new LedgerSummary(false);
        for (int i = 0; i < 100; i++) {
            if (i%7 == 0)
                data.addGrossProfit(testDataPositive);
            if (i%3 == 0)
                data.addRevenue(testDataPositive);
        }
        data.updateCalculatedFields();
        assert new BigDecimal((100-1)/7+1)
                .divide(new BigDecimal((100-1)/3+1), 3, BigDecimal.ROUND_HALF_EVEN)
                .compareTo(data.getGrossProfitMargin()) == 0;
    }

    @Test
    public void test_NetProfitMarginRealTime() {
        LedgerSummary data = new LedgerSummary(true);
        for (int i = 0; i < 100; i++) {
            if (i%5 == 0)
                data.addRevenue(testDataPositive);
            if (i%3 == 0)
                data.addExpenses(testDataPositive);
        }
        assert new BigDecimal((100-1)/5+1).multiply(testDataPositive)
                .subtract(new BigDecimal((100-1)/3+1).multiply(testDataPositive))
                .divide(new BigDecimal((100-1)/5+1).multiply(testDataPositive), 3, BigDecimal.ROUND_HALF_EVEN)
                .compareTo(data.getNetProfitMargin()) == 0;
    }

    @Test
    public void test_NetProfitMarginFinally() {
        LedgerSummary data = new LedgerSummary(false);
        for (int i = 0; i < 100; i++) {
            if (i%5 == 0)
                data.addRevenue(testDataPositive);
            if (i%4 == 0)
                data.addExpenses(testDataPositive);
        }
        data.updateCalculatedFields();
        assert new BigDecimal((100-1)/5+1).multiply(testDataPositive)
                .subtract(new BigDecimal((100-1)/4+1).multiply(testDataPositive))
                .divide(new BigDecimal((100-1)/5+1).multiply(testDataPositive), 3, BigDecimal.ROUND_HALF_EVEN)
                .compareTo(data.getNetProfitMargin()) == 0;
    }

    @Test
    public void test_WorkingCapitalRatioRealTime() {
        LedgerSummary data = new LedgerSummary(true);
        for (int i = 0; i < 100; i++) {
            if (i%5 == 0)
                data.addAssets(testDataNegative);
            if (i%4 == 0)
                data.addAssets(testDataPositive);
            if (i%3 == 0)
                data.addLiabilities(testDataNegative);
            if (i%2 == 0)
                data.addLiabilities(testDataPositive);
        }

        assert new BigDecimal((100-1)/5+1).multiply(testDataNegative)
                .add(new BigDecimal((100-1)/4+1).multiply(testDataPositive))
                .divide(new BigDecimal((100-1)/3+1).multiply(testDataNegative)
                         .add(new BigDecimal((100-1)/2+1).multiply(testDataPositive)),
                        3, BigDecimal.ROUND_HALF_EVEN)
                .compareTo(data.getWorkingCapitalRatio()) == 0;
    }

    @Test
    public void test_WorkingCapitalRatioFinally() {
        LedgerSummary data = new LedgerSummary(false);
        for (int i = 0; i < 100; i++) {
            if (i%6 == 0)
                data.addAssets(testDataNegative);
            if (i%7 == 0)
                data.addAssets(testDataPositive);
            if (i%8 == 0)
                data.addLiabilities(testDataNegative);
            if (i%9 == 0)
                data.addLiabilities(testDataPositive);
        }

        data.updateCalculatedFields();
        assert new BigDecimal((100-1)/6+1).multiply(testDataNegative)
                .add(new BigDecimal((100-1)/7+1).multiply(testDataPositive))
                .divide(new BigDecimal((100-1)/8+1).multiply(testDataNegative)
                        .add(new BigDecimal((100-1)/9+1).multiply(testDataPositive)),
                        3, BigDecimal.ROUND_HALF_EVEN)
                .compareTo(data.getWorkingCapitalRatio()) == 0;
    }
}
