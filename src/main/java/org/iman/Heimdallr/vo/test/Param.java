package org.iman.Heimdallr.vo.test;

import java.io.Serializable;

import org.iman.Heimdallr.constants.enums.TaskState;

public class Param implements Serializable{

    private static final long serialVersionUID = 689961096310519921L;

    private String name;
    
    private TaskState taskState;
    
    private Long typeId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return "Param [name=" + name + ", taskState=" + taskState + ", typeId=" + typeId + "]";
    }
    
}
