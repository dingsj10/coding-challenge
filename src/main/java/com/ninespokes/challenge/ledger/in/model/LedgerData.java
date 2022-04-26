package com.ninespokes.challenge.ledger.in.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LedgerData {

    @JsonAlias({"account_category"})
    private String accountCategory;

    @JsonAlias({"account_type"})
    private String accountType;

    @JsonAlias({"value_type"})
    private String valueType;

    @JsonAlias({"system_account"})
    private String systemAccount;

    @JsonAlias({"total_value"})
    private BigDecimal totalValue;

    @JsonAlias({"account_code"})
    private String accountCode;

    @JsonAlias({"account_currency"})
    private String accountCurrency;

    @JsonAlias({"account_identifier"})
    private String accountIdentifier;

    @JsonAlias({"account_name"})
    private String accountName;

    @JsonAlias({"account_status"})
    private String accountStatus;

    @JsonAlias({"account_type_bank"})
    private String accountTypeBank;

}
