package com.ninespokes.challenge.settings;

import org.junit.Test;

public class InputArgumentTest {
    @Test
    public void test() {
        assert InputArgument.JSON_PATH == InputArgument.valueOfStr("--json_path");
        assert InputArgument.JSON_PATH == InputArgument.valueOfStr("--JSON_path");
        assert InputArgument.JSON_PATH == InputArgument.valueOfStr("--JSON_path");
        assert null == InputArgument.valueOfStr(null);
        assert null == InputArgument.valueOfStr("");
        assert null == InputArgument.valueOfStr(" ");
        assert null == InputArgument.valueOfStr(",");
        assert null == InputArgument.valueOfStr("json_path");
        assert null == InputArgument.valueOfStr("123");
        assert null == InputArgument.valueOfStr("--json_path ");
        assert null == InputArgument.valueOfStr(" --json_path");
        assert null == InputArgument.valueOfStr("-- json_path");
        assert null == InputArgument.valueOfStr("--json_pat");
        assert null == InputArgument.valueOfStr("-json_path");
    }
}
