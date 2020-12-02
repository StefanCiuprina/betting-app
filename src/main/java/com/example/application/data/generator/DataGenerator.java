package com.example.application.data.generator;

import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
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
    public CommandLineRunner loadData(UserRepository userRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
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

            logger.info("Generated demo data");
        };
    }

}