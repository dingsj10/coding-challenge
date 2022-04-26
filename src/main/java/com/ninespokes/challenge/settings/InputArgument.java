package com.ninespokes.challenge.settings;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@Getter
public enum InputArgument {

    JSON_PATH   ("--json_path",
            "classpath://data.json");

    // TODO other input args could be supported potentially in future

    private String name;

    private String defaultValue;

    InputArgument(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public static InputArgument valueOfStr(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        return Arrays.stream(values()).filter(v -> v.getName().equals(name.toLowerCase())).findAny().orElse(null);
    }

}
