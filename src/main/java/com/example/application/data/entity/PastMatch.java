package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "past_matches")
public class PastMatch extends AbstractEntity {

    private LocalDate match_date;
    private LocalTime match_time;
    private String home_team;
    private String away_team;
    private int home_goals;
    private int away_goals;
    private String match_result;

    public PastMatch(LocalDate match_date, LocalTime match_time, String home_team, String away_team, int home_goals, int away_goals, String match_result) {
        this.match_date = match_date;
        this.match_time = match_time;
        this.home_team = home_team;
        this.away_team = away_team;
        this.home_goals = home_goals;
        this.away_goals = away_goals;
        this.match_result = match_result;
    }

    public PastMatch() {
    }

    @Override
    public String toString() {
        return "Match{" +
                "match_date=" + match_date +
                ", match_time=" + match_time +
                ", home_team='" + home_team + '\'' +
                ", away_team='" + away_team + '\'' +
                ", home_goals=" + home_goals +
                ", away_goals=" + away_goals +
                ", match_result='" + match_result + '\'' +
                '}';
    }

}
