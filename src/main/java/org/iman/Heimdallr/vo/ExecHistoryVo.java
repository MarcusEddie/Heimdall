package org.iman.Heimdallr.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.iman.Heimdallr.constants.enums.TaskState;
import org.iman.Heimdallr.constants.enums.TaskType;
import org.iman.Heimdallr.constants.enums.TestCaseState;
import org.iman.Heimdallr.constants.enums.TestType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class ExecHistoryVo implements Serializable{

    private static final long serialVersionUID = 5098357845360087246L;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("planId")
    private Long planId;
    @JsonProperty("planName")
    private String planName;
    @JsonProperty("type")
    private TaskType type;
    @JsonProperty("taskState")
    private TaskState taskState;
    @JsonProperty("triggerTime")
    private LocalDateTime triggerTime;
    @JsonProperty("details")
    private JsonNode details;
    @JsonProperty("testType")
    private TestType testType;
    
    @JsonProperty("state")
    private TestCaseState state;

    public ExecHistoryVo() {
        super();
    }

    public ExecHistoryVo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    public LocalDateTime getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(LocalDateTime triggerTime) {
        this.triggerTime = triggerTime;
    }

    public JsonNode getDetails() {
        return details;
    }

    public void setDetails(JsonNode details) {
        this.details = details;
    }

    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
    }

    public TestCaseState getState() {
        return state;
    }

    public void setState(TestCaseState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ExecHistoryVo [id=" + id + ", planId=" + planId + ", planName=" + planName
                + ", type=" + type + ", taskState=" + taskState + ", triggerTime=" + triggerTime
                + ", details=" + details + ", testType=" + testType + ", state=" + state + "]";
    }
}
