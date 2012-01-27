package com.easyinsight.core;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: Aug 31, 2010
 * Time: 9:37:55 PM
 */
public class CoordinateValue extends Value implements Serializable {

    private String x;
    private String y;
    private String zip;

    public CoordinateValue() {
    }

    public CoordinateValue(String x, String y, String zip) {
        this.x = x;
        this.y = y;
        this.zip = zip;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    @Override
    public int type() {
        return Value.COORDINATE;
    }

    @Override
    public Double toDouble() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoordinateValue that = (CoordinateValue) o;

        if (!x.equals(that.x)) return false;
        if (!y.equals(that.y)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x.hashCode();
        result = 31 * result + y.hashCode();
        return result;
    }

    public int compareTo(Value value) {
        return 0;
    }
}
