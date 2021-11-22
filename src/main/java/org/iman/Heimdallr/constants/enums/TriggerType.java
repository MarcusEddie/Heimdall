package org.iman.Heimdallr.constants.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TriggerType implements BaseEnum {

    SCHEDULING(1, "scheduling"), 
    JENKINS(2, "jenkins");

    private Integer code;
    private String val;

    private TriggerType(Integer code, String val) {
        this.code = code;
        this.val = val;
    }

    public Integer getCode() {
        return code;
    }

    @JsonValue
    public String getVal() {
        return val;
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
