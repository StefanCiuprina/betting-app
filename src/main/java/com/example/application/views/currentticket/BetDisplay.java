package com.example.application.views.currentticket;

import lombok.Data;

@Data
public class BetDisplay {
    private int betId;

    private BetDisplayType betDisplayTipe;

    private String imageHome;
    private String imageAway;
    private String nameTeamHome;
    private String nameTeamAway;
    private String date;
    private String time;
    private String betType;
    private String odd;

    //for bet place
    private String finalOdd; //also for finished bets
    private String betIDs; //also for finished bets

    //for finished bets:
    private String amountPlaced;
    private String ongoing;
    private String scoreHome;
    private String scoreAway;
    private String possibleAmountToWin;

    private boolean placed;
    private boolean won;
}