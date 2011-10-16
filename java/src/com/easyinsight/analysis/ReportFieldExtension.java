package com.easyinsight.analysis;

import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 9/26/11
 * Time: 9:52 AM
 */
@Entity
@Table(name="report_field_extension")
@Inheritance(strategy= InheritanceType.JOINED)
public class ReportFieldExtension implements Cloneable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="report_field_extension_id")
    private long reportFieldExtensionID;

    public long getReportFieldExtensionID() {
        return reportFieldExtensionID;
    }

    public void setReportFieldExtensionID(long reportFieldExtensionID) {
        this.reportFieldExtensionID = reportFieldExtensionID;
    }

    @Override
    public ReportFieldExtension clone() throws CloneNotSupportedException {
        ReportFieldExtension reportFieldExtension = (ReportFieldExtension) super.clone();
        reportFieldExtension.setReportFieldExtensionID(0);
        return reportFieldExtension;
    }

    public List<AnalysisItem> getAnalysisItems(boolean getEverything) {
        return new ArrayList<AnalysisItem>();
    }

    public void reportSave(Session session) {
    }

    public void afterLoad() {
    }

    public void updateIDs(ReplacementMap replacementMap) {

    }
}
