package com.example.application.views.home;

import com.example.application.data.service.BetService;
import com.example.application.data.service.MatchWithOddsService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Bet Now")
@CssImport(value = "./styles/views/home/home-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class BetNowView extends VerticalLayout implements AfterNavigationObserver {

    private BetService betService;
    private MatchWithOddsService matchWithOddsService;
    private Grid<MatchWithOdds> grid = new Grid<>();

    public BetNowView(@Autowired BetService betService, @Autowired MatchWithOddsService matchWithOddsService) {
        this.betService = betService;
        this.matchWithOddsService = matchWithOddsService;
        setClassName("home-view");
        setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::populateGrid);
        add(grid);

    }

    private HorizontalLayout populateGrid(MatchWithOdds matchWithOdds) {
        HorizontalLayout gridItem = new HorizontalLayout();
        gridItem.setSpacing(false);

        Button homeWinButton = new Button("1 _____ " + matchWithOdds.getBetOdds().getHomeWin());
        Button drawButton = new Button("X _____" + matchWithOdds.getBetOdds().getDraw());
        Button awayWinButton = new Button("2 _____ " + matchWithOdds.getBetOdds().getAwayWin());

        homeWinButton.addClickListener(buttonClickEvent -> {
            System.out.println(homeWinButton.getText());
        });

        drawButton.addClickListener(buttonClickEvent -> {
            System.out.println(drawButton.getText());
        });

        awayWinButton.addClickListener(buttonClickEvent -> {
            System.out.println(awayWinButton.getText());
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
        gridItem.add(homeTeamLayout, middleLayout, awayTeamLayout);
        return gridItem;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        List<MatchWithOdds> matchWithOdds = matchWithOddsService.getMatchesWithOdds();
        grid.setItems(matchWithOdds);
    }
}
