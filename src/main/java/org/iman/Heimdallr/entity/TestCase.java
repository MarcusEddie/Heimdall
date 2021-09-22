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
@TableName("caseGeneralInfo")
public class TestCase extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -6977750650710351943L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long appId;

    private String appName;

    private Long moduleId;

    private String moduleName;

    private Long functionId;

    private String functionName;

    private String name;

    private String description;

    private String step;

    private String results;

    private Long rawDataId;

    public TestCase() {
        super();
    }

    public TestCase(Long id) {
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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public Long getRawDataId() {
        return rawDataId;
    }

    public void setRawDataId(Long rawDataId) {
        this.rawDataId = rawDataId;
    }

    @Override
    public String toString() {
        return "TestCase [id=" + id + ", appId=" + appId + ", appName=" + appName + ", moduleId="
                + moduleId + ", moduleName=" + moduleName + ", functionId=" + functionId
                + ", functionName=" + functionName + ", name=" + name + ", description="
                + description + ", step=" + step + ", results=" + results + ", rawDataId="
                + rawDataId + "]";
    }

}
