/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/28/11
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filter {
import com.easyinsight.filtering.FilterDefinition;
import com.easyinsight.reportviews.FilterInfo;
import com.easyinsight.util.AbsoluteDateFilterTablet;
import com.easyinsight.util.AnalysisItemFilterTablet;
import com.easyinsight.util.FlatDateFilterTablet;
import com.easyinsight.util.MeasureFilterTablet;
import com.easyinsight.util.MultiFlatValueFilterTablet;
import com.easyinsight.util.PatternFilterTablet;
import com.easyinsight.util.RollingFilterTablet;
import com.easyinsight.util.SingleValueFilterTablet;

import mx.core.UIComponent;

public class FilterFactory {
    public function FilterFactory() {
    }

    public static function createTabletFilter(filterDefinition:FilterDefinition, dataSourceID:int):UIComponent {
        var filterInfo:FilterInfo = new FilterInfo(filterDefinition, dataSourceID, 0, 0);
        if (filterDefinition.getType() == FilterDefinition.VALUE) {
            var singleValueTablet:SingleValueFilterTablet = new SingleValueFilterTablet();
            singleValueTablet.filterInfo = filterInfo;
            return singleValueTablet;
        } else if (filterDefinition.getType() == FilterDefinition.FLAT_DATE) {
            var flatValueTablet:FlatDateFilterTablet = new FlatDateFilterTablet();
            flatValueTablet.filterInfo = filterInfo;
            return flatValueTablet;
        } else if (filterDefinition.getType() == FilterDefinition.ANALYSIS_ITEM) {
            var analysisItemTablet:AnalysisItemFilterTablet = new AnalysisItemFilterTablet();
            analysisItemTablet.filterInfo = filterInfo;
            return analysisItemTablet;
        } else if (filterDefinition.getType() == FilterDefinition.ROLLING_DATE) {
            var rolling:RollingFilterTablet = new RollingFilterTablet();
            rolling.filterInfo = filterInfo;
            return rolling;
        } else if (filterDefinition.getType() == FilterDefinition.MULTI_FLAT_DATE) {
            var multi:MultiFlatValueFilterTablet = new MultiFlatValueFilterTablet();
            multi.filterInfo = filterInfo;
            return multi;
        } else if (filterDefinition.getType() == FilterDefinition.DATE) {
            var date:AbsoluteDateFilterTablet = new AbsoluteDateFilterTablet();
            date.filterInfo = filterInfo;
            return date;
        } else if (filterDefinition.getType() == FilterDefinition.RANGE) {
            var range:MeasureFilterTablet = new MeasureFilterTablet();
            range.filterInfo = filterInfo;
            return range;
        } else if (filterDefinition.getType() == FilterDefinition.PATTERN) {
            var pattern:PatternFilterTablet = new PatternFilterTablet();
            pattern.filterInfo = filterInfo;
            return pattern;
        }
        return null;
    }
}
}
