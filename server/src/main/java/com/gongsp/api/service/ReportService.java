package com.gongsp.api.service;

import com.gongsp.api.request.report.ReportSendPostReq;

public interface ReportService {
    Boolean createReport(Integer userSeq, ReportSendPostReq reportInfo);
}
