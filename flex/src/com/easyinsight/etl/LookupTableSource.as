package com.easyinsight.etl {
import com.easyinsight.FullScreenPage;
import com.easyinsight.listing.AnalyzeSource;

public class LookupTableSource implements AnalyzeSource {

    private var lookupTable:int;

    public function LookupTableSource(lookupTable:int) {
        this.lookupTable = lookupTable;
    }

    public function createAnalysisPopup():FullScreenPage {
        var page:LookupTableEditor = new LookupTableEditor();
        page.lookupTableID = lookupTable;
        return page;
    }
}
}