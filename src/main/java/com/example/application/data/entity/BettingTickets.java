package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "BettingTickets")
public class BettingTickets extends AbstractEntity {
    private List<BettingTicket> bettingTickets;
    private boolean onGoing;
    private boolean won;
}
