package com.example.application.views.home;

import com.example.application.data.BetType;
import com.example.application.data.entity.Bet;
import com.example.application.data.entity.PastMatch;
import com.example.application.data.service.AuthService;
import com.example.application.data.service.BetService;
import com.example.application.data.service.MatchWithOddsService;
import com.example.application.data.service.PastMatchService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@PageTitle("Bet Now")
@CssImport(value = "./styles/views/home/home-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class BetNowView extends VerticalLayout implements AfterNavigationObserver {

    private BetService betService;
    private MatchWithOddsService matchWithOddsService;
    private Grid<MatchWithOdds> grid = new Grid<>();
    private Grid <Head2HeadMatch> head2HeadGrid = new Grid<>();
    private VerticalLayout head2HeadLayout = new VerticalLayout();
    private Button buttonThatOpenedTheGrid = null;
    private PastMatchService pastMatchService;

    public BetNowView(@Autowired BetService betService, @Autowired MatchWithOddsService matchWithOddsService, @Autowired PastMatchService pastMatchService) {
        this.betService = betService;
        this.matchWithOddsService = matchWithOddsService;
        this.pastMatchService = pastMatchService;
        setClassName("home-view");
        setSizeFull();
        head2HeadLayout.setSizeFull();
        head2HeadLayout.setSpacing(false);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::populateGrid);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        head2HeadGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        head2HeadGrid.setSelectionMode(Grid.SelectionMode.NONE);
        head2HeadGrid.addComponentColumn(this::populateHead2HeadGrid);
        head2HeadLayout.add(head2HeadGrid);
        head2HeadLayout.setHeight("60%");
        add(grid, head2HeadLayout);
        head2HeadLayout.setVisible(false);

    }

    private HorizontalLayout populateHead2HeadGrid(Head2HeadMatch head2HeadMatch) {
        HorizontalLayout gridItem = new HorizontalLayout();

        Span matchDate = new Span(head2HeadMatch.getMatchDate().toString());
        Span homeTeam = new Span(head2HeadMatch.getHomeTeam());
        Span awayTeam = new Span(head2HeadMatch.getAwayTeam());
        Span goals = new Span(head2HeadMatch.getHomeTeamGoals() + " - " + head2HeadMatch.getAwayTeamGoals());
        Span line = new Span(" - ");
        HorizontalLayout h1 = new HorizontalLayout();
        HorizontalLayout h2 = new HorizontalLayout();
        HorizontalLayout h3 = new HorizontalLayout();
        HorizontalLayout h4 = new HorizontalLayout();
        HorizontalLayout h5 = new HorizontalLayout(line);
        h1.setSpacing(false);
        h2.setSpacing(false);
        h3.setSpacing(false);
        h4.setSpacing(false);
        h5.setSpacing(false);
        h1.add(matchDate);
        h2.add(homeTeam);
        h3.add(awayTeam);
        h4.add(goals);

        if(head2HeadMatch.getResult().equals("home")) {
            homeTeam.getStyle().set("color", "red");
        }else if(head2HeadMatch.getResult().equals("away")) {
            awayTeam.getStyle().set("color", "red");
        }else {
            homeTeam.getStyle().set("color", "blue");
            awayTeam.getStyle().set("color", "blue");
        }

        gridItem.add(h1, h2, h5, h3, h4);
        gridItem.setAlignItems(Alignment.STRETCH);

        return gridItem;
    }

    private HorizontalLayout populateGrid(MatchWithOdds matchWithOdds) {
        HorizontalLayout gridItem = new HorizontalLayout();
        gridItem.setSpacing(false);

        Button homeWinButton = new Button("1 _____ " + matchWithOdds.getBetOdds().getHomeWin());
        Button drawButton = new Button("X _____" + matchWithOdds.getBetOdds().getDraw());
        Button awayWinButton = new Button("2 _____ " + matchWithOdds.getBetOdds().getAwayWin());
        Button seeH2HComparisonButton = new Button("H2H");

        homeWinButton.setDisableOnClick(true);
        awayWinButton.setDisableOnClick(true);
        drawButton.setDisableOnClick(true);

        homeWinButton.addClickListener(buttonClickEvent -> {
            awayWinButton.setEnabled(true);
            drawButton.setEnabled(true);
            placeBet("home", matchWithOdds);
        });


        drawButton.addClickListener(buttonClickEvent -> {
            awayWinButton.setEnabled(true);
            homeWinButton.setEnabled(true);
            placeBet("draw", matchWithOdds);
        });

        awayWinButton.addClickListener(buttonClickEvent -> {
            homeWinButton.setEnabled(true);
            drawButton.setEnabled(true);
            placeBet("away", matchWithOdds);
        });

        seeH2HComparisonButton.addClickListener(buttonClickEvent -> {
            if(head2HeadLayout.isVisible() && (buttonThatOpenedTheGrid != null && buttonThatOpenedTheGrid == seeH2HComparisonButton)) {
                head2HeadLayout.setVisible(false);
            }else {
                head2HeadLayout.setVisible(true);
                buttonThatOpenedTheGrid = seeH2HComparisonButton;
                updateHead2HeadLayout(matchWithOdds);
            }
        });

        VerticalLayout homeTeamLayout = new VerticalLayout();
        VerticalLayout awayTeamLayout = new VerticalLayout();
        VerticalLayout middleLayout = new VerticalLayout();
        HorizontalLayout dateLayout = new HorizontalLayout();
        HorizontalLayout oddsLayout = new HorizontalLayout();
        homeTeamLayout.setSpacing(false);
        awayTeamLayout.setSpacing(false);
        middleLayout.setSpacing(false);
        awayTeamLayout.setAlignItems(Alignment.END);



        Image homeTeamLogo = new Image();
        Image awayTeamLogo = new Image();
        homeTeamLogo.setSrc(matchWithOdds.getHomeTeamPicture());
        awayTeamLogo.setSrc(matchWithOdds.getAwayTeamPicture());

        homeTeamLayout.add(homeTeamLogo, new Span(matchWithOdds.getHomeTeam()));
        awayTeamLayout.add(awayTeamLogo, new Span(matchWithOdds.getAwayTeam()));

        dateLayout.add(new Span(matchWithOdds.getMatchDate().toString()), new Span(matchWithOdds.getMatchTime().toString()));


        oddsLayout.add(homeWinButton,  drawButton, awayWinButton);

        middleLayout.add(dateLayout, oddsLayout);
        gridItem.add(homeTeamLayout, middleLayout, awayTeamLayout,seeH2HComparisonButton);
        return gridItem;
    }

    private void updateHead2HeadLayout(MatchWithOdds matchWithOdds) {
        updateHead2HeadGrid(matchWithOdds);
        head2HeadLayout.removeAll();
        head2HeadLayout.add(getStatisticsLayout(matchWithOdds), head2HeadGrid);
    }

    private HorizontalLayout getStatisticsLayout(MatchWithOdds matchWithOdds) {
        int homeTeamWon = pastMatchService.getHowManyTimesTheFirstParameterTeamWonFromH2H(matchWithOdds.getHomeTeam(), matchWithOdds.getAwayTeam());
        int awayTeamWon = pastMatchService.getHowManyTimesTheFirstParameterTeamWonFromH2H(matchWithOdds.getAwayTeam(), matchWithOdds.getHomeTeam());
        int numberOfDraws = pastMatchService.getHowManyDrawsFromH2H(matchWithOdds.getAwayTeam(), matchWithOdds.getHomeTeam());
        HorizontalLayout statistics = new HorizontalLayout();
        VerticalLayout buttonLayout = new VerticalLayout();
        statistics.setSpacing(true);
        statistics.setAlignItems(Alignment.END);
        Button closeButton = new Button("Close H2H");
        closeButton.addClickListener(buttonClickEvent -> {
           head2HeadLayout.setVisible(false);
        });

        VerticalLayout results = new VerticalLayout();
        results.setSpacing(true);
        results.setAlignItems(Alignment.START);


        VerticalLayout v1 = new VerticalLayout();
        v1.add(new Span(matchWithOdds.getHomeTeam()));
        v1.add(new Span(homeTeamWon + ""));
        v1.setAlignItems(Alignment.CENTER);

        VerticalLayout v2 = new VerticalLayout();
        v2.add(new Span("Draw"));
        v2.add(new Span(numberOfDraws + ""));
        v2.setAlignItems(Alignment.CENTER);

        VerticalLayout v3 = new VerticalLayout();
        v3.add(new Span(matchWithOdds.getAwayTeam()));
        v3.add(new Span(awayTeamWon + ""));
        v3.setAlignItems(Alignment.CENTER);

        buttonLayout.add(closeButton);
        buttonLayout.setAlignItems(Alignment.CENTER);
        statistics.add(v1, v2, v3, buttonLayout);
        return statistics;
    }

    private void updateHead2HeadGrid(MatchWithOdds matchWithOdds) {
        List<PastMatch> pastHead2HeadMatches = pastMatchService.getHead2HeadMatches(matchWithOdds.getHomeTeam(), matchWithOdds.getAwayTeam());
        List<Head2HeadMatch> gridList = new ArrayList<>();

        for(PastMatch pastMatch : pastHead2HeadMatches) {
            Head2HeadMatch head2HeadMatch = new Head2HeadMatch();
            head2HeadMatch.setHomeTeam(pastMatch.getHome_team());
            head2HeadMatch.setAwayTeam(pastMatch.getAway_team());
            head2HeadMatch.setMatchDate(pastMatch.getMatch_date());
            head2HeadMatch.setHomeTeamGoals(pastMatch.getHome_goals());
            head2HeadMatch.setAwayTeamGoals(pastMatch.getAway_goals());
            gridList.add(head2HeadMatch);
        }
        Collections.sort(gridList);
        head2HeadGrid.setItems(gridList);
        head2HeadGrid.scrollToStart();
    }

    private void placeBet(String button, MatchWithOdds matchWithOdds) {
        float odd = getOddBasedOnButtonType(button, matchWithOdds);
        BetType betType = getBetTypeBasedOnButtonType(button);

        Bet foundBet = findBet(matchWithOdds);

        if(foundBet == null) {
            Bet bet = new Bet(AuthService.currentUserID, matchWithOdds.getHomeTeam(), matchWithOdds.getAwayTeam(), betType,
                    round2Decimals(odd), matchWithOdds.getMatchDate(),
                    matchWithOdds.getMatchTime(), false);
            betService.createBet(bet);
        }
        else {
            betService.deleteBetById(foundBet.getId());
            foundBet.setBetType(betType);
            foundBet.setOdd(round2Decimals(odd));
            betService.createBet(foundBet);
        }

    }

    private Bet findBet(MatchWithOdds matchWithOdds) {
        List<Bet> foundBets = betService.getAllCurrentBetsOfUser(AuthService.currentUserID);
        for(Bet bet : foundBets) {
            if(bet.getHomeTeam().equals(matchWithOdds.getHomeTeam()) && bet.getAwayTeam().equals(matchWithOdds.getAwayTeam()) &&
            bet.getDate().isEqual(matchWithOdds.getMatchDate()) && bet.getTime().equals(matchWithOdds.getMatchTime())) {
                return bet;
            }
        }
        return null;
    }

    private double round2Decimals(double f) {
        return (Math.round(f * 100.0) / 100.0);
    }

    private float getOddBasedOnButtonType(String button, MatchWithOdds matchWithOdds) {
        if(button.equals("home"))
            return matchWithOdds.getBetOdds().getHomeWin();
        else if(button.equals("draw"))
            return matchWithOdds.getBetOdds().getDraw();
        else
            return matchWithOdds.getBetOdds().getAwayWin();
    }

    private BetType getBetTypeBasedOnButtonType(String button){
        if(button.equals("home"))
            return BetType.ONE;
        else if(button.equals("draw"))
            return BetType.X;
        else
            return BetType.TWO;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        List<MatchWithOdds> matchWithOdds = matchWithOddsService.getMatchesWithOdds();
        grid.setItems(matchWithOdds);
    }
}
