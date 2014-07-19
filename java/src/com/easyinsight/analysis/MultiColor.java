package com.easyinsight.analysis;

import com.easyinsight.export.ExportMetadata;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
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

    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("color_start_enabled", color1StartEnabled);
        jo.put("color_start", String.format("#%06X", color1Start & 0xFFFFFF));
        jo.put("color_end_enabled", color1EndEnabled);
        jo.put("color_end", String.format("#%06X", color1End & 0xFFFFFF));
        return jo;
    }

    public static MultiColor fromJSON(net.minidev.json.JSONObject jo) {
        MultiColor mc = new MultiColor();
        mc.setColor1StartEnabled(Boolean.valueOf(String.valueOf(jo.get("color_start_enabled"))));
        mc.setColor1EndEnabled(Boolean.valueOf(String.valueOf(jo.get("color_end_enabled"))));
        mc.setColor1Start(Color.decode(String.valueOf(jo.get("color_start"))).getRGB());

        mc.setColor1End(Color.decode(String.valueOf(jo.get("color_end"))).getRGB());
        return mc;
    }
}
