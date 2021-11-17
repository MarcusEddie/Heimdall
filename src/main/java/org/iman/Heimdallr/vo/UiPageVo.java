/**
 * 
 */
package org.iman.Heimdallr.vo;

import java.io.Serializable;

import org.iman.Heimdallr.constants.enums.TestCaseState;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ey
 *
 */
public class UiPageVo implements Serializable{

    private static final long serialVersionUID = 2893699891368272247L;

    @JsonProperty("id")
    private Long id;
    @JsonProperty("appId")
    private Long appId;
    @JsonProperty("appName")
    private String appName;
    @JsonProperty("moduleId")
    private Long moduleId;
    @JsonProperty("moduleName")
    private String moduleName;
    @JsonProperty("functionId")
    private Long functionId;
    @JsonProperty("functionName")
    private String functionName;
    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;
    @JsonProperty("state")
    private TestCaseState state;
    
    public UiPageVo() {
        super();
    }

    public UiPageVo(Long id) {
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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
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

    public TestCaseState getState() {
        return state;
    }

    public void setState(TestCaseState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "UiPageVo [id=" + id + ", appId=" + appId + ", appName=" + appName + ", moduleId="
                + moduleId + ", moduleName=" + moduleName + ", functionId=" + functionId
                + ", functionName=" + functionName + ", name=" + name + ", url=" + url + ", state="
                + state + "]";
    }

}

