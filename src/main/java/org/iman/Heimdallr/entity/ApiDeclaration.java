/**
 * 
 */
package org.iman.Heimdallr.entity;

import java.io.Serializable;

import org.iman.Heimdallr.constants.enums.HttpMethod;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ey
 *
 */
@TableName("apiDeclaration")
public class ApiDeclaration extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3243197995708513614L;
    
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long appId;

    private Long moduleId;

    private Long functionId;

    private String name;

    private String url;

    private String path;

    private HttpMethod method;

    private JsonNode header;

    private JsonNode arguments;

    private JsonNode response;
    
    public ApiDeclaration() {
        super();
    }

    public ApiDeclaration(Long id) {
        super();
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
        return "ApiDeclaration [id=" + id + ", appId=" + appId + ", moduleId=" + moduleId
                + ", functionId=" + functionId + ", name=" + name + ", url=" + url + ", path="
                + path + ", method=" + method + ", header=" + header + ", arguments=" + arguments
                + ", response=" + response + ", getCreateBy()=" + getCreateBy() + ", getEnabled()="
                + getEnabled() + ", getDeleted()=" + getDeleted() + ", getCreateTime()="
                + getCreateTime() + ", getModifiedBy()=" + getModifiedBy() + ", getModifiedTime()="
                + getModifiedTime() + "]";
    }

}
