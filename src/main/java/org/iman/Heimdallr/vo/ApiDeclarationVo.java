/**
 * 
 */
package org.iman.Heimdallr.vo;

import java.io.Serializable;

import org.iman.Heimdallr.constants.enums.HttpMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ey
 *
 */
public class ApiDeclarationVo implements Serializable{

    private static final long serialVersionUID = 2893699891368272247L;

    @JsonProperty("id")
    private Long id;
    @JsonProperty("appId")
    private Long appId;
    @JsonProperty("moduleId")
    private Long moduleId;
    @JsonProperty("functionId")
    private Long functionId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;
    @JsonProperty("path")
    private String path;
    @JsonProperty("method")
    private HttpMethod method;
    @JsonProperty("header")
    private JsonNode header;
    @JsonProperty("arguments")
    private JsonNode arguments;
    @JsonProperty("response")
    private JsonNode response;
    
    public ApiDeclarationVo() {
        super();
    }

    public ApiDeclarationVo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public JsonNode getHeader() {
        return header;
    }

    public void setHeader(JsonNode header) {
        this.header = header;
    }

    public JsonNode getArguments() {
        return arguments;
    }

    public void setArguments(JsonNode arguments) {
        this.arguments = arguments;
    }

    public JsonNode getResponse() {
        return response;
    }

    public void setResponse(JsonNode response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ApiDeclarationVo [id=" + id + ", appId=" + appId + ", moduleId=" + moduleId
                + ", functionId=" + functionId + ", name=" + name + ", url=" + url + ", path="
                + path + ", method=" + method + ", header=" + header + ", arguments=" + arguments
                + ", response=" + response + "]";
    }
    
}

