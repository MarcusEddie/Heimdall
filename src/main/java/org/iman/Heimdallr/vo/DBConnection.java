/**
 * 
 */
package org.iman.Heimdallr.vo;

import java.io.Serializable;

/**
 * @author ey
 *
 */
public class DBConnection implements Serializable{

    private static final long serialVersionUID = -5217729272828948153L;

    private Boolean status;
    
    private String errorMsg;

    public DBConnection() {
        super();
    }

    public DBConnection(Boolean status) {
        this.status = status;
    }

    public DBConnection(Boolean status, String errorMsg) {
        this.status = status;
        this.errorMsg = errorMsg;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "DBConnection [status=" + status + ", errorMsg=" + errorMsg + "]";
    }
}
