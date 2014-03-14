package com.easyinsight.analysis;

import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import nu.xom.Element;
import org.hibernate.Session;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * User: jamesboe
 * Date: 9/26/11
 * Time: 9:52 AM
 */
@Entity
@Table(name="report_field_extension")
@Inheritance(strategy= InheritanceType.JOINED)
public class ReportFieldExtension implements Cloneable, Serializable {

    public static final int TEXT = 1;
    public static final int YTD = 2;
    public static final int VERTICAL_LIST = 3;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="report_field_extension_id")
    private long reportFieldExtensionID;

    @Transient
    private long fromFieldRuleID;

    public long getFromFieldRuleID() {
        return fromFieldRuleID;
    }

    public void setFromFieldRuleID(long fromFieldRuleID) {
        this.fromFieldRuleID = fromFieldRuleID;
    }

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

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        return new ArrayList<AnalysisItem>();
    }

    public void reportSave(Session session) {
    }

    public void afterLoad() {
    }

    public void updateIDs(ReplacementMap replacementMap) {

    }

    public Element toXML(XMLMetadata xmlMetadata) {
        return null;
    }

    public static ReportFieldExtension fromXML(Element extensionElement, XMLImportMetadata xmlImportMetadata) {
        String extensionType = extensionElement.getAttribute("extensionType").getValue();
        ReportFieldExtension reportFieldExtension;
        if ("text".equals(extensionType)) {
            reportFieldExtension = new TextReportFieldExtension();
        } else if ("diagram".equals(extensionType)) {
            reportFieldExtension = new DiagramReportFieldExtension();
        } else if ("trend".equals(extensionType)) {
            reportFieldExtension = new TrendReportFieldExtension();
        } else {
            throw new RuntimeException();
        }
        reportFieldExtension.subclassFromXML(extensionElement, xmlImportMetadata);
        return reportFieldExtension;
    }

    protected void subclassFromXML(Element extensionElement, XMLImportMetadata xmlImportMetadata) {

    }

    public int extensionType() {
        return 0;
    }

    public void validate(Set<Long> sourceIDs) {

    }
}
