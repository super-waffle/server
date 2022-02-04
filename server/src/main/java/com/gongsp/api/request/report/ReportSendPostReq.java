package com.gongsp.api.request.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportSendPostReq {
    private Integer userSeqTo;
    private String content;
}
