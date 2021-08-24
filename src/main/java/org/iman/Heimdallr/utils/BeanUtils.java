/**
 * 
 */
package org.iman.Heimdallr.utils;

import java.lang.reflect.InvocationTargetException;

import org.springframework.cglib.beans.BeanCopier;

/**
 * @author ey
 *
 */
public class BeanUtils {

    public static <E, T> T copy(E source, Class<T> targetClass)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        if (null == source) {
            return null;
        }
        
        T target = targetClass.getDeclaredConstructor().newInstance();
        BeanCopier copier = BeanCopier.create(source.getClass(), targetClass, false);
        copier.copy(source, target, null);
        return target;
    }
}
