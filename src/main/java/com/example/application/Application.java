package com.example.application;

import com.example.application.data.entity.PastMatch;
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

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);

        PastMatchService matchService = (PastMatchService) context.getBean("pastMatchService");
        List<PastMatch> list1 = matchService.getHead2HeadMatches("CFR Cluj", "U Craiova");
        List<PastMatch> list2 = matchService.getMatchesWhereTheTeamWasAwayTeam("CFR Cluj");
        List<String> list = matchService.getListOfTeamsName();

        if(list == null) {
            System.out.println("the list is null");
        }else {
            System.out.println(list.size());
            for (String match : list) {
                System.out.println(match);
            }
        }

    }

}
