package com.easyinsight.analysis;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 1/22/13
 * Time: 10:25 AM
 */
@Entity
@Table(name="report_multi_color_property")
public class ReportMultiColorProperty extends ReportProperty {
    private int color1Start;
    private boolean color1StartEnabled;
    private int color1End;
    private boolean color1EndEnabled;

    public static ReportMultiColorProperty fromColors(List<MultiColor> multiColors, String name) {
        ReportMultiColorProperty prop = new ReportMultiColorProperty();
        prop.setPropertyName("multiColors");
        if (multiColors.size() > 0) {
            prop.color1Start = multiColors.get(0).getColor1Start();
            prop.color1StartEnabled = multiColors.get(0).isColor1StartEnabled();
            prop.color1End = multiColors.get(0).getColor1End();
            prop.color1EndEnabled = multiColors.get(0).isColor1EndEnabled();
        }
        return prop;
    }

    /*private int color2Start;
    private boolean color2StartEnabled;
    private int color2End;
    private boolean color2EndEnabled;

    private int color3Start;
    private boolean color3StartEnabled;
    private int color3End;
    private boolean color3EndEnabled;
*/

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

    public List<MultiColor> toMultiColorList() {
        List<MultiColor> multiColors = new ArrayList<MultiColor>();
        multiColors.add(new MultiColor(color1Start, color1StartEnabled, color1End, color1EndEnabled));
        return multiColors;
    }
}
