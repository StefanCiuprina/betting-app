package com.example.application.data.service;

import com.example.application.views.home.MatchWithOdds;
import com.example.application.data.entity.UpcomingMatch;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchWithOddsService {

    private UpcomingMatchService upcomingMatchService;
    private OddsService oddsService;

    public MatchWithOddsService(UpcomingMatchService upcomingMatchService, OddsService oddsService) {
        this.upcomingMatchService = upcomingMatchService;
        this.oddsService = oddsService;
    }

    private String getTeamLogo(String teamName) {
        switch(teamName) {
            case "Academica Clinceni":
                return "https://lpf.ro/imagini-stiri/echipa/72/5e5372c76a33bechipa__0004_Layer-9.png";
            case "Din. Bucuresti":
                return "https://lpf.ro/imagini-stiri/echipa/72/5e53729c9f6fdechipa__0003_Layer-10.png";
            case "Poli Iasi":
                return "https://lpf.ro/imagini-stiri/echipa/72/5e53728ea1721echipa__0012_Layer-1.png";
            case "FC Arges":
                return "https://lpf.ro/imagini-stiri/echipa/72/5f3d0663f1835arges.png";
            case "FC Voluntari":
                return "https://lpf.ro/imagini-stiri/echipa/72/5e537276c6304echipa__0008_Layer-5.png";
            case "Viitorul Constanta":
                return "https://lpf.ro/imagini-stiri/echipa/72/5f3d078075789viitorul.png";
            case "Astra":
                return "https://lpf.ro/imagini-stiri/echipa/72/5e5372a40ebd1echipa__0006_Layer-7.png";
            case "Sepsi Sf. Gheorghe":
                return "https://lpf.ro/imagini-stiri/echipa/72/5e5372b3b92e3echipa__0010_Layer-3.png";
            case "U Craiova":
                return "https://lpf.ro/imagini-stiri/echipa/72/5e53728820ce1echipa__0011_Layer-2.png";
            case "FC Steaua Bucuresti":
                return "https://lpf.ro/imagini-stiri/echipa/72/5e537262549aaechipa__0007_Layer-6.png";
            case "UTA Arad":
                return "https://lpf.ro/imagini-stiri/echipa/72/5f537d3010412logo-uta.1597331445.png";
            case "Gaz Metan Medias":
                return "https://lpf.ro/imagini-stiri/echipa/72/5f4648f3df19flogo-gaz-metan.png";
            case "FC Botosani":
                return "https://lpf.ro/imagini-stiri/echipa/72/5e53728024e04echipa__0002_Layer-11.png";
            case "Chindia Targoviste":
                return "https://lpf.ro/imagini-stiri/echipa/72/5e5372c28d620echipa__0001_Layer-12.png";
            case "FC Hermannstadt":
                return "https://lpf.ro/imagini-stiri/echipa/72/5e5372bd94512echipa__0000_Layer-13.png";
            case "CFR Cluj":
                return "https://lpf.ro/imagini-stiri/echipa/72/5e53726d5ac83echipa__0005_Layer-8.png";
            default:
                return null;
        }
    }

    public List<MatchWithOdds> getMatchesWithOdds() {

        List<MatchWithOdds> listOfMatchesWithOdds = new ArrayList<>();
        List<UpcomingMatch> listOfUpcomingMatches = upcomingMatchService.getUpcomingMatchesForUsers();
        listOfUpcomingMatches.forEach(match -> {
            MatchWithOdds matchWithOdds = new MatchWithOdds();
            matchWithOdds.setHomeTeam(match.getHome_team());
            matchWithOdds.setAwayTeam(match.getAway_team());
            matchWithOdds.setMatchDate(match.getMatch_date());
            matchWithOdds.setMatchTime(match.getMatch_time());
            matchWithOdds.setHomeTeamPicture(getTeamLogo(match.getHome_team()));
            matchWithOdds.setAwayTeamPicture(getTeamLogo(match.getAway_team()));
            matchWithOdds.setBetOdds(oddsService.getBetOdds(match.getHome_team(), match.getAway_team()));
            listOfMatchesWithOdds.add(matchWithOdds);
        });

        return listOfMatchesWithOdds;
    }
}
