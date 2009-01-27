package com.easyinsight.storage;

/**
 * User: James Boe
* Date: Nov 10, 2008
* Time: 6:27:40 PM
*/
public class Comparison {
    private int comparisonType;

    public static final Comparison GREATER_THAN = new Comparison(1);
    public static final Comparison EQUAL_TO = new Comparison(2);
    public static final Comparison LESS_THAN = new Comparison(3);

    Comparison(int comparisonType) {
        this.comparisonType = comparisonType;
    }

    public String createComparison() {
        if (comparisonType == 1) {
            return ">";
        } else if (comparisonType == 2) {
            return "=";
        } else {
            return "<";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comparison that = (Comparison) o;

        return comparisonType == that.comparisonType;

    }

    @Override
    public int hashCode() {
        return comparisonType;
    }
}
