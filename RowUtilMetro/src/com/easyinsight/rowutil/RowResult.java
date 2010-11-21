package com.easyinsight.rowutil;

import com.easyinsight.rowutil.transactional.DatePair;
import com.easyinsight.rowutil.transactional.NumberPair;
import com.easyinsight.rowutil.transactional.StringPair;

import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 10, 2010
 * Time: 9:31:32 AM
 */
public class RowResult {
    private List<StringPair> stringPairs;
    private List<NumberPair> numberPairs;
    private List<DatePair> datePairs;
    private String failureMessage;

    public RowResult(List<StringPair> stringPairs, List<NumberPair> numberPairs, List<DatePair> datePairs, String failureMessage) {
        this.stringPairs = stringPairs;
        this.numberPairs = numberPairs;
        this.datePairs = datePairs;
        this.failureMessage = failureMessage;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public List<StringPair> getStringPairs() {
        return stringPairs;
    }

    public List<NumberPair> getNumberPairs() {
        return numberPairs;
    }

    public List<DatePair> getDatePairs() {
        return datePairs;
    }

    @Override
    public String toString() {
        return "RowResult{" +
                "stringPairs=" + stringPairs +
                ", numberPairs=" + numberPairs +
                ", datePairs=" + datePairs +
                ", failureMessage='" + failureMessage + '\'' +
                '}';
    }
}
