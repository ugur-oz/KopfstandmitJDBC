
package com.ugur;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ProblemRowMapper implements RowMapper<ProblemForm> {
    @Override
    public ProblemForm mapRow(ResultSet rs, int rowNum) throws SQLException {

        ProblemForm result = new ProblemForm();
        result.setId(rs.getInt("id"));
        result.setDescription(rs.getString("description"));

        return result;
    }
}