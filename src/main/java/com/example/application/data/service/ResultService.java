package com.example.application.data.service;

import com.example.application.data.entity.Result;
import com.example.application.data.entity.UpcomingMatch;
import com.example.application.data.repositories.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {

    private final ResultRepository resultRepository;

    public ResultService(@Autowired ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public List<Result> findAll() {
        return resultRepository.findAll();
    }

    public void addResult(Result match) {
        List<UpcomingMatch> checkForDuplicates = resultRepository.getResultsByMatchDetails(match.getMatch_date(), match.getMatch_time(), match.getHome_team(), match.getAway_team());
        if(checkForDuplicates == null || checkForDuplicates.size() == 0) {
            resultRepository.save(match);
        }
    }

    public void removeMatch(Result match) {
        resultRepository.delete(match);
    }
}
