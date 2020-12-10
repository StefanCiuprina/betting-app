package com.example.application.data.service;

import com.example.application.data.BetOdds;
import com.example.application.data.entity.PastMatch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OddsService {

    private final PastMatchService pastMatchService;

    private class Container {
        int win;
        int draw;
        int lost;

         Container() {
            this.win = 0;
            this.lost = 0;
            this.draw = 0;
        }

        int getSum() {
             return win + draw + lost;
        }

        @Override
        public String toString() {
            return "Container{" +
                    "win=" + win +
                    ", draw=" + draw +
                    ", lost=" + lost +
                    '}';
        }
    }

    public OddsService(PastMatchService pastMatchService) {
        this.pastMatchService = pastMatchService;
    }

    public BetOdds getBetOdds(String homeTeam, String awayTeam) {

        float homeWinAllGames;
        float awayWinAllGames;
        float drawAllGames;
        float homeWinHead2Head;
        float awayWinHead2Head;
        float drawHead2Head;
        float percentageOfH2HGames =  0.85f;
        float finalWin;
        float finalDraw;
        float finalLost;

        Container homeTeamStats = getStatsFromPastMatchesForHomeTeam(homeTeam);
        Container awayTeamStats = getStatsFromPastMatchesForAwayTeam(awayTeam);
        Container allGamesStats = new Container();

        allGamesStats.win = homeTeamStats.win + awayTeamStats.lost;
        allGamesStats.draw = homeTeamStats.draw + awayTeamStats.draw;
        allGamesStats.lost = homeTeamStats.lost + awayTeamStats.win;

        Container head2headGames = getStatsFromHead2HeadPastMatches(homeTeam, awayTeam);
        homeWinAllGames = (float) allGamesStats.getSum() / allGamesStats.win;
        drawAllGames = (float) allGamesStats.getSum() / allGamesStats.draw;
        awayWinAllGames = (float) allGamesStats.getSum() / allGamesStats.lost;

        homeWinHead2Head = (float) head2headGames.getSum() / head2headGames.win;
        drawHead2Head = (float) head2headGames.getSum() / head2headGames.draw;
        awayWinHead2Head = (float) head2headGames.getSum() / head2headGames.lost;

        if(head2headGames.lost != 0 && head2headGames.win != 0 && head2headGames.draw != 0) {
            finalWin = percentageOfH2HGames * homeWinHead2Head + (1 - percentageOfH2HGames) * homeWinAllGames;
            finalDraw = percentageOfH2HGames * drawHead2Head + (1 - percentageOfH2HGames) * drawAllGames;
            finalLost = percentageOfH2HGames * awayWinHead2Head + (1 - percentageOfH2HGames) * awayWinAllGames;
            finalDraw *= 0.65;
        }else {
            finalWin =  homeWinAllGames;
            finalDraw = drawAllGames;
            finalLost = awayWinAllGames;
            finalDraw *= 0.65;
        }
        return adjustBets(homeTeam, awayTeam, finalWin, finalDraw, finalLost);
    }

    private BetOdds adjustBets(String homeTeam, String awayTeam, float win, float draw, float lost) {
        float finalWin = win;
        float finalLost = lost;

        float homeTeamValue = TeamsService.getTeamValue(homeTeam);
        float awayTeamValue = TeamsService.getTeamValue(awayTeam);

        if (homeTeamValue - awayTeamValue > 5 && finalWin > finalLost) {
            float temp = finalWin;
            finalWin = finalLost;
            finalLost = temp;
        }else if(awayTeamValue - homeTeamValue > 5 && finalLost > finalWin) {
            float temp = finalLost;
            finalLost = finalWin;
            finalWin = temp;
        }
        if (finalWin >= 5) {
            finalLost = 1 + finalLost - (int) finalLost;
            if(finalLost > 1.5)
                finalLost = 0.85f * finalLost;
        }
        if (finalLost >= 5) {
            finalWin = 1 + finalWin - (int) finalWin;
            if(finalWin > 1.5)
                finalWin = 0.85f * finalWin;
        }
        if(finalWin > 6) {
            finalWin = 0.85f * finalWin;
        }
        if(finalLost > 6) {
            finalLost = 0.85f * finalLost;
        }
        return new BetOdds(round2Decimals(finalWin), round2Decimals(finalLost), round2Decimals(draw));
    }

    private float round2Decimals(float f) {
        return (float) ((float) Math.round(f * 100.0) / 100.0);
    }

    private Container getStatsFromPastMatchesForHomeTeam(String homeTeam) {
        Container container = new Container();
        List<PastMatch> pastMatches = pastMatchService.getMatchesWhereTheTeamWasHomeTeam(homeTeam);
        pastMatches.forEach(pastMatch -> {
            switch (pastMatch.getMatch_result()) {
                case "H" : {container.win++; break;}
                case "D" : {container.draw++; break;}
                case "A" : {container.lost++; break;}
            }
        });
        return container;
    }

    private Container getStatsFromPastMatchesForAwayTeam(String awayTeam) {
        Container container = new Container();
        List<PastMatch> pastMatches = pastMatchService.getMatchesWhereTheTeamWasHomeTeam(awayTeam);
        pastMatches.forEach(pastMatch -> {
            switch (pastMatch.getMatch_result()) {
                case "H" : {container.lost++; break;}
                case "D" : {container.draw++; break;}
                case "A" : {container.win++; break;}
            }
        });
        return container;
    }

    private Container getStatsFromHead2HeadPastMatches(String homeTeam, String awayTeam) {
        Container container = new Container();
        List<PastMatch> pastMatches = pastMatchService.getHead2HeadMatches(homeTeam, awayTeam);
        pastMatches.forEach(pastMatch -> {
            switch (pastMatch.getMatch_result()) {
                case "H" : {
                    if (pastMatch.getHome_team().equals(homeTeam)) {
                        container.win++;
                    }else {
                        container.lost++;
                    }
                    break;
                }
                case "D" : {container.draw++; break;}
                case "A" : {
                    if (pastMatch.getAway_team().equals(homeTeam)) {
                        container.win++;
                    }else {
                        container.lost++;
                    }
                    break;
                }
            }
        });
        return container;
    }
}
