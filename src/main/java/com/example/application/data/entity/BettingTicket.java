package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "BettingTicket")
public class BettingTicket extends AbstractEntity {
    private String homeTeam;
    private String awayTeam;
    private double odd;
    private LocalDate date;
}
