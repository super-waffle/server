package com.gongsp.common.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidUtil {

    public String createRandomUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        return uuid;
    }
}
