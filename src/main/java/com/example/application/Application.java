package com.example.application;

import com.example.application.data.entity.PastMatch;
import com.example.application.data.service.OddsService;
import com.example.application.data.service.PastMatchService;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the  * and some desktop browsers.
 *
 */
@SpringBootApplication
@PWA(name = "auth-example", shortName = "auth-example")
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);

//        OddsService oddsService = (OddsService) context.getBean("oddsService");
//        System.out.println(oddsService.getBetOdds("FC Voluntari", "Academica Clinceni"));

    }

}
