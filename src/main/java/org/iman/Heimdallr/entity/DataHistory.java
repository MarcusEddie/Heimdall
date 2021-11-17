/**
 * 
 */
package org.iman.Heimdallr.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.iman.Heimdallr.constants.enums.Action;
import org.iman.Heimdallr.constants.enums.FuncTag;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ey
 *
 */
@TableName("dataHistory")
public class DataHistory implements Serializable{

    private static final long serialVersionUID = 1710175780584858141L;

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Action actionType;
    
    private Long dataId;
    
    private FuncTag tableName;
    
    private JsonNode oldData;
    
    private JsonNode newData;

    private String createBy;
    
    private LocalDateTime createTime;
    
    public DataHistory() {
        super();
    }

    public DataHistory(Long id) {
        this.id = id;
    }

    public DataHistory(Long dataId, FuncTag tableName) {
        this.dataId = dataId;
        this.tableName = tableName;
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

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public FuncTag getTableName() {
        return tableName;
    }

    public void setTableName(FuncTag tableName) {
        this.tableName = tableName;
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

    @Override
    public String toString() {
        return "DataHistory [id=" + id + ", actionType=" + actionType + ", dataId=" + dataId
                + ", tableName=" + tableName + ", oldData=" + oldData + ", newData=" + newData
                + ", createBy=" + createBy + ", createTime=" + createTime + "]";
    }

}
