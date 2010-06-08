package com.easyinsight.export;

import com.easyinsight.analysis.DataService;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedDescriptor;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.email.SendGridEmail;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.logging.LogClass;
import com.easyinsight.core.Value;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.DateValue;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.analysis.ListRow;
import com.easyinsight.analysis.*;

import com.easyinsight.storage.DataStorage;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: James Boe
 * Date: Jun 2, 2008
 * Time: 4:26:26 PM
 */
public class ExportService {

    public static final String CURRENCY_STYLE = "currency";
    public static final String GENERIC_STYLE = "generic";

    public void addOrUpdateSchedule(ScheduledActivity scheduledActivity, int utcOffset) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            scheduledActivity.save(conn, utcOffset);
            scheduledActivity.setup(conn);
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public List<FeedDescriptor> getRefreshableDataSources(ScheduledActivity scheduledActivity) {
        List<FeedDescriptor> validSources = new ArrayList<FeedDescriptor>();
        List<FeedDescriptor> dataSources = new FeedService().searchForSubscribedFeeds();
        for (FeedDescriptor fd : dataSources) {
            if (isRefreshable(fd.getFeedType())) {
                validSources.add(fd);
            }
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT FEED_TYPE, scheduled_account_activity.scheduled_account_activity_id FROM " +
                    "DATA_FEED, SCHEDULED_DATA_SOURCE_REFRESH, scheduled_account_activity WHERE " +
                    "DATA_FEED.data_feed_id = SCHEDULED_DATA_SOURCE_REFRESH.data_source_id and " +
                    "scheduled_data_source_refresh.scheduled_account_activity_id = scheduled_account_activity.scheduled_account_activity_id and " +
                    "scheduled_account_activity.account_id = ?");
            queryStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                int feedType = rs.getInt(1);
                long id = rs.getLong(2);
                if (scheduledActivity != null && id == scheduledActivity.getScheduledActivityID()) continue;
                Iterator<FeedDescriptor> descIter = validSources.iterator();
                while (descIter.hasNext()) {
                    FeedDescriptor fd = descIter.next();
                    if (fd.getFeedType() == feedType) {
                        descIter.remove();
                    }
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return validSources;
    }

    private boolean isRefreshable(int feedType) {
        return (feedType == FeedType.BASECAMP_MASTER.getType() || feedType == FeedType.HIGHRISE_COMPOSITE.getType());
    }

    public List<ScheduledActivity> getScheduledActivities(int utcOffset) {
        EIConnection conn = Database.instance().getConnection();
        List<ScheduledActivity> activities = new ArrayList<ScheduledActivity>();
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT SCHEDULED_ACCOUNT_ACTIVITY.scheduled_account_activity_id," +
                    "SCHEDULED_ACCOUNT_ACTIVITY.activity_type FROM SCHEDULED_ACCOUNT_ACTIVITY WHERE ACCOUNT_ID = ?");
            queryStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long activityID = rs.getLong(1);
                int activityType = rs.getInt(2);
                activities.add(ScheduledActivity.createActivity(activityType, activityID, conn, utcOffset));
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return activities;
    }



    public void deleteSchedule(long scheduledActivityID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM SCHEDULED_ACCOUNT_ACTIVITY WHERE " +
                    "scheduled_account_activity_id = ?");
            deleteStmt.setLong(1, scheduledActivityID);
            deleteStmt.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public long exportDataSourceToCSV(long dataSourceID) {
        SecurityUtil.authorizeFeedAccess(dataSourceID);
        try {
            FeedDefinition dataSource = new FeedService().getFeedDefinition(dataSourceID);
            DataStorage dataStorage = DataStorage.readConnection(dataSource.getFields(), dataSource.getDataFeedID());
            DataSet dataSet = dataStorage.allData(new ArrayList<FilterDefinition>(), null);
            StringBuilder sb = new StringBuilder();
            for (AnalysisItem item : dataSource.getFields()) {
                sb.append(item.getDisplayName());
                sb.append(",");
                sb.deleteCharAt(sb.length());
                sb.append("\r\n");
            }
            for (IRow row : dataSet.getRows()) {
                for (AnalysisItem item : dataSource.getFields()) {
                    Value value = row.getValue(item.getKey());
                    sb.append(value.toString());
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length());
                sb.append("\r\n");
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return 0;
    }

    public byte[] exportReportIDTOCSV(long reportID, List<FilterDefinition> customFilters, List<FilterDefinition> drillThroughFilters) {
        SecurityUtil.authorizeInsight(reportID);
        try {
            WSAnalysisDefinition analysisDefinition = new AnalysisService().openAnalysisDefinition(reportID);
            analysisDefinition.updateMetadata();
            if (customFilters != null) {
                analysisDefinition.setFilterDefinitions(customFilters);
            }
            if (drillThroughFilters != null) {
                analysisDefinition.applyFilters(drillThroughFilters);
            }
            ListDataResults listDataResults = (ListDataResults) new DataService().list(analysisDefinition, new InsightRequestMetadata());
            return toCSV(analysisDefinition, listDataResults);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private byte[] toCSV(WSAnalysisDefinition analysisDefinition, ListDataResults listDataResults) {
        return null;
    }

    public void testDeliver(long reportID, String reportName) {
        try {
            WSAnalysisDefinition analysisDefinition = new AnalysisService().openAnalysisDefinition(reportID);
            analysisDefinition.updateMetadata();
            ListDataResults listDataResults = (ListDataResults) new DataService().list(analysisDefinition, new InsightRequestMetadata());
            byte[] bytes = toExcel(analysisDefinition, listDataResults);
            new SendGridEmail().sendAttachmentEmail("jboe99@gmail.com", reportName, "Here's your weekly run of " + reportName + ". If you want to view the" +
                    "latest data of the report, you can follow this link: <a href=\"http://www.easy-insight.com\">View Report in Easy Insight</a>. Enjoy!", bytes, reportName);
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    public byte[] exportReportIDToExcel(long reportID, List<FilterDefinition> customFilters, List<FilterDefinition> drillThroughFilters, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeInsight(reportID);
        try {
            WSAnalysisDefinition analysisDefinition = new AnalysisService().openAnalysisDefinition(reportID);
            analysisDefinition.updateMetadata();
            if (customFilters != null) {
                analysisDefinition.setFilterDefinitions(customFilters);
            }
            if (drillThroughFilters != null) {
                analysisDefinition.applyFilters(drillThroughFilters);
            }
            ListDataResults listDataResults = (ListDataResults) new DataService().list(analysisDefinition, insightRequestMetadata);
            return toExcel(analysisDefinition, listDataResults);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public byte[] exportToExcel(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        SecurityUtil.authorizeFeed(analysisDefinition.getDataFeedID(), Roles.SUBSCRIBER);
        try {
            analysisDefinition.updateMetadata();
            ListDataResults listDataResults = (ListDataResults) new DataService().list(analysisDefinition, insightRequestMetadata);
            return toExcel(analysisDefinition, listDataResults);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public byte[] toExcel(WSAnalysisDefinition analysisDefinition, ListDataResults listDataResults) throws IOException, SQLException {
        EIConnection conn = Database.instance().getConnection();
        int dateFormat;
        try {
            PreparedStatement dateFormatStmt = conn.prepareStatement("SELECT DATE_FORMAT FROM ACCOUNT WHERE ACCOUNT_ID = ?");
            dateFormatStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet rs = dateFormatStmt.executeQuery();
            rs.next();
            dateFormat = rs.getInt(1);
        } finally {
            Database.closeConnection(conn);
        }
        HSSFWorkbook workbook = createWorkbookFromList(analysisDefinition, listDataResults, dateFormat);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        byte[] bytes = baos.toByteArray();
        baos.close();
        return bytes;
    }

    private HSSFWorkbook createWorkbookFromList(WSAnalysisDefinition listDefinition, ListDataResults listDataResults, int dateFormat) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        Map<String, HSSFCellStyle> styleMap = new HashMap<String, HSSFCellStyle>();
        HSSFCellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("$##,##0.00"));
        styleMap.put(CURRENCY_STYLE, currencyStyle);
        HSSFCellStyle genericStyle = workbook.createCellStyle();
        styleMap.put(GENERIC_STYLE, genericStyle);
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "Data");
        HSSFRow headerRow = sheet.createRow(0);
        Map<AnalysisItem, Short> positionMap = new HashMap<AnalysisItem, Short>();
        List<AnalysisItem> items = new ArrayList<AnalysisItem>(listDefinition.getAllAnalysisItems());
        items.remove(null);
        Collections.sort(items, new Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        });
        for (short i = 0; i < items.size(); i++) {
            AnalysisItem analysisItem = items.get(i);
            positionMap.put(analysisItem, i);
        }
        for (AnalysisItem analysisItem : listDataResults.getHeaders()) {
            int headerPosition = positionMap.get(analysisItem);
            if (analysisItem.getWidth() > 0) {
                sheet.setColumnWidth(headerPosition, (short) (analysisItem.getWidth() / 15 * 256));
            } else {
                sheet.setColumnWidth(headerPosition, 7000);
            }
            HSSFCell headerCell = headerRow.createCell(headerPosition);
            String displayName;
            if (analysisItem.getDisplayName() == null) {
                displayName = analysisItem.getKey().toDisplayName();
            } else {
                displayName = analysisItem.getDisplayName();
            }
            headerCell.setCellValue(new HSSFRichTextString(displayName));
            Font font = workbook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);
            headerCell.setCellStyle(cellStyle);
        }

        int i = 1;
        for (ListRow listRow : listDataResults.getRows()) {
            HSSFRow row = sheet.createRow(i);
            Value[] values = listRow.getValues();
            short cellIndex = 0;
            for (Value value : values) {
                AnalysisItem analysisItem = listDataResults.getHeaders()[cellIndex];
                short translatedIndex = positionMap.get(analysisItem);
                HSSFCellStyle style = getStyle(styleMap, analysisItem, workbook, dateFormat);
                populateCell(row, translatedIndex, value, style);
                cellIndex++;
            }
            i++;
        }
        return workbook;
    }

    private HSSFCellStyle getStyle(Map<String, HSSFCellStyle> styleMap, AnalysisItem analysisItem, HSSFWorkbook wb, int dateFormat) {
        HSSFCellStyle style;
        if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            FormattingConfiguration formattingConfiguration = analysisItem.getFormattingConfiguration();
            switch (formattingConfiguration.getFormattingType()) {
                case FormattingConfiguration.CURRENCY:
                    style = styleMap.get(CURRENCY_STYLE);
                    break;
                default:
                    style = styleMap.get(GENERIC_STYLE);
                    break;
            }
        } else if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
            style = styleMap.get(analysisItem.qualifiedName());
            if (style == null) {
                HSSFCellStyle cellStyle = wb.createCellStyle();
                CreationHelper createHelper = wb.getCreationHelper();
                AnalysisDateDimension dateDim = (AnalysisDateDimension) analysisItem;
                if (dateDim.getDateLevel() == AnalysisDateDimension.YEAR_LEVEL) {
                    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("y"));
                } else if (dateDim.getDateLevel() == AnalysisDateDimension.MONTH_LEVEL) {
                    if (dateFormat == 0 || dateFormat == 3) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/y"));
                    } else if (dateFormat == 1) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("y-m"));
                    } else if (dateFormat == 2) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m-y"));
                    } else if (dateFormat == 4) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m.y"));
                    }
                } else if (dateDim.getDateLevel() == AnalysisDateDimension.WEEK_LEVEL ||
                        dateDim.getDateLevel() == AnalysisDateDimension.DAY_LEVEL) {
                    if (dateFormat == 0) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/y"));
                    } else if (dateFormat == 1) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("y-m-d"));
                    } else if (dateFormat == 2) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("d-m-y"));
                    } else if (dateFormat == 3) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("d/m/y"));
                    } else if (dateFormat == 4) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("d.m.y"));
                    }
                } else {
                    cellStyle = styleMap.get(GENERIC_STYLE);
                }
                styleMap.put(analysisItem.qualifiedName(), cellStyle);
                style = cellStyle;
            }
        } else {
            style = styleMap.get(GENERIC_STYLE);
        }
        return style;
    }

    private void populateCell(HSSFRow row, int cellIndex, Value value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(cellIndex);
        cell.setCellStyle(style);
        if (value.type() == Value.STRING) {
            StringValue stringValue = (StringValue) value;
            HSSFRichTextString richText = new HSSFRichTextString(stringValue.getValue());
            cell.setCellValue(richText);
        } else if (value.type() == Value.NUMBER) {
            NumericValue numericValue = (NumericValue) value;
            cell.setCellValue(numericValue.toDouble());
        } else if (value.type() == Value.DATE) {
            DateValue dateValue = (DateValue) value;
            cell.setCellValue(dateValue.getDate());
        }
    }
}
