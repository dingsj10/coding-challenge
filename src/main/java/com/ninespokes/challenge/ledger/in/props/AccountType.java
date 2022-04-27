package com.ninespokes.challenge.ledger.in.props;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Slf4j
@Getter
public enum AccountType {

    AT_SALES                        ("sales"),

    AT_CURRENT                      ("current"),

    AT_BANK                         ("bank"),

    AT_CURRENT_ACCOUNTS_RECEIVABLE  ("current_accounts_receivable"),

    AT_CURRENT_ACCOUNTS_PAYABLE     ("current_accounts_payable");

    private String value;

    AccountType(String value) {
        this.value = value;
    }

    public static AccountType valueOfStr(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        return Arrays.stream(values()).filter(v -> v.getValue().equals(value.toLowerCase())).findAny().orElse(null);
    }
}
