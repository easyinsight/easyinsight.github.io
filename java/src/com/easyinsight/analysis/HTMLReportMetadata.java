package com.easyinsight.analysis;

/**
 * User: jamesboe
 * Date: 8/12/12
 * Time: 12:00 PM
 */
public class HTMLReportMetadata {
    private int customHeight;
    private boolean fullScreenHeight = true;

    public int getCustomHeight() {
        return customHeight;
    }

    public void setCustomHeight(int customHeight) {
        this.customHeight = customHeight;
    }

    public boolean isFullScreenHeight() {
        return fullScreenHeight;
    }

    public void setFullScreenHeight(boolean fullScreenHeight) {
        this.fullScreenHeight = fullScreenHeight;
    }
}
