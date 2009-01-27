package com.easyinsight.analysis;

import com.easyinsight.AnalysisItem;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Apr 15, 2008
 * Time: 8:56:13 PM
 */
@Entity
@Table(name="list_analysis_field")
public class ListField extends AnalysisField {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="analysis_field_id")
    private long listFieldID;

    public ListField(AnalysisItem analysisItem) {
        super(analysisItem);
    }

    public ListField() {
    }

    public long getListFieldID() {
        return listFieldID;
    }

    public void setListFieldID(long listFieldID) {
        this.listFieldID = listFieldID;
    }
}
