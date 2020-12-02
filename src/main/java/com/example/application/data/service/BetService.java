package com.example.application.data.service;

import com.example.application.data.entity.Bet;
import com.example.application.data.repositories.BetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BetService {

    @Autowired
    private BetRepository betRepository;

    public void createBet(Bet bet) {
        betRepository.save(bet);
    }

    public void deleteBetById(int id) {
        betRepository.deleteById(id);
    }

}
