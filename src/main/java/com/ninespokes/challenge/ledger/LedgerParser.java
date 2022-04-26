package com.ninespokes.challenge.ledger;

import com.ninespokes.challenge.ledger.in.model.LedgerData;
import com.ninespokes.challenge.ledger.in.model.LedgerGeneral;
import com.ninespokes.challenge.ledger.in.props.AccountCategory;
import com.ninespokes.challenge.ledger.in.props.AccountType;
import com.ninespokes.challenge.ledger.in.props.ValueType;
import com.ninespokes.challenge.ledger.out.model.LedgerSummary;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class LedgerParser {

    public static LedgerSummary parse(LedgerGeneral ledgerGeneral) {
        if (ledgerGeneral == null) {
            return null;
        }

        LedgerSummary summary = new LedgerSummary(false);

        if (CollectionUtils.isEmpty(ledgerGeneral.getData())) {
            return summary;
        }

        Set<AccountType> assetsAccountTypes = new HashSet<>(Arrays.asList(AccountType.AT_CURRENT, AccountType.AT_BANK, AccountType.AT_CURRENT_ACCOUNTS_RECEIVABLE));
        Set<AccountType> liabilitiesAccountTypes = new HashSet<>(Arrays.asList(AccountType.AT_CURRENT, AccountType.AT_CURRENT_ACCOUNTS_PAYABLE));
        for (LedgerData ledgerData : ledgerGeneral.getData()) {
            AccountCategory accountCategory = AccountCategory.valueOfStr(ledgerData.getAccountCategory());
            AccountType accountType = AccountType.valueOfStr(ledgerData.getAccountType());
            ValueType valueType = ValueType.valueOfStr(ledgerData.getValueType());

            if (accountCategory == AccountCategory.AC_REVENUE) {
                summary.addRevenue(ledgerData.getTotalValue());
            }
            if (accountCategory == AccountCategory.AC_EXPENSE) {
                summary.addExpenses(ledgerData.getTotalValue());
            }
            if (accountCategory == AccountCategory.AC_ASSETS && assetsAccountTypes.contains(accountType)) {
                if (valueType == ValueType.VT_DEBIT)
                    summary.addAssets(ledgerData.getTotalValue());
                if (valueType == ValueType.VT_CREDIT)
                    summary.addAssets(ledgerData.getTotalValue().negate());
            }
            if (accountCategory == AccountCategory.AC_LIABILITY && liabilitiesAccountTypes.contains(accountType)) {
                if (valueType == ValueType.VT_CREDIT)
                    summary.addLiabilities(ledgerData.getTotalValue());
                if (valueType == ValueType.VT_DEBIT)
                    summary.addLiabilities(ledgerData.getTotalValue().negate());
            }

            if (accountType == AccountType.AT_SALES && valueType == ValueType.VT_DEBIT) {
                summary.addGrossProfit(ledgerData.getTotalValue());
            }
        }

        return summary;
    }

}
