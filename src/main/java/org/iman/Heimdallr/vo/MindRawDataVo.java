/**
 * 
 */
package org.iman.Heimdallr.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ey
 *
 */
public class MindRawDataVo implements Serializable {

    private static final long serialVersionUID = -4369775428131424142L;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("appId")
    private Long appId;

    @JsonProperty("moduleId")
    private Long moduleId;

    @JsonProperty("functionId")
    private Long functionId;

    @JsonProperty("rawJson")
    private RootNodeVo rawNode;

    private String rawData;
    
    public MindRawDataVo() {
        super();
    }

    public MindRawDataVo(Long id) {
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

    public RootNodeVo getRawNode() {
        return rawNode;
    }

    public void setRawNode(RootNodeVo rawNode) {
        this.rawNode = rawNode;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    @Override
    public String toString() {
        return "MindDataVo [id=" + id + ", appId=" + appId + ", moduleId=" + moduleId
                + ", functionId=" + functionId + ", rawNode=" + rawNode + "]";
    }
}
