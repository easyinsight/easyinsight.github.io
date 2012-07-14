package com.easyinsight.dashboard;

import com.easyinsight.preferences.ImageDescriptor;

/**
 * User: jamesboe
 * Date: 7/9/12
 * Time: 1:45 PM
 */
public class DashboardUIProperties {
    private int color;
    private ImageDescriptor header;

    public DashboardUIProperties(int color, ImageDescriptor header) {
        this.color = color;
        this.header = header;
    }

    public int getColor() {
        return color;
    }

    public ImageDescriptor getHeader() {
        return header;
    }
}
