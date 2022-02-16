package com.gongsp.api.request.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserInfoPatchReq {
    private Integer timeGoal;
    private String profileMessage;
    private MultipartFile profileImage;
}
