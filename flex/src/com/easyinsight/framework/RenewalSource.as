package com.easyinsight.framework {
import com.easyinsight.customupload.wizard.*;
import com.easyinsight.FullScreenPage;
import com.easyinsight.listing.AnalyzeSource;

import mx.collections.ArrayCollection;

public class RenewalSource implements AnalyzeSource {

    public function RenewalSource() {
    }

    public function createAnalysisPopup():FullScreenPage {
        return new LoginUpdateWindow();
    }
}
}