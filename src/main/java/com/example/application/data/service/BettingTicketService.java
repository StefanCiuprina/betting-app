package com.example.application.data.service;

import com.example.application.data.entity.BettingTicket;
import com.example.application.data.repositories.BettingTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BettingTicketService {

    @Autowired
    private BettingTicketRepository bettingTicketRepository;

    public boolean createBettingTicket(BettingTicket bettingTicket) {
        bettingTicketRepository.save(bettingTicket);
    }

}
