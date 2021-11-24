/**
 * 
 */
package org.iman.Heimdallr.utils;

/**
 * @author ey
 *
 */
public class ObjectUtils {

    public static Boolean isNotEmpty(Object obj) {
        if (obj.toString().equalsIgnoreCase("null")) {
            return false;
        }
        return !org.springframework.util.ObjectUtils.isEmpty(obj);
    }
    
    public static Boolean isEmpty(Object obj) {
        if (obj.toString().equalsIgnoreCase("null")) {
            return true;
        }
        return org.springframework.util.ObjectUtils.isEmpty(obj);
    }
}
