/**
 * 
 */
package org.iman.Heimdallr.constants.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author ey
 *
 */
public enum TestCaseState {

    ALL(99999, "all"),
    ENABLED(1, "enabled"),
    DISABLED(0, "disabled");
    
    private Integer status;
    
    private String label;

    private TestCaseState(Integer status, String label) {
        this.status = status;
        this.label = label;
    }

    public Integer getStatus() {
        return status;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
    
    public static TestCaseState valueOf(Integer status) {
        for (TestCaseState caseState : TestCaseState.values()) {
            if (caseState.getStatus().compareTo(status) == 0) {
                return caseState;
            }
        }
        return null;
    }
    
    public static TestCaseState valueOf(Boolean status) {
        Integer temp = status ? 1 : 0;
        for (TestCaseState caseState : TestCaseState.values()) {
            if (caseState.getStatus().compareTo(temp) == 0) {
                return caseState;
            }
        }
        return null;
    }
    
    public static Boolean convertToBoolean(TestCaseState state) {
        if (null == state) {
            return true;
        }
        
        Integer temp = -1;
        for (TestCaseState caseState : TestCaseState.values()) {
            if (caseState.getStatus().compareTo(state.getStatus()) == 0) {
                temp = caseState.getStatus();
            }
        }
        if (temp.compareTo(0) == 0) {
            return false;
        } else if (temp.compareTo(1) == 0) {
            return true;
        }
        
        return null;
    }
}
