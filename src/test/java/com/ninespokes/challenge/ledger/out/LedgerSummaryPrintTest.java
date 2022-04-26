package com.ninespokes.challenge.ledger.out;

import com.ninespokes.challenge.ledger.out.model.LedgerSummary;
import org.junit.Test;

import java.math.BigDecimal;

public class LedgerSummaryPrintTest {

    @Test
    public void test() {
        LedgerSummary result = new LedgerSummary(true);
        result.setRevenue(new BigDecimal("519169.25"));
        result.setExpenses(new BigDecimal("-41131.65"));
        result.setGrossProfitMargin(new BigDecimal("0.456212"));
        result.setNetProfitMargin(new BigDecimal("0.456212"));
        result.setWorkingCapitalRatio(new BigDecimal("-0.456212"));

        assert (
            "Revenue: $519,169\n" +
            "Expenses: -$41,131\n" +
            "Gross Profit Margin: 45.6%\n" +
            "Net Profit Margin: 45.6%\n" +
            "Working Capital Ratio: -45.6%").equals(result.toString());
    }

}
