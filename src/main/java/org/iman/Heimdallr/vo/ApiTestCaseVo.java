/**
 * 
 */
package org.iman.Heimdallr.vo;

import java.io.Serializable;

import org.iman.Heimdallr.constants.enums.CasePriority;
import org.iman.Heimdallr.constants.enums.ResultCheckMode;
import org.iman.Heimdallr.constants.enums.TestCaseState;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ey
 *
 */
public class ApiTestCaseVo implements Serializable {

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
    @JsonProperty("apiId")
    private Long apiId;
    @JsonProperty("api")
    private ApiDeclarationVo api;
    @JsonProperty("name")
    private String name;
    @JsonProperty("header")
    private JsonNode header;
    @JsonProperty("steps")
    private String steps;
    @JsonProperty("parameters")
    private JsonNode parameters;
    @JsonProperty("expectedResult")
    private JsonNode expectedResult;
    @JsonProperty("resultCheckMode")
    private ResultCheckMode resultCheckMode;
    @JsonProperty("dbConnId")
    private Long dbConnId;
    @JsonProperty("dbConnName")
    private String dbConnName;
    @JsonProperty("querySql")
    private String querySql;
    @JsonProperty("state")
    private TestCaseState state;
    
    public ApiTestCaseVo() {
        super();
    }

    public ApiTestCaseVo(Long id) {
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

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonNode getHeader() {
        return header;
    }

    public void setHeader(JsonNode header) {
        this.header = header;
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

    public ResultCheckMode getResultCheckMode() {
        return resultCheckMode;
    }

    public void setResultCheckMode(ResultCheckMode resultCheckMode) {
        this.resultCheckMode = resultCheckMode;
    }

    public Long getDbConnId() {
        return dbConnId;
    }

    public void setDbConnId(Long dbConnId) {
        this.dbConnId = dbConnId;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }
   
    public String getGeneralCaseName() {
        return generalCaseName;
    }

    public void setGeneralCaseName(String generalCaseName) {
        this.generalCaseName = generalCaseName;
    }

    public ApiDeclarationVo getApi() {
        return api;
    }

    public void setApi(ApiDeclarationVo api) {
        this.api = api;
    }

    public String getDbConnName() {
        return dbConnName;
    }

    public void setDbConnName(String dbConnName) {
        this.dbConnName = dbConnName;
    }

    public TestCaseState getState() {
        return state;
    }

    public void setState(TestCaseState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ApiTestCaseVo [id=" + id + ", appId=" + appId + ", generalCaseId=" + generalCaseId
                + ", generalCaseName=" + generalCaseName + ", priority=" + priority + ", apiId="
                + apiId + ", api=" + api + ", name=" + name + ", header=" + header + ", steps="
                + steps + ", parameters=" + parameters + ", expectedResult=" + expectedResult
                + ", resultCheckMode=" + resultCheckMode + ", dbConnId=" + dbConnId
                + ", dbConnName=" + dbConnName + ", querySql=" + querySql + ", state=" + state
                + "]";
    }
}
