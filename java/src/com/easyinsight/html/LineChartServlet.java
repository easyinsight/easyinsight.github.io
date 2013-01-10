package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSTwoAxisDefinition;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
                    Value v = row.getValue(date);
                    if (!(v instanceof EmptyValue)) {
                        DateValue dateValue = (DateValue) v;
                        JSONArray values = new JSONArray();
                        measureArray.put(values);
                        values.put(dateFormat.format(dateValue.getDate()));
                        if (row.getValue(measure).toDouble() == 0) {
                            values.put(1);
                        } else {
                            values.put(row.getValue(measure).toDouble());
                        }
                    }
                }
            }
        } else {
            AnalysisItem measure = twoAxisDefinition.getMeasure();
            AnalysisDateDimension date = (AnalysisDateDimension) twoAxisDefinition.getXaxis();
            AnalysisItem yAxis = twoAxisDefinition.getYaxis();

            Map<String, Map<Date, Double>> series = new HashMap<String, Map<Date, Double>>();

            Set<Date> xAxisValues = new HashSet<Date>();

            for (IRow row : dataSet.getRows()) {
                String yAxisValue = row.getValue(yAxis).toString();
                Map<Date, Double> array = series.get(yAxisValue);
                if (array == null) {
                    array = new HashMap<Date, Double>();
                    series.put(yAxisValue, array);
                }
                JSONArray values = new JSONArray();
                DateValue dateValue = (DateValue) row.getValue(date);
                String formattedDate = dateFormat.format(dateValue.getDate());
                xAxisValues.add(dateValue.getDate());
                values.put(formattedDate);
                values.put(row.getValue(measure).toDouble());
                array.put(dateValue.getDate(), row.getValue(measure).toDouble());
                //array.put(values);
            }


            for (Map.Entry<String, Map<Date, Double>> entry : series.entrySet()) {
                Set<Date> uniques = new HashSet<Date>();
                for (Map.Entry<Date, Double> entry1 : entry.getValue().entrySet()) {
                    uniques.add(entry1.getKey());
                }
                for (Date xAxisValue : xAxisValues) {
                    if (!uniques.contains(xAxisValue)) {
                        JSONArray ph = new JSONArray();
                        ph.put(xAxisValue);
                        ph.put(0.0);
                        entry.getValue().put(xAxisValue, 0.0);
                    }
                }
                labelArray.put(entry.getKey());
                List<Date> dates = new ArrayList<Date>(entry.getValue().keySet());
                Collections.sort(dates);
                JSONArray array = new JSONArray();
                for (Date x : dates) {
                    JSONArray point = new JSONArray();
                    String formattedDate = dateFormat.format(x);
                    point.put(formattedDate);
                    point.put(entry.getValue().get(x));
                    array.put(point);
                }
                blahArray.put(array);
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
