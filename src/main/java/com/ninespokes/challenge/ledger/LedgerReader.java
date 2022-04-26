package com.ninespokes.challenge.ledger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ninespokes.challenge.exception.InvalidValueException;
import com.ninespokes.challenge.ledger.in.model.LedgerGeneral;
import com.ninespokes.challenge.settings.InputArgument;
import com.ninespokes.challenge.settings.Settings;
import com.ninespokes.challenge.util.JSONUtil;
import com.ninespokes.challenge.util.PathUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class LedgerReader {

    /**
     * Read data from path in settings and serialise it as {@link LedgerGeneral}
     * @return
     */
    public static LedgerGeneral read() {
        LedgerGeneral ret = null;
        InputStream inputStream = null;
        String path = Settings.get(InputArgument.JSON_PATH);
        try {
            inputStream = PathUtil.getInputStreamFromPath(path);
        } catch (Exception e) {
            log.error("Unknown exception while initialising inputStream from specified resource ({})", path, e);
            throw new InvalidValueException("Unknown exception while initialising inputStream specified resource: " + path, e);
        }

        if (inputStream == null) {
            log.error("Unable to read resource from specified resource ({})", path);
            throw new InvalidValueException("Unable to read resource from specified resource: " + path);
        }

        try {
            ret = JSONUtil.JSONInputStreamToClass(inputStream, LedgerGeneral.class);
        } catch (InvalidValueException e) {
            throw e;
        } catch (JsonParseException e) {
            log.error("JsonParseException, please check if JSON input is malformed", e);
        } catch (JsonMappingException e) {
            log.error("Failed to map JSON to model", e);
        } catch (IOException e) {
            log.error("IOException while reading JSON from inputStream", e);
        } catch (Exception e) {
            log.error("Unknown exception while reading JSON from inputStream", e);
            throw new InvalidValueException("Unknown exception while reading JSON from inputStream", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Failed to close inputStream");
                }
            }
            return ret;
        }
    }

}
