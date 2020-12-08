package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "results")
public class Result extends AbstractEntity {

    private LocalDate match_date;
    private LocalTime match_time;
    private String home_team;
    private String away_team;
    private int scoreHome;
    private int scoreAway;

}
