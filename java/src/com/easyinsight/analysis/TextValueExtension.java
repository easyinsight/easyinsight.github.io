package com.easyinsight.analysis;

import com.easyinsight.core.ValueExtension;

/**
 * User: jamesboe
 * Date: 10/6/11
 * Time: 11:46 AM
 */
public class TextValueExtension extends ValueExtension {

    public static final int WHITE = 16777215;

    private int color;
    private int backgroundColor = WHITE;
    private boolean bold;
    private boolean italic;

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }
}
