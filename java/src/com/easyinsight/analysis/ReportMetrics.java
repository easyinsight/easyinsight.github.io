package com.easyinsight.analysis;

/**
 * User: jamesboe
 * Date: Aug 30, 2009
 * Time: 6:47:57 PM
 */
public class ReportMetrics {
    private int count;
    private double average;
    private int myRating;

    public ReportMetrics() {
    }

    public ReportMetrics(int count, double average, int myRating) {
        this.count = count;
        this.average = average;
        this.myRating = myRating;
    }

    public int getMyRating() {
        return myRating;
    }

    public void setMyRating(int myRating) {
        this.myRating = myRating;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }
}
