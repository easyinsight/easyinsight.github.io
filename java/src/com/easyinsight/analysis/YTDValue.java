package com.easyinsight.analysis;

import com.easyinsight.core.Value;

import java.util.ArrayList;
import java.util.List;

/**
* User: jamesboe
* Date: 12/2/11
* Time: 6:06 PM
*/
public class YTDValue {
    private AnalysisMeasure analysisMeasure;
    private AnalysisMeasure benchmarkMeasure;
    private Value ytd;
    private Value average;
    private Value benchmarkValue;
    private Value variation;
    private List<TimeIntervalValue> timeIntervalValues = new ArrayList<TimeIntervalValue>();

    public AnalysisMeasure getBenchmarkMeasure() {
        return benchmarkMeasure;
    }

    public void setBenchmarkMeasure(AnalysisMeasure benchmarkMeasure) {
        this.benchmarkMeasure = benchmarkMeasure;
    }

    public AnalysisMeasure getAnalysisMeasure() {
        return analysisMeasure;
    }

    public void setAnalysisMeasure(AnalysisMeasure analysisMeasure) {
        this.analysisMeasure = analysisMeasure;
    }

    public Value getYtd() {
        return ytd;
    }

    public void setYtd(Value ytd) {
        this.ytd = ytd;
    }

    public Value getAverage() {
        return average;
    }

    public void setAverage(Value average) {
        this.average = average;
    }

    public Value getBenchmarkValue() {
        return benchmarkValue;
    }

    public void setBenchmarkValue(Value benchmarkValue) {
        this.benchmarkValue = benchmarkValue;
    }

    public Value getVariation() {
        return variation;
    }

    public void setVariation(Value variation) {
        this.variation = variation;
    }

    public List<TimeIntervalValue> getTimeIntervalValues() {
        return timeIntervalValues;
    }

    public void setTimeIntervalValues(List<TimeIntervalValue> timeIntervalValues) {
        this.timeIntervalValues = timeIntervalValues;
    }
}
