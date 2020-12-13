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
@PWA(name = "betting-app", shortName = "betting-app")
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
