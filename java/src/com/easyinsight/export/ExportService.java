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
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import flex.messaging.FlexContext;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

import java.net.MalformedURLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * User: James Boe
 * Date: Jun 2, 2008
 * Time: 4:26:26 PM
 */
public class ExportService {

    public static final String CURRENCY_STYLE = "currency";
    public static final String GENERIC_STYLE = "generic";
    
    public void seleniumDraw(long requestID, byte[] bytes) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE SELENIUM_REQUEST SET RESULT_BYTES = ? WHERE SELENIUM_REQUEST_ID = ?");
            updateStmt.setBytes(1, bytes);
            updateStmt.setLong(2, requestID);
            updateStmt.executeUpdate();
            conn.commit();
            PreparedStatement queryStmt = conn.prepareStatement("SELECT SELENIUM_PROCESSOR_ID, ACCOUNT_ID FROM SELENIUM_REQUEST WHERE SELENIUM_REQUEST_ID = ?");
            queryStmt.setLong(1, requestID);
            ResultSet rs = queryStmt.executeQuery();
            rs.next();
            long processorID = rs.getLong(1);
            long accountID = rs.getLong(2);
            SeleniumPostProcessor processor = SeleniumPostProcessor.loadProcessor(processorID, conn);
            PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM SELENIUM_REQUEST WHERE SELENIUM_REQUEST_ID = ?");
            clearStmt.setLong(1, requestID);
            clearStmt.executeUpdate();
            processor.process(bytes, conn, accountID);
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

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

    public void addOrUpdateSchedule(ScheduledActivity scheduledActivity, int utcOffset, EIConnection conn) throws SQLException {
        scheduledActivity.save(conn, utcOffset);
        scheduledActivity.setup(conn);        
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

    public ReportDelivery getReportDelivery(long reportID, int utcOffset) {
        ReportDelivery reportDelivery = null;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT SCHEDULED_ACCOUNT_ACTIVITY_ID FROM REPORT_DELIVERY WHERE REPORT_ID = ?");
            queryStmt.setLong(1, reportID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long activityID = rs.getLong(1);
                reportDelivery = (ReportDelivery) ScheduledActivity.createActivity(ScheduledActivity.REPORT_DELIVERY, activityID, conn);
            }
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return reportDelivery;
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
                try {
                    activities.add(ScheduledActivity.createActivity(activityType, activityID, conn));
                } catch (SQLException e) {
                    LogClass.error(e.getMessage() + " on loading activity " + activityID);
                    LogClass.error(e);
                }
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

    public void exportToPDF(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata,
                              byte[] bytes, int width, int height) {
        if (analysisDefinition.getAnalysisID() > 0) SecurityUtil.authorizeInsight(analysisDefinition.getAnalysisID());
        else SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        EIConnection conn = Database.instance().getConnection();
        try {
            if (analysisDefinition.getReportType() == WSAnalysisDefinition.LIST) {
                analysisDefinition.updateMetadata();
                ListDataResults listDataResults = (ListDataResults) new DataService().list(analysisDefinition, insightRequestMetadata);
                toListPDF(analysisDefinition, listDataResults, conn);
            } else {
                toImagePDF(analysisDefinition, bytes, width, height, conn);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private void toListPDF(WSAnalysisDefinition analysisDefinition, ListDataResults listDataResults, EIConnection conn) throws SQLException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PreparedStatement dateFormatStmt = conn.prepareStatement("SELECT DATE_FORMAT FROM ACCOUNT WHERE ACCOUNT_ID = ?");
        dateFormatStmt.setLong(1, SecurityUtil.getAccountID());
        ResultSet rs = dateFormatStmt.executeQuery();
        rs.next();
        int dateFormat = rs.getInt(1);
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, baos);
        document.open();
        analysisDefinition.updateMetadata();
        PdfPTable table = new PdfPTable(listDataResults.getHeaders().length);
        table.setSpacingBefore(20);
        table.getDefaultCell().setPadding(5);
        List<AnalysisItem> items = new ArrayList<AnalysisItem>(analysisDefinition.getAllAnalysisItems());
        items.remove(null);
        Collections.sort(items, new Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        });
        for (AnalysisItem analysisItem : items) {
            for (AnalysisItem headerItem : listDataResults.getHeaders()) {
                if (headerItem == analysisItem) {
                    PdfPCell cell = new PdfPCell(new Phrase(analysisItem.toDisplay()));
                    cell.setFixedHeight(20f);
                    cell.setBackgroundColor(new BaseColor(180, 180, 180));
                    table.addCell(cell);
                }
            }
        }
        table.setHeaderRows(1);

        for (ListRow listRow : listDataResults.getRows()) {
            //PdfPCell[] cells = new PdfPCell[listDataResults.getHeaders().length];
            for (AnalysisItem analysisItem : items) {
                for (int i = 0; i < listDataResults.getHeaders().length; i++) {
                    AnalysisItem headerItem = listDataResults.getHeaders()[i];
                    if (headerItem == analysisItem) {
                        Value value = listRow.getValues()[i];
                        String valueString = createValue(dateFormat, headerItem, value);
                        PdfPCell valueCell = new PdfPCell(new Phrase(valueString));
                        valueCell.setFixedHeight(20f);
                        table.addCell(valueCell);
                        //cells[j] = valueCell;
                    }
                }
            }
        }
        document.add(table);
        document.close();
        toDatabase(analysisDefinition.getName(), baos.toByteArray(), conn);
    }

    private void toImagePDF(WSAnalysisDefinition analysisDefinition, byte[] bytes, int width, int height, EIConnection conn) throws DocumentException, IOException, SQLException {
        Document document;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document = new Document(PageSize.A4.rotate());

        PdfWriter.getInstance(document, baos);
        document.open();
        Image image = Image.getInstance(bytes);
        image.setAlignment(Element.ALIGN_CENTER);
        // ratio = 1.5
        float pageWidth = document.getPageSize().getWidth();
        float ratio = pageWidth / width;
        float adjustedHeight = height * ratio;
        image.scaleAbsolute(pageWidth, adjustedHeight);
        document.add(image);
        document.close();
        toDatabase(analysisDefinition.getName(), baos.toByteArray(), conn);
    }

    public static String createValue(int dateFormat, AnalysisItem headerItem, Value value) {
        String valueString;
        if (headerItem.hasType(AnalysisItemTypes.MEASURE)) {
            FormattingConfiguration formattingConfiguration = headerItem.getFormattingConfiguration();
            if (formattingConfiguration.getFormattingType() == FormattingConfiguration.CURRENCY) {
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
                valueString = currencyFormatter.format(value.toDouble());
            } else {
                valueString = String.valueOf(value.toDouble());
            }
        } else if (headerItem.hasType(AnalysisItemTypes.DATE_DIMENSION) && value.type() == Value.DATE) {
            AnalysisDateDimension dateDim = new AnalysisDateDimension();
            DateFormat sdf = null;
            if (dateDim.getDateLevel() == AnalysisDateDimension.YEAR_LEVEL) {
                sdf = new SimpleDateFormat("yyyy");
            } else if (dateDim.getDateLevel() == AnalysisDateDimension.MONTH_LEVEL) {
                if (dateFormat == 0 || dateFormat == 3) {
                    sdf = new SimpleDateFormat("MM/yyyy");
                } else if (dateFormat == 1) {
                    sdf = new SimpleDateFormat("yyyy-MM");
                } else if (dateFormat == 2) {
                    sdf = new SimpleDateFormat("MM-yyyy");
                } else if (dateFormat == 4) {
                    sdf = new SimpleDateFormat("MM.yyyy");
                }
            } else if (dateDim.getDateLevel() == AnalysisDateDimension.HOUR_LEVEL ||
                    dateDim.getDateLevel() == AnalysisDateDimension.MINUTE_LEVEL) {
                if (dateFormat == 0) {
                    sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm");
                } else if (dateFormat == 1) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                } else if (dateFormat == 2) {
                    sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                } else if (dateFormat == 3) {
                    sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                } else if (dateFormat == 4) {
                    sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                }
            } else {
                if (dateFormat == 0) {
                    sdf = new SimpleDateFormat("MM/dd/yyyy");
                } else if (dateFormat == 1) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                } else if (dateFormat == 2) {
                    sdf = new SimpleDateFormat("dd-MM-yyyy");
                } else if (dateFormat == 3) {
                    sdf = new SimpleDateFormat("dd/MM/yyyy");
                } else if (dateFormat == 4) {
                    sdf = new SimpleDateFormat("dd.MM.yyyy");
                }
            }
            if (sdf == null) {
                throw new RuntimeException("No date format found.");
            }
            DateValue dateValue = (DateValue) value;
            valueString = sdf.format(dateValue.getDate());
        } else {
            valueString = value.toString();
        }
        return valueString;
    }

    public byte[] exportToExcel(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        if (analysisDefinition.getAnalysisID() > 0) SecurityUtil.authorizeInsight(analysisDefinition.getAnalysisID());
        else SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        try {
            analysisDefinition.updateMetadata();
            ListDataResults listDataResults = (ListDataResults) new DataService().list(analysisDefinition, insightRequestMetadata);
            return toExcel(analysisDefinition, listDataResults);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void exportToPNG(String reportName, byte[] bytes) {
        EIConnection conn = Database.instance().getConnection();
        try {
            toDatabase(reportName, bytes, conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private void toDatabase(String reportName, byte[] bytes, EIConnection conn) throws SQLException {
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO PNG_EXPORT (USER_ID, PNG_IMAGE, REPORT_NAME) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        BufferedInputStream bis = new BufferedInputStream(bais, 1024);
        insertStmt.setLong(1, SecurityUtil.getUserID());
        insertStmt.setBinaryStream(2, bis, bytes.length);
        insertStmt.setString(3, reportName == null ? "export" : reportName);
        insertStmt.execute();
        long id = Database.instance().getAutoGenKey(insertStmt);
        FlexContext.getHttpRequest().getSession().setAttribute("imageID", id);
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

            HSSFWorkbook workbook = createWorkbookFromList(analysisDefinition, listDataResults, dateFormat);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            byte[] bytes = baos.toByteArray();
            baos.close();

            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO EXCEL_EXPORT (USER_ID, EXCEL_FILE, REPORT_NAME) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            BufferedInputStream bis = new BufferedInputStream(bais, 1024);
            insertStmt.setLong(1, SecurityUtil.getUserID());
            insertStmt.setBinaryStream(2, bis, bytes.length);
            insertStmt.setString(3, analysisDefinition.getName() == null ? "export" : analysisDefinition.getName());
            insertStmt.execute();
            long id = Database.instance().getAutoGenKey(insertStmt);
            FlexContext.getHttpRequest().getSession().setAttribute("imageID", id);
            return bytes;
        } finally {
            Database.closeConnection(conn);
        }
    }

    public byte[] toExcelEmail(WSAnalysisDefinition analysisDefinition, ListDataResults listDataResults, EIConnection conn) throws IOException, SQLException {

        int dateFormat;
        PreparedStatement dateFormatStmt = conn.prepareStatement("SELECT DATE_FORMAT FROM ACCOUNT WHERE ACCOUNT_ID = ?");
        dateFormatStmt.setLong(1, SecurityUtil.getAccountID());
        ResultSet rs = dateFormatStmt.executeQuery();
        rs.next();
        dateFormat = rs.getInt(1);

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
                populateCell(row, translatedIndex, value, style, analysisItem);
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
                    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy"));
                } else if (dateDim.getDateLevel() == AnalysisDateDimension.MONTH_LEVEL) {
                    if (dateFormat == 0 || dateFormat == 3) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/yyyy"));
                    } else if (dateFormat == 1) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-m"));
                    } else if (dateFormat == 2) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m-yyyy"));
                    } else if (dateFormat == 4) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m.yyyy"));
                    }
                } else if (dateDim.getDateLevel() == AnalysisDateDimension.WEEK_LEVEL ||
                        dateDim.getDateLevel() == AnalysisDateDimension.DAY_LEVEL) {
                    if (dateFormat == 0) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yyyy"));
                    } else if (dateFormat == 1) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-m-d"));
                    } else if (dateFormat == 2) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("d-m-yyyy"));
                    } else if (dateFormat == 3) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("d/m/yyyy"));
                    } else if (dateFormat == 4) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("d.m.yyyy"));
                    }
                } else if (dateDim.getDateLevel() == AnalysisDateDimension.HOUR_LEVEL ||
                        dateDim.getDateLevel() == AnalysisDateDimension.MINUTE_LEVEL) {
                    if (dateFormat == 0) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yyyy hh:mm"));
                    } else if (dateFormat == 1) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-m-d hh:mm"));
                    } else if (dateFormat == 2) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("d-m-yyyy hh:mm"));
                    } else if (dateFormat == 3) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("d/m/yyyy hh:mm"));
                    } else if (dateFormat == 4) {
                        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("d.m.yyyy hh:mm"));
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

    private void populateCell(HSSFRow row, int cellIndex, Value value, HSSFCellStyle style, AnalysisItem analysisItem) {
        HSSFCell cell = row.createCell(cellIndex);
        cell.setCellStyle(style);
        if (value.type() == Value.STRING) {
            StringValue stringValue = (StringValue) value;
            String string = stringValue.getValue();
            if (analysisItem.hasType(AnalysisItemTypes.TEXT)) {
                string = string.replaceAll("\\<.*?\\>", "");
            }
            HSSFRichTextString richText = new HSSFRichTextString(string);
            cell.setCellValue(richText);
        } else if (value.type() == Value.NUMBER) {
            NumericValue numericValue = (NumericValue) value;
            cell.setCellValue(numericValue.toDouble());
        } else if (value.type() == Value.DATE) {
            DateValue dateValue = (DateValue) value;
            cell.setCellValue(dateValue.getDate());
        }
    }

    public static String toTable(WSAnalysisDefinition report, int dateFormat) {
        StringBuilder sb = new StringBuilder();
        report.updateMetadata();
        com.easyinsight.analysis.ListDataResults listDataResults = (com.easyinsight.analysis.ListDataResults) new com.easyinsight.analysis.DataService().list(report, new com.easyinsight.analysis.InsightRequestMetadata());
        java.util.List<AnalysisItem> items = new java.util.ArrayList<AnalysisItem>(report.getAllAnalysisItems());
        items.remove(null);
        java.util.Collections.sort(items, new java.util.Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        });
        for (AnalysisItem analysisItem : items) {
            for (AnalysisItem headerItem : listDataResults.getHeaders()) {
                if (headerItem == analysisItem) {
                    sb.append("<td>");
                    sb.append(headerItem.toDisplay());
                    sb.append("</td>");
                }
            }
        }
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("</tr>");
        for (com.easyinsight.analysis.ListRow listRow : listDataResults.getRows()) {
            sb.append("<tr>");
            for (AnalysisItem analysisItem : items) {
                for (int i = 0; i < listDataResults.getHeaders().length; i++) {
                    AnalysisItem headerItem = listDataResults.getHeaders()[i];
                    if (headerItem == analysisItem) {
                        com.easyinsight.core.Value value = listRow.getValues()[i];
                        sb.append("<td>");
                        sb.append(com.easyinsight.export.ExportService.createValue(dateFormat, headerItem, value));
                        sb.append("</td>");
                    }
                }
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }
}
