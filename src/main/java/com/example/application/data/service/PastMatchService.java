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

}
