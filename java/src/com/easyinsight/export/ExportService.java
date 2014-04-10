package com.easyinsight.export;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.easyinsight.analysis.DataService;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.definitions.*;
import com.easyinsight.calculations.*;
import com.easyinsight.core.*;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.email.SendGridEmail;
import com.easyinsight.html.UIData;
import com.easyinsight.html.Utils;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIOutcome;
import com.easyinsight.pipeline.PipelineData;
import com.easyinsight.preferences.ImageDescriptor;
import com.easyinsight.preferences.PreferencesService;
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
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.mail.smtp.SMTPSendFailedException;
import flex.messaging.FlexContext;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.*;

import java.net.URLEncoder;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * User: James Boe
 * Date: Jun 2, 2008
 * Time: 4:26:26 PM
 */
public class ExportService {

    public static final String CURRENCY_STYLE = "currency";
    public static final String GENERIC_STYLE = "generic";
    public static final String PERCENT_STYLE = "percentStyle";
    public static final String CROSSTAB_HEADER_STYLE = "crosstabHeaderStyle";
    public static final String LIST_HEADER_STYLE = "listHeaderStyle";
    public static final String CROSSTAB_SUMMARY_STYLE = "crosstabSummaryStyle";

    public static String getIconImage(TrendOutcome o) {
        switch (o.getOutcome()) {
            case KPIOutcome.EXCEEDING_GOAL:
            case KPIOutcome.POSITIVE:
                if (o.getDirection() == KPIOutcome.UP_DIRECTION) {
                    return "arrow2_up_green.png";
                } else {
                    return "arrow2_down_green.png";
                }
            case KPIOutcome.NEGATIVE:
                if (o.getDirection() == KPIOutcome.UP_DIRECTION) {
                    return "arrow2_up_red.png";
                } else {
                    return "arrow2_down_red.png";
                }
            case KPIOutcome.NEUTRAL:
                if (o.getDirection() == KPIOutcome.UP_DIRECTION) {
                    return "arrow2_up_blue.png";
                } else if (o.getDirection() == KPIOutcome.DOWN_DIRECTION) {
                    return "arrow2_down_blue.png";
                } else {
                    return "bullet_ball_blue.png";
                }
            case KPIOutcome.NO_DATA:
            default:
                return "bullet_ball_blue.png";
        }
    }

    public static String getFontColor(TrendOutcome o) {
        switch (o.getOutcome()) {
            case KPIOutcome.EXCEEDING_GOAL:
            case KPIOutcome.POSITIVE:
                return "#009900";
            case KPIOutcome.NEGATIVE:
                return "#990000";
            default:
                return "inherit";
        }
    }

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
            System.out.println("received " + requestID);
            SeleniumPostProcessor processor = SeleniumPostProcessor.loadProcessor(processorID, conn);
            PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM SELENIUM_REQUEST WHERE SELENIUM_REQUEST_ID = ?");
            clearStmt.setLong(1, requestID);
            clearStmt.executeUpdate();
            processor.process(bytes, conn, accountID, requestID, processorID);
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

    public void seleniumDashboardDraw(long requestID, List<Page> pages, boolean landscapeOrientation) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT SELENIUM_PROCESSOR_ID, ACCOUNT_ID FROM SELENIUM_REQUEST WHERE SELENIUM_REQUEST_ID = ?");
            queryStmt.setLong(1, requestID);
            ResultSet rs = queryStmt.executeQuery();
            rs.next();
            long processorID = rs.getLong(1);
            System.out.println("received " + requestID);
            SeleniumPostProcessor processor = SeleniumPostProcessor.loadProcessor(processorID, conn);
            PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM SELENIUM_REQUEST WHERE SELENIUM_REQUEST_ID = ?");
            clearStmt.setLong(1, requestID);
            clearStmt.executeUpdate();
            processor.processDashboardPDF(pages, conn, requestID, processorID, landscapeOrientation);
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

    public void archive(long dataSourceID) {

        try {
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSourceID);
            DataStorage dataStorage = DataStorage.readConnection(dataSource.getFields(), dataSource.getDataFeedID());
            try {
                File file = dataStorage.archive(dataSource.getFields(), new InsightRequestMetadata());

                File archiveFile = new File(System.currentTimeMillis() + ".zip");
                FileOutputStream archiveStream = new FileOutputStream(archiveFile);
                ZipOutputStream zos = new ZipOutputStream(archiveStream);
                ZipEntry zipEntry = new ZipEntry("data.csv");
                zos.putNextEntry(zipEntry);
                BufferedOutputStream bufOS = new BufferedOutputStream(zos, 1024);
                byte[] buffer = new byte[1024];
                InputStream bfis = new FileInputStream(file);
                int nBytes;
                while ((nBytes = bfis.read(buffer)) != -1) {
                    bufOS.write(buffer, 0, nBytes);
                }
                bufOS.flush();
                zos.flush();
                zos.closeEntry();

                bfis.close();

                archiveStream.flush();

                zos.close();

                archiveStream.close();

                AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI"));
                s3.putObject(new PutObjectRequest("archival1", "archive.zip", archiveFile));
                archiveFile.delete();
                file.delete();
            } finally {
                dataStorage.closeConnection();
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
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

    public void copy(ScheduledActivity scheduledActivity) {

    }

    public void runNow(ScheduledDelivery scheduledDelivery) {
        EIConnection conn = Database.instance().getConnection();
        try {
            scheduledDelivery.taskNow(conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
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
            if (fd.getDataSourceBehavior() == DataSourceInfo.COMPOSITE_PULL || fd.getDataSourceBehavior() == DataSourceInfo.STORED_PULL) {
                validSources.add(fd);
            }
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED_ID, scheduled_account_activity.scheduled_account_activity_id FROM " +
                    "DATA_FEED, SCHEDULED_DATA_SOURCE_REFRESH, scheduled_account_activity WHERE " +
                    "DATA_FEED.data_feed_id = SCHEDULED_DATA_SOURCE_REFRESH.data_source_id AND " +
                    "scheduled_data_source_refresh.scheduled_account_activity_id = scheduled_account_activity.scheduled_account_activity_id AND " +
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
            queryStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return validSources;
    }

    /*private boolean isRefreshable(int feedType) {
        return (feedType == FeedType.BASECAMP_MASTER.getType() || feedType == FeedType.HIGHRISE_COMPOSITE.getType() ||
            feedType == FeedType.PIVOTAL_TRACKER.getType() || feedType == FeedType.WHOLE_FOODS.getType() ||
            feedType == FeedType.CONSTANT_CONTACT.getType() || feedType == FeedType.ZENDESK_COMPOSITE.getType() ||
            feedType == FeedType.HARVEST_COMPOSITE.getType() || feedType == FeedType.QUICKBASE_COMPOSITE.getType() ||
            feedType == FeedType.LINKEDIN.getType() || feedType == FeedType.BATCHBOOK_COMPOSITE.getType());
    }*/

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
            queryStmt.close();
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
                    ScheduledActivity scheduledActivity = ScheduledActivity.createActivity(activityType, activityID, conn);
                    if (scheduledActivity.authorize()) {
                        activities.add(scheduledActivity);
                    }
                } catch (Exception e) {
                    //LogClass.error(e);
                    // blah
                }
            }
            queryStmt.close();
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

    public ScheduledActivity getActivityByID(long id) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT SCHEDULED_ACCOUNT_ACTIVITY.scheduled_account_activity_id," +
                    "SCHEDULED_ACCOUNT_ACTIVITY.activity_type FROM SCHEDULED_ACCOUNT_ACTIVITY WHERE ACCOUNT_ID = ? AND SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
            queryStmt.setLong(1, SecurityUtil.getAccountID());
            queryStmt.setLong(2, id);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long activityID = rs.getLong(1);
                int activityType = rs.getInt(2);
                try {
                    ScheduledActivity scheduledActivity = ScheduledActivity.createActivity(activityType, activityID, conn);
                    if (scheduledActivity.authorize()) {
                        return scheduledActivity;
                    }
                } catch (Exception e) {
                    //LogClass.error(e);
                    // blah
                }
            }
            queryStmt.close();
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return null;
    }


    public void deleteSchedules(List<Integer> activities) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM SCHEDULED_ACCOUNT_ACTIVITY WHERE " +
                    "scheduled_account_activity_id = ?");
            for (Integer scheduledActivityID : activities) {
                deleteStmt.setLong(1, scheduledActivityID);
                deleteStmt.executeUpdate();
            }
            deleteStmt.close();
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

    /*public long exportDataSourceToCSV(long dataSourceID) {
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
    }*/

    public void exportDashboardToPDF(Dashboard dashboard, List<Page> pages, boolean landscapeOrientation) {
        SecurityUtil.authorizeDashboard(dashboard.getId());
        EIConnection conn = Database.instance().getConnection();
        try {
            byte[] pdfBytes = toImagePDF(pages, landscapeOrientation);
            toDatabase(dashboard.getName(), pdfBytes, conn);
        } catch (Exception e) {
            LogClass.error(e.getMessage() + " on saving PDF of dashboard " + dashboard.getId(), e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void exportToPDF(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata,
                            byte[] bytes, int width, int height) {
        if (analysisDefinition.getAnalysisID() > 0) SecurityUtil.authorizeInsight(analysisDefinition.getAnalysisID());
        else SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        EIConnection conn = Database.instance().getConnection();
        try {
            if (analysisDefinition.getReportType() == WSAnalysisDefinition.LIST || analysisDefinition.getReportType() == WSAnalysisDefinition.TREE ||
                    analysisDefinition.getReportType() == WSAnalysisDefinition.CROSSTAB || analysisDefinition.getReportType() == WSAnalysisDefinition.SUMMARY) {
                analysisDefinition.updateMetadata();
                toListPDFInDatabase(analysisDefinition, conn, insightRequestMetadata);
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

    public void toListPDFInDatabase(WSAnalysisDefinition analysisDefinition, EIConnection conn, InsightRequestMetadata insightRequestMetadata) throws SQLException, DocumentException {
        toDatabase(analysisDefinition.getName(), toPDFBytes(analysisDefinition, conn, insightRequestMetadata), conn);
    }

    public ReportFault emailReport(WSAnalysisDefinition analysisDefinition, int format, InsightRequestMetadata insightRequestMetadata, String email, String subject, String body,
                                   byte[] pdfBytes, int width, int height) {
        boolean includeTitle = true;
        if (analysisDefinition.getAnalysisID() > 0) SecurityUtil.authorizeInsight(analysisDefinition.getAnalysisID());
        else SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        EIConnection conn = Database.instance().getConnection();
        String name = analysisDefinition.getName() != null ? analysisDefinition.getName() : "report";
        try {
            analysisDefinition.updateMetadata();
            if (format == ReportDelivery.EXCEL) {
                byte[] bytes = toExcel(analysisDefinition, insightRequestMetadata, true, false);
                new SendGridEmail().sendAttachmentEmail(email, subject, body, bytes, name + ".xls", false, "reports@easy-insight.com", "Easy Insight",
                        "application/excel");
            } else if (format == ReportDelivery.EXCEL_2007) {
                byte[] bytes = toExcel(analysisDefinition, insightRequestMetadata, true, true);
                new SendGridEmail().sendAttachmentEmail(email, subject, body, bytes, name + ".xls", false, "reports@easy-insight.com", "Easy Insight",
                        "application/excel");
            } else if (format == ReportDelivery.HTML_TABLE) {
                String html;
                if (analysisDefinition.getReportType() == WSAnalysisDefinition.VERTICAL_LIST) {
                    DataSet dataSet = DataService.listDataSet(analysisDefinition, insightRequestMetadata, conn);
                    html = ExportService.verticalListToHTMLTable(analysisDefinition, dataSet, conn, insightRequestMetadata, includeTitle);
                } else if (analysisDefinition.getReportType() == WSAnalysisDefinition.YTD) {
                    html = ExportService.ytdToHTMLTable(analysisDefinition, conn, insightRequestMetadata, includeTitle);
                } else if (analysisDefinition.getReportType() == WSAnalysisDefinition.TREE ||
                        analysisDefinition.getReportType() == WSAnalysisDefinition.SUMMARY) {
                    html = ExportService.treeReportToHTMLTable(analysisDefinition, conn, insightRequestMetadata, includeTitle);
                } else if (analysisDefinition.getReportType() == WSAnalysisDefinition.COMPARE_YEARS) {
                    html = ExportService.compareYearsToHTMLTable(analysisDefinition, conn, insightRequestMetadata, includeTitle);
                } else if (analysisDefinition.getReportType() == WSAnalysisDefinition.CROSSTAB) {
                    DataSet dataSet = DataService.listDataSet(analysisDefinition, insightRequestMetadata, conn);
                    html = ExportService.crosstabReportToHTMLTable(analysisDefinition, dataSet, conn, insightRequestMetadata, includeTitle);
                } else if (analysisDefinition.getReportType() == WSAnalysisDefinition.TREND ||
                        analysisDefinition.getReportType() == WSAnalysisDefinition.TREND_GRID ||
                        analysisDefinition.getReportType() == WSAnalysisDefinition.DIAGRAM) {
                    html = ExportService.kpiReportToHtmlTable(analysisDefinition, conn, insightRequestMetadata, true, includeTitle);
                } else if (analysisDefinition.getReportType() == WSAnalysisDefinition.TEXT) {
                    html = ExportService.textReportToHtml(analysisDefinition, conn, insightRequestMetadata, includeTitle);
                } else {
                    ListDataResults listDataResults = (ListDataResults) DataService.list(analysisDefinition, insightRequestMetadata, conn);
                    html = ExportService.listReportToHTMLTable(analysisDefinition, listDataResults, conn, insightRequestMetadata, includeTitle, new ExportProperties(true, true, null));
                }
                String htmlBody = body + html;
                new SendGridEmail().sendNoAttachmentEmail(email, subject, htmlBody, true, "reports@easy-insight.com", "Easy Insight");
            } else if (format == ReportDelivery.PDF) {
                byte[] result;
                if (analysisDefinition.getReportType() == WSAnalysisDefinition.LIST || analysisDefinition.getReportType() == WSAnalysisDefinition.TREE ||
                        analysisDefinition.getReportType() == WSAnalysisDefinition.CROSSTAB) {
                    analysisDefinition.updateMetadata();
                    result = toPDFBytes(analysisDefinition, conn, insightRequestMetadata);
                } else {
                    result = toImagePDF(pdfBytes, width, height);
                }
                new SendGridEmail().sendAttachmentEmail(email, subject, body, result, name + ".pdf", false, "reports@easy-insight.com", "Easy Insight",
                        "application/pdf");
            }
            return null;
        } catch (ReportException re) {
            return re.getReportFault();
        } catch (SMTPSendFailedException smtpException) {
            if (smtpException.getMessage().contains("Could not send e-mail, max size")) {
                return new ServerError("The report was too large for our email provider to handle.");
            } else {
                LogClass.error(smtpException);
                throw new RuntimeException(smtpException);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static String crosstabReportToHTMLTable(WSAnalysisDefinition analysisDefinition, DataSet dataSet, EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean includeTitle) throws SQLException {
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);
        WSCrosstabDefinition crosstabDefinition = (WSCrosstabDefinition) analysisDefinition;
        Crosstab crosstab = new Crosstab();
        crosstab.crosstab(crosstabDefinition, dataSet);
        CrosstabValue[][] values = crosstab.toTable(crosstabDefinition, insightRequestMetadata, conn);
        StringBuilder sb = new StringBuilder();
        AnalysisMeasure measure = (AnalysisMeasure) crosstabDefinition.getMeasures().get(0);
        String headerBackgroundColor = createHexString(crosstabDefinition.getHeaderBackgroundColor());
        String headerTextColor = createHexString(crosstabDefinition.getHeaderTextColor());
        String summaryBackgroundColor = createHexString(crosstabDefinition.getSummaryBackgroundColor());
        String summaryTextColor = createHexString(crosstabDefinition.getSummaryTextColor());
        String headerCell = "background: " + headerBackgroundColor + ";color: " + headerTextColor + ";" + tdStyle + "left";
        String summaryCell = "background: " + summaryBackgroundColor + ";color: " + summaryTextColor + ";" + tdStyle + "right";
        String dataCell = tdStyle + "right";
        if (includeTitle && analysisDefinition.getName() != null) {
            sb.append("<div style=\"").append(headerLabelStyle).append("\">").append("<h0>").append(analysisDefinition.getName()).append("</h0>").append("</div>");
        }
        sb.append("<table style=\"").append(tableStyle).append("\">\r\n");
        int rowOffset = crosstabDefinition.getMeasures().size() > 1 ? 3 : 2;
        for (int j = 0; j < (crosstab.getRowSections().size() + crosstabDefinition.getColumns().size() + rowOffset); j++) {
            if (j < crosstabDefinition.getColumns().size()) {
                sb.append("<tr>");
            } else {
                sb.append("<tr>");
            }
            for (int i = 0; i < ((crosstab.getColumnSections().size() * crosstabDefinition.getMeasures().size()) + crosstabDefinition.getRows().size() + 1); i++) {
                CrosstabValue crosstabValue = values[j][i];
                if (crosstabValue == null) {
                    if (i == 0 || j < 2) {
                        sb.append("<td style=\"" + headerCell + "\"></td>");
                    } else if (i == crosstab.getColumnSections().size() + crosstabDefinition.getRows().size() &&
                            j == crosstab.getRowSections().size() + crosstabDefinition.getColumns().size() + 1) {
                        sb.append("<td style=\"" + summaryCell + "\"></td>");
                    } else {
                        sb.append("<td style=\"" + headerCell + "\"></td>");
                    }
                } else {
                    if (crosstabValue.getHeader() == null) {
                        if (crosstabValue.isSummaryValue()) {
                            sb.append("<td style=\"" + summaryCell + "\">");
                        } else {
                            sb.append("<td style=\"" + dataCell + "\">");
                        }
                        sb.append(createValue(exportMetadata.dateFormat, measure, crosstabValue.getValue(), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false));
                    } else {
                        sb.append("<td style=\"" + headerCell + "\">");
                        sb.append(crosstabValue.getValue());
                    }
                    sb.append("</td>");
                }
            }
            sb.append("</tr>\r\n");
        }
        sb.append("</table>\r\n");
        return sb.toString();
    }

    @Nullable
    public static String filterTransformForReport(WSAnalysisDefinition report) {
        if (report.getExportString() != null && !"".equals(report.getExportString())) {
            return filterTransform(Arrays.asList(report), new ArrayList<Dashboard>(), report.getExportString());
        }
        return null;
    }

    @Nullable
    public static String filterTransformForReport(WSAnalysisDefinition report, String exportString) {
        return filterTransform(Arrays.asList(report), new ArrayList<Dashboard>(), exportString);
    }

    @Nullable
    public static String filterTransform(List<WSAnalysisDefinition> reports, List<Dashboard> dashboards, String exportString) {
        Map<FilterKey, FilterDefinition> namespaces = new HashMap<FilterKey, FilterDefinition>();
        for (WSAnalysisDefinition report : reports) {
            if (report.getFilterDefinitions() != null) {
                for (FilterDefinition filter : report.getFilterDefinitions()) {
                    namespaces.put(new FilterKey(filter.label(false), report.getName()), filter);
                }
            }
        }
        for (Dashboard dashboard : dashboards) {
            if (dashboard.getFilters() != null) {
                for (FilterDefinition filter : dashboard.getFilters()) {
                    namespaces.put(new FilterKey(filter.label(false), dashboard.getName()), filter);
                }
            }
        }


        try {
            Pattern pattern = Pattern.compile("\\{(.*?)\\}");
            Matcher matcher = pattern.matcher(exportString);
            String end = exportString;
            while (matcher.find()) {
                String g = matcher.group();
                g = g.substring(1, g.length() - 1);
                CalculationTreeNode tree;

                ResolverVisitor visitor;
                tree = CalculationHelper.createTree(g, false);

                visitor = new ResolverVisitor(namespaces, new FunctionFactory(), new HashMap<String, UniqueKey>(), new ArrayList<String>());
                tree.accept(visitor);
                EvaluationVisitor rowVisitor = new EvaluationVisitor();
                tree.accept(rowVisitor);
                Value result = rowVisitor.getResult();
                if (result.type() != Value.EMPTY) {
                    String toReplace = result.toString();
                    end = matcher.replaceAll(toReplace);
                }
            }
            return end;

        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }

    public byte[] toPDFBytes(WSAnalysisDefinition analysisDefinition, EIConnection conn, InsightRequestMetadata insightRequestMetadata) throws SQLException, DocumentException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);

        Document document = new Document(PageSize.A4.rotate(), 0, 0, 5, 5);
        //Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);
        document.open();
        analysisDefinition.updateMetadata();

        // if we have a header...

        try {
            UIData uiData = Utils.createUIData(conn);
            if (uiData.getApplicationSkin() != null && uiData.getApplicationSkin().isReportHeader() && uiData.getHeaderImageDescriptor() != null) {
                ImageDescriptor imageDescriptor = uiData.getHeaderImageDescriptor();
                byte[] bytes = new PreferencesService().getImage(imageDescriptor.getId());
                Image image = Image.getInstance(bytes);
                image.setAlignment(Element.ALIGN_CENTER);
                document.add(image);
            }
        } catch (Exception e) {
            LogClass.error(e);
        }

        try {
            String string = filterTransformForReport(analysisDefinition);
            com.itextpdf.text.Font phraseFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.NORMAL,
                    new BaseColor(20, 20, 20));
            Phrase phrase = new Phrase(string, phraseFont);
            Paragraph paragraph = new Paragraph(phrase);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
        } catch (Exception e) {
            LogClass.error(e);
        }

        if (analysisDefinition.getReportType() == WSAnalysisDefinition.CROSSTAB) {
            crosstabToPDFTable(analysisDefinition, conn, insightRequestMetadata, document, exportMetadata);
        } else if (analysisDefinition.getReportType() == WSAnalysisDefinition.TREND_GRID) {
            kpiReportToPDFTable(analysisDefinition, conn, insightRequestMetadata, document, exportMetadata);
        } else if (analysisDefinition instanceof WSTreeDefinition) {
            treeToPDFTable(analysisDefinition, conn, insightRequestMetadata, document, exportMetadata);
        } else {
            listReportToPDFTable(analysisDefinition, conn, insightRequestMetadata, document, exportMetadata);
        }


        document.close();
        return baos.toByteArray();
    }

    private void listReportToPDFTable(WSAnalysisDefinition analysisDefinition, EIConnection conn, InsightRequestMetadata insightRequestMetadata, Document document,
                                      ExportMetadata exportMetadata) throws DocumentException {

        ListDataResults listDataResults = (ListDataResults) DataService.list(analysisDefinition, insightRequestMetadata, conn);
        PdfPTable table = new PdfPTable(listDataResults.getHeaders().length);
        table.setSpacingBefore(20);
        table.getDefaultCell().setPadding(3);
        List<AnalysisItem> items = new ArrayList<AnalysisItem>(analysisDefinition.getAllAnalysisItems());
        items.remove(null);
        Collections.sort(items, new Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        });
        int fontSize;
        Color color;
        Color headerTextColor;
        if (analysisDefinition instanceof WSListDefinition) {
            WSListDefinition list = (WSListDefinition) analysisDefinition;
            fontSize = list.getFontSize();
            color = new Color(list.getHeaderColor1());
            headerTextColor = new Color(list.getHeaderTextColor());
        } else {
            fontSize = 10;
            color = new Color(200, 200, 200);
            headerTextColor = new Color(0, 0, 0);
        }

        if (fontSize < 4) {
            fontSize = 10;
        } else {
            fontSize -= 2;
        }
        int minimumSize = fontSize + 5;

        int totalWidth = 0;
        for (AnalysisItem analysisItem : items) {
            int widthToUse = analysisItem.getWidth();
            if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension() instanceof TextReportFieldExtension) {
                TextReportFieldExtension ext = (TextReportFieldExtension) analysisItem.getReportFieldExtension();
                if (ext.getFixedWidth() > 0) {
                    widthToUse = ext.getFixedWidth();
                }
            }
            totalWidth += widthToUse;
        }

        if (totalWidth > 0) {
            float[] widths = new float[items.size()];
            int wCtr = 0;
            for (AnalysisItem analysisItem : items) {
                int widthToUse = analysisItem.getWidth();
                if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension() instanceof TextReportFieldExtension) {
                    TextReportFieldExtension ext = (TextReportFieldExtension) analysisItem.getReportFieldExtension();
                    if (ext.getFixedWidth() > 0) {
                        widthToUse = ext.getFixedWidth();
                    }
                }
                float percent = (float) widthToUse / (float) totalWidth;
                if (percent < .01f) {
                    percent = .1f;
                }
                widths[wCtr++] = percent;
            }
            table.setWidths(widths);
        }


        for (AnalysisItem analysisItem : items) {
            for (AnalysisItem headerItem : listDataResults.getHeaders()) {
                if (headerItem == analysisItem) {
                    com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, fontSize, com.itextpdf.text.Font.BOLD,
                            new BaseColor(headerTextColor.getRed(), headerTextColor.getGreen(), headerTextColor.getBlue()));
                    PdfPCell cell = new PdfPCell(new Phrase(analysisItem.toUnqualifiedDisplay(), boldFont));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setMinimumHeight(minimumSize);
                    cell.setBackgroundColor(new BaseColor(color.getRed(), color.getGreen(), color.getBlue()));
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
                        valueCell.setMinimumHeight(minimumSize);
                        table.addCell(valueCell);
                    }
                }
            }
        } else {
            for (ListRow listRow : listDataResults.getRows()) {
                //PdfPCell[] cells = new PdfPCell[listDataResults.getHeaders().length];
                for (AnalysisItem analysisItem : items) {
                    TextReportFieldExtension ext = null;
                    if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension() instanceof TextReportFieldExtension) {
                        ext = (TextReportFieldExtension) analysisItem.getReportFieldExtension();
                    }
                    for (int i = 0; i < listDataResults.getHeaders().length; i++) {
                        AnalysisItem headerItem = listDataResults.getHeaders()[i];
                        if (headerItem == analysisItem) {
                            Value value = listRow.getValues()[i];
                            com.itextpdf.text.Font.FontFamily fontFamily = com.itextpdf.text.Font.FontFamily.HELVETICA;
                            int fontStyle = 0;
                            BaseColor pdfTextColor = null;
                            BaseColor pdfBackgroundColor = null;
                            if (value.getValueExtension() != null && value.getValueExtension() instanceof TextValueExtension) {
                                TextValueExtension textValueExtension = (TextValueExtension) value.getValueExtension();
                                boolean isBold = textValueExtension.isBold();
                                if (isBold) {
                                    fontStyle = com.itextpdf.text.Font.BOLD;
                                }
                                int foregroundColor = textValueExtension.getColor();
                                if (foregroundColor != 0) {
                                    Color c = new Color(foregroundColor);
                                    pdfTextColor = new BaseColor(c.getRed(), c.getGreen(), c.getBlue());
                                }
                                int backgroundColor = textValueExtension.getBackgroundColor();
                                if (backgroundColor != 0) {
                                    Color c = new Color(backgroundColor);
                                    pdfBackgroundColor = new BaseColor(c.getRed(), c.getGreen(), c.getBlue());
                                }
                            }
                            com.itextpdf.text.Font cellFont = new com.itextpdf.text.Font(fontFamily, fontSize, fontStyle, pdfTextColor);
                            String valueString = createValue(exportMetadata.dateFormat, headerItem, value, exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, true);
                            Phrase phrase = new Phrase(valueString, cellFont);
                            PdfPCell valueCell = new PdfPCell(phrase);
                            if (pdfBackgroundColor != null) {
                                valueCell.setBackgroundColor(pdfBackgroundColor);
                            }
                            if (ext != null) {
                                if ("Left".equals(ext.getAlign())) {
                                    valueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                } else if ("Center".equals(ext.getAlign())) {
                                    valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                } else if ("Right".equals(ext.getAlign())) {
                                    valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                }
                            }
                            valueCell.setMinimumHeight(minimumSize);
                            table.addCell(valueCell);
                        }
                    }
                }
            }
        }
        document.add(table);
    }

    private void kpiReportToPDFTable(WSAnalysisDefinition analysisDefinition, EIConnection conn, InsightRequestMetadata insightRequestMetadata, Document document,
                                     ExportMetadata exportMetadata) throws SQLException, DocumentException {
        WSKPIDefinition kpiDefinition = (WSKPIDefinition) analysisDefinition;


        TrendDataResults trendDataResults = DataService.getTrendDataResults(kpiDefinition, insightRequestMetadata, conn);
        PdfPTable table = new PdfPTable(kpiDefinition.getGroupings().size() + 4);
        table.setSpacingBefore(20);
        table.getDefaultCell().setPadding(5);
        table.setHeaderRows(1);
        List<TrendOutcome> outcomes = trendDataResults.getTrendOutcomes();
        int i;
        if (kpiDefinition.getGroupings() != null) {
            for (i = 0; i < kpiDefinition.getGroupings().size(); i++) {
                AnalysisItem grouping = kpiDefinition.getGroupings().get(i);
                PdfPCell cell = new PdfPCell(new Phrase(grouping.toUnqualifiedDisplay()));
                cell.setMinimumHeight(20f);
                cell.setBackgroundColor(new BaseColor(180, 180, 180));
                table.addCell(cell);
            }
        }
        PdfPCell labelCell = new PdfPCell(new Phrase("Name"));
        PdfPCell latestValueCell = new PdfPCell(new Phrase("Latest Value"));
        PdfPCell previousValueCell = new PdfPCell(new Phrase("Previous Value"));
        PdfPCell percentChangeCell = new PdfPCell(new Phrase("Percent Change"));
        table.addCell(labelCell);
        table.addCell(latestValueCell);
        table.addCell(previousValueCell);
        table.addCell(percentChangeCell);
        int j = 1;
        for (TrendOutcome trendOutcome : outcomes) {
            //HSSFRow dataRow = sheet.createRow(j++);
            i = 0;
            if (kpiDefinition.getGroupings() != null) {
                for (i = 0; i < kpiDefinition.getGroupings().size(); i++) {
                    AnalysisItem grouping = kpiDefinition.getGroupings().get(i);
                    Value value = trendOutcome.getDimensions().get(grouping.qualifiedName());
                    PdfPCell groupingCell = new PdfPCell(new Phrase(createValue(exportMetadata.dateFormat, grouping, value, exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)));
                    groupingCell.setMinimumHeight(20f);
                    table.addCell(groupingCell);
                }
            }
            PdfPCell labelDataCell = new PdfPCell(new Phrase(trendOutcome.getMeasure().toUnqualifiedDisplay()));
            PdfPCell nowMeasureStyle = new PdfPCell(new Phrase(createValue(exportMetadata.dateFormat, trendOutcome.getMeasure(), trendOutcome.getNow(),
                    exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)));
            PdfPCell previousMeasureStyle = new PdfPCell(new Phrase(createValue(exportMetadata.dateFormat, trendOutcome.getMeasure(), trendOutcome.getHistorical(),
                    exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)));

            String percentChangeString;
            if (trendOutcome.getHistorical().toDouble() != 0) {
                double percentChange = (trendOutcome.getNow().toDouble() - trendOutcome.getHistorical().toDouble()) / trendOutcome.getHistorical().toDouble();
                percentChangeString = percentChange + "%";
            } else {
                percentChangeString = "";
            }
            PdfPCell percentChangeDataCell = new PdfPCell(new Phrase(percentChangeString));
            table.addCell(labelDataCell);
            table.addCell(nowMeasureStyle);
            table.addCell(previousMeasureStyle);
            table.addCell(percentChangeDataCell);
        }
        document.add(table);
    }

    private void treeToPDFTable(WSAnalysisDefinition report, EIConnection conn, InsightRequestMetadata insightRequestMetadata, Document document,
                                ExportMetadata exportMetadata) throws DocumentException {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);
        PipelineData pipelineData = dataSet.getPipelineData();

        java.util.List<AnalysisItem> items = new java.util.ArrayList<AnalysisItem>(report.getAllAnalysisItems());
        items.remove(null);
        java.util.Collections.sort(items, new java.util.Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        });

        WSTreeDefinition tree = (WSTreeDefinition) report;


        /*sb.append("<tr style=\"").append(headerTRStyle).append("\">");*/

        AnalysisHierarchyItem hierarchy = (AnalysisHierarchyItem) tree.getHierarchy();
        PdfPTable table = new PdfPTable(1 + tree.getItems().size());
        table.setSpacingBefore(20);
        table.getDefaultCell().setPadding(5);

        com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
        PdfPCell hierarchyCell = new PdfPCell(new Phrase(hierarchy.toUnqualifiedDisplay(), boldFont));
        hierarchyCell.setMinimumHeight(20f);
        hierarchyCell.setBackgroundColor(new BaseColor(210, 210, 210));

        table.addCell(hierarchyCell);
        for (AnalysisItem analysisItem : tree.getItems()) {
            PdfPCell valueCell = new PdfPCell(new Phrase(analysisItem.toUnqualifiedDisplay(), boldFont));
            valueCell.setBackgroundColor(new BaseColor(210, 210, 210));
            valueCell.setMinimumHeight(20f);
            table.addCell(valueCell);
        }

        TreeData treeData = new TreeData(tree, hierarchy, exportMetadata, dataSet);
        for (IRow row : dataSet.getRows()) {
            treeData.addRow(row);
        }
        List<TreeRow> rows = treeData.toTreeRows(pipelineData);
        for (TreeRow row : rows) {
            row.toPDF(tree, exportMetadata, table);
        }
        document.add(table);
    }

    private void crosstabToPDFTable(WSAnalysisDefinition analysisDefinition, EIConnection conn, InsightRequestMetadata insightRequestMetadata, Document document,
                                    ExportMetadata exportMetadata) throws DocumentException {
        WSCrosstabDefinition crosstabDefinition = (WSCrosstabDefinition) analysisDefinition;
        DataSet dataSet = DataService.listDataSet(analysisDefinition, insightRequestMetadata, conn);
        Crosstab crosstab = new Crosstab();
        crosstab.crosstab(crosstabDefinition, dataSet);
        CrosstabValue[][] values = crosstab.toTable(crosstabDefinition, insightRequestMetadata, conn);
        AnalysisMeasure measure = (AnalysisMeasure) crosstabDefinition.getMeasures().get(0);
        PdfPTable table = new PdfPTable(crosstab.getColumnSections().size() + crosstabDefinition.getRows().size() + 1);
        table.setSpacingBefore(20);
        table.getDefaultCell().setPadding(5);
        int rowOffset = crosstabDefinition.getMeasures().size() > 1 ? 3 : 2;
        for (int j = 0; j < (crosstab.getRowSections().size() + crosstabDefinition.getColumns().size()) + rowOffset; j++) {

            for (int i = 0; i < ((crosstab.getColumnSections().size() * crosstabDefinition.getMeasures().size()) + crosstabDefinition.getRows().size() + 1); i++) {
                CrosstabValue crosstabValue = values[j][i];
                String cellValue;
                if (crosstabValue == null) {
                    cellValue = "";
                } else {
                    if (crosstabValue.getHeader() == null) {
                        Value value = crosstabValue.getValue();
                        cellValue = createValue(exportMetadata.dateFormat, measure, value, exportMetadata.cal,
                                exportMetadata.currencySymbol, exportMetadata.locale,  false);
                    } else {
                        cellValue = crosstabValue.getValue().toString();
                    }
                }
                PdfPCell cell = new PdfPCell(new Phrase(cellValue));
                table.addCell(cell);
            }
        }
        document.add(table);
    }

    public void toImagePDFDatabase(WSAnalysisDefinition analysisDefinition, byte[] bytes, int width, int height, EIConnection conn) throws IOException, DocumentException, SQLException {
        toDatabase(analysisDefinition.getName(), toImagePDF(bytes, width, height), conn);
    }

    public byte[] toImagePDF(List<Page> pages, boolean landscapeOrientation) throws DocumentException, IOException, SQLException {
        Document document;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (landscapeOrientation) {
            document = new Document(PageSize.A4.rotate());
        } else {
            document = new Document(PageSize.A4);
        }

        PdfWriter.getInstance(document, baos);
        document.open();

        Iterator<Page> pageIter = pages.iterator();
        while (pageIter.hasNext()) {
            Page page = pageIter.next();
            Image image = Image.getInstance(page.getBytes());
            float percent = (landscapeOrientation ? 770f : 523f) / page.getWidth() * 100;
            image.scalePercent(percent);
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);
            if (pageIter.hasNext()) {
                document.newPage();
            }
        }

        // ratio = 1.5


        document.close();
        return baos.toByteArray();
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
        float pageWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
        float pageHeight = document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin();
        //float ratio = pageWidth / width;
        //float adjustedHeight = height * ratio;
        double widthRatio = width / pageWidth;
        double heightRatio = height / pageHeight;
        double maxRatio = Math.max(widthRatio, heightRatio);
        double targetWidth = width / maxRatio;
        double targetHeight = height / maxRatio;
        image.scaleAbsolute((float) targetWidth, (float) targetHeight);
        document.add(image);
        document.close();
        return baos.toByteArray();
    }

    public static String createValue(int dateFormat, AnalysisItem headerItem, Value value, Calendar cal, String currencySymbol, Locale locale, boolean pdf) {
        return createValue(dateFormat, headerItem, value, cal, currencySymbol, locale, pdf, null);
    }

    public static String createValue(int dateFormat, AnalysisItem headerItem, Value value, Calendar cal, String currencySymbol, Locale locale, boolean pdf, @Nullable String explicitDateFormat) {
        String valueString;
        if (headerItem.hasType(AnalysisItemTypes.MEASURE)) {
            AnalysisMeasure analysisMeasure = (AnalysisMeasure) headerItem;
            double doubleValue = value.toDouble();
            if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
                doubleValue = 0.;
            }
            if (analysisMeasure.getFormattingType() == FormattingConfiguration.CURRENCY) {
                String symbolToUse = currencySymbol;
                NumberFormat currencyFormatter;
                if (!pdf && symbolToUse.length() > 0 && symbolToUse.charAt(0) == 8364) {
                    symbolToUse = "&euro;";
                }
                currencyFormatter = DecimalFormat.getInstance(locale);
                //currencyFormatter = new DecimalFormat("###.###,##");
                currencyFormatter.setMaximumFractionDigits(analysisMeasure.getPrecision());
                currencyFormatter.setMinimumFractionDigits(analysisMeasure.getMinPrecision());

                valueString = symbolToUse + currencyFormatter.format(doubleValue);
            } else if (analysisMeasure.getFormattingType() == FormattingConfiguration.MILLISECONDS) {
                double absoluteValue = Math.abs(doubleValue);
                if (absoluteValue < 60000) {
                    int seconds = (int) (absoluteValue / 1000);
                    int milliseconds = (int) (absoluteValue % 1000);
                    valueString = seconds + "s" + (analysisMeasure.getPrecision() > 0 ? (":" + milliseconds + "ms") : "");
                } else if (absoluteValue < (60000 * 60)) {
                    int minutes = (int) (absoluteValue / 60000);
                    int seconds = (int) (absoluteValue / 1000) % 60;
                    valueString = minutes + "m" + (analysisMeasure.getPrecision() > 0 ? (":" + seconds + "s") : "");
                } else if (absoluteValue < (60000 * 60 * 24)) {
                    int hours = (int) (absoluteValue / (60000 * 60));
                    int minutes = (int) (absoluteValue % 24);
                    valueString = hours + "h" + (analysisMeasure.getPrecision() > 0 ? (":" + minutes + "m") : "");
                } else {
                    int days = (int) (absoluteValue / (60000 * 60 * 24));
                    int hours = (int) (absoluteValue / (60000 * 60) % 24);
                    valueString = days + "d" + (analysisMeasure.getPrecision() > 0 ? (":" + hours + "h") : "");
                }
                if (doubleValue < 0) {
                    valueString = "(" + valueString + ")";
                }
            } else if (analysisMeasure.getFormattingType() == FormattingConfiguration.SECONDS) {
                doubleValue = doubleValue * 1000;
                if (doubleValue < 60000) {
                    int seconds = (int) (doubleValue / 1000);
                    int milliseconds = (int) (doubleValue % 1000);
                    valueString = seconds + "s:" + milliseconds + "ms";
                } else if (doubleValue < (60000 * 60)) {
                    int minutes = (int) (doubleValue / 60000);
                    int seconds = (int) (doubleValue / 1000) % 60;
                    valueString = minutes + "m: " + seconds + "s";
                } else if (doubleValue < (60000 * 60 * 24)) {
                    int hours = (int) (doubleValue / (60000 * 60));
                    int minutes = (int) (doubleValue % 24);
                    valueString = hours + "h:" + minutes + "m";
                } else {
                    int days = (int) (doubleValue / (60000 * 60 * 24));
                    int hours = (int) (doubleValue / (60000 * 60) % 24);
                    valueString = days + "d:" + hours + "h";
                }
            } else if (analysisMeasure.getFormattingType() == FormattingConfiguration.PERCENTAGE) {
                NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
                numberFormat.setMaximumFractionDigits(analysisMeasure.getPrecision());
                numberFormat.setMinimumFractionDigits(analysisMeasure.getMinPrecision());
                valueString = numberFormat.format(doubleValue) + "%";
            } else {
                NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
                numberFormat.setMaximumFractionDigits(analysisMeasure.getPrecision());
                numberFormat.setMinimumFractionDigits(analysisMeasure.getMinPrecision());
                valueString = numberFormat.format(doubleValue);
            }
        } else if (headerItem.hasType(AnalysisItemTypes.DATE_DIMENSION) && value.type() == Value.DATE) {
            AnalysisDateDimension dateDim = (AnalysisDateDimension) headerItem;
            DateFormat sdf = getDateFormatForAccount(dateDim.getDateLevel(), dateDim.getOutputDateFormat(), dateFormat);

            if (sdf == null) {
                throw new RuntimeException("No date format found.");
            }
            DateValue dateValue = (DateValue) value;
            if (dateDim.isTimeshift()) {
                sdf.setCalendar(cal);
            }
            valueString = sdf.format(dateValue.getDate());
        } else {
            if (value.type() == Value.NUMBER) {
                int intValue = value.toDouble().intValue();
                double doubleValue = value.toDouble().doubleValue();
                if (intValue == doubleValue) {
                    valueString = String.valueOf(intValue);
                } else {
                    valueString = value.toHTMLString();
                }
            } else {
                valueString = value.toHTMLString();
            }
            if (pdf && headerItem.hasType(AnalysisItemTypes.TEXT)) {
                AnalysisText text = (AnalysisText) headerItem;
                if (text.isHtml()) {
                    valueString = valueString.replaceAll("</p>", "\n");
                    valueString = valueString.replaceAll("\\<.*?\\>", "");
                }
            } else if (pdf && headerItem.hasType(AnalysisItemTypes.DERIVED_DIMENSION)) {
                DerivedAnalysisDimension text = (DerivedAnalysisDimension) headerItem;
                if (text.isHtml()) {
                    valueString = valueString.replaceAll("</p>", "\n");
                    valueString = valueString.replaceAll("\\<.*?\\>", "");
                }
            }
        }
        return valueString;
    }

    public
    @Nullable
    static DateFormat getDateFormat(int dateLevel, @Nullable String explicitDateFormat) {
        long accountID = SecurityUtil.getAccountID();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT DATE_FORMAT FROM ACCOUNT WHERE ACCOUNT_ID = ?");
            ps.setLong(1, accountID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int format = rs.getInt(1);
            ps.close();
            return getDateFormatForAccount(dateLevel, explicitDateFormat, format);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static
    @Nullable
    DateFormat getDateFormatForAccount(int dateLevel, @Nullable String explicitDateFormat) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement accountStmt = conn.prepareStatement("SELECT DATE_FORMAT FROM ACCOUNT WHERE ACCOUNT_ID = ?");
            accountStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet rs = accountStmt.executeQuery();
            rs.next();
            int dateFormat = rs.getInt(1);
            accountStmt.close();
            return getDateFormatForAccount(dateLevel, explicitDateFormat, dateFormat);
        } catch (Exception e) {
            return new SimpleDateFormat("yyyy-MM-dd");
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static
    @Nullable
    DateFormat getDateFormatForAccount(int dateLevel, @Nullable String explicitDateFormat, int dateFormat) {
        DateFormat sdf = null;
        if (explicitDateFormat != null && !"".equals(explicitDateFormat.trim())) {
            sdf = new SimpleDateFormat(explicitDateFormat);
        } else {
            if (dateLevel == AnalysisDateDimension.YEAR_LEVEL) {
                sdf = new SimpleDateFormat("yyyy");
            } else if (dateLevel == AnalysisDateDimension.MONTH_FLAT) {
                sdf = new SimpleDateFormat("MMMM");
            } else if (dateLevel == AnalysisDateDimension.MONTH_LEVEL) {
                if (dateFormat == 0 || dateFormat == 3) {
                    sdf = new SimpleDateFormat("MM/yyyy");
                } else if (dateFormat == 1) {
                    sdf = new SimpleDateFormat("yyyy-MM");
                } else if (dateFormat == 2) {
                    sdf = new SimpleDateFormat("MM-yyyy");
                } else if (dateFormat == 4) {
                    sdf = new SimpleDateFormat("MM.yyyy");
                }
            } else if (dateLevel == AnalysisDateDimension.HOUR_LEVEL ||
                    dateLevel == AnalysisDateDimension.MINUTE_LEVEL) {
                if (dateFormat == 0) {
                    sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                } else if (dateFormat == 1) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                } else if (dateFormat == 2) {
                    sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                } else if (dateFormat == 3) {
                    sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                } else if (dateFormat == 4) {
                    sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
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
        }
        return sdf;
    }

    public ExcelResponse exportToExcel(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        if (analysisDefinition.getAnalysisID() > 0) SecurityUtil.authorizeInsight(analysisDefinition.getAnalysisID());
        else SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        EIConnection conn = Database.instance().getConnection();
        try {
            analysisDefinition.updateMetadata();
            byte[] bytes = toExcel(analysisDefinition, insightRequestMetadata, true, false);
            ExcelResponse response = new ExcelResponse();
            response.setBytes(bytes);
            return response;
        } catch (ReportException re) {
            ExcelResponse excelResponse = new ExcelResponse();
            excelResponse.setReportFault(re.getReportFault());
            return excelResponse;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public ExcelResponse exportToExcel2007(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        if (analysisDefinition.getAnalysisID() > 0) SecurityUtil.authorizeInsight(analysisDefinition.getAnalysisID());
        else SecurityUtil.authorizeFeedAccess(analysisDefinition.getDataFeedID());
        EIConnection conn = Database.instance().getConnection();
        try {
            analysisDefinition.updateMetadata();
            byte[] bytes = toExcel(analysisDefinition, insightRequestMetadata, true, true);
            ExcelResponse response = new ExcelResponse();
            response.setBytes(bytes);
            return response;
        } catch (ReportException re) {
            ExcelResponse excelResponse = new ExcelResponse();
            excelResponse.setReportFault(re.getReportFault());
            return excelResponse;
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

    public byte[] toExcel(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata, boolean onNoData, boolean format2007) throws IOException, SQLException {
        EIConnection conn = Database.instance().getConnection();
        try {
            ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);

            Workbook workbook = createWorkbookFromList(analysisDefinition, exportMetadata, conn, insightRequestMetadata, onNoData, format2007);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            byte[] bytes = baos.toByteArray();
            baos.close();

            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO EXCEL_EXPORT (USER_ID, EXCEL_FILE, REPORT_NAME, ANONYMOUS_ID, EXCEL_FORMAT) VALUES (?, ?, ?, ?, ?)",
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
            insertStmt.setInt(5, format2007 ? 1 : 0);
            insertStmt.execute();
            long id = Database.instance().getAutoGenKey(insertStmt);
            if (FlexContext.getHttpRequest() != null) {
                FlexContext.getHttpRequest().getSession().setAttribute("imageID", id);
                FlexContext.getHttpRequest().getSession().setAttribute("anonID", anonID);
            }
            return bytes;
        } catch (IllegalArgumentException iae) {
            if (iae.getMessage().contains("Invalid row number (65536) outside allowable range (0..65535)")) {
                throw new ReportException(new GenericReportFault("We can only export reports with less than 65,536 rows."));
            } else {
                throw iae;
            }
        } finally {
            Database.closeConnection(conn);
        }
    }

    public byte[] toExcelEmail(WSAnalysisDefinition analysisDefinition, EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean onNoData, boolean excel2007) throws IOException, SQLException {
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);
        Workbook workbook = createWorkbookFromList(analysisDefinition, exportMetadata, conn, insightRequestMetadata, onNoData, excel2007);
        if (workbook == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        byte[] bytes = baos.toByteArray();
        baos.close();
        return bytes;
    }

    private static String vListToTableWithActualCSS(VListInfo vListInfo, ExportMetadata exportMetadata, WSVerticalListDefinition verticalList) {
        StringBuilder sb = new StringBuilder();
        String cellStyle;
        String thStyle = "text-align:center";
        sb.append("<table style=\"font-size:").append(verticalList.getFontSize()).append("px").append("\" class=\"table ").append("reportTable").append(verticalList.getAnalysisID()).append(" table-hover table-bordered table-condensed\">");
        sb.append("<thead><tr style=\"background: #333333; color: #FFFFFF\">");
        sb.append("<th></th>");
        for (SortInfo sortInfo : vListInfo.columns) {
            sb.append("<th style=\"").append(thStyle).append("\">");
            sb.append(sortInfo.label);
            sb.append("</th>");
        }
        sb.append("</tr>");
        sb.append("</thead>");
        sb.append("<tbody>");
        for (Map<String, Object> map : vListInfo.dColl) {
            AnalysisMeasure baseMeasure = (AnalysisMeasure) map.get("baseMeasure");
            String ytdTRStyle = "";
            boolean alwaysShow = false;
            if (baseMeasure.getReportFieldExtension() != null && baseMeasure.getReportFieldExtension() instanceof VerticalListReportExtension) {
                VerticalListReportExtension ytdReportFieldExtension = (VerticalListReportExtension) baseMeasure.getReportFieldExtension();
                if (ytdReportFieldExtension.isLineAbove()) {
                    ytdTRStyle += "border-top: solid 2px black";
                }
                if (ytdReportFieldExtension.isAlwaysShow()) {
                    alwaysShow = true;
                    cellStyle = "text-align:left;font-weight:bold";
                } else {
                    cellStyle = "text-align:right";
                }
            } else {
                cellStyle = "text-align:right";
            }
            if (baseMeasure.isUnderline()) {
                ytdTRStyle += ";border-bottom:solid 2px black";
            }
            sb.append("<tr style=\"" + ytdTRStyle + "\">");
            //AnalysisMeasure baseMeasure = (AnalysisMeasure) map.get("baseMeasure");
            sb.append("<td style=\"white-space: nowrap;" + cellStyle + "\">");
            sb.append(baseMeasure.toUnqualifiedDisplay());
            sb.append("</td>");
            for (SortInfo sortInfo : vListInfo.columns) {
                String columnName = sortInfo.label;
                sb.append("<td style=\"").append(cellStyle).append("\">");
                if (alwaysShow) {
                    sb.append("");
                } else {
                    Value measureValue = (Value) map.get(columnName);
                    String text;
                    if (measureValue != null && measureValue.type() == Value.NUMBER) {
                        text = ExportService.createValue(exportMetadata.dateFormat, baseMeasure, measureValue, exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false);
                    } else {
                        text = "";
                    }
                    sb.append(text);
                }
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</tbody>");
        sb.append("</table>");
        return sb.toString();
    }

    private static String vListToTable(VListInfo vListInfo, ExportMetadata exportMetadata, WSVerticalListDefinition verticalList) {
        StringBuilder sb = new StringBuilder();
        String cellStyle;
        String thStyle = "padding:2px";
        sb.append("<table style=\"width:100%;font-size:" + verticalList.getFontSize() + "px;\" class='export_table'>");
        sb.append("<tr style=\"background: #333333; color: #FFFFFF\">");
        sb.append("<th></th>");
        for (SortInfo sortInfo : vListInfo.columns) {
            sb.append("<th style=\"").append(thStyle).append("\">");
            sb.append(sortInfo.label);
            sb.append("</th>");
        }
        sb.append("</tr>");
        for (Map<String, Object> map : vListInfo.dColl) {
            AnalysisMeasure baseMeasure = (AnalysisMeasure) map.get("baseMeasure");
            String ytdTRStyle = "";
            boolean alwaysShow = false;
            if (baseMeasure.getReportFieldExtension() != null && baseMeasure.getReportFieldExtension() instanceof VerticalListReportExtension) {
                VerticalListReportExtension ytdReportFieldExtension = (VerticalListReportExtension) baseMeasure.getReportFieldExtension();
                if (ytdReportFieldExtension.isLineAbove()) {
                    ytdTRStyle += "border-top: solid 1px black";
                }
                if (ytdReportFieldExtension.isAlwaysShow()) {
                    alwaysShow = true;
                    cellStyle = "padding:2px;text-align:left;font-weight:bold";
                } else {
                    cellStyle = "padding:2px;text-align:right";
                }
            } else {
                cellStyle = "padding:2px;text-align:right";
            }
            if (baseMeasure.isUnderline()) {
                ytdTRStyle += ";border-bottom:solid 1px black";
            }
            sb.append("<tr style=\"" + ytdTRStyle + "\">");
            //AnalysisMeasure baseMeasure = (AnalysisMeasure) map.get("baseMeasure");
            sb.append("<td style=\"white-space: nowrap;" + cellStyle + "\">");
            sb.append(baseMeasure.toUnqualifiedDisplay());
            sb.append("</td>");
            for (SortInfo sortInfo : vListInfo.columns) {
                String columnName = sortInfo.label;
                sb.append("<td style=\"").append(cellStyle).append("\">");
                if (alwaysShow) {
                    sb.append("");
                } else {
                    Value measureValue = (Value) map.get(columnName);
                    String text;
                    if (measureValue != null && measureValue.type() == Value.NUMBER) {
                        text = ExportService.createValue(exportMetadata.dateFormat, baseMeasure, measureValue, exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false);
                    } else {
                        text = "";
                    }
                    sb.append(text);
                }
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
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
            map.put("Label", analysisMeasure.toUnqualifiedDisplay());
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
                } else if (analysisMeasure.getReportFieldExtension() != null && analysisMeasure.getReportFieldExtension() instanceof VerticalListReportExtension) {
                    VerticalListReportExtension verticalListReportExtension = (VerticalListReportExtension) analysisMeasure.getReportFieldExtension();
                    if (verticalListReportExtension.isAlwaysShow()) {
                        atLeastOneValue = true;
                    }
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

    public static String textReportToHtml(WSAnalysisDefinition listDefinition, EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean email) {
        DataSet dataSet = DataService.listDataSet(listDefinition, insightRequestMetadata, conn);
        WSTextDefinition wsTextDefinition = (WSTextDefinition) listDefinition;
        StringBuilder sb = new StringBuilder();
        if (!email) {
            String style = MessageFormat.format("<div style=\"text-align:left; color:{1}; font-size:{0}px\">", wsTextDefinition.getFontSize(),
                    createHexString(wsTextDefinition.getFontColor()));
            sb.append(style);
        }
        for (IRow row : dataSet.getRows()) {
            for (int i = 0; i < wsTextDefinition.getColumns().size(); i++) {
                AnalysisItem item = wsTextDefinition.getColumns().get(i);
                Value value = row.getValue(item);
                sb.append(value.toString());
                if (i < wsTextDefinition.getColumns().size() - 1) {
                    sb.append(" ");
                }
            }
        }
        if (!email) {
            sb.append("</div>");
        }
        return sb.toString();
    }

    public static String kpiReportToHtmlTable(WSAnalysisDefinition listDefinition, EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean sendIfNoData, boolean includeTitle) throws SQLException {
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);
        WSKPIDefinition kpiReport = (WSKPIDefinition) listDefinition;
        TrendDataResults trendDataResults = DataService.getTrendDataResults(kpiReport, insightRequestMetadata, conn);
        if (trendDataResults.getTrendOutcomes().size() == 0 && !sendIfNoData) {
            return null;
        }
        List<TrendOutcome> outcomes = trendDataResults.getTrendOutcomes();
        StringBuilder sb = new StringBuilder();

        if (includeTitle && listDefinition.getName() != null) {
            sb.append("<div style=\"" + headerLabelStyle + "\">").append("<h0>").append(listDefinition.getName()).append("</h0></div>");
        }
        sb.append("<table style=\"" + tableStyle + "\">");
        sb.append("<tr style=\"" + headerTRStyle + "\">");
        int i;
        if (kpiReport.getGroupings() != null) {
            for (i = 0; i < kpiReport.getGroupings().size(); i++) {
                AnalysisItem grouping = kpiReport.getGroupings().get(i);
                sb.append("<th style=\"" + thStyle + "\">");
                sb.append(grouping.toUnqualifiedDisplay());
                sb.append("</th>");
            }
        }
        sb.append("<th style=\"" + thStyle + "\">Name</th>");
        if (trendDataResults.getNowString() == null) {
            sb.append("<th style=\"width:120px;" + thStyle + "\">Latest Value</th>");
        } else {
            sb.append("<th style=\"width:120px;" + thStyle + "\">").append(trendDataResults.getNowString()).append("</th>");
        }
        if (trendDataResults.getPreviousString() == null) {
            sb.append("<th style=\"width:120px;" + thStyle + "\">Previous Value</th>");
        } else {
            sb.append("<th style=\"width:120px;" + thStyle + "\">").append(trendDataResults.getPreviousString()).append("</th>");
        }

        sb.append("<th style=\"width:120px;" + thStyle + "\">Percent Change</th>");
        AnalysisMeasure percentMeasure = new AnalysisMeasure();
        percentMeasure.setFormattingType(FormattingConfiguration.PERCENTAGE);
        percentMeasure.setMinPrecision(1);
        percentMeasure.setPrecision(1);
        for (TrendOutcome trendOutcome : outcomes) {
            sb.append("<tr>");
            if (kpiReport.getGroupings() != null) {
                for (i = 0; i < kpiReport.getGroupings().size(); i++) {
                    AnalysisItem grouping = kpiReport.getGroupings().get(i);
                    Value value = trendOutcome.getDimensions().get(grouping.qualifiedName());
                    sb.append("<td style=\"" + tdStyle + "left\">");
                    sb.append(value);
                    sb.append("</td>");
                }
            }
            sb.append("<td style=\"" + tdStyle + "left\">").append(trendOutcome.getMeasure().toUnqualifiedDisplay()).append("</td>");
            String nowValue = ExportService.createValue(exportMetadata.dateFormat, trendOutcome.getMeasure(), trendOutcome.getNow(), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false);
            sb.append("<td style=\"" + tdStyle + "right\">").append(nowValue).append("</td>");
            String previousValue = ExportService.createValue(exportMetadata.dateFormat, trendOutcome.getMeasure(), trendOutcome.getHistorical(), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false);
            sb.append("<td style=\"" + tdStyle + "right\">").append(previousValue).append("</td>");
            sb.append("<td style=\"" + tdStyle + "right\">");
            if (trendOutcome.getHistorical().toDouble() != 0) {
                double percentChange = (trendOutcome.getNow().toDouble() - trendOutcome.getHistorical().toDouble()) / trendOutcome.getHistorical().toDouble() * 100;
                sb.append(createValue(exportMetadata.dateFormat, percentMeasure, new NumericValue(percentChange), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false));
            }
            sb.append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    public static String kpiReportToHtmlTableWithActualCSS(WSAnalysisDefinition listDefinition, EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean sendIfNoData, boolean includeTitle) throws SQLException {
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);
        WSKPIDefinition kpiReport = (WSKPIDefinition) listDefinition;
        TrendDataResults trendDataResults = DataService.getTrendDataResults(kpiReport, insightRequestMetadata, conn);
        if (trendDataResults.getTrendOutcomes().size() == 0 && !sendIfNoData) {
            return null;
        }
        List<TrendOutcome> outcomes = trendDataResults.getTrendOutcomes();
        StringBuilder sb = new StringBuilder();

        if (includeTitle && listDefinition.getName() != null) {
            sb.append("<div style=\"" + headerLabelStyle + "\">").append("<h0>").append(listDefinition.getName()).append("</h0></div>");
        }
        sb.append("<table class=\"table table-bordered table-condensed\" style=\"font-size:").append(listDefinition.getFontSize()).append("px\">");
        sb.append("<thead>");
        sb.append("<tr>");
        int i;
        if (kpiReport.getGroupings() != null) {
            for (i = 0; i < kpiReport.getGroupings().size(); i++) {
                AnalysisItem grouping = kpiReport.getGroupings().get(i);
                sb.append("<th style=\"" + thStyle + "\">");
                sb.append(grouping.toUnqualifiedDisplay());
                sb.append("</th>");
            }
        }
        sb.append("<th style=\"" + thStyle + "\">Name</th>");
        if (trendDataResults.getNowString() == null) {
            sb.append("<th style=\"width:120px;" + thStyle + "\">Latest Value</th>");
        } else {
            sb.append("<th style=\"width:120px;" + thStyle + "\">").append(trendDataResults.getNowString()).append("</th>");
        }
        if (trendDataResults.getPreviousString() == null) {
            sb.append("<th style=\"width:120px;" + thStyle + "\">Previous Value</th>");
        } else {
            sb.append("<th style=\"width:120px;" + thStyle + "\">").append(trendDataResults.getPreviousString()).append("</th>");
        }

        sb.append("<th style=\"width:120px;" + thStyle + "\">Percent Change</th>");
        sb.append("</tr>");
        sb.append("</thead>");
        sb.append("<tbody>");
        AnalysisMeasure percentMeasure = new AnalysisMeasure();
        percentMeasure.setFormattingType(FormattingConfiguration.PERCENTAGE);
        percentMeasure.setMinPrecision(1);
        percentMeasure.setPrecision(1);
        for (TrendOutcome trendOutcome : outcomes) {
            sb.append("<tr>");
            if (kpiReport.getGroupings() != null) {
                for (i = 0; i < kpiReport.getGroupings().size(); i++) {
                    AnalysisItem grouping = kpiReport.getGroupings().get(i);
                    Value value = trendOutcome.getDimensions().get(grouping.qualifiedName());
                    sb.append("<td style=\"" + tdStyle + "left\">");
                    sb.append(value);
                    sb.append("<span class='sortData'>");
                    sb.append(value.toString());
                    sb.append("</span>");
                    sb.append("</td>");
                }
            }
            sb.append("<td style=\"" + tdStyle + "left\">");
            sb.append(trendOutcome.getMeasure().toUnqualifiedDisplay());
            sb.append("<span class='sortData'>");
            sb.append(trendOutcome.getMeasure().toUnqualifiedDisplay());
            sb.append("</span>");
            sb.append("</td>");
            String nowValue = ExportService.createValue(exportMetadata.dateFormat, trendOutcome.getMeasure(), trendOutcome.getNow(), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false);
            sb.append("<td style=\"" + tdStyle + "right\">").append(nowValue);
            sb.append("<span class='sortData'>");
            sb.append(trendOutcome.getNow().toDouble());
            sb.append("</span>");
            sb.append("</td>");
            String previousValue = ExportService.createValue(exportMetadata.dateFormat, trendOutcome.getMeasure(), trendOutcome.getHistorical(), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false);
            sb.append("<td style=\"" + tdStyle + "right\">").append(previousValue);
            sb.append("<span class='sortData'>");
            sb.append(trendOutcome.getHistorical().toDouble());
            sb.append("</span>");
            sb.append("</td>");
            sb.append("<td style=\"" + tdStyle + "right\">");
            if (trendOutcome.getHistorical().toDouble() != 0) {
                double percentChange = (trendOutcome.getNow().toDouble() - trendOutcome.getHistorical().toDouble()) / trendOutcome.getHistorical().toDouble() * 100;
                sb.append(createValue(exportMetadata.dateFormat, percentMeasure, new NumericValue(percentChange), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false));
                sb.append("<span class='sortData'>");
                sb.append(percentChange);
                sb.append("</span>");
            }
            sb.append("</td>");
            sb.append("</tr>");
        }
        sb.append("</tbody>");
        sb.append("</table>");
        return sb.toString();
    }

    public static String verticalListToHTMLTable(WSAnalysisDefinition listDefinition, DataSet dataSet, EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean emailing) throws SQLException {
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);
        WSVerticalListDefinition verticalList = (WSVerticalListDefinition) listDefinition;
        VListInfo vListInfo = getVListInfo(verticalList, dataSet);
        if (emailing) {
            return vListToTable(vListInfo, exportMetadata, verticalList);
        } else {
            return vListToTableWithActualCSS(vListInfo, exportMetadata, verticalList);
        }
    }

    public static String compareYearsToHTMLTableWithActualCSS(WSAnalysisDefinition report, EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean includeTitle) throws SQLException {
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);
        WSCompareYearsDefinition verticalList = (WSCompareYearsDefinition) report;
        if (verticalList.getFilterDefinitions() != null) {
            List<FilterDefinition> filters = verticalList.getFilterDefinitions();
            Iterator<FilterDefinition> iter = filters.iterator();
            while (iter.hasNext()) {
                FilterDefinition filter = iter.next();
                if (filter instanceof FlatDateFilter) {
                    iter.remove();
                }
            }
        }

        ExtendedDataSet dataSet = DataService.extendedListDataSet(report, insightRequestMetadata, conn);
        YearStuff ytdStuff = YTDUtil.getYearStuff(verticalList, dataSet.getDataSet(), dataSet.getPipelineData(), dataSet.getReportItems());

        String cellStyle;
        String thStyle = "text-align:center";
        StringBuilder sb = new StringBuilder();
        if (includeTitle && report.getName() != null) {
            sb.append("<div style=\"" + headerLabelStyle + "\">").append("<h0>").append(report.getName()).append("</h0></div>");
        }
        sb.append("<table style=\"font-size:").append(verticalList.getFontSize()).append("px").append("\" class=\"table ").append("reportTable").append(verticalList.getAnalysisID()).append(" table-hover table-bordered table-condensed\">");
        sb.append("<thead><tr style=\"background: #333333; color: #FFFFFF\">");
        sb.append("<th></th>");
        for (int i = 0; i < ytdStuff.getHeaders().size(); i++) {
            sb.append("<th style=\"").append(thStyle).append("\">").append(ytdStuff.getHeaders().get(i)).append("</th>");
        }
        sb.append("</tr>");
        sb.append("</thead>");
        sb.append("<tbody>");
        AnalysisMeasure percentMeasure = new AnalysisMeasure();
        percentMeasure.setFormattingType(FormattingConfiguration.PERCENTAGE);
        percentMeasure.setMinPrecision(1);
        percentMeasure.setPrecision(1);
        for (CompareYearsRow ytdValue : ytdStuff.getRows()) {
            boolean alwaysShow = false;
            AnalysisMeasure baseMeasure = (AnalysisMeasure) ytdValue.getMeasure();
            if (baseMeasure.getReportFieldExtension() != null && baseMeasure.getReportFieldExtension() instanceof VerticalListReportExtension) {
                VerticalListReportExtension ytdReportFieldExtension = (VerticalListReportExtension) baseMeasure.getReportFieldExtension();
                if (ytdReportFieldExtension.isAlwaysShow()) {
                    alwaysShow = true;
                }
            }
            boolean atLeastOneValue = false;
            for (CompareYearsResult result : ytdValue.getResults().values()) {
                if (result.getValue().toDouble() > 0) {
                    atLeastOneValue = true;
                }
            }
            if (!alwaysShow && !atLeastOneValue) {
                continue;
            }
            String ytdTRStyle = "";


            if (baseMeasure.getReportFieldExtension() != null && baseMeasure.getReportFieldExtension() instanceof VerticalListReportExtension) {
                VerticalListReportExtension ytdReportFieldExtension = (VerticalListReportExtension) baseMeasure.getReportFieldExtension();
                if (ytdReportFieldExtension.isLineAbove()) {
                    ytdTRStyle += "border-top: solid 2px black;";
                }
                if (alwaysShow) {
                    cellStyle = "text-align:left;font-weight:bold";
                } else {
                    cellStyle = "text-align:right";
                }
            } else {
                cellStyle = "text-align:right";
            }
            if (baseMeasure.isUnderline()) {
                ytdTRStyle += "border-bottom:solid 2px black";
            }
            sb.append("<tr style=\"" + ytdTRStyle + "\">");
            sb.append("<td style=\"white-space: nowrap; ").append(cellStyle).append("\">").append(baseMeasure.toUnqualifiedDisplay()).append("</td>");

            for (String header : ytdStuff.getHeaders()) {
                if (alwaysShow) {
                    sb.append("<td style=\"").append(cellStyle).append("\"></td>");
                } else {
                    CompareYearsResult compareYearsResult = ytdValue.getResults().get(header);

                    Value value = compareYearsResult.getValue();
                    if (compareYearsResult.isPercentChange()) {
                        String tempStyle = cellStyle + ";color:#880000";
                        if (compareYearsResult.getValue().toDouble() < 0) {
                            sb.append("<td style=\"").append(tempStyle).append("\">").append(createValue(exportMetadata.dateFormat, percentMeasure, value, exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)).append("</td>");
                        } else {
                            sb.append("<td style=\"").append(cellStyle).append("\">").append(createValue(exportMetadata.dateFormat, percentMeasure, value, exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)).append("</td>");
                        }
                    } else {
                        sb.append("<td style=\"").append(cellStyle).append("\">").append(createValue(exportMetadata.dateFormat, ytdValue.getMeasure(), value, exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)).append("</td>");
                    }
                }
            }
            sb.append("</tr>");
        }
        sb.append("</tbody>");
        sb.append("</table>");
        return sb.toString();
    }

    public static String compareYearsToHTMLTable(WSAnalysisDefinition report, EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean includeTitle) throws SQLException {
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);
        WSCompareYearsDefinition verticalList = (WSCompareYearsDefinition) report;
        if (verticalList.getFilterDefinitions() != null) {
            List<FilterDefinition> filters = verticalList.getFilterDefinitions();
            Iterator<FilterDefinition> iter = filters.iterator();
            while (iter.hasNext()) {
                FilterDefinition filter = iter.next();
                if (filter instanceof FlatDateFilter) {
                    iter.remove();
                }
            }
        }

        ExtendedDataSet dataSet = DataService.extendedListDataSet(report, insightRequestMetadata, conn);
        YearStuff ytdStuff = YTDUtil.getYearStuff(verticalList, dataSet.getDataSet(), dataSet.getPipelineData(), dataSet.getReportItems());

        String cellStyle;
        String thStyle = "";
        StringBuilder sb = new StringBuilder();
        if (includeTitle && report.getName() != null) {
            sb.append("<div style=\"" + headerLabelStyle + "\">").append("<h0>").append(report.getName()).append("</h0></div>");
        }
        sb.append("<table style=\"width:100%;font-size:" + verticalList.getFontSize() + "px;\" class='export_table'>");
        sb.append("<tr style=\"background: #333333; color: #FFFFFF\">");
        sb.append("<th></th>");
        for (int i = 0; i < ytdStuff.getHeaders().size(); i++) {
            sb.append("<th style=\"").append(thStyle).append("\">").append(ytdStuff.getHeaders().get(i)).append("</th>");
        }
        sb.append("</tr>");

        AnalysisMeasure percentMeasure = new AnalysisMeasure();
        percentMeasure.setFormattingType(FormattingConfiguration.PERCENTAGE);
        percentMeasure.setMinPrecision(1);
        percentMeasure.setPrecision(1);
        for (CompareYearsRow ytdValue : ytdStuff.getRows()) {
            boolean atLeastOneValue = false;
            for (CompareYearsResult result : ytdValue.getResults().values()) {
                if (result.getValue().toDouble() > 0) {
                    atLeastOneValue = true;
                }
            }
            if (!atLeastOneValue) {
                continue;
            }
            String ytdTRStyle = "";
            AnalysisMeasure baseMeasure = (AnalysisMeasure) ytdValue.getMeasure();
            boolean alwaysShow = false;
            if (baseMeasure.getReportFieldExtension() != null && baseMeasure.getReportFieldExtension() instanceof VerticalListReportExtension) {
                VerticalListReportExtension ytdReportFieldExtension = (VerticalListReportExtension) baseMeasure.getReportFieldExtension();
                if (ytdReportFieldExtension.isLineAbove()) {
                    ytdTRStyle += "border-top: solid 1px black;";
                }
                if (ytdReportFieldExtension.isAlwaysShow()) {
                    alwaysShow = true;
                    cellStyle = "padding:2px;text-align:left;font-weight:bold";
                } else {
                    cellStyle = "padding:2px;text-align:right";
                }
            } else {
                cellStyle = "padding:2px;text-align:right";
            }
            if (baseMeasure.isUnderline()) {
                ytdTRStyle += "border-bottom:solid 1px black";
            }
            sb.append("<tr style=\"" + ytdTRStyle + "\">");
            sb.append("<td style=\"white-space: nowrap;text-align:left;").append(cellStyle).append("\">").append(baseMeasure.toUnqualifiedDisplay()).append("</td>");

            for (String header : ytdStuff.getHeaders()) {
                if (alwaysShow) {
                    sb.append("<td style=\"").append(cellStyle).append("\"></td>");
                } else {
                    CompareYearsResult compareYearsResult = ytdValue.getResults().get(header);

                    Value value = compareYearsResult.getValue();
                    if (compareYearsResult.isPercentChange()) {
                        sb.append("<td style=\"").append(cellStyle).append("\">").append(createValue(exportMetadata.dateFormat, percentMeasure, value, exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)).append("</td>");
                    } else {
                        sb.append("<td style=\"").append(cellStyle).append("\">").append(createValue(exportMetadata.dateFormat, ytdValue.getMeasure(), value, exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)).append("</td>");
                    }
                }
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    // sb.append("<table style=\"font-size:").append(report.getFontSize()).append("px").append("\" class=\"table ").append("reportTable").append(report.getAnalysisID()).append(" table-bordered table-hover table-condensed\">");

    public static String ytdToHTMLTableWithActualCSS(WSAnalysisDefinition listDefinition, EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean includeTitle) throws SQLException {
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);
        WSYTDDefinition verticalList = (WSYTDDefinition) listDefinition;
        if (verticalList.getTimeDimension() instanceof AnalysisDateDimension) {
            AnalysisDateDimension date = (AnalysisDateDimension) verticalList.getTimeDimension();
            date.setDateLevel(AnalysisDateDimension.MONTH_FLAT);
        }
        ExtendedDataSet dataSet = DataService.extendedListDataSet(verticalList, insightRequestMetadata, conn);
        YTDStuff ytdStuff = YTDUtil.getYTDStuff(verticalList, dataSet.getDataSet(), insightRequestMetadata, conn, dataSet.getPipelineData(), dataSet.getReportItems());
        boolean hasBenchmark = false;
        for (AnalysisItem analysisItem : verticalList.getMeasures()) {
            if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension() instanceof YTDReportFieldExtension) {
                YTDReportFieldExtension ytdReportFieldExtension = (YTDReportFieldExtension) analysisItem.getReportFieldExtension();
                if (ytdReportFieldExtension.getBenchmark() != null) {
                    hasBenchmark = true;
                }
            }
        }

        String cellStyle;
        String thStyle = "text-align:center";
        StringBuilder sb = new StringBuilder();
        if (includeTitle && listDefinition.getName() != null) {
            sb.append("<div style=\"").append(headerLabelStyle).append("\">").append("<h0>").append(listDefinition.getName()).append("</h0>").append("</div>");
        }
        sb.append("<table style=\"font-size:").append(verticalList.getFontSize()).append("px").append("\" class=\"table ").append("reportTable").append(verticalList.getAnalysisID()).append(" table-hover table-condensed\">");
        //sb.append("<table style=\"width:100%;font-size:" + verticalList.getFontSize() + "px;\" class='export_table'>");
        sb.append("<thead><tr style=\"background: #333333; color: #FFFFFF\">");
        sb.append("<th></th>");
        for (int i = 0; i < ytdStuff.getIntervals().size(); i++) {
            String date = createValue(exportMetadata.dateFormat, verticalList.getTimeDimension(), ytdStuff.getIntervals().get(i), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false, "MMM");
            sb.append("<th style=\"").append(thStyle).append("\">").append(date).append("</th>");
        }
        sb.append("<th style=\"").append(thStyle).append("\">").append("YTD").append("</th>");
        sb.append("<th style=\"").append(thStyle).append("\">").append("Average").append("</th>");
        if (hasBenchmark) {
            sb.append("<th style=\"").append(thStyle).append("\">").append("BK").append("</th>");
            sb.append("<th style=\"").append(thStyle).append("\">").append("Variation").append("</th>");
        }
        sb.append("</tr>");
        sb.append("</thead>");
        sb.append("<tbody>");
        AnalysisMeasure percentMeasure = new AnalysisMeasure();
        percentMeasure.setFormattingType(FormattingConfiguration.PERCENTAGE);
        percentMeasure.setMinPrecision(1);
        percentMeasure.setPrecision(1);

        int maxColumns = 0;
        for (YTDValue ytdValue : ytdStuff.getValues()) {
            if (ytdValue != null) {
                if (ytdValue.getTimeIntervalValues().size() > 0 && ytdValue.getYtd().toDouble() != null && ytdValue.getYtd().toDouble() != 0) {
                    int ctr = 0;
                    for (int i = 0; i < ytdStuff.getIntervals().size(); i++) {
                        ctr++;
                    }
                    ctr += 2;
                    maxColumns = Math.max(ctr, maxColumns);
                }
            }
        }

        for (YTDValue ytdValue : ytdStuff.getValues()) {
            AnalysisMeasure baseMeasure = ytdValue.getAnalysisMeasure();
            boolean alwaysShow = false;
            if (baseMeasure.getReportFieldExtension() != null && baseMeasure.getReportFieldExtension() instanceof YTDReportFieldExtension) {
                YTDReportFieldExtension ytdReportFieldExtension = (YTDReportFieldExtension) baseMeasure.getReportFieldExtension();
                if (ytdReportFieldExtension.isAlwaysShow()) {
                    alwaysShow = true;
                }
            }
            if (alwaysShow || (ytdValue != null && ytdValue.getTimeIntervalValues().size() > 0 && ytdValue.getYtd().toDouble() != null && ytdValue.getYtd().toDouble() != 0)) {
                String ytdTRStyle = "";
                if (baseMeasure.getReportFieldExtension() != null && baseMeasure.getReportFieldExtension() instanceof YTDReportFieldExtension) {
                    YTDReportFieldExtension ytdReportFieldExtension = (YTDReportFieldExtension) baseMeasure.getReportFieldExtension();
                    if (ytdReportFieldExtension.isLineAbove()) {
                        ytdTRStyle += "border-top: solid 2px #222222;";
                    }
                    if (alwaysShow) {
                        cellStyle = "text-align:left;font-weight:bold";
                    } else {
                        cellStyle = "text-align:right";
                    }
                } else {
                    cellStyle = "text-align:right";
                }
                if (baseMeasure.isUnderline()) {
                    ytdTRStyle += "border-bottom:solid 2px #222222";
                }

                sb.append("<tr style=\"").append(ytdTRStyle).append("\">");
                sb.append("<td style=\"white-space: nowrap; ").append(cellStyle).append("\">").append(baseMeasure.toUnqualifiedDisplay()).append("</td>");
                if (ytdValue.getTimeIntervalValues().size() > 0 && ytdValue.getYtd().toDouble() != null && ytdValue.getYtd().toDouble() != 0) {
                    Map<Value, TimeIntervalValue> map = new HashMap<Value, TimeIntervalValue>();
                    for (int i = 0; i < ytdValue.getTimeIntervalValues().size(); i++) {
                        TimeIntervalValue timeIntervalValue = ytdValue.getTimeIntervalValues().get(i);
                        map.put(timeIntervalValue.getDateValue(), timeIntervalValue);
                    }
                    for (int i = 0; i < ytdStuff.getIntervals().size(); i++) {
                        TimeIntervalValue timeIntervalValue = map.get(ytdStuff.getIntervals().get(i));
                        if (timeIntervalValue != null) {
                            Value value = timeIntervalValue.getValue();
                            sb.append("<td style=\"").append(cellStyle).append("\">").append(createValue(exportMetadata.dateFormat, baseMeasure, value, exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)).append("</td>");
                        } else {
                            sb.append("<td style=\"").append(cellStyle).append("</td>");
                        }
                    }
                    sb.append("<td style=\"").append(cellStyle).append("\">").append(createValue(exportMetadata.dateFormat, baseMeasure, ytdValue.getYtd(), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)).append("</td>");
                    sb.append("<td style=\"").append(cellStyle).append("\">").append(createValue(exportMetadata.dateFormat, baseMeasure, ytdValue.getAverage(), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)).append("</td>");

                    if (hasBenchmark) {
                        if (ytdValue.getBenchmarkMeasure() != null) {
                            sb.append("<td style=\"").append(cellStyle).append("\">").append(createValue(exportMetadata.dateFormat, ytdValue.getBenchmarkMeasure(), ytdValue.getBenchmarkValue(), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)).append("</td>");
                            sb.append("<td style=\"").append(cellStyle).append("\">").append(createValue(exportMetadata.dateFormat, percentMeasure, ytdValue.getVariation(), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)).append("</td>");
                        } else {
                            sb.append("<td style=\"").append(cellStyle).append("\"></td>");
                            sb.append("<td style=\"").append(cellStyle).append("\"></td>");
                        }
                    }
                } else if (alwaysShow) {
                    for (int i = 0; i < maxColumns; i++) {
                        sb.append("<td style=\"").append(cellStyle).append("\"></td>");
                    }
                    if (hasBenchmark) {
                        sb.append("<td style=\"").append(cellStyle).append("\"></td>");
                        sb.append("<td style=\"").append(cellStyle).append("\"></td>");
                    }
                }
                sb.append("</tr>");
            }
        }
        sb.append("</tbody>");
        sb.append("</table>");
        return sb.toString();
    }

    public static String ytdToHTMLTable(WSAnalysisDefinition listDefinition, EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean includeTitle) throws SQLException {
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);
        WSYTDDefinition verticalList = (WSYTDDefinition) listDefinition;
        if (verticalList.getTimeDimension() instanceof AnalysisDateDimension) {
            AnalysisDateDimension date = (AnalysisDateDimension) verticalList.getTimeDimension();
            date.setDateLevel(AnalysisDateDimension.MONTH_FLAT);
        }
        ExtendedDataSet dataSet = DataService.extendedListDataSet(verticalList, insightRequestMetadata, conn);
        YTDStuff ytdStuff = YTDUtil.getYTDStuff(verticalList, dataSet.getDataSet(), insightRequestMetadata, conn, dataSet.getPipelineData(), dataSet.getReportItems());
        boolean hasBenchmark = false;
        for (AnalysisItem analysisItem : verticalList.getMeasures()) {
            if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension() instanceof YTDReportFieldExtension) {
                YTDReportFieldExtension ytdReportFieldExtension = (YTDReportFieldExtension) analysisItem.getReportFieldExtension();
                if (ytdReportFieldExtension.getBenchmark() != null) {
                    hasBenchmark = true;
                }
            }
        }

        String cellStyle;
        String thStyle = "padding:2px";
        StringBuilder sb = new StringBuilder();
        if (includeTitle && listDefinition.getName() != null) {
            sb.append("<div style=\"").append(headerLabelStyle).append("\">").append("<h0>").append(listDefinition.getName()).append("</h0>").append("</div>");
        }
        sb.append("<table style=\"width:100%;font-size:" + verticalList.getFontSize() + "px;\" class='export_table'>");
        sb.append("<tr style=\"background: #333333; color: #FFFFFF\">");
        sb.append("<th></th>");
        for (int i = 0; i < ytdStuff.getIntervals().size(); i++) {
            String date = createValue(exportMetadata.dateFormat, verticalList.getTimeDimension(), ytdStuff.getIntervals().get(i), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false, "MMM");
            sb.append("<th style=\"").append(thStyle).append("\">").append(date).append("</th>");
        }
        sb.append("<th>").append("YTD").append("</th>");
        sb.append("<th>").append("Average").append("</th>");
        if (hasBenchmark) {
            sb.append("<th>").append("BK").append("</th>");
            sb.append("<th>").append("Variation").append("</th>");
            // https://www.easy-insight.com/app/newUser?token=ihLpPZtiVAPByJnoCqeFiOfMCapsgH
        }
        sb.append("</tr>");
        AnalysisMeasure percentMeasure = new AnalysisMeasure();
        percentMeasure.setFormattingType(FormattingConfiguration.PERCENTAGE);
        percentMeasure.setMinPrecision(1);
        percentMeasure.setPrecision(1);

        int maxColumns = 0;
        for (YTDValue ytdValue : ytdStuff.getValues()) {
            if (ytdValue != null) {
                if (ytdValue.getTimeIntervalValues().size() > 0 && ytdValue.getYtd().toDouble() != null && ytdValue.getYtd().toDouble() != 0) {
                    int ctr = 0;
                    for (int i = 0; i < ytdStuff.getIntervals().size(); i++) {
                        ctr++;
                    }
                    ctr += 2;
                    maxColumns = Math.max(ctr, maxColumns);
                }
            }
        }

        for (YTDValue ytdValue : ytdStuff.getValues()) {
            if (ytdValue != null && ytdValue.getTimeIntervalValues().size() > 0 && ytdValue.getYtd().toDouble() != null && ytdValue.getYtd().toDouble() != 0) {
                String ytdTRStyle = "";
                AnalysisMeasure baseMeasure = ytdValue.getAnalysisMeasure();
                boolean alwaysShow = false;
                if (baseMeasure.getReportFieldExtension() != null && baseMeasure.getReportFieldExtension() instanceof YTDReportFieldExtension) {
                    YTDReportFieldExtension ytdReportFieldExtension = (YTDReportFieldExtension) baseMeasure.getReportFieldExtension();
                    if (ytdReportFieldExtension.isLineAbove()) {
                        ytdTRStyle += "border-top: solid 1px black;";
                    }
                    if (ytdReportFieldExtension.isAlwaysShow()) {
                        alwaysShow = true;
                        cellStyle = "padding:2px;text-align:left;font-weight:bold";
                    } else {
                        cellStyle = "padding:2px;text-align:right";
                    }
                } else {
                    cellStyle = "padding:2px;text-align:right";
                }
                if (baseMeasure.isUnderline()) {
                    ytdTRStyle += "border-bottom:solid 1px black";
                }

                sb.append("<tr style=\"" + ytdTRStyle + "\">");
                sb.append("<td style=\"white-space: nowrap; ").append(cellStyle).append("\">").append(baseMeasure.toUnqualifiedDisplay()).append("</td>");
                if (ytdValue.getTimeIntervalValues().size() > 0 && ytdValue.getYtd().toDouble() != null && ytdValue.getYtd().toDouble() != 0) {
                    Map<Value, TimeIntervalValue> map = new HashMap<Value, TimeIntervalValue>();
                    for (int i = 0; i < ytdValue.getTimeIntervalValues().size(); i++) {
                        TimeIntervalValue timeIntervalValue = ytdValue.getTimeIntervalValues().get(i);
                        map.put(timeIntervalValue.getDateValue(), timeIntervalValue);
                    }
                    for (int i = 0; i < ytdStuff.getIntervals().size(); i++) {
                        TimeIntervalValue timeIntervalValue = map.get(ytdStuff.getIntervals().get(i));
                        if (timeIntervalValue != null) {
                            Value value = timeIntervalValue.getValue();
                            sb.append("<td style=\"").append(cellStyle).append("\">").append(createValue(exportMetadata.dateFormat, baseMeasure, value, exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)).append("</td>");
                        } else {
                            sb.append("<td style=\"").append(cellStyle).append("</td>");
                        }
                    }
                    sb.append("<td style=\"").append(cellStyle).append("\">").append(createValue(exportMetadata.dateFormat, baseMeasure, ytdValue.getYtd(), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)).append("</td>");
                    sb.append("<td style=\"").append(cellStyle).append("\">").append(createValue(exportMetadata.dateFormat, baseMeasure, ytdValue.getAverage(), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)).append("</td>");

                    if (ytdValue.getBenchmarkMeasure() != null) {
                        sb.append("<td style=\"").append(cellStyle).append("\">").append(createValue(exportMetadata.dateFormat, ytdValue.getBenchmarkMeasure(), ytdValue.getBenchmarkValue(), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)).append("</td>");
                        sb.append("<td style=\"").append(cellStyle).append("\">").append(createValue(exportMetadata.dateFormat, percentMeasure, ytdValue.getVariation(), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false)).append("</td>");
                    } else {
                        sb.append("<td style=\"").append(cellStyle).append("</td>");
                        sb.append("<td style=\"").append(cellStyle).append("</td>");
                    }
                } else if (alwaysShow) {
                    for (int i = 0; i < maxColumns; i++) {
                        sb.append("<td style=\"").append(cellStyle).append("</td>");
                    }
                    if (hasBenchmark) {
                        sb.append("<td>").append("</td>");
                        sb.append("<td>").append("</td>");
                    }
                }
                sb.append("</tr>");
            }
        }
        sb.append("</table>");
        return sb.toString();
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

    private Workbook createWorkbookFromList(WSAnalysisDefinition listDefinition, ExportMetadata exportMetadata,
                                            EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean generateWithNoData, boolean format2007) throws SQLException {
        Workbook workbook;
        if (format2007) {
            workbook = new XSSFWorkbook();
        } else {
            workbook = new HSSFWorkbook();
        }

        Map<AnalysisItem, Style> styleMap = new HashMap<AnalysisItem, Style>();

        Sheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "Data");

        boolean hasData;
        if (listDefinition.getReportType() == WSAnalysisDefinition.VERTICAL_LIST) {
            hasData = listVerticalList(listDefinition, exportMetadata, styleMap, sheet, workbook, insightRequestMetadata, conn);
        } else if (listDefinition.getReportType() == WSAnalysisDefinition.CROSSTAB) {
            hasData = listCrosstab(listDefinition, exportMetadata, styleMap, sheet, workbook, insightRequestMetadata, conn);
        } else if (listDefinition.getReportType() == WSAnalysisDefinition.TREND ||
                listDefinition.getReportType() == WSAnalysisDefinition.TREND_GRID ||
                listDefinition.getReportType() == WSAnalysisDefinition.DIAGRAM) {
            hasData = listTrends(listDefinition, exportMetadata, styleMap, sheet, workbook, insightRequestMetadata, conn);
        } else if (listDefinition.getReportType() == WSAnalysisDefinition.YTD) {
            hasData = listYTD(listDefinition, exportMetadata, styleMap, sheet, workbook, insightRequestMetadata, conn);
        } else if (listDefinition.getReportType() == WSAnalysisDefinition.COMPARE_YEARS) {
            hasData = listCompareYears(listDefinition, exportMetadata, styleMap, sheet, workbook, insightRequestMetadata, conn);
        } else {
            hasData = listExcel(listDefinition, workbook, sheet, insightRequestMetadata, conn, exportMetadata);
        }
        if (!hasData && !generateWithNoData) {
            return null;
        }
        return workbook;
    }

    private static RichTextString createRichTextString(String string, Cell cell) {
        if (cell instanceof HSSFCell) {
            return new HSSFRichTextString(string);
        } else {
            return new XSSFRichTextString(string);
        }
    }

    private boolean listTrends(WSAnalysisDefinition report, ExportMetadata exportMetadata, Map<AnalysisItem, Style> styleMap, Sheet sheet, Workbook workbook,
                               InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws SQLException {
        WSKPIDefinition crosstabDefinition = (WSKPIDefinition) report;
        TrendDataResults trendDataResults = DataService.getTrendDataResults(crosstabDefinition, insightRequestMetadata, conn);
        List<TrendOutcome> outcomes = trendDataResults.getTrendOutcomes();
        Row headerRow = sheet.createRow(0);
        int i = 0;
        if (crosstabDefinition.getGroupings() != null) {
            for (i = 0; i < crosstabDefinition.getGroupings().size(); i++) {
                AnalysisItem grouping = crosstabDefinition.getGroupings().get(i);
                Cell groupingCell = headerRow.createCell(i);
                groupingCell.setCellValue(createRichTextString(grouping.toUnqualifiedDisplay(), groupingCell));
            }
        }
        Cell labelCell = headerRow.createCell(i++);
        labelCell.setCellValue(createRichTextString("Name", labelCell));
        Cell latestValueCell = headerRow.createCell(i++);
        latestValueCell.setCellValue(createRichTextString("Latest Value", latestValueCell));
        Cell previousValueCell = headerRow.createCell(i++);
        previousValueCell.setCellValue(createRichTextString("Previous Value", previousValueCell));
        Cell percentChangeCell = headerRow.createCell(i);
        percentChangeCell.setCellValue(createRichTextString("Percent Change", percentChangeCell));
        for (int h = 0; h <= i; h++) {
            sheet.setColumnWidth(h, 5000);
        }
        int j = 1;
        AnalysisMeasure percentMeasure = new AnalysisMeasure();
        percentMeasure.setFormattingType(FormattingConfiguration.PERCENTAGE);
        percentMeasure.setMinPrecision(1);
        percentMeasure.setPrecision(1);
        for (TrendOutcome trendOutcome : outcomes) {
            Row dataRow = sheet.createRow(j++);
            i = 0;
            if (crosstabDefinition.getGroupings() != null) {
                for (i = 0; i < crosstabDefinition.getGroupings().size(); i++) {
                    AnalysisItem grouping = crosstabDefinition.getGroupings().get(i);
                    Value value = trendOutcome.getDimensions().get(grouping.qualifiedName());
                    Style style = getStyle(styleMap, grouping, workbook, exportMetadata, value);
                    style.format(dataRow, i, value, grouping, exportMetadata.cal);
                }
            }
            Cell labelDataCell = dataRow.createCell(i++);
            labelDataCell.setCellValue(createRichTextString(trendOutcome.getMeasure().toUnqualifiedDisplay(), labelDataCell));
            Style nowMeasureStyle = getStyle(styleMap, trendOutcome.getMeasure(), workbook, exportMetadata, trendOutcome.getNow());
            nowMeasureStyle.format(dataRow, i++, trendOutcome.getNow(), trendOutcome.getMeasure(), exportMetadata.cal);
            Style previousMeasureStyle = getStyle(styleMap, trendOutcome.getMeasure(), workbook, exportMetadata, trendOutcome.getHistorical());
            previousMeasureStyle.format(dataRow, i++, trendOutcome.getHistorical(), trendOutcome.getMeasure(), exportMetadata.cal);
            if (trendOutcome.getHistorical().toDouble() != 0) {
                Value percentValue = new NumericValue(((trendOutcome.getNow().toDouble() - trendOutcome.getHistorical().toDouble()) / trendOutcome.getHistorical().toDouble()));
                Style style = getStyle(styleMap, percentMeasure, workbook, exportMetadata, percentValue);
                style.format(dataRow, i, percentValue, percentMeasure, exportMetadata.cal);
            }
        }
        return trendDataResults.getTrendOutcomes().size() > 0;
    }

    private boolean listCrosstab(WSAnalysisDefinition report, ExportMetadata exportMetadata, Map<AnalysisItem, Style> styleMap, Sheet sheet, Workbook workbook,
                                 InsightRequestMetadata insightRequestMetadata, EIConnection conn) {
        WSCrosstabDefinition crosstabDefinition = (WSCrosstabDefinition) report;
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);
        Crosstab crosstab = new Crosstab();
        crosstab.crosstab(crosstabDefinition, dataSet);
        CrosstabValue[][] values = crosstab.toTable(crosstabDefinition, insightRequestMetadata, conn);
        AnalysisMeasure measure = (AnalysisMeasure) crosstabDefinition.getMeasures().get(0);
        NumericStyle measureStyle = (NumericStyle) createStyle(measure, workbook, exportMetadata);
        NumericStyle summaryStyle = (NumericStyle) createStyle(measure, workbook, exportMetadata);
        Font font = workbook.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        summaryStyle.cellStyle1.setFillForegroundColor(IndexedColors.BLACK.getIndex());
        summaryStyle.cellStyle1.setFillPattern(CellStyle.SOLID_FOREGROUND);
        summaryStyle.cellStyle1.setFont(font);

        CellStyle crosstabHeaderStyle = workbook.createCellStyle();
        crosstabHeaderStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
        crosstabHeaderStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        crosstabHeaderStyle.setFont(font);

        for (int i = 0; i < (crosstab.getColumnSections().size() + crosstabDefinition.getRows().size()) + 1; i++) {
            sheet.setColumnWidth(i, 5000);
        }
        int rowOffset = crosstabDefinition.getMeasures().size() > 1 ? 3 : 2;
        for (int j = 0; j < (crosstab.getRowSections().size() + crosstabDefinition.getColumns().size()) + rowOffset; j++) {
            Row row = sheet.createRow(j);
            for (int i = 0; i < ((crosstab.getColumnSections().size() * crosstabDefinition.getMeasures().size()) + crosstabDefinition.getRows().size() + 1); i++) {
                CrosstabValue crosstabValue = values[j][i];
                if (crosstabValue == null) {
                    if (j == (crosstab.getColumnSections().size() + crosstabDefinition.getRows().size()) + 1 &&
                            i == (crosstab.getColumnSections().size() + crosstabDefinition.getRows().size())) {
                        Cell cell = row.createCell(i);
                        cell.setCellStyle(crosstabHeaderStyle);
                    } else {
                        Cell cell = row.createCell(i);
                        cell.setCellStyle(crosstabHeaderStyle);
                    }
                } else {
                    Cell cell = row.createCell(i);
                    if (crosstabValue.getHeader() == null) {
                        if (crosstabValue.isSummaryValue()) {
                            summaryStyle.format(row, i, crosstabValue.getValue(), measure, exportMetadata.cal);
                        } else {
                            measureStyle.format(row, i, crosstabValue.getValue(), measure, exportMetadata.cal);
                        }
                    } else {
                        cell.setCellValue(createRichTextString(crosstabValue.getValue().toString(), cell));
                        cell.setCellStyle(crosstabHeaderStyle);
                    }
                }
            }
        }
        return dataSet.getRows().size() > 0;
    }

    private static Value createPercentValue(Value value) {
        if (value.toDouble() == null) {
            return new EmptyValue();
        } else {
            return new NumericValue(value.toDouble() / 100);
        }
    }

    private boolean listCompareYears(WSAnalysisDefinition report, ExportMetadata exportMetadata, Map<AnalysisItem, Style> styleMap, Sheet sheet, Workbook workbook,
                                     InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws SQLException {
        WSCompareYearsDefinition verticalList = (WSCompareYearsDefinition) report;
        ExtendedDataSet dataSet = DataService.extendedListDataSet(report, insightRequestMetadata, conn);
        YearStuff ytdStuff = YTDUtil.getYearStuff(verticalList, dataSet.getDataSet(), dataSet.getPipelineData(), dataSet.getReportItems());

        Row headerRow = sheet.createRow(0);
        sheet.setColumnWidth(0, 5000);
        for (int i = 0; i < ytdStuff.getHeaders().size(); i++) {
            sheet.setColumnWidth(i + 1, 5000);
            Cell cell = headerRow.createCell(i + 1);
            cell.setCellValue(ytdStuff.getHeaders().get(i));
        }

        int j = 1;
        AnalysisMeasure percentMeasure = new AnalysisMeasure();
        percentMeasure.setFormattingType(FormattingConfiguration.PERCENTAGE);
        percentMeasure.setMinPrecision(1);
        percentMeasure.setPrecision(1);
        for (CompareYearsRow ytdValue : ytdStuff.getRows()) {
            Row row = sheet.createRow(j++);
            AnalysisItem baseMeasure = ytdValue.getMeasure();
            Cell rowHeaderCell = row.createCell(0);
            rowHeaderCell.setCellValue(baseMeasure.toUnqualifiedDisplay());
            int i = 0;
            for (String header : ytdStuff.getHeaders()) {
                CompareYearsResult compareYearsResult = ytdValue.getResults().get(header);
                Value value = compareYearsResult.getValue();
                if (compareYearsResult.isPercentChange()) {
                    Style style = getStyle(styleMap, percentMeasure, workbook, exportMetadata, value);
                    style.format(row, i + 1, createPercentValue(value), percentMeasure, exportMetadata.cal);
                } else {
                    Style style = getStyle(styleMap, baseMeasure, workbook, exportMetadata, value);
                    style.format(row, i + 1, value, baseMeasure, exportMetadata.cal);
                }
                i++;
            }
        }
        return dataSet.getDataSet().getRows().size() > 0;
    }

    private boolean listYTD(WSAnalysisDefinition report, ExportMetadata exportMetadata, Map<AnalysisItem, Style> styleMap, Sheet sheet, Workbook workbook,
                            InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws SQLException {
        WSYTDDefinition verticalList = (WSYTDDefinition) report;
        ExtendedDataSet dataSet = DataService.extendedListDataSet(report, insightRequestMetadata, conn);
        YTDStuff ytdStuff = YTDUtil.getYTDStuff(verticalList, dataSet.getDataSet(), insightRequestMetadata, conn, dataSet.getPipelineData(), dataSet.getReportItems());
        boolean hasBenchmark = false;
        for (AnalysisItem analysisItem : verticalList.getMeasures()) {
            if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension() instanceof YTDReportFieldExtension) {
                YTDReportFieldExtension ytdReportFieldExtension = (YTDReportFieldExtension) analysisItem.getReportFieldExtension();
                if (ytdReportFieldExtension.getBenchmark() != null) {
                    hasBenchmark = true;
                }
            }
        }
        Row headerRow = sheet.createRow(0);
        sheet.setColumnWidth(0, 5000);
        for (int i = 0; i < ytdStuff.getIntervals().size(); i++) {
            sheet.setColumnWidth(i + 1, 5000);
            Cell cell = headerRow.createCell(i + 1);
            cell.setCellValue(ytdStuff.getIntervals().get(i).toString());
        }
        Cell ytdCell = headerRow.createCell(ytdStuff.getIntervals().size() + 1);
        sheet.setColumnWidth(ytdStuff.getIntervals().size() + 1, 5000);
        ytdCell.setCellValue("YTD");
        Cell avgCell = headerRow.createCell(ytdStuff.getIntervals().size() + 2);
        sheet.setColumnWidth(ytdStuff.getIntervals().size() + 2, 5000);
        avgCell.setCellValue("Average");
        if (hasBenchmark) {
            Cell bkCell = headerRow.createCell(ytdStuff.getIntervals().size() + 3);
            sheet.setColumnWidth(ytdStuff.getIntervals().size() + 3, 5000);
            bkCell.setCellValue("BK");
            Cell variationCell = headerRow.createCell(ytdStuff.getIntervals().size() + 4);
            sheet.setColumnWidth(ytdStuff.getIntervals().size() + 4, 5000);
            variationCell.setCellValue("Variation");
        }
        int j = 1;
        AnalysisMeasure percentMeasure = new AnalysisMeasure();
        percentMeasure.setFormattingType(FormattingConfiguration.PERCENTAGE);
        percentMeasure.setMinPrecision(1);
        percentMeasure.setPrecision(1);
        for (YTDValue ytdValue : ytdStuff.getValues()) {
            Row row = sheet.createRow(j++);
            AnalysisMeasure baseMeasure = ytdValue.getAnalysisMeasure();
            Cell rowHeaderCell = row.createCell(0);
            rowHeaderCell.setCellValue(baseMeasure.toUnqualifiedDisplay());
            if (ytdValue.getTimeIntervalValues().size() > 0 && ytdValue.getYtd().toDouble() != null && ytdValue.getYtd().toDouble() != 0) {
                Map<Value, TimeIntervalValue> map = new HashMap<Value, TimeIntervalValue>();
                for (int i = 0; i < ytdValue.getTimeIntervalValues().size(); i++) {
                    TimeIntervalValue timeIntervalValue = ytdValue.getTimeIntervalValues().get(i);
                    map.put(timeIntervalValue.getDateValue(), timeIntervalValue);
                }
                for (int i = 0; i < ytdStuff.getIntervals().size(); i++) {
                    TimeIntervalValue timeIntervalValue = map.get(ytdStuff.getIntervals().get(i));
                    if (timeIntervalValue != null) {
                        Value value = timeIntervalValue.getValue();
                        Style style = getStyle(styleMap, baseMeasure, workbook, exportMetadata, value);
                        style.format(row, i + 1, value, baseMeasure, exportMetadata.cal);
                    }
                }
                Style ytdStyle = getStyle(styleMap, baseMeasure, workbook, exportMetadata, ytdValue.getYtd());
                ytdStyle.format(row, ytdValue.getTimeIntervalValues().size() + 1, ytdValue.getYtd(), baseMeasure, exportMetadata.cal);
                Style avgStyle = getStyle(styleMap, baseMeasure, workbook, exportMetadata, ytdValue.getAverage());
                avgStyle.format(row, ytdValue.getTimeIntervalValues().size() + 2, ytdValue.getAverage(), baseMeasure, exportMetadata.cal);
                if (ytdValue.getBenchmarkMeasure() != null) {
                    Style benchmarkStyle = getStyle(styleMap, ytdValue.getBenchmarkMeasure(), workbook, exportMetadata, ytdValue.getBenchmarkValue());
                    benchmarkStyle.format(row, ytdValue.getTimeIntervalValues().size() + 3, ytdValue.getBenchmarkValue(), baseMeasure, exportMetadata.cal);
                    Style variationStyle = getStyle(styleMap, percentMeasure, workbook, exportMetadata, ytdValue.getVariation());
                    variationStyle.format(row, ytdValue.getTimeIntervalValues().size() + 4, createPercentValue(ytdValue.getVariation()), percentMeasure, exportMetadata.cal);
                }
            }
        }
        return dataSet.getDataSet().getRows().size() > 0;
    }

    public List<ReportDeliveryAudit> getReportDeliveryAudits(long activityID) {
        List<ReportDeliveryAudit> audits = new ArrayList<ReportDeliveryAudit>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT SUCCESSFUL, MESSAGE, SEND_DATE, TARGET_EMAIL FROM REPORT_DELIVERY_AUDIT WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
            queryStmt.setLong(1, activityID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                int status = rs.getInt(1);
                String message = rs.getString(2);
                Date sendDate = new Date(rs.getTimestamp(3).getTime());
                String targetEmail = rs.getString(4);
                audits.add(new ReportDeliveryAudit(targetEmail, status, message, sendDate));
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return audits;
    }

    private boolean listVerticalList(WSAnalysisDefinition report, ExportMetadata exportMetadata, Map<AnalysisItem, Style> styleMap, Sheet sheet, Workbook workbook,
                                     InsightRequestMetadata insightRequestMetadata, EIConnection conn) {
        WSVerticalListDefinition verticalList = (WSVerticalListDefinition) report;
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);
        VListInfo vListInfo = getVListInfo(verticalList, dataSet);
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < vListInfo.columns.size(); i++) {
            sheet.setColumnWidth(i, 5000);
            SortInfo sortInfo = vListInfo.columns.get(i);
            Cell cell = headerRow.createCell(i + 1);
            cell.setCellValue(sortInfo.label);
        }
        int j = 1;
        for (Map<String, Object> map : vListInfo.dColl) {
            Row row = sheet.createRow(j++);
            AnalysisMeasure baseMeasure = (AnalysisMeasure) map.get("baseMeasure");
            Cell rowHeaderCell = row.createCell(0);
            rowHeaderCell.setCellValue(baseMeasure.toUnqualifiedDisplay());
            for (int i = 0; i < vListInfo.columns.size(); i++) {
                SortInfo sortInfo = vListInfo.columns.get(i);
                Value value = (Value) map.get(sortInfo.label);
                Style style = getStyle(styleMap, baseMeasure, workbook, exportMetadata, value);
                style.format(row, i + 1, value, baseMeasure, exportMetadata.cal);
            }
        }
        return dataSet.getRows().size() > 0;
    }

    private boolean listExcel(WSAnalysisDefinition listDefinition, Workbook workbook, Sheet sheet,
                              InsightRequestMetadata insightRequestMetadata, EIConnection conn, ExportMetadata exportMetadata) {
        ListDataResults listDataResults = (ListDataResults) DataService.list(listDefinition, insightRequestMetadata, conn);
        if (listDefinition.getReportType() == WSAnalysisDefinition.LIST) {
            WSListDefinition list = (WSListDefinition) listDefinition;
            if (list.isSummaryTotal()) {
                listDataResults.summarize();
            }
        }
        Row headerRow = sheet.createRow(0);
        Map<AnalysisItem, Short> positionMap = new HashMap<AnalysisItem, Short>();
        List<AnalysisItem> items = new ArrayList<AnalysisItem>(listDefinition.getAllAnalysisItems());
        items.remove(null);
        Collections.sort(items, new Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        });
        Map<AnalysisItem, Style> styleMap = new HashMap<AnalysisItem, Style>();
        for (short i = 0; i < items.size(); i++) {
            AnalysisItem analysisItem = items.get(i);
            positionMap.put(analysisItem, i);
        }
        for (AnalysisItem analysisItem : listDataResults.getHeaders()) {
            TextReportFieldExtension textReportFieldExtension = null;
            if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension() instanceof TextReportFieldExtension) {
                textReportFieldExtension = (TextReportFieldExtension) analysisItem.getReportFieldExtension();
            }
            Short headerPosition = positionMap.get(analysisItem);
            if (headerPosition == null) {
                continue;
            }
            int width;
            if (textReportFieldExtension != null && textReportFieldExtension.getFixedWidth() > 0) {
                width = textReportFieldExtension.getFixedWidth() / 15 * 256;
            } else if (analysisItem.getWidth() > 0) {
                width = Math.max((analysisItem.getWidth() / 15 * 256), 5000);
            } else {
                width = 5000;
            }

            sheet.setColumnWidth(headerPosition, width);
            Cell headerCell = headerRow.createCell(headerPosition);
            String displayName;
            if (analysisItem.getDisplayName() == null) {
                displayName = analysisItem.getKey().toDisplayName();
            } else {
                displayName = analysisItem.getDisplayName();
            }
            headerCell.setCellValue(createRichTextString(displayName, headerCell));
            Font font = workbook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);
            headerCell.setCellStyle(cellStyle);
        }

        int i = 1;
        for (ListRow listRow : listDataResults.getRows()) {
            Row row = sheet.createRow(i);
            Value[] values = listRow.getValues();
            short cellIndex = 0;
            for (Value value : values) {
                AnalysisItem analysisItem = listDataResults.getHeaders()[cellIndex];
                Short translatedIndex = positionMap.get(analysisItem);
                if (translatedIndex != null) {
                    getStyle(styleMap, analysisItem, workbook, exportMetadata, value).format(row, translatedIndex, value, analysisItem, exportMetadata.cal);
                    cellIndex++;
                }
            }
            i++;
        }
        if (listDefinition.getReportType() == WSAnalysisDefinition.LIST) {
            WSListDefinition list = (WSListDefinition) listDefinition;
            if (list.isSummaryTotal()) {
                Row summaryRow = sheet.createRow(i);
                for (int j = 0; j < listDataResults.getHeaders().length; j++) {
                    AnalysisItem analysisItem = listDataResults.getHeaders()[j];
                    if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                        int headerPosition = positionMap.get(analysisItem);
                        double summary = listDataResults.getSummaries()[j];
                        if (Double.isNaN(summary) || Double.isInfinite(summary)) {
                            summary = 0;
                        }
                        getStyle(styleMap, analysisItem, workbook, exportMetadata, new NumericValue(summary)).format(summaryRow, headerPosition, new NumericValue(summary), analysisItem, exportMetadata.cal);
                    }
                }
            }
        }
        return listDataResults.getRows().length > 0;
    }

    private static class NumericStyle extends Style {
        private CellStyle cellStyle1;
        private CellStyle cellStyle2;
        private boolean flexibleFormatting;

        private NumericStyle(CellStyle cellStyle1) {
            this.cellStyle1 = cellStyle1;
        }

        private NumericStyle(CellStyle cellStyle1, CellStyle cellStyle2) {
            this.cellStyle1 = cellStyle1;
            this.cellStyle2 = cellStyle2;
            this.flexibleFormatting = true;
        }

        public void format(Row row, int cellIndex, Value value, AnalysisItem analysisItem, Calendar cal) {
            Cell cell = row.createCell(cellIndex);
            if (value == null) {
                return;
            }
            if (flexibleFormatting) {
                double doubleValue = value.toDouble();
                int castInt = (int) doubleValue;
                if (doubleValue - castInt < 0.0001) {
                    cell.setCellStyle(cellStyle1);
                } else {
                    cell.setCellStyle(cellStyle2);
                }
            } else {
                cell.setCellStyle(cellStyle1);
            }
            if (value.type() == Value.NUMBER) {
                NumericValue numericValue = (NumericValue) value;
                if (numericValue.toDouble() == null) {
                    return;
                }
                double doubleValue = numericValue.toDouble();
                if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
                    doubleValue = 0;
                }
                if (analysisItem.getFormattingType() == FormattingConfiguration.MILLISECONDS || analysisItem.getFormattingType() == FormattingConfiguration.SECONDS) {
                    String result;
                    if (analysisItem.getFormattingType() == FormattingConfiguration.SECONDS) {
                        doubleValue = doubleValue * 1000;
                    }
                    double unsigned = Math.abs(doubleValue);
                    if (unsigned < 60000L) {
                        int seconds = (int) (unsigned / 1000);
                        int milliseconds = (int) (unsigned % 1000);
                        result = seconds + "s:";
                        if (analysisItem.getFormattingType() == FormattingConfiguration.MILLISECONDS)
                            result = result + milliseconds + "ms";
                    } else if (unsigned < (60000 * 60)) {
                        int minutes = (int) (unsigned / 60000);
                        int seconds = (int) (unsigned / 1000) % 60;
                        result = minutes + "m: " + seconds + "s";
                    } else if (unsigned < (60000 * 60 * 24)) {
                        int hours = (int) (unsigned / (60000 * 60));
                        int minutes = (int) (unsigned % 24);
                        result = hours + "h:" + minutes + "m";
                    } else {
                        int days = (int) (unsigned / (60000 * 60 * 24));
                        int hours = (int) (unsigned / (60000 * 60) % 24);
                        result = days + "d:" + hours + "h";
                    }
                    if (doubleValue < 0) {
                        result = "(" + result + ")";
                    }
                    cell.setCellValue(result);
                } else if (analysisItem.getFormattingType() == FormattingConfiguration.PERCENTAGE) {
                    doubleValue = doubleValue / 100;
                    cell.setCellValue(doubleValue);
                } else {
                    cell.setCellValue(doubleValue);
                }
            }
        }
    }

    private static class DateStyle extends Style {

        private CellStyle cellStyle1;

        private DateStyle(CellStyle cellStyle1) {
            this.cellStyle1 = cellStyle1;
        }

        @Override
        public void format(Row row, int cellIndex, Value value, AnalysisItem analysisItem, Calendar cal) {
            Cell cell = row.createCell(cellIndex);
            cell.setCellStyle(cellStyle1);
            if (value.type() == Value.DATE) {
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
            } else {
                cell.setCellValue(value.toString());
            }
        }
    }

    private static class StringStyle extends Style {

        private CellStyle cellStyle1;

        private URLLink defaultLink;

        private CreationHelper creationHeper;

        private StringStyle(CellStyle cellStyle1, AnalysisItem analysisItem, Workbook workbook) {
            this.cellStyle1 = cellStyle1;
            if (analysisItem.getLinks() != null) {
                URLLink defaultLink = null;
                for (Link link : analysisItem.getLinks()) {
                    if (link.isDefaultLink() && link instanceof URLLink) {
                        defaultLink = (URLLink) link;
                    } else if (defaultLink == null && link instanceof URLLink) {
                        defaultLink = (URLLink) link;
                    }
                }
                this.defaultLink = defaultLink;
                this.creationHeper = workbook.getCreationHelper();
            }
        }

        @Override
        public void format(Row row, int cellIndex, Value value, AnalysisItem analysisItem, Calendar cal) {
            Cell cell = row.createCell(cellIndex);
            cell.setCellStyle(cellStyle1);
            if (value.type() == Value.STRING) {
                StringValue stringValue = (StringValue) value;
                String string = stringValue.getValue();
                if (analysisItem.hasType(AnalysisItemTypes.TEXT)) {
                    string = string.replaceAll("\\<.*?\\>", "");
                }
                if (analysisItem.hasType(AnalysisItemTypes.DERIVED_DIMENSION)) {
                    DerivedAnalysisDimension dim = (DerivedAnalysisDimension) analysisItem;
                    if (dim.isHtml()) {
                        string = string.replaceAll("</p>", "\n");
                        string = string.replaceAll("\\<.*?\\>", "");
                    }
                }
                if (string.length() > 15000) {
                    string = string.substring(0, 15000);
                }
                /*boolean showLink = defaultLink != null && value.getLinks() != null && value.getLinks().get(defaultLink.getLabel()) != null;
                if (showLink) {
                    Hyperlink link = creationHeper.createHyperlink(Hyperlink.LINK_URL);
                    link.setAddress(value.getLinks().get(defaultLink.getLabel()));
                    cell.setHyperlink(link);
                }*/
                RichTextString richText = createRichTextString(string, cell);
                cell.setCellValue(richText);
            } else if (value.type() == Value.NUMBER) {
                NumericValue numericValue = (NumericValue) value;
                double doubleValue = numericValue.toDouble();
                if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
                    doubleValue = 0;
                }
                cell.setCellValue(doubleValue);
            } else {
                cell.setCellValue(value.toHTMLString());
            }
        }
    }

    private abstract static class Style {
        private AnalysisItem analysisItem;

        public abstract void format(Row row, int cellIndex, Value value, AnalysisItem analysisItem, Calendar cal);
    }

    private Style createStyle(AnalysisItem analysisItem, Workbook workbook, ExportMetadata exportMetadata) {
        Style style;
        TextReportFieldExtension textExtension = null;
        if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension() instanceof TextReportFieldExtension) {
            textExtension = (TextReportFieldExtension) analysisItem.getReportFieldExtension();
        }
        if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            AnalysisMeasure analysisMeasure = (AnalysisMeasure) analysisItem;

            switch (analysisMeasure.getFormattingType()) {
                case FormattingConfiguration.CURRENCY:
                    CellStyle currencyStyle = workbook.createCellStyle();
                    String formatString = exportMetadata.currencySymbol + "##,##0";
                    if (analysisMeasure.getPrecision() > 0) {
                        formatString += ".";
                    }
                    for (int i = 0; i < analysisMeasure.getPrecision(); i++) {
                        formatString += "0";
                    }
                    currencyStyle.setDataFormat(workbook.createDataFormat().getFormat(formatString));
                    style = new NumericStyle(currencyStyle);
                    break;
                case FormattingConfiguration.MILLISECONDS:
                    CellStyle genericStyle = workbook.createCellStyle();
                    genericStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
                    style = new NumericStyle(genericStyle);
                    break;
                case FormattingConfiguration.PERCENTAGE:
                    CellStyle percentageStyle = workbook.createCellStyle();
                    String percentFormatString = "0";
                    if (analysisMeasure.getPrecision() > 0) {
                        percentFormatString += ".";
                    }
                    for (int i = 0; i < analysisMeasure.getPrecision(); i++) {
                        percentFormatString += "0";
                    }
                    percentageStyle.setDataFormat(workbook.createDataFormat().getFormat(percentFormatString + "%"));
                    style = new NumericStyle(percentageStyle);
                    break;
                default:
                    CellStyle style1 = workbook.createCellStyle();
                    style1.setDataFormat(workbook.createDataFormat().getFormat("0"));
                    CellStyle style2 = workbook.createCellStyle();
                    String decimalFormatString = "0";
                    if (analysisMeasure.getPrecision() > 0) {
                        decimalFormatString += ".";
                    }
                    for (int i = 0; i < analysisMeasure.getPrecision(); i++) {
                        decimalFormatString += "0";
                    }
                    style2.setDataFormat(workbook.createDataFormat().getFormat(decimalFormatString));
                    style = new NumericStyle(style1, style2);
                    break;
            }
        } else if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
            int dateFormat = exportMetadata.dateFormat;
            CellStyle cellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
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
                cellStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
            }
            style = new DateStyle(cellStyle);
        } else if (analysisItem.hasType(AnalysisItemTypes.TEXT)) {
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);
            style = new StringStyle(cellStyle, analysisItem, workbook);
        } else {
            boolean wordWrap = false;
            if (analysisItem.hasType(AnalysisItemTypes.DERIVED_DIMENSION)) {
                DerivedAnalysisDimension dim = (DerivedAnalysisDimension) analysisItem;
                wordWrap = dim.isWordWrap();
            }
            CellStyle genericStyle = workbook.createCellStyle();
            genericStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
            if (textExtension != null) {
                wordWrap = textExtension.isWordWrap() || wordWrap;
            }
            genericStyle.setWrapText(wordWrap);
            style = new StringStyle(genericStyle, analysisItem, workbook);
        }


        return style;
    }


    private Style getStyle(Map<AnalysisItem, Style> styleMap, AnalysisItem analysisItem, Workbook wb, ExportMetadata exportMetadata, Value value) {
        Style style = styleMap.get(analysisItem);
        if (style == null) {
            style = createStyle(analysisItem, wb, exportMetadata);
            styleMap.put(analysisItem, style);
        }
        return style;
    }

    public static ExportMetadata createExportMetadata(long accountID, EIConnection conn, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        int dateFormat;
        String currencySymbol;
        String locale;
        try {
            PreparedStatement dateFormatStmt = conn.prepareStatement("SELECT DATE_FORMAT, CURRENCY_SYMBOL, ACCOUNT_LOCALE FROM ACCOUNT WHERE ACCOUNT_ID = ?");
            dateFormatStmt.setLong(1, accountID);
            ResultSet rs = dateFormatStmt.executeQuery();
            if (rs.next()) {
                dateFormat = rs.getInt(1);
                currencySymbol = rs.getString(2);
                locale = rs.getString(3);
            } else {
                dateFormat = 1;
                currencySymbol = "$";
                locale = "EN";
            }
        } catch (com.easyinsight.security.SecurityException e) {
            dateFormat = 1;
            currencySymbol = "$";
            locale = "EN";
        }
        int time = insightRequestMetadata.getUtcOffset() / 60;
        String string;
        if (time > 0) {
            string = "GMT-" + Math.abs(time);
        } else if (time < 0) {
            string = "GMT+" + Math.abs(time);
        } else {
            string = "GMT";
        }
        TimeZone timeZone = TimeZone.getTimeZone(string);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(timeZone);
        Locale localeObject;
        localeObject = createLocale(locale);
        return new ExportMetadata(dateFormat, currencySymbol, cal, localeObject);
    }

    public static Locale createLocale(String locale) {
        Locale localeObject;
        if ("EN".equals(locale)) {
            localeObject = new Locale("en", "US");
        } else if ("UK".equals(locale)) {
            localeObject = new Locale("en", "GB");
        } else if ("DE".equals(locale)) {
            localeObject = new Locale("de", "DEU");
        } else if ("ES".equals(locale)) {
            localeObject = new Locale("es", "ESP");
        } else {
            localeObject = new Locale("en", "US");
        }
        return localeObject;
    }

    private static final String headerLabelStyle = "text-align:center;padding-top:15px;padding-bottom:15px;font-size:14px";
    private static final String tableStyle = "font-size:12px;border-collapse:collapse;border-style:solid;border-width:1px;border-spacing:0;border-color:#000000;width:100%";
    private static final String thStyle = "border-style:solid;padding:6px;border-width:1px;border-color:#000000";
    private static final String headerTRStyle = "background-color:#EEEEEE";
    private static final String trStyle = "padding:0px;margin:0px";
    private static final String summaryTRStyle = "padding:0px;margin:0px;background-color:#F4F4F4";
    private static final String tdStyle = "border-color:#000000;padding:6px;border-style:solid;border-width:1px;text-align:";

    public static String treeReportToHTMLTableWithActualCSS(WSAnalysisDefinition report, EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean includeTitle) throws SQLException {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);
        PipelineData pipelineData = dataSet.getPipelineData();
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);

        StringBuilder sb = new StringBuilder();
        java.util.List<AnalysisItem> items = new java.util.ArrayList<AnalysisItem>(report.getAllAnalysisItems());
        items.remove(null);
        java.util.Collections.sort(items, new java.util.Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        });

        WSTreeDefinition tree = (WSTreeDefinition) report;

        sb.append("<table style=\"").append(tableStyle).append("\">");
        sb.append("<tr style=\"").append(headerTRStyle).append("\">");

        AnalysisHierarchyItem hierarchy = (AnalysisHierarchyItem) tree.getHierarchy();
        sb.append("<th style=\"").append(thStyle).append("\">");
        sb.append(hierarchy.toUnqualifiedDisplay());
        sb.append("</th>");
        for (AnalysisItem analysisItem : tree.getItems()) {
            sb.append("<th style=\"").append(thStyle).append("\">");
            sb.append(analysisItem.toUnqualifiedDisplay());
            sb.append("</th>");
        }
        sb.append("</tr>");

        TreeData treeData = new TreeData(tree, hierarchy, exportMetadata, dataSet);
        for (IRow row : dataSet.getRows()) {
            treeData.addRow(row);
        }
        List<TreeRow> rows = treeData.toTreeRows(pipelineData);
        for (TreeRow row : rows) {
            sb.append(row.toHTML(tree, exportMetadata));
        }

        sb.append("</table>");

        return sb.toString();
    }

    public static String treeReportToHTMLTable(WSAnalysisDefinition report, EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean includeTitle) throws SQLException {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);
        PipelineData pipelineData = dataSet.getPipelineData();
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);

        StringBuilder sb = new StringBuilder();
        java.util.List<AnalysisItem> items = new java.util.ArrayList<AnalysisItem>(report.getAllAnalysisItems());
        items.remove(null);
        java.util.Collections.sort(items, new java.util.Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        });

        WSTreeDefinition tree = (WSTreeDefinition) report;

        sb.append("<table style=\"").append(tableStyle).append("\">");
        sb.append("<tr style=\"").append(headerTRStyle).append("\">");

        AnalysisHierarchyItem hierarchy = (AnalysisHierarchyItem) tree.getHierarchy();
        sb.append("<th style=\"").append(thStyle).append("\">");
        sb.append(hierarchy.toUnqualifiedDisplay());
        sb.append("</th>");
        for (AnalysisItem analysisItem : tree.getItems()) {
            sb.append("<th style=\"").append(thStyle).append("\">");
            sb.append(analysisItem.toUnqualifiedDisplay());
            sb.append("</th>");
        }
        sb.append("</tr>");

        TreeData treeData = new TreeData(tree, hierarchy, exportMetadata, dataSet);
        for (IRow row : dataSet.getRows()) {
            treeData.addRow(row);
        }
        List<TreeRow> rows = treeData.toTreeRows(pipelineData);
        for (TreeRow row : rows) {
            sb.append(row.toHTML(tree, exportMetadata));
        }

        sb.append("</table>");

        return sb.toString();
    }

    public static String listReportToHTMLTable(WSAnalysisDefinition report, ListDataResults listDataResults, EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean includeTitle,
                                               ExportProperties exportProperties) throws SQLException {

        if (report.getReportType() == WSAnalysisDefinition.LIST) {
            WSListDefinition list = (WSListDefinition) report;
            if (list.isSummaryTotal()) {
                listDataResults.summarize();
            }
        }

        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);

        StringBuilder sb = new StringBuilder();
        java.util.List<AnalysisItem> items = new java.util.ArrayList<AnalysisItem>(report.getAllAnalysisItems());
        items.remove(null);
        java.util.Collections.sort(items, new java.util.Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        });


        if (includeTitle && report.getName() != null) {
            sb.append("<div style=\"").append(headerLabelStyle).append("\">").append("<h0>").append(report.getName()).append("</h0>").append("</div>");
        }

        sb.append("<table style=\"").append(tableStyle).append("\">");
        sb.append("<thead>");
        sb.append("<tr style=\"").append(headerTRStyle).append("\">");
        Map<AnalysisItem, Link> linkMap = new HashMap<AnalysisItem, Link>();
        for (AnalysisItem analysisItem : items) {
            if (analysisItem.getLinks() != null) {
                Link defaultLink = null;
                if (includeTitle) {
                    for (Link link : analysisItem.getLinks()) {
                        if (link.isDefaultLink() && link instanceof URLLink) {
                            defaultLink = link;
                        } else if (defaultLink == null && link instanceof URLLink) {
                            defaultLink = link;
                        }
                    }
                } else {
                    for (Link link : analysisItem.getLinks()) {
                        if (link.isDefaultLink()) {
                            defaultLink = link;
                        }
                    }
                    if (defaultLink == null && analysisItem.getLinks().size() == 1) {
                        defaultLink = analysisItem.getLinks().get(0);
                    }
                }

                linkMap.put(analysisItem, defaultLink);
            }
            for (AnalysisItem headerItem : listDataResults.getHeaders()) {
                if (headerItem == analysisItem) {
                    sb.append("<th style=\"").append(thStyle).append("\">");
                    sb.append(headerItem.toUnqualifiedDisplay());
                    sb.append("</th>");
                }
            }
        }
        sb.append("</tr>");
        sb.append("</thead>");
        sb.append("<tbody>");
        for (com.easyinsight.analysis.ListRow listRow : listDataResults.getRows()) {
            sb.append("<tr style=\"").append(trStyle).append("\">");
            for (AnalysisItem analysisItem : items) {
                for (int i = 0; i < listDataResults.getHeaders().length; i++) {
                    AnalysisItem headerItem = listDataResults.getHeaders()[i];
                    if (headerItem == analysisItem) {
                        StringBuilder styleString = new StringBuilder(tdStyle);
                        String align = "left";
                        if (headerItem.getReportFieldExtension() != null && headerItem.getReportFieldExtension() instanceof TextReportFieldExtension) {
                            TextReportFieldExtension textReportFieldExtension = (TextReportFieldExtension) headerItem.getReportFieldExtension();
                            if (textReportFieldExtension.getAlign() != null) {
                                if ("Left".equals(textReportFieldExtension.getAlign())) {
                                    align = "left";
                                } else if ("Center".equals(textReportFieldExtension.getAlign())) {
                                    align = "center";
                                } else if ("Right".equals(textReportFieldExtension.getAlign())) {
                                    align = "right";
                                }
                            }
                            styleString.append(align);
                            if (textReportFieldExtension.getFixedWidth() > 0) {
                                styleString.append(";width:").append(textReportFieldExtension.getFixedWidth()).append("px");
                            }
                        } else {
                            styleString.append(align);
                        }
                        com.easyinsight.core.Value value = listRow.getValues()[i];
                        if (value.getValueExtension() != null && value.getValueExtension() instanceof TextValueExtension) {
                            TextValueExtension textValueExtension = (TextValueExtension) value.getValueExtension();
                            if (textValueExtension.getColor() != 0) {
                                String hexString = createHexString(textValueExtension.getColor());
                                styleString.append(";color:").append(hexString);
                            }
                            if (textValueExtension.getBackgroundColor() != TextValueExtension.WHITE) {
                                String hexString = createHexString(textValueExtension.getBackgroundColor());
                                styleString.append(";background-color:").append(hexString);
                            }
                            if (textValueExtension.isBold()) {
                                styleString.append(";font-weight:bold");
                            }
                        }
                        sb.append("<td style=\"").append(styleString.toString()).append("\">");
                        //sb.append("<td>");
                        if (!exportProperties.isEmailed()) {
                            sb.append("<span class='sortData'>");
                            Value v = value;
                            if (value.getSortValue() != null) {
                                v = value.getSortValue();
                            }
                            sb.append(StringEscapeUtils.escapeHtml(v.toHTMLString()));
                            sb.append("</span>");
                        }

                        Link defaultLink = linkMap.get(analysisItem);
                        boolean showLink = false;
                        if (defaultLink != null) {
                            if (defaultLink instanceof URLLink) {
                                showLink = defaultLink != null && value.getLinks() != null && value.getLinks().get(defaultLink.getLabel()) != null;
                                if (showLink) {
                                    sb.append("<a href=\"");
                                    sb.append(value.getLinks().get(defaultLink.getLabel()));
                                    sb.append("\">");
                                }
                            } else if (defaultLink instanceof DrillThrough && !exportProperties.isEmailed()) {
                                StringBuilder paramBuilder = new StringBuilder();
                                DrillThrough drillThrough = (DrillThrough) defaultLink;
                                paramBuilder.append("drillThrough('reportID=").append(report.getUrlKey()).append("&embedded=").append(exportProperties.isEmbedded()).append("&drillthroughID=").append(drillThrough.createID()).append("&").append("sourceField=").append(analysisItem.getAnalysisItemID()).append("&");
                                if (exportProperties.getEmbedKey() != null) {
                                    paramBuilder.append("&embedKey=" + exportProperties.getEmbedKey() + "&");
                                }
                                for (AnalysisItem dataItem : items) {
                                    for (int k = 0; k < listDataResults.getHeaders().length; k++) {
                                        AnalysisItem dataHeaderItem = listDataResults.getHeaders()[k];
                                        String encodedValue;
                                        if (dataItem == dataHeaderItem) {
                                            try {
                                                Value drillthroughValue = listRow.getValues()[k];
                                                String drillthroughValueString;
                                                if (drillthroughValue.type() == Value.NUMBER) {
                                                    drillthroughValueString = String.valueOf(drillthroughValue.toDouble().intValue());
                                                } else {
                                                    drillthroughValueString = drillthroughValue.toString();
                                                }
                                                encodedValue = StringEscapeUtils.escapeHtml(URLEncoder.encode(drillthroughValueString, "UTF-8"));
                                            } catch (UnsupportedEncodingException e) {
                                                throw new RuntimeException(e);
                                            }
                                            paramBuilder.append("f").append(dataItem.getAnalysisItemID()).append("=").append(encodedValue).append("&");
                                        }
                                    }
                                }
                                paramBuilder.append("')");
                                sb.append("<a href=\"#\" onclick=\"");
                                sb.append(paramBuilder.toString());
                                sb.append("\">");
                                showLink = true;
                            }
                        }


                        sb.append(createValue(exportMetadata.dateFormat, headerItem, value, exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false));
                        if (showLink) {
                            sb.append("</a>");
                        }
                        sb.append("</td>");
                    }
                }
            }
            sb.append("</tr>");
        }
        sb.append("</tbody>");
        if (report.getReportType() == WSAnalysisDefinition.LIST) {
            WSListDefinition list = (WSListDefinition) report;
            if (list.isSummaryTotal()) {
                sb.append("<tfoot>");
                sb.append("<tr style=\"").append(summaryTRStyle).append("\">");
                for (AnalysisItem analysisItem : items) {
                    for (int j = 0; j < listDataResults.getHeaders().length; j++) {
                        AnalysisItem headerItem = listDataResults.getHeaders()[j];

                        if (headerItem == analysisItem) {
                            if (headerItem.hasType(AnalysisItemTypes.MEASURE)) {
                                double summary = listDataResults.getSummaries()[j];
                                if (Double.isNaN(summary) || Double.isInfinite(summary)) {
                                    summary = 0;
                                }
                                StringBuilder styleString = new StringBuilder(tdStyle);
                                String align = "left";
                                if (headerItem.getReportFieldExtension() != null && headerItem.getReportFieldExtension() instanceof TextReportFieldExtension) {
                                    TextReportFieldExtension textReportFieldExtension = (TextReportFieldExtension) headerItem.getReportFieldExtension();
                                    if (textReportFieldExtension.getAlign() != null) {
                                        if ("Left".equals(textReportFieldExtension.getAlign())) {
                                            align = "left";
                                        } else if ("Center".equals(textReportFieldExtension.getAlign())) {
                                            align = "center";
                                        } else if ("Right".equals(textReportFieldExtension.getAlign())) {
                                            align = "right";
                                        }
                                    }
                                    styleString.append(align);
                                    if (textReportFieldExtension.getFixedWidth() > 0) {
                                        styleString.append(";width:").append(textReportFieldExtension.getFixedWidth()).append("px");
                                    }
                                } else {
                                    styleString.append(align);
                                }
                                sb.append("<td style=\"").append(styleString.toString()).append("\">");
                                sb.append(com.easyinsight.export.ExportService.createValue(exportMetadata.dateFormat, headerItem, new NumericValue(summary), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false));
                                sb.append("</td>");
                            } else {
                                sb.append("<td></td>");
                            }
                        }
                    }
                }
                sb.append("</tr>");
                sb.append("</tfoot>");
            }
        }

        sb.append("</table>");
        return sb.toString();
    }

    public static String listReportToHTMLTableWithActualCSS(WSAnalysisDefinition report, ListDataResults listDataResults, EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean includeTitle,
                                                            ExportProperties exportProperties) throws SQLException {

        if (report.getReportType() == WSAnalysisDefinition.LIST) {
            WSListDefinition list = (WSListDefinition) report;
            if (list.isSummaryTotal()) {
                listDataResults.summarize();
            }
        }

        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);

        StringBuilder sb = new StringBuilder();
        java.util.List<AnalysisItem> items = new java.util.ArrayList<AnalysisItem>(report.getAllAnalysisItems());
        items.remove(null);
        Comparator sortOrder = new java.util.Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        };

        java.util.Collections.sort(items, sortOrder);
        List<AnalysisItem> headers = new ArrayList(Arrays.asList(listDataResults.getHeaders()));
        List<AnalysisItem> unsortedHeaders = Arrays.asList(listDataResults.getHeaders());
        java.util.Collections.sort(headers, sortOrder);

        if (includeTitle && report.getName() != null) {
            sb.append("<div style=\"").append(headerLabelStyle).append("\">").append("<h0>").append(report.getName()).append("</h0>").append("</div>");
        }
        String backgroundColor1 = "#f9f9f9";
        String backgroundColor2 = "#ffffff";
        if (report instanceof WSListDefinition) {
            WSListDefinition list = (WSListDefinition) report;
            backgroundColor1 = createHexString(list.getRowColor1());
            backgroundColor2 = createHexString(list.getRowColor2());
        }
        sb.append("<style type=\"text/css\">").
                append(".reportTable").append(report.getAnalysisID()).append(" > tbody > tr:nth-child(odd) > td,\n" +
                ".reportTable").append(report.getAnalysisID()).append(" > tbody > tr:nth-child(odd) > th {\n" +
                "  background-color: ").append(backgroundColor1).append(";\n" +
                "}").append("}");
        sb.append(".reportTable").append(report.getAnalysisID()).append(" > tbody > tr:nth-child(even) > td,\n" +
                ".reportTable").append(report.getAnalysisID()).append(" > tbody > tr:nth-child(even) > th {\n" +
                "  background-color: ").append(backgroundColor2).append(";\n" +
                "}").append("}");
        /*sb.append(".reportTable").append(report.getAnalysisID()).append("{ font-size:").append(report.getFontSize()).append("px;\n").append("}");*/
        sb.append("</style>");
        sb.append("<table style=\"font-size:").append(report.getFontSize()).append("px").append("\" class=\"table ").append("reportTable").append(report.getAnalysisID()).append(" table-bordered table-hover table-condensed\">");
        sb.append("<thead>");
        sb.append("<tr>");
        Map<AnalysisItem, Link> linkMap = new HashMap<AnalysisItem, Link>();

        for (AnalysisItem headerItem : headers) {
            sb.append("<th style=\"text-align:center\">");
            sb.append(headerItem.toUnqualifiedDisplay());
            sb.append("</th>");
            if (headerItem.getLinks() != null) {
                Link defaultLink = null;
                if (includeTitle) {
                    for (Link link : headerItem.getLinks()) {
                        if (link.isDefaultLink() && link instanceof URLLink) {
                            defaultLink = link;
                        } else if (defaultLink == null && link instanceof URLLink) {
                            defaultLink = link;
                        }
                    }
                } else {
                    for (Link link : headerItem.getLinks()) {
                        if (link.isDefaultLink()) {
                            defaultLink = link;
                        }
                    }
                    if (defaultLink == null && headerItem.getLinks().size() == 1) {
                        defaultLink = headerItem.getLinks().get(0);
                    }
                }

                linkMap.put(headerItem, defaultLink);
            }
        }
        sb.append("</tr>");
        sb.append("</thead>");
        sb.append("<tbody>");
        for (com.easyinsight.analysis.ListRow listRow : listDataResults.getRows()) {
            sb.append("<tr>");
            for (AnalysisItem analysisItem : headers) {
                AnalysisItem headerItem = analysisItem;
                if (headerItem == analysisItem) {
                    StringBuilder styleString = new StringBuilder("text-align:");
                    String align = "left";
                    if (headerItem.getReportFieldExtension() != null && headerItem.getReportFieldExtension() instanceof TextReportFieldExtension) {
                        TextReportFieldExtension textReportFieldExtension = (TextReportFieldExtension) headerItem.getReportFieldExtension();
                        if (textReportFieldExtension.getAlign() != null) {
                            if ("Left".equals(textReportFieldExtension.getAlign())) {
                                align = "left";
                            } else if ("Center".equals(textReportFieldExtension.getAlign())) {
                                align = "center";
                            } else if ("Right".equals(textReportFieldExtension.getAlign())) {
                                align = "right";
                            }
                        }
                        styleString.append(align);
                        if (textReportFieldExtension.getFixedWidth() > 0) {
                            styleString.append(";width:").append(textReportFieldExtension.getFixedWidth()).append("px");
                        }
                    } else {
                        styleString.append(align);
                    }
                    com.easyinsight.core.Value value = listRow.getValues()[unsortedHeaders.indexOf(analysisItem)];
                    if (value.getValueExtension() != null && value.getValueExtension() instanceof TextValueExtension) {
                        TextValueExtension textValueExtension = (TextValueExtension) value.getValueExtension();
                        if (textValueExtension.getColor() != 0) {
                            String hexString = createHexString(textValueExtension.getColor());
                            styleString.append(";color:").append(hexString);
                        }
                        if (textValueExtension.getBackgroundColor() != TextValueExtension.WHITE) {
                            String hexString = createHexString(textValueExtension.getBackgroundColor());
                            styleString.append(";background-color:").append(hexString);
                        }
                        if (textValueExtension.isBold()) {
                            styleString.append(";font-weight:bold");
                        }
                    }

                    sb.append("<td style=\"").append(styleString).append("\">");
                    if (!exportProperties.isEmailed()) {
                        sb.append("<span class='sortData'>");
                        Value v = value;
                        if (value.getSortValue() != null) {
                            v = value.getSortValue();
                        }
                        sb.append(StringEscapeUtils.escapeHtml(v.toHTMLString()));
                        sb.append("</span>");
                    }

                    Link defaultLink = linkMap.get(analysisItem);
                    boolean showLink = false;
                    if (defaultLink != null) {
                        if (defaultLink instanceof URLLink) {
                            showLink = defaultLink != null && value.getLinks() != null && value.getLinks().get(defaultLink.getLabel()) != null;
                            if (showLink) {
                                sb.append("<a href=\"");
                                sb.append(value.getLinks().get(defaultLink.getLabel()));
                                sb.append("\">");
                            }
                        } else if (defaultLink instanceof DrillThrough && !exportProperties.isEmailed()) {
                            StringBuilder paramBuilder = new StringBuilder();
                            DrillThrough drillThrough = (DrillThrough) defaultLink;
//                                paramBuilder.append("drillThrough('reportID=").append(report.getUrlKey()).append("&embedded=").append(exportProperties.isEmbedded()).append("&drillthroughID=").append(drillThrough.createID()).append("&").append("sourceField=").append(analysisItem.getAnalysisItemID()).append("&");
//                                if (exportProperties.getEmbedKey() != null) {
//                                    paramBuilder.append("&embedKey=" + exportProperties.getEmbedKey()+"&");
//                                }
                            sb.append("<a class=\"list_drillthrough\" href=\"#\" data-reportid=\"");
                                                            sb.append(report.getUrlKey());
                                                            sb.append("\" data-drillthroughid=\"");
                                                            sb.append(drillThrough.createID());
                                                            sb.append("\" data-embedded=\"");
                                                            sb.append(exportProperties.isEmbedded());
                                                            sb.append("\" data-source=\"");
                                                            sb.append(analysisItem.getAnalysisItemID());
                                                            sb.append("\"");
                            if (drillThrough.isFilterRowGroupings()) {


                                for (AnalysisItem dataItem : headers) {
                                    if (dataItem.hasType(AnalysisItemTypes.DIMENSION)) {
                                        int k = unsortedHeaders.indexOf(dataItem);
                                        AnalysisItem dataHeaderItem = listDataResults.getHeaders()[k];
                                        String encodedValue;

                                        try {
                                            encodedValue = toDrillthroughValue(listRow.getValues()[k]);
                                            sb.append(" data-drillthrough");
                                            sb.append(dataItem.getAnalysisItemID());
                                            sb.append("=\"");
                                            sb.append(encodedValue);
                                            sb.append("\"");
                                        } catch (UnsupportedEncodingException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }

                                }
                            } else {
                                sb.append(" data-drillthrough");
                                sb.append(headerItem.getAnalysisItemID());
                                sb.append("=\"");

                                try {
                                    sb.append(toDrillthroughValue(value));
                                } catch (UnsupportedEncodingException e) {
                                    throw new RuntimeException(e);
                                }
                                sb.append("\"");
                            }
                            sb.append(">");
                            showLink = true;
                        }
                    }


                        sb.append(createValue(exportMetadata.dateFormat, headerItem, value, exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false));
                        if (showLink) {
                            sb.append("</a>");
                        }
                        sb.append("</td>");
                    }
                    sb.append("</td>");
                }
            sb.append("</tr>");
        }

        sb.append("</tbody>");
        if (report.getReportType() == WSAnalysisDefinition.LIST) {
            WSListDefinition list = (WSListDefinition) report;
            if (list.isSummaryTotal()) {
                sb.append("<tfoot>");
                sb.append("<tr style=\"").append(summaryTRStyle).append("\">");
                for (AnalysisItem analysisItem : items) {
                    for (int j = 0; j < listDataResults.getHeaders().length; j++) {
                        AnalysisItem headerItem = listDataResults.getHeaders()[j];

                        if (headerItem == analysisItem) {
                            if (headerItem.hasType(AnalysisItemTypes.MEASURE)) {
                                Value summary = (Value) listDataResults.getAdditionalProperties().get("summary" + headerItem.qualifiedName());
                                if (summary == null) {
                                    sb.append("<td></td>");
                                } else {
                                    double doubleValue = summary.toDouble();
                                    if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
                                        doubleValue = 0;
                                    }
                                    StringBuilder styleString = new StringBuilder("text-align:");
                                    String align = "left";
                                    if (headerItem.getReportFieldExtension() != null && headerItem.getReportFieldExtension() instanceof TextReportFieldExtension) {
                                        TextReportFieldExtension textReportFieldExtension = (TextReportFieldExtension) headerItem.getReportFieldExtension();
                                        if (textReportFieldExtension.getAlign() != null) {
                                            if ("Left".equals(textReportFieldExtension.getAlign())) {
                                                align = "left";
                                            } else if ("Center".equals(textReportFieldExtension.getAlign())) {
                                                align = "center";
                                            } else if ("Right".equals(textReportFieldExtension.getAlign())) {
                                                align = "right";
                                            }
                                        }
                                        styleString.append(align);
                                        if (textReportFieldExtension.getFixedWidth() > 0) {
                                            styleString.append(";width:").append(textReportFieldExtension.getFixedWidth()).append("px");
                                        }
                                    } else {
                                        styleString.append(align);
                                    }
                                    if (summary.getValueExtension() != null && summary.getValueExtension() instanceof TextValueExtension) {
                                        TextValueExtension textValueExtension = (TextValueExtension) summary.getValueExtension();
                                        if (textValueExtension.getColor() != 0) {
                                            String hexString = createHexString(textValueExtension.getColor());
                                            styleString.append(";color:").append(hexString);
                                        }
                                        if (textValueExtension.getBackgroundColor() != TextValueExtension.WHITE) {
                                            String hexString = createHexString(textValueExtension.getBackgroundColor());
                                            styleString.append(";background-color:").append(hexString);
                                        }
                                        if (textValueExtension.isBold()) {
                                            styleString.append(";font-weight:bold");
                                        }
                                    }
                                    sb.append("<td style=\"").append(styleString.toString()).append("\">");
                                    sb.append(com.easyinsight.export.ExportService.createValue(exportMetadata.dateFormat, headerItem, new NumericValue(doubleValue), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false));
                                    sb.append("</td>");
                                }
                            } else {
                                sb.append("<td></td>");
                            }
                        }
                    }
                }
                sb.append("</tr>");
                sb.append("</tfoot>");
            }
        }

        sb.append("</table>");
        return sb.toString();
    }

    private static String toDrillthroughValue(Value value) throws UnsupportedEncodingException {
        String encodedValue;
        Value drillthroughValue = value;
        String drillthroughValueString;
        if (drillthroughValue.type() == Value.NUMBER) {
            drillthroughValueString = String.valueOf(drillthroughValue.toDouble().intValue());
        } else {
            drillthroughValueString = drillthroughValue.toString();
        }
        encodedValue = StringEscapeUtils.escapeHtml(URLEncoder.encode(drillthroughValueString, "UTF-8"));
        return encodedValue;
    }

    public static String createHexString(int color) {
        String hexString = Integer.toHexString(color);
        if (hexString.length() == 4) {
            hexString = "00" + hexString;
        } else if (hexString.length() == 2) {
            hexString = "0000" + hexString;
        }
        return "#" + hexString;
    }

    public void exportScorecardToXLS(long scorecardID, InsightRequestMetadata insightRequestMetadata) throws Exception {
        SecurityUtil.authorizeScorecard(scorecardID);
        EIConnection conn = Database.instance().getConnection();
        try {
            ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);
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
            Map<AnalysisItem, Style> styleMap = new HashMap<AnalysisItem, Style>();

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
            AnalysisMeasure percentMeasure = new AnalysisMeasure();
            percentMeasure.setFormattingType(FormattingConfiguration.PERCENTAGE);
            percentMeasure.setMinPrecision(1);
            percentMeasure.setPrecision(1);
            int i = 1;
            for (KPI kpi : scorecard.getKpis()) {
                HSSFRow kpiRow = sheet.createRow(i++);
                HSSFCell kpiNameCell = kpiRow.createCell(0);
                kpiNameCell.setCellValue(new HSSFRichTextString(kpi.getName()));

                Style style = getStyle(styleMap, kpi.getAnalysisMeasure(), workbook, exportMetadata, new NumericValue(kpi.getKpiOutcome().getOutcomeValue()));
                style.format(kpiRow, 1, new NumericValue(kpi.getKpiOutcome().getOutcomeValue()), kpi.getAnalysisMeasure(), exportMetadata.cal);

                HSSFCell timeCell = kpiRow.createCell(2);
                timeCell.setCellValue(kpi.getDayWindow());
                Value percentValue = new NumericValue(kpi.getKpiOutcome().getPercentChange());
                Style percentStyle = getStyle(styleMap, percentMeasure, workbook, exportMetadata, percentValue);
                percentStyle.format(kpiRow, 3, percentValue, percentMeasure, exportMetadata.cal);
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
        ExportMetadata exportMetadata = createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);
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
        if (scorecard.getName() != null) {
            sb.append("<div style=\"").append(headerLabelStyle).append("\">").append("<h0>").append(scorecard.getName()).append("</h0>").append("</div>");
        }
        sb.append("<table style=\"").append(tableStyle).append("\">");
        sb.append("<tr style=\"" + headerTRStyle + "\">");
        sb.append("<th style=\"" + thStyle + "\"></th>");
        sb.append("<th style=\"" + thStyle + "\">KPI Name</th>");
        sb.append("<th style=\"" + thStyle + "\">Latest Value</th>");
        sb.append("<th style=\"" + thStyle + "\">Time</th>");
        sb.append("<th style=\"" + thStyle + "\">% Change</th>");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        sb.append("</tr>");
        int i = 1;
        for (KPI kpi : scorecard.getKpis()) {
            sb.append("<tr>");
            //String tdStyle = "padding: 8px;color: #669;";
            /*if (i % 2 == 1) {
                tdStyle += "background: #e8edff;";
            }*/
            sb.append("<td style=\"").append(tdStyle).append("center\">");
            sb.append("<img src=\"http://www.easy-insight.com/icons/16x16/").append(kpi.getIconImage()).append("\"/>");
            sb.append("</td>");
            sb.append("<td style=\"").append(tdStyle).append("left\">");
            sb.append(kpi.getName());
            sb.append("</td>");
            sb.append("<td style=\"").append(tdStyle).append("right\">");
            sb.append(createValue(0, kpi.getAnalysisMeasure(), new NumericValue(kpi.getKpiOutcome().getOutcomeValue()), exportMetadata.cal, exportMetadata.currencySymbol, exportMetadata.locale, false));
            sb.append("</td>");
            sb.append("<td style=\"").append(tdStyle).append("right\">");
            sb.append(kpi.getDayWindow());
            sb.append(" days");
            sb.append("</td>");
            sb.append("<td style=\"").append(tdStyle).append("right\">");
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
