package com.ninespokes.challenge.ledger.in.props;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Slf4j
@Getter
public enum AccountCategory {

    AC_REVENUE      ("revenue"),

    AC_EXPENSE      ("expense"),

    AC_ASSETS       ("assets"),

    AC_LIABILITY    ("liability");

    private String value;

    AccountCategory(String value) {
        this.value = value;
    }

    public static AccountCategory valueOfStr(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        return Arrays.stream(values()).filter(v -> v.getValue().equals(value.toLowerCase())).findAny().orElse(null);
    }
}
