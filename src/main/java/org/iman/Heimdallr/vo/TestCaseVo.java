/**
 * 
 */
package org.iman.Heimdallr.vo;

import java.io.Serializable;

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
    
    @JsonProperty("moduleId")
    private Long moduleId;
    
    @JsonProperty("functionId")
    private Long functionId;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("description")
    private String description;

    @JsonProperty("steps")
    private String step;
    
    @JsonProperty("results")
    private String results;

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

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "TestCaseVo [id=" + id + ", appId=" + appId + ", moduleId=" + moduleId
                + ", functionId=" + functionId + ", name=" + name + ", step=" + step
                + ", description=" + description + ", results=" + results + "]";
    }
}
