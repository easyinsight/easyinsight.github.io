package com.easyinsight.analysis.charts.plot {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.charts.ChartTypes;
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSPlotChartDefinition")]
public class PlotChartDefinition extends ChartDefinition{
    public var dimension:AnalysisItem;
    public var iconGrouping:AnalysisItem;
    public var xaxisMeasure:AnalysisItem;
    public var yaxisMeasure:AnalysisItem;

    public function PlotChartDefinition() {
        super();
    }

    override public function supportsEmbeddedFonts():Boolean {
        return true;
    }

    override public function populate(fields:ArrayCollection):void {
        var measures:ArrayCollection = findItems(fields, AnalysisItemTypes.MEASURE);
        if (measures.length > 0) {
            xaxisMeasure = measures.getItemAt(0) as AnalysisItem;
            if (measures.length > 1) {
                yaxisMeasure = measures.getItemAt(1) as AnalysisItem;
            }
        }
        var dimensions:ArrayCollection = findItems(fields, AnalysisItemTypes.DIMENSION);
        if (dimensions.length > 0) {
            dimension = dimensions.getItemAt(0) as AnalysisItem;
        }
    }

    override public function getFields():ArrayCollection {
        var fields:ArrayCollection = new ArrayCollection([ dimension, xaxisMeasure, yaxisMeasure ]);
        if (iconGrouping != null) {
            fields.addItem(iconGrouping);
        }
        return fields;
    }

    override public function get type():int {
        return AnalysisDefinition.PLOT;
    }

    override public function getChartType():int {
        return ChartTypes.PLOT;
    }

    override public function getChartFamily():int {
        return ChartTypes.PLOT_FAMILY;
    }
}
}