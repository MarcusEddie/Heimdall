package org.iman.Heimdallr.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.iman.Heimdallr.constants.enums.CasePriority;
import org.iman.Heimdallr.constants.enums.TaskState;
import org.iman.Heimdallr.constants.enums.TaskType;
import org.iman.Heimdallr.constants.enums.TestCaseState;
import org.iman.Heimdallr.constants.enums.TestType;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskQueueVo implements Serializable{

    private static final long serialVersionUID = -5044024430547135799L;
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
    @JsonProperty("priority")
    private CasePriority priority;
    @JsonProperty("triggerTime")
    private LocalDateTime triggerTime;
    @JsonProperty("testType")
    private TestType testType;
    @JsonProperty("state")
    private TestCaseState state;

    public TaskQueueVo() {
        super();
    }

    public TaskQueueVo(Long id) {
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

    public CasePriority getPriority() {
        return priority;
    }

    public void setPriority(CasePriority priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "TaskQueueVo [id=" + id + ", planId=" + planId + ", planName=" + planName + ", type="
                + type + ", taskState=" + taskState + ", priority=" + priority + ", triggerTime="
                + triggerTime + ", testType=" + testType + ", state=" + state + "]";
    }

}
