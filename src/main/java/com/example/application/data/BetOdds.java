package com.example.application.data;

import lombok.Getter;

@Getter
public class BetOdds {

    private float homeWin;
    private float awayWin;
    private float draw;

    public BetOdds(float homeWin, float awayWin, float draw) {
        this.homeWin = homeWin;
        this.awayWin = awayWin;
        this.draw = draw;
    }

    @Override
    public String toString() {
        return "BetOdds{" +
                "homeWin=" + homeWin +
                ", awayWin=" + awayWin +
                ", draw=" + draw +
                '}';
    }
}
