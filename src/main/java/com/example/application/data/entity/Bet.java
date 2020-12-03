package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import com.example.application.data.BetType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

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
    private boolean placed;
}
