package com.easyinsight.analysis {
import com.easyinsight.HierarchyComboBox;

import mx.controls.Label;
import mx.core.UIComponent;

public class DropAreaFactory {
    public function DropAreaFactory() {
    }

    public static function createDropItemElement(dropArea:DropArea, analysisItem:AnalysisItem):UIComponent {
        /*if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
            var hierarchyComboBox:HierarchyComboBox = new HierarchyComboBox();
            hierarchyComboBox.hierarchy = analysisItem as AnalysisHierarchyItem;
            hierarchyComboBox.editable = false;
            hierarchyComboBox.dropArea = dropArea;
            return hierarchyComboBox;
        } else {*/
            var dropAreaLabel:Label = new Label();
            dropAreaLabel.text = analysisItem.display;
            return dropAreaLabel;
        //}
    }
}
}