package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSMap;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
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

        AnalysisItem region = wsMap.getRegion();
        AnalysisItem measure = wsMap.getMeasure();
        AnalysisItem latitude = wsMap.getLatitude();
        AnalysisItem longitude = wsMap.getLongitude();
        AnalysisItem pointMeasure = wsMap.getPointMeasure();
        AnalysisItem pointGrouping = wsMap.getPointGrouping();

        populateRegionData(insightRequestMetadata, conn, wsMap, object, region, measure);

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
                    point.put("lat", Double.parseDouble(row.getValue(latitude).toString()));
                    point.put("lon", Double.parseDouble(row.getValue(longitude).toString()));
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
            throw new RuntimeException("Unknown map " + wsMap.getMap());
        }

        JSONArray geoData = new JSONArray();
        for (IRow row : regionSet.getRows()) {
            JSONObject regionRow = new JSONObject();
            String regionValue = row.getValue(region).toString();
            String original = regionValue;
            regionValue = lookup.getValue(regionValue);
            if (regionValue != null) {
                regionRow.put("region", regionValue);
                regionRow.put("originalRegion", original);
                double doubleValue = row.getValue(measure).toDouble();
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
        }

        object.put("regions", geoData);
        object.put("map", wsMap.getMap());
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

        object.put("colors", colorArray);
        object.put("noDataFill", ExportService.createHexString(wsMap.getNoDataFill()));
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
}
