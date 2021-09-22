/**
 * 
 */
package org.iman.Heimdallr.entity;

import java.io.Serializable;

import org.iman.Heimdallr.constants.enums.CasePriority;
import org.iman.Heimdallr.constants.enums.ResultCheckMode;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ey
 *
 */
@TableName("apiTestCaseDetails")
public class ApiTestCase extends BaseEntity implements Serializable{

    private static final long serialVersionUID = 2603614749700192418L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long appId;
    private Long generalCaseId;
    private CasePriority priority;
    private Long apiId;
    private String name;
    private JsonNode header;
    private String steps;
    private JsonNode parameters;
    private JsonNode expectedResult;
    private ResultCheckMode resultCheckMode;
    private Long dbConnId;
    private String querySql;
    
    public ApiTestCase() {
        super();
    }

    public ApiTestCase(Long id) {
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

    @Override
    public String toString() {
        return "ApiTestCase [id=" + id + ", appId=" + appId + ", generalCaseId=" + generalCaseId
                + ", priority=" + priority + ", apiId=" + apiId + ", name=" + name + ", header="
                + header + ", steps=" + steps + ", parameters=" + parameters + ", expectedResult="
                + expectedResult + ", resultCheckMode=" + resultCheckMode + ", dbConnId=" + dbConnId
                + ", querySql=" + querySql + "]";
    }
}
