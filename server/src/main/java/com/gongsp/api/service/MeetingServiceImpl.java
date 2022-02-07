package com.gongsp.api.service;

import com.gongsp.api.request.meeting.MeetingCreatePostReq;
import com.gongsp.api.request.meeting.MeetingParameter;
import com.gongsp.api.response.meeting.MeetingDetailGetRes;
import com.gongsp.api.response.meeting.MeetingRes;
import com.gongsp.db.entity.Category;
import com.gongsp.db.entity.Meeting;
import com.gongsp.db.entity.MeetingDetail;
import com.gongsp.db.repository.MeetingDetailRepository;
import com.gongsp.db.repository.MeetingRepository;
import io.openvidu.java.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service("meetingService")
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private MeetingDetailRepository meetingDetailRepository;

    // Collection to pair session names and OpenVidu Session objects
    // ConcurrentHashMap : Multi-Thread 환경에서 사용할 수 있도록 나온 클래스
    private Map<String, Session> mapSessions = new ConcurrentHashMap<>();

    // Collection to pair session names and tokens (the inner Map pairs tokens and role associated)
    private Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens = new ConcurrentHashMap<>();

    @Override
    public String getToken(OpenVidu openVidu, Integer userSeq, Meeting meeting) {
//        System.out.println("Getting a token from OpenVidu Server | {Meetingroom name}=" + meeting.getMeetingTitle());

        // sessionName = meetingUrl
        String sessionName = meeting.getMeetingUrl();
//        System.out.println(sessionName);
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
        if (meeting.getMeetingHeadcount() == 0) meeting.setIsMeetingOnair(true);
//        System.out.println("현재인원 : " + meeting.getMeetingHeadcount());
        meeting.setMeetingHeadcount(meeting.getMeetingHeadcount() + flag);
//        System.out.println("현재인원 : " + meeting.getMeetingHeadcount());
        meetingRepository.save(meeting);
    }

    @Override
    public List<MeetingRes> getMeetingList(MeetingParameter meetingParameter, Integer userSeq) {
        List<Meeting> meetingList = new ArrayList<>();
        List<MeetingRes> meetingResList = new ArrayList<>();

        int start = meetingParameter.getPage() == 0 ? 0 : (meetingParameter.getPage() - 1) * meetingParameter.getSpp();
        //정렬기준 정해야됨
//        Pageable pageRequest = (Pageable) PageRequest.of(start, 10, Sort.by("meeting_seq").descending());

//        System.out.println("페이지: " + meetingParameter.getPage() + "카테고리: " + meetingParameter.getType() + "검색어 : " + meetingParameter.getKey());

        //카테고리 선택안한경우
        if (meetingParameter.getType() == 0) {
            //검색어 없음 = 전체목록
            if (meetingParameter.getKey() == null || meetingParameter.getKey().equals("")) {
//                System.out.println("카테고리X 검색어X");
                meetingList = meetingRepository.searchAll(start, 10, userSeq);
            } else {
                //검색어 있음 - 필터링(글제목, 글내용)
//                Optional<List<Meeting>> optionalMeetings = meetingRepository.findByMeetingTitleContainingOrMeetingDescContaining(meetingParameter.getKey(), meetingParameter.getKey(), pageRequest);
//                if (optionalMeetings.isPresent())
//                    meetingList = optionalMeetings.get();
//                System.out.println("카테고리X 검색어O");
                meetingList = meetingRepository.searchByKey(meetingParameter.getKey(), start, 10, userSeq);
            }
        } else {    //카테고리 선택한경우
            //검색어 없음 = 선택한 카테고리 모두
            if (meetingParameter.getKey() == null || meetingParameter.getKey().equals("")) {
//                System.out.println("카테고리O 검색어X");
                meetingList = meetingRepository.searchByCategorySeq(meetingParameter.getType(), start, 10, userSeq);
            } else {
                //검색어 있음 - 필터링(글제목, 글내용)
//                Optional<List<Meeting>> optionalMeetings = meetingRepository.findByMeetingTitleContainingOrMeetingDescContaining(meetingParameter.getKey(), meetingParameter.getKey(), pageRequest);
//                if (optionalMeetings.isPresent())
//                    meetingList = optionalMeetings.get();
//                System.out.println("카테고리O 검색어O");
                meetingList = meetingRepository.searchByKeyAndCategory(meetingParameter.getKey(), meetingParameter.getType(), start, 10, userSeq);
            }
        }

        for (Meeting meeting : meetingList) {
            MeetingRes meetingRes = new MeetingRes();
            meetingRes.setMeetingImg(meeting.getMeetingImg());
            meetingRes.setMeetingOnair(meeting.getIsMeetingOnair());
            meetingRes.setMeetingSeq(meeting.getMeetingSeq());
            meetingRes.setMeetingHeadcount(meeting.getMeetingHeadcount());
            meetingRes.setMeetingTitle(meeting.getMeetingTitle());
            meetingRes.setCategoryName(meeting.getCategory().getCategoryName());
            meetingResList.add(meetingRes);
        }
        return meetingResList;
    }

    @Override
    public Meeting createMeeting(MeetingCreatePostReq meetingCreatePostReq, Integer userSeq, String uuidFilename) {
        Meeting meeting = new Meeting();
        meeting.setHostSeq(userSeq);
        meeting.setCategory(new Category(meetingCreatePostReq.getCategorySeq()));
        meeting.setMeetingTitle(meetingCreatePostReq.getMeetingTitle());
        meeting.setMeetingDesc(meetingCreatePostReq.getMeetingDesc());
        meeting.setMeetingUrl(meetingCreatePostReq.getMeetingTitle() + userSeq);
        meeting.setMeetingCamType(meetingCreatePostReq.getMeetingCamType());
        meeting.setMeetingMicType(meetingCreatePostReq.getMeetingMicType());
        meeting.setMeetingImg(uuidFilename);
        return meetingRepository.save(meeting);
    }

    @Override
    public MeetingDetailGetRes getMeetingDetail(Integer meetingSeq) {
        MeetingDetailGetRes meetingDetailGetRes = new MeetingDetailGetRes();
        Optional<MeetingDetail> opMeetingDetail = meetingDetailRepository.findMeetingByMeetingSeq(meetingSeq);
        if (!opMeetingDetail.isPresent())
            return null;
        MeetingDetail meetingDetail = opMeetingDetail.get();
        meetingDetailGetRes.setMeetingSeq(meetingSeq);
        meetingDetailGetRes.setHostName(meetingDetail.getUser().getUserNickname());
        meetingDetailGetRes.setCategoryName(meetingDetail.getCategory().getCategoryName());
        meetingDetailGetRes.setMeetingTitle(meetingDetail.getMeetingTitle());
        meetingDetailGetRes.setMeetingDesc(meetingDetail.getMeetingDesc());
        meetingDetailGetRes.setMeetingImg(meetingDetail.getMeetingImg());
        meetingDetailGetRes.setMeetingHeadcount(meetingDetail.getMeetingHeadcount());
        meetingDetailGetRes.setMeetingUrl(meetingDetail.getMeetingUrl());
        String type = "";
        switch(meetingDetail.getMeetingCamType()){
            case 1:
                type = "얼굴";
                break;
            case 2:
                type = "손";
                break;
            case 3:
                type = "노캠";
                break;
        }
        meetingDetailGetRes.setMeetingCamType(type);
        type = "";
        switch(meetingDetail.getMeetingMicType()){
            case 1:
                type = "음소거";
                break;
            case 2:
                type = "소음";
                break;
        }
        meetingDetailGetRes.setMeetingMicType(type);
        meetingDetailGetRes.setIsMeetingOnair(meetingDetail.getIsMeetingOnair());
        return meetingDetailGetRes;
    }

    @Override
    public boolean isUserOwnMeeting(Integer userSeq) {
        return meetingRepository.existsMeetingByHostSeq(userSeq);
    }

    @Override
    public Integer getHostSeq(Integer meetingSeq) {
        Optional<Meeting> opMeeting = meetingRepository.findMeetingByMeetingSeq(meetingSeq);
        if(!opMeeting.isPresent())
            return 0;
        return opMeeting.get().getHostSeq();
    }

    @Override
    public String getMeetingUrl(Integer meetingSeq) {
        Optional<Meeting> opMeeting = meetingRepository.findMeetingByMeetingSeq(meetingSeq);
        if(!opMeeting.isPresent())
            return null;
        return opMeeting.get().getMeetingUrl();
    }

    @Override
    public String removeUser(String sessionName, String token, Integer meetingSeq) {
        // If the session exists
        if (this.mapSessions.get(sessionName) != null && this.mapSessionNamesTokens.get(sessionName) != null) {
            // If the token exists
            if (this.mapSessionNamesTokens.get(sessionName).remove(token) != null) {
                // User left the session
                System.out.println("세션 종료여부: " + this.mapSessionNamesTokens.get(sessionName).isEmpty());
                if (this.mapSessionNamesTokens.get(sessionName).isEmpty()) {
                    // Last user left: session must be removed
                    this.mapSessions.remove(sessionName);
                    Optional<Meeting> opMeeting = getMeeting(meetingSeq);
                    if (opMeeting.isPresent()) {
                        Meeting meeting = opMeeting.get();
                        meeting.setMeetingHeadcount(0);
                        meeting.setIsMeetingOnair(false);
                        meetingRepository.save(meeting);
                    }
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
