/**
 * 
 */
package org.iman.Heimdallr.constant;

/**
 * @author ey
 *
 */
public enum Rule {

    IS_NULL(1),
    IS_NOT_NULL(2),
    IS_EMPTY(3),
    IS_NOT_EMPTY(4),
    IS_BLANK(5),
    IS_NOT_BLANK(6),
    
    IS_DIGITAL(8),
    IS_POSITIVE(9),
    IS_NEGATIVE(10),
    EQUALS(7),
    GREATER_THAN(11),
    GREATER_THAN_OR_EQUAL(12),
    LESS_THAN(13),
    LESS_THAN_OR_EQUAL(14),
    
    ;
    
    private Integer ruleId;

    private Rule(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public static Rule valueOf(Integer id) {
        for (Rule rule : Rule.values()) {
            if (rule.getRuleId().compareTo(id) == 0) {
                return rule;
            }
        }
        return null;
    }
}
