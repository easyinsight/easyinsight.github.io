package com.easyinsight.analysis;

/**
 * User: James Boe
 * Date: Apr 13, 2008
 * Time: 12:52:15 PM
 */
public interface AggregationTypes {
    public static final int SUM = 1;
    public static final int AVERAGE = 2;
    public static final int MAX = 3;
    public static final int MIN = 4;
    public static final int COUNT = 5;
    public static final int NORMALS = 6;
    public static final int CORRELATION = 7;

    public static final int DELTA = 8;
    public static final int LAST_VALUE = 9;
    public static final int PERCENT_CHANGE = 10;
    public static final int MEDIAN = 11;
    public static final int VARIANCE = 12;
    public static final int RANK = 13;
}
