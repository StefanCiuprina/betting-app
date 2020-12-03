package com.example.application.data.repositories;

import com.example.application.data.entity.PastMatch;
import com.example.application.data.entity.UpcomingMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface UpcomingMatchesRepository extends JpaRepository<UpcomingMatch, Integer> {

    @Query("SELECT t from UpcomingMatch t where t.match_date = ?1 and t.match_time = ?2 and t.home_team = ?3 and t.away_team = ?4")
    List<UpcomingMatch> getUpcomingMatchByAllProperties(LocalDate matchDate, LocalTime matchTime, String homeTeam, String awayTeam);

}