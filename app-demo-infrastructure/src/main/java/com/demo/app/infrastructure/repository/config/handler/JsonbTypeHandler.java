package com.demo.app.infrastructure.repository.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Jsonb type handler.
 */
public class JsonbTypeHandler extends BaseTypeHandler<Object> {

    private static final ObjectMapper mapper = new ObjectMapper();

    private final Class<?> type;

    /**
     * Instantiates a new Jsonb type handler.
     *
     * @param type the type
     */
    public JsonbTypeHandler(Class<?> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        PGobject jsonObject = new PGobject();
        jsonObject.setType("jsonb");
        try {
            jsonObject.setValue(mapper.writeValueAsString(parameter));
        } catch (Exception e) {
            throw new SQLException("Failed to convert object to JSON", e);
        }
        ps.setObject(i, jsonObject);
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        if (json == null) {
            return null;
        }
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw new SQLException("Failed to parse JSON to object", e);
        }
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        if (json == null) {
            return null;
        }
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw new SQLException("Failed to parse JSON to object", e);
        }
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        if (json == null) {
            return null;
        }
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw new SQLException("Failed to parse JSON to object", e);
        }
    }
}