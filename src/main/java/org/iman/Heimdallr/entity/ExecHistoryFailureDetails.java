package org.iman.Heimdallr.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;

@TableName("execHistoryFailureDetails")
public class ExecHistoryFailureDetails extends BaseEntity implements Serializable{

    private static final long serialVersionUID = -5020736372597878682L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long historyId;

    private String caseName;

    private JsonNode caseError;

    public ExecHistoryFailureDetails() {
        super();
    }

    public ExecHistoryFailureDetails(Long id) {
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

    @Override
    public String toString() {
        return "ExecHistoryFailureDetails [id=" + id + ", historyId=" + historyId + ", caseName="
                + caseName + ", caseError=" + caseError + ", getCreateBy()=" + getCreateBy()
                + ", getEnabled()=" + getEnabled() + ", getDeleted()=" + getDeleted()
                + ", getCreateTime()=" + getCreateTime() + ", getModifiedBy()=" + getModifiedBy()
                + ", getModifiedTime()=" + getModifiedTime() + "]";
    }
}
