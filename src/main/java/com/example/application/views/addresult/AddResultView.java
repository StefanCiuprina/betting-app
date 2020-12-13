package com.example.application.views.addresult;

import com.example.application.data.entity.Bet;
import com.example.application.data.entity.Result;
import com.example.application.data.entity.UpcomingMatch;
import com.example.application.data.service.BetService;
import com.example.application.data.service.ResultService;
import com.example.application.data.service.TeamsService;
import com.example.application.data.service.UpcomingMatchService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

@PageTitle("Add result")
@CssImport(value = "./styles/views/addresult/addresult-view.css", include = "lumo-badge")
public class AddResultView extends VerticalLayout {

    private ResultService resultService;
    private BetService betService;

    private DatePicker matchDatePicker = new DatePicker();
    private TimePicker matchTimePicker = new TimePicker();
    private ComboBox<String> homeTeamComboBox = new ComboBox<>();
    private ComboBox<String> awayTeamComboBox = new ComboBox<>();
    private TextField scoreHome = new TextField();
    private TextField scoreAway = new TextField();
    private Button addMatchButton = new Button("Add match");
    private Button deleteMatchButton = new Button("Delete match");
    private Grid<Result> grid = new Grid<>();

    public AddResultView(@Autowired ResultService resultService, @Autowired BetService betService) {
        this.resultService = resultService;
        this.betService = betService;
        setSizeFull();
        setId("addresult-view");
        configureDateTimePicker();
        configureComboBox();
        configureAddButton();
        configureDeleteButton();
        configureScoreTextFields();
        configureGrid();
        VerticalLayout v1 = new VerticalLayout();
        HorizontalLayout h1 = new HorizontalLayout(matchDatePicker, matchTimePicker, homeTeamComboBox, awayTeamComboBox,
                scoreHome, scoreAway);
        h1.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        v1.add(h1, addMatchButton, deleteMatchButton);
        Div content = new Div(grid);
        content.setSizeFull();
        updateGrid();
        add(h1, v1, content);
    }

    private void configureScoreTextFields() {
        scoreHome.setLabel("Score home");
        scoreAway.setLabel("Score away");
    }

    private void configureDeleteButton() {
        deleteMatchButton.addClickListener(buttonClickEvent -> {
            Result result = grid.asSingleSelect().getValue();
            if(result != null) {
                resultService.removeMatch(result);
                updateGrid();
            }
        });
    }

    private void updateGrid() {
        grid.setItems(resultService.findAll());
    }

    private void configureGrid() {
        grid.addClassName("addresult-grid");
        grid.setSizeFull();
        grid.addColumn(Result::getMatch_date).setHeader("Match date");
        grid.addColumn(Result::getMatch_time).setHeader("Match time");
        grid.addColumn(Result::getHome_team).setHeader("Home team");
        grid.addColumn(Result::getAway_team).setHeader("Away team");
        grid.addColumn(Result::getScoreHome).setHeader("Score home team");
        grid.addColumn(Result::getScoreAway).setHeader("Score away team");

    }

    private void configureAddButton() {
        addMatchButton.addClickListener(buttonClickEvent ->  {
            if(checkForValidInput()) {
                Result result = new Result(matchDatePicker.getValue(), matchTimePicker.getValue(), homeTeamComboBox.getValue(), awayTeamComboBox.getValue(),
                        Integer.parseInt(scoreHome.getValue()),
                        Integer.parseInt(scoreAway.getValue()));
                resultService.addResult(result);
                updateBetsWithResult(result);
                updateGrid();
            }
        });

    }

    private void updateBetsWithResult(Result result) {
        List<Bet> foundBets = betService.getAllBetsByMatch(result.getHome_team(), result.getAway_team(),
                                                            result.getMatch_date(), result.getMatch_time());
        for(Bet bet : foundBets) {
            bet.setScoreHome(result.getScoreHome());
            bet.setScoreAway(result.getScoreAway());
            bet.setFinished(true);

            bet.setWon(false);
            switch (bet.getBetType()) {
                case ONE:
                    if(result.getScoreHome() > result.getScoreAway()) {
                        bet.setWon(true);
                    }
                    break;
                case X:
                    if(result.getScoreHome() == result.getScoreAway()) {
                        bet.setWon(true);
                    }
                    break;
                case TWO:
                    if(result.getScoreHome() < result.getScoreAway()) {
                        bet.setWon(true);
                    }
                default:
                    System.out.println("error: invalid bet type");
            }
            betService.updateBet(bet);
        }
    }

    private boolean checkForValidInput() {

        try {
            Integer.parseInt(scoreHome.getValue());
            Integer.parseInt(scoreAway.getValue());
        } catch(NumberFormatException e) {
            return false;
        }

        return homeTeamComboBox.getValue() != null && awayTeamComboBox.getValue() != null &&
                !homeTeamComboBox.getValue().equals("") && !awayTeamComboBox.getValue().equals("") &&
                !homeTeamComboBox.getValue().equals(awayTeamComboBox.getValue());
    }

    private void configureComboBox() {
        homeTeamComboBox.setItems(TeamsService.getFirstLeagueCurrentTeams());
        awayTeamComboBox.setItems(TeamsService.getFirstLeagueCurrentTeams());
        homeTeamComboBox.setLabel("Home team");
        awayTeamComboBox.setLabel("Away team");
        homeTeamComboBox.setAllowCustomValue(false);
        awayTeamComboBox.setAllowCustomValue(false);
    }

    private void configureDateTimePicker() {
        LocalDate dateNow = LocalDate.now();
        LocalTime timeSetter = LocalTime.of(12,0);
        matchDatePicker.setValue(dateNow);
        matchDatePicker.setLabel("Match date");
        matchTimePicker.setValue(timeSetter);
        matchTimePicker.setLabel("Match time");
        matchDatePicker.setMax(dateNow);
        matchTimePicker.setStep(Duration.ofMinutes(15));
        matchTimePicker.setLocale(Locale.ITALY);
        matchDatePicker.setLocale(Locale.ITALY);
    }

}
