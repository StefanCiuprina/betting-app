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
@Table(name = "upcoming_matches")
public class UpcomingMatch extends AbstractEntity {

    private LocalDate match_date;
    private LocalTime match_time;
    private String home_team;
    private String away_team;

    public UpcomingMatch(LocalDate match_date, LocalTime match_time, String home_team, String away_team) {
        this.match_date = match_date;
        this.match_time = match_time;
        this.home_team = home_team;
        this.away_team = away_team;
    }

    public UpcomingMatch() {

    }
}
