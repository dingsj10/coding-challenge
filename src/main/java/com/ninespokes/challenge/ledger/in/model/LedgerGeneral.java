package com.ninespokes.challenge.ledger.in.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class LedgerGeneral {

    @JsonAlias({"object_category"})
    private String objectCategory;

    @JsonAlias({"connection_id"})
    private String connectionID;

    @JsonAlias({"user"})
    private String user;

    @JsonAlias({"object_creation_date"})
    private Date objectCreationDate;

    @JsonAlias({"data"})
    private List<LedgerData> data;

    @JsonAlias({"currency"})
    private String currency;

    @JsonAlias({"object_origin_type"})
    private String objectOriginType;

    @JsonAlias({"object_origin_category"})
    private String objectOriginCategory;

    @JsonAlias({"object_type"})
    private String objectType;

    @JsonAlias({"object_class"})
    private String objectClass;

    @JsonAlias({"balance_date"})
    private Date balanceDate;

}
