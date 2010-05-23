package com.easyinsight.account {
import com.easyinsight.FullScreenPage;
import com.easyinsight.listing.AnalyzeSource;

public class AccountSetupWizardSource implements AnalyzeSource {
    public function AccountSetupWizardSource() {
    }

    public function createAnalysisPopup():FullScreenPage {
        return new AccountSetupWizard();
    }
}
}