package com.ninespokes.challenge.settings;

import com.ninespokes.challenge.exception.InvalidValueException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SettingsParser {

    private static final String paramSplitter = "=";

    /**
     * Parse the given args list, if more than 1 value given for the same argument type,
     * The one passed in earlier will be overwritten by the later one.
     *
     * @param args              args from command line when executing the Main program.
     * @param ignoreInvalidArgs invalid arguments will be ignored if true, otherwise throw InvalidInputException
     * @throws InvalidValueException
     */
    public static void parse(String[] args, boolean ignoreInvalidArgs) {
        if (args == null || args.length == 0) {
            return;
        }

        for (String arg : args) {
            String[] param = arg.split(paramSplitter, 2);
            if (param.length != 2) {
                if (ignoreInvalidArgs) {
                    log.warn("Ignore invalid argument ({})", arg);
                } else {
                    throw new InvalidValueException("Invalid argument: " + arg);
                }
            }

            String inArg = param[0];
            String inValue = param[1];

            InputArgument inKey = InputArgument.valueOfStr(inArg);
            if (inKey == null) {
                if (ignoreInvalidArgs) {
                    log.warn("Ignore unsupported argument ({})", arg);
                } else {
                    throw new InvalidValueException("Unsupported argument: " + arg);
                }
            } else {
                Settings.set(inKey, inValue);
            }
        }
    }
}
