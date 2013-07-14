package com.easyinsight.core;

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.database.Database;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 2/5/13
 * Time: 8:43 AM
 */
@Entity
@Table(name="report_key")
@PrimaryKeyJoinColumn(name="item_key_id")
public class ReportKey extends Key {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_item_key_id")
    private Key parentKey;
    @Column(name="report_id")
    private long reportID;

    @Override
    public void beforeSave(Session session) {
        super.beforeSave(session);
        if (parentKey.getKeyID() == 0) {
            session.save(parentKey);
        } else {
            session.merge(parentKey);
        }
    }

    public void updateIDs(Map<Long, AnalysisDefinition> reportReplacementMap) {
        if (reportReplacementMap.containsKey(reportID)) {
            reportID = reportReplacementMap.get(reportID).getAnalysisID();
        }
    }

    public void afterLoad() {
        super.afterLoad();
        parentKey = (Key) Database.deproxy(parentKey);
        parentKey.afterLoad();
    }

    @Override
    public List<EIDescriptor> getDescriptors() {
        List<EIDescriptor> descs = super.getDescriptors();
        descs.addAll(parentKey.getDescriptors());
        return descs;
    }

    public Key getParentKey() {
        return parentKey;
    }

    public void setParentKey(Key parentKey) {
        this.parentKey = parentKey;
    }

    public long getReportID() {
        return reportID;
    }

    public void setReportID(long reportID) {
        this.reportID = reportID;
    }

    public boolean hasReport(long reportID) {
        return this.reportID == reportID;
    }

    @Override
    public boolean matchesOrContains(Key key) {
        return false;
    }

    @Override
    public String toSQL() {
        return toBaseKey().toSQL();
    }

    @Override
    public Key toBaseKey() {
        return getParentKey().toBaseKey();
    }

    public String toDisplayName() {
        return toBaseKey().toDisplayName();
    }

    @Override
    public boolean indexed() {
        return toBaseKey().indexed();
    }

    public String toKeyString() {
        return toBaseKey().toKeyString();
    }

    @Override
    public String internalString() {
        return reportID + "-" + parentKey.internalString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportKey reportKey = (ReportKey) o;

        if (reportID != reportKey.reportID) return false;
        if (!parentKey.equals(reportKey.parentKey)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = parentKey.hashCode();
        result = 31 * result + (int) (reportID ^ (reportID >>> 32));
        return result;
    }

    public String urlKeyString(XMLMetadata xmlMetadata) {
        return reportID + "-" + parentKey.urlKeyString(xmlMetadata);
    }
}
