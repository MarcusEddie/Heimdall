/**
 * 
 */
package org.iman.Heimdallr.entity;

import java.io.Serializable;

import org.iman.Heimdallr.constants.enums.DBType;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ey
 *
 */
@TableName("testDBConnectionInfo")
public class DBConnectionInfo extends BaseEntity implements Serializable{

    private static final long serialVersionUID = 5494364216246166334L;

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long appId;
    
    private String name;
    
    private DBType dbType;
    
    private String url;
    
    private String userName;
    
    private String password;
    
    private JsonNode header;

    public DBConnectionInfo() {
        super();
    }

    public DBConnectionInfo(Long id) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DBType getDbType() {
        return dbType;
    }

    public void setDbType(DBType dbType) {
        this.dbType = dbType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JsonNode getHeader() {
        return header;
    }

    public void setHeader(JsonNode header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return "DBConnectionInfo [id=" + id + ", appId=" + appId + ", name=" + name + ", dbType="
                + dbType + ", url=" + url + ", userName=" + userName + ", password=" + password
                + ", header=" + header + ", getCreateBy()=" + getCreateBy() + ", getEnabled()="
                + getEnabled() + ", getDeleted()=" + getDeleted() + ", getCreateTime()="
                + getCreateTime() + ", getModifiedBy()=" + getModifiedBy() + ", getModifiedTime()="
                + getModifiedTime() + "]";
    }

}
