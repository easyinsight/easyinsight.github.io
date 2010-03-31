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
