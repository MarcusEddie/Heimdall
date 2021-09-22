/**
 * 
 */
package org.iman.Heimdallr.constants.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author ey
 *
 */
public enum CasePriority implements BaseEnum{
    P1(1, "P1"),
    P2(2, "P2"),
    P3(3, "P3"),
    P4(4, "P4");

    private Integer level;
    
    private String val;
    
    private CasePriority(Integer level, String val) {
        this.level = level;
        this.val = val;
    }

    public Integer getLevel() {
        return level;
    }

    @JsonValue
    public String getVal() {
        return val;
    }

    @Override
    public Integer getDigit() {
        return this.level;
    }

    @Override
    public String getDesc() {
        return this.val;
    }

}
