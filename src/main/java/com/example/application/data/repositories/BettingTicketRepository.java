package com.example.application.data.repositories;

import com.example.application.data.entity.BettingTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BettingTicketRepository extends JpaRepository<BettingTicket, Integer> {

    List<BettingTicket> getAllByUserID(int userID);

}
