package com.gongsp.api.service;

import com.gongsp.api.request.report.ReportSendPostReq;
import com.gongsp.db.entity.Report;
import com.gongsp.db.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("reportService")
public class ReportServiceImpl implements ReportService{

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public Boolean createReport(Integer userSeq, ReportSendPostReq reportInfo) {
        if (reportInfo.getUserSeqTo() == null || reportInfo.getContent() == null || userSeq.equals(reportInfo.getUserSeqTo())) {
            // 피신고인 누락, 신고 사유 누락, 피신고인==신고인인 경우 신고 X
            return false;
        }
        Report report = new Report();
        report.setUserSeqFrom(userSeq);
        report.setUserSeqTo(reportInfo.getUserSeqTo());
        report.setReportContent(reportInfo.getContent());
        reportRepository.save(report);
        return true;
    }
}
