package com.example.application.views.upcomingMatchesAdder;

import com.example.application.data.service.UpcomingMatchService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

@Route(value = "addmatches")
@PageTitle("Add upcoming matches")
public class UpcomingMatchesAdderView extends Div {

    private UpcomingMatchService upcomingMatchService;
    private DatePicker matchDatePicker = new DatePicker();
    private TimePicker matchTimePicker = new TimePicker();
    private ComboBox<String> homeTeamComboBox = new ComboBox<>();
    private ComboBox<String> awayTeamComboBox = new ComboBox<>();
    private Button addMatchButton = new Button("Add match");

    public UpcomingMatchesAdderView(@Autowired UpcomingMatchService upcomingMatchService) {
        this.upcomingMatchService = upcomingMatchService;
        setId("upcoming-matches-view");
        configureDateTimePicker();
        configureComboBox();


        HorizontalLayout h1 = new HorizontalLayout(matchDatePicker, matchTimePicker, homeTeamComboBox, awayTeamComboBox);
        add(h1);

    }

    private void configureComboBox() {
        homeTeamComboBox.setItems(upcomingMatchService.getFirstLeagueCurrentTeams());
        awayTeamComboBox.setItems(upcomingMatchService.getFirstLeagueCurrentTeams());
        homeTeamComboBox.setLabel("Home team");
        awayTeamComboBox.setLabel("Away team");
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
