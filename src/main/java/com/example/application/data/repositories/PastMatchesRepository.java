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

    @Query("SELECT Count (t) from PastMatch t where t.home_team = ?1 and t.away_team = ?2 and t.match_result = 'H' or t.home_team = ?2 and t.away_team = ?1 and t.match_result = 'A'")
    int getHowManyTimesTheFirstParameterTeamWonFromH2H(String teamToWin, String team2);

    @Query("SELECT Count (t) from PastMatch t where t.home_team = ?1 and t.away_team = ?2 and t.match_result = 'D' or t.home_team = ?2 and t.away_team = ?1 and t.match_result = 'D'")
    int getHowManyDrawsFromH2H(String team1, String team2);

    @Query(value = "select sum(home_goals)from past_matches where home_team = ?1 and id in (\n" +
            "select id from past_matches where home_team = ?2 and away_team = ?3 or home_team = ?3 and away_team = ?2)", nativeQuery = true)
    int getSumOfGoalsWhenHomeTeam(String homTeam, String team1, String team2);

    @Query(value = "select sum(away_goals)from past_matches where away_team = ?1 and id in (\n" +
            "select id from past_matches where home_team = ?2 and away_team = ?3 or home_team = ?3 and away_team = ?2)", nativeQuery = true)
    int getSumOfGoalsWhenAwayTeam(String awayTeam, String team1, String team2);

    @Query("SELECT Count(t) from PastMatch t where t.home_team = ?1 and t.away_team = ?2 or t.home_team = ?2 and t.away_team = ?1")
    int getNumberOfMatchesH2H(String team1, String team2);
}
