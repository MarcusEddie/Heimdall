/**
 * 
 */
package org.iman.Heimdallr.utils;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.map.HashedMap;
import org.iman.Heimdallr.constants.enums.ErrorCode;
import org.iman.Heimdallr.vo.PageInfo;
import org.iman.Heimdallr.vo.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author ey
 *
 */
public class ControllerUtils {

    public static final String PARAMS = "params";
    public static final String PAGE_INFO = "pageInfo";

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

    public static Map<String, Object> isolatePojos(ObjectNode node, Class<Object> targetClass) {
        if (null == node || null == targetClass) {
            throw new IllegalArgumentException("Node and targetClass are required");
        }
        Map<String, Object> result = new HashedMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            result.put(targetClass.getSimpleName(), mapper.readValue(PARAMS, targetClass));
            result.put(PageInfo.class.getSimpleName(), mapper.readValue(PAGE_INFO, PageInfo.class));
        } catch (JsonProcessingException e) {
            Response resp = encapsulateErrCode(ErrorCode.PARAMETERS_PARSING_FAILED);
            result.put(Response.class.getSimpleName(), resp);
        }
        return result;

    }
}
