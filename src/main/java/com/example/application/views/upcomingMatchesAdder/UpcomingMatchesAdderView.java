package com.example.application.views.upcomingMatchesAdder;

import com.example.application.data.entity.UpcomingMatch;
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
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

@PageTitle("Add upcoming matches")
@CssImport(value = "./styles/views/addresult/addresult-view.css", include = "lumo-badge")
public class UpcomingMatchesAdderView extends VerticalLayout {

    private UpcomingMatchService upcomingMatchService;
    private DatePicker matchDatePicker = new DatePicker();
    private TimePicker matchTimePicker = new TimePicker();
    private ComboBox<String> homeTeamComboBox = new ComboBox<>();
    private ComboBox<String> awayTeamComboBox = new ComboBox<>();
    private Button addMatchButton = new Button("Add match");
    private Button deleteMatchButton = new Button("Delete match");
    private Grid<UpcomingMatch> grid = new Grid<>();

    public UpcomingMatchesAdderView(@Autowired UpcomingMatchService upcomingMatchService) {
        this.upcomingMatchService = upcomingMatchService;
        setSizeFull();
        setId("upcoming-matches-view");
        configureDateTimePicker();
        configureComboBox();
        configureAddButton();
        configureDeleteButton();
        configureGrid();
        HorizontalLayout h1 = new HorizontalLayout(matchDatePicker, matchTimePicker, homeTeamComboBox, awayTeamComboBox, addMatchButton, deleteMatchButton);
        h1.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        Div content = new Div(grid);
        content.setSizeFull();
        updateGrid();
        add(h1, content);
    }

    private void configureDeleteButton() {
        deleteMatchButton.addClickListener(buttonClickEvent -> {
            UpcomingMatch upcomingMatch = grid.asSingleSelect().getValue();
            if(upcomingMatch != null) {
                upcomingMatchService.removeMatch(upcomingMatch);
                updateGrid();
            }
        });
    }

    private void updateGrid() {
        grid.setItems(upcomingMatchService.findAll());
    }

    private void configureGrid() {
        grid.addClassName("upcoming-matches-grid");
        grid.setSizeFull();
        grid.addColumn(UpcomingMatch::getMatch_date).setHeader("Match date");
        grid.addColumn(UpcomingMatch::getMatch_time).setHeader("Match time");
        grid.addColumn(UpcomingMatch::getHome_team).setHeader("Home team");
        grid.addColumn(UpcomingMatch::getAway_team).setHeader("Away team");

    }

    private void configureAddButton() {
        addMatchButton.addClickListener(buttonClickEvent ->  {
            if(checkForValidInput()) {
                UpcomingMatch upcomingMatch = new UpcomingMatch(matchDatePicker.getValue(), matchTimePicker.getValue(), homeTeamComboBox.getValue(), awayTeamComboBox.getValue());
                upcomingMatchService.addUpcomingMatch(upcomingMatch);
                updateGrid();
            }
        });

    }

    private boolean checkForValidInput() {

        return homeTeamComboBox.getValue() != null && awayTeamComboBox.getValue() != null &&
                !homeTeamComboBox.getValue().equals("") && !awayTeamComboBox.getValue().equals("") &&
                !homeTeamComboBox.getValue().equals(awayTeamComboBox.getValue());
    }

    private void configureComboBox() {
        homeTeamComboBox.setItems(upcomingMatchService.getFirstLeagueCurrentTeams());
        awayTeamComboBox.setItems(upcomingMatchService.getFirstLeagueCurrentTeams());
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
        matchDatePicker.setMin(dateNow);
        matchTimePicker.setStep(Duration.ofMinutes(15));
        matchTimePicker.setLocale(Locale.ITALY);
        matchDatePicker.setLocale(Locale.ITALY);
    }


}
