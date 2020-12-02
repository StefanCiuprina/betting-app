package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import com.example.application.data.BetType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "Bet")
public class Bet extends AbstractEntity {
    private int userID;
    private String homeTeam;
    private String awayTeam;
    private BetType betType;
    private double odd;
    private LocalDate date;
}
