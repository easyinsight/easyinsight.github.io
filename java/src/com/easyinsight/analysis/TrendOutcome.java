package com.easyinsight.analysis;

import com.easyinsight.core.Value;

import java.util.Map;

/**
 * User: jamesboe
 * Date: 9/26/11
 * Time: 2:04 PM
 */
public class TrendOutcome {
    private Value now;
    private Value historical;
    private int direction;
    private int outcome;
    private AnalysisMeasure measure;
    private Map<String, Value> dimensions;

    public AnalysisMeasure getMeasure() {
        return measure;
    }

    public void setMeasure(AnalysisMeasure measure) {
        this.measure = measure;
    }

    public Map<String, Value> getDimensions() {
        return dimensions;
    }

    public void setDimensions(Map<String, Value> dimensions) {
        this.dimensions = dimensions;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getOutcome() {
        return outcome;
    }

    public void setOutcome(int outcome) {
        this.outcome = outcome;
    }

    public Value getNow() {
        return now;
    }

    public void setNow(Value now) {
        this.now = now;
    }

    public Value getHistorical() {
        return historical;
    }

    public void setHistorical(Value historical) {
        this.historical = historical;
    }
}
