package com.gongsp.api.service;

import com.gongsp.api.request.study.StudyCreatePostReq;
import com.gongsp.api.request.study.StudyParameter;
import com.gongsp.api.response.study.StudyRes;
import com.gongsp.db.entity.Category;
import com.gongsp.db.entity.Meeting;
import com.gongsp.db.entity.StudyRoom;
import com.gongsp.db.entity.User;
import com.gongsp.db.repository.StudyMemberRepository;
import com.gongsp.db.repository.StudyRoomRepository;
import io.openvidu.java.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service("studyService")
public class StudyRoomServiceImpl implements StudyRoomService {

    @Autowired
    StudyRoomRepository studyRoomRepository;
    @Autowired
    StudyMemberRepository studyMemberRepository;

    // Collection to pair session names and OpenVidu Session objects
    private Map<String, Session> mapSessions = new ConcurrentHashMap<>();
    // Collection to pair session names and tokens (the inner Map pairs tokens and role associated)
    private Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens = new ConcurrentHashMap<>();


    @Override
    public Optional<StudyRoom> getStudyRoom(Integer studySeq) {
        return studyRoomRepository.findStudyByStudySeq(studySeq);
    }

    @Override
    public String getToken(OpenVidu openVidu, Integer userSeq, StudyRoom studyRoom) {

        // sessionName = studyUrl
        String sessionName = studyRoom.getStudyUrl();
        OpenViduRole role = OpenViduRole.PUBLISHER;
        String serverData = "{\"serverData\": \"" + userSeq + "\"}";

        // Build connectionProperties object with the serverData and the role
        ConnectionProperties connectionProperties = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC).data(serverData).role(role).build();

        if (this.mapSessions.get(sessionName) != null) {
            // Session already exists
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
    public List<StudyRes> getStudyList(StudyParameter studyParameter, int userSeq) {
        List<StudyRoom> studyList = new ArrayList<>();
        List<StudyRes> studyResList = new ArrayList<>();

        int start = studyParameter.getPage() == 0 ? 0 : (studyParameter.getPage() - 1) * studyParameter.getSpp();

        //카테고리 선택안한경우
        if (studyParameter.getType() == 0) {
            //검색어 없음 = 전체목록
            if (studyParameter.getKey() == null || studyParameter.getKey().equals("")) {
//                System.out.println("카테고리X 검색어X");
                studyList = studyRoomRepository.searchAll(start, studyParameter.getSpp());
            } else {
                //검색어 있음 - 필터링(글제목, 글내용)
//                System.out.println("카테고리X 검색어O");
                studyList = studyRoomRepository.searchByKey(studyParameter.getKey(), start, studyParameter.getSpp());
            }
        } else {    //카테고리 선택한경우
            //검색어 없음 = 선택한 카테고리 모두
            if (studyParameter.getKey() == null || studyParameter.getKey().equals("")) {
//                System.out.println("카테고리O 검색어X");
                studyList = studyRoomRepository.searchByCategorySeq(studyParameter.getType(), start, studyParameter.getSpp());
            } else {
                //검색어 있음 - 필터링(글제목, 글내용)
//                System.out.println("카테고리O 검색어O");
                studyList = studyRoomRepository.searchByKeyAndCategory(studyParameter.getKey(), studyParameter.getType(), start, studyParameter.getSpp());
            }
        }

        for (StudyRoom study : studyList) {
            StudyRes studyRes = new StudyRes();
            studyRes.setStudySeq(study.getStudySeq());
            studyRes.setHostNickname(study.getHost().getUserNickname());
            studyRes.setCategoryName(study.getCategory().getCategoryName());
            studyRes.setStudyTitle(study.getStudyTitle());
            studyRes.setStudyShortDesc(study.getStudyShortDesc());
            studyRes.setStudyHeadcount(getStudyMemberNum(study.getStudySeq()));
            studyRes.setStudyRecruitEnd(study.getStudyRecruitEnd());
            studyResList.add(studyRes);
        }
        return studyResList;
    }

    @Override
    public int getStudyMemberNum(Integer studySeq) {
        return studyMemberRepository.countStudyMemberByStudySeq(studySeq);
    }

    @Override
    public int getStudyCnt(StudyParameter studyParameter) {
        //카테고리 선택안한경우
        if (studyParameter.getType() == 0) {
            //검색어 없음 = 전체목록
            if (studyParameter.getKey() == null || studyParameter.getKey().equals("")) {
//                System.out.println("카테고리X 검색어X");
                return (int) studyRoomRepository.count();
            } else {
                //검색어 있음 - 필터링(글제목, 글내용)
                return studyRoomRepository.countByLike(studyParameter.getKey());
            }
        } else {    //카테고리 선택한경우
            //검색어 없음 = 선택한 카테고리 모두
            if (studyParameter.getKey() == null || studyParameter.getKey().equals("")) {
                return studyRoomRepository.countByCategory(studyParameter.getType());
            } else {
                //검색어 있음 - 필터링(글제목, 글내용)
                return studyRoomRepository.countByLikeAndCategory(studyParameter.getType(), studyParameter.getKey());
            }
        }
    }

    @Override
    public Optional<StudyRoom> getStudyDetail(Integer studySeq) {
        return studyRoomRepository.findStudyByStudySeq(studySeq);
    }

    @Override
    public StudyRoom createStudy(StudyCreatePostReq studyCreatePostReq, Integer userSeq) {
        StudyRoom studyRoom = new StudyRoom();
        studyRoom.setHost(new User(userSeq));
        studyRoom.setCategory(new Category(studyCreatePostReq.getCategorySeq()));
        if(studyCreatePostReq.getStudyTitle().length()>50)
            studyRoom.setStudyTitle(studyCreatePostReq.getStudyTitle().substring(50));
        else
            studyRoom.setStudyTitle(studyCreatePostReq.getStudyTitle());
        if(studyCreatePostReq.getStudyShortDesc().length()>50)
            studyRoom.setStudyShortDesc(studyCreatePostReq.getStudyShortDesc().substring(50));
        else
            studyRoom.setStudyShortDesc(studyCreatePostReq.getStudyShortDesc());
        studyRoom.setStudyDesc(studyCreatePostReq.getStudyDesc());
        studyRoom.setStudyCapacity(6);
        studyRoom.setStudyUrl(studyCreatePostReq.getStudyTitle() + userSeq);
        studyRoom.setStudyLate(10);
        studyRoom.setStudyDateStart(null);
        studyRoom.setStudyDateEnd(null);
        studyRoom.setStudyRecruitStart(LocalDate.now());
        studyRoom.setStudyRecruitEnd(studyCreatePostReq.getStudyRecruitEnd());
        return studyRoomRepository.save(studyRoom);
    }

    @Override
    public String getStudyUrl(Integer studySeq) {
        Optional<StudyRoom> opStudyRoom = studyRoomRepository.findStudyByStudySeq(studySeq);
        if (!opStudyRoom.isPresent()) {
            return null;
        }
        return opStudyRoom.get().getStudyUrl();
    }

    @Override
    public void updateStudyOnair(Integer studySeq, int onairCnt) {
        Optional<StudyRoom> opStudyRoom = studyRoomRepository.findStudyByStudySeq(studySeq);
        if (!opStudyRoom.isPresent())
            return;
        StudyRoom studyRoom = opStudyRoom.get();
        if (onairCnt == 0)
            studyRoom.setIsStudyOnair(false);
        else
            studyRoom.setIsStudyOnair(true);
        studyRoomRepository.save(studyRoom);
    }

    @Override
    public String removeUser(String sessionName, String token, Integer studySeq) {
        // If the session exists
        if (this.mapSessions.get(sessionName) != null && this.mapSessionNamesTokens.get(sessionName) != null) {
            // If the token exists
            if (this.mapSessionNamesTokens.get(sessionName).remove(token) != null) {
                // User left the session
                System.out.println("세션 종료여부: " + this.mapSessionNamesTokens.get(sessionName).isEmpty());
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
