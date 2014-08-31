package com.easyinsight.export;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.*;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.dashboard.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.html.UIData;
import com.easyinsight.html.Utils;
import com.easyinsight.logging.LogClass;
import com.easyinsight.preferences.ImageDescriptor;
import com.easyinsight.preferences.PreferencesService;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.util.RandomTextGenerator;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSUtils;
import org.hibernate.Session;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;

/**
 * User: jamesboe
 * Date: 6/27/14
 * Time: 9:45 AM
 */
public class DashboardPDF {

    public static final int PENDING = 0;
    public static final int SUCCESS = 1;
    public static final int FAILURE = 2;

    public static final int PDF = 0;
    public static final int PNG = 1;

    public static Element generatePDF(WSAnalysisDefinition report, int width, int height, EIConnection conn) throws SQLException, BadElementException, IOException, CloneNotSupportedException {
        return (Element) blah(report, width, height, conn, PDF);
    }

    public static byte[] generatePNG(WSAnalysisDefinition report, int width, int height, EIConnection conn) throws SQLException, BadElementException, IOException, CloneNotSupportedException {
        return (byte[]) blah(report, width, height, conn, PNG);
    }

    public static Object blah(WSAnalysisDefinition report, int width, int height, EIConnection conn, int outputFormat) throws SQLException, BadElementException, IOException, CloneNotSupportedException {



        PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO image_selenium_trigger (report_id, image_state, user_id, " +
                "image_preferred_width, image_preferred_height, created_at, updated_at, selenium_username, selenium_password) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        saveStmt.setLong(1, report.getAnalysisID());
        saveStmt.setInt(2, PENDING);
        saveStmt.setLong(3, SecurityUtil.getUserID());
        saveStmt.setInt(4, width);
        saveStmt.setInt(5, height);
        saveStmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
        saveStmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
        String u = RandomTextGenerator.generateText(25);
        String p = RandomTextGenerator.generateText(25);
        saveStmt.setString(8, u);
        saveStmt.setString(9, p);
        saveStmt.execute();
        long id = Database.instance().getAutoGenKey(saveStmt);

        PreparedStatement saveFilterStmt = conn.prepareStatement("INSERT INTO image_selenium_trigger_to_filter (image_selenium_trigger_id, filter_id) values (?, ?)");
        Session session = Database.instance().createSession(conn);
        try {
            for (FilterDefinition filter : report.getFilterDefinitions()) {
                filter = filter.clone();
                filter.beforeSave(session);
                session.save(filter);
                session.flush();
                saveFilterStmt.setLong(1, id);
                saveFilterStmt.setLong(2, filter.getFilterID());
                saveFilterStmt.execute();
            }
        } finally {
            session.close();
        }
        saveFilterStmt.close();

        String url = "/app/embed/seleniumReport.jsp?seleniumUserName={0}&seleniumPassword={1}&seleniumID={2}";
        String formatted = MessageFormat.format(url, u, p, String.valueOf(id));
        System.out.println("https://www.easy-insight.com" + formatted);

        PDFImageData imageData = launchAndWaitForRequest(formatted, conn, id);

        if (outputFormat == PDF) {

            Image image = Image.getInstance(imageData.getBytes());
            //float percent = (landscapeOrientation ? 770f : 523f) / page.getWidth() * 100;
            float percent = 770f / imageData.getWidth() * 100;
            //float percent = 50f;
            image.setBorder(Image.NO_BORDER);
            image.scalePercent(percent);
            image.setAlignment(Element.ALIGN_CENTER);
            return image;
        } else if (outputFormat == PNG) {
            return imageData.getBytes();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    //public static final String OUTBOUND_QUEUE = "EISelenium";

    private static PDFImageData launchAndWaitForRequest(String url, EIConnection conn, long id) {
        // send an SQS request
        try {
            MessageQueue msgQueue = SQSUtils.connectToQueue(ConfigLoader.instance().getBaseSeleniumQueue(), "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
            msgQueue.sendMessage(url);

            PreparedStatement stmt = conn.prepareStatement("SELECT image_state FROM image_selenium_trigger WHERE image_selenium_trigger_id = ?");

            for (int i = 0; i < 60; i++) {
                System.out.println("Checking...");
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                int state = rs.getInt(1);
                if (state == FAILURE) {
                    throw new RuntimeException();
                } else if (state == SUCCESS) {
                    PreparedStatement findDetailStmt = conn.prepareStatement("SELECT image_response, image_preferred_width, image_preferred_height FROM image_selenium_trigger WHERE " +
                            "image_selenium_trigger_id = ?");
                    findDetailStmt.setLong(1, id);
                    ResultSet detailRS = findDetailStmt.executeQuery();
                    detailRS.next();
                    byte[] bytes = detailRS.getBytes(1);
                    int width = detailRS.getInt(2);
                    int height = detailRS.getInt(3);
                    PDFImageData imageData = new PDFImageData();
                    imageData.setBytes(bytes);
                    imageData.setWidth(width);
                    imageData.setHeight(height);
                    findDetailStmt.close();
                    stmt.close();
                    return imageData;
                } else {
                    System.out.println("Waiting...");
                    Thread.sleep(10000);
                }
            }
            stmt.close();
        } catch (Exception e) {
            LogClass.error(e);
        }
        throw new RuntimeException("Timeout");
    }

    public byte[] createPDF(Dashboard dashboard, DashboardStackPositions selected, Map<String, PDFImageData> images, int timezoneOffset, boolean includeHeader,
                            boolean landscapeOrientation) {
        // have to find the underlying grid of reports
        EIConnection conn = Database.instance().getConnection();
        try {
            Map<String, WSAnalysisDefinition> reportMap = new HashMap<>();
            List<FilterDefinition> replaceFilters = new ArrayList<>();
            for (FilterDefinition filter : dashboard.getFilters()) {
                long filterID = filter.getFilterID();
                System.out.println("dashboard filter ID = " + filterID);
                FilterDefinition overrideFilter = selected.getFilterMap().get(1 + "|" + filterID);
                if (overrideFilter != null) {
                    replaceFilters.add(overrideFilter);
                } else {
                    replaceFilters.add(filter);
                }
                //dashboard.setFilters(replaceFilters);
            }
            DashboardElement root = dashboard.getRootElement();
            DashboardElement element = findElementToRender(root, selected, replaceFilters);
            populateReportData(element, conn, selected, reportMap, replaceFilters);
            Document document;
            if (landscapeOrientation) {
                document = new Document(PageSize.A4.rotate(), 0, 0, 5, 5);
            } else {
                document = new Document(PageSize.A4, 0, 0, 5, 5);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, baos);
            document.open();

            try {
                if (includeHeader) {
                    UIData uiData = Utils.createUIData(conn);
                    DashboardUIProperties dashboardProps = dashboard.findHeaderImage();
                    if (dashboardProps.isImageFullHeader() && dashboardProps.getHeader() != null) {
                        ImageDescriptor imageDescriptor = dashboardProps.getHeader();
                        byte[] bytes = new PreferencesService().getImage(imageDescriptor.getId());
                        Image image = Image.getInstance(bytes);
                        float width = image.getPlainWidth();
                        if (width > 800) {
                            float ratio = 800 / width;
                            float adjustedHeight = ratio * image.getPlainHeight();
                            image.scaleAbsolute(800, adjustedHeight);
                        }
                        image.setAlignment(Element.ALIGN_CENTER);
                        document.add(image);
                    } else if (uiData.getApplicationSkin() != null && uiData.getApplicationSkin().isReportHeader() && uiData.getHeaderImageDescriptor() != null) {
                        ImageDescriptor imageDescriptor = uiData.getHeaderImageDescriptor();
                        byte[] bytes = new PreferencesService().getImage(imageDescriptor.getId());
                        Image image = Image.getInstance(bytes);
                        image.setAlignment(Element.ALIGN_CENTER);
                        document.add(image);
                    }
                }
            } catch (Exception e) {
                LogClass.error(e);
            }

            /*try {
                String string = filterTransformForReport(analysisDefinition);
                com.itextpdf.text.Font phraseFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.NORMAL,
                        new BaseColor(20, 20, 20));
                Phrase phrase = new Phrase(string, phraseFont);
                Paragraph paragraph = new Paragraph(phrase);
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);
            } catch (Exception e) {
                LogClass.error(e);
            }*/

            Element object = populate(element, reportMap, conn, images, dashboard, timezoneOffset);

            document.add(object);
            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static BaseColor fromColor(int colorNumber) {
        Color color = new Color(colorNumber);
        return new BaseColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    private Element populate(DashboardElement element, Map<String, WSAnalysisDefinition> reportMap, EIConnection conn, Map<String, PDFImageData> images,
                             Dashboard dashboard, int timezoneOffset) throws SQLException, DocumentException, IOException, CloneNotSupportedException {
        if (element instanceof DashboardGrid) {
            DashboardGrid dashboardGrid = (DashboardGrid) element;
            PdfPTable table = new PdfPTable(dashboardGrid.getColumns());
            table.setSplitLate(false);
            table.setSplitRows(true);
            table.setHeaderRows(0);
            for (int j = 0; j < dashboardGrid.getRows(); j++) {
                for (int i = 0; i < dashboardGrid.getColumns(); i++) {
                    DashboardElement child = dashboardGrid.findItem(j, i).getDashboardElement();
                    Element e1 = populate(child, reportMap, conn, images, dashboard, timezoneOffset);
                    if (e1 instanceof PdfPTable) {
                        PdfPTable t1 = (PdfPTable) e1;
                        PdfPCell cell = new PdfPCell(t1);
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setPaddingBottom(20);
                        table.addCell(cell);
                    } else if (e1 instanceof Image) {
                        PdfPCell cell = new PdfPCell((Image) e1, true);
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setPaddingBottom(20);
                        table.addCell(cell);
                    } else {
                        throw new RuntimeException("not yet implemented");
                    }
                }
            }
            return table;
        } else if (element instanceof DashboardReport) {
            DashboardReport dashboardReport = (DashboardReport) element;
            PdfPTable table = null;
            PdfPTable stupidIText = null;
            if (dashboardReport.isShowLabel()) {
                table = new PdfPTable(1);
                table.setSplitLate(false);
                table.setSplitRows(true);
                table.setHeaderRows(0);

                com.itextpdf.text.Font font = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, Font.BOLD, fromColor(dashboard.getReportHeaderTextColor()));
                PdfPCell cell = new PdfPCell(new Phrase(dashboardReport.getReport().getName(), font));
                cell.setPaddingBottom(5);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setBackgroundColor(fromColor(dashboard.getReportHeaderBackgroundColor()));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setFixedHeight(20f);
                table.addCell(cell);

                stupidIText = new PdfPTable(1);
                stupidIText.setHeaderRows(0);
                stupidIText.setSplitLate(false);
                stupidIText.setSplitRows(true);
                PdfPCell phCell = new PdfPCell(stupidIText);
                phCell.setPaddingTop(5);
                phCell.setBorder(PdfPCell.NO_BORDER);
                table.addCell(phCell);
            }
            WSAnalysisDefinition report = reportMap.get(dashboardReport.getUrlKey());
            InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
            insightRequestMetadata.setUtcOffset(timezoneOffset);
            ExportMetadata exportMetadata = ExportService.createExportMetadata(conn, insightRequestMetadata);
            Element result;
            if (report.getReportType() == WSAnalysisDefinition.CROSSTAB) {
                result = new ExportService().crosstabToPDFTable(report, conn, insightRequestMetadata, exportMetadata);
            } else if (report.getReportType() == WSAnalysisDefinition.TREND_GRID) {
                result = new ExportService().kpiReportToPDFTable(report, conn, insightRequestMetadata, exportMetadata);
            } else if (report.getReportType() == WSAnalysisDefinition.YTD) {
                result = ExportService.ytdToPDF(report, conn, insightRequestMetadata);
            } else if (report instanceof WSTreeDefinition) {
                result = new ExportService().treeToPDFTable(report, conn, insightRequestMetadata, exportMetadata);
            } else if (report instanceof WSForm) {
                result = new ExportService().formReportToPDF(report, conn, insightRequestMetadata, exportMetadata);
            } else if (report instanceof WSYTDDefinition) {
                result = ExportService.ytdToPDF(report, conn, insightRequestMetadata);
            } else if (report instanceof WSCompareYearsDefinition) {
                result = ExportService.compareYearsToPDF(report, conn, insightRequestMetadata);
            } else if (report instanceof WSTrendDefinition) {
                WSTrendDefinition trend = (WSTrendDefinition) report;
                result = trend.kpiReportToPDFTable(conn, insightRequestMetadata, exportMetadata);
            } else if (report instanceof WSVerticalListDefinition) {
                result = ExportService.verticalListToPDF(report, conn, insightRequestMetadata);
            } else if (report instanceof WSChartDefinition || report instanceof WSGaugeDefinition || report instanceof WSMap) {
                // do we have the chart data as is?
                // if not, we'll need to retrieve via selenium or phantomjs
                PDFImageData imageData = images.get(dashboardReport.getUrlKey());

                if (imageData.getBytes() == null) {
                    result = generatePDF(report, imageData.getWidth(), imageData.getHeight(), conn);
                } else {
                    Image image = Image.getInstance(imageData.getBytes());
                    //float percent = (landscapeOrientation ? 770f : 523f) / page.getWidth() * 100;
                    float percent = 770f / imageData.getWidth() * 100;
                    image.setBorder(Image.NO_BORDER);
                    image.scalePercent(percent);
                    image.setAlignment(Element.ALIGN_CENTER);
                    result = image;
                }
            } else {
                result = new ExportService().listReportToPDFTable(report, conn, insightRequestMetadata, exportMetadata);
            }
            if (table == null) {
                return result;
            } else {
                if (result instanceof PdfPTable) {
                    PdfPTable t1 = (PdfPTable) result;
                    PdfPCell cell = new PdfPCell(t1);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setPaddingBottom(20);
                    stupidIText.addCell(cell);
                } else if (result instanceof Image) {
                    PdfPCell cell = new PdfPCell((Image) result, true);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setPaddingBottom(20);
                    stupidIText.addCell(cell);
                } else {
                    throw new RuntimeException("not yet implemented");
                }
                return table;
            }

        } else {
            throw new RuntimeException();
        }
    }

    private DashboardElement findElementToRender(DashboardElement root, DashboardStackPositions selected, List<FilterDefinition> recurseFilters) {
        if (root instanceof DashboardStack) {
            DashboardStack dashboardStack = (DashboardStack) root;
            Integer index = selected.getPositions().get(dashboardStack.getUrlKey());
            if (dashboardStack.getFilters() != null) {
                for (FilterDefinition filter : dashboardStack.getFilters()) {
                    long filterID = filter.getFilterID();
                    FilterDefinition overrideFilter = selected.getFilterMap().get(4 + "|" + filterID + "|" + dashboardStack.getUrlKey());
                    if (overrideFilter != null) {
                        recurseFilters.add(overrideFilter);
                    } else {
                        recurseFilters.add(filter);
                    }
                    //dashboardStack.setFilters(replaceFilters);
                }
                //recurseFilters.addAll(dashboardStack.getFilters());
            }
            if (index != null) {
                return findElementToRender(dashboardStack.getGridItems().get(index).getDashboardElement(), selected, recurseFilters);
            } else {
                return findElementToRender(dashboardStack.getGridItems().get(0).getDashboardElement(), selected, recurseFilters);
            }
        } else if (root instanceof DashboardGrid) {
            DashboardGrid dashboardGrid = (DashboardGrid) root;
            if (dashboardGrid.getColumns() == 1 && dashboardGrid.getRows() == 1) {
                return findElementToRender(dashboardGrid.getGridItems().get(0).getDashboardElement(), selected, recurseFilters);
            }
        }
        return root;
    }

    private void populateReportData(DashboardElement element, EIConnection conn, DashboardStackPositions dashboardStackPositions, Map<String, WSAnalysisDefinition> reportMap,
                                    List<FilterDefinition> filters) throws SQLException {
        if (element instanceof DashboardStack) {
            DashboardStack dashboardStack = (DashboardStack) element;
            if (dashboardStack.getGridItems().size() > 1) {
                throw new RuntimeException();
            }
            List<FilterDefinition> recurse = new ArrayList<>(filters);
            recurse.addAll(dashboardStack.getFilters());
            populateReportData(dashboardStack.getGridItems().get(0).getDashboardElement(), conn, dashboardStackPositions, reportMap, recurse);
        } else if (element instanceof DashboardGrid) {
            DashboardGrid dashboardGrid = (DashboardGrid) element;
            for (int i = 0; i < dashboardGrid.getColumns(); i++) {
                for (int j = 0; j < dashboardGrid.getRows(); j++) {
                    DashboardElement child = dashboardGrid.findItem(j, i).getDashboardElement();
                    populateReportData(child, conn, dashboardStackPositions, reportMap, filters);
                }
            }
        } else if (element instanceof DashboardReport) {
            DashboardReport dashboardReport = (DashboardReport) element;
            if (dashboardReport.getDashboardFilterOverrides() != null && dashboardReport.getDashboardFilterOverrides().size()> 0) {

                for (DashboardFilterOverride dashboardFilterOverride : dashboardReport.getDashboardFilterOverrides()) {
                    System.out.println("override applies to " + dashboardFilterOverride.getFilterID() + " - " + dashboardFilterOverride.isHideFilter());
                }

                Iterator<FilterDefinition> iter = filters.iterator();
                while (iter.hasNext()) {
                    FilterDefinition filter = iter.next();
                    for (DashboardFilterOverride dashboardFilterOverride : dashboardReport.getDashboardFilterOverrides()) {
                        if (dashboardFilterOverride.isHideFilter() && filter.getFilterID() == dashboardFilterOverride.getFilterID()) {
                            System.out.println("removing overriden filter " + filter.getFilterID());
                            iter.remove();
                        }

                    }
                    /*String sid = String.valueOf(filter.getFilterID());
                    if (k.contains(sid)) {
                        System.out.println("suppressing filter " + sid);
                        filter.override(dashboardReport.getOverridenFilters().get(sid));
                        filter.setEnabled(dashboardReport.getOverridenFilters().get(sid).isEnabled());
                        //iter.remove();
                    }*/
                }
            }
            WSAnalysisDefinition report = AnalysisService.openAnalysisDefinitionWithConn(dashboardReport.getReport().getId(), conn);
            List<FilterDefinition> replaceFilters = new ArrayList<>(filters);
            if (report.getFilterDefinitions() != null) {
                for (FilterDefinition filter : report.getFilterDefinitions()) {
                    long filterID = filter.getFilterID();
                    FilterDefinition overrideFilter = dashboardStackPositions.getFilterMap().get(3 + "|" + filterID + "|" + dashboardReport.getUrlKey());
                    if (overrideFilter != null) {
                        replaceFilters.add(overrideFilter);
                    } else {
                        replaceFilters.add(filter);
                    }
                }
            }
            report.setFilterDefinitions(replaceFilters);
            reportMap.put(dashboardReport.getUrlKey(), report);
        } else {
            throw new RuntimeException();
        }
    }
}
