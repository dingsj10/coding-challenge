package com.ninespokes.challenge.ledger.in.props;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Slf4j
@Getter
public enum ValueType {

    VT_DEBIT    ("debit"),

    VT_CREDIT   ("credit");

    private String value;

    ValueType(String value) {
        this.value = value;
    }

    public static ValueType valueOfStr(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        return Arrays.stream(values()).filter(v -> v.getValue().equals(value.toLowerCase())).findAny().orElse(null);
    }
}
