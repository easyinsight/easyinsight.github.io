package com.easyinsight.core;

import com.easyinsight.analysis.AnalysisDefinition;
import org.hibernate.Session;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: James Boe
 * Date: Jun 12, 2008
 * Time: 10:16:45 AM
 */
@Entity
@Table(name="item_key")
@Inheritance(strategy= InheritanceType.JOINED)
public class Key implements Comparable<Key>, Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="item_key_id")
    private long keyID;

    public String toSQL() { throw new UnsupportedOperationException(); }

    public Key toBaseKey() { return this; }

    public boolean isPrimaryKey() {
        return false;
    }

    public void beforeSave(Session session) {

    }

    public void updateIDs(Map<Long, AnalysisDefinition> reportReplacementMap) {

    }

    public void afterLoad() {

    }

    public void setPkName(String pkName) {

    }

    public boolean hasDataSource(long dataSourceID) {
        return false;
    }

    public boolean hasReport(long reportID) {
        return false;
    }
    
    public List<EIDescriptor> getDescriptors() {
        List<EIDescriptor> descriptors = new ArrayList<EIDescriptor>();
        descriptors.add(new KeyDescriptor(this));
        return descriptors;
    }

    public long getKeyID() {
        return keyID;
    }

    public void setKeyID(long keyID) {
        this.keyID = keyID;
    }

    public String toDisplayName() { return ""; }

    public boolean indexed() { return false; }

    public String toKeyString() {
        return "";
    }
    
    public boolean matchesOrContains(Key key) { return false; }

    public String internalString() { return ""; }

    public int compareTo(Key o) {
        return toDisplayName().compareTo(o.toDisplayName());
    }
    
    public Key clone() throws CloneNotSupportedException {
        Key key = (Key) super.clone();
        key.keyID = 0;
        return key;
    }

    public String urlKeyString(XMLMetadata xmlMetadata) {
        return null;
    }
}
