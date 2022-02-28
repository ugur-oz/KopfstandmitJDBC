package com.ugur;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LosungForm {
    private long id;
    private String description;
    private long worsening_id;

    @Override
    public String toString() {
        return "LosungForm{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", worsening_id=" + worsening_id +
                '}';
    }
}


