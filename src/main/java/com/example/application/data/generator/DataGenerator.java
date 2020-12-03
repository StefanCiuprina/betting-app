package com.example.application.data.generator;

import com.example.application.data.BetType;
import com.example.application.data.entity.Bet;
import com.example.application.data.entity.BettingTicket;
import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
import com.example.application.data.repositories.BetRepository;
import com.example.application.data.repositories.BettingTicketRepository;
import com.example.application.data.repositories.UserRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.Month;

@SpringComponent
public class DataGenerator {


    @Bean
    public CommandLineRunner loadData(BetRepository betRepository, BettingTicketRepository bettingTicketRepository, UserRepository userRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());

//            betRepository.save(new Bet(62, "FC Voluntari", "CFR Cluj", BetType.ONE, 1.12, LocalDate.of(2000, Month.JUNE, 30), false));
//            betRepository.save(new Bet(62, "Astra", "CFR Cluj", BetType.ONE, 1.63, LocalDate.of(2000, Month.JUNE, 30), false));
//            bettingTicketRepository.save(new BettingTicket(62, "2_3", 3.47, 275,
//                    true, false, LocalDate.now()));
//            bettingTicketRepository.save(new BettingTicket(62, "2_3", 4.2, 105,
//                    false, false, LocalDate.now()));
//            bettingTicketRepository.save(new BettingTicket(62, "2_3", 85.2, 3845.75,
//                    false, true, LocalDate.now()));
            logger.info("Generated demo data");

            if (userRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            LocalDate date = LocalDate.of(1999, Month.MARCH, 18);
            LocalDate date2 = LocalDate.of(1999, Month.SEPTEMBER, 6);


            userRepository.save(new User("stefan", "s", Role.ADMIN, "stefan.ciuprina@gmail.com", "Stefan Ciuprina", date));
            userRepository.save(new User("andrei", "a", Role.USER, "andreiavram99@gmail.com", "Andrei Avram", date2));


        };
    }

}