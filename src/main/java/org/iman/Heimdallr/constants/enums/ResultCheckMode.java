/**
 * 
 */
package org.iman.Heimdallr.constants.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author ey
 *
 */
public enum ResultCheckMode implements BaseEnum{
    RESPONSE_DATA(1, "RESPONSE_DATA"),
    DB_DATA(2, "DB_DATA");

    private Integer id;
    
    private String label;
    
    private ResultCheckMode(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @Override
    public Integer getDigit() {
        return this.id;
    }

    @Override
    public String getDesc() {
        return this.label;
    }

}
