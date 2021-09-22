/**
 * 
 */
package org.iman.Heimdallr.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.iman.Heimdallr.exception.DataConversionException;
import org.springframework.cglib.beans.BeanCopier;

/**
 * @author ey
 *
 */
public class BeanUtils {

    public static <E, T> Optional<T> copy(E source, Class<T> targetClass)
            throws DataConversionException {
        if (null == source) {
            return Optional.empty();
        }

        T target;
        try {
            target = targetClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            String err = "Convert from " + source.getClass().getSimpleName() + "to "
                    + targetClass.getSimpleName() + " failed";
            throw new DataConversionException(err, e);

        }
        BeanCopier copier = BeanCopier.create(source.getClass(), targetClass, false);
        copier.copy(source, target, null);

        return Optional.ofNullable(target);
    }

}
