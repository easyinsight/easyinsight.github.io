package com.easyinsight.analysis;

import com.easyinsight.dashboard.Dashboard;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Aug 28, 2009
 * Time: 10:04:26 AM
 */
@Entity
@Table(name="link")
@Inheritance(strategy=InheritanceType.JOINED)
public class Link implements Cloneable, Serializable {

    public static final int URL = 1;
    public static final int DRILLTHROUGH = 2;

    @Column(name="label")
    private String label;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="link_id")
    private long linkID;

    @Column(name="code_generated")
    private boolean codeGenerated;

    @Column(name="default_link")
    private boolean defaultLink;

    public boolean isDefaultLink() {
        return defaultLink;
    }

    public void setDefaultLink(boolean defaultLink) {
        this.defaultLink = defaultLink;
    }

    @Override
    public Link clone() throws CloneNotSupportedException {
        Link link = (Link) super.clone();
        link.setLinkID(0);
        return link;
    }

    public boolean isCodeGenerated() {
        return codeGenerated;
    }

    public void setCodeGenerated(boolean codeGenerated) {
        this.codeGenerated = codeGenerated;
    }

    public long getLinkID() {
        return linkID;
    }

    public void setLinkID(long linkID) {
        this.linkID = linkID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<AnalysisItem> neededKeys(Map<String, List<AnalysisItem>> keyItems, Map<String, List<AnalysisItem>> displayItems) {
        return new ArrayList<AnalysisItem>();
    }

    public boolean generatesURL() {
        return false;
    }

    public void beforeSave() {

    }

    public String generateLink(IRow row, Map<String, String> dataSourceProperties, Collection<AnalysisItem> fields) {
        throw new UnsupportedOperationException();
    }

    public void updateReportIDs(Map<Long, AnalysisDefinition> replacementMap, Map<Long, Dashboard> dashboardReplacementMap) {
        
    }

    public String toXML() {
        return null;
    }
}
