package com.example.application.views.mybets;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BettingTicketDisplay {
    private String odd;
    private String possibleAmountToWin;
    private String amountPlaced;
    private String datePlaced;
    private String ongoing;
    private String ongoingIcon;
    private String wonOrLost;
    private String wonOrLostIcon;

    private int id;
}
