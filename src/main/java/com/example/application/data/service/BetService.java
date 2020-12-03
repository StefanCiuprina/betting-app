package com.example.application.data.service;

import com.example.application.data.entity.Bet;
import com.example.application.data.repositories.BetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BetService {

    private BetRepository betRepository;

    public BetService(@Autowired BetRepository betRepository) {
        this.betRepository = betRepository;
    }

    public void createBet(Bet bet) {
        betRepository.save(bet);
    }

    public void deleteBetById(int id) {
        betRepository.deleteById(id);
    }

    public List<Bet> getAllCurrentBetsOfUser(int id) {
        return betRepository.getAllByUserIDAndNotPlacedYet(id, true);
    }

}
