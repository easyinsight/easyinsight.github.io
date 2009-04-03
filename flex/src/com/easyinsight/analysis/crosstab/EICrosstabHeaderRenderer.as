package com.easyinsight.analysis.crosstab {
import com.easyinsight.analysis.AnalysisItem;
import flash.ui.ContextMenu;
import mx.controls.olapDataGridClasses.OLAPDataGridHeaderRenderer;
public class EICrosstabHeaderRenderer extends OLAPDataGridHeaderRenderer {

    private var _analysisItem:AnalysisItem;

    public function EICrosstabHeaderRenderer() {
        contextMenu = new ContextMenu();
        contextMenu.hideBuiltInItems();
    }

    public function set analysisItem(val:AnalysisItem):void {
        _analysisItem = val;
    }
}
}