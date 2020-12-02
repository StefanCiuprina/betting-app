package com.example.application.data.repositories;

import com.example.application.data.entity.BettingTickets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BettingTicketsRepository extends JpaRepository<BettingTickets, Integer> {
}
