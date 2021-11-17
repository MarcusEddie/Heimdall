/**
 * 
 */
package org.iman.Heimdallr.constants.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author ey
 *
 */
public enum FuncTag implements BaseEnum {

    API_DECLARATION(1, "apiDeclaration"), 
    API_TEST_CASE_DETAILS(2, "apiTestCaseDetails"),
    APP_STRUCTURE(3, "appStructure"), 
    CASE_GENERAL_INFO(4, "caseGeneralInfo"),
    DATA_HISTORY(5, "dataHistory"), 
    MIND_RAW_DATA(6, "mindRawData"),
    PAGE_DECLARATION(7, "pageDeclaration"), 
    TEST_DB_CONNECTION_INFO(8, "testDBConnectionInfo"),
    UI_TEST_CASE_DETAILS(9, "uiTestCaseDetails");

    private Integer code;
    private String val;

    private FuncTag(Integer code, String val) {
        this.code = code;
        this.val = val;
    }

    public Integer getCode() {
        return code;
    }

    @JsonValue
    public String getVal() {
        return val;
    }

    @Override
    public Integer getDigit() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.val;
    }

}
