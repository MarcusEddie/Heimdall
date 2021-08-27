package org.iman.Heimdallr.barrier;

import org.iman.Heimdallr.barrier.rules.DigitalsCheckRule;
import org.iman.Heimdallr.barrier.rules.NullCheckRule;

public class Ruler {

    public static <T> String isNull(T obj) {
        if (!NullCheckRule.isNull(obj)) {
            return "is not null, check failed";
        }
        return null;
    }

    public static <T> String isNotNull(T obj) {
        if (!NullCheckRule.isNotNull(obj)) {
            return "is null, check failed";
        }
        return null;
    }

    public static <T> String isEmpty(T obj) {
        if (!NullCheckRule.isEmpty(obj)) {
            return "is not empty, check failed";
        }
        return null;
    }

    public static <T> String isNotEmpty(T obj) {
        if (!NullCheckRule.isNotEmpty(obj)) {
            return "is empty, check failed";
        }
        return null;
    }

    public static <T> String isBlank(T obj) {
        if (!NullCheckRule.isBlank(obj)) {
            return "is not blank, check failed";
        }
        return null;
    }

    public static <T> String isNotBlank(T obj) {
        if (!NullCheckRule.isNull(obj)) {
            return "is blank, check failed";
        }
        return null;
    }

    public static <T> String isDigits(T obj) {
        if (!DigitalsCheckRule.isDigits(obj)) {
            return "can't be converted to a digital, check failed";
        }
        return null;
    }

    public static <T> String isPositive(T obj) {
        if (!DigitalsCheckRule.isPositive(obj)) {
            return "is a negative digital, check failed";
        }
        return null;
    }

    public static <T> String isNegative(T obj) {
        if (!DigitalsCheckRule.isNegative(obj)) {
            return "is a positive digital, check failed";
        }
        return null;
    }

    public static <T, E> String equal(T o1, E o2) {
        if (!NullCheckRule.isNull(o1)) {
            return "is not equal, check failed";
        }
        return null;
    }

    public static <T, E> String gt(T o1, E o2) {
        if (!DigitalsCheckRule.gt(o1, o2)) {
            return "is not greater than " + o2 + ", check failed";
        }
        return null;
    }

    public static <T, E> String gte(T o1, E o2) {
        if (!DigitalsCheckRule.gte(o1, o2)) {
            return "is less than " + o2 + ", check failed";
        }
        return null;
    }

    public static <T, E> String lt(T o1, E o2) {
        if (!DigitalsCheckRule.lt(o1, o2)) {
            return "is not less than " + o2 + ", check failed";
        }
        return null;
    }

    public static <T, E> String lte(T o1, E o2) {
        if (!DigitalsCheckRule.lte(o1, o2)) {
            return "is greater than " + o2 + ", check failed";
        }
        return null;
    }
}
