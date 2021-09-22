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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ey
 *
 */
@MappedTypes({JsonNode.class})
public class JsonNodeDbConvertHandler <T extends JsonNode> extends BaseTypeHandler<T>{

    private Class<T> fieldType;
    
    public JsonNodeDbConvertHandler() {
        super();
    }

    public JsonNodeDbConvertHandler(Class<T> fieldType) {
        super();
        this.fieldType = fieldType;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, parameter.toPrettyString());
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (null == rs || (null != rs && null == rs.getObject(columnName))) {
            return null;
        }
        String json = rs.getString(columnName);
        return convertToJsonNode(json);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (null == rs || (null != rs && null == rs.getObject(columnIndex))) {
            return null;
        }
        String json = rs.getString(columnIndex);
        return convertToJsonNode(json);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (null == cs || (null != cs && null == cs.getObject(columnIndex))) {
            return null;
        }
        
        String json = cs.getString(columnIndex);
        return convertToJsonNode(json);
    }
    
    private T convertToJsonNode(String json) throws SQLException {
        ObjectMapper mapper = new ObjectMapper();
        try {
           return (T) mapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new SQLException(e);
        }
    }

}
