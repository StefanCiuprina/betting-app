package com.example.application.views.currentticket;

import com.example.application.data.BetType;
import com.example.application.data.entity.Bet;
import com.example.application.data.service.AuthService;
import com.example.application.data.service.BetService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@PageTitle("Current ticket")
@CssImport(value = "./styles/views/currentticket/currentticket-view.css", include = "lumo-badge")
public class CurrentticketView extends Div implements AfterNavigationObserver {

    Grid<BetDisplay> grid = new Grid<>();

    private BetService betService;

    public CurrentticketView(@Autowired BetService betService) {
        setId("currentticket-view");
        this.betService = betService;
        addClassName("currentticket-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::createCard);
        add(grid);
    }

    private HorizontalLayout createCard(BetDisplay betDisplay) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        if(betDisplay.getBetDisplayTipe() == BetDisplayType.BET) {
            Image imageHome = new Image();
            imageHome.setSrc(betDisplay.getImageHome());
            imageHome.addClassName("imageHome");
            Image imageAway = new Image();
            imageAway.setSrc(betDisplay.getImageAway());
            imageAway.addClassName("imageAway");


            HorizontalLayout header = new HorizontalLayout();
            header.addClassName("header");
            header.setSpacing(false);
            header.getThemeList().add("spacing-s");

            Span nameTeamHome = new Span(betDisplay.getNameTeamHome());
            nameTeamHome.addClassName("nameTeamHome");
            Span nameTeamAway = new Span(betDisplay.getNameTeamAway());
            nameTeamHome.addClassName("nameTeamAway");
            Span date = new Span(betDisplay.getDate());
            date.addClassName("date");
            header.add(nameTeamHome, nameTeamAway, date);

            Span betType = new Span(betDisplay.getBetType());
            betType.addClassName("betType");
            Span odd = new Span(betDisplay.getOdd());
            betType.addClassName("odd");

            description.add(header, betType, odd);
            card.add(imageHome, imageAway, description);
        } else if(betDisplay.getBetDisplayTipe() == BetDisplayType.FINISHBET) {
            Text textTitleFinalOdd = new Text("Odd: ");
            Text textFinalOdd = new Text(betDisplay.getFinalOdd());

            HorizontalLayout amountAndPlace = new HorizontalLayout();
            Text textTitleAmountToBet = new Text("Amount: ");
            TextField textFieldAmountToBet = new TextField();
            Text textTitlePossibleSumToWin = new Text("Possible sum to win: ");
            Text textPossibleSumToWin = new Text("0");
            textFieldAmountToBet.setValueChangeMode(ValueChangeMode.EAGER);
            textFieldAmountToBet.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>() {
                @Override
                public void valueChanged(AbstractField.ComponentValueChangeEvent<TextField, String> textFieldStringComponentValueChangeEvent) {
                    try {
                        double possibleSumToWin = Double.parseDouble(betDisplay.getFinalOdd()) * Double.parseDouble(textFieldAmountToBet.getValue());
                        textPossibleSumToWin.setText(String.valueOf(possibleSumToWin));
                    } catch (NumberFormatException e) {
                        textPossibleSumToWin.setText("0");
                    }
                }
            });
            Button buttonPlaceBet = new Button("Place bet");

            amountAndPlace.add(textTitleAmountToBet, textFieldAmountToBet, textTitlePossibleSumToWin, textPossibleSumToWin);
            description.add(textTitleFinalOdd, textFinalOdd, amountAndPlace, buttonPlaceBet);
            card.add(description);
        }
        return card;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        List<Bet> bets = betService.getAllCurrentBetsOfUser(AuthService.currentUserID);

        List<BetDisplay> betDisplays = new ArrayList<>();
        double finalOdd = 1;
        StringBuilder betIDs = new StringBuilder();
        for(Bet bet : bets) {
            String imageHome, imageAway;
            imageHome = getTeamLogo(bet.getHomeTeam());
            imageAway = getTeamLogo(bet.getAwayTeam());
            betDisplays.add(createBet(imageHome, imageAway, bet.getHomeTeam(), bet.getAwayTeam(),
                    bet.getDate(), bet.getBetType(), bet.getOdd()));
            finalOdd *= bet.getOdd();
            betIDs.append(bet.getId());
            betIDs.append("_");
        }
        betIDs.deleteCharAt(betIDs.length()-1); //delete last _
        betDisplays.add(finishBet(finalOdd, betIDs.toString()));

        grid.setItems(betDisplays);
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

    private static BetDisplay createBet(String imageHome, String imageAway, String nameTeamHome, String nameTeamAway
            , LocalDate date, BetType betType, double odd){
        BetDisplay b = new BetDisplay();
        b.setBetDisplayTipe(BetDisplayType.BET);
        b.setImageHome(imageHome);
        b.setImageAway(imageAway);
        b.setNameTeamHome(nameTeamHome);
        b.setNameTeamAway(nameTeamAway);
        b.setDate(date.toString());

        String betTypeString;
        switch(betType) {
            case X: betTypeString = "X"; break;
            case ONE: betTypeString = "1"; break;
            case TWO: betTypeString = "2"; break;
            case ONEX: betTypeString = "1X"; break;
            case TWOX: betTypeString = "2x"; break;
            default: betTypeString = "error";
        }
        b.setBetType(betTypeString);
        b.setOdd(String.valueOf(odd));

        return b;
    }

    private static BetDisplay finishBet(double finalOdd, String betIDs) {
        BetDisplay b = new BetDisplay();
        b.setBetDisplayTipe(BetDisplayType.FINISHBET);
        b.setFinalOdd(String.valueOf(finalOdd));
        b.setBetIDs(betIDs);
        return b;
    }

}
