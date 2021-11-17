/**
 * 
 */
package org.iman.Heimdallr.constants.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author ey
 *
 */
public enum Action implements BaseEnum {

    CREATE(1, "create"), 
    UPDATE(2, "update"),
    DELETE(3, "delete");

    private Integer code;
    private String val;

    private Action(Integer code, String val) {
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
