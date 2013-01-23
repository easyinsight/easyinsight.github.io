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

    private int color2Start;
    private boolean color2StartEnabled;
    private int color2End;
    private boolean color2EndEnabled;

    private int color3Start;
    private boolean color3StartEnabled;
    private int color3End;
    private boolean color3EndEnabled;

    private int color4Start;
    private boolean color4StartEnabled;
    private int color4End;
    private boolean color4EndEnabled;

    private int color5Start;
    private boolean color5StartEnabled;
    private int color5End;
    private boolean color5EndEnabled;

    private int color6Start;
    private boolean color6StartEnabled;
    private int color6End;
    private boolean color6EndEnabled;

    private int color7Start;
    private boolean color7StartEnabled;
    private int color7End;
    private boolean color7EndEnabled;

    private int color8Start;
    private boolean color8StartEnabled;
    private int color8End;
    private boolean color8EndEnabled;

    private int color9Start;
    private boolean color9StartEnabled;
    private int color9End;
    private boolean color9EndEnabled;

    private int color10Start;
    private boolean color10StartEnabled;
    private int color10End;
    private boolean color10EndEnabled;

    private int color11Start;
    private boolean color11StartEnabled;
    private int color11End;
    private boolean color11EndEnabled;

    private int color12Start;
    private boolean color12StartEnabled;
    private int color12End;
    private boolean color12EndEnabled;

    private int color13Start;
    private boolean color13StartEnabled;
    private int color13End;
    private boolean color13EndEnabled;

    private int color14Start;
    private boolean color14StartEnabled;
    private int color14End;
    private boolean color14EndEnabled;

    private int color15Start;
    private boolean color15StartEnabled;
    private int color15End;
    private boolean color15EndEnabled;

    public static ReportMultiColorProperty fromColors(List<MultiColor> multiColors, String name) {
        ReportMultiColorProperty prop = new ReportMultiColorProperty();
        prop.setPropertyName("multiColors");
        if (multiColors.size() > 0) {
            prop.color1Start = multiColors.get(0).getColor1Start();
            prop.color1StartEnabled = multiColors.get(0).isColor1StartEnabled();
            prop.color1End = multiColors.get(0).getColor1End();
            prop.color1EndEnabled = multiColors.get(0).isColor1EndEnabled();

            prop.color2Start = multiColors.get(1).getColor1Start();
            prop.color2StartEnabled = multiColors.get(1).isColor1StartEnabled();
            prop.color2End = multiColors.get(1).getColor1End();
            prop.color2EndEnabled = multiColors.get(1).isColor1EndEnabled();

            prop.color3Start = multiColors.get(2).getColor1Start();
            prop.color3StartEnabled = multiColors.get(2).isColor1StartEnabled();
            prop.color3End = multiColors.get(2).getColor1End();
            prop.color3EndEnabled = multiColors.get(2).isColor1EndEnabled();

            prop.color4Start = multiColors.get(3).getColor1Start();
            prop.color4StartEnabled = multiColors.get(3).isColor1StartEnabled();
            prop.color4End = multiColors.get(3).getColor1End();
            prop.color4EndEnabled = multiColors.get(3).isColor1EndEnabled();

            prop.color5Start = multiColors.get(4).getColor1Start();
            prop.color5StartEnabled = multiColors.get(4).isColor1StartEnabled();
            prop.color5End = multiColors.get(4).getColor1End();
            prop.color5EndEnabled = multiColors.get(4).isColor1EndEnabled();

            prop.color6Start = multiColors.get(5).getColor1Start();
            prop.color6StartEnabled = multiColors.get(5).isColor1StartEnabled();
            prop.color6End = multiColors.get(5).getColor1End();
            prop.color6EndEnabled = multiColors.get(5).isColor1EndEnabled();

            prop.color7Start = multiColors.get(6).getColor1Start();
            prop.color7StartEnabled = multiColors.get(6).isColor1StartEnabled();
            prop.color7End = multiColors.get(6).getColor1End();
            prop.color7EndEnabled = multiColors.get(6).isColor1EndEnabled();

            prop.color8Start = multiColors.get(7).getColor1Start();
            prop.color8StartEnabled = multiColors.get(7).isColor1StartEnabled();
            prop.color8End = multiColors.get(7).getColor1End();
            prop.color8EndEnabled = multiColors.get(7).isColor1EndEnabled();

            prop.color9Start = multiColors.get(8).getColor1Start();
            prop.color9StartEnabled = multiColors.get(8).isColor1StartEnabled();
            prop.color9End = multiColors.get(8).getColor1End();
            prop.color9EndEnabled = multiColors.get(8).isColor1EndEnabled();

            prop.color10Start = multiColors.get(9).getColor1Start();
            prop.color10StartEnabled = multiColors.get(9).isColor1StartEnabled();
            prop.color10End = multiColors.get(9).getColor1End();
            prop.color10EndEnabled = multiColors.get(9).isColor1EndEnabled();

            prop.color11Start = multiColors.get(10).getColor1Start();
            prop.color11StartEnabled = multiColors.get(10).isColor1StartEnabled();
            prop.color11End = multiColors.get(10).getColor1End();
            prop.color11EndEnabled = multiColors.get(10).isColor1EndEnabled();

            prop.color12Start = multiColors.get(11).getColor1Start();
            prop.color12StartEnabled = multiColors.get(11).isColor1StartEnabled();
            prop.color12End = multiColors.get(11).getColor1End();
            prop.color12EndEnabled = multiColors.get(11).isColor1EndEnabled();

            prop.color13Start = multiColors.get(12).getColor1Start();
            prop.color13StartEnabled = multiColors.get(12).isColor1StartEnabled();
            prop.color13End = multiColors.get(12).getColor1End();
            prop.color13EndEnabled = multiColors.get(12).isColor1EndEnabled();

            prop.color14Start = multiColors.get(13).getColor1Start();
            prop.color14StartEnabled = multiColors.get(13).isColor1StartEnabled();
            prop.color14End = multiColors.get(13).getColor1End();
            prop.color14EndEnabled = multiColors.get(13).isColor1EndEnabled();

            prop.color15Start = multiColors.get(14).getColor1Start();
            prop.color15StartEnabled = multiColors.get(14).isColor1StartEnabled();
            prop.color15End = multiColors.get(14).getColor1End();
            prop.color15EndEnabled = multiColors.get(14).isColor1EndEnabled();
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
        multiColors.add(new MultiColor(color2Start, color2StartEnabled, color2End, color2EndEnabled));
        multiColors.add(new MultiColor(color3Start, color3StartEnabled, color3End, color3EndEnabled));
        multiColors.add(new MultiColor(color4Start, color4StartEnabled, color4End, color4EndEnabled));
        multiColors.add(new MultiColor(color5Start, color5StartEnabled, color5End, color5EndEnabled));
        multiColors.add(new MultiColor(color6Start, color6StartEnabled, color6End, color6EndEnabled));
        multiColors.add(new MultiColor(color7Start, color7StartEnabled, color7End, color7EndEnabled));
        multiColors.add(new MultiColor(color8Start, color8StartEnabled, color8End, color8EndEnabled));
        multiColors.add(new MultiColor(color9Start, color9StartEnabled, color9End, color9EndEnabled));
        multiColors.add(new MultiColor(color10Start, color10StartEnabled, color10End, color10EndEnabled));
        multiColors.add(new MultiColor(color11Start, color11StartEnabled, color11End, color11EndEnabled));
        multiColors.add(new MultiColor(color12Start, color12StartEnabled, color12End, color12EndEnabled));
        multiColors.add(new MultiColor(color13Start, color13StartEnabled, color13End, color13EndEnabled));
        multiColors.add(new MultiColor(color14Start, color14StartEnabled, color14End, color14EndEnabled));
        multiColors.add(new MultiColor(color15Start, color15StartEnabled, color15End, color15EndEnabled));
        return multiColors;
    }
}
