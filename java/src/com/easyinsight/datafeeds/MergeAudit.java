package com.easyinsight.datafeeds;

import com.easyinsight.dataset.DataSet;

import java.util.Arrays;
import java.util.List;

/**
 * User: jamesboe
 * Date: Jul 15, 2010
 * Time: 1:43:57 PM
 */
public class MergeAudit {
    private List<String> mergeStrings;
    private DataSet dataSet;

    public MergeAudit(String mergeString, DataSet dataSet) {
        this.mergeStrings = Arrays.asList(mergeString);
        this.dataSet = dataSet;
    }

    public MergeAudit(List<String> mergeStrings, DataSet dataSet) {
        this.mergeStrings = mergeStrings;
        this.dataSet = dataSet;
    }

    public List<String> getMergeStrings() {
        return mergeStrings;
    }

    public void setMergeStrings(List<String> mergeStrings) {
        this.mergeStrings = mergeStrings;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }
}
