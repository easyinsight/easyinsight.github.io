package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSTwoAxisDefinition;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
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
public class LineD3ChartServlet extends HtmlServlet {
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report, ExportMetadata md) throws Exception {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);

        JSONObject object = new JSONObject();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DateFormat dFormat = new SimpleDateFormat("MM/dd/yyyy");
        // need series, need ticks
        WSTwoAxisDefinition twoAxisDefinition = (WSTwoAxisDefinition) report;
        JSONArray blahArray = new JSONArray();
        JSONArray labelArray = new JSONArray();
        List<String> colors = twoAxisDefinition.createMultiColors();
        if (twoAxisDefinition.isMultiMeasure()) {
            List<AnalysisItem> measures = twoAxisDefinition.getMeasures();
            AnalysisDateDimension date = (AnalysisDateDimension) twoAxisDefinition.getXaxis();

            int i = 0;
            for (AnalysisItem measure : measures) {
                List<JSONObject> pointList = new ArrayList<JSONObject>();
                for (IRow row : dataSet.getRows()) {

                    Value v = row.getValue(date);
                    if (!(v instanceof EmptyValue)) {
                        DateValue dateValue = (DateValue) v;
                        JSONObject point = new JSONObject();
                        pointList.add(point);
                        //point.put("x", dateValue.getDate().getTime());
                        point.put("x", dFormat.format(dateValue.getDate()));
                        point.put("y", row.getValue(measure).toDouble());
                    }
                }
                Collections.sort(pointList, new Comparator<JSONObject>() {

                    public int compare(JSONObject jsonObject, JSONObject jsonObject1) {
                        try {
                            return ((Long)jsonObject.getLong("x")).compareTo(jsonObject1.getLong("x"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                JSONArray points = new JSONArray(pointList);
                JSONObject axisObject = new JSONObject();
                axisObject.put("values", points);
                axisObject.put("key", measure.toDisplay());
                String color = colors.get(i % colors.size());
                axisObject.put("color", color);
                blahArray.put(axisObject);
                i++;
            }

        } else {
            AnalysisItem measure = twoAxisDefinition.getMeasure();
            AnalysisDateDimension date = (AnalysisDateDimension) twoAxisDefinition.getXaxis();
            AnalysisItem yAxis = twoAxisDefinition.getYaxis();
            Map<String, Map<Date, Double>> series = new HashMap<String, Map<Date, Double>>();

            Set<Date> xAxisValues = new HashSet<Date>();

            Map<String, String> customColorMap = new HashMap<>();
            for (IRow row : dataSet.getRows()) {
                Value yAxisValueObj = row.getValue(yAxis);
                String yAxisValue = yAxisValueObj.toString();
                String customColor;
                if (yAxisValueObj.getValueExtension() != null && yAxisValueObj.getValueExtension() instanceof TextValueExtension) {
                    TextValueExtension textValueExtension = (TextValueExtension) yAxisValueObj.getValueExtension();
                    if (textValueExtension.getColor() > 0) {
                        customColor = String.format("#%06X", (0xFFFFFF & textValueExtension.getColor()));
                        customColorMap.put(yAxisValue, customColor);
                    }
                }

                Map<Date, Double> array = series.get(yAxisValue);
                if (array == null) {
                    array = new HashMap<>();
                    series.put(yAxisValue, array);
                }
                JSONArray values = new JSONArray();
                Value value = row.getValue(date);
                if (value.type() == Value.DATE) {
                    DateValue dateValue = (DateValue) value;
                    String formattedDate = dateFormat.format(dateValue.getDate());
                    xAxisValues.add(dateValue.getDate());
                    values.put(formattedDate);
                    values.put(row.getValue(measure).toDouble());
                    array.put(dateValue.getDate(), row.getValue(measure).toDouble());
                }
            }



            int i = 0;
            for (Map.Entry<String, Map<Date, Double>> entry : series.entrySet()) {
                JSONObject axisObject = new JSONObject();
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
                JSONArray points = new JSONArray();
                for (Date x : dates) {
                    JSONObject point = new JSONObject();
                    String formattedDate = dateFormat.format(x);
                    point.put("label", formattedDate);
                    //point.put("x", x.getTime());
                    point.put("x", dFormat.format(x));
                    point.put("y", entry.getValue().get(x));
                    points.put(point);
                }
                axisObject.put("values", points);
                axisObject.put("key", entry.getKey());
                String customColor = customColorMap.get(entry.getKey());
                String color = customColor != null ? customColor : colors.get(i % colors.size());
                axisObject.put("color", color);
                i++;
                blahArray.put(axisObject);
            }
        }

        object.put("values", blahArray);
        object.put("date_format", LineD3ChartServlet.getJSFormat(((AnalysisDateDimension) twoAxisDefinition.getXaxis()).getDateLevel(), md.dateFormat));
        if (!twoAxisDefinition.isMultiMeasure()) {
            configureAxes(object, twoAxisDefinition, twoAxisDefinition.getXaxis(), twoAxisDefinition.getMeasure(), md);
        } else {
            configureAxes(object, twoAxisDefinition, twoAxisDefinition.getXaxis(), twoAxisDefinition.getMeasures(), md);
        }

        response.setContentType("application/json");
        String jsonString = object.toString();
        response.getOutputStream().write(jsonString.getBytes());
        response.getOutputStream().flush();
    }

    public static
    @Nullable
    String getJSFormat(int dateLevel, int dateFormat) {
        String sdf = null;
        if (dateLevel == AnalysisDateDimension.YEAR_LEVEL) {
            sdf = "%Y";
        } else if (dateLevel == AnalysisDateDimension.MONTH_LEVEL || dateLevel == AnalysisDateDimension.QUARTER_OF_YEAR_LEVEL) {
            if (dateFormat == 0 || dateFormat == 3) {
                sdf = "%m/%Y";
            } else if (dateFormat == 1) {
                sdf = "%Y-%m";
            } else if (dateFormat == 2) {
                sdf = "%m-%Y";
            } else if (dateFormat == 4) {
                sdf = "%m.%Y";
            }
        } else if (dateLevel == AnalysisDateDimension.HOUR_LEVEL ||
                dateLevel == AnalysisDateDimension.MINUTE_LEVEL) {
            if (dateFormat == 0) {
                sdf = "%m/%d/%Y %H:%M";
            } else if (dateFormat == 1) {
                sdf = "%Y-%m-%d %H:%M";
            } else if (dateFormat == 2) {
                sdf = "%d-%m-%Y %H:%M";
            } else if (dateFormat == 3) {
                sdf = "%d/%m/%Y %H:%M";
            } else if (dateFormat == 4) {
                sdf = "%d.%m.%Y %H:%M";
            }
        } else {
            if (dateFormat == 0) {
                sdf = "%m/%d/%Y";
            } else if (dateFormat == 1) {
                sdf = "%Y-%m-%d";
            } else if (dateFormat == 2) {
                sdf = "%d-%m-%Y";
            } else if (dateFormat == 3) {
                sdf = "%d/%m/%Y";
            } else if (dateFormat == 4) {
                sdf = "%d.%m.%Y";
            }
        }
        return sdf;
    }
}
