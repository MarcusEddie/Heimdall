/**
 * 
 */
package org.iman.Heimdallr.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author ey
 *
 */
@TableName("mindData")
public class MindData extends BaseEntity implements Serializable{

    private static final long serialVersionUID = -6150632610495290935L;

    @TableId(type = IdType.AUTO)
    private Long id;
    
   private Long appId;
    
    private Long moduleId;
    
    private Long functionId; 
    
    private String mapData;

    public MindData() {
        super();
    }

    public MindData(Long id) {
        super();
        this.id = id;
    }

    public MindData(String mapData) {
        super();
        this.mapData = mapData;
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

    public String getMapData() {
        return mapData;
    }

    public void setMapData(String mapData) {
        this.mapData = mapData;
    }

    @Override
    public String toString() {
        return "MindData [id=" + id + ", appId=" + appId + ", moduleId=" + moduleId
                + ", functionId=" + functionId + ", mapData=" + mapData + ", getCreateBy()="
                + getCreateBy() + ", getEnabled()=" + getEnabled() + ", getDeleted()="
                + getDeleted() + ", getCreateTime()=" + getCreateTime() + ", getModifiedBy()="
                + getModifiedBy() + ", getModifiedTime()=" + getModifiedTime() + "]";
    }
    
}
