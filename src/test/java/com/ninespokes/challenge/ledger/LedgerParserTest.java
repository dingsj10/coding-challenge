package com.ninespokes.challenge.ledger;

import com.ninespokes.challenge.ledger.in.model.LedgerData;
import com.ninespokes.challenge.ledger.in.model.LedgerGeneral;
import com.ninespokes.challenge.ledger.in.props.AccountCategory;
import com.ninespokes.challenge.ledger.in.props.AccountType;
import com.ninespokes.challenge.ledger.in.props.ValueType;
import com.ninespokes.challenge.ledger.out.model.LedgerSummary;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LedgerParserTest {

    private static final BigDecimal testDataPositive = new BigDecimal("100000000000");
    private static final BigDecimal testDataNegative = new BigDecimal("-100000000000");

    private LedgerData buildRevenue(BigDecimal value) {
        LedgerData data = new LedgerData();
        data.setAccountCategory(AccountCategory.AC_REVENUE.getValue());
        data.setTotalValue(value);
        return data;
    }

    private LedgerData buildExpense(BigDecimal value) {
        LedgerData data = new LedgerData();
        data.setAccountCategory(AccountCategory.AC_EXPENSE.getValue());
        data.setTotalValue(value);
        return data;
    }

    private LedgerData buildSaleDebit(BigDecimal value) {
        LedgerData data = new LedgerData();
        data.setAccountType(AccountType.AT_SALES.getValue());
        data.setValueType(ValueType.VT_DEBIT.getValue());
        data.setTotalValue(value);
        return data;
    }

    private LedgerData buildAssets(BigDecimal value, ValueType valueType, AccountType accountType) {
        LedgerData data = new LedgerData();
        data.setAccountCategory(AccountCategory.AC_ASSETS.getValue());
        data.setValueType(valueType.getValue());
        data.setAccountType(accountType.getValue());
        data.setTotalValue(value);
        return data;
    }

    private LedgerData buildLiabilities(BigDecimal value, ValueType valueType, AccountType accountType) {
        LedgerData data = new LedgerData();
        data.setAccountCategory(AccountCategory.AC_LIABILITY.getValue());
        data.setValueType(valueType.getValue());
        data.setAccountType(accountType.getValue());
        data.setTotalValue(value);
        return data;
    }

    @Test
    public void test_Null() {
        LedgerGeneral ledgerGeneral = new LedgerGeneral();
        ledgerGeneral.setData(null);

        LedgerSummary summary = LedgerParser.parse(ledgerGeneral);
        summary.updateCalculatedFields();
        assert summary.getRevenue().equals(BigDecimal.ZERO);
        assert summary.getExpenses().equals(BigDecimal.ZERO);
        assert summary.getNetProfitMargin().equals(BigDecimal.ZERO);
        assert summary.getGrossProfitMargin().equals(BigDecimal.ZERO);
        assert summary.getWorkingCapitalRatio().equals(BigDecimal.ZERO);
    }

    @Test
    public void test_Empty() {
        LedgerGeneral ledgerGeneral = new LedgerGeneral();
        ledgerGeneral.setData(Collections.EMPTY_LIST);

        LedgerSummary summary = LedgerParser.parse(ledgerGeneral);
        summary.updateCalculatedFields();
        assert summary.getRevenue().equals(BigDecimal.ZERO);
        assert summary.getExpenses().equals(BigDecimal.ZERO);
        assert summary.getNetProfitMargin().equals(BigDecimal.ZERO);
        assert summary.getGrossProfitMargin().equals(BigDecimal.ZERO);
        assert summary.getWorkingCapitalRatio().equals(BigDecimal.ZERO);
    }

    @Test
    public void test_Revenue() {
        LedgerGeneral ledgerGeneral = new LedgerGeneral();
        ledgerGeneral.setData(Arrays.asList(
                buildRevenue(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildSaleDebit(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildSaleDebit(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1"))));

        LedgerSummary summary = LedgerParser.parse(ledgerGeneral);
        assert new BigDecimal("8.8").compareTo(summary.getRevenue()) == 0;

        String expected = "Revenue: $8";
        Pattern p = Pattern.compile("\\b(Revenue: .*?)\n");
        Matcher m = p.matcher(summary.toString());
        assert m.find() && expected.equals(m.group(1));
    }

    @Test
    public void test_Expenses() {
        LedgerGeneral ledgerGeneral = new LedgerGeneral();
        ledgerGeneral.setData(Arrays.asList(
                buildExpense(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildSaleDebit(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildSaleDebit(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1"))));

        LedgerSummary summary = LedgerParser.parse(ledgerGeneral);
        assert new BigDecimal("9.9").compareTo(summary.getExpenses()) == 0;

        String expected = "Expenses: $9";
        Pattern p = Pattern.compile("\\b(Expenses: .*?)\n");
        Matcher m = p.matcher(summary.toString());
        assert m.find() && expected.equals(m.group(1));
    }

    @Test
    public void test_GrossProfitMargin() {
        LedgerGeneral ledgerGeneral = new LedgerGeneral();
        ledgerGeneral.setData(Arrays.asList(
                buildExpense(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildAssets(new BigDecimal("1.1"), ValueType.VT_DEBIT, AccountType.AT_CURRENT),
                buildExpense(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildSaleDebit(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildSaleDebit(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildAssets(new BigDecimal("1.1"), ValueType.VT_CREDIT, AccountType.AT_BANK),
                buildExpense(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildSaleDebit(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1"))));

        LedgerSummary summary = LedgerParser.parse(ledgerGeneral);
        summary.updateCalculatedFields();
        assert new BigDecimal("0.6").compareTo(summary.getGrossProfitMargin()) == 0;

        String expected = "Gross Profit Margin: 60.0%";
        Pattern p = Pattern.compile("\\b(Gross Profit Margin: .*?%)");
        Matcher m = p.matcher(summary.toString());
        assert m.find() && expected.equals(m.group(1));
    }

    @Test
    public void test_NetProfitMargin() {
        LedgerGeneral ledgerGeneral = new LedgerGeneral();
        ledgerGeneral.setData(Arrays.asList(
                buildExpense(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildAssets(new BigDecimal("1.1"), ValueType.VT_DEBIT, AccountType.AT_CURRENT_ACCOUNTS_PAYABLE),
                buildExpense(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildAssets(new BigDecimal("1.1"), ValueType.VT_CREDIT, AccountType.AT_BANK),
                buildExpense(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildSaleDebit(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildSaleDebit(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1"))));

        LedgerSummary summary = LedgerParser.parse(ledgerGeneral);
        summary.updateCalculatedFields();
        assert new BigDecimal("-1.25").compareTo(summary.getNetProfitMargin()) == 0;

        String expected = "Net Profit Margin: -125.0%";
        Pattern p = Pattern.compile("\\b(Net Profit Margin: .*?%)");
        Matcher m = p.matcher(summary.toString());
        assert m.find() && expected.equals(m.group(1));
    }

    @Test
    public void test_WorkingCapitalRatioRealTime() {
        LedgerGeneral ledgerGeneral = new LedgerGeneral();
        ledgerGeneral.setData(Arrays.asList(
                buildAssets(new BigDecimal("1.1"), ValueType.VT_DEBIT, AccountType.AT_CURRENT), //a+1
                buildAssets(new BigDecimal("1.1"), ValueType.VT_DEBIT, AccountType.AT_CURRENT), //a+1
                buildAssets(new BigDecimal("1.1"), ValueType.VT_DEBIT, AccountType.AT_BANK),    //a+1
                buildAssets(new BigDecimal("1.1"), ValueType.VT_DEBIT, AccountType.AT_CURRENT_ACCOUNTS_RECEIVABLE), //a+1
                buildAssets(new BigDecimal("1.1"), ValueType.VT_DEBIT, AccountType.AT_CURRENT_ACCOUNTS_PAYABLE),
                buildAssets(new BigDecimal("1.1"), ValueType.VT_DEBIT, AccountType.AT_SALES),
                buildAssets(new BigDecimal("1.1"), ValueType.VT_CREDIT, AccountType.AT_CURRENT), //a-1
                buildAssets(new BigDecimal("1.1"), ValueType.VT_CREDIT, AccountType.AT_BANK),    //a-1
                buildAssets(new BigDecimal("1.1"), ValueType.VT_CREDIT, AccountType.AT_CURRENT_ACCOUNTS_RECEIVABLE), //a-1
                buildAssets(new BigDecimal("1.1"), ValueType.VT_CREDIT, AccountType.AT_CURRENT_ACCOUNTS_PAYABLE),
                buildAssets(new BigDecimal("1.1"), ValueType.VT_CREDIT, AccountType.AT_SALES),

                buildLiabilities(new BigDecimal("1.1"), ValueType.VT_DEBIT, AccountType.AT_CURRENT), //a-1
                buildLiabilities(new BigDecimal("1.1"), ValueType.VT_DEBIT, AccountType.AT_CURRENT), //a-1
                buildLiabilities(new BigDecimal("1.1"), ValueType.VT_DEBIT, AccountType.AT_BANK),
                buildLiabilities(new BigDecimal("1.1"), ValueType.VT_DEBIT, AccountType.AT_CURRENT_ACCOUNTS_RECEIVABLE),
                buildLiabilities(new BigDecimal("1.1"), ValueType.VT_DEBIT, AccountType.AT_CURRENT_ACCOUNTS_PAYABLE), //a-1
                buildLiabilities(new BigDecimal("1.1"), ValueType.VT_DEBIT, AccountType.AT_SALES),
                buildLiabilities(new BigDecimal("1.1"), ValueType.VT_CREDIT, AccountType.AT_CURRENT), //a+1
                buildLiabilities(new BigDecimal("1.1"), ValueType.VT_CREDIT, AccountType.AT_BANK),
                buildLiabilities(new BigDecimal("1.1"), ValueType.VT_CREDIT, AccountType.AT_CURRENT_ACCOUNTS_RECEIVABLE),
                buildLiabilities(new BigDecimal("1.1"), ValueType.VT_CREDIT, AccountType.AT_CURRENT_ACCOUNTS_PAYABLE),//a+1

                buildRevenue(new BigDecimal("1.1")),
                buildRevenue(new BigDecimal("1.1")),
                buildSaleDebit(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1")),
                buildExpense(new BigDecimal("1.1"))));

        LedgerSummary summary = LedgerParser.parse(ledgerGeneral);
        summary.updateCalculatedFields();
        assert new BigDecimal("-1").compareTo(summary.getWorkingCapitalRatio()) == 0;

        String expected = "Working Capital Ratio: -100.0%";
        Pattern p = Pattern.compile("\\b(Working Capital Ratio: .*?%)");
        Matcher m = p.matcher(summary.toString());
        assert m.find() && expected.equals(m.group(1));
    }
}
