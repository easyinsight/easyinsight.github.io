package com.easyinsight.solutions;

import com.easyinsight.core.EIDescriptor;
import com.easyinsight.preferences.ApplicationSkin;

/**
 * User: jamesboe
 * Date: 12/8/13
 * Time: 3:20 PM
 */
public class PostInstallSteps {
    private boolean reloadStyling;
    private ApplicationSkin applicationSkin;
    private EIDescriptor result;

    public EIDescriptor getResult() {
        return result;
    }

    public void setResult(EIDescriptor result) {
        this.result = result;
    }

    public ApplicationSkin getApplicationSkin() {
        return applicationSkin;
    }

    public void setApplicationSkin(ApplicationSkin applicationSkin) {
        this.applicationSkin = applicationSkin;
    }

    public boolean isReloadStyling() {
        return reloadStyling;
    }

    public void setReloadStyling(boolean reloadStyling) {
        this.reloadStyling = reloadStyling;
    }
}
