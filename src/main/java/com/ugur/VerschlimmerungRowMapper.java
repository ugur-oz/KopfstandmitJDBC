package com.ugur;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.ResultSet;
import java.sql.SQLException;


public class VerschlimmerungRowMapper implements RowMapper<VerschlimmerungForm> {
    @Override
    public VerschlimmerungForm mapRow(ResultSet rs, int rowNum) throws SQLException {

        VerschlimmerungForm result = new VerschlimmerungForm();
        result.setId(rs.getInt("id"));
        result.setDescription(rs.getString("description"));
        result.setProblem_id(rs.getInt("problem_id"));

        return result;
    }
}
