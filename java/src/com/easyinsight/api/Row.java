package com.easyinsight.api;

/**
 * User: James Boe
 * Date: Jan 15, 2009
 * Time: 1:27:39 PM
 */
public class Row {
    private StringPair[] stringPairs;
    private NumberPair[] numberPairs;
    private DatePair[] datePairs;

    public StringPair[] getStringPairs() {
        return stringPairs;
    }

    public void setStringPairs(StringPair[] stringPairs) {
        this.stringPairs = stringPairs;
    }

    public NumberPair[] getNumberPairs() {
        return numberPairs;
    }

    public void setNumberPairs(NumberPair[] numberPairs) {
        this.numberPairs = numberPairs;
    }

    public DatePair[] getDatePairs() {
        return datePairs;
    }

    public void setDatePairs(DatePair[] datePairs) {
        this.datePairs = datePairs;
    }
}
