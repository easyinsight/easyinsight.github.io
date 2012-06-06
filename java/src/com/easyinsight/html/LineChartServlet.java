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
        // need series, need ticks
        WSTwoAxisDefinition twoAxisDefinition = (WSTwoAxisDefinition) report;
        AnalysisItem measure = twoAxisDefinition.getMeasure();
        AnalysisDateDimension date = (AnalysisDateDimension) twoAxisDefinition.getXaxis();
        AnalysisItem yAxis = twoAxisDefinition.getYaxis();

        Map<String, JSONArray> series = new HashMap<String, JSONArray>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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

        JSONArray blahArray = new JSONArray();
        for (Map.Entry<String, JSONArray> entry : series.entrySet()) {
            blahArray.put(entry.getValue());
        }
        // object.put("ticks", ticks);

        object.put("values", blahArray);

        response.setContentType("application/json");
        String argh = object.toString();
        System.out.println(argh);
        response.getOutputStream().write(argh.getBytes());
        response.getOutputStream().flush();
    }
}
