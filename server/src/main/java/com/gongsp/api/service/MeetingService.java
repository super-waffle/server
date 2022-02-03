package com.gongsp.api.service;

import com.gongsp.db.entity.Meeting;
import com.gongsp.db.entity.MeetingOnair;
import io.openvidu.java.client.OpenVidu;

import java.util.Optional;

public interface MeetingService {
    //미팅룸 입실(session 연결)
    String getToken(OpenVidu openVidu, Integer userSeq, Meeting meeting);
    //미팅룸 퇴실(session 연결종료)
    String removeUser(String sessionName, String token);

    Optional<Meeting> getMeeting(Integer meetingSeq);
    void updateMeeting(Integer meetingSeq, int flag);
    void deleteOnair(Integer userSeq, Integer meetingSeq);
    MeetingOnair createOnair(Integer userSeq, Integer meetingSeq, Boolean isHost);
    MeetingOnair createOnair(Integer userSeq, Integer meetingSeq);
    boolean existsOnair(Integer userSeq, Integer meetingSeq);
}
