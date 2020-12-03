package com.example.application.data.repositories;

import com.example.application.data.entity.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Integer> {

    List<Bet> getAllByUserIDAndPlaced(int id, boolean placed);

}
