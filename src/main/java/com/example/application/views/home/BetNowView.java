package com.example.application.views.home;

import com.example.application.data.service.BetService;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Bet Now")
@CssImport(value = "./styles/views/home/home-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class BetNowView extends Div {

    BetService betService;

    public BetNowView(@Autowired BetService betService) {
        setId("home-view");
        this.betService = betService;
        addClassName("home-view");
        setSizeFull();
    }

}
