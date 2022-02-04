package com.gongsp.api.controller;

import com.gongsp.api.request.report.ReportSendPostReq;
import com.gongsp.api.service.ReportService;
import com.gongsp.common.model.response.BaseResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping()
    public ResponseEntity<? extends BaseResponseBody> sendReport(Authentication authentication, @RequestBody ReportSendPostReq reportInfo) {
        if (authentication == null) {
            return ResponseEntity.ok(BaseResponseBody.of(403, "Access Denied"));
        }
        Boolean reportCreated = reportService.createReport(Integer.parseInt((String) authentication.getPrincipal()), reportInfo);
        if (reportCreated) {
            return ResponseEntity.ok(BaseResponseBody.of(201, "Report Sent"));
        }
        return ResponseEntity.ok(BaseResponseBody.of(400, "Failed to send report"));
    }
}
