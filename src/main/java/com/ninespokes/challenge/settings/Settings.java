package com.ninespokes.challenge.settings;

import com.ninespokes.challenge.exception.InvalidValueException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Settings {

    private static Map<InputArgument, String> settings = new HashMap<>();

    /**
     * Set settings of the argument type
     *
     * @param arg   Argument to get the setting value, {@link InputArgument}
     * @param value Value to be set
     */
    public static synchronized void set(InputArgument arg, String value) {
        if (arg == null) {
            throw new InvalidValueException("Unable to set settings without key argument");
        }
        settings.put(arg, value);
    }

    /**
     * Get settings of the argument type,
     * if no value found in settings, return default value associated with the argument given
     *
     * @param arg   Argument to get the setting value, {@link InputArgument}
     * @return      Setting value of this argument
     */
    public static synchronized String get(InputArgument arg) {
        return MapUtils.getString(settings, arg, arg.getDefaultValue());
    }

    /**
     * Get settings of the argument type,
     * if no value found in settings, return default value given
     *
     * @param arg   Argument to get the setting value, {@link InputArgument}
     * @return      Setting value of this argument
     */
    public static synchronized String get(InputArgument arg, String defaultValue) {
        return MapUtils.getString(settings, arg, defaultValue);
    }
}
