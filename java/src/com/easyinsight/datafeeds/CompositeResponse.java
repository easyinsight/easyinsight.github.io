package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;

import java.util.List;

/**
 * User: jamesboe
 * Date: 3/2/11
 * Time: 3:33 PM
 */
public class CompositeResponse {
    private List<AnalysisItem> firstFields;
    private List<AnalysisItem> secondFields;
    private String firstName;
    private String secondName;
    private long firstID;
    private long secondID;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public long getFirstID() {
        return firstID;
    }

    public void setFirstID(long firstID) {
        this.firstID = firstID;
    }

    public long getSecondID() {
        return secondID;
    }

    public void setSecondID(long secondID) {
        this.secondID = secondID;
    }

    public List<AnalysisItem> getFirstFields() {
        return firstFields;
    }

    public void setFirstFields(List<AnalysisItem> firstFields) {
        this.firstFields = firstFields;
    }

    public List<AnalysisItem> getSecondFields() {
        return secondFields;
    }

    public void setSecondFields(List<AnalysisItem> secondFields) {
        this.secondFields = secondFields;
    }
}
