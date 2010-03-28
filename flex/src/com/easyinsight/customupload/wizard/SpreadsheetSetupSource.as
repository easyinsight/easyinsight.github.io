package com.easyinsight.customupload.wizard {
import com.easyinsight.FullScreenPage;
import com.easyinsight.listing.AnalyzeSource;

import mx.collections.ArrayCollection;

public class SpreadsheetSetupSource implements AnalyzeSource {

    private var uploadContext:UploadContext;
    private var fields:ArrayCollection;

    public function SpreadsheetSetupSource(uploadContext:UploadContext, fields:ArrayCollection) {
        this.uploadContext = uploadContext;
        this.fields = fields;
    }

    public function createAnalysisPopup():FullScreenPage {
        var wizard:SpreadsheetWizard = new SpreadsheetWizard();
        wizard.uploadContext = uploadContext;
        wizard.fields = fields;
        return wizard;
    }
}
}