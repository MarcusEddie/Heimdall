package org.iman.Heimdallr.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.iman.Heimdallr.constants.enums.CasePriority;
import org.iman.Heimdallr.constants.enums.TestCaseState;
import org.iman.Heimdallr.constants.enums.TestType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class TestPlanVo implements Serializable{

    private static final long serialVersionUID = 6717326690855403321L;

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
    
    @JsonProperty("testType")
    private TestType testType;
    
    @JsonProperty("priority")
    private CasePriority priority;
    
    @JsonProperty("repeat")
    private Boolean repeat;
    
    @JsonProperty("triggerTime")
    private LocalDateTime triggerTime;
    
    @JsonProperty("cron")
    private String cron;
    
    @JsonProperty("nextTriggerTime")
    private LocalDateTime nextTriggerTime;
    
    @JsonProperty("caseSet")
    private JsonNode caseSet;
    
    @JsonProperty("caseSize")
    private Integer caseSize;
    
    @JsonProperty("state")
    private TestCaseState state;

    public TestPlanVo() {
        super();
    }

    public TestPlanVo(Long id) {
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

    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
    }

    public CasePriority getPriority() {
        return priority;
    }

    public void setPriority(CasePriority priority) {
        this.priority = priority;
    }

    public Boolean getRepeat() {
        return repeat;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }

    public LocalDateTime getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(LocalDateTime triggerTime) {
        this.triggerTime = triggerTime;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public LocalDateTime getNextTriggerTime() {
        return nextTriggerTime;
    }

    public void setNextTriggerTime(LocalDateTime nextTriggerTime) {
        this.nextTriggerTime = nextTriggerTime;
    }

    public JsonNode getCaseSet() {
        return caseSet;
    }

    public void setCaseSet(JsonNode caseSet) {
        this.caseSet = caseSet;
    }

    public TestCaseState getState() {
        return state;
    }

    public void setState(TestCaseState state) {
        this.state = state;
    }

    public Integer getCaseSize() {
        return caseSize;
    }

    public void setCaseSize(Integer caseSize) {
        this.caseSize = caseSize;
    }

    @Override
    public String toString() {
        return "TestPlanVo [id=" + id + ", appId=" + appId + ", appName=" + appName + ", moduleId="
                + moduleId + ", moduleName=" + moduleName + ", functionId=" + functionId
                + ", functionName=" + functionName + ", name=" + name + ", testType=" + testType
                + ", priority=" + priority + ", repeat=" + repeat + ", triggerTime=" + triggerTime
                + ", cron=" + cron + ", nextTriggerTime=" + nextTriggerTime + ", caseSet=" + caseSet
                + ", caseSize=" + caseSize + ", state=" + state + "]";
    }
}
