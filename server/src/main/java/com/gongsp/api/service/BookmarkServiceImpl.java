package com.gongsp.api.service;

import com.gongsp.db.entity.Meeting;
import com.gongsp.db.repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("bookmarkService")
public class BookmarkServiceImpl implements BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Override
    public List<Meeting> findAllByUserSeq(Integer userSeq) {
        return bookmarkRepository.findAllByUser(userSeq);
    }
}
