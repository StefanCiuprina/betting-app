package com.example.application.data.repositories;

import com.example.application.data.entity.PastMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PastMatchesRepository extends JpaRepository<PastMatch, Integer> {

    @Query("SELECT t from PastMatch t where t.home_team = ?1 and t.away_team = ?2 or t.home_team = ?2 and t.away_team = ?1")
    List<PastMatch> getHead2HeadMatches(String team1, String team2);

    @Query("SELECT t from PastMatch t where t.home_team = ?1")
    List<PastMatch> getMatchesWhereTheTeamWasHomeTeam(String team);

    @Query("SELECT t from PastMatch t where t.away_team = ?1")
    List<PastMatch> getMatchesWhereTheTeamWasAwayTeam(String team);

    @Query("SELECT t from PastMatch t where t.away_team = ?1 or t.home_team = ?1")
    List<PastMatch> getAllMatchesForTeam(String team);

    @Query(value = "select distinct away_team home_team from past_matches ORDER BY home_team ASC, away_team ASC", nativeQuery = true)
    List<String> getListOfTeams();
}
