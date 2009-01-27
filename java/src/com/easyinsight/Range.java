package com.easyinsight;

import java.util.Collection;
import java.util.ArrayList;
import java.text.NumberFormat;

/**
 * User: jboe
 * Date: Jan 29, 2008
 * Time: 10:08:55 AM
 */
public class Range {

    private Collection<Section> fractions;
    private NumberFormat numberFormat = NumberFormat.getInstance();

    public String getRange(double value) {
        for (Section section : fractions) {
            if (section.min == null) {
                if (section.max > value) {
                    return section.getLabel();
                }
            } else if (section.max == null) {
                if (section.min <= value) {
                    return section.getLabel();
                }
            } else if (section.min <= value && section.max > value) {
                return section.getLabel();
            } 
        }
        throw new RuntimeException("should never get here");
    }

    public Range(Collection<Double> values, int threshold) {

        Collection<Section> rangeResults = new ArrayList<Section>();
        // find the min, find the max
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (Double value : values) {
            min = Math.min(min, value);
            max = Math.max(max, value);
        }
        double delta = max - min;
        double divider = delta / threshold;
        double startingPoint = min + divider;
        Double previousMin = null;
        for (int i = 0; i < threshold - 1; i++) {
            Double dividerMax;
            if (previousMin == null) {
                dividerMax = startingPoint;
            } else {
                dividerMax = previousMin + divider;
            }
            rangeResults.add(new Section(previousMin, dividerMax));
            previousMin = dividerMax;
        }
        rangeResults.add(new Section(previousMin, null));
        this.fractions = rangeResults;
    }

    private class Section {
        private Double min;
        private Double max;
        private String label;

        private Section(Double min, Double max) {
            this.min = min;
            this.max = max;
            if (this.min == null) {
                label = "< " + numberFormat.format(max);
            } else if (this.max == null) {
                label = ">= " + numberFormat.format(min);
            } else {
                label = numberFormat.format(min) + " to " + numberFormat.format(max);
            }
        }

        public String getLabel() {
            return label;
        }

        public String toString() {
            return "Min: " + min + " - Max: " + max;
        }
    }
}
