package com.gongsp.api.service;

import com.gongsp.db.entity.Bookmark;
import com.gongsp.db.entity.Meeting;
import com.gongsp.db.entity.User;
import com.gongsp.db.repository.BookmarkRepository;
import com.gongsp.db.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("bookmarkService")
public class BookmarkServiceImpl implements BookmarkService {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MeetingService meetingService;

    @Override
    public List<Meeting> findAllByUserSeq(Integer userSeq) {
        return meetingRepository.findAllByUser(userSeq);
    }

    @Override
    public Boolean addMeetingToBookmark(Integer userSeq, Integer meetingSeq) {
        // 등록
        try {
            Bookmark bookmark = new Bookmark();
            User user = userService.getUserByUserSeq(userSeq).orElse(null);
            Meeting meeting = meetingService.getMeeting(meetingSeq).orElse(null);
            bookmark.setUser(user);
            bookmark.setMeeting(meeting);
            bookmarkRepository.save(bookmark);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean deleteMeetingFromBookmark(Integer userSeq, Integer meetingSeq) {
        // 삭제
        Bookmark bookmark = bookmarkRepository.findBookmarkByUserAndMeeting(userSeq, meetingSeq);
        if (bookmark == null) {
            return false;
        }
        try {
            bookmarkRepository.delete(bookmark);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
