package com.example.application.views.mybets;

import com.example.application.data.entity.Bet;
import com.example.application.data.entity.BettingTicket;
import com.example.application.data.service.AuthService;
import com.example.application.data.service.BettingTicketService;
import com.example.application.views.currentticket.BetDisplay;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@PageTitle("My bets")
@CssImport(value = "./styles/views/mybets/mybets-view.css", include = "lumo-badge")
public class MybetsView extends Div implements AfterNavigationObserver {

    Grid<BettingTicketDisplay> grid = new Grid<>();

    private BettingTicketService bettingTicketService;

    public MybetsView(@Autowired BettingTicketService bettingTicketService) {
        setId("mybets-view");
        this.bettingTicketService = bettingTicketService;
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

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Label space = new Label("_____");
        space.getStyle().set("color", "white");

        Label space2 = new Label("_____");
        space2.getStyle().set("color", "white");

        HorizontalLayout left = new HorizontalLayout();
        String titlePossibleAmountToWinString = bettingTicketDisplay.getOngoing().equals("ongoing") ?
                "Possible amount to win: " : "Won: ";
        Text titlePossibleAmountToWin = new Text(titlePossibleAmountToWinString);
        Span possibleAmountToWin = new Span(bettingTicketDisplay.getPossibleAmountToWin());
        possibleAmountToWin.addClassName("possibleAmountToWin");
        left.add(titlePossibleAmountToWin, possibleAmountToWin);
        left.setAlignItems(FlexComponent.Alignment.START);

        HorizontalLayout middle = new HorizontalLayout();
        Text titleOdd = new Text("Odd: ");
        Span odd = new Span(bettingTicketDisplay.getOdd());
        odd.addClassName("odd");
        middle.add(titleOdd, odd);
        middle.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout right = new HorizontalLayout();
        Text titleDatePlaced = new Text("Date Placed: ");
        Span datePlaced = new Span(bettingTicketDisplay.getDatePlaced());
        datePlaced.addClassName("datePlaced");
        right.add(titleDatePlaced, datePlaced);
        right.setAlignItems(FlexComponent.Alignment.END);

        header.add(left, middle, right);


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

        if(bettingTicketDisplay.getOngoing().equals("ongoing")) {
            status.add(ongoing, ongoingIcon);
        } else {
            status.add(ongoing, ongoingIcon, wonOrLost, wonOrLostIcon);
        }

        description.add(header, status);
        card.add(description);
        return card;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        List<BettingTicket> bettingTickets = bettingTicketService.getAllBettingTicketsOfUser(AuthService.currentUserID);

        List<BettingTicketDisplay> bettingTicketDisplays = new ArrayList<>();

        for(BettingTicket bettingTicket : bettingTickets) {
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
                    bettingTicket.getDatePlaced().toString(),
                    ongoing, ongoingIcon,
                    wonOrLost, wonOrLostIcon));
        }

        grid.setItems(bettingTicketDisplays);
    }

}
