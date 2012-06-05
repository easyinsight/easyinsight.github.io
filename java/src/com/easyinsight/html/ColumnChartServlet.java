package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSBarChartDefinition;
import com.easyinsight.analysis.definitions.WSColumnChartDefinition;
import com.easyinsight.analysis.definitions.WSPieChartDefinition;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 5/23/12
 * Time: 4:56 PM
 */
public class ColumnChartServlet extends HtmlServlet {
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report) throws Exception {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);

        JSONObject object = new JSONObject();
        // need series, need ticks
        AnalysisItem xAxisItem;
        AnalysisItem measureItem;
        if (report instanceof WSColumnChartDefinition) {
            WSColumnChartDefinition columnChartDefinition = (WSColumnChartDefinition) report;
            xAxisItem = columnChartDefinition.getXaxis();
            measureItem = columnChartDefinition.getMeasures().get(0);
        } else if (report instanceof WSBarChartDefinition) {
            WSBarChartDefinition columnChartDefinition = (WSBarChartDefinition) report;
            xAxisItem = columnChartDefinition.getYaxis();
            measureItem = columnChartDefinition.getMeasures().get(0);
        } else if (report instanceof WSPieChartDefinition) {
            WSPieChartDefinition pieChart = (WSPieChartDefinition) report;
            xAxisItem = pieChart.getXaxis();
            measureItem = pieChart.getMeasures().get(0);
        } else {
            throw new RuntimeException();
        }

        List<Object> blahs = new ArrayList<Object>();

        for (IRow row : dataSet.getRows()) {
            List<Object> arghs = new ArrayList<Object>();
            if (report instanceof WSColumnChartDefinition) {
                arghs.add(row.getValue(xAxisItem).toString());
                arghs.add(row.getValue(measureItem).toDouble());
            } else if (report instanceof WSBarChartDefinition) {
                arghs.add(row.getValue(measureItem).toDouble());
                arghs.add(row.getValue(xAxisItem).toString());
            } else if (report instanceof WSPieChartDefinition) {
                arghs.add(row.getValue(xAxisItem).toString());
                arghs.add(row.getValue(measureItem).toDouble());
            }
            blahs.add(new JSONArray(arghs));
        }
        // object.put("ticks", ticks);
        JSONArray blahArray = new JSONArray(blahs);
        object.put("values", blahArray);
        System.out.println(blahArray);
        response.setContentType("application/json");
        response.getOutputStream().write(object.toString().getBytes());
        response.getOutputStream().flush();
    }
}
