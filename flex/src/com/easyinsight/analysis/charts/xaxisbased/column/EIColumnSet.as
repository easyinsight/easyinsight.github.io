/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/4/13
 * Time: 8:49 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.charts.xaxisbased.column {
import mx.charts.CategoryAxis;
import mx.charts.HitData;
import mx.charts.chartClasses.CartesianTransform;
import mx.charts.chartClasses.IAxis;
import mx.charts.chartClasses.IStackable;
import mx.charts.chartClasses.Series;
import mx.charts.series.ColumnSet;
import mx.charts.series.items.ColumnSeriesItem;
import mx.formatters.Formatter;
import mx.core.mx_internal;
use namespace mx_internal;

public class EIColumnSet extends ColumnSet {

    public var xAxisFormatter:Formatter;
    public var yAxisFormatter:Formatter;
    public var stackFormatter:Formatter;

    public function EIColumnSet() {
    }

    override protected function formatDataTip(hd:HitData):String
    {
        var dt:String = "";
        var elt : IStackable = IStackable(hd.element);
        var item:ColumnSeriesItem = ColumnSeriesItem(hd.chartItem);

        var percent:Number;

        var total:Number = posTotalsByPrimaryAxis[item.xValue];

        // now compute the percentage
        if (type == "100%")
        {
            percent = Number(item.yValue) - Number(item.minValue);
            percent = Math.round(percent * 10) / 10;
        }
        else
        {
            if(type=="stacked" && allowNegativeForStacked)
            {
                if(isNaN(total))
                    total = 0;
                total += isNaN(negTotalsByPrimaryAxis[item.xValue]) ? 0 : negTotalsByPrimaryAxis[item.xValue];
            }
            var size:Number = Number(item.yValue) - Number(item.minValue);
            percent = Math.round(size / total * 1000) / 10;
        }



        var n:String = (elt as Series).displayName;
        if (n != null && n.length>0) {
            dt += "<b>" + stackFormatter.format(n) + "</b><BR/>";
        }

        var hAxis:IAxis = dataTransform.getAxis(CartesianTransform.HORIZONTAL_AXIS);
        var xName:String = hAxis.displayName;
        if (xName != "")
            dt += "<i>" + xName + ":</i> ";
        dt += xAxisFormatter.format(CategoryAxis(hAxis).getCategoryValues()[Math.round(Number(item.xValue))]) + "\n";
        //dt += hAxis.formatForScreen(item.xValue) + "\n";

        var vAxis:IAxis = dataTransform.getAxis(CartesianTransform.VERTICAL_AXIS);
        var yName:String = vAxis.displayName;

        if (yName != "")
            dt += "<i>" + yName + ":</i> ";
        dt += yAxisFormatter.format(Number(item.yValue) - Number(item.minValue)) + " (" +  percent + "%)\n";
        if (yName != "")
            dt += "<i>" + yName + " (total):</i> ";
        else
            dt += "<i>total:</i> ";
        dt += yAxisFormatter.format(total);
        return dt;
    }
}
}
