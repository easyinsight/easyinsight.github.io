package com.easyinsight.export;

import com.easyinsight.analysis.DataService;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.definitions.WSCombinedVerticalListDefinition;
import com.easyinsight.analysis.definitions.WSVerticalListDefinition;
import com.easyinsight.core.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.email.SendGridEmail;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIOutcome;
import com.easyinsight.scorecard.Scorecard;
import com.easyinsight.scorecard.ScorecardService;
import com.easyinsight.scorecard.ScorecardStorage;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.logging.LogClass;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.analysis.ListRow;
import com.easyinsight.analysis.*;

import com.easyinsight.storage.DataStorage;
import com.easyinsight.util.RandomTextGenerator;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import flex.messaging.FlexContext;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;

import java.io.*;

import java.sql.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
    public static final String PERCENT_STYLE = "percentStyle";

    public void seleniumDraw(long requestID, byte[] bytes) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE SELENIUM_REQUEST SET RESULT_BYTES = ? WHERE SELENIUM_REQUEST_ID = ?");
            updateStmt.setBytes(1, bytes);
            updateStmt.setLong(2, requestID);
            updateStmt.executeUpdate();
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
            processor.process(bytes, conn, accountID, requestID);
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

    public List<DataSourceDescriptor> getRefreshableDataSources(ScheduledActivity scheduledActivity) {
        List<DataSourceDescriptor> validSources = new ArrayList<DataSourceDescriptor>();
        List<DataSourceDescriptor> dataSources = new FeedService().searchForSubscribedFeeds();
        for (DataSourceDescriptor fd : dataSources) {
            if (isRefreshable(fd.getDataSourceType())) {
                validSources.add(fd);
            }
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED_ID, scheduled_account_activity.scheduled_account_activity_id FROM " +
                    "DATA_FEED, SCHEDULED_DATA_SOURCE_REFRESH, scheduled_account_activity WHERE " +
                    "DATA_FEED.data_feed_id = SCHEDULED_DATA_SOURCE_REFRESH.data_source_id and " +
                    "scheduled_data_source_refresh.scheduled_account_activity_id = scheduled_account_activity.scheduled_account_activity_id and " +
                    "scheduled_account_activity.account_id = ?");
            queryStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long dataSourceID = rs.getLong(1);
                long id = rs.getLong(2);
                if (scheduledActivity != null && id == scheduledActivity.getScheduledActivityID()) continue;
                Iterator<DataSourceDescriptor> descIter = validSources.iterator();
                while (descIter.hasNext()) {
                    DataSourceDescriptor fd = descIter.next();
                    if (fd.getId() == dataSourceID) {
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
        return (feedType == FeedType.BASECAMP_MASTER.getType() || feedType == FeedType.HIGHRISE_COMPOSITE.getType() ||
            feedType == FeedType.PIVOTAL_TRACKER.getType() || feedType == FeedType.WHOLE_FOODS.getType() ||
            feedType == FeedType.CONSTANT_CONTACT.getType() || feedType == FeedType.ZENDESK_COMPOSITE.getType() ||
            feedType == FeedType.HARVEST_COMPOSITE.getType() || feedType == FeedType.QUICKBASE_COMPOSITE.getType() ||
            feedType == FeedType.LINKEDIN.getType() || feedType == FeedType.BATCHBOOK_COMPOSITE.getType());
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
                } catch (Exception e) {
                    //LogClass.error(e);
                    // blah
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

    public void exportToPDF(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata,
                              byte[] bytes, int width, int height) {
        if (analysisDefinition.getAnalysisID() > 0) SecurityUtil.authorizeInsight(analysisDefinition.getAnalysisID());
        else SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        EIConnection conn = Database.instance().getConnection();
        try {
            if (analysisDefinition.getReportType() == WSAnalysisDefinition.LIST || analysisDefinition.getReportType() == WSAnalysisDefinition.TREE ||
                    analysisDefinition.getReportType() == WSAnalysisDefinition.CROSSTAB) {
                analysisDefinition.updateMetadata();
                ListDataResults listDataResults = (ListDataResults) DataService.list(analysisDefinition, insightRequestMetadata, conn);
                toListPDFInDatabase(analysisDefinition, listDataResults, conn, insightRequestMetadata);
            } else {
                toImagePDFDatabase(analysisDefinition, bytes, width, height, conn);
            }
        } catch (Exception e) {
            LogClass.error(e.getMessage() + " on saving report " + analysisDefinition.getAnalysisID() + " - " + analysisDefinition.getReportType(), e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void toListPDFInDatabase(WSAnalysisDefinition analysisDefinition, ListDataResults listDataResults, EIConnection conn, InsightRequestMetadata insightRequestMetadata) throws SQLException, DocumentException {
        toDatabase(analysisDefinition.getName(), toListPDF(analysisDefinition, listDataResults, conn, insightRequestMetadata), conn);
    }

    public void emailReport(WSAnalysisDefinition analysisDefinition, int format, InsightRequestMetadata insightRequestMetadata, String email, String subject, String body) {
        if (analysisDefinition.getAnalysisID() > 0) SecurityUtil.authorizeInsight(analysisDefinition.getAnalysisID());
        else SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        EIConnection conn = Database.instance().getConnection();
        String name = analysisDefinition.getName() != null ? analysisDefinition.getName() : "report";
        try {
            analysisDefinition.updateMetadata();
            if (format == ReportDelivery.EXCEL) {
                byte[] bytes = toExcel(analysisDefinition, insightRequestMetadata);
                    new SendGridEmail().sendAttachmentEmail(email, subject, body, bytes, name + ".xls", false, "reports@easy-insight.com", "Easy Insight",
                            "application/excel");
            } else if (format == ReportDelivery.HTML_TABLE) {
                String html;
                if (analysisDefinition.getReportType() == WSAnalysisDefinition.VERTICAL_LIST) {
                    DataSet dataSet = DataService.listDataSet(analysisDefinition, insightRequestMetadata, conn);
                    html = ExportService.argh(analysisDefinition, dataSet, conn, insightRequestMetadata);
                } else if (analysisDefinition.getReportType() == WSAnalysisDefinition.VERTICAL_LIST_COMBINED) {
                    List<DataSet> dataSets = DataService.getEmbeddedVerticalDataSets((WSCombinedVerticalListDefinition) analysisDefinition,
                            insightRequestMetadata, conn);
                    html = ExportService.argh2(analysisDefinition, dataSets, conn, insightRequestMetadata);
                } else if (analysisDefinition.getReportType() == WSAnalysisDefinition.CROSSTAB) {
                    DataSet dataSet = DataService.listDataSet(analysisDefinition, insightRequestMetadata, conn);
                    html = ExportService.crosstab(analysisDefinition, dataSet, conn, insightRequestMetadata);
                } else {
                    ListDataResults listDataResults = (ListDataResults) DataService.list(analysisDefinition, insightRequestMetadata, conn);
                    html = ExportService.toTable(analysisDefinition, listDataResults, conn, insightRequestMetadata);
                }
                String htmlBody = body + html;
                new SendGridEmail().sendNoAttachmentEmail(email, subject, htmlBody, true, "reports@easy-insight.com", "Easy Insight");
            } else if (format == ReportDelivery.PDF) {
                ListDataResults listDataResults = (ListDataResults) DataService.list(analysisDefinition, insightRequestMetadata, conn);
                byte[] bytes = new ExportService().toListPDF(analysisDefinition, listDataResults, conn, insightRequestMetadata);
                new SendGridEmail().sendAttachmentEmail(email, subject, body, bytes, name + ".pdf", false, "reports@easy-insight.com", "Easy Insight",
                        "application/pdf");
            }

        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private static String crosstab(WSAnalysisDefinition analysisDefinition, DataSet dataSet, EIConnection conn, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(), conn, insightRequestMetadata);
        WSCrosstabDefinition crosstabDefinition = (WSCrosstabDefinition) analysisDefinition;
        Crosstab crosstab = new Crosstab();
        crosstab.crosstab(crosstabDefinition, dataSet);
        CrosstabValue[][] values = crosstab.toTable(crosstabDefinition);
        StringBuilder sb = new StringBuilder();
        AnalysisMeasure measure = (AnalysisMeasure) crosstabDefinition.getMeasures().get(0);
        sb.append("<table>\r\n");
        for (int j = 0; j < (crosstab.getRowSections().size() + crosstabDefinition.getColumns().size()); j++) {
            if (j < crosstabDefinition.getColumns().size()) {
                sb.append("<tr style=\"background: #333333; color: #FFFFFF\">");
            } else {
                sb.append("<tr>");
            }
            for (int i = 0; i < (crosstab.getColumnSections().size() + crosstabDefinition.getRows().size()); i++) {
                CrosstabValue crosstabValue = values[j][i];
                if (crosstabValue == null) {
                    sb.append("<td></td>");
                } else {
                    if (crosstabValue.getHeader() == null) {
                        sb.append("<td>");
                        sb.append(createValue(exportMetadata.dateFormat, measure, crosstabValue.getValue(), exportMetadata.cal, exportMetadata.currencySymbol));
                    } else {
                        sb.append("<td style=\"background: #333333; color: #FFFFFF\">");
                        sb.append(crosstabValue.getValue());
                    }
                    sb.append("</td>");
                }
            }
            sb.append("</tr>\r\n");
            System.out.println();
        }
        sb.append("</table>\r\n");

        return sb.toString();
    }

    public byte[] toListPDF(WSAnalysisDefinition analysisDefinition, ListDataResults listDataResults, EIConnection conn, InsightRequestMetadata insightRequestMetadata) throws SQLException, DocumentException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(), conn, insightRequestMetadata);
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

        if (listDataResults.getRows() == null || listDataResults.getRows().length == 0) {
            for (AnalysisItem analysisItem : items) {
                for (int i = 0; i < listDataResults.getHeaders().length; i++) {
                    AnalysisItem headerItem = listDataResults.getHeaders()[i];
                    if (headerItem == analysisItem) {
                        PdfPCell valueCell = new PdfPCell(new Phrase(""));
                        valueCell.setFixedHeight(20f);
                        table.addCell(valueCell);
                    }
                }
            }
        } else {
            for (ListRow listRow : listDataResults.getRows()) {
                //PdfPCell[] cells = new PdfPCell[listDataResults.getHeaders().length];
                for (AnalysisItem analysisItem : items) {
                    for (int i = 0; i < listDataResults.getHeaders().length; i++) {
                        AnalysisItem headerItem = listDataResults.getHeaders()[i];
                        if (headerItem == analysisItem) {
                            Value value = listRow.getValues()[i];
                            String valueString = createValue(exportMetadata.dateFormat, headerItem, value, exportMetadata.cal, exportMetadata.currencySymbol);
                            PdfPCell valueCell = new PdfPCell(new Phrase(valueString));
                            valueCell.setFixedHeight(20f);
                            table.addCell(valueCell);
                            //cells[j] = valueCell;
                        }
                    }
                }
            }
        }
        document.add(table);
        document.close();
        return baos.toByteArray();
    }

    public void toImagePDFDatabase(WSAnalysisDefinition analysisDefinition, byte[] bytes, int width, int height, EIConnection conn) throws IOException, DocumentException, SQLException {
        toDatabase(analysisDefinition.getName(), toImagePDF(bytes, width, height), conn);
    }

    public byte[] toImagePDF(byte[] bytes, int width, int height) throws DocumentException, IOException, SQLException {
        Document document;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document = new Document(PageSize.A4.rotate());

        PdfWriter.getInstance(document, baos);
        document.open();
        Image image = Image.getInstance(bytes);
        image.setAlignment(Element.ALIGN_CENTER);
        // ratio = 1.5
        System.out.println("pdf width = " + document.getPageSize().getWidth());
        System.out.println("pdf height = " + document.getPageSize().getHeight());
        float pageWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
        float pageHeight = document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin();
        //float ratio = pageWidth / width;
        //float adjustedHeight = height * ratio;
        System.out.println("inbound " + width + " and " + height);
        System.out.println("scaling to " + pageWidth + " and " + pageHeight);
        image.scaleAbsolute(pageWidth, pageHeight);
        document.add(image);
        document.close();
        return baos.toByteArray();
    }

    public static String createValue(int dateFormat, AnalysisItem headerItem, Value value, Calendar cal, String currencySymbol) {
        String valueString;
        if (headerItem.hasType(AnalysisItemTypes.MEASURE)) {
            AnalysisMeasure analysisMeasure = (AnalysisMeasure) headerItem;
            double doubleValue = value.toDouble();
            if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
                doubleValue = 0.;
            }
            FormattingConfiguration formattingConfiguration = headerItem.getFormattingConfiguration();
            if (formattingConfiguration.getFormattingType() == FormattingConfiguration.CURRENCY) {
                NumberFormat currencyFormatter = new DecimalFormat(currencySymbol + "###,###.##");
                valueString = currencyFormatter.format(doubleValue);
            } else {
                NumberFormat numberFormat = NumberFormat.getNumberInstance();
                numberFormat.setMaximumFractionDigits(analysisMeasure.getPrecision());
                valueString = numberFormat.format(doubleValue);
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
            if (dateDim.isTimeshift()) {
                sdf.setCalendar(cal);
            }
            valueString = sdf.format(dateValue.getDate());
        } else {
            valueString = value.toString();
        }
        return valueString;
    }

    public byte[] exportToExcel(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        if (analysisDefinition.getAnalysisID() > 0) SecurityUtil.authorizeInsight(analysisDefinition.getAnalysisID());
        else SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        EIConnection conn = Database.instance().getConnection();
        try {
            analysisDefinition.updateMetadata();
            return toExcel(analysisDefinition, insightRequestMetadata);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
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
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO PNG_EXPORT (USER_ID, PNG_IMAGE, REPORT_NAME, ANONYMOUS_ID) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        BufferedInputStream bis = new BufferedInputStream(bais, 1024);
        long userID = SecurityUtil.getUserID(false);
        if (userID == 0) {
            insertStmt.setNull(1, Types.BIGINT);
        } else {
            insertStmt.setLong(1, SecurityUtil.getUserID());
        }
        insertStmt.setBinaryStream(2, bis, bytes.length);
        insertStmt.setString(3, reportName == null ? "export" : reportName);
        String anonID = RandomTextGenerator.generateText(20);
        insertStmt.setString(4, anonID);
        insertStmt.execute();
        long id = Database.instance().getAutoGenKey(insertStmt);
        FlexContext.getHttpRequest().getSession().setAttribute("imageID", id);
        FlexContext.getHttpRequest().getSession().setAttribute("anonID", anonID);
    }

    public byte[] toExcel(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) throws IOException, SQLException {
        EIConnection conn = Database.instance().getConnection();
        try {
            ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(), conn, insightRequestMetadata);

            HSSFWorkbook workbook = createWorkbookFromList(analysisDefinition, exportMetadata, conn, insightRequestMetadata);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            byte[] bytes = baos.toByteArray();
            baos.close();

            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO EXCEL_EXPORT (USER_ID, EXCEL_FILE, REPORT_NAME, ANONYMOUS_ID) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            BufferedInputStream bis = new BufferedInputStream(bais, 1024);
            long userID = SecurityUtil.getUserID(false);
            if (userID == 0) {
                insertStmt.setNull(1, Types.BIGINT);
            } else {
                insertStmt.setLong(1, userID);
            }
            insertStmt.setBinaryStream(2, bis, bytes.length);
            String anonID = RandomTextGenerator.generateText(20);
            insertStmt.setString(3, (analysisDefinition.getName() == null || "".equals(analysisDefinition.getName())) ? "export" : analysisDefinition.getName());
            insertStmt.setString(4, anonID);
            insertStmt.execute();
            long id = Database.instance().getAutoGenKey(insertStmt);
            FlexContext.getHttpRequest().getSession().setAttribute("imageID", id);
            FlexContext.getHttpRequest().getSession().setAttribute("anonID", anonID);
            return bytes;
        } finally {
            Database.closeConnection(conn);
        }
    }

    public byte[] toExcelEmail(WSAnalysisDefinition analysisDefinition, EIConnection conn, InsightRequestMetadata insightRequestMetadata) throws IOException, SQLException {
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(), conn, insightRequestMetadata);
        HSSFWorkbook workbook = createWorkbookFromList(analysisDefinition, exportMetadata, conn, insightRequestMetadata);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        byte[] bytes = baos.toByteArray();
        baos.close();
        return bytes;
    }

    private static VListInfo getCombinedVListInfo(WSCombinedVerticalListDefinition verticalList, List<DataSet> dataSets) {
        List<Map<String, Object>> dColl = new ArrayList<Map<String, Object>>();
        Set<SortInfo> columnSet = new HashSet<SortInfo>();
        WSVerticalListDefinition vertReport = (WSVerticalListDefinition) verticalList.getReports().get(0);
        for (AnalysisItem measureItem : vertReport.getMeasures()) {
            AnalysisMeasure analysisMeasure = (AnalysisMeasure) measureItem;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Label", analysisMeasure.toDisplay());
            map.put("baseMeasure", analysisMeasure);
            boolean atLeastOneValue = false;
            for (int i = 0; i < verticalList.getReports().size(); i++) {
                WSVerticalListDefinition vert = (WSVerticalListDefinition) verticalList.getReports().get(i);
                AnalysisMeasure applyMeasure = null;
                for (AnalysisItem measure : vert.getMeasures()) {
                    if (measure.toDisplay().equals(analysisMeasure.toDisplay())) {
                        applyMeasure = (AnalysisMeasure) measure;
                    }
                }
                if (applyMeasure == null) {
                    continue;
                }
                DataSet dataSet = dataSets.get(i);
                List<Map<String, Value>> valueList = new ArrayList<Map<String, Value>>();
                for (IRow row : dataSet.getRows()) {
                    Map<String, Value> valueMap = new HashMap<String, Value>();
                    for (AnalysisItem aItem : vert.getAllAnalysisItems()) {
                        Value value = row.getValue(aItem);
                        valueMap.put(aItem.toDisplay(), value);
                    }
                    valueList.add(valueMap);
                }
                for (Map<String, Value> row : valueList) {
                    String columnValue;
                    Value value = null;
                    Value sortValue;
                    if (vert.getColumn() == null) {
                        sortValue = new StringValue(vert.getName());
                        columnValue = vert.getName();
                    } else {
                        value = row.get(vert.getColumn().toDisplay());
                        if (value.getSortValue() != null) {
                            sortValue = value.getSortValue();
                        } else {
                            sortValue = value;
                        }
                        columnValue = value.toString();
                    }
                    //if (firstRow) {
                        columnSet.add(new SortInfo(i, sortValue, columnValue));
                    //}
                    if (value != null && value.type() == Value.EMPTY) {
                        continue;
                    }
                    Value measureValue = row.get(applyMeasure.toDisplay());
                    if (measureValue.toDouble() != 0) {
                        atLeastOneValue = true;
                    }
                    map.put(columnValue, measureValue);
                    map.put(columnValue + "measure", applyMeasure);
                }
            }
            if (atLeastOneValue) {
                dColl.add(map);
            }
        }
        List<SortInfo> columns = new ArrayList<SortInfo>(columnSet);
        Collections.sort(columns, new Comparator<SortInfo>() {

            public int compare(SortInfo sortInfo, SortInfo sortInfo1) {
                if (sortInfo.firstSort != sortInfo1.firstSort) {
                    return new Integer(sortInfo.firstSort).compareTo(sortInfo1.firstSort);
                }
                if (sortInfo.secondSort.type() == Value.STRING && sortInfo1.secondSort.type() == Value.STRING) {
                    return sortInfo.secondSort.toString().compareTo(sortInfo1.secondSort.toString());
                } else if (sortInfo.secondSort.type() == Value.NUMBER && sortInfo1.secondSort.type() == Value.NUMBER) {
                    return sortInfo.secondSort.toDouble().compareTo(sortInfo1.secondSort.toDouble());
                }
                return 0;
            }
        });
        return new VListInfo(dColl, columns);
    }

    private static String vListToTable(VListInfo vListInfo, ExportMetadata exportMetadata) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");
        sb.append("<tr style=\"background: #333333; color: #FFFFFF\">");
        sb.append("<td></td>");
        for (SortInfo sortInfo : vListInfo.columns) {
            sb.append("<td>");
            sb.append(sortInfo.label);
            sb.append("</td>");
        }
        sb.append("</tr>");
        for (Map<String, Object> map : vListInfo.dColl) {
            sb.append("<tr>");
            AnalysisMeasure baseMeasure = (AnalysisMeasure) map.get("baseMeasure");
            sb.append("<td>");
            sb.append(baseMeasure.toDisplay());
            sb.append("</td>");
            for (SortInfo sortInfo : vListInfo.columns) {
                String columnName = sortInfo.label;
                AnalysisMeasure analysisMeasure = (AnalysisMeasure) map.get(columnName + "measure");
                if (analysisMeasure != null) {

                }
                sb.append("<td>");
                Value measureValue = (Value) map.get(columnName);
                String text;
                if (measureValue != null && measureValue.type() == Value.NUMBER) {
                    text = ExportService.createValue(exportMetadata.dateFormat, analysisMeasure, measureValue, exportMetadata.cal, exportMetadata.currencySymbol);
                } else {
                    text = "";
                }
                sb.append(text);
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    public static String argh2(WSAnalysisDefinition listDefinition, List<DataSet> dataSets, EIConnection conn, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(), conn, insightRequestMetadata);
        WSCombinedVerticalListDefinition verticalList = (WSCombinedVerticalListDefinition) listDefinition;
        VListInfo vListInfo = getCombinedVListInfo(verticalList, dataSets);
        return vListToTable(vListInfo, exportMetadata);
    }

    private static class VListInfo {
        List<Map<String, Object>> dColl = new ArrayList<Map<String, Object>>();
        List<SortInfo> columns = new ArrayList<SortInfo>();

        private VListInfo(List<Map<String, Object>> dColl, List<SortInfo> columns) {
            this.dColl = dColl;
            this.columns = columns;
        }
    }

    private static VListInfo getVListInfo(WSVerticalListDefinition verticalList, DataSet dataSet) {
        List<Map<String, Object>> dColl = new ArrayList<Map<String, Object>>();
        Set<SortInfo> columnSet = new HashSet<SortInfo>();
        for (AnalysisItem measureItem : verticalList.getMeasures()) {
            AnalysisMeasure analysisMeasure = (AnalysisMeasure) measureItem;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Label", analysisMeasure.toDisplay());
            map.put("baseMeasure", analysisMeasure);
            boolean atLeastOneValue = false;
            for (IRow row : dataSet.getRows()) {
                String columnValue;
                Value value = null;
                Value sortValue;
                if (verticalList.getColumn() == null) {
                    sortValue = new StringValue(verticalList.getName());
                    columnValue = verticalList.getName();
                } else {
                    value = row.getValue(verticalList.getColumn().createAggregateKey());
                    if (value.getSortValue() != null) {
                        sortValue = value.getSortValue();
                    } else {
                        sortValue = value;
                    }
                    columnValue = value.toString();
                }
                //if (firstRow) {
                    columnSet.add(new SortInfo(0, sortValue, columnValue));
                //}
                if (value != null && value.type() == Value.EMPTY) {
                    continue;
                }
                Value measureValue = row.getValue(analysisMeasure.createAggregateKey());
                if (measureValue.toDouble() != 0) {
                    atLeastOneValue = true;
                }
                map.put(columnValue, measureValue);
                map.put(columnValue + "measure", analysisMeasure);
            }
            if (atLeastOneValue) {
                dColl.add(map);
            }
        }
        List<SortInfo> columns = new ArrayList<SortInfo>(columnSet);

        Collections.sort(columns, new Comparator<SortInfo>() {

            public int compare(SortInfo sortInfo, SortInfo sortInfo1) {
                if (sortInfo.firstSort != sortInfo1.firstSort) {
                    return new Integer(sortInfo.firstSort).compareTo(sortInfo1.firstSort);
                }
                if (sortInfo.secondSort.type() == Value.STRING && sortInfo1.secondSort.type() == Value.STRING) {
                    return sortInfo.secondSort.toString().compareTo(sortInfo1.secondSort.toString());
                } else if (sortInfo.secondSort.type() == Value.NUMBER && sortInfo1.secondSort.type() == Value.NUMBER) {
                    return sortInfo.secondSort.toDouble().compareTo(sortInfo1.secondSort.toDouble());
                }
                return 0;
            }
        });
        return new VListInfo(dColl, columns);
    }

    public static String argh(WSAnalysisDefinition listDefinition, DataSet dataSet, EIConnection conn, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(), conn, insightRequestMetadata);
        WSVerticalListDefinition verticalList = (WSVerticalListDefinition) listDefinition;
        VListInfo vListInfo = getVListInfo(verticalList, dataSet);
        return vListToTable(vListInfo, exportMetadata);
    }

    private static class SortInfo {
        private int firstSort;
        private Value secondSort;
        private String label;

        private SortInfo(int firstSort, Value secondSort, String label) {
            this.firstSort = firstSort;
            this.secondSort = secondSort;
            this.label = label;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SortInfo sortInfo = (SortInfo) o;

            if (firstSort != sortInfo.firstSort) return false;
            if (label != null ? !label.equals(sortInfo.label) : sortInfo.label != null) return false;
            if (secondSort != null ? !secondSort.equals(sortInfo.secondSort) : sortInfo.secondSort != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = firstSort;
            result = 31 * result + (secondSort != null ? secondSort.hashCode() : 0);
            result = 31 * result + (label != null ? label.hashCode() : 0);
            return result;
        }
    }

    private HSSFWorkbook createWorkbookFromList(WSAnalysisDefinition listDefinition, ExportMetadata exportMetadata,
                                                EIConnection conn, InsightRequestMetadata insightRequestMetadata) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        Map<String, HSSFCellStyle> styleMap = new HashMap<String, HSSFCellStyle>();
        HSSFCellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.setDataFormat(workbook.createDataFormat().getFormat(exportMetadata.currencySymbol + "##,##0.00"));
        styleMap.put(CURRENCY_STYLE, currencyStyle);
        HSSFCellStyle genericStyle = workbook.createCellStyle();
        genericStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
        styleMap.put(GENERIC_STYLE, genericStyle);
        HSSFCellStyle percentStyle = workbook.createCellStyle();
        percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
        styleMap.put(PERCENT_STYLE, percentStyle);

        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "Data");

        if (listDefinition.getReportType() == WSAnalysisDefinition.VERTICAL_LIST) {
            listVerticalList(listDefinition, exportMetadata, styleMap, sheet, workbook, insightRequestMetadata, conn);
        } else if (listDefinition.getReportType() == WSAnalysisDefinition.VERTICAL_LIST_COMBINED) {
            listCombinedList(listDefinition, exportMetadata, styleMap, sheet, workbook, insightRequestMetadata, conn);
        } else if (listDefinition.getReportType() == WSAnalysisDefinition.CROSSTAB) {
            listCrosstab(listDefinition, exportMetadata, styleMap, sheet, workbook, insightRequestMetadata, conn);
        } else {
            listExcel(listDefinition, workbook, styleMap, sheet, insightRequestMetadata, conn, exportMetadata);
        }
        return workbook;
    }

    private void listCrosstab(WSAnalysisDefinition report, ExportMetadata exportMetadata, Map<String, HSSFCellStyle> styleMap, HSSFSheet sheet, HSSFWorkbook workbook,
                                  InsightRequestMetadata insightRequestMetadata, EIConnection conn) {
        WSCrosstabDefinition crosstabDefinition = (WSCrosstabDefinition) report;
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);
        Crosstab crosstab = new Crosstab();
        crosstab.crosstab(crosstabDefinition, dataSet);
        CrosstabValue[][] values = crosstab.toTable(crosstabDefinition);
        AnalysisMeasure measure = (AnalysisMeasure) crosstabDefinition.getMeasures().get(0);

        for (int i = 0; i < (crosstab.getColumnSections().size() + crosstabDefinition.getRows().size()) + 1; i++) {
            sheet.setColumnWidth(i, 5000);
        }
        for (int j = 0; j < (crosstab.getRowSections().size() + crosstabDefinition.getColumns().size()) + 2; j++) {
            HSSFRow row = sheet.createRow(j);
            for (int i = 0; i < (crosstab.getColumnSections().size() + crosstabDefinition.getRows().size()) + 1; i++) {
                CrosstabValue crosstabValue = values[j][i];
                if (crosstabValue == null) {

                } else {
                    HSSFCell cell = row.createCell(i);
                    if (crosstabValue.getHeader() == null) {
                        Value value = crosstabValue.getValue();
                        HSSFCellStyle style = getStyle(styleMap, measure, workbook, exportMetadata.dateFormat, value);
                        populateCell(row, i, value, style, measure, exportMetadata.cal);
                    } else {
                        cell.setCellValue(new HSSFRichTextString(crosstabValue.getValue().toString()));
                        Font font = workbook.createFont();
                        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                        HSSFCellStyle cellStyle = workbook.createCellStyle();
                        cellStyle.setFont(font);
                        cell.setCellStyle(cellStyle);
                    }
                }
            }
        }
    }

    private void listCombinedList(WSAnalysisDefinition report, ExportMetadata exportMetadata, Map<String, HSSFCellStyle> styleMap, HSSFSheet sheet, HSSFWorkbook workbook,
                                  InsightRequestMetadata insightRequestMetadata, EIConnection conn) {
        WSCombinedVerticalListDefinition verticalList = (WSCombinedVerticalListDefinition) report;
        List<DataSet> dataSets = DataService.getEmbeddedVerticalDataSets(verticalList, insightRequestMetadata, conn);
        VListInfo vListInfo = getCombinedVListInfo(verticalList, dataSets);
        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < vListInfo.columns.size(); i++) {
            SortInfo sortInfo = vListInfo.columns.get(i);
            HSSFCell cell = headerRow.createCell(i + 1);
            cell.setCellValue(sortInfo.label);
        }
        int j = 1;
        for (Map<String, Object> map : vListInfo.dColl) {
            HSSFRow row = sheet.createRow(j++);
            AnalysisMeasure baseMeasure = (AnalysisMeasure) map.get("baseMeasure");
            HSSFCell rowHeaderCell = row.createCell(0);
            rowHeaderCell.setCellValue(baseMeasure.toDisplay());
            for (int i = 0; i < vListInfo.columns.size(); i++) {
                SortInfo sortInfo = vListInfo.columns.get(i);
                Value value = (Value) map.get(sortInfo.label);
                HSSFCellStyle style = getStyle(styleMap, baseMeasure, workbook, exportMetadata.dateFormat, value);
                populateCell(row, i + 1, value, style, baseMeasure, exportMetadata.cal);
            }
        }
    }

    private void listVerticalList(WSAnalysisDefinition report, ExportMetadata exportMetadata, Map<String, HSSFCellStyle> styleMap, HSSFSheet sheet, HSSFWorkbook workbook,
                                  InsightRequestMetadata insightRequestMetadata, EIConnection conn) {
        WSVerticalListDefinition verticalList = (WSVerticalListDefinition) report;
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);
        VListInfo vListInfo = getVListInfo(verticalList, dataSet);
        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < vListInfo.columns.size(); i++) {
            SortInfo sortInfo = vListInfo.columns.get(i);
            HSSFCell cell = headerRow.createCell(i + 1);
            cell.setCellValue(sortInfo.label);
        }
        int j = 1;
        for (Map<String, Object> map : vListInfo.dColl) {
            HSSFRow row = sheet.createRow(j++);
            AnalysisMeasure baseMeasure = (AnalysisMeasure) map.get("baseMeasure");
            HSSFCell rowHeaderCell = row.createCell(0);
            rowHeaderCell.setCellValue(baseMeasure.toDisplay());
            for (int i = 0; i < vListInfo.columns.size(); i++) {
                SortInfo sortInfo = vListInfo.columns.get(i);
                Value value = (Value) map.get(sortInfo.label);
                HSSFCellStyle style = getStyle(styleMap, baseMeasure, workbook, exportMetadata.dateFormat, value);
                populateCell(row, i + 1, value, style, baseMeasure, exportMetadata.cal);
            }
        }
    }

    private void listExcel(WSAnalysisDefinition listDefinition, HSSFWorkbook workbook, Map<String, HSSFCellStyle> styleMap, HSSFSheet sheet,
                           InsightRequestMetadata insightRequestMetadata, EIConnection conn, ExportMetadata exportMetadata) {
        ListDataResults listDataResults = (ListDataResults) DataService.list(listDefinition, insightRequestMetadata, conn);
        if (listDefinition.getReportType() == WSAnalysisDefinition.LIST) {
            WSListDefinition list = (WSListDefinition) listDefinition;
            if (list.isSummaryTotal()) {
                listDataResults.summarize();
            }
        }
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
            int width;
            if (analysisItem.getWidth() > 0) {
                width = Math.max((analysisItem.getWidth() / 15 * 256), 5000);
            } else {
                width = 5000;
            }

            sheet.setColumnWidth(headerPosition, width);
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
                HSSFCellStyle style = getStyle(styleMap, analysisItem, workbook, exportMetadata.dateFormat, value);
                populateCell(row, translatedIndex, value, style, analysisItem, exportMetadata.cal);
                cellIndex++;
            }
            i++;
        }
        if (listDefinition.getReportType() == WSAnalysisDefinition.LIST) {
            WSListDefinition list = (WSListDefinition) listDefinition;
            if (list.isSummaryTotal()) {
                HSSFRow summaryRow = sheet.createRow(i);
                for (int j = 0; j < listDataResults.getHeaders().length; j++) {
                    AnalysisItem analysisItem = listDataResults.getHeaders()[j];
                    if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                        int headerPosition = positionMap.get(analysisItem);
                        double summary = listDataResults.getSummaries()[j];
                        if (Double.isNaN(summary) || Double.isInfinite(summary)) {
                            summary = 0;
                        }
                        HSSFCellStyle style = getStyle(styleMap, analysisItem, workbook, exportMetadata.dateFormat, new NumericValue(summary));
                        HSSFCell cell = summaryRow.createCell(headerPosition);
                        cell.setCellStyle(style);
                        cell.setCellValue(summary);
                    }
                }
            }
        }
    }

    private HSSFCellStyle getStyle(Map<String, HSSFCellStyle> styleMap, AnalysisItem analysisItem, HSSFWorkbook wb, int dateFormat, Value value) {
        HSSFCellStyle style;
        if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            FormattingConfiguration formattingConfiguration = analysisItem.getFormattingConfiguration();
            switch (formattingConfiguration.getFormattingType()) {
                case FormattingConfiguration.CURRENCY:
                    style = styleMap.get(CURRENCY_STYLE);
                    break;
                case FormattingConfiguration.MILLISECONDS:
                    style = styleMap.get(GENERIC_STYLE);
                    break;
                default:
                    double doubleValue = value.toDouble();
                    int castInt = (int) doubleValue;
                    if (doubleValue - castInt < 0.0001) {
                        style = styleMap.get(GENERIC_STYLE);
                    } else {
                        style = styleMap.get(PERCENT_STYLE);
                    }
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
        } else if (analysisItem.hasType(AnalysisItemTypes.TEXT)) {
            HSSFCellStyle cellStyle = wb.createCellStyle();
            cellStyle.setWrapText(true);
            style = cellStyle;
        } else {
            style = styleMap.get(GENERIC_STYLE);
        }
        return style;
    }

    private void populateCell(HSSFRow row, int cellIndex, Value value, HSSFCellStyle style, AnalysisItem analysisItem, Calendar cal) {
        HSSFCell cell = row.createCell(cellIndex);
        cell.setCellStyle(style);
        if (value.type() == Value.STRING) {
            StringValue stringValue = (StringValue) value;
            String string = stringValue.getValue();
            if (analysisItem.hasType(AnalysisItemTypes.TEXT)) {
                string = string.replaceAll("\\<.*?\\>", "");
            }
            if (string.length() > 15000) {
                string = string.substring(0, 15000);
            }
            HSSFRichTextString richText = new HSSFRichTextString(string);
            cell.setCellValue(richText);
        } else if (value.type() == Value.NUMBER) {
            NumericValue numericValue = (NumericValue) value;
            double doubleValue = numericValue.toDouble();
            if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
                doubleValue = 0;
            }
            if (analysisItem.getFormattingConfiguration().getFormattingType() == FormattingConfiguration.MILLISECONDS) {
                String result;

                if (doubleValue < 60000) {
                    int seconds = (int) (doubleValue / 1000);
                    int milliseconds = (int) (doubleValue % 1000);
                    result = seconds + "s:" + milliseconds + "ms";
                } else if (doubleValue < (60000 * 60)) {
                    int minutes = (int) (doubleValue / 60000);
                    int seconds = (int) (doubleValue / 1000) % 60;
                    result = minutes + "m: " +seconds + "s";
                } else if (doubleValue < (60000 * 60 * 24)) {
                    int hours = (int) (doubleValue / (60000 * 60));
                    int minutes = (int) (doubleValue % 24);
                    result = hours + "h:" + minutes + "m";
                } else {
                    int days = (int) (doubleValue / (60000 * 60 * 24));
                    int hours = (int) (doubleValue / (60000 * 60) % 24);
                    result = days + "d:" + hours + "h";
                }
                cell.setCellValue(result);
            } else {
                cell.setCellValue(doubleValue);
            }
        } else if (value.type() == Value.DATE) {
            DateValue dateValue = (DateValue) value;
            if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                AnalysisDateDimension dateDim = (AnalysisDateDimension) analysisItem;
                if (dateDim.isTimeshift()) {
                    cal.setTime(dateValue.getDate());
                    cell.setCellValue(cal);
                } else {
                    cell.setCellValue(dateValue.getDate());
                }
            } else {
                cell.setCellValue(dateValue.getDate());
            }
        }
    }

    private static class ExportMetadata {
        int dateFormat;
        String currencySymbol;
        Calendar cal;

        private ExportMetadata(int dateFormat, String currencySymbol, Calendar cal) {
            this.dateFormat = dateFormat;
            this.currencySymbol = currencySymbol;
            this.cal = cal;
        }
    }

    private static ExportMetadata createExportMetadata(long accountID, EIConnection conn, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        int dateFormat;
        String currencySymbol;
        try {
            PreparedStatement dateFormatStmt = conn.prepareStatement("SELECT DATE_FORMAT, CURRENCY_SYMBOL FROM ACCOUNT WHERE ACCOUNT_ID = ?");
            dateFormatStmt.setLong(1, accountID);
            ResultSet rs = dateFormatStmt.executeQuery();
            rs.next();
            dateFormat = rs.getInt(1);
            currencySymbol = rs.getString(2);
        } catch (com.easyinsight.security.SecurityException e) {
            dateFormat = 1;
            currencySymbol = "$";
        }
        int time = insightRequestMetadata.getUtcOffset() / 60;
        String string;
        if (time > 0) {
            string = "GMT-"+Math.abs(time);
        } else if (time < 0) {
            string = "GMT+"+Math.abs(time);
        } else {
            string = "GMT";
        }
        TimeZone timeZone = TimeZone.getTimeZone(string);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(timeZone);
        return new ExportMetadata(dateFormat, currencySymbol, cal);
    }

    public static String toTable(WSAnalysisDefinition report, ListDataResults listDataResults, EIConnection conn, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        if (listDataResults.getReportFault() != null) {
            return listDataResults.getReportFault().toString();
        }

        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(), conn, insightRequestMetadata);

        StringBuilder sb = new StringBuilder();
        java.util.List<AnalysisItem> items = new java.util.ArrayList<AnalysisItem>(report.getAllAnalysisItems());
        items.remove(null);
        java.util.Collections.sort(items, new java.util.Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        });
        String style = "style=\"font-size:" + report.getFontSize() + "px;font-family:" + report.getFontName() + ",serif;border-style:solid;border-width:1px;border-spacing:0\"";
        sb.append("<table ").append(style).append(">");
        sb.append("<tr style=\"background-color:#EEEEEE\">");
        for (AnalysisItem analysisItem : items) {
            for (AnalysisItem headerItem : listDataResults.getHeaders()) {
                if (headerItem == analysisItem) {
                    sb.append("<td style=\"border-style:solid;border-width:1px\">");
                    sb.append(headerItem.toDisplay());
                    sb.append("</td>");
                }
            }
        }
        sb.append("</tr>");
        for (com.easyinsight.analysis.ListRow listRow : listDataResults.getRows()) {
            sb.append("<tr>");
            for (AnalysisItem analysisItem : items) {
                for (int i = 0; i < listDataResults.getHeaders().length; i++) {
                    AnalysisItem headerItem = listDataResults.getHeaders()[i];
                    if (headerItem == analysisItem) {
                        com.easyinsight.core.Value value = listRow.getValues()[i];
                        sb.append("<td style=\"border-style:solid;border-width:1px\">");
                        sb.append(com.easyinsight.export.ExportService.createValue(exportMetadata.dateFormat, headerItem, value, exportMetadata.cal, exportMetadata.currencySymbol));
                        sb.append("</td>");
                    }
                }
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    public void exportScorecardToXLS(long scorecardID, InsightRequestMetadata insightRequestMetadata) throws Exception {
        SecurityUtil.authorizeScorecard(scorecardID);
        EIConnection conn = Database.instance().getConnection();
        try {
            ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(), conn, insightRequestMetadata);
            Scorecard scorecard = new ScorecardStorage().getScorecard(scorecardID, conn);
            List<KPIOutcome> outcomes = new ScorecardService().getValues(scorecard.getKpis(), conn, insightRequestMetadata);
            for (KPI kpi : scorecard.getKpis()) {
                for (KPIOutcome outcome : outcomes) {
                    if (kpi.getName().equals(outcome.getKpiName())) {
                        kpi.setKpiOutcome(outcome);
                        break;
                    }
                }
            }
            HSSFWorkbook workbook = new HSSFWorkbook();
            Map<String, HSSFCellStyle> styleMap = new HashMap<String, HSSFCellStyle>();
            HSSFCellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(workbook.createDataFormat().getFormat(exportMetadata.currencySymbol+"##,##0.00"));
            styleMap.put(CURRENCY_STYLE, currencyStyle);
            HSSFCellStyle genericStyle = workbook.createCellStyle();
            genericStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
            styleMap.put(GENERIC_STYLE, genericStyle);
            HSSFCellStyle percentStyle = workbook.createCellStyle();
            percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
            styleMap.put(PERCENT_STYLE, percentStyle);

            HSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(0, "Data");
            sheet.setColumnWidth(0, 12000);
            sheet.setColumnWidth(1, 4000);
            sheet.setColumnWidth(2, 4000);
            sheet.setColumnWidth(3, 4000);
            Font font = workbook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);
            HSSFRow headerRow = sheet.createRow(0);
            {
                HSSFCell kpiNameCell = headerRow.createCell(0);
                kpiNameCell.setCellValue(new HSSFRichTextString("KPI Name"));
                kpiNameCell.setCellStyle(cellStyle);
                HSSFCell kpiValueCell = headerRow.createCell(1);
                kpiValueCell.setCellValue(new HSSFRichTextString("KPI Value"));
                kpiValueCell.setCellStyle(cellStyle);
                HSSFCell kpiTimeCell = headerRow.createCell(2);
                kpiTimeCell.setCellValue(new HSSFRichTextString("Time"));
                kpiTimeCell.setCellStyle(cellStyle);
                HSSFCell kpiChangeCell = headerRow.createCell(3);
                kpiChangeCell.setCellValue(new HSSFRichTextString("KPI Change"));
                kpiChangeCell.setCellStyle(cellStyle);
            }
            int i = 1;
            for (KPI kpi : scorecard.getKpis()) {
                HSSFRow kpiRow = sheet.createRow(i++);
                HSSFCell kpiNameCell = kpiRow.createCell(0);
                kpiNameCell.setCellValue(new HSSFRichTextString(kpi.getName()));
                HSSFCell kpiValueCell = kpiRow.createCell(1);
                kpiValueCell.setCellValue(kpi.getKpiOutcome().getOutcomeValue());
                HSSFCellStyle style = getStyle(styleMap, kpi.getAnalysisMeasure(), workbook, 0, new NumericValue(kpi.getKpiOutcome().getOutcomeValue()));
                kpiValueCell.setCellStyle(style);
                HSSFCell timeCell = kpiRow.createCell(2);
                timeCell.setCellValue(kpi.getDayWindow());
                HSSFCell percentCell = kpiRow.createCell(3);
                if (kpi.getKpiOutcome().getPercentChange() == null) {
                    percentCell.setCellValue("");
                } else {
                    percentCell.setCellStyle(percentStyle);
                    percentCell.setCellValue(kpi.getKpiOutcome().getPercentChange() / 100);
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            byte[] bytes = baos.toByteArray();
            baos.close();
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO EXCEL_EXPORT (USER_ID, EXCEL_FILE, REPORT_NAME, ANONYMOUS_ID) VALUES (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            BufferedInputStream bis = new BufferedInputStream(bais, 1024);
            long userID = SecurityUtil.getUserID(false);
            if (userID == 0) {
                insertStmt.setNull(1, Types.BIGINT);
            } else {
                insertStmt.setLong(1, userID);
            }
            insertStmt.setBinaryStream(2, bis, bytes.length);
            String anonID = RandomTextGenerator.generateText(20);
            insertStmt.setString(3, scorecard.getName() == null ? "export" : scorecard.getName());
            insertStmt.setString(4, anonID);
            insertStmt.execute();
            long id = Database.instance().getAutoGenKey(insertStmt);
            FlexContext.getHttpRequest().getSession().setAttribute("imageID", id);
            FlexContext.getHttpRequest().getSession().setAttribute("anonID", anonID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static String exportScorecard(long scorecardID, InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws Exception {
        SecurityUtil.authorizeScorecard(scorecardID);
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(), conn, insightRequestMetadata);
        Scorecard scorecard = new ScorecardStorage().getScorecard(scorecardID, conn);
        List<KPIOutcome> outcomes = new ScorecardService().getValues(scorecard.getKpis(), conn, insightRequestMetadata);
        for (KPI kpi : scorecard.getKpis()) {
            for (KPIOutcome outcome : outcomes) {
                if (kpi.getName().equals(outcome.getKpiName())) {
                    kpi.setKpiOutcome(outcome);
                    break;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        String style = "style=\"width:100%;font-size:12px;font-family:\"Lucida Sans Unicode\", \"Lucida Grande\", Sans-Serif,serif;border-collapse:collapse;text-align:left\"";
        sb.append("<table ").append(style).append(">");
        sb.append("<thead>");
        sb.append("<tr style=\"\">");
        sb.append("<th style=\"font-size: 14px;font-weight: normal;padding: 10px 8px;color: #039\"></td>");
        sb.append("<th style=\"font-size: 14px;font-weight: normal;padding: 10px 8px;color: #039\">KPI Name</td>");
        sb.append("<th style=\"font-size: 14px;font-weight: normal;padding: 10px 8px;color: #039\">Latest Value</td>");
        sb.append("<th style=\"font-size: 14px;font-weight: normal;padding: 10px 8px;color: #039\">Time</td>");
        sb.append("<th style=\"font-size: 14px;font-weight: normal;padding: 10px 8px;color: #039\">% Change</td>");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        sb.append("</tr>");
        sb.append("</thead>");
        int i = 1;
        for (KPI kpi : scorecard.getKpis()) {
            sb.append("<tr>");
            String tdStyle = "padding: 8px;color: #669;";
            if (i % 2 == 1) {
                tdStyle += "background: #e8edff;";
            }
            sb.append("<td style=\"").append(tdStyle).append("text-align:center\">");
            sb.append("<img src=\"http://www.easy-insight.com/icons/16x16/").append(kpi.getIconImage()).append("\"/>");
            sb.append("</td>");
            sb.append("<td style=\"").append(tdStyle).append("\">");
            sb.append(kpi.getName());
            sb.append("</td>");
            sb.append("<td style=\"").append(tdStyle).append("\">");
            sb.append(createValue(0, kpi.getAnalysisMeasure(), new NumericValue(kpi.getKpiOutcome().getOutcomeValue()), exportMetadata.cal, exportMetadata.currencySymbol));
            sb.append("</td>");
            sb.append("<td style=\"").append(tdStyle).append("\">");
            sb.append(kpi.getDayWindow());
            sb.append(" days");
            sb.append("</td>");
            sb.append("<td style=\"").append(tdStyle).append("\">");
            String percent;
            if (kpi.getKpiOutcome() != null) {
                Double percentChange = kpi.getKpiOutcome().getPercentChange();
                if (percentChange == null || Double.isNaN(percentChange) || Double.isInfinite(percentChange)) {
                    percent = "";
                } else {
                    percent = nf.format(percentChange) + "%";
                }
            } else {
                percent = "";
            }
            sb.append(percent);
            sb.append("</td>");
            sb.append("</tr>");
            i++;
        }
        sb.append("</table>");
        return sb.toString();
    }
}
