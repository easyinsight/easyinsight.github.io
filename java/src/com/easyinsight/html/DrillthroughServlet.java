package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSStackedBarChartDefinition;
import com.easyinsight.analysis.definitions.WSStackedColumnChartDefinition;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.FieldRule;
import com.easyinsight.tag.Tag;
import com.easyinsight.util.RandomTextGenerator;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

/**
 * User: jamesboe
 * Date: 6/20/12
 * Time: 9:45 AM
 */
public class DrillthroughServlet extends HtmlServlet {

    protected String getValue(JSONObject jsonObject, String param) {
        Object o = jsonObject.get(param);
        if (o != null) {
            return o.toString();
        }
        return null;
    }
    @Override
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSAnalysisDefinition report, Object o) throws Exception {

        JSONObject jsonObject = (JSONObject) o;

        String drillthroughID = getValue(jsonObject, "drillthroughID");
        Long sourceField = Long.parseLong(getValue(jsonObject, "source"));
        String embeddedString = getValue(jsonObject, "embedded");
        boolean embedded = false;
        if (embeddedString != null) {
            embedded = Boolean.parseBoolean(embeddedString);
        }
        String embedKey = getValue(jsonObject, "embedKey");
        AnalysisItem linkItem = null;
        Map<String, Object> data = new HashMap<String, Object>();
        JSONObject dtValues = (JSONObject) jsonObject.get("drillthrough_values");
        {
            Set<AnalysisItem> fieldsReplaced = new HashSet<AnalysisItem>();
            for (FilterDefinition filter : report.getFilterDefinitions()) {

                if (filter instanceof AnalysisItemFilterDefinition) {
                    AnalysisItemFilterDefinition analysisItemFilterDefinition = (AnalysisItemFilterDefinition) filter;
                    if (analysisItemFilterDefinition.isEnabled()) {

                        Map<String, AnalysisItem> structure = report.createStructure();
                        Map<String, AnalysisItem> structureCopy = new HashMap<String, AnalysisItem>(structure);
                        for (Map.Entry<String, AnalysisItem> entry : structureCopy.entrySet()) {
                            if (entry.getValue() instanceof AnalysisHierarchyItem) {
                                continue;
                            }
                            if (entry.getValue().toDisplay().equals(filter.getField().toDisplay())) {
                                if (!fieldsReplaced.contains(entry.getValue())) {
                                    if (insightRequestMetadata.getBaseDate() != null && insightRequestMetadata.getBaseDate().qualifiedName().equals(filter.getField().qualifiedName())) {
                                        insightRequestMetadata.setBaseDate((AnalysisDateDimension) analysisItemFilterDefinition.getTargetItem());
                                    }
                                    structure.put(entry.getKey(), analysisItemFilterDefinition.getTargetItem());
                                    fieldsReplaced.add(entry.getValue());
                                }
                            }
                        }
                        report.populateFromReportStructure(structure);
                    }
                } else if (filter instanceof MultiFieldFilterDefinition) {
                    MultiFieldFilterDefinition multiFieldFilterDefinition = (MultiFieldFilterDefinition) filter;
                    if (multiFieldFilterDefinition.isEnabled()) {
                        report.multiField(multiFieldFilterDefinition);
                    }
                }
            }
        }
        for (AnalysisItem analysisItem : report.getAllAnalysisItems()) {
            if (analysisItem.getAnalysisItemID() == sourceField) {
                linkItem = analysisItem;
            }
            Object v = dtValues.get(String.valueOf(analysisItem.getAnalysisItemID()));
            if (v != null) {
                if(data.get(analysisItem.qualifiedName()) == null)
                    data.put(analysisItem.qualifiedName(), v.toString());
            }
            //String value = getValue(jsonObject, "f" + analysisItem.getAnalysisItemID());

        }
        if (linkItem == null) {
            throw new RuntimeException();
        }
        List<FieldRule> rules = FieldRule.load(conn, report.getDataFeedID());

        Feed feed = FeedRegistry.instance().getFeed(report.getDataFeedID(), conn);
        Map<String, List<Tag>> fieldMap = new HashMap<String, List<Tag>>();
        for (AnalysisItem field : feed.getFields()) {
            if (field.getTags() != null) {
                fieldMap.put(field.toOriginalDisplayName(), field.getTags());
            }
        }
        linkItem.setTags(fieldMap.get(linkItem.toOriginalDisplayName()));
        for (FieldRule rule : rules) {
            if (rule.matches(linkItem)) {
                rule.update(linkItem, report, insightRequestMetadata);
            }
        }

        DrillThrough drillThrough = null;
        for (Link link : linkItem.getLinks()) {
            if (link.createID().equals(drillthroughID)) {
                drillThrough = (DrillThrough) link;
            }
        }
        if (drillThrough == null) {
            if (linkItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                AnalysisHierarchyItem hierarchyItem = (AnalysisHierarchyItem) linkItem;
                if (hierarchyItem.getHierarchyLevels().indexOf(hierarchyItem.getHierarchyLevel()) == (hierarchyItem.getHierarchyLevels().size() - 1)) {

                } else {
                    for (FilterDefinition filter : report.getFilterDefinitions()) {
                        if (filter.getField() != null && filter instanceof AnalysisItemFilterDefinition &&
                                filter.getField().qualifiedName().equals(hierarchyItem.qualifiedName())) {
                            AnalysisItemFilterDefinition analysisItemFilterDefinition = (AnalysisItemFilterDefinition) filter;
                            HierarchyLevel targetLevel = null;
                            for (HierarchyLevel level : hierarchyItem.getHierarchyLevels()) {
                                if (level.getAnalysisItem().toDisplay().equals(analysisItemFilterDefinition.getTargetItem().toDisplay())) {
                                    targetLevel = level;
                                }
                            }
                            hierarchyItem.setHierarchyLevel(targetLevel);
                        }
                    }

                    drillThrough = new DrillThrough();
                    AnalysisItemHandle handle = new AnalysisItemHandle();
                    handle.setName(hierarchyItem.getHierarchyLevel().getAnalysisItem().toDisplay());
                    drillThrough.setPassThroughField(handle);
                    drillThrough.setReportID(report.getAnalysisID());
                    drillThrough.setDefaultLink(true);
                    drillThrough.setLabel("hierarchy");
                    drillThrough.setShowDrillThroughFilters(false);
                }
            } else if (linkItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                AnalysisDateDimension date = (AnalysisDateDimension) linkItem;
                drillThrough = new DrillThrough();
                drillThrough.setReportID(report.getAnalysisID());
                drillThrough.setFilterRowGroupings(true);
                drillThrough.setAddAllFilters(true);
                drillThrough.setDefaultLink(true);
                drillThrough.setLabel("date");
                drillThrough.setShowDrillThroughFilters(false);
            }
        }
        if (drillThrough == null) {
            throw new RuntimeException();
        }

        String altKey = null;

        if(report.getReportType() == WSAnalysisDefinition.STACKED_BAR) {
            altKey = request.getParameter("f" + String.valueOf(((WSStackedBarChartDefinition) report).getStackItem().getAnalysisItemID()));
        } else if (report.getReportType() == WSAnalysisDefinition.STACKED_COLUMN) {
            altKey = request.getParameter("f" + String.valueOf(((WSStackedColumnChartDefinition) report).getStackItem().getAnalysisItemID()));
        }
        DrillThroughResponse drillThroughResponse = new AnalysisService().drillThrough(drillThrough, data, linkItem, report, altKey, null);
        // return a URL for response redirect?
        JSONObject result = new JSONObject();



        EIDescriptor descriptor = drillThroughResponse.getDescriptor();
        if (descriptor.getType() == EIDescriptor.REPORT) {
            InsightDescriptor insightDescriptor = (InsightDescriptor) descriptor;
            PreparedStatement saveDrillStmt = conn.prepareStatement("INSERT INTO DRILLTHROUGH_SAVE (REPORT_ID, URL_KEY, SAVE_TIME) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            saveDrillStmt.setLong(1, insightDescriptor.getId());
            String urlKey = RandomTextGenerator.generateText(40);
            saveDrillStmt.setString(2, urlKey);
            saveDrillStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            saveDrillStmt.execute();
            long drillID = Database.instance().getAutoGenKey(saveDrillStmt);
            saveDrillStmt.close();
            PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO DRILLTHROUGH_REPORT_SAVE_FILTER (DRILLTHROUGH_SAVE_ID, FILTER_ID) VALUES (?, ?)");
            for (FilterDefinition filter : drillThroughResponse.getFilters()) {
                Session session = Database.instance().createSession(conn);
                filter.beforeSave(session);
                session.save(filter);
                session.flush();
                session.close();
                saveStmt.setLong(1, drillID);
                saveStmt.setLong(2, filter.getFilterID());
                saveStmt.execute();
            }
            saveStmt.close();
            if (embedKey != null) {
                result.put("url", "/app/html/user/"+embedKey+"/report/drillthrough/" + urlKey + "/embed");
            } else if (embedded) {
                result.put("url", "/app/html/report/drillthrough/" + urlKey + "/embed");
            } else {
                result.put("url", "/app/html/report/drillthrough/" + urlKey);
            }
        } else if (descriptor.getType() == EIDescriptor.DASHBOARD) {
            DashboardDescriptor dashboardDescriptor = (DashboardDescriptor) descriptor;
            PreparedStatement saveDrillStmt = conn.prepareStatement("INSERT INTO DRILLTHROUGH_SAVE (DASHBOARD_ID, URL_KEY, SAVE_TIME) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            saveDrillStmt.setLong(1, dashboardDescriptor.getId());
            String urlKey = RandomTextGenerator.generateText(40);
            saveDrillStmt.setString(2, urlKey);
            saveDrillStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            saveDrillStmt.execute();
            long drillID = Database.instance().getAutoGenKey(saveDrillStmt);
            saveDrillStmt.close();
            PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO DRILLTHROUGH_REPORT_SAVE_FILTER (DRILLTHROUGH_SAVE_ID, FILTER_ID) VALUES (?, ?)");
            for (FilterDefinition filter : drillThroughResponse.getFilters()) {
                Session session = Database.instance().createSession(conn);
                filter.beforeSave(session);
                session.save(filter);
                session.flush();
                session.close();
                saveStmt.setLong(1, drillID);
                saveStmt.setLong(2, filter.getFilterID());
                saveStmt.execute();
            }
            saveStmt.close();
            if (embedKey != null) {
                result.put("url", "/app/html/user/"+embedKey+"/report/drillthrough/" + urlKey + "/embed");
            } else if (embedded) {
                result.put("url", "/app/html/dashboard/drillthrough/" + urlKey + "/embed");
            } else {
                result.put("url", "/app/html/dashboard/drillthrough/" + urlKey);
            }
        }
        response.setContentType("application/json");
        response.getOutputStream().write(result.toString().getBytes());
        response.getOutputStream().flush();
    }
}
