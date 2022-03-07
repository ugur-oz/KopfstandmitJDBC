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

/*
package com.ugur;

        import org.springframework.boot.web.servlet.error.ErrorController;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.RequestMapping;




public class KopfstandmitJDBCErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        //do something like logging
        return "error";
    }
    /*
    @RequestMapping("/error")
    public String handleError2(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error-404";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error-500";
            }
        }
        return "error";
    }

     */




