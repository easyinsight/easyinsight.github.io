package com.easyinsight.customupload.wizard {
import com.easyinsight.analysis.AnalysisItem;

import mx.containers.VBox;

public class SpreadsheetTutorialPanel extends VBox {

    private var _info:FieldUploadInfo;

    public function SpreadsheetTutorialPanel() {
        super();
    }

    public function set info(value:FieldUploadInfo):void {
        _info = value;
    }

    public function get analysisItem():AnalysisItem {

    }
}
}