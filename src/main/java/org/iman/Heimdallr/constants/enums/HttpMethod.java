/**
 * 
 */
package org.iman.Heimdallr.constants.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author ey
 *
 */
public enum HttpMethod implements BaseEnum{

    GET(1, "GET"),
    HEAD(2, "HEAD"),
    POST(3, "POST"),
    PUT(4,"PUT"),
    DELETE(5, "DELETE"),
    CONNECT(6, "CONNECT"),
    OPTIONS(7, "OPTIONS"),
    TRACE(8, "TRACE"),
    PATCH(9, "PATCH");
    
    private Integer id;
    private String val;

    private HttpMethod(Integer id, String val) {
        this.id = id;
        this.val = val;
    }

    @JsonValue
    public String getVal() {
        return val;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public Integer getDigit() {
        return this.id;
    }

    @Override
    public String getDesc() {
        return this.val;
    }
    
}
