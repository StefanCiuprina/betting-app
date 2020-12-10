package com.example.application.data.service;

import com.example.application.data.entity.UpcomingMatch;
import com.example.application.data.repositories.UpcomingMatchesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class UpcomingMatchService {

    private final UpcomingMatchesRepository upcomingMatchesRepository;

    public UpcomingMatchService(UpcomingMatchesRepository upcomingMatchesRepository) {
        this.upcomingMatchesRepository = upcomingMatchesRepository;
    }

    public List<UpcomingMatch> findAll() {
        deletePastMatches();
        return upcomingMatchesRepository.findAll();
    }

    public void addUpcomingMatch(UpcomingMatch match) {
        List<UpcomingMatch> checkForDuplicates = upcomingMatchesRepository.getUpcomingMatchByAllProperties(match.getMatch_date(), match.getMatch_time(), match.getHome_team(), match.getAway_team());
        if(checkForDuplicates == null || checkForDuplicates.size() == 0) {
            upcomingMatchesRepository.save(match);
        }
    }

    public void removeMatch(UpcomingMatch upcomingMatch) {
        upcomingMatchesRepository.delete(upcomingMatch);
    }

    private void deletePastMatches() {
        upcomingMatchesRepository.deleteAllMatchesBeforeCurrentDate();
        upcomingMatchesRepository.deleteAllMatchesFromCurrentDateAndTimeDifferenceOf2Hours();
    }
}
