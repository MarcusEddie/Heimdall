/**
 * 
 */
package org.iman.Heimdallr.vo;

import java.io.Serializable;

import org.iman.Heimdallr.constants.enums.CasePriority;
import org.iman.Heimdallr.constants.enums.TestCaseState;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ey
 *
 */
public class UiTestCaseVo implements Serializable {

    private static final long serialVersionUID = 5970435435666564364L;

    @JsonProperty("id")
    private Long id;
    @JsonProperty("appId")
    private Long appId;
    @JsonProperty("generalCaseId")
    private Long generalCaseId;
    @JsonProperty("generalCaseName")
    private String generalCaseName;
    @JsonProperty("priority")
    private CasePriority priority;
    @JsonProperty("pageId")
    private Long pageId;
    @JsonProperty("page")
    private UiPageVo page;
    @JsonProperty("name")
    private String name;
    @JsonProperty("steps")
    private String steps;
    @JsonProperty("parameters")
    private JsonNode parameters;
    @JsonProperty("expectedResult")
    private JsonNode expectedResult;
    @JsonProperty("state")
    private TestCaseState state;

    public UiTestCaseVo() {
        super();
    }

    public UiTestCaseVo(Long id) {
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

    public String getGeneralCaseName() {
        return generalCaseName;
    }

    public void setGeneralCaseName(String generalCaseName) {
        this.generalCaseName = generalCaseName;
    }

    public TestCaseState getState() {
        return state;
    }

    public void setState(TestCaseState state) {
        this.state = state;
    }

    public UiPageVo getPage() {
        return page;
    }

    public void setPage(UiPageVo page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "UiTestCaseVo [id=" + id + ", appId=" + appId + ", generalCaseId=" + generalCaseId
                + ", generalCaseName=" + generalCaseName + ", priority=" + priority + ", pageId="
                + pageId + ", page=" + page + ", name=" + name + ", steps=" + steps
                + ", parameters=" + parameters + ", expectedResult=" + expectedResult + ", state="
                + state + "]";
    }
}
