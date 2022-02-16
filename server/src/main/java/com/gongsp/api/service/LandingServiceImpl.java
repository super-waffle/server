package com.gongsp.api.service;

import com.gongsp.db.entity.Landing;
import com.gongsp.db.repository.LandingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("landingService")
public class LandingServiceImpl implements LandingService{

    @Autowired
    private LandingRepository landingRepository;

    @Override
    public void saveDailyStats(Integer people, Integer totalTime) {
        Landing landing = new Landing();
        landing.setTotalUser(people);
        landing.setTotalTime(totalTime);
        landingRepository.save(landing);
    }
}
