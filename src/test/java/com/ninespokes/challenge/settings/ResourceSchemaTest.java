package com.ninespokes.challenge.settings;

import org.junit.Test;

public class ResourceSchemaTest {
    @Test
    public void test() {
        assert ResourceSchema.CLASSPATH == ResourceSchema.valueOfStr("classpath://");
        assert ResourceSchema.CLASSPATH == ResourceSchema.valueOfStr("ClassPath://");
        assert true == ResourceSchema.valueOfStr("classpath://").isSupported();
        assert ResourceSchema.FILE == ResourceSchema.valueOfStr("file://");
        assert ResourceSchema.FILE == ResourceSchema.valueOfStr("FILE://");
        assert true == ResourceSchema.valueOfStr("file://").isSupported();
        assert ResourceSchema.HTTPS == ResourceSchema.valueOfStr("https://");
        assert ResourceSchema.HTTPS == ResourceSchema.valueOfStr("hTTps://");
        assert false == ResourceSchema.valueOfStr("https://").isSupported();
        assert ResourceSchema.HTTP == ResourceSchema.valueOfStr("http://");
        assert ResourceSchema.HTTP == ResourceSchema.valueOfStr("HttP://");
        assert false == ResourceSchema.valueOfStr("http://").isSupported();
        assert null == ResourceSchema.valueOfStr(null);
        assert null == ResourceSchema.valueOfStr("");
        assert null == ResourceSchema.valueOfStr(" ");
        assert null == ResourceSchema.valueOfStr(",");
        assert null == ResourceSchema.valueOfStr("data.json");
        assert null == ResourceSchema.valueOfStr("123");
        assert null == ResourceSchema.valueOfStr("classpath:///");
        assert null == ResourceSchema.valueOfStr(" classpath://");
        assert null == ResourceSchema.valueOfStr("classpath:// ");
        assert null == ResourceSchema.valueOfStr("classpath ://");
        assert null == ResourceSchema.valueOfStr("classpath:/");
        assert null == ResourceSchema.valueOfStr("lasspath://");
    }
}
