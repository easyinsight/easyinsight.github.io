package com.easyinsight.html;

import com.csvreader.CsvReader;
import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSMap;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import net.minidev.json.parser.JSONParser;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.List;

/**
 * User: jamesboe
 * Date: 5/23/12
 * Time: 4:56 PM
 */
public class TopoMapServlet extends HtmlServlet {
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report, ExportMetadata md) throws Exception {

        WSMap wsMap = (WSMap) report;

        JSONObject object = new JSONObject();
        object.put("map", wsMap.getMap());
        object.put("noDataFill", ExportService.createHexString(wsMap.getNoDataFill()));
        AnalysisItem region = wsMap.getRegion();
        AnalysisItem measure = wsMap.getMeasure();
        AnalysisItem latitude = wsMap.getLatitude();
        AnalysisItem longitude = wsMap.getLongitude();
        AnalysisItem pointMeasure = wsMap.getPointMeasure();
        AnalysisItem pointGrouping = wsMap.getPointGrouping();

        if (region != null && measure != null) {
            populateRegionData(insightRequestMetadata, conn, wsMap, object, region, measure);
        }

        if (longitude != null && latitude != null && pointMeasure != null) {
            populatePointData(insightRequestMetadata, conn, wsMap, object, longitude, latitude, pointMeasure, pointGrouping);
        }

        response.setContentType("application/json");
        response.getOutputStream().write(object.toString().getBytes());
        response.getOutputStream().flush();
    }

    private void populatePointData(InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSMap wsMap, JSONObject object, AnalysisItem longitude, AnalysisItem latitude, AnalysisItem pointMeasure, AnalysisItem pointGrouping) throws JSONException {
        wsMap.setRegion(null);
        wsMap.setMeasure(null);
        wsMap.setLongitude(longitude);
        wsMap.setLatitude(latitude);
        wsMap.setPointMeasure(pointMeasure);
        wsMap.setPointGrouping(pointGrouping);

        DataSet pointSet = DataService.listDataSet(wsMap, insightRequestMetadata, conn);

        Set<Value> pointValues = null;
        Map<Value, String> pointToColor = new HashMap<>();
        List<String> colors = wsMap.createMultiColors();
        //String[] colors = new String[] { "#3333ee", "#33ee33", "#ee3333", "#ee33ee" };

        if (pointGrouping != null) {

            pointValues = new HashSet<>();
            for (IRow row : pointSet.getRows()) {
                pointValues.add(row.getValue(pointGrouping));
            }
            int i = 0;
            for (Value value : pointValues) {
                pointToColor.put(value, colors.get(i % colors.size()));
                i++;
            }
        }

        JSONArray pointSets = new JSONArray();

        if (pointGrouping == null) {
            JSONObject pointData = new JSONObject();
            JSONArray points = new JSONArray();
            pointData.put("points", points);
            pointData.put("color", colors.get(0));
            for (IRow row : pointSet.getRows()) {
                JSONObject point = new JSONObject();
                double doubleValue = row.getValue(pointMeasure).toDouble();
                if (doubleValue > 0) {
                    point.put("pointValue", doubleValue);
                    Value latitudeValue = row.getValue(latitude);
                    Value longitudeValue = row.getValue(longitude);
                    if (latitudeValue.type() == Value.EMPTY) {
                        continue;
                    }
                    if (longitudeValue.type() == Value.EMPTY) {
                        continue;
                    }
                    point.put("lat", Double.parseDouble(latitudeValue.toString()));
                    point.put("lon", Double.parseDouble(longitudeValue.toString()));
                    points.put(point);
                }
            }
            pointSets.put(pointData);
        } else {
            for (Value pointValue : pointValues) {
                JSONObject pointData = new JSONObject();
                JSONArray points = new JSONArray();
                pointData.put("points", points);
                pointData.put("color", pointToColor.get(pointValue));
                pointData.put("name", StringEscapeUtils.escapeHtml(pointValue.toString()));
                for (IRow row : pointSet.getRows()) {
                    if (row.getValue(pointGrouping).equals(pointValue)) {
                        JSONObject point = new JSONObject();
                        double doubleValue = row.getValue(pointMeasure).toDouble();
                        if (doubleValue > 0) {
                            point.put("pointValue", doubleValue);
                            point.put("lat", Double.parseDouble(row.getValue(latitude).toString()));
                            point.put("lon", Double.parseDouble(row.getValue(longitude).toString()));
                            points.put(point);
                        }
                    }
                }
                pointSets.put(pointData);
            }
        }

        object.put("pointDatas", pointSets);
    }

    private void populateRegionData(InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSMap wsMap, JSONObject object, AnalysisItem region, AnalysisItem measure) throws JSONException {
        wsMap.setLatitude(null);
        wsMap.setLongitude(null);
        wsMap.setPointMeasure(null);
        wsMap.setPointGrouping(null);

        DataSet regionSet = DataService.listDataSet(wsMap, insightRequestMetadata, conn);

        IRegionLookup lookup;

        if ("US States".equals(wsMap.getMap())) {
            lookup = new USStatesRegionLookup();
        } else if ("World".equals(wsMap.getMap())) {
            lookup = new WorldRegionLookup();
        } else {
            lookup = new NoOpLookup();
        }

        AggregationFactory factory = new AggregationFactory((AnalysisMeasure) measure, false);
        Map<String, Region> translatedMap = new HashMap<>();
        JSONArray geoData = new JSONArray();
        for (IRow row : regionSet.getRows()) {
            Value rv = row.getValue(region);
            if (rv.type() == Value.EMPTY) {
                continue;
            }
            String regionValue = rv.toString();

            String original = regionValue;
            regionValue = lookup.getValue(regionValue);


            if (regionValue != null) {
                Region regionObj = translatedMap.get(regionValue);
                if (regionObj == null) {
                    regionObj = new Region(regionValue, original, factory.getAggregation());
                    translatedMap.put(regionValue, regionObj);
                }
                regionObj.aggregation.addValue(row.getValue(measure));

            }
        }
        for (Region regionObj : translatedMap.values()) {
            JSONObject regionRow = new JSONObject();
            regionRow.put("region", regionObj.region);
            regionRow.put("originalRegion", regionObj.originalRegion);
            double doubleValue = regionObj.aggregation.getValue().toDouble();
            if (doubleValue > 0) {
                //regionRow.put("scaledValue", Math.log(doubleValue));
                double logValue = Math.log(doubleValue);
                if (logValue < 1) {
                    logValue = 1;
                }
                regionRow.put("scaledValue", logValue);
                regionRow.put("value", doubleValue);
                geoData.put(regionRow);
            }
        }

        object.put("regions", geoData);

        int startIntColor = wsMap.getRegionFillStart();
        int endInitColor = wsMap.getRegionFillEnd();
        Color startColor = new Color(startIntColor);
        Color endColor = new Color(endInitColor);
        int startGreen = startColor.getGreen();
        int startRed = startColor.getRed();
        int startBlue = startColor.getBlue();
        int endGreen = endColor.getGreen();
        int endRed = endColor.getRed();
        int endBlue = endColor.getBlue();

        int colors = 10;

        int greenIncrement = (endGreen - startGreen) / colors;
        int redIncrement = (endRed - startRed) / colors;
        int blueIncrement = (endBlue - startBlue) / colors;

        JSONArray colorArray = new JSONArray();
        for (int i = colors; i >= 0; i--) {
            Color color = new Color(Math.min(startRed + (redIncrement * i), 255), Math.min(startGreen + (greenIncrement * i), 255),
                    Math.min(startBlue + (blueIncrement * i), 255));
            colorArray.put(ExportService.createHexString(color.getRGB()));
        }

        Link l = region.defaultLink();
        if (l != null && l instanceof DrillThrough) {
            JSONObject drillthrough = new JSONObject();
            drillthrough.put("reportID", wsMap.getUrlKey());
            drillthrough.put("id", l.createID());
            drillthrough.put("source", region.getAnalysisItemID());
            drillthrough.put("region", region.getAnalysisItemID());
            object.put("drillthrough", drillthrough);
        }

        if (wsMap.getBoundSet() != null) {
            JSONArray bounds = new JSONArray();
            for (String string : wsMap.getBoundSet()) {
                bounds.put(StringEscapeUtils.escapeJavaScript(string.trim()));
            }
            object.put("bounds_set", bounds);
        }

        object.put("colors", colorArray);

    }

    private static class Region {
        private String region;
        private String originalRegion;
        private Aggregation aggregation;

        private Region(String region, String originalRegion, Aggregation aggregation) {
            this.region = region;
            this.originalRegion = originalRegion;
            this.aggregation = aggregation;
        }
    }

    private interface IRegionLookup {
        String getValue(String string);
    }

    private class WorldRegionLookup implements IRegionLookup {
        private Map<String, String> lookups = new HashMap<>();



        private WorldRegionLookup() {
            lookups.put("af", "Afghanistan");
            lookups.put("afg", "Afghanistan");
            lookups.put("004", "Afghanistan");
            lookups.put("al", "Albania");
            lookups.put("alb", "Albania");
            lookups.put("008", "Albania");
            lookups.put("dz", "Algeria");
            lookups.put("dza", "Algeria");
            lookups.put("012", "Algeria");
            lookups.put("ad", "Andorra");
            lookups.put("and", "Andorra");
            lookups.put("020", "Andorra");
            lookups.put("ao", "Angola");
            lookups.put("ago", "Angola");
            lookups.put("024", "Angola");
            lookups.put("ag", "Antigua and Barbuda");
            lookups.put("atg", "Antigua and Barbuda");
            lookups.put("028", "Antigua and Barbuda");
            lookups.put("ar", "Argentina");
            lookups.put("arg", "Argentina");
            lookups.put("032", "Argentina");
            lookups.put("am", "Armenia");
            lookups.put("arm", "Armenia");
            lookups.put("051", "Armenia");
            lookups.put("au", "Australia");
            lookups.put("aus", "Australia");
            lookups.put("036", "Australia");
            lookups.put("at", "Austria");
            lookups.put("aut", "Austria");
            lookups.put("040", "Austria");
            lookups.put("az", "Azerbaijan");
            lookups.put("aze", "Azerbaijan");
            lookups.put("031", "Azerbaijan");
            lookups.put("bhr", "Bahrain");
            lookups.put("bh", "Bahrain");
            lookups.put("048", "Bahrain");
            lookups.put("bd", "Bangladesh");
            lookups.put("bgd", "Bangladesh");
            lookups.put("050", "Bangladesh");
            lookups.put("bb", "Barbados");
            lookups.put("brb", "Barbados");
            lookups.put("052", "Barbados");
            lookups.put("by", "Belarus");
            lookups.put("blr", "Belarus");
            lookups.put("112", "Belarus");
            lookups.put("be", "Belgium");
            lookups.put("bel", "Belgium");
            lookups.put("056", "Belgium");
            lookups.put("bz", "Belize");
            lookups.put("blz", "Belize");
            lookups.put("084", "Belize");
            lookups.put("bj", "Benin");
            lookups.put("ben", "Benin");
            lookups.put("204", "Benin");
            lookups.put("bm", "Bermuda");
            lookups.put("bmu", "Bermuda");
            lookups.put("060", "Bermuda");
            lookups.put("bt", "Bhutan");
            lookups.put("btn", "Bhutan");
            lookups.put("064", "Bhutan");
            lookups.put("bo", "Bolivia");
            lookups.put("bol", "Bolivia");
            lookups.put("068", "Bolivia");
            lookups.put("ba", "Bosnia and Herzegovina");
            lookups.put("bih", "Bosnia and Herzegovina");
            lookups.put("070", "Bosnia and Herzegovina");
            lookups.put("bw", "Botswana");
            lookups.put("bwa", "Botswana");
            lookups.put("072", "Botswana");
            lookups.put("br", "Brazil");
            lookups.put("bra", "Brazil");
            lookups.put("076", "Brazil");
            lookups.put("bn", "Brunei Darussalem");
            lookups.put("brn", "Brunei Darussalem");
            lookups.put("096", "Brunei Darussalem");
            lookups.put("bg", "Bulgaria");
            lookups.put("bgr", "Bulgaria");
            lookups.put("100", "Bulgaria");
            lookups.put("bf", "Burkina Faso");
            lookups.put("bfa", "Burkina Faso");
            lookups.put("854", "Burkina Faso");
            lookups.put("bi", "Burundi");
            lookups.put("bdi", "Burundi");
            lookups.put("108", "Burundi");
            lookups.put("kh", "Cambodia");
            lookups.put("khm", "Cambodia");
            lookups.put("116", "Cambodia");
            lookups.put("cm", "Cameroon");
            lookups.put("cmr", "Cameroon");
            lookups.put("120", "Cameroon");
            lookups.put("ca", "Canada");
            lookups.put("can", "Canada");
            lookups.put("124", "Canada");
            lookups.put("cf", "Central African Republic");
            lookups.put("caf", "Central African Republic");
            lookups.put("140", "Central African Republic");
            lookups.put("td", "Chad");
            lookups.put("tcd", "Chad");
            lookups.put("148", "Chad");
            lookups.put("cl", "Chile");
            lookups.put("chl", "Chile");
            lookups.put("152", "Chile");
            lookups.put("cn", "China");
            lookups.put("chn", "China");
            lookups.put("156", "China");
            lookups.put("co", "Colombia");
            lookups.put("col", "Colombia");
            lookups.put("170", "Colombia");
            lookups.put("km", "Comoros");
            lookups.put("com", "Comoros");
            lookups.put("174", "Comoros");
            lookups.put("cg", "Congo");
            lookups.put("cog", "Congo");
            lookups.put("178", "Congo");
            lookups.put("cd", "Congo, the Democratic Republic of the");
            lookups.put("cod", "Congo, the Democratic Republic of the");
            lookups.put("180", "Congo, the Democratic Republic of the");
            lookups.put("cr", "Costa Rica");
            lookups.put("cri", "Costa Rica");
            lookups.put("188", "Costa Rica");
            lookups.put("ci", "Cote d'Ivoire");
            lookups.put("civ", "Cote d'Ivoire");
            lookups.put("384", "Cote d'Ivoire");
            lookups.put("hr", "Croatia");
            lookups.put("hrv", "Croatia");
            lookups.put("191", "Croatia");
            lookups.put("cu", "Cuba");
            lookups.put("cub", "Cuba");
            lookups.put("192", "Cuba");
            lookups.put("cy", "Cyprus");
            lookups.put("cyp", "Cyprus");
            lookups.put("196", "Cyprus");
            lookups.put("cz", "Czech Republic");
            lookups.put("cze", "Czech Republic");
            lookups.put("203", "Czech Republic");
            lookups.put("dk", "Denmark");
            lookups.put("dnk", "Denmark");
            lookups.put("208", "Denmark");
            lookups.put("dj", "Djibouti");
            lookups.put("dji", "Djibouti");
            lookups.put("262", "Djibouti");
            lookups.put("dm", "Dominica");
            lookups.put("dma", "Dominica");
            lookups.put("212", "Dominica");
            lookups.put("do", "Dominican Republic");
            lookups.put("dom", "Dominican Republic");
            lookups.put("214", "Dominican Republic");
            lookups.put("ec", "Ecuador");
            lookups.put("ecu", "Ecuador");
            lookups.put("218", "Ecuador");
            lookups.put("eg", "Egypt");
            lookups.put("egy", "Egypt");
            lookups.put("818", "Egypt");
            lookups.put("sv", "El Salvador");
            lookups.put("slv", "El Salvador");
            lookups.put("222", "El Salvador");
            lookups.put("gq", "Equatorial Guinea");
            lookups.put("gnq", "Equatorial Guinea");
            lookups.put("226", "Equatorial Guinea");
            lookups.put("er", "Eritrea");
            lookups.put("eri", "Eritrea");
            lookups.put("232", "Eritrea");
            lookups.put("ee", "Estonia");
            lookups.put("est", "Estonia");
            lookups.put("233", "Estonia");
            lookups.put("et", "Ethiopia");
            lookups.put("eth", "Ethiopia");
            lookups.put("231", "Ethiopia");
            lookups.put("fj", "Fiji");
            lookups.put("fji", "Fiji");
            lookups.put("242", "Fiji");
            lookups.put("fi", "Finland");
            lookups.put("fin", "Finland");
            lookups.put("246", "Finland");
            lookups.put("fr", "France");
            lookups.put("fra", "France");
            lookups.put("250", "France");
            lookups.put("gf", "French Guiana");
            lookups.put("guf", "French Guiana");
            lookups.put("254", "French Guiana");
            lookups.put("ga", "Gabon");
            lookups.put("gab", "Gabon");
            lookups.put("266", "Gabon");
            lookups.put("gm", "Gambia");
            lookups.put("gmb", "Gambia");
            lookups.put("270", "Gambia");
            lookups.put("ge", "Georgia");
            lookups.put("geo", "Georgia");
            lookups.put("268", "Georgia");
            lookups.put("de", "Germany");
            lookups.put("deu", "Germany");
            lookups.put("276", "Germany");
            lookups.put("gh", "Ghana");
            lookups.put("gha", "Ghana");
            lookups.put("288", "Ghana");
            lookups.put("gr", "Greece");
            lookups.put("grc", "Greece");
            lookups.put("300", "Greece");
            lookups.put("gt", "Guatemala");
            lookups.put("gtm", "Guatemala");
            lookups.put("320", "Guatemala");
            lookups.put("gn", "Guinea");
            lookups.put("gin", "Guinea");
            lookups.put("324", "Guinea");
            lookups.put("gw", "Guinea-Bissau");
            lookups.put("gnb", "Guinea-Bissau");
            lookups.put("624", "Guinea-Bissau");
            lookups.put("gy", "Guyana");
            lookups.put("guy", "Guyana");
            lookups.put("328", "Guyana");
            lookups.put("ht", "Haiti");
            lookups.put("hti", "Haiti");
            lookups.put("332", "Haiti");
            lookups.put("hn", "Honduras");
            lookups.put("hnd", "Honduras");
            lookups.put("340", "Honduras");
            lookups.put("hk", "Hong Kong");
            lookups.put("hkg", "Hong Kong");
            lookups.put("344", "Hong Kong");
            lookups.put("hu", "Hungary");
            lookups.put("hun", "Hungary");
            lookups.put("348", "Hungary");
            lookups.put("is", "Iceland");
            lookups.put("isl", "Iceland");
            lookups.put("352", "Iceland");
            lookups.put("in", "India");
            lookups.put("ind", "India");
            lookups.put("356", "India");
            lookups.put("id", "Indonesia");
            lookups.put("idn", "Indonesia");
            lookups.put("360", "Indonesia");
            lookups.put("ir", "Iran, Islamic Republic of");
            lookups.put("irn", "Iran, Islamic Republic of");
            lookups.put("364", "Iran, Islamic Republic of");
            lookups.put("id", "Indonesia");
            lookups.put("idn", "Indonesia");
            lookups.put("360", "Indonesia");
            lookups.put("iq", "Iraq");
            lookups.put("irq", "Iraq");
            lookups.put("368", "Iraq");
            lookups.put("ie", "Ireland");
            lookups.put("irl", "Ireland");
            lookups.put("372", "Ireland");
            lookups.put("il", "Israel");
            lookups.put("isr", "Israel");
            lookups.put("376", "Israel");
            lookups.put("it", "Italy");
            lookups.put("ita", "Italy");
            lookups.put("380", "Italy");
            lookups.put("jm", "Jamaica");
            lookups.put("jam", "Jamaica");
            lookups.put("388", "Jamaica");
            lookups.put("jp", "Japan");
            lookups.put("jpn", "Japan");
            lookups.put("392", "Japan");
            lookups.put("jo", "Jordan");
            lookups.put("jor", "Jordan");
            lookups.put("400", "Jordan");
            lookups.put("kz", "Kazakhstan");
            lookups.put("kaz", "Kazakhstan");
            lookups.put("398", "Kazakhstan");
            lookups.put("ke", "Kenya");
            lookups.put("ken", "Kenya");
            lookups.put("404", "Kenya");
            lookups.put("ki", "Kiribati");
            lookups.put("kir", "Kiribati");
            lookups.put("296", "Kiribati");
            lookups.put("kp", "Korea, Democratic People's Republic of");
            lookups.put("prk", "Korea, Democratic People's Republic of");
            lookups.put("408", "Korea, Democratic People's Republic of");
            lookups.put("kr", "Korea, Republic of");
            lookups.put("kor", "Korea, Republic of");
            lookups.put("410", "Korea, Republic of");
            lookups.put("kw", "Kuwait");
            lookups.put("kwt", "Kuwait");
            lookups.put("414", "Kuwait");
            lookups.put("kg", "Kyrgzystan");
            lookups.put("kgz", "Kyrgzystan");
            lookups.put("417", "Kyrgzystan");
            lookups.put("la", "Laos");
            lookups.put("lao", "Laos");
            lookups.put("418", "Laos");
            lookups.put("lv", "Lativa");
            lookups.put("lva", "Lativa");
            lookups.put("428", "Lativa");
            lookups.put("lb", "Lebanon");
            lookups.put("lbn", "Lebanon");
            lookups.put("422", "Lebanon");
            lookups.put("ls", "Lesotho");
            lookups.put("lso", "Lesotho");
            lookups.put("426", "Lesotho");
            lookups.put("lr", "Liberia");
            lookups.put("lbr", "Liberia");
            lookups.put("430", "Liberia");
            lookups.put("ly", "Libya");
            lookups.put("lby", "Libya");
            lookups.put("434", "Libya");
            lookups.put("li", "Liechtenstein");
            lookups.put("lie", "Liechtenstein");
            lookups.put("438", "Liechtenstein");
            lookups.put("lt", "Lithuania");
            lookups.put("ltu", "Lithuania");
            lookups.put("440", "Lithuania");
            lookups.put("lu", "Luxembourg");
            lookups.put("lux", "Luxembourg");
            lookups.put("442", "Luxembourg");
            lookups.put("mo", "Macao");
            lookups.put("mac", "Macao");
            lookups.put("446", "Macao");
            lookups.put("mk", "Macedonia");
            lookups.put("mkd", "Macedonia");
            lookups.put("807", "Macedonia");
            lookups.put("mg", "Madagascar");
            lookups.put("mdg", "Madagascar");
            lookups.put("450", "Madagascar");
            lookups.put("mw", "Malawi");
            lookups.put("mwi", "Malawi");
            lookups.put("454", "Malawi");
            lookups.put("my", "Malaysia");
            lookups.put("mys", "Malaysia");
            lookups.put("458", "Malaysia");
            lookups.put("mv", "Malvides");
            lookups.put("mdv", "Malvides");
            lookups.put("462", "Malvides");
            lookups.put("ml", "Mali");
            lookups.put("mli", "Mali");
            lookups.put("466", "Mali");
            lookups.put("mr", "Mauritania");
            lookups.put("mrt", "Mauritania");
            lookups.put("478", "Mauritania");
            lookups.put("mu", "Mauritius");
            lookups.put("mus", "Mauritius");
            lookups.put("480", "Mauritius");
            lookups.put("mx", "Mexico");
            lookups.put("mex", "Mexico");
            lookups.put("484", "Mexico");
            lookups.put("md", "Moldova");
            lookups.put("mda", "Moldova");
            lookups.put("498", "Moldova");
            lookups.put("mc", "Monaco");
            lookups.put("mco", "Monaco");
            lookups.put("492", "Monaco");
            lookups.put("mn", "Mongolia");
            lookups.put("mng", "Mongolia");
            lookups.put("496", "Mongolia");
            lookups.put("me", "Montenegro");
            lookups.put("mne", "Montenegro");
            lookups.put("499", "Montenegro");
            lookups.put("ma", "Morocco");
            lookups.put("mar", "Morocco");
            lookups.put("504", "Morocco");
            lookups.put("mz", "Mozambique");
            lookups.put("mzo", "Mozambique");
            lookups.put("508", "Mozambique");
            lookups.put("mm", "Myanmar");
            lookups.put("mmr", "Myanmar");
            lookups.put("104", "Myanmar");
            lookups.put("na", "Namibia");
            lookups.put("nam", "Namibia");
            lookups.put("516", "Namibia");
            lookups.put("np", "Nepal");
            lookups.put("npl", "Nepal");
            lookups.put("524", "Nepal");
            lookups.put("nl", "Netherlands");
            lookups.put("nld", "Netherlands");
            lookups.put("528", "Netherlands");
            lookups.put("nz", "New Zealand");
            lookups.put("nzl", "New Zealand");
            lookups.put("554", "New Zealand");
            lookups.put("ni", "Nicaragua");
            lookups.put("nic", "Nicaragua");
            lookups.put("558", "Nicaragua");
            lookups.put("ne", "Niger");
            lookups.put("ner", "Niger");
            lookups.put("562", "Niger");
            lookups.put("ng", "Nigeria");
            lookups.put("nga", "Nigeria");
            lookups.put("566", "Nigeria");
            lookups.put("no", "Norway");
            lookups.put("nor", "Norway");
            lookups.put("578", "Norway");
            lookups.put("om", "Oman");
            lookups.put("omn", "Oman");
            lookups.put("512", "Oman");
            lookups.put("pk", "Pakistan");
            lookups.put("pak", "Pakistan");
            lookups.put("586", "Pakistan");
            lookups.put("pa", "Panama");
            lookups.put("pan", "Panama");
            lookups.put("591", "Panama");
            lookups.put("pg", "Papua New Guinea");
            lookups.put("png", "Papua New Guinea");
            lookups.put("598", "Papua New Guinea");
            lookups.put("py", "Paraguay");
            lookups.put("pry", "Paraguay");
            lookups.put("600", "Paraguay");
            lookups.put("pe", "Peru");
            lookups.put("per", "Peru");
            lookups.put("604", "Peru");
            lookups.put("ph", "Philippines");
            lookups.put("phl", "Philippines");
            lookups.put("608", "Philippines");
            lookups.put("pl", "Poland");
            lookups.put("pol", "Poland");
            lookups.put("616", "Poland");
            lookups.put("pt", "Portugal");
            lookups.put("prt", "Portugal");
            lookups.put("620", "Portugal");
            lookups.put("qa", "Qatar");
            lookups.put("qat", "Qatar");
            lookups.put("634", "Qatar");
            lookups.put("ro", "Romania");
            lookups.put("rou", "Romania");
            lookups.put("642", "Romania");
            lookups.put("ru", "Russian Federation");
            lookups.put("rus", "Russian Federation");
            lookups.put("643", "Russian Federation");
            lookups.put("rw", "Rwanda");
            lookups.put("rwa", "Rwanda");
            lookups.put("646", "Rwanda");
            lookups.put("sa", "Saudi Arabia");
            lookups.put("sau", "Saudi Arabia");
            lookups.put("682", "Saudi Arabia");
            lookups.put("sn", "Sengal");
            lookups.put("sen", "Sengal");
            lookups.put("686", "Sengal");
            lookups.put("rs", "Serbia");
            lookups.put("srb", "Serbia");
            lookups.put("688", "Serbia");
            lookups.put("sc", "Seychelles");
            lookups.put("syc", "Seychelles");
            lookups.put("690", "Seychelles");
            lookups.put("sl", "Sierra Leone");
            lookups.put("sle", "Sierra Leone");
            lookups.put("694", "Sierra Leone");
            lookups.put("sg", "Singapore");
            lookups.put("sgp", "Singapore");
            lookups.put("702", "Singapore");
            lookups.put("sk", "Slovakia");
            lookups.put("svk", "Slovakia");
            lookups.put("703", "Slovakia");
            lookups.put("so", "Somalia");
            lookups.put("som", "Somalia");
            lookups.put("706", "Somalia");
            lookups.put("za", "South Africa");
            lookups.put("zaf", "South Africa");
            lookups.put("710", "South Africa");
            lookups.put("ss", "South Sudan");
            lookups.put("ssd", "South Sudan");
            lookups.put("728", "South Sudan");
            lookups.put("es", "Spain");
            lookups.put("esp", "Spain");
            lookups.put("724", "Spain");
            lookups.put("lk", "Sri Lanka");
            lookups.put("lka", "Sri Lanka");
            lookups.put("144", "Sri Lanka");
            lookups.put("sd", "Sudan");
            lookups.put("sdn", "Sudan");
            lookups.put("729", "Sudan");
            lookups.put("sr", "Suriname");
            lookups.put("sur", "Suriname");
            lookups.put("740", "Suriname");
            lookups.put("sz", "Swaziland");
            lookups.put("swz", "Swaziland");
            lookups.put("748", "Swaziland");
            lookups.put("se", "Sweden");
            lookups.put("swe", "Sweden");
            lookups.put("752", "Sweden");
            lookups.put("ch", "Switzerland");
            lookups.put("che", "Switzerland");
            lookups.put("756", "Switzerland");
            lookups.put("sy", "Syrian Arab Republic");
            lookups.put("syr", "Syrian Arab Republic");
            lookups.put("760", "Syrian Arab Republic");
            lookups.put("tw", "Taiwan");
            lookups.put("twn", "Taiwan");
            lookups.put("158", "Taiwan");
            lookups.put("tj", "Tajikstan");
            lookups.put("tjk", "Tajikstan");
            lookups.put("762", "Tajikstan");
            lookups.put("tz", "Tanzania");
            lookups.put("tza", "Tanzania");
            lookups.put("834", "Tanzania");
            lookups.put("th", "Thailand");
            lookups.put("tha", "Thailand");
            lookups.put("764", "Thailand");
            lookups.put("tg", "Togo");
            lookups.put("tgo", "Togo");
            lookups.put("768", "Togo");
            lookups.put("to", "Tonga");
            lookups.put("ton", "Tonga");
            lookups.put("776", "Tonga");
            lookups.put("tn", "Tunisia");
            lookups.put("tun", "Tunisia");
            lookups.put("788", "Tunisia");
            lookups.put("tr", "Turkey");
            lookups.put("tur", "Turkey");
            lookups.put("792", "Turkey");
            lookups.put("tm", "Turkmenistan");
            lookups.put("tkm", "Turkmenistan");
            lookups.put("795", "Turkmenistan");
            lookups.put("ug", "Uganda");
            lookups.put("uga", "Uganda");
            lookups.put("800", "Uganda");
            lookups.put("ua", "Ukraine");
            lookups.put("uga", "Ukraine");
            lookups.put("804", "Ukraine");
            lookups.put("ae", "United Arab Emirates");
            lookups.put("are", "United Arab Emirates");
            lookups.put("784", "United Arab Emirates");
            lookups.put("gb", "United Kingdom");
            lookups.put("gbr", "United Kingdom");
            lookups.put("826", "United Kingdom");
            lookups.put("us", "United States");
            lookups.put("usa", "United States");
            lookups.put("840", "United States");
            lookups.put("uy", "Uruguay");
            lookups.put("ury", "Uruguay");
            lookups.put("858", "Uruguay");
            lookups.put("uz", "Uzbekistan");
            lookups.put("uzb", "Uzbekistan");
            lookups.put("860", "Uzbekistan");
            lookups.put("ve", "Venezuela");
            lookups.put("ven", "Venezuela");
            lookups.put("862", "Venezuela");
            lookups.put("vn", "Vietnam");
            lookups.put("vnm", "Vietnam");
            lookups.put("704", "Vietnam");
            lookups.put("ye", "Yemen");
            lookups.put("yem", "Yemen");
            lookups.put("887", "Yemen");

            List<String> copiedValues = new ArrayList<>(lookups.values());
            for (String string : copiedValues) {
                lookups.put(string.toLowerCase(), string);
            }
        }

        @Override
        public String getValue(String string) {
            return lookups.get(string.toLowerCase());
        }
    }

    private class NoOpLookup implements IRegionLookup {

        @Override
        public String getValue(String string) {
            if (string != null && string.length() > 5) {
                return string.substring(0, 5);
            }
            return string;
        }
    }

    private class USStatesRegionLookup implements IRegionLookup {
        private Map<String, String> lookups = new HashMap<String, String>();



        private USStatesRegionLookup() {
            lookups.put("al", "Alabama");
            lookups.put("ak", "Alaska");
            lookups.put("az", "Arizona");
            lookups.put("ar", "Arkansas");
            lookups.put("ca", "California");
            lookups.put("co", "Colorado");
            lookups.put("ct", "Connecticut");
            lookups.put("de", "Delaware");
            lookups.put("dc", "District of Columbia");
            lookups.put("fl", "Florida");
            lookups.put("ga", "Georgia");
            lookups.put("hi", "Hawaii");
            lookups.put("id", "Idaho");
            lookups.put("il", "Illinois");
            lookups.put("in", "Indiana");
            lookups.put("ia", "Iowa");
            lookups.put("ks", "Kansas");
            lookups.put("ky", "Kentucky");
            lookups.put("la", "Louisiana");
            lookups.put("me", "Maine");
            lookups.put("md", "Maryland");
            lookups.put("ma", "Massachusetts");
            lookups.put("mi", "Michigan");
            lookups.put("mn", "Minnesota");
            lookups.put("ms", "Mississippi");
            lookups.put("mo", "Missouri");
            lookups.put("mt", "Montana");
            lookups.put("ne", "Nebraska");
            lookups.put("nv", "Nevada");
            lookups.put("nh", "New Hampshire");
            lookups.put("nj", "New Jersey");
            lookups.put("nm", "New Mexico");
            lookups.put("ny", "New York");
            lookups.put("nc", "North Carolina");
            lookups.put("nd", "North Dakota");
            lookups.put("oh", "Ohio");
            lookups.put("ok", "Oklahoma");
            lookups.put("or", "Oregon");
            lookups.put("pa", "Pennsylvania");
            lookups.put("ri", "Rhode Island");
            lookups.put("sc", "South Carolina");
            lookups.put("sd", "South Dakota");
            lookups.put("tn", "Tennessee");
            lookups.put("tx", "Texas");
            lookups.put("ut", "Utah");
            lookups.put("vt", "Vermont");
            lookups.put("va", "Virginia");
            lookups.put("wa", "Washington");
            lookups.put("wv", "West Virginia");
            lookups.put("wi", "Wisconsin");
            lookups.put("wy", "Wyoming");

            List<String> copiedValues = new ArrayList<>(lookups.values());
            for (String string : copiedValues) {
                lookups.put(string.toLowerCase(), string);
            }
        }

        @Override
        public String getValue(String string) {
            return lookups.get(string.toLowerCase());
        }
    }

    public static void main(String[] args) throws Exception {
        File zipFile = new File("/Users/jamesboe/Downloads/zbp12totals.txt");
        FileReader zipFileReader = new FileReader(zipFile);
        CsvReader reader = new CsvReader(zipFileReader);
        reader.readHeaders();
        Map<String, String> zipMap = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        while (reader.readRecord()) {
            String zip = reader.get(0);
            String state = reader.get(11);
            zipMap.put(zip, state);
            if ("TN".equals(state)) {
                sb.append("'").append(zip).append("',");
            }
            //System.out.println(zip + " - " + state);
        }
        sb.deleteCharAt(sb.length() - 1);
        String inClause = sb.toString();

        // topojson -o tn.json -p name=GEOID10,name=NAME -- tncounties.json

        /* ogr2ogr -f GeoJSON -where "GEOID10 IN ('37010','37011','37012','37013','37014','37015','37016','37018','37019','37020','37022','37023','37024','37025','37026','37027','37028','37029','37030','37031','37032','37033','37034','37035','37036','37037','37040','37041','37042','37043','37044','37046','37047','37048','37049','37050','37051','37052','37055','37056','37057','37058','37059','37060','37061','37062','37063','37064','37065','37066','37067','37068','37069','37070','37071','37072','37073','37074','37075','37076','37077','37078','37079','37080','37082','37083','37085','37086','37087','37088','37089','37090','37091','37095','37096','37097','37098','37101','37110','37111','37115','37116','37118','37121','37122','37127','37128','37129','37130','37131','37132','37133','37134','37135','37136','37137','37138','37140','37141','37142','37143','37144','37145','37146','37148','37149','37150','37151','37152','37153','37160','37162','37166','37167','37171','37172','37174','37175','37178','37179','37180','37181','37183','37184','37185','37186','37187','37188','37189','37190','37191','37201','37202','37203','37204','37205','37206','37207','37208','37209','37210','37211','37212','37213','37214','37215','37216','37217','37218','37219','37220','37221','37222','37224','37227','37228','37229','37232','37234','37235','37236','37238','37240','37241','37242','37243','37244','37246','37250','37301','37302','37303','37304','37305','37306','37307','37308','37309','37310','37311','37312','37313','37314','37315','37316','37317','37318','37320','37321','37322','37323','37324','37325','37326','37327','37328','37329','37330','37331','37332','37333','37334','37335','37336','37337','37338','37339','37340','37341','37342','37343','37345','37347','37348','37349','37350','37352','37353','37354','37355','37356','37357','37359','37360','37361','37362','37363','37364','37365','37366','37367','37369','37370','37371','37373','37374','37375','37376','37377','37378','37379','37380','37381','37382','37383','37384','37385','37387','37388','37389','37391','37394','37397','37398','37401','37402','37403','37404','37405','37406','37407','37408','37409','37410','37411','37412','37414','37415','37416','37419','37421','37422','37424','37450','37501','37601','37602','37604','37605','37614','37615','37616','37617','37618','37620','37621','37625','37640','37641','37642','37643','37644','37645','37650','37656','37657','37658','37659','37660','37662','37663','37664','37665','37680','37681','37682','37683','37684','37686','37687','37688','37690','37691','37692','37694','37701','37705','37707','37708','37709','37710','37711','37713','37714','37715','37716','37717','37719','37721','37722','37723','37724','37725','37726','37727','37729','37730','37731','37732','37733','37737','37738','37742','37743','37744','37745','37748','37752','37753','37754','37755','37756','37757','37760','37762','37763','37764','37766','37769','37770','37771','37772','37773','37774','37777','37779','37801','37802','37803','37804','37806','37807','37809','37810','37811','37813','37814','37815','37816','37818','37819','37820','37821','37822','37824','37825','37826','37828','37829','37830','37831','37840','37841','37843','37845','37846','37847','37848','37849','37852','37853','37854','37857','37860','37861','37862','37863','37864','37865','37866','37867','37868','37869','37870','37871','37872','37873','37874','37876','37877','37878','37879','37880','37881','37882','37885','37886','37887','37888','37890','37891','37892','37901','37902','37909','37912','37914','37915','37916','37917','37918','37919','37920','37921','37922','37923','37924','37927','37928','37929','37930','37931','37932','37933','37934','37938','37939','37940','37950','37996','37998','38001','38002','38004','38006','38007','38008','38011','38012','38014','38015','38016','38017','38018','38019','38021','38023','38024','38025','38027','38028','38029','38030','38034','38036','38037','38039','38040','38041','38042','38044','38046','38047','38048','38049','38050','38052','38053','38054','38055','38057','38058','38059','38060','38061','38063','38066','38067','38068','38069','38070','38075','38076','38077','38079','38080','38083','38088','38101','38103','38104','38105','38106','38107','38108','38109','38111','38112','38113','38114','38115','38116','38117','38118','38119','38120','38122','38124','38125','38126','38127','38128','38130','38131','38132','38133','38134','38135','38137','38138','38139','38141','38145','38151','38152','38157','38159','38163','38167','38168','38173','38174','38175','38177','38181','38182','38183','38184','38186','38187','38190','38193','38194','38197','38201','38220','38221','38222','38223','38224','38225','38226','38229','38230','38231','38232','38233','38235','38236','38237','38238','38240','38241','38242','38251','38253','38254','38255','38256','38257','38258','38259','38260','38261','38271','38281','38301','38302','38303','38305','38308','38310','38311','38313','38314','38315','38316','38317','38320','38321','38324','38326','38327','38328','38329','38330','38332','38333','38334','38337','38338','38339','38340','38341','38342','38343','38344','38345','38346','38347','38348','38351','38352','38355','38356','38357','38358','38359','38361','38362','38363','38365','38366','38367','38368','38369','38370','38371','38372','38374','38375','38376','38378','38379','38380','38381','38382','38388','38389','38390','38391','38392','38401','38402','38425','38449','38450','38451','38452','38453','38454','38455','38456','38457','38459','38460','38461','38462','38463','38464','38468','38469','38471','38472','38473','38474','38475','38476','38477','38478','38481','38482','38483','38485','38486','38487','38488','38501','38502','38503','38504','38505','38506','38541','38543','38544','38545','38547','38548','38549','38550','38551','38552','38553','38554','38555','38556','38557','38558','38559','38560','38562','38563','38564','38565','38567','38568','38569','38570','38571','38572','38573','38574','38575','38577','38578','38579','38580','38581','38582','38583','38585','38587','38588')" oput.json cb_2013_us_zcta510_500k.shp */
        /* ogr2ogr -f GeoJSON -where "GEOID10 IN ('37010','37011','37012','37013','37014','37015','37016','37018','37019','37020','37022','37023','37024','37025','37026','37027','37028','37029','37030','37031','37032','37033','37034','37035','37036','37037','37040','37041','37042','37043','37044','37046','37047','37048','37049','37050','37051','37052','37055','37056','37057','37058','37059','37060','37061','37062','37063','37064','37065','37066','37067','37068','37069','37070','37071','37072','37073','37074','37075','37076','37077','37078','37079','37080','37082','37083','37085','37086','37087','37088','37089','37090','37091','37095','37096','37097','37098','37101','37110','37111','37115','37116','37118','37121','37122','37127','37128','37129','37130','37131','37132','37133','37134','37135','37136','37137','37138','37140','37141','37142','37143','37144','37145','37146','37148','37149','37150','37151','37152','37153','37160','37162','37166','37167','37171','37172','37174','37175','37178','37179','37180','37181','37183','37184','37185','37186','37187','37188','37189','37190','37191','37201','37202','37203','37204','37205','37206','37207','37208','37209','37210','37211','37212','37213','37214','37215','37216','37217','37218','37219','37220','37221','37222','37224','37227','37228','37229','37232','37234','37235','37236','37238','37240','37241','37242','37243','37244','37246','37250','37301','37302','37303','37304','37305','37306','37307','37308','37309','37310','37311','37312','37313','37314','37315','37316','37317','37318','37320','37321','37322','37323','37324','37325','37326','37327','37328','37329','37330','37331','37332','37333','37334','37335','37336','37337','37338','37339','37340','37341','37342','37343','37345','37347','37348','37349','37350','37352','37353','37354','37355','37356','37357','37359','37360','37361','37362','37363','37364','37365','37366','37367','37369','37370','37371','37373','37374','37375','37376','37377','37378','37379','37380','37381','37382','37383','37384','37385','37387','37388','37389','37391','37394','37397','37398','37401','37402','37403','37404','37405','37406','37407','37408','37409','37410','37411','37412','37414','37415','37416','37419','37421','37422','37424','37450','37501','37601','37602','37604','37605','37614','37615','37616','37617','37618','37620','37621','37625','37640','37641','37642','37643','37644','37645','37650','37656','37657','37658','37659','37660','37662','37663','37664','37665','37680','37681','37682','37683','37684','37686','37687','37688','37690','37691','37692','37694','37701','37705','37707','37708','37709','37710','37711','37713','37714','37715','37716','37717','37719','37721','37722','37723','37724','37725','37726','37727','37729','37730','37731','37732','37733','37737','37738','37742','37743','37744','37745','37748','37752','37753','37754','37755','37756','37757','37760','37762','37763','37764','37766','37769','37770','37771','37772','37773','37774','37777','37779','37801','37802','37803','37804','37806','37807','37809','37810','37811','37813','37814','37815','37816','37818','37819','37820','37821','37822','37824','37825','37826','37828','37829','37830','37831','37840','37841','37843','37845','37846','37847','37848','37849','37852','37853','37854','37857','37860','37861','37862','37863','37864','37865','37866','37867','37868','37869','37870','37871','37872','37873','37874','37876','37877','37878','37879','37880','37881','37882','37885','37886','37887','37888','37890','37891','37892','37901','37902','37909','37912','37914','37915','37916','37917','37918','37919','37920','37921','37922','37923','37924','37927','37928','37929','37930','37931','37932','37933','37934','37938','37939','37940','37950','37996','37998','38001','38002','38004','38006','38007','38008','38011','38012','38014','38015','38016','38017','38018','38019','38021','38023','38024','38025','38027','38028','38029','38030','38034','38036','38037','38039','38040','38041','38042','38044','38046','38047','38048','38049','38050','38052','38053','38054','38055','38057','38058','38059','38060','38061','38063','38066','38067','38068','38069','38070','38075','38076','38077','38079','38080','38083','38088','38101','38103','38104','38105','38106','38107','38108','38109','38111','38112','38113','38114','38115','38116','38117','38118','38119','38120','38122','38124','38125','38126','38127','38128','38130','38131','38132','38133','38134','38135','38137','38138','38139','38141','38145','38151','38152','38157','38159','38163','38167','38168','38173','38174','38175','38177','38181','38182','38183','38184','38186','38187','38190','38193','38194','38197','38201','38220','38221','38222','38223','38224','38225','38226','38229','38230','38231','38232','38233','38235','38236','38237','38238','38240','38241','38242','38251','38253','38254','38255','38256','38257','38258','38259','38260','38261','38271','38281','38301','38302','38303','38305','38308','38310','38311','38313','38314','38315','38316','38317','38320','38321','38324','38326','38327','38328','38329','38330','38332','38333','38334','38337','38338','38339','38340','38341','38342','38343','38344','38345','38346','38347','38348','38351','38352','38355','38356','38357','38358','38359','38361','38362','38363','38365','38366','38367','38368','38369','38370','38371','38372','38374','38375','38376','38378','38379','38380','38381','38382','38388','38389','38390','38391','38392','38401','38402','38425','38449','38450','38451','38452','38453','38454','38455','38456','38457','38459','38460','38461','38462','38463','38464','38468','38469','38471','38472','38473','38474','38475','38476','38477','38478','38481','38482','38483','38485','38486','38487','38488','38501','38502','38503','38504','38505','38506','38541','38543','38544','38545','38547','38548','38549','38550','38551','38552','38553','38554','38555','38556','38557','38558','38559','38560','38562','38563','38564','38565','38567','38568','38569','38570','38571','38572','38573','38574','38575','38577','38578','38579','38580','38581','38582','38583','38585','38587','38588')" oput.json cb_2013_us_county_500k.shp */
        System.out.println(inClause);
        /*File file = new File("/Users/jamesboe/Downloads/cb_2013_us_zcta510_500k/simplified.json");
        FileReader fr = new FileReader(file);
        JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
        Map jo = (Map) parser.parse(fr);
        Map childMap = (Map) jo.get("objects");
        Map child2 = (Map) childMap.get("output");
        List<Map> geometries = (List<Map>) child2.get("geometries");
        System.out.println("geometries size = " + geometries.size());
        net.minidev.json.JSONArray surviving = new net.minidev.json.JSONArray();
        for (Map geometry : geometries) {
            Map properties = (Map) geometry.get("properties");
            //System.out.println(properties.keySet());
            properties.remove("AWATER10");
            properties.remove("ALAND10");
            properties.remove("AFFGEOID10");
            properties.remove("ZCTA5CE10");
            String zip =  properties.remove("GEOID10").toString();
            properties.put("id", zip);
            String output = zipMap.get(zip);
            if ("TN".equals(output)) {
                surviving.add(geometry);
                //System.out.println("including " + zip);
            } else {
                //iter.remove();
            }
        }
        System.out.println("surviving size = " + surviving.size());
        child2.put("geometries", surviving);
        FileWriter fw = new FileWriter("/Users/jamesboe/Downloads/cb_2013_us_zcta510_500k/reverse.json");
        String string = jo.toString();
        List contained = (List) ((Map)((Map) jo.get("objects")).get("output")).get("geometries");
        System.out.println(contained.size());
        //System.out.println(string.substring(0, 50000));
        fw.write(string);
        fw.close();*/
        /*if (child instanceof Map) {
            Map c = (Map) child;
            System.out.println(c.keySet());
        }*/
        //System.out.println(obj.getClass().getName());
    }
}
