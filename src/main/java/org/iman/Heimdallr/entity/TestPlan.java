/**
 * 
 */
package org.iman.Heimdallr.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.iman.Heimdallr.constants.enums.CasePriority;
import org.iman.Heimdallr.constants.enums.TestType;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ey
 *
 */
@TableName("testPlan")
public class TestPlan extends BaseEntity implements Serializable{

    private static final long serialVersionUID = -4457744020613287466L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long appId;

    private Long moduleId;

    private Long functionId;

    private String name;
    
    private TestType testType;
    
    private CasePriority priority;
    
    private Boolean repeat;
    
    private LocalDateTime triggerTime;
    
    private String cron;
    
    private LocalDateTime nextTriggerTime;
    
    private JsonNode caseSet;
    
    private Integer caseSize;

    public TestPlan() {
        super();
    }

    public TestPlan(Long id) {
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

    public Integer getCaseSize() {
        return caseSize;
    }

    public void setCaseSize(Integer caseSize) {
        this.caseSize = caseSize;
    }

    @Override
    public String toString() {
        return "TestPlan [id=" + id + ", appId=" + appId + ", moduleId=" + moduleId
                + ", functionId=" + functionId + ", name=" + name + ", testType=" + testType
                + ", priority=" + priority + ", repeat=" + repeat + ", triggerTime=" + triggerTime
                + ", cron=" + cron + ", nextTriggerTime=" + nextTriggerTime + ", caseSet=" + caseSet
                + ", caseSize=" + caseSize + ", getCreateBy()=" + getCreateBy() + ", getEnabled()="
                + getEnabled() + ", getDeleted()=" + getDeleted() + ", getCreateTime()="
                + getCreateTime() + ", getModifiedBy()=" + getModifiedBy() + ", getModifiedTime()="
                + getModifiedTime() + "]";
    }
}
