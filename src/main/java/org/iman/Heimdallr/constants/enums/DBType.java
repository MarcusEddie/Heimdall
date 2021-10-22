/**
 * 
 */
package org.iman.Heimdallr.constants.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author ey
 *
 */
public enum DBType implements BaseEnum{
    
    ALL(99999, "all"),
    MySQL(1, "MySQL"),
    HANA(2, "HANA");

    private Integer code;
    private String val;
    
    private DBType(Integer code, String val) {
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
