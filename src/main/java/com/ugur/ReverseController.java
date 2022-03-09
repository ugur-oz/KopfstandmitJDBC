package com.ugur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class ReverseController {

    String showProblem;

    @GetMapping
    public String getProblem(Model model) {
        model.addAttribute("saveProblemForm", new ProblemForm());
        return "problemForm";
    }

    @PostMapping("/saveProblem")
    public String saveProblemForm(Model model, ProblemForm problemForm) {
        Long savedId = this.saveProblemToDbankReturnId(problemForm);
        VerschlimmerungForm worseningToSave = new VerschlimmerungForm();
        worseningToSave.setProblem_id(savedId.intValue());
        model.addAttribute("problemId", savedId.intValue());
        model.addAttribute("saveVerschlimmerungForm", worseningToSave);
        model.addAttribute("problemText", problemForm.getDescription());
        showProblem = problemForm.getDescription();
        return "verschlimmerungForm";
    }

    @PostMapping("/verschlimm")
    public String saveVerschlimmerungForm(Model model, VerschlimmerungForm verschlimmerungForm) {

        VerschlimmerungForm newVerschlimmerungForm = new VerschlimmerungForm();
        newVerschlimmerungForm.setProblem_id(verschlimmerungForm.getProblem_id());

        String saveSQL = "INSERT INTO WORSENING(description, problem_id) VALUES (?,?)";
        jdbcTemplate.update(saveSQL, verschlimmerungForm.getDescription(), verschlimmerungForm.getProblem_id());


        List<VerschlimmerungForm> verschlimmerungFormList = jdbcTemplate.query("SELECT * FROM WORSENING WHERE worsening.problem_id = " + verschlimmerungForm.getProblem_id(), new VerschlimmerungRowMapper());
        model.addAttribute("verschlimmerungFormList", verschlimmerungFormList);
        model.addAttribute("problemText", showProblem);
        model.addAttribute("saveVerschlimmerungForm", newVerschlimmerungForm);
        return "verschlimmerungForm";
    }

    @GetMapping("/verschlimm")
    public String saveVerschlimmerungForm(Model model, @RequestParam int problemId) {
        List<VerschlimmerungForm> verschlimmerungFormList = jdbcTemplate.query("SELECT * FROM WORSENING WHERE worsening.problem_id = " + problemId, new VerschlimmerungRowMapper());
        model.addAttribute("verschlimmerungFormList", verschlimmerungFormList);

        model.addAttribute("problemText", showProblem);

        VerschlimmerungForm newVerschlimmerungForm = new VerschlimmerungForm();
        newVerschlimmerungForm.setProblem_id(problemId);
        model.addAttribute("saveVerschlimmerungForm", newVerschlimmerungForm);
        model.addAttribute("problemId", problemId);
        return "verschlimmerungForm";
    }

    @GetMapping("/losung")
    public String getLosungForm(Model model, @RequestParam int problemId) {
        model.addAttribute("saveLosungForm", new LosungForm());
        List<VerschlimmerungForm> verschlimmerungFormList = jdbcTemplate.query("SELECT * FROM WORSENING WHERE worsening.problem_id = " + problemId, new VerschlimmerungRowMapper());
        List<LosungForm> losungFormList = jdbcTemplate.query("SELECT * FROM SOLUTIONS WHERE solutions.WORSENING_ID = " + verschlimmerungFormList.get(0).getId() , new LosungRowMapper());
        model.addAttribute("verschlimmerungFormList", verschlimmerungFormList);
        model.addAttribute("losungFormList", losungFormList);
        model.addAttribute("problemId", problemId);
        return "losungForm";
    }

    @PostMapping("/losung")
    public String getLosungForm(Model model, LosungForm losungForm, String problemId) {

        model.addAttribute("saveLosungForm", new LosungForm());
        String saveSQL = "INSERT INTO SOLUTIONS(description, WORSENING_ID) VALUES (?,?)";
        jdbcTemplate.update(saveSQL, losungForm.getDescription(), losungForm.getWorsening_id());
        List<VerschlimmerungForm> verschlimmerungFormList = jdbcTemplate.query("SELECT * FROM WORSENING WHERE worsening.problem_id = " + problemId, new VerschlimmerungRowMapper());
        List<LosungForm> losungFormList = jdbcTemplate.query("SELECT * FROM SOLUTIONS" , new LosungRowMapper());


        model.addAttribute("selectedWorsening", jdbcTemplate.query("SELECT * FROM WORSENING WHERE id = " + losungForm.getWorsening_id(), new VerschlimmerungRowMapper() ));
        model.addAttribute("verschlimmerungFormList", verschlimmerungFormList);
        model.addAttribute("losungFormList", losungFormList);
        model.addAttribute("problemId", problemId);
        return "losungForm";
    }

    @GetMapping("/ansicht")
    public String showSeite(Model model, LosungForm losungForm, String problemId) {

        List<VerschlimmerungForm> verschlimmerungFormList = jdbcTemplate.query("SELECT * FROM WORSENING WHERE worsening.problem_id = " + problemId, new VerschlimmerungRowMapper());
        List<LosungForm> losungFormList = jdbcTemplate.query("SELECT * FROM SOLUTIONS", new LosungRowMapper());

        model.addAttribute("verschlimmerungFormList", verschlimmerungFormList);
        model.addAttribute("losungFormList", losungFormList);
        model.addAttribute("problemId", problemId);

        return "ansicht";
    }

    @Bean
    public FlywayMigrationStrategy repairFlyway() {
        return flyway -> {
            // repair each script's checksum
            flyway.repair();
            // before new migrations are executed
            flyway.migrate();
        };
    }

    SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public ReverseController(DataSource dataSource) {
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("PROBLEM").usingGeneratedKeyColumns("id");

    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public long saveProblemToDbankReturnId(ProblemForm problemForm) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("description", problemForm.getDescription());
        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
        return (Long) newId;
    }
}