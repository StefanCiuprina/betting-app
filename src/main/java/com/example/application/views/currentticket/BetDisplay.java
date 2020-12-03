package com.example.application.views.currentticket;

import lombok.Data;

@Data
public class BetDisplay {
    private BetDisplayType betDisplayTipe;

    private String imageHome;
    private String imageAway;
    private String nameTeamHome;
    private String nameTeamAway;
    private String date;
    private String betType;
    private String odd;

    private String finalOdd;
    private String betIDs;
}