/**
 * 
 */
package org.iman.Heimdallr.constants.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author ey
 *
 */
public enum NodeDirection {

    RIGHT("right"),
    LEFT("left");
    
    private String label;

    private NodeDirection(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return this.label;
    }
    
    @JsonCreator
    public static NodeDirection valueof(String val) {
        for (NodeDirection direction : NodeDirection.values()) {
            if (direction.getLabel().equalsIgnoreCase(val)) {
                return direction;
            }
        }
        return null;
        
    }
}
