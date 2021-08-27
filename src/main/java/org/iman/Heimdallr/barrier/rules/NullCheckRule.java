/**
 * 
 */
package org.iman.Heimdallr.barrier.rules;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ey
 *
 */
public class NullCheckRule {

    public static <T> Boolean isNull(T obj) {
        if (null == obj) {
            return true;
        }
        return false;
    }

    public static <T> Boolean isNotNull(T obj) {
        return !isNull(obj);
    }

    public static <T> Boolean isEmpty(T obj) {
        Boolean rs = false;
        if (obj instanceof String || obj instanceof CharSequence || obj instanceof Character) {
            rs = StringUtils.isEmpty((CharSequence) obj);
        }

        if (obj instanceof Collection<?>) {
            rs = CollectionUtils.isEmpty((Collection<?>) obj);
        }

        if (obj instanceof Map<?, ?>) {
            rs = MapUtils.isEmpty((Map<?, ?>) obj);
        }

        return rs;
    }

    public static <T> Boolean isNotEmpty(T obj) {
        return !isEmpty(obj);
    }

    public static <T> Boolean isBlank(T obj) {
        Boolean rs = false;
        if (obj instanceof String || obj instanceof CharSequence || obj instanceof Character) {
            rs = StringUtils.isBlank((CharSequence) obj);
        }

        return rs;
    }

    public static <T> Boolean isNotBlank(T obj) {
        return !isBlank(obj);
    }

}
