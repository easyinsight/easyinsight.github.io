/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/10/11
 * Time: 7:09 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.filtering.AnalysisItemFilterDefinition;
import com.easyinsight.filtering.DateLevelWrapper;
import com.easyinsight.filtering.FilterDateRangeDefinition;
import com.easyinsight.filtering.FilterPatternDefinition;
import com.easyinsight.filtering.FilterRangeDefinition;
import com.easyinsight.filtering.FilterValueDefinition;
import com.easyinsight.filtering.FirstValueFilterDefinition;
import com.easyinsight.filtering.FlatDateFilterDefinition;
import com.easyinsight.filtering.LastValueFilterDefinition;
import com.easyinsight.filtering.MonthCutoffFilterDefinition;
import com.easyinsight.filtering.MultiFlatDateFilterDefinition;
import com.easyinsight.filtering.NamedFilterReference;
import com.easyinsight.filtering.NullValueFilterDefinition;
import com.easyinsight.filtering.OrFilter;
import com.easyinsight.filtering.RollingDateRangeFilterDefinition;
import com.easyinsight.scorecard.ScorecardDescriptor;

public class TORegistry {
    public function TORegistry() {
    }

    public static function registerTypes():void {
        var analysisDim:AnalysisDimension;
        var analysisStep:AnalysisStep;
        var analysisLat:AnalysisLatitude;
        var analysisLong:AnalysisLongitude;
        var analysisText:AnalysisText;
        var reagg:ReaggregateAnalysisMeasure;
        var analysisRange:AnalysisRangeDimension;
        var analysisZip:AnalysisZipCode;
        var sigma:SixSigmaMeasure;
        var tags:AnalysisList;
        var hierarchy:AnalysisHierarchyItem;
        var hierarchyLevel:HierarchyLevel;
        var calc:AnalysisCalculation;
        var derived:DerivedAnalysisDimension;
        var derivedDate:DerivedAnalysisDateDimension;
        var urlLink:URLLink;
        var drillthrough:DrillThrough;
        var coordinate:CoordinateValue;
        var stringValue:StringValue;
        var emptyValue:EmptyValue;
        var numValue:NumericValue;
        var filter1:FilterValueDefinition;
        var filter2:FilterDateRangeDefinition;
        var filter3:FilterRangeDefinition;
        var filter4:FilterPatternDefinition;
        var filter5:RollingDateRangeFilterDefinition;
        var filter6:FirstValueFilterDefinition;
        var filter7:LastValueFilterDefinition;
        var filter8:NullValueFilterDefinition;
        var filter9:NamedFilterReference;
        var filter10:AnalysisItemFilterDefinition;
        var filter11:FlatDateFilterDefinition;
        var filter12:MultiFlatDateFilterDefinition;
        var filter13:OrFilter;
        var filter14:MonthCutoffFilterDefinition;
        var dateLevelWrapper:DateLevelWrapper;
        var dashboardDesc:DashboardDescriptor;
        var scorecardDesc:ScorecardDescriptor;
        var join:JoinOverride;
    }
}
}
