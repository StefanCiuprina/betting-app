package com.example.application.views.home;

import com.example.application.data.BetOdds;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class MatchWithOdds {

    private String homeTeam;
    private String awayTeam;
    private LocalDate matchDate;
    private LocalTime matchTime;
    private String homeTeamPicture;
    private String awayTeamPicture;
    private BetOdds betOdds;

    @Override
    public String toString() {
        return "MatchWithOdds{" +
                "homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", matchDate=" + matchDate +
                ", matchTime=" + matchTime +
                ", homeTeamPicture='" + homeTeamPicture + '\'' +
                ", awayTeamPicture='" + awayTeamPicture + '\'' +
                ", betOdds=" + betOdds +
                '}';
    }
}
