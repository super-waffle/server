package com.gongsp.api.service;

import com.gongsp.db.entity.MeetingOnair;
import com.gongsp.db.entity.MeetingOnairId;
import com.gongsp.db.repository.MeetingOnairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("meetingOnairService")
public class MeetingOnairServiceImpl implements MeetingOnairService{

    @Autowired
    private MeetingOnairRepository meetingOnairRepository;

    @Override
    public void deleteOnair(Integer userSeq, Integer meetingSeq) {
        meetingOnairRepository.deleteById(new MeetingOnairId(userSeq, meetingSeq));
    }

    @Override
    public MeetingOnair createOnair(Integer userSeq, Integer meetingSeq, Boolean isHost) {
        MeetingOnair meetingOnair = new MeetingOnair();
        meetingOnair.setMeetingOnairId(new MeetingOnairId(userSeq, meetingSeq));
        meetingOnair.setIsHost(isHost);
        return meetingOnairRepository.save(meetingOnair);
    }

    @Override
    public MeetingOnair createOnair(Integer userSeq, Integer meetingSeq) {
        MeetingOnair meetingOnair = new MeetingOnair();
        meetingOnair.setMeetingOnairId(new MeetingOnairId(userSeq, meetingSeq));
        return meetingOnairRepository.save(meetingOnair);
    }

    @Override
    public boolean existsOnair(Integer userSeq, Integer meetingSeq) {
        return meetingOnairRepository.existsMeetingOnairByMeetingOnairId(new MeetingOnairId(userSeq, meetingSeq));
    }
}
