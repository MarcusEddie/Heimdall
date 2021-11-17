package org.iman.Heimdallr.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.iman.Heimdallr.constants.enums.Action;
import org.iman.Heimdallr.constants.enums.FuncTag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class DataHistoryVo implements Serializable {

    private static final long serialVersionUID = -7390685603814117818L;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("action")
    private Action actionType;
    
    @JsonProperty("funcTag")
    private FuncTag tableName;

    @JsonProperty("dataId")
    private Long dataId;

    @JsonProperty("operatorName")
    private String createBy;

    @JsonProperty("operationTime")
    private LocalDateTime createTime;

    @JsonIgnore
    private JsonNode oldData;
    @JsonIgnore
    private JsonNode newData;

    @JsonProperty("oldData")
    private String oldDataVal;

    @JsonProperty("newData")
    private String newDataVal;
    
    public DataHistoryVo() {
        super();
    }

    public DataHistoryVo(Long id) {
        this.id = id;
    }

    public DataHistoryVo(FuncTag tableName, Long dataId) {
        this.tableName = tableName;
        this.dataId = dataId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Action getActionType() {
        return actionType;
    }

    public void setActionType(Action actionType) {
        this.actionType = actionType;
    }

    public FuncTag getTableName() {
        return tableName;
    }

    public void setTableName(FuncTag tableName) {
        this.tableName = tableName;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public JsonNode getOldData() {
        return oldData;
    }

    public void setOldData(JsonNode oldData) {
        this.oldData = oldData;
    }

    public JsonNode getNewData() {
        return newData;
    }

    public void setNewData(JsonNode newData) {
        this.newData = newData;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getOldDataVal() {
        return oldDataVal;
    }

    public void setOldDataVal(String oldDataVal) {
        this.oldDataVal = oldDataVal;
    }

    public String getNewDataVal() {
        return newDataVal;
    }

    public void setNewDataVal(String newDataVal) {
        this.newDataVal = newDataVal;
    }

    @Override
    public String toString() {
        return "DataHistoryVo [id=" + id + ", actionType=" + actionType + ", tableName=" + tableName
                + ", dataId=" + dataId + ", createBy=" + createBy + ", createTime=" + createTime
                + ", oldData=" + oldData + ", newData=" + newData + ", oldDataVal=" + oldDataVal
                + ", newDataVal=" + newDataVal + "]";
    }
}
