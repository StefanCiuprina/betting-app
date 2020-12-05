package com.example.application.data.service;

import com.example.application.data.entity.BettingTicket;
import com.example.application.data.repositories.BettingTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BettingTicketService {

    @Autowired
    private BettingTicketRepository bettingTicketRepository;

    public void addBettingTicket(BettingTicket bettingTicket) {
        bettingTicketRepository.save(bettingTicket);
    }

    public void deleteBettingTicketById(int id) {
        bettingTicketRepository.deleteById(id);
    }

    public List<BettingTicket> getAllBettingTicketsOfUser(int userID) {
        return bettingTicketRepository.getAllByUserID(userID);
    }

    public void setTicketWon(BettingTicket bettingTicket) {
        BettingTicket existingTicket = bettingTicketRepository.findById(bettingTicket.getId()).orElse(null);
        existingTicket.setOngoing(false);
        existingTicket.setWon(true);
        bettingTicketRepository.save(existingTicket);
    }

    public void setTicketLost(BettingTicket bettingTicket) {
        BettingTicket existingTicket = bettingTicketRepository.findById(bettingTicket.getId()).orElse(null);
        existingTicket.setOngoing(false);
        existingTicket.setWon(false);
        bettingTicketRepository.save(existingTicket);
    }

    public void setCashedIn(BettingTicket bettingTicket) {
        BettingTicket existingTicket = bettingTicketRepository.findById(bettingTicket.getId()).orElse(null);
        existingTicket.setCashedIn(true);
        bettingTicketRepository.save(existingTicket);
    }
}
