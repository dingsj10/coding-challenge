package com.ninespokes.challenge.settings;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Slf4j
@Getter
public enum ResourceSchema {

    CLASSPATH   ("classpath://", true),

    FILE        ("file://", true),

    HTTP        ("http://", false),

    HTTPS       ("https://", false);

    // TODO other protocols could be supported potentially in future

    private String prefix;
    private boolean supported;

    ResourceSchema(String prefix, boolean supported) {
        this.prefix = prefix;
        this.supported = supported;
    }

    public static ResourceSchema valueOfStr(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return null;
        }

        return Arrays.stream(values()).filter(v -> v.getPrefix().equals(prefix.toLowerCase())).findAny().orElse(null);
    }
}
