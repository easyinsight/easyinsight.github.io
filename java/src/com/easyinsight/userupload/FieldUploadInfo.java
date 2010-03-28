package com.easyinsight.userupload;

import com.easyinsight.analysis.AnalysisItem;

import java.util.List;

/**
 * User: jamesboe
 * Date: Mar 27, 2010
 * Time: 11:58:38 AM
 */
public class FieldUploadInfo {
    private AnalysisItem guessedItem;
    private List<String> sampleValues;

    public AnalysisItem getGuessedItem() {
        return guessedItem;
    }

    public void setGuessedItem(AnalysisItem guessedItem) {
        this.guessedItem = guessedItem;
    }

    public List<String> getSampleValues() {
        return sampleValues;
    }

    public void setSampleValues(List<String> sampleValues) {
        this.sampleValues = sampleValues;
    }
}
