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
public class TestCaseVo implements Serializable{

    private static final long serialVersionUID = -434640523179820459L;

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
    
    @JsonProperty("description")
    private String description;

    @JsonProperty("steps")
    private String step;
    
    @JsonProperty("results")
    private String results;

    @JsonProperty("rawDataId")
    private Long rawDataId;
    
    @JsonProperty("state")
    private TestCaseState state ;
    
    public TestCaseVo() {
        super();
    }

    public TestCaseVo(Long id) {
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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public Long getRawDataId() {
        return rawDataId;
    }

    public void setRawDataId(Long rawDataId) {
        this.rawDataId = rawDataId;
    }

    public TestCaseState getState() {
        return state;
    }

    public void setState(TestCaseState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "TestCaseVo [id=" + id + ", appId=" + appId + ", appName=" + appName + ", moduleId="
                + moduleId + ", moduleName=" + moduleName + ", functionId=" + functionId
                + ", functionName=" + functionName + ", name=" + name + ", description="
                + description + ", step=" + step + ", results=" + results + ", rawDataId="
                + rawDataId + ", state=" + state + "]";
    }

}
