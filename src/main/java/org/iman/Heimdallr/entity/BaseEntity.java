/**
 * 
 */
package org.iman.Heimdallr.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.iman.Heimdallr.constants.Consts;

/**
 * @author ey
 *
 */
public class BaseEntity implements Serializable{

    private static final long serialVersionUID = -5588154147142244895L;

    private Boolean enabled;
    
    private Boolean deleted;
    
    private String createBy;
    
    private LocalDateTime createTime;
    
    private String modifiedBy;
    
    private LocalDateTime modifiedTime;

    public BaseEntity() {
        super();
        setCreateBy(Consts.SYSTEM_ADMIN);
        setCreateTime(LocalDateTime.now());
        setEnabled(true);
        setDeleted(false);
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    @Override
    public String toString() {
        return "BaseEntity [enabled=" + enabled + ", deleted=" + deleted + ", createBy=" + createBy
                + ", createTime=" + createTime + ", modifiedBy=" + modifiedBy + ", modifiedTime="
                + modifiedTime + "]";
    }
}
