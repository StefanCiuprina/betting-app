package com.example.application.data.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TeamsService {

    public static List<String> getFirstLeagueCurrentTeams() {
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

    public static float getTeamValue(String team) {
        switch (team) {
            case "FC Steaua Bucuresti" : return 28.1f;
            case "U Craiova" : return 22.33f;
            case "CFR Cluj" : return 23.8f;
            case "Sepsi Sf. Gheorghe" : return 8.63f;
            case "Academica Clinceni" : return 6.0f;
            case "UTA Arad" : return 6.08f;
            case "Viitorul Constanta" : return 15.48f;
            case "Chindia Targoviste" : return 4.88f;
            case "Gaz Metan Medias" : return 8.78f;
            case "FC Voluntari" : return 6.73f;
            case "FC Hermannstadt" : return 6.88f;
            case "Poli Iasi" : return 5.18f;
            case "Din. Bucuresti" : return 13.6f;
            case "FC Arges" : return 6.25f;
            case "Astra" : return 14.85f;
            case "FC Botosani" : return 9.55f;
            default: return 0.0f;
        }
    }
}
