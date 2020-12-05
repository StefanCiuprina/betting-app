package com.example.application.views.currentticket;

import com.example.application.data.BetType;
import com.example.application.data.entity.Bet;
import com.example.application.data.entity.BettingTicket;
import com.example.application.data.service.AuthService;
import com.example.application.data.service.BetService;
import com.example.application.data.service.BettingTicketService;
import com.example.application.views.mybets.MybetsView;
import com.example.application.views.wallet.WalletView;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Current ticket")
@CssImport(value = "./styles/views/currentticket/currentticket-view.css", include = "lumo-badge")
public class CurrentticketView extends Div implements AfterNavigationObserver {

    Grid<BetDisplay> grid = new Grid<>();

    private BetService betService;
    private BettingTicketService bettingTicketService;
    private AuthService authService;

    private String betIDs;
    private double finalOdd;
    private double possibleAmountToWin;
    private double amountPlaced;

    public CurrentticketView(@Autowired BetService betService, @Autowired BettingTicketService bettingTicketService,
                             @Autowired AuthService authService) {
        setId("currentticket-view");
        this.betService = betService;
        this.bettingTicketService = bettingTicketService;
        this.authService = authService;
        addClassName("currentticket-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::createCard);
        add(grid);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
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

        if (betDisplay.getBetDisplayTipe() == BetDisplayType.BET) {
            Image imageHome = new Image();
            imageHome.setSrc(betDisplay.getImageHome());
            imageHome.addClassName("imageHome");
            Span nameTeamHome = new Span(betDisplay.getNameTeamHome());
            nameTeamHome.addClassName("nameTeamHome");

            Image imageAway = new Image();
            Span nameTeamAway = new Span(betDisplay.getNameTeamAway());
            nameTeamHome.addClassName("nameTeamAway");
            imageAway.setSrc(betDisplay.getImageAway());
            imageAway.addClassName("imageAway");

            Label space1 = new Label("_____");
            space1.getStyle().set("color", "white");
            Label space2 = new Label("_____");
            space2.getStyle().set("color", "white");

            Span vs = new Span("-");
            Span status = new Span("");
            Span wonOrLost = new Span("");
            IronIcon wonOrLostIcon = new IronIcon("vaadin", "play-arrow");
            if (betDisplay.isPlaced()) {
                if (betDisplay.getOngoing().equals("finished")) {
                    vs.setText(betDisplay.getScoreHome() + "-" + betDisplay.getScoreAway());
                    String wonOrLostSt = betDisplay.isWon() ? "won" : "lost";
                    wonOrLost.setText(wonOrLostSt);
                    String wonOrLostIconSt = betDisplay.isWon() ? "check" : "close";
                    wonOrLostIcon = new IronIcon("vaadin", wonOrLostIconSt);
                }
                status.setText(betDisplay.getOngoing());
            }
            HorizontalLayout winStatus = new HorizontalLayout();
            winStatus.add(wonOrLost, wonOrLostIcon);
            winStatus.setAlignItems(FlexComponent.Alignment.CENTER);


            HorizontalLayout header = new HorizontalLayout();
            header.addClassName("header");
            header.setSpacing(false);
            header.getThemeList().add("spacing-s");


            Span date = new Span(betDisplay.getDate());
            date.addClassName("date");
            Span time = new Span(betDisplay.getTime());
            time.addClassName("time");
            header.add(imageHome, nameTeamHome, space1, vs, space2, nameTeamAway, imageAway);
            header.setAlignItems(FlexComponent.Alignment.CENTER);

            HorizontalLayout betTypeLayout = new HorizontalLayout();
            Span titleBetType = new Span("You have bet on: ");
            Span betType = new Span(betDisplay.getBetType());
            betType.addClassName("betType");
            betTypeLayout.add(titleBetType, betType);

            HorizontalLayout oddLayout = new HorizontalLayout();
            Span titleOdd = new Span("Odd:");
            Span odd = new Span(betDisplay.getOdd());
            betType.addClassName("odd");
            oddLayout.add(titleOdd, odd);

            HorizontalLayout dateAndTime = new HorizontalLayout();
            dateAndTime.add(date, time);
            dateAndTime.setAlignItems(FlexComponent.Alignment.CENTER);

            Button buttonDeleteBet = new Button("Delete");
            buttonDeleteBet.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    betService.deleteBetById(betDisplay.getBetId());
                    UI.getCurrent().getPage().reload();
                }
            });

            description.add(header, dateAndTime, betTypeLayout, oddLayout, status, winStatus);
            if(!betDisplay.isPlaced()) {
                description.add(buttonDeleteBet);
            }

        } else {
            Span textTitleFinalOdd = new Span("Odd: ");
            Span textFinalOdd = new Span(betDisplay.getFinalOdd());

            HorizontalLayout amountAndPlace = new HorizontalLayout();
            Span textTitlePossibleSumToWin = new Span("Possible sum to win: ");
            Span textPossibleSumToWin = new Span("0");

            Button buttonPlaceBet = new Button("Place bet");
            buttonPlaceBet.setEnabled(false);

            if (betDisplay.getBetDisplayTipe() == BetDisplayType.FINISH_CREATING_BET) {
                Span textTitleAmountToBet = new Span("Amount: ");
                TextField textFieldAmountToBet = new TextField();
                textFieldAmountToBet.setValueChangeMode(ValueChangeMode.EAGER);
                textFieldAmountToBet.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>() {
                    @Override
                    public void valueChanged(AbstractField.ComponentValueChangeEvent<TextField, String> textFieldStringComponentValueChangeEvent) {
                        try {
                            possibleAmountToWin = Double.parseDouble(betDisplay.getFinalOdd()) * Double.parseDouble(textFieldAmountToBet.getValue());
                            possibleAmountToWin = round(possibleAmountToWin, 2);
                            amountPlaced = Double.parseDouble(textFieldAmountToBet.getValue());
                            textPossibleSumToWin.setText(String.valueOf(possibleAmountToWin));
                            buttonPlaceBet.setEnabled(true);
                        } catch (NumberFormatException e) {
                            textPossibleSumToWin.setText("0");
                            buttonPlaceBet.setEnabled(false);
                        }
                    }
                });


                buttonPlaceBet.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        if(amountPlaced <= WalletView.currentBalance) {
                            WalletView.updateCurrentBalance(-amountPlaced);
                            authService.updateBalance(WalletView.currentBalance);
                            BettingTicket bettingTicket = new BettingTicket(AuthService.currentUserID,
                                    betIDs, finalOdd,
                                    possibleAmountToWin, amountPlaced,
                                    true, false,
                                    LocalDate.now(),
                                    false);
                            bettingTicketService.addBettingTicket(bettingTicket);
                            Notification.show("Bet placed!");
                            betService.setAllBetsPlaced(AuthService.currentUserID);
                            UI.getCurrent().getPage().setLocation("mybets");
                        } else {
                            Notification.show("Insufficient funds!");
                        }
                    }
                });

                Button buttonDeleteAll = new Button("Delete all");
                buttonDeleteAll.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        betService.deleteAllUnplacedBetsOfId(AuthService.currentUserID);
                        UI.getCurrent().getPage().reload();
                    }
                });

                amountAndPlace.add(textTitleAmountToBet, textFieldAmountToBet, textTitlePossibleSumToWin, textPossibleSumToWin);
                description.add(textTitleFinalOdd, textFinalOdd, amountAndPlace, buttonPlaceBet, buttonDeleteAll);
            } else if (betDisplay.getBetDisplayTipe() == BetDisplayType.FINISHED_BET) {
                if (betDisplay.getOngoing().equals("finished")) {
                    textTitlePossibleSumToWin.setText("Won: ");
                    if (betDisplay.isWon()) {
                        textPossibleSumToWin.setText(betDisplay.getPossibleAmountToWin());
                    } else {
                        textPossibleSumToWin.setText("0");
                    }
                } else {
                    textPossibleSumToWin.setText(betDisplay.getPossibleAmountToWin());
                }
                Button buttonClose = new Button("Close");
                buttonClose.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        MybetsView.betIDs = null;
                        UI.getCurrent().getPage().setLocation("mybets");
                    }
                });
                HorizontalLayout layoutAmoundPlaced = new HorizontalLayout();
                Span titleAmountPlaced = new Span("Amount placed:");
                Span amountPlaced = new Span(betDisplay.getAmountPlaced());
                layoutAmoundPlaced.add(titleAmountPlaced, amountPlaced);
                amountAndPlace.add(textTitlePossibleSumToWin, textPossibleSumToWin);
                description.add(textTitleFinalOdd, textFinalOdd, layoutAmoundPlaced, amountAndPlace, buttonClose);
            }
        }
        card.add(description);
        return card;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        boolean placeBetMode = true;
        boolean ongoing = false;
        boolean betLost = false;
        boolean won = true;

        List<BetDisplay> betDisplays = new ArrayList<>();
        List<Bet> bets;

        if (!betService.isEmpty()) {
            if (!betService.noCurrentBetsOfUser(AuthService.currentUserID)) {
                bets = betService.getAllCurrentBetsOfUser(AuthService.currentUserID);
            } else {
                bets = new ArrayList<>();
                this.betIDs = MybetsView.betIDs;
                if (this.betIDs == null) {
                    grid.setItems(betDisplays);
                    return;
                }
                placeBetMode = false;
                String[] betIDs = this.betIDs.split("_");
                for (String betIDString : betIDs) {
                    int betID = Integer.parseInt(betIDString);
                    bets.add(betService.getBetByID(betID));
                }
            }

            finalOdd = 1;
            StringBuilder betIDs = new StringBuilder();
            for (Bet bet : bets) {
                String imageHome, imageAway;
                imageHome = getTeamLogo(bet.getHomeTeam());
                imageAway = getTeamLogo(bet.getAwayTeam());
                betDisplays.add(createBet(bet.getId(), imageHome, imageAway, bet.getHomeTeam(), bet.getAwayTeam(),
                        bet.getDate(), bet.getTime(),
                        bet.getBetType(), bet.getOdd(),
                        bet.isFinished(), bet.getScoreHome(), bet.getScoreAway(), bet.isPlaced(),
                        bet.isWon()));
                finalOdd *= bet.getOdd();
                if (!bet.isFinished()) {
                    ongoing = true;
                }
                if (placeBetMode) {
                    betIDs.append(bet.getId());
                    betIDs.append("_");
                }
                if (bet.isFinished() && !bet.isWon()) {
                    betLost = true;
                }
                if(!bet.isWon()) {
                    won = false;
                }
            }
            finalOdd = round(finalOdd, 2);
            if (placeBetMode) {
                betIDs.deleteCharAt(betIDs.length() - 1); //delete last _
                betDisplays.add(finishCreatingBet(finalOdd, betIDs.toString()));
                this.betIDs = betIDs.toString();
            } else {
                betDisplays.add(finishedBet(ongoing, finalOdd, MybetsView.possibleAmountToWin, betLost, won, MybetsView.amountPlaced));
            }
        }


        grid.setItems(betDisplays);
    }

    private String getTeamLogo(String teamName) {
        switch (teamName) {
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

    private static BetDisplay createBet(int id, String imageHome, String imageAway, String nameTeamHome, String nameTeamAway,
                                        LocalDate date, LocalTime time, BetType betType, double odd, boolean finished, int scoreHome, int scoreAway,
                                        boolean placed,
                                        boolean won) {
        BetDisplay b = new BetDisplay();
        b.setBetId(id);
        b.setBetDisplayTipe(BetDisplayType.BET);
        b.setImageHome(imageHome);
        b.setImageAway(imageAway);
        b.setNameTeamHome(nameTeamHome);
        b.setNameTeamAway(nameTeamAway);
        b.setDate(date.toString());
        b.setTime(time.toString());
        b.setOngoing(finished ? "finished" : "ongoing");
        b.setScoreHome(String.valueOf(scoreHome));
        b.setScoreAway(String.valueOf(scoreAway));
        b.setPlaced(placed);
        b.setWon(won);

        String betTypeString;
        switch (betType) {
            case X:
                betTypeString = "X";
                break;
            case ONE:
                betTypeString = "1";
                break;
            case TWO:
                betTypeString = "2";
                break;
            case ONEX:
                betTypeString = "1X";
                break;
            case TWOX:
                betTypeString = "2x";
                break;
            default:
                betTypeString = "error";
        }
        b.setBetType(betTypeString);
        b.setOdd(String.valueOf(odd));

        return b;
    }

    private static BetDisplay finishCreatingBet(double finalOdd, String betIDs) {
        BetDisplay b = new BetDisplay();
        b.setBetDisplayTipe(BetDisplayType.FINISH_CREATING_BET);
        b.setFinalOdd(String.valueOf(finalOdd));
        b.setBetIDs(betIDs);
        return b;
    }

    private static BetDisplay finishedBet(boolean ongoing, double finalOdd, String possibleAmountToWin, boolean betLost,
                                          boolean won, String amountPlaced) {
        BetDisplay b = new BetDisplay();
        b.setBetDisplayTipe(BetDisplayType.FINISHED_BET);
        if(betLost) {
            b.setOngoing("finished");
        } else {
            b.setOngoing(ongoing ? "ongoing" : "finished");
        }
        b.setFinalOdd(String.valueOf(finalOdd));
        b.setPossibleAmountToWin(possibleAmountToWin);
        b.setAmountPlaced(amountPlaced);
        b.setWon(won);
        return b;
    }

}
