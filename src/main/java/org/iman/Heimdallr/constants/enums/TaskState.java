/**
 * 
 */
package org.iman.Heimdallr.constants.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author ey
 *
 */
public enum TaskState implements BaseEnum {
    READY(1, "Ready"), 
    RUNNING(2, "Running"), 
    DELAYED(3, "Delay"), 
    CANCELD(4, "Canceled"),
    SUCCESS(5, "Successful"), 
    FAILED(6, "Failed"), 
    DELETED(7, "Deleted");

    private Integer level;

    private String val;

    private TaskState(Integer level, String val) {
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