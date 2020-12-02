package com.example.application.data.repositories;

import com.example.application.data.entity.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepository extends JpaRepository<Bet, Integer> {

}
