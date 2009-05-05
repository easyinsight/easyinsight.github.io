package com.easyinsight.api;

import org.jetbrains.annotations.Nullable;

/**
 * User: James Boe
 * Date: Jan 15, 2009
 * Time: 1:27:39 PM
 */
public class Row {
    private StringPair[] stringPairs;
    private NumberPair[] numberPairs;
    private DatePair[] datePairs;

    @Nullable
    public StringPair[] getStringPairs() {
        return stringPairs;
    }

    public void setStringPairs(StringPair[] stringPairs) {
        this.stringPairs = stringPairs;
    }

    @Nullable
    public NumberPair[] getNumberPairs() {
        return numberPairs;
    }

    public void setNumberPairs(NumberPair[] numberPairs) {
        this.numberPairs = numberPairs;
    }

    @Nullable
    public DatePair[] getDatePairs() {
        return datePairs;
    }

    public void setDatePairs(DatePair[] datePairs) {
        this.datePairs = datePairs;
    }
}
