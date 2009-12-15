package com.easyinsight.analysis;

import com.easyinsight.core.Key;
import com.easyinsight.calculations.Resolver;

import javax.persistence.*;
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
public class Link implements Cloneable {
    public static final int URL = 1;
    public static final int DRILLTHROUGH = 2;

    @Column(name="label")
    private String label;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="link_id")
    private long linkID;

    @Override
    public Link clone() throws CloneNotSupportedException {
        Link link = (Link) super.clone();
        link.setLinkID(0);
        return link;
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

    public List<Key> neededKeys(Resolver resolver) {
        return new ArrayList<Key>();
    }

    public boolean generatesURL() {
        return false;
    }

    public String generateLink(IRow row, Map<String, String> dataSourceProperties) {
        throw new UnsupportedOperationException();
    }

    public void updateReportIDs(Map<Long, AnalysisDefinition> replacementMap) {
        
    }
}
