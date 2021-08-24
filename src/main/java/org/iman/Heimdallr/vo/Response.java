/**
 * 
 */
package org.iman.Heimdallr.vo;

import java.io.Serializable;

/**
 * @author ey
 *
 */
public class Response<T> implements Serializable {

    private static final long serialVersionUID = -3974519641255781571L;

    private Boolean success;
    private T data;
    private Integer errorCode;
    private String errorMsg;
    private String traceId;
    private String host;

    public Response() {
        super();
        setSuccess(true);
    }

    public Response(T data) {
        this.data = data;
        setSuccess(true);
    }

    public Response(T data, Integer errorCode, String errorMsg) {
        this.data = data;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        setSuccess(false);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return "Response [success=" + success + ", data=" + data + ", errorCode=" + errorCode
                + ", errorMsg=" + errorMsg + ", traceId=" + traceId + ", host=" + host + "]";
    }

}
