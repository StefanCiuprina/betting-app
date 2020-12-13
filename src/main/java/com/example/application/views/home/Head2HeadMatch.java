package com.example.application.views.home;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;

@Data
public class Head2HeadMatch implements Comparable<Head2HeadMatch>{

    private String homeTeam;
    private String awayTeam;
    private int homeTeamGoals;
    private int awayTeamGoals;
    private LocalDate matchDate;

    @Setter(AccessLevel.NONE)
    private String result;

    public String getResult() {
        if (homeTeamGoals == awayTeamGoals) {
            return "draw";
        }else if(homeTeamGoals > awayTeamGoals) {
            return "home";
        }else {
            return "away";
        }
    }

    @Override
    public int compareTo(Head2HeadMatch o) {
        if(matchDate.isBefore(o.getMatchDate()))
            return 1;
        else if(matchDate.isAfter(o.getMatchDate()))
            return -1;
        return 0;
    }
}
