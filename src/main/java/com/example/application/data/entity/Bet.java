package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import com.example.application.data.BetType;
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
@Table(name = "Bets")
public class Bet extends AbstractEntity {
    private int userID;
    private String homeTeam;
    private String awayTeam;
    private BetType betType;
    private double odd;
    private LocalDate date;
    private LocalTime time;
    private boolean placed;
    private boolean finished;
    private int scoreHome;
    private int scoreAway;
    private boolean won;

    public Bet(int currentUserID, String homeTeam, String awayTeam, BetType betType, double round2Decimals, LocalDate matchDate, LocalTime matchTime, boolean b) {
        this(currentUserID, homeTeam, awayTeam, betType, round2Decimals, matchDate, matchTime, b, false, 0, 0, false);
    }
}
