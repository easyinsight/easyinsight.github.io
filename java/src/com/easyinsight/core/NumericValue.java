package com.easyinsight.core;

import com.easyinsight.analysis.Aggregation;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Jul 1, 2008
 * Time: 11:00:51 AM
 */
public class NumericValue extends Value implements Serializable {

    private Double value;
    private Aggregation aggregation;
    private static final long serialVersionUID = -9170406442789546755L;


    public NumericValue() {
    }

    public NumericValue(Number value) {
        this.value = value.doubleValue();
    }

    public NumericValue(Double value, Aggregation aggregation) {
        this.value = value;
        this.aggregation = aggregation;
    }

    @Nullable
    public Aggregation getAggregation() {
        return aggregation;
    }

    public void setAggregation(Aggregation aggregation) {
        this.aggregation = aggregation;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public NumericValue(Double value) {
        this.value = value;
    }

    public void setValue(String valueObj) {
        this.value = produceDoubleValue(valueObj);
    }

    public static double produceDoubleValue(String valueObj) {
        Double value;
        if (valueObj == null || "".equals(valueObj)) {
            value = 0.;
        } else {
            try {
                value = Double.parseDouble(valueObj);
            } catch (NumberFormatException e) {
                // see if we can find a # in there somewhere...
                char[] transferArray = new char[valueObj.length()];
                int i = 0;
                for (char character : valueObj.toCharArray()) {
                    if (Character.isDigit(character) || character == '.') {
                        transferArray[i++] = character;
                    }
                }
                if (transferArray.length > 0) {
                    try {
                        value = Double.parseDouble(new String(transferArray));
                    } catch (NumberFormatException e1) {
                        value = 0.;
                    }
                } else {
                    value = 0.;
                }
            }
        }
        return value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public int type() {
        return Value.NUMBER;
    }

    public Double toDouble() {
        return value;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NumericValue that = (NumericValue) o;

        return value.equals(that.value);

    }

    public int hashCode() {
        return value.hashCode();
    }
}
