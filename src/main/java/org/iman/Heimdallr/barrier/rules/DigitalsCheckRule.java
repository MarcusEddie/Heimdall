package org.iman.Heimdallr.barrier.rules;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class DigitalsCheckRule {

    public static <T> Boolean isDigits(T obj) {
        Boolean rs = false;
        if (NullCheckRule.isNull(obj)) {
            return rs;
        }
        if (obj instanceof Number) {
            rs = true;
        }
        if (obj instanceof String && NullCheckRule.isNotBlank(obj)) {
            String tempVal = (String) obj;
            if (tempVal.indexOf("+") == 0) {
                tempVal = tempVal.substring(1);
            }
            rs = NumberUtils.isCreatable(tempVal);
        }
        if (obj instanceof Character && NullCheckRule.isNotBlank(obj)) {
            Character tempVal = (Character) obj;
            rs = CharUtils.isAsciiNumeric(tempVal);
        }
        if (obj instanceof CharSequence && NullCheckRule.isNotBlank(obj)) {
            CharSequence tempVal = (CharSequence) obj;

            if (Character.valueOf('+').compareTo(tempVal.charAt(0)) == 0) {
                tempVal = tempVal.subSequence(1, tempVal.length());
            }
            rs = NumberUtils.isCreatable(String.valueOf(tempVal));
        }

        return rs;
    }

    public static <T> Boolean isPositive(T obj) {
        Boolean rs = false;
        rs = isDigits(obj);
        if (!rs) {
            return rs;
        }
        BigDecimal bigDecimal = convertToDigits(obj);
        if (null == bigDecimal) {
            return rs;
        }

        rs = bigDecimal.compareTo(new BigDecimal(0)) == 1;

        return rs;
    }

    public static <T> Boolean isNegative(T obj) {
        Boolean rs = false;
        rs = isDigits(obj);
        if (!rs) {
            return rs;
        }
        BigDecimal bigDecimal = convertToDigits(obj);
        if (null == bigDecimal) {
            return rs;
        }

        rs = bigDecimal.compareTo(new BigDecimal(0)) == -1;

        return rs;
    }

    public static <T, E> boolean gt(T obj, E o2) {
        Boolean rs = false;
        if (!isDigits(obj) || !isDigits(o2)) {
            return rs;
        }
        BigDecimal bigDecimal = convertToDigits(obj);
        BigDecimal operand = convertToDigits(o2);
        if (null == bigDecimal || null == operand) {
            return rs;
        }
        rs = bigDecimal.compareTo(operand) == 1;
        return rs;
    }

    public static <T, E> boolean gte(T obj, E o2) {
        Boolean rs = false;
        if (!isDigits(obj) || !isDigits(o2)) {
            return rs;
        }
        BigDecimal bigDecimal = convertToDigits(obj);
        BigDecimal operand = convertToDigits(o2);
        if (null == bigDecimal || null == operand) {
            return rs;
        }
        rs = bigDecimal.compareTo(operand) != -1;
        return rs;
    }

    public static <T, E> boolean lt(T obj, E o2) {
        Boolean rs = false;
        if (!isDigits(obj) || !isDigits(o2)) {
            return rs;
        }
        BigDecimal bigDecimal = convertToDigits(obj);
        BigDecimal operand = convertToDigits(o2);
        if (null == bigDecimal || null == operand) {
            return rs;
        }
        rs = bigDecimal.compareTo(operand) == -1;
        return rs;
    }

    public static <T, E> boolean lte(T obj, E o2) {
        Boolean rs = false;
        if (!isDigits(obj) || !isDigits(o2)) {
            return rs;
        }
        BigDecimal bigDecimal = convertToDigits(obj);
        BigDecimal operand = convertToDigits(o2);
        if (null == bigDecimal || null == operand) {
            return rs;
        }
        rs = bigDecimal.compareTo(operand) != 1;
        return rs;
    }

    private static <T> BigDecimal convertToDigits(T obj) {

        if (obj instanceof Byte) {
            return new BigDecimal((Byte) obj);
        }
        if (obj instanceof Double) {
            return new BigDecimal((Double) obj);
        }
        if (obj instanceof Float) {
            return new BigDecimal((Float) obj);
        }
        if (obj instanceof Integer) {
            return new BigDecimal((Integer) obj);
        }
        if (obj instanceof Long) {
            return new BigDecimal((Long) obj);
        }
        if (obj instanceof Short) {
            return new BigDecimal((Short) obj);
        }
        if (obj instanceof BigInteger) {
            return new BigDecimal((BigInteger) obj);
        }
        if (obj instanceof Byte) {
            return new BigDecimal((Byte) obj);
        }
        if (obj instanceof String || obj instanceof CharSequence) {
            return new BigDecimal((String) obj);
        }
        if (obj instanceof Character) {
            return new BigDecimal((Character) obj);
        }
        return null;
    }
}
