package com.demo.app.infrastructure.repository.config.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.time.LocalDateTime;


/**
 * The type Custom local date time type handler.
 */
@MappedTypes(LocalDateTime.class)
@MappedJdbcTypes(JdbcType.TIMESTAMP)
public class CustomLocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object object = rs.getObject(columnName);
        if (object instanceof LocalDateTime localdatetime) {
            return localdatetime;
        } else {
            return ((java.sql.Timestamp) object).toLocalDateTime();
        }
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object object = rs.getObject(columnIndex);
        if (object instanceof LocalDateTime localdatetime) {
            return localdatetime;
        } else {
            return ((java.sql.Timestamp) object).toLocalDateTime();
        }
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object object = cs.getObject(columnIndex);
        if (object instanceof LocalDateTime localdatetime) {
            return localdatetime;
        } else {
            return ((java.sql.Timestamp) object).toLocalDateTime();
        }
    }
}
