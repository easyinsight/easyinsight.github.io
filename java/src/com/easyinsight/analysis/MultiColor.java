package com.easyinsight.analysis;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: 1/22/13
 * Time: 2:38 PM
 */
public class MultiColor implements Serializable {
    private int color1Start;
    private boolean color1StartEnabled;
    private int color1End;
    private boolean color1EndEnabled;

    public MultiColor(int color1Start, boolean color1StartEnabled, int color1End, boolean color1EndEnabled) {
        this.color1Start = color1Start;
        this.color1StartEnabled = color1StartEnabled;
        this.color1End = color1End;
        this.color1EndEnabled = color1EndEnabled;
    }

    public MultiColor() {

    }

    public int getColor1Start() {
        return color1Start;
    }

    public void setColor1Start(int color1Start) {
        this.color1Start = color1Start;
    }

    public boolean isColor1StartEnabled() {
        return color1StartEnabled;
    }

    public void setColor1StartEnabled(boolean color1StartEnabled) {
        this.color1StartEnabled = color1StartEnabled;
    }

    public int getColor1End() {
        return color1End;
    }

    public void setColor1End(int color1End) {
        this.color1End = color1End;
    }

    public boolean isColor1EndEnabled() {
        return color1EndEnabled;
    }

    public void setColor1EndEnabled(boolean color1EndEnabled) {
        this.color1EndEnabled = color1EndEnabled;
    }
}
