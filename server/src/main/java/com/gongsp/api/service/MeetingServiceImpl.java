package com.gongsp.api.service;

import com.gongsp.db.entity.Meeting;
import com.gongsp.db.repository.MeetingRepository;
import io.openvidu.java.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service("meetingService")
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;


    // Collection to pair session names and OpenVidu Session objects
    // ConcurrentHashMap : Multi-Thread 환경에서 사용할 수 있도록 나온 클래스
    private Map<String, Session> mapSessions = new ConcurrentHashMap<>();

    // Collection to pair session names and tokens (the inner Map pairs tokens and role associated)
    private Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens = new ConcurrentHashMap<>();

    @Override
    public String getToken(OpenVidu openVidu, Integer userSeq, Meeting meeting) {
//        System.out.println("Getting a token from OpenVidu Server | {Meetingroom name}=" + meeting.getMeetingTitle());

        // sessionName = meetingSeq
        String sessionName = meeting.getMeetingSeq().toString();
        // 근데 아예 Subscriber로 설정하면 화면송출이 안되는듯?? 일단 예제따라서
//        System.out.println(userSeq + " " + meeting.getHostSeq() + userSeq.equals(meeting.getHostSeq()));

        OpenViduRole role = OpenViduRole.PUBLISHER;

//        OpenViduRole role = userSeq.equals(meeting.getHostSeq()) ? OpenViduRole.PUBLISHER : OpenViduRole.SUBSCRIBER;
//        System.out.println("역할:" + role);
        String serverData = "{\"serverData\": \"" + userSeq + "\"}";

        // Build connectionProperties object with the serverData and the role
        ConnectionProperties connectionProperties = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC).data(serverData).role(role).build();

        if (this.mapSessions.get(sessionName) != null) {
            // Session already exists
//            System.out.println("Existing session " + sessionName);
            try {
                // Generate a new Connection with the recently created connectionProperties
                String token = this.mapSessions.get(sessionName).createConnection(connectionProperties).getToken();
                // Update our collection storing the new token
                this.mapSessionNamesTokens.get(sessionName).put(token, role);
                return token;
            } catch (OpenViduJavaClientException e1) {
                // If internal error generate an error message and return it to client
                System.out.println(e1.getStackTrace());
                System.out.println("cause: " + e1.getCause());
                System.out.println("error: " + e1.getMessage());
                System.out.println("exception: " + e1.getClass());
                return "InternalError";
            } catch (OpenViduHttpException e2) {
                if (404 == e2.getStatus()) {
                    // Invalid sessionId (user left unexpectedly). Session object is not valid
                    // anymore. Clean collections and continue as new session
                    this.mapSessions.remove(sessionName);
                    this.mapSessionNamesTokens.remove(sessionName);
                }
                return "InternalError";
            }
        }

        // New session
        System.out.println("New session " + sessionName);
        try {
            // Create a new OpenVidu Session
            Session session = openVidu.createSession();
            // Generate a new Connection with the recently created connectionProperties
            String token = session.createConnection(connectionProperties).getToken();

            // Store the session and the token in our collections
            this.mapSessions.put(sessionName, session);
            this.mapSessionNamesTokens.put(sessionName, new ConcurrentHashMap<>());
            this.mapSessionNamesTokens.get(sessionName).put(token, role);

            return token;
        } catch (Exception e) {
            // If error generate an error message and return it to client
            return "GenError";
        }
    }

    @Override
    public Optional<Meeting> getMeeting(Integer meetingSeq) {
        return meetingRepository.findMeetingByMeetingSeq(meetingSeq);
    }


    @Override
    public void updateMeeting(Integer meetingSeq, int flag) {
        Optional<Meeting> opMeeting = getMeeting(meetingSeq);
        if (!opMeeting.isPresent()) return;
        Meeting meeting = opMeeting.get();
        if(meeting.getMeetingHeadcount() == 0)
            meeting.setIsMeetingOnair(true);
//        System.out.println("현재인원 : " + meeting.getMeetingHeadcount());
        meeting.setMeetingHeadcount(meeting.getMeetingHeadcount() + flag);
//        System.out.println("현재인원 : " + meeting.getMeetingHeadcount());
        meetingRepository.save(meeting);
    }

    @Override
    public String removeUser(String sessionName, String token) {
        // If the session exists
        if (this.mapSessions.get(sessionName) != null && this.mapSessionNamesTokens.get(sessionName) != null) {
            // If the token exists
            if (this.mapSessionNamesTokens.get(sessionName).remove(token) != null) {
                // User left the session
                if (this.mapSessionNamesTokens.get(sessionName).isEmpty()) {
                    // Last user left: session must be removed
                    this.mapSessions.remove(sessionName);
                }
                return "OK";
            } else {
                // The TOKEN wasn't valid
                System.out.println("Problems in the app server: the TOKEN wasn't valid");
                return "Error";
            }
        } else {
            // The SESSION does not exist
            System.out.println("Problems in the app server: the SESSION does not exist");
            return "Error";
        }
    }
}
