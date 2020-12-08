package com.example.application.data.repositories;

import com.example.application.data.entity.Result;
import com.example.application.data.entity.UpcomingMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Integer> {

    @Query("SELECT t from Result t where t.match_date = ?1 and t.match_time = ?2 and t.home_team = ?3 and t.away_team = ?4")
    List<UpcomingMatch> getResultsByMatchDetails(LocalDate matchDate, LocalTime matchTime, String homeTeam, String awayTeam);

}
