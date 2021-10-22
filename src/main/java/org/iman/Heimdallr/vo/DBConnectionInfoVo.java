/**
 * 
 */
package org.iman.Heimdallr.vo;

import java.io.Serializable;

import org.iman.Heimdallr.constants.enums.DBType;
import org.iman.Heimdallr.constants.enums.TestCaseState;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ey
 *
 */
public class DBConnectionInfoVo implements Serializable{

    private static final long serialVersionUID = 6263434502017224475L;

    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("appId")
    private Long appId;
    
    @JsonProperty("appName")
    private String appName;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("dbType")
    private DBType dbType;
    
    @JsonProperty("url")
    private String url;
    
    @JsonProperty("userName")
    private String userName;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("header")
    private JsonNode header;

    @JsonProperty("state")
    private TestCaseState state;
    
    public DBConnectionInfoVo() {
        super();
    }

    public DBConnectionInfoVo(Long id) {
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

    
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public TestCaseState getState() {
        return state;
    }

    public void setState(TestCaseState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "DBConnectionInfoVo [id=" + id + ", appId=" + appId + ", appName=" + appName
                + ", name=" + name + ", dbType=" + dbType + ", url=" + url + ", userName="
                + userName + ", password=" + password + ", header=" + header + ", state=" + state
                + "]";
    }

}
