package com.ninespokes.challenge.ledger;

import com.ninespokes.challenge.ledger.in.model.LedgerGeneral;
import com.ninespokes.challenge.settings.InputArgument;
import com.ninespokes.challenge.settings.Settings;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class LedgerReaderTest {
    @Test
    public void test() {
        LedgerGeneral ledgerGeneral = null;
        try {
            Settings.set(InputArgument.JSON_PATH, "invalid path");
            ledgerGeneral = LedgerReader.read();
        } catch (Exception e) {
            log.error("==expected==:  {}", e.getMessage());
        }
        assert ledgerGeneral == null;

        ledgerGeneral = null;
        try {
            Settings.set(InputArgument.JSON_PATH, "classpath://data.json");
            ledgerGeneral = LedgerReader.read();
        } catch (Exception e) {
            log.error("==expected==:  {}", e.getMessage());
        }
        assert ledgerGeneral != null;

        ledgerGeneral = null;
        try {
            Settings.set(InputArgument.JSON_PATH, "file:///tmp/data.json");
            ledgerGeneral = LedgerReader.read();
        } catch (Exception e) {
            log.error("==expected==:  {}", e.getMessage());
        }
        assert ledgerGeneral == null;

        ledgerGeneral = null;
        try {
            Settings.set(InputArgument.JSON_PATH, "/tmp/data.json");
            ledgerGeneral = LedgerReader.read();
        } catch (Exception e) {
            log.error("==expected==:  {}", e.getMessage());
        }
        assert ledgerGeneral == null;
    }
}
