package com.ugur;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemForm {
    private long id;
    private String description;

    //private List<VerschlimmerungForm> verschlimm = new ArrayList<>();
    // private List<LosungForm> losungFormList = new ArrayList();


    @Override
    public String toString() {
        return "ProblemForm{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}

