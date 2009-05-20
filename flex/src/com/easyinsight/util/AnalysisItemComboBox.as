package com.easyinsight.util {
import com.easyinsight.analysis.AnalysisItem;

import mx.controls.ComboBox;

public class AnalysisItemComboBox extends ComboBox{
    public function AnalysisItemComboBox() {
        super();
    }

    private var _selectedValue:AnalysisItem;
    private var bSelectedValueSet:Boolean = false;
    private var bDataProviderSet:Boolean = false;

    // Override committ, this may be called repeatedly
    override protected function commitProperties():void
    {
        // invoke ComboBox version
        super.commitProperties();

        if (_selectedValue == null) {
            this.selectedIndex = 0;
        } else {
            // If value set and have dataProvider
            if (bSelectedValueSet && bDataProviderSet) {
                // Set flag to false so code won't be called until selectedValue is set again
                bSelectedValueSet = false;
                // Loop through dataProvider
                for (var i:int = 0; i < this.dataProvider.length; i++) {
                    // Get this item's data
                    var item:AnalysisItem = this.dataProvider[i];

                    // Check if is selectedValue
                    if (item.analysisItemID == _selectedValue.analysisItemID) {
                        // Yes, set selectedIndex
                        this.selectedIndex = i;
                        break;
                    }
                }
            }
        }
    }

    // Trap dataProvider being set
    override public function set dataProvider(o:Object):void {
        // invoke ComboBox version
        super.dataProvider = o;

        // This may get called before dataProvider is set, so make sure not null and has entries
        if (o != null && o.length)
        {
            // Got it, set flag
            bDataProviderSet = true;
        }
    }

    // set for selectedValue
    public function set selectedValue(s:AnalysisItem):void {
        // Set flag
        bSelectedValueSet = true;
        // Save value
        _selectedValue = s;
        // Invalidate to force commit
        invalidateProperties();
    }


}
}