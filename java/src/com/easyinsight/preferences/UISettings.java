package com.easyinsight.preferences;

import java.util.List;

/**
 * User: jamesboe
 * Date: Mar 20, 2010
 * Time: 11:55:33 AM
 */
public class UISettings {
    private List<UIVisibilitySetting> visibilitySettings;
    private boolean useCustomScorecard = false;
    private boolean reportSharing = true;
    private boolean marketplace = true;
    private boolean publicSharing = true;

    public boolean isReportSharing() {
        return reportSharing;
    }

    public void setReportSharing(boolean reportSharing) {
        this.reportSharing = reportSharing;
    }

    public boolean isMarketplace() {
        return marketplace;
    }

    public void setMarketplace(boolean marketplace) {
        this.marketplace = marketplace;
    }

    public boolean isPublicSharing() {
        return publicSharing;
    }

    public void setPublicSharing(boolean publicSharing) {
        this.publicSharing = publicSharing;
    }

    public boolean isUseCustomScorecard() {
        return useCustomScorecard;
    }

    public void setUseCustomScorecard(boolean useCustomScorecard) {
        this.useCustomScorecard = useCustomScorecard;
    }

    public List<UIVisibilitySetting> getVisibilitySettings() {
        return visibilitySettings;
    }

    public void setVisibilitySettings(List<UIVisibilitySetting> visibilitySettings) {
        this.visibilitySettings = visibilitySettings;
    }
}
