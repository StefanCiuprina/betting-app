package com.example.application.data.service;

import com.example.application.data.entity.PastMatch;
import com.example.application.data.repositories.PastMatchesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PastMatchService {


    private final PastMatchesRepository pastMatchesRepository;

    public PastMatchService(PastMatchesRepository pastMatchesRepository) {
        this.pastMatchesRepository = pastMatchesRepository;
    }

    public List<PastMatch> getAllMatches() {
        return pastMatchesRepository.findAll();
    }

    public List<PastMatch> getHead2HeadMatches(String team1, String team2) {
        return pastMatchesRepository.getHead2HeadMatches(team1, team2);
    }

    public List<PastMatch> getMatchesWhereTheTeamWasHomeTeam(String team) {
        return pastMatchesRepository.getMatchesWhereTheTeamWasHomeTeam(team);
    }

    public List<PastMatch> getMatchesWhereTheTeamWasAwayTeam(String team) {
        return pastMatchesRepository.getMatchesWhereTheTeamWasAwayTeam(team);
    }

    public List<PastMatch> getAllMatchesForTeam(String team) {
        return pastMatchesRepository.getAllMatchesForTeam(team);
    }

    public List<String> getListOfTeamsName() {
        return pastMatchesRepository.getListOfTeams();
    }

    public int getHowManyTimesTheFirstParameterTeamWonFromH2H(String teamToWin, String team2){
        return pastMatchesRepository.getHowManyTimesTheFirstParameterTeamWonFromH2H(teamToWin, team2);
    }

    public int getHowManyDrawsFromH2H(String team1, String team2) {
        return pastMatchesRepository.getHowManyDrawsFromH2H(team1, team2);
    }

    public boolean haveTheTeamsMet(String team1, String team2) {
        List<PastMatch> p = getHead2HeadMatches(team1, team2);
        return p != null && p.size() != 0;
    }

    public float getAverageGoalsForATeamInH2H(String teamToGetAverage, String team1, String team2) {
        return round2Decimals((float)(pastMatchesRepository.getSumOfGoalsWhenHomeTeam(teamToGetAverage, team1, team2) +
                pastMatchesRepository.getSumOfGoalsWhenAwayTeam(teamToGetAverage, team1, team2)) /
                (float) pastMatchesRepository.getNumberOfMatchesH2H(team1, team2));
    }

    private float round2Decimals(float f) {
        return (float) ((float) Math.round(f * 100.0) / 100.0);
    }

}
