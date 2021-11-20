/**
 * 
 */
package org.iman.Heimdallr.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.iman.Heimdallr.constants.enums.TaskState;
import org.iman.Heimdallr.constants.enums.TaskType;
import org.iman.Heimdallr.constants.enums.TestType;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ey
 *
 */
@TableName("execHistory")
public class ExecHistory extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5860612687640279955L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long planId;

    private String planName;
    
    private TaskType type;
    
    private TaskState taskState;

    private LocalDateTime triggerTime;
    
    private JsonNode details;

    private TestType testType;

    public ExecHistory() {
        super();
    }

    public ExecHistory(Long id) {
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

    public JsonNode getDetails() {
        return details;
    }

    public void setDetails(JsonNode details) {
        this.details = details;
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

    @Override
    public String toString() {
        return "ExecHistory [id=" + id + ", planId=" + planId + ", planName=" + planName + ", type="
                + type + ", taskState=" + taskState + ", triggerTime=" + triggerTime + ", details="
                + details + ", testType=" + testType + ", getCreateBy()=" + getCreateBy()
                + ", getEnabled()=" + getEnabled() + ", getDeleted()=" + getDeleted()
                + ", getCreateTime()=" + getCreateTime() + ", getModifiedBy()=" + getModifiedBy()
                + ", getModifiedTime()=" + getModifiedTime() + "]";
    }
}
