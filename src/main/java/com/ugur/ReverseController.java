package com.ugur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class ReverseController {

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

        model.addAttribute("saveProblemForm", new ProblemForm());
        model.addAttribute("saveVerschlimmerungForm", worseningToSave);
        return "verschlimmerungForm";
    }
    @PostMapping("/verschlimm")
    public String saveVerschlimmerungForm(Model model, VerschlimmerungForm verschlimmerungForm) {

        VerschlimmerungForm newVerschlimmerungForm = new VerschlimmerungForm();
        newVerschlimmerungForm.setProblem_id(verschlimmerungForm.getProblem_id());

        String saveSQL = "INSERT INTO WORSENING(description, problem_id) VALUES (?,?)";
        jdbcTemplate.update(saveSQL, verschlimmerungForm.getDescription(), verschlimmerungForm.getProblem_id());


        List<VerschlimmerungForm> verschlimmerungFormList = jdbcTemplate.query("SELECT * FROM WORSENING", new VerschlimmerungRowMapper());
       model.addAttribute("verschlimmerungFormList",verschlimmerungFormList);


        model.addAttribute("saveVerschlimmerungForm", newVerschlimmerungForm);
        return "verschlimmerungForm";
    }
    @GetMapping("/losung")
    public String getLosungForm(Model model, @RequestParam  int problem_id) {
        model.addAttribute("saveLosungForm", new LosungForm());
        List<VerschlimmerungForm> verschlimmerungFormList = jdbcTemplate.query("SELECT * FROM WORSENING WHERE problem_id = ?", new VerschlimmerungRowMapper(), problem_id);
        model.addAttribute("verschlimmerungFormList", verschlimmerungFormList);

        return "losungForm";
    }

    @PostMapping("/losung")
    public String getLosungForm(Model model, LosungForm losungForm, String problemId) {

        model.addAttribute("saveLosungForm", new LosungForm());

        String saveSQL = "INSERT INTO SOLUTIONS(description, WORSENING_ID) VALUES (?,?)";
        jdbcTemplate.update(saveSQL, losungForm.getDescription(), losungForm.getWorsening_id());

        List<VerschlimmerungForm> verschlimmerungFormList = jdbcTemplate.query("SELECT * FROM WORSENING WHERE worsening.problem_id = " + problemId, new VerschlimmerungRowMapper());
        model.addAttribute("verschlimmerungFormList", verschlimmerungFormList);
        System.out.println(losungForm.getDescription());
        return "losungForm";
    }
    @GetMapping("/ansicht")
    public String showSeite(Model model, LosungForm losungForm, String problemId) {

        List<VerschlimmerungForm> verschlimmerungFormList = jdbcTemplate.query("SELECT * FROM WORSENING WHERE worsening.problem_id = " + problemId, new VerschlimmerungRowMapper());
        List<LosungForm> losungFormList = jdbcTemplate.query("SELECT * FROM SOLUTIONS", new LosungRowMapper());



        model.addAttribute("verschlimmerungFormList", verschlimmerungFormList);
        model.addAttribute("losungFormList", losungFormList);

       // verschlimmerungFormList.get(losungForm.getIndexOfVerschlimmerung()).getLosungen().add(losungForm);
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


        /*    problemFormList.get(verschlimmerungForm.getIndexOfProblem()).getVerschlimm().add(verschlimmerungForm);
          verschlimmerungFormList.get(verschlimmerungFormList.getIndexOfVerschlimmerung()).getLosung().add(LosungForm)         */


     /*    problemFormList.get(verschlimmerungForm.getIndexOfProblem()).getVerschlimm().add(verschlimmerungForm);
          verschlimmerungFormList.get(verschlimmerungFormList.getIndexOfVerschlimmerung()).getLosung().add(LosungForm)         */


/*
public static List<VerschlimmerungForm> verschlimmerungFormList = new ArrayList<>();
    //    public static List<ProblemForm> problemFormList = new ArrayList<>();
    public static List<LosungForm> losungFormList = new ArrayList<>();

    private String problem;

    @GetMapping("/")
    public String getProblemForm(Model model) {
        model.addAttribute("saveProblemForm", new ProblemForm());
        return "problemForm";
    }

    @PostMapping("/saveProblemForm")
    public String saveProblemForm(Model model, String problemForm) {
        model.addAttribute("saveProblemForm", new ProblemForm());
        problem = problemForm;
        System.out.println(problem);
        return "redirect:/verschlimm";
    }

    @GetMapping("/verschlimm")
    public String getVerschlimmerungForm(Model model) {
        model.addAttribute("saveVerschlimmerungForm", new VerschlimmerungForm());
        model.addAttribute("problem", problem);
        return "verschlimmerungForm";
    }

    @PostMapping("/verschlimm")
    public String saveVerschlimmerungForm(Model model, VerschlimmerungForm verschlimmerungForm, ProblemForm problemForm) {
        model.addAttribute("saveVerschlimmerungForm", new VerschlimmerungForm());
        model.addAttribute("problemForm", problemForm);
        verschlimmerungFormList.add(verschlimmerungForm);

        return "verschlimmerungForm";
    }

    @GetMapping("/losung")
    public String getLosungForm(Model model, VerschlimmerungForm verschlimmerungForm) {
        model.addAttribute("verschlimmerungFormList", verschlimmerungFormList);
        model.addAttribute("saveLosungForm", new LosungForm());
        model.addAttribute("verschlimmerungForm", verschlimmerungForm);


        //     problemFormList.get(verschlimmerungForm.getIndexOfProblem()).getVerschlimm().add(verschlimmerungForm);
        //    verschlimmerungFormList.get(verschlimmerungFormList.getIndexOfVerschlimmerung()).getLosung().add(LosungForm)
        return "losungForm";
    }

    @PostMapping("/losung")
    public String getLosungForm(Model model, LosungForm losungForm) {
        model.addAttribute("verschlimmerungFormList", verschlimmerungFormList);
        model.addAttribute("saveLosungForm", new LosungForm());
        losungFormList.add(losungForm);
        model.addAttribute("losungFormList", losungFormList);
        verschlimmerungFormList.get(losungForm.getIndexOfVerschlimmerung()).getLosungen().add(losungForm);
        // System.out.println(verschlimmerungFormList.get(losungForm.getIndexOfVerschlimmerung()));

        return "losungForm";
    }

    @GetMapping("/ansicht")
    public String showSeite(Model model, LosungForm losungForm) {
        model.addAttribute("verschlimmerungFormList", verschlimmerungFormList);
        model.addAttribute("saveLosungForm", new LosungForm());
        losungFormList.add(losungForm);
        model.addAttribute("losungFormList", losungFormList);
        verschlimmerungFormList.get(losungForm.getIndexOfVerschlimmerung()).getLosungen().add(losungForm);

        return "ansicht";
    }

 */