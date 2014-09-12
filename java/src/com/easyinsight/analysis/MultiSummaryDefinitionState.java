package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSForm;
import com.easyinsight.analysis.definitions.WSMultiSummaryDefinition;
import com.easyinsight.core.InsightDescriptor;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Oct 28, 2010
 * Time: 10:44:03 PM
 */
@Entity
@Table(name="multi_summary")
public class MultiSummaryDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="multi_summary_id")
    private long multiSummaryID;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "multi_summary_to_report",
            joinColumns = @JoinColumn(name = "source_report_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "report_stub_id", nullable = false))
    private List<ReportStub> summaryReports = new ArrayList<>();

    public List<ReportStub> getSummaryReports() {
        return summaryReports;
    }

    public void setSummaryReports(List<ReportStub> summaryReports) {
        this.summaryReports = summaryReports;
    }

    @Override
    public WSAnalysisDefinition createWSDefinition() {
        WSMultiSummaryDefinition multiSummaryDefinition = new WSMultiSummaryDefinition();
        multiSummaryDefinition.setMultiSummaryID(multiSummaryID);
        List<InsightDescriptor> ds = new ArrayList<>();
        for (ReportStub reportStub : summaryReports) {
            InsightDescriptor insightDescriptor = new InsightDescriptor();
            insightDescriptor.setId(reportStub.getReportID());
            ds.add(insightDescriptor);
        }
        multiSummaryDefinition.setReports(ds);
        return multiSummaryDefinition;
    }

    public long getMultiSummaryID() {
        return multiSummaryID;
    }

    public void setMultiSummaryID(long multiSummaryID) {
        this.multiSummaryID = multiSummaryID;
    }

    @Override
    public AnalysisDefinitionState clone(List<AnalysisItem> allFields) throws CloneNotSupportedException {
        MultiSummaryDefinitionState formDefinitionState = (MultiSummaryDefinitionState) super.clone(allFields);
        formDefinitionState.setMultiSummaryID(0);
        formDefinitionState.setSummaryReports(new ArrayList<>(summaryReports));
        return formDefinitionState;
    }

    public void updateReportIDs(Map<Long, AnalysisDefinition> reportReplacementMap) {
        List<ReportStub> newReports = new ArrayList<>();
        for (ReportStub reportStub : summaryReports) {
            AnalysisDefinition report = reportReplacementMap.get(reportStub.getReportID());
            ReportStub newStub = new ReportStub();
            newStub.setReportID(report.getAnalysisID());
            newReports.add(newStub);
        }
        setSummaryReports(newReports);
    }

    @Override
    public Collection<? extends AnalysisDefinition> containedReports(Session session) {
        List<AnalysisDefinition> reports = new ArrayList<>();
        for (ReportStub reportStub : summaryReports) {
            reports.add((AnalysisDefinition) session.createQuery("from AnalysisDefinition where analysisID = ?").setLong(0, reportStub.getReportID()).list().get(0));
        }
        return reports;
    }
}
