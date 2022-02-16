package com.gongsp.api.request.diary;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class DiaryUpdatePatchReq {
    private DiaryContentInfo contentInfo;
    @Nullable
    private MultipartFile image;
}
