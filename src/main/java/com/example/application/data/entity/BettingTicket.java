package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
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
@Table(name = "BettingTickets")
public class BettingTicket extends AbstractEntity {
    private int userID;
    private String betIDs;
    private double odd;
    private double possibleAmountToWin;
    private boolean ongoing;
    private boolean won;
    private LocalDate datePlaced;
}
