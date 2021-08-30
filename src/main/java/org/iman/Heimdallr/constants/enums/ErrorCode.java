/**
 * 
 */
package org.iman.Heimdallr.constants.enums;

/**
 * @author ey
 *
 */
public enum ErrorCode {

    SUCCESS("success", 200),
    DATA_CONVERSION_FAILURE("Data Conversion Failure", 510),
    PARAMETERS_ARE_INVALID("Parameters are Invalid", 400),
    REQUIRED_DATA_NOT_AVAILABLE("Required Data Not Available", 4001),
    DATA_IS_DELETED("Data is Deleted", 410);
    
    private String msg;
    private Integer code;
    
    private ErrorCode(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public Integer getCode() {
        return code;
    }
}
