package org.iman.Heimdallr.constants.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskType implements BaseEnum {
    FROM_PLAN(1, "fromPlan"),
    FROM_HIST(2, "fromHistory");

    private Integer level;

    private String val;

    private TaskType(Integer level, String val) {
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
