package com.gongsp.api.request.diary;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class DiaryCreatePostReq {
    private DiaryDateInfo dateInfo;
    private DiaryContentInfo contentInfo;
    private MultipartFile image;
}
