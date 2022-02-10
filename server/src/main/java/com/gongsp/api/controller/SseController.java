package com.gongsp.api.controller;

import com.gongsp.api.service.NoticeService;
import com.gongsp.api.service.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
public class SseController {

    @Autowired
    SseService sseService;

    @GetMapping(value = "/sub/{user-seq}", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(@PathVariable("user-seq") Integer userSeq) {
        //현재 클라이언트를 위한 SseEmitter 생성
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        sseService.register(sseEmitter, userSeq);
        System.out.println("sseEmitter 생성");
        try {
            sseEmitter.send(SseEmitter.event().name("connect").data("data 들어갑니까?"));
        } catch (Exception e){
            e.printStackTrace();
        }

        return sseEmitter;
    }
}
