package com.example.application.data.service;

import com.example.application.data.repositories.UpcomingMatchesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class UpcomingMatchService {

    private final UpcomingMatchesRepository upcomingMatchesRepository;

    public UpcomingMatchService(UpcomingMatchesRepository upcomingMatchesRepository) {
        this.upcomingMatchesRepository = upcomingMatchesRepository;
    }

    public List<String> getFirstLeagueCurrentTeams() {
        List<String> teams = new ArrayList<>();
        teams.add("FC Steaua Bucuresti");
        teams.add("U Craiova");
        teams.add("CFR Cluj");
        teams.add("Sepsi Sf. Gheorghe");
        teams.add("Academica Clinceni");
        teams.add("UTA Arad");
        teams.add("Viitorul Constanta");
        teams.add("Chindia Targoviste");
        teams.add("Gaz Metan Medias");
        teams.add("FC Voluntari");
        teams.add("FC Hermannstadt");
        teams.add("Poli Iasi");
        teams.add("Din. Bucuresti");
        teams.add("FC Arges");
        teams.add("Astra");
        teams.add("FC Botosani");
        Collections.sort(teams);
        return teams;
    }
}
