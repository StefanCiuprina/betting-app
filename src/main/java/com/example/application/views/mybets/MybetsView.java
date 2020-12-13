package com.example.application.views.mybets;

import com.example.application.data.entity.Bet;
import com.example.application.data.entity.BettingTicket;
import com.example.application.data.service.AuthService;
import com.example.application.data.service.BetService;
import com.example.application.data.service.BettingTicketService;
import com.example.application.views.wallet.WalletView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@PageTitle("My bets")
@CssImport(value = "./styles/views/mybets/mybets-view.css", include = "lumo-badge")
public class MybetsView extends Div implements AfterNavigationObserver {

    Grid<BettingTicketDisplay> grid = new Grid<>();

    private BettingTicketService bettingTicketService;
    private BetService betService;
    private AuthService authService;

    public static String possibleAmountToWin;
    public static String amountPlaced;

    public static int bettingTicketId;

    public MybetsView(@Autowired BettingTicketService bettingTicketService, @Autowired BetService betService,
                      @Autowired AuthService authService) {
        setId("mybets-view");
        this.bettingTicketService = bettingTicketService;
        this.betService = betService;
        this.authService = authService;
        addClassName("mybets-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::createCard);
        add(grid);
    }

    private HorizontalLayout createCard(BettingTicketDisplay bettingTicketDisplay) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout left = new HorizontalLayout();
        String titlePossibleAmountToWinString = bettingTicketDisplay.getOngoing().equals("ongoing") ?
                "Possible amount to win: " : "Won: ";
        Span titlePossibleAmountToWin = new Span(titlePossibleAmountToWinString);
        Span possibleAmountToWin = new Span(bettingTicketDisplay.getPossibleAmountToWin());
        possibleAmountToWin.addClassName("possibleAmountToWin");
        left.add(titlePossibleAmountToWin, possibleAmountToWin);

        VerticalLayout middle = new VerticalLayout();
        HorizontalLayout middleTop = new HorizontalLayout();
        Span textTitleAmountPlaced = new Span("Amount placed:");
        Span textAmountPlaced = new Span(bettingTicketDisplay.getAmountPlaced());
        middleTop.add(textTitleAmountPlaced, textAmountPlaced);
        HorizontalLayout middleBottom = new HorizontalLayout();
        Span titleOdd = new Span("Odd: ");
        Span odd = new Span(bettingTicketDisplay.getOdd());
        odd.addClassName("odd");
        middleBottom.add(titleOdd, odd);
        middle.add(middleTop, middleBottom);
        middle.getStyle().set("width", "100%");

        HorizontalLayout right = new HorizontalLayout();
        right.setSpacing(false);
        right.setPadding(false);
        Span titleDatePlaced = new Span("Date Placed: ");
        Span datePlaced = new Span(bettingTicketDisplay.getDatePlaced());
        datePlaced.addClassName("datePlaced");
        right.add(titleDatePlaced, datePlaced);

        HorizontalLayout status = new HorizontalLayout();
        status.addClassName("status");
        status.setSpacing(false);
        status.getThemeList().add("spacing-s");

        Span ongoing = new Span(bettingTicketDisplay.getOngoing());
        ongoing.addClassName("ongoing");
        IronIcon ongoingIcon = new IronIcon("vaadin", bettingTicketDisplay.getOngoingIcon());
        Span wonOrLost = new Span(bettingTicketDisplay.getWonOrLost());
        wonOrLost.addClassName("wonOrLost");
        IronIcon wonOrLostIcon = new IronIcon("vaadin", bettingTicketDisplay.getWonOrLostIcon());

        Button buttonViewTicket = new Button("View ticket");
        Button buttonDeleteTicket = new Button("Delete");

        if(bettingTicketDisplay.getOngoing().equals("ongoing")) {
            status.add(ongoing, ongoingIcon);
            buttonDeleteTicket.setEnabled(false);
        } else {
            status.add(ongoing, ongoingIcon, wonOrLost, wonOrLostIcon);
        }

        status.add(buttonViewTicket, buttonDeleteTicket);

        buttonViewTicket.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(betService.noCurrentBetsOfUser(AuthService.currentUserID)) {
                    bettingTicketId = bettingTicketDisplay.getId();
                    MybetsView.possibleAmountToWin = bettingTicketDisplay.getPossibleAmountToWin();
                    MybetsView.amountPlaced = bettingTicketDisplay.getAmountPlaced();
                    UI.getCurrent().getPage().setLocation("currentticket");
                } else {
                    Notification.show("Please finish/delete your Current Ticket first.");
                    bettingTicketId = -1;
                }
            }
        });

        buttonDeleteTicket.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                betService.deleteBetByTicketId(bettingTicketDisplay.getId());
                bettingTicketService.deleteBettingTicketById(bettingTicketDisplay.getId());
                UI.getCurrent().getPage().reload();
            }
        });


        description.add(left, status);
        card.add(description, middle, right);
        return card;
    }


    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        List<BettingTicket> bettingTickets = bettingTicketService.getAllBettingTicketsOfUser(AuthService.currentUserID);

        List<BettingTicketDisplay> bettingTicketDisplays = new ArrayList<>();

        for(BettingTicket bettingTicket : bettingTickets) {
            boolean ticketWon = checkTicketWon(bettingTicket);
            boolean ticketLost = checkTicketLost(bettingTicket);
            if(ticketWon) {
                bettingTicketService.setTicketWon(bettingTicket);
                bettingTicket.setOngoing(false);
                bettingTicket.setWon(true);
                if(!bettingTicket.isCashedIn()) {
                    WalletView.updateCurrentBalance(bettingTicket.getPossibleAmountToWin());
                    authService.updateBalance(WalletView.currentBalance);
                    bettingTicketService.setCashedIn(bettingTicket);
                }
            } else if(ticketLost) {
                bettingTicketService.setTicketLost(bettingTicket);
                bettingTicket.setOngoing(false);
                bettingTicket.setWon(false);
            } else {
                bettingTicket.setOngoing(true);
            }

            String ongoing = bettingTicket.isOngoing() ? "ongoing" : "finished";
            String ongoingIcon = bettingTicket.isOngoing() ? "forward" : "check";
            String wonOrLost = bettingTicket.isWon() ? "won" : "lost";
            String wonOrLostIcon = bettingTicket.isWon() ? "check" : "close";

            String possibleAmountToWin;
            if(bettingTicket.isOngoing()) {
                possibleAmountToWin = String.valueOf(bettingTicket.getPossibleAmountToWin());
            } else {
                if(bettingTicket.isWon()) {
                    possibleAmountToWin = String.valueOf(bettingTicket.getPossibleAmountToWin());
                } else {
                    possibleAmountToWin = "0";
                }
            }

            bettingTicketDisplays.add(new BettingTicketDisplay(String.valueOf(bettingTicket.getOdd()),
                    possibleAmountToWin,
                    String.valueOf(bettingTicket.getAmountPlaced()),
                    bettingTicket.getDatePlaced().toString(),
                    ongoing, ongoingIcon,
                    wonOrLost, wonOrLostIcon,
                    bettingTicket.getId()));
        }

        grid.setItems(bettingTicketDisplays);
    }

    private boolean checkTicketLost(BettingTicket bettingTicket) {
        List<Bet> bets = betService.getAllBetsOfTicket(bettingTicket.getId());
        for(Bet bet : bets) {
            if(betService.isBetFinishedAndLostById(bet.getId())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkTicketWon(BettingTicket bettingTicket) {
        List<Bet> bets = betService.getAllBetsOfTicket(bettingTicket.getId());
        int noOfBets = 0;
        int noOfBetsWon = 0;
        for(Bet bet : bets) {
            noOfBets++;
            if(betService.isBetWonById(bet.getId())) {
                noOfBetsWon++;
            }
        }
        if(noOfBetsWon == noOfBets) {
            return true;
        }
        return false;
    }

}
