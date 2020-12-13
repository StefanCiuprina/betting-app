package com.example.application.data.service;

import com.example.application.data.entity.Bet;
import com.example.application.data.repositories.BetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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

    public void setAllBetsPlaced(int id) {
        List<Bet> bets = betRepository.getAllByUserIDAndPlaced(id, false);
        for(Bet bet : bets) {
            bet.setPlaced(true);
        }
        betRepository.saveAll(bets);
    }

    public List<Bet> getAllCurrentBetsOfUser(int userID) {
        return betRepository.getAllByUserIDAndPlaced(userID, false);
    }

    public Bet getBetByID(int id) {
        return betRepository.getById(id);
    }

    public boolean noCurrentBetsOfUser(int userID) {
        return (long) betRepository.getAllByUserIDAndPlaced(userID, false).size() == 0;
    }

    public boolean isEmpty() {
        return betRepository.count() == 0;
    }

    public List<Bet> getAllBetsByMatch(String home_team, String away_team, LocalDate match_date, LocalTime match_time) {
        return betRepository.getByMatch(home_team, away_team, match_date, match_time);
    }

    public void updateBet(Bet bet) {
        Bet existingBet = betRepository.findById(bet.getId()).orElse(null);
        existingBet.setScoreHome(bet.getScoreHome());
        existingBet.setScoreAway(bet.getScoreAway());
        existingBet.setFinished(bet.isFinished());
        existingBet.setWon(bet.isWon());
        betRepository.save(existingBet);
    }

    public boolean isBetWonById(int betID) {
        Bet existingBet = betRepository.findById(betID).orElse(null);
        return existingBet.isFinished() && existingBet.isWon();
    }

    public boolean isBetFinishedAndLostById(int betID) {
        Bet existingBet = betRepository.findById(betID).orElse(null);
        return existingBet.isFinished() && !existingBet.isWon();
    }

    public void deleteAllUnplacedBetsOfId(int id) {
        betRepository.removeAllUnplacedById(id);
    }

    public List<Bet> getAllBetsOfTicket(int bettingTicketId) {
        return betRepository.getAllByBettingTicketId(bettingTicketId);
    }

    public void setBettingTicketIdToBet(int betId, int ticketId) {
        Bet existingBet = betRepository.getById(betId);
        existingBet.setBettingTicketId(ticketId);
        betRepository.save(existingBet);
    }

    public void deleteBetByTicketId(int id) {
        betRepository.deleteByBettingTicketId(id);
    }
}
