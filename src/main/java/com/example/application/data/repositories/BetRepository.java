package com.example.application.data.repositories;

import com.example.application.data.entity.Bet;
import com.example.application.data.entity.UpcomingMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Integer> {

    List<Bet> getAllByUserIDAndPlaced(int id, boolean placed);
    Bet getById(int id);

    @Query("SELECT t from Bet t where t.homeTeam = ?1 and t.awayTeam = ?2 and t.date = ?3 and t.time = ?4")
    List<Bet> getByMatch(String home_team, String away_team, LocalDate match_date, LocalTime match_time);

    @Transactional
    @Modifying
    @Query("DELETE FROM Bet t where t.placed = false and t.userID = ?1")
    void removeAllUnplacedById(int id);

    List<Bet> getAllByBettingTicketId(int id);

    @Transactional
    @Modifying
    void deleteByBettingTicketId(int id);
}
