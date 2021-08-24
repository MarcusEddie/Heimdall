/**
 * 
 */
package org.iman.Heimdallr.constant;

/**
 * @author ey
 *
 */
public enum AppLevel {

    SYSTEM(1), MODULE(2), FUNCTION(3);

    private Integer level;

    private AppLevel(Integer level) {
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }

    public static AppLevel valueOf(Integer level) {
        for (AppLevel appLevel : AppLevel.values()) {
            if (appLevel.getLevel().compareTo(level) == 0) {
                return appLevel;
            }
        }
        return null;
    }
}
