package org.iman.Heimdallr.constants.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TestType implements BaseEnum{

    API_TEST(1, "API_Test"),
    UI_AUTO_TEST(2, "UI_Auto_Test"),
    CHAOS_TEST(3, "Chaos_Test");
    
    private Integer code;
    private String val;

    private TestType(Integer code, String val) {
        this.code = code;
        this.val = val;
    }

    @JsonValue
    public String getVal() {
        return val;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public Integer getDigit() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.val;
    }
}