/**
 * 
 */
package org.iman.Heimdallr.utils;

import java.util.Optional;

import org.iman.Heimdallr.constants.enums.ErrorCode;
import org.iman.Heimdallr.vo.Response;

/**
 * @author ey
 *
 */
public class ControllerUtils {

    public static <T> Response<T> encapsulateErrCode(ErrorCode code) {
        Optional.ofNullable(code).orElseThrow(() -> {
            throw new IllegalArgumentException("Code is required");
        });
        
        Response<T> resp = new Response<T>();
        resp.setSuccess(false);
        resp.setErrorCode(code.getCode());
        resp.setErrorMsg(code.getMsg());
        return resp;
    }
}
