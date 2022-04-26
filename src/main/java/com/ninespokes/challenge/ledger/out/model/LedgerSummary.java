package com.ninespokes.challenge.ledger.out.model;

import com.ninespokes.challenge.ledger.out.annotation.CurrencyAdvice;
import com.ninespokes.challenge.ledger.out.annotation.PercentageAdvice;
import com.ninespokes.challenge.ledger.out.annotation.OutputValue;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;

@Slf4j
@Setter
@Getter
public class LedgerSummary {
    @CurrencyAdvice
    @OutputValue("Revenue")
    BigDecimal revenue = BigDecimal.ZERO;

    @CurrencyAdvice
    @OutputValue("Expenses")
    BigDecimal expenses = BigDecimal.ZERO;

    @PercentageAdvice
    @OutputValue("Gross Profit Margin")
    BigDecimal grossProfitMargin = null;
    BigDecimal grossProfit = BigDecimal.ZERO;

    @PercentageAdvice
    @OutputValue("Net Profit Margin")
    BigDecimal netProfitMargin = null;

    @PercentageAdvice
    @OutputValue("Working Capital Ratio")
    BigDecimal workingCapitalRatio = null;
    BigDecimal assets = BigDecimal.ZERO;
    BigDecimal liabilities = BigDecimal.ZERO;

    private boolean realTimeCalc;

    public LedgerSummary(boolean realTimeCalc) {
        this.realTimeCalc = realTimeCalc;
    }

    @Override
    public String toString() {
        if (!realTimeCalc) {
            updateCalculatedFields();
        }

        return LedgerSummarySerializer.serialise(this, this.getClass());
    }

    public void addRevenue(BigDecimal value) {
        revenue = revenue.add(value);
        if (realTimeCalc) {
            updateGrossProfitMargin();
            updateNetProfitMargin();
        }
    }

    public void addExpenses(BigDecimal value) {
        expenses = expenses.add(value);
        if (realTimeCalc) {
            updateNetProfitMargin();
        }
    }

    public void addGrossProfit(BigDecimal value) {
        grossProfit = grossProfit.add(value);
        if (realTimeCalc) {
            updateGrossProfitMargin();
        }
    }

    public void addAssets(BigDecimal value) {
        assets = assets.add(value);
        if (realTimeCalc) {
            updateWorkingCapitalRatio();
        }
    }

    public void addLiabilities(BigDecimal value) {
        liabilities = liabilities.add(value);
        if (realTimeCalc) {
            updateWorkingCapitalRatio();
        }
    }

    /**
     * This is calculated in two steps:
     * first by adding all the total_value fields where the account_type is set to sales and the value_type is set to debit;
     * then dividing that by the revenue value calculated earlier to generate a percentage value.
     */
    private void updateGrossProfitMargin() {
        if (revenue.compareTo(BigDecimal.ZERO) != 0) {
            PercentageAdvice advice = LedgerSummarySerializer.getPercentageAdviceOfFieldInClass("grossProfitMargin", this.getClass());
            boolean negative = LedgerSummarySerializer.isDivideOrMultiplyResultNegative(grossProfit, revenue);
            grossProfitMargin = grossProfit.divide(revenue, advice.decimal() + (int)Math.log10(advice.coefficient()), negative && advice.roundNegative() != -1 ? advice.roundNegative() : advice.round());
        } else {
            grossProfitMargin = BigDecimal.ZERO;
        }
    }

    /**
     * This metric is calculated by subtracting the expenses value from the revenue value
     * and dividing the remainder by revenue to calculate a percentage.
     */
    private void updateNetProfitMargin() {
        if (revenue.compareTo(BigDecimal.ZERO) != 0) {
            PercentageAdvice advice = LedgerSummarySerializer.getPercentageAdviceOfFieldInClass("netProfitMargin", this.getClass());
            BigDecimal netProfit = revenue.subtract(expenses);
            boolean negative = LedgerSummarySerializer.isDivideOrMultiplyResultNegative(netProfit, revenue);
            netProfitMargin = netProfit.divide(revenue, advice.decimal() + (int)Math.log10(advice.coefficient()), negative && advice.roundNegative() != -1 ? advice.roundNegative() : advice.round());
        } else {
            netProfitMargin = BigDecimal.ZERO;
        }
    }

    /**
     * This is calculated dividing the assets by the liabilities creating a percentage value where
     *
     * assets are calculated by:
     *
     * adding the total_value from all records where the account_category is set to assets,
     * the value_type is set to debit,
     * and the account_type is one of current, bank, or current_accounts_receivable
     * subtracting the total_value from all records where the account_category is set to assets,
     * the value_type is set to credit,
     * and the account_type is one of current, bank, or current_accounts_receivable
     *
     * and liabilities are calculated by:
     *
     * adding the total_value from all records where the account_category is set to liability,
     * the value_type is set to credit,
     * and the account_type is one of current or current_accounts_payable
     * subtracting the total_value from all records where the account_category is set to liability,
     * the value_type is set to debit,
     * and the account_type is one current or current_accounts_payable
     */
    private void updateWorkingCapitalRatio() {
        if (liabilities.compareTo(BigDecimal.ZERO) != 0) {
            PercentageAdvice advice = LedgerSummarySerializer.getPercentageAdviceOfFieldInClass("workingCapitalRatio", this.getClass());
            boolean negative = LedgerSummarySerializer.isDivideOrMultiplyResultNegative(assets, liabilities);
            workingCapitalRatio = assets.divide(liabilities, advice.decimal() + (int)Math.log10(advice.coefficient()), negative && advice.roundNegative() != -1 ? advice.roundNegative() : advice.round());
        } else {
            workingCapitalRatio = BigDecimal.ZERO;
        }
    }

    public void updateCalculatedFields() {
        updateGrossProfitMargin();
        updateNetProfitMargin();
        updateWorkingCapitalRatio();
    }
}
