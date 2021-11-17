/**
 * 
 */
package org.iman.Heimdallr.entity;

import java.io.Serializable;

import org.iman.Heimdallr.constants.enums.CasePriority;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ey
 *
 */
@TableName("uiTestCaseDetails")
public class UiTestCase extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 2603614749700192418L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long appId;
    private Long generalCaseId;
    private CasePriority priority;
    private Long pageId;
    private String name;
    private String steps;
    private JsonNode parameters;
    private JsonNode expectedResult;

    public UiTestCase() {
        super();
    }

    public UiTestCase(Long id) {
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

    public Long getGeneralCaseId() {
        return generalCaseId;
    }

    public void setGeneralCaseId(Long generalCaseId) {
        this.generalCaseId = generalCaseId;
    }

    public CasePriority getPriority() {
        return priority;
    }

    public void setPriority(CasePriority priority) {
        this.priority = priority;
    }

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public JsonNode getParameters() {
        return parameters;
    }

    public void setParameters(JsonNode parameters) {
        this.parameters = parameters;
    }

    public JsonNode getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(JsonNode expectedResult) {
        this.expectedResult = expectedResult;
    }

    @Override
    public String toString() {
        return "UiTestCase [id=" + id + ", appId=" + appId + ", generalCaseId=" + generalCaseId
                + ", priority=" + priority + ", pageId=" + pageId + ", name=" + name + ", steps="
                + steps + ", parameters=" + parameters + ", expectedResult=" + expectedResult + "]";
    }
}
