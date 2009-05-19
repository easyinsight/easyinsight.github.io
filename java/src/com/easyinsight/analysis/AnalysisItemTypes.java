package com.easyinsight.analysis;

/**
 * User: James Boe
 * Date: Jan 20, 2008
 * Time: 11:11:13 PM
 */
public interface AnalysisItemTypes {
    public static final int MEASURE = 1;
    public static final int DIMENSION = 2;
    public static final int RANGE_DIMENSION = 4;
    public static final int DATE_DIMENSION = 8;
    public static final int LISTING = 16;
    public static final int CALCULATION = 32;
    public static final int IMAGE = 64;
    public static final int TEXT = 128;
    public static final int LOOKUP_TABLE = 256;
    public static final int TEMPORAL_MEASURE = 512;
    public static final int COMPLEX_MEASURE = 1024;
    public static final int HIERARCHY = 2048;
    public static final int SIX_SIGMA_MEASURE = 4096;
    public static final int STEP = 8192;
}
