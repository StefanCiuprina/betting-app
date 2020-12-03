package com.example.application.data.repositories;

import com.example.application.data.entity.UpcomingMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpcomingMatchesRepository extends JpaRepository<UpcomingMatch, Integer> {

}
