package com.ugur;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LosungRowMapper implements RowMapper<LosungForm> {

    @Override
    public LosungForm mapRow(ResultSet rs, int rowNum) throws SQLException {

        LosungForm result = new LosungForm();
        result.setId(rs.getInt("id"));
        result.setDescription(rs.getString("description"));
        result.setWorsening_id(rs.getInt("worsening_id"));

        return result;
    }
}

