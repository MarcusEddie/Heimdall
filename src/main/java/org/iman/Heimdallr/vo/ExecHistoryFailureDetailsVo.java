/**
 * 
 */
package org.iman.Heimdallr.vo;

import java.io.Serializable;

import org.iman.Heimdallr.constants.enums.TestCaseState;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author ey
 *
 */
public class ExecHistoryFailureDetailsVo implements Serializable{

    private static final long serialVersionUID = -3115505096927451539L;

    @JsonProperty("id")
    private Long id;
    @JsonProperty("historyId")
    private Long historyId;
    @JsonProperty("caseName")
    private String caseName;
    @JsonIgnore
    private JsonNode caseError;
    @JsonProperty("caseError")
    private String caseErrorVal;

    private TestCaseState state;
    
    public ExecHistoryFailureDetailsVo() {
        super();
    }

    public ExecHistoryFailureDetailsVo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public JsonNode getCaseError() {
        return caseError;
    }

    public void setCaseError(JsonNode caseError) {
        this.caseError = caseError;
    }

    public TestCaseState getState() {
        return state;
    }

    public void setState(TestCaseState state) {
        this.state = state;
    }

    public String getCaseErrorVal() {
        return caseErrorVal;
    }

    public void setCaseErrorVal(String caseErrorVal) {
        this.caseErrorVal = caseErrorVal;
    }

    @Override
    public String toString() {
        return "ExecHistoryFailureDetailsVo [id=" + id + ", historyId=" + historyId + ", caseName="
                + caseName + ", caseError=" + caseError + ", caseErrorVal=" + caseErrorVal
                + ", state=" + state + "]";
    }
}
