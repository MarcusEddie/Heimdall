/**
 * 
 */
package org.iman.Heimdallr.config.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.iman.Heimdallr.constants.enums.Action;
import org.iman.Heimdallr.constants.enums.BaseEnum;
import org.iman.Heimdallr.constants.enums.CasePriority;
import org.iman.Heimdallr.constants.enums.DBType;
import org.iman.Heimdallr.constants.enums.FuncTag;
import org.iman.Heimdallr.constants.enums.HttpMethod;
import org.iman.Heimdallr.constants.enums.ResultCheckMode;
import org.iman.Heimdallr.constants.enums.TaskState;
import org.iman.Heimdallr.constants.enums.TaskType;
import org.iman.Heimdallr.constants.enums.TestType;
import org.iman.Heimdallr.constants.enums.TriggerType;

/**
 * @author ey
 *
 */
@MappedTypes({ HttpMethod.class, CasePriority.class, ResultCheckMode.class, DBType.class,
        FuncTag.class, Action.class, TestType.class, TaskState.class, TaskType.class,
        TriggerType.class })
public class EnumDbConvertHandler<T extends BaseEnum> extends BaseTypeHandler<T> {

    private Class<T> enumType;
    private T[] enums;

    public EnumDbConvertHandler() {
        super();
    }

    public EnumDbConvertHandler(Class<T> enumType) {
        if (null == enumType) {
            throw new IllegalArgumentException("enumType is required");
        }

        this.enumType = enumType;
        this.enums = enumType.getEnumConstants();
        if (null == this.enums || (null != this.enums && this.enums.length == 0)) {
            throw new IllegalArgumentException(enumType.getSimpleName() + "don't have any enums");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, parameter.getDigit());
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (null == rs || (null != rs && null == rs.getObject(columnName))) {
            return null;
        }
        Integer idx = rs.getInt(columnName);
        return convertEnum(idx);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (null == rs || (null != rs && null == rs.getObject(columnIndex))) {
            return null;
        }
        Integer idx = rs.getInt(columnIndex);
        return convertEnum(idx);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (null == cs || null != cs && null == cs.getObject(columnIndex)) {
            return null;
        }
        Integer idx = cs.getInt(columnIndex);
        return convertEnum(idx);
    }

    private T convertEnum(Integer digit) {
        for (T t : enums) {
            if (t.getDigit().equals(digit)) {
                return t;
            }
        }

        throw new IllegalArgumentException(
                digit + "can not be converted to a enum of " + enumType.getSimpleName());
    }
}
