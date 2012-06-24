package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSTwoAxisDefinition;
import com.easyinsight.core.DateValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 5/23/12
 * Time: 4:56 PM
 */
public class LineChartServlet extends HtmlServlet {
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report) throws Exception {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);

        JSONObject object = new JSONObject();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // need series, need ticks
        WSTwoAxisDefinition twoAxisDefinition = (WSTwoAxisDefinition) report;
        JSONArray blahArray = new JSONArray();
        JSONArray labelArray = new JSONArray();
        if (twoAxisDefinition.isMultiMeasure()) {
            List<AnalysisItem> measures = twoAxisDefinition.getMeasures();
            AnalysisDateDimension date = (AnalysisDateDimension) twoAxisDefinition.getXaxis();
            for (AnalysisItem measure : measures) {
                JSONArray measureArray = new JSONArray();
                labelArray.put(measure.toDisplay());
                blahArray.put(measureArray);
                for (IRow row : dataSet.getRows()) {
                    DateValue dateValue = (DateValue) row.getValue(date);
                    JSONArray values = new JSONArray();
                    measureArray.put(values);
                    values.put(dateFormat.format(dateValue.getDate()));
                    values.put(row.getValue(measure).toDouble());
                }
            }
        } else {
            AnalysisItem measure = twoAxisDefinition.getMeasure();
            AnalysisDateDimension date = (AnalysisDateDimension) twoAxisDefinition.getXaxis();
            AnalysisItem yAxis = twoAxisDefinition.getYaxis();

            Map<String, JSONArray> series = new HashMap<String, JSONArray>();



            for (IRow row : dataSet.getRows()) {
                String yAxisValue = row.getValue(yAxis).toString();
                JSONArray array = series.get(yAxisValue);
                if (array == null) {
                    array = new JSONArray();
                    series.put(yAxisValue, array);
                }
                JSONArray values = new JSONArray();
                DateValue dateValue = (DateValue) row.getValue(date);
                values.put(dateFormat.format(dateValue.getDate()));
                values.put(row.getValue(measure).toDouble());
                array.put(values);
            }


            for (Map.Entry<String, JSONArray> entry : series.entrySet()) {
                labelArray.put(entry.getKey());
                blahArray.put(entry.getValue());
            }
        }

        // object.put("ticks", ticks);

        object.put("values", blahArray);
        object.put("labels", labelArray);

        response.setContentType("application/json");
        String argh = object.toString();
        System.out.println(argh);
        response.getOutputStream().write(argh.getBytes());
        response.getOutputStream().flush();
    }
}
