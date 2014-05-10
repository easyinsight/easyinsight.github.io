package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSBarChartDefinition;
import com.easyinsight.analysis.definitions.WSColumnChartDefinition;
import com.easyinsight.analysis.definitions.WSHeatMap;
import com.easyinsight.analysis.definitions.WSPieChartDefinition;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * User: jamesboe
 * Date: 5/23/12
 * Time: 4:56 PM
 */
public class HeatMapServlet extends HtmlServlet {
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report, ExportMetadata md) throws Exception {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);
        WSHeatMap chart = (WSHeatMap) report;
        JSONObject blah = new JSONObject();
        JSONArray blahArray = new JSONArray();

        Map<IRow, Double> scaling = new HashMap<IRow, Double>();
        double max = Double.MIN_VALUE;
        double min = 0;
        for (IRow row : dataSet.getRows()) {
            double weight = row.getValue(chart.getMeasure()).toDouble();
            max = Math.max(max, weight);
        }

        for (IRow row : dataSet.getRows()) {
            double weight = row.getValue(chart.getMeasure()).toDouble();
            if (weight == 0) {
                continue;
            }
            double rem = weight / max + .5;
            scaling.put(row, rem);
        }

        for (IRow row : dataSet.getRows()) {
            JSONObject point = new JSONObject();
            point.put("type", "Feature");
            JSONObject geometry = new JSONObject();
            geometry.put("type", "Point");
            JSONArray coordinates = new JSONArray();
            coordinates.put(row.getValue(chart.getLongitudeItem()).toDouble());
            coordinates.put(row.getValue(chart.getLatitudeItem()).toDouble());
            /*point.put("longitude", row.getValue(chart.getLongitudeItem()));
            point.put("latitude", row.getValue(chart.getLatitudeItem()));*/
            //point.put("value", row.getValue(chart.getMeasure()));
            geometry.put("coordinates", coordinates);
            Double weight = scaling.get(row);
            if (weight != null) {
                point.put("weight", weight);
                point.put("geometry", geometry);
                blahArray.put(point);
            }
        }
        blah.put("features", blahArray);
        JSONObject object = new JSONObject();
        JSONArray b = new JSONArray();
        b.put(blah);
        object.put("values", b);
        System.out.println(b);
        /*
        'features': [
                        {
                            'type': 'Feature',
                            'geometry': {
                                'type': 'Point',
                                'coordinates': [7.798720453887116, 46.8579083635356]
                            }
                        }
                    ]
         */

        response.setContentType("application/json");
        response.getOutputStream().write(object.toString().getBytes());
        response.getOutputStream().flush();
    }
}
