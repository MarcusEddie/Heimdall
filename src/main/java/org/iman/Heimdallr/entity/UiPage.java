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
@TableName("pageDeclaration")
public class UiPage extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3243197995708513614L;
    
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long appId;

    private Long moduleId;

    private Long functionId;

    private String name;

    private String url;

    public UiPage() {
        super();
    }

    public UiPage(Long id) {
        super();
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UiPage [id=" + id + ", appId=" + appId + ", moduleId=" + moduleId + ", functionId="
                + functionId + ", name=" + name + ", url=" + url + ", getCreateBy()="
                + getCreateBy() + ", getEnabled()=" + getEnabled() + ", getDeleted()="
                + getDeleted() + ", getCreateTime()=" + getCreateTime() + ", getModifiedBy()="
                + getModifiedBy() + ", getModifiedTime()=" + getModifiedTime() + "]";
    }

}
