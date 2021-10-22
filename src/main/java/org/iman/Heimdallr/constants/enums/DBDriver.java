/**
 * 
 */
package org.iman.Heimdallr.constants.enums;

/**
 * @author ey
 *
 */
public enum DBDriver {
    MySQL("com.mysql.cj.jdbc.Driver"),
    HANA("com.sap.db.jdbc.Driver");

    private String driver;

    private DBDriver(String driver) {
        this.driver = driver;
    }

    public String getDriver() {
        return driver;
    }
}
