/**
 * 
 */
package org.iman.Heimdallr.constant;

/**
 * @author ey
 *
 */
public enum ErrorCode {

    SUCCESS("success", 200),
    DATA_CONVERSION_FAILURE("Data Conversion Failure", 510);
    
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
