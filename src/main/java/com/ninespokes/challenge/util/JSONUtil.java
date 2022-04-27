package com.ninespokes.challenge.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class JSONUtil {

    private static ObjectMapper defaultMapper = new ObjectMapper();

//    public static class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
//        @Override
//        public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
//            jgen.writeString(value.toPlainString());
//        }
//    }
//
//    public static class DateSerializer extends JsonSerializer<Date> {
//        @Override
//        public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
//            jgen.writeNumber(value.getTime()/1000);
//        }
//    }

    static {

        defaultMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        // Stop using int to represent Enum values in deserialisation for security reasons
        defaultMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        defaultMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        defaultMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        defaultMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);

//        SimpleModule module = new SimpleModule();
//        module.addSerializer(BigDecimal.class, new BigDecimalSerializer());
//        module.addSerializer(Date.class, new DateSerializer());
//
//        defaultMapper.registerModule(module);
    }

    /**
     * Read JSON data from inputStream and map it to an instance of the class specified.
     *
     * @param inputStream   The inputStream where the JSON data provided
     * @param classType     The target class type as which the JSON data should be deserialised
     * @param <T>           The target class type T
     * @return
     */
    public static <T> T JSONInputStreamToClassSilent(InputStream inputStream, Class<T> classType) {
        if (inputStream == null || classType == null) {
            return null;
        }

        T ret = null;
        try {
            ret = defaultMapper.readValue(inputStream, classType);
        } catch (JsonParseException | JsonMappingException e) {
            log.error("Failed to parse JSON from inputStream", e);
        } catch (IOException e) {
            log.error("IO issue while reading JSON from inputStream", e);
        } catch (Exception e) {
            log.error("Unknown issue while processing JSON from inputStream", e);
        }
        return ret;
    }

    /**
     * Read JSON data from inputStream and map it to an instance of the class specified.
     *
     * @param inputStream   The inputStream where the JSON data provided
     * @param classType     The target class type as which the JSON data should be deserialised
     * @param <T>           The target class type T
     * @return
     */
    public static <T> T JSONInputStreamToClass(InputStream inputStream, Class<T> classType) throws IOException {
        if (inputStream == null || classType == null) {
            return null;
        }

        return defaultMapper.readValue(inputStream, classType);
    }

}
