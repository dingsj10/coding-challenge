package com.ninespokes.challenge;

import com.ninespokes.challenge.exception.InvalidValueException;
import com.ninespokes.challenge.ledger.LedgerParser;
import com.ninespokes.challenge.ledger.LedgerReader;
import com.ninespokes.challenge.ledger.out.model.LedgerSummary;
import com.ninespokes.challenge.ledger.in.model.LedgerGeneral;
import com.ninespokes.challenge.settings.InputArgument;
import com.ninespokes.challenge.settings.Settings;
import com.ninespokes.challenge.settings.SettingsParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {


    public static void main(String[] args) {
        try {
            SettingsParser.parse(args, false);
        } catch (InvalidValueException e) {
            log.warn("Exception while parsing args, {}", e.getMessage(), e);
            return;
        } catch (Exception e) {
            log.warn("Unknown exception while parsing args", e);
            return;
        }

        LedgerGeneral ledgerGeneral = null;
        try {
            ledgerGeneral = LedgerReader.read();
        } catch (InvalidValueException e) {
            log.warn("Exception while reading JSON, {}", e.getMessage(), e);
            return;
        } catch (Exception e) {
            log.warn("Unknown exception while reading JSON", e);
            return;
        }

        LedgerSummary summary = null;
        try {
            summary = LedgerParser.parse(ledgerGeneral);
        } catch (InvalidValueException e) {
            log.warn("Exception while parsing data, {}", e.getMessage(), e);
            return;
        } catch (Exception e) {
            log.warn("Unknown exception while parsing data", e);
            return;
        }

        if (summary == null) {
            log.warn("Unable to parse ledger from specified resource " + Settings.get(InputArgument.JSON_PATH));
        } else {
            System.out.println(summary);
        }
    }

}
