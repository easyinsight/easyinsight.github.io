package com.easyinsight.export;

import com.easyinsight.database.EIConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: 2/26/13
 * Time: 8:48 PM
 */
public class PDFDeliveryExtension extends DeliveryExtension {

    private int generateByHTML;

    private boolean showHeader;
    private boolean displayType;
    private int width;
    private int height;
    private String orientation = "Landscape";

    public int getGenerateByHTML() {
        return generateByHTML;
    }

    public void setGenerateByHTML(int generateByHTML) {
        this.generateByHTML = generateByHTML;
    }

    public boolean isDisplayType() {
        return displayType;
    }

    public void setDisplayType(boolean displayType) {
        this.displayType = displayType;
    }

    public boolean isShowHeader() {
        return showHeader;
    }

    public void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toURL() {
        return "&showHeader=" + showHeader + "&orientation=" + orientation+"&pdfWidth="+width+"&pdfHeight="+height;
    }

    @Override
    public void save(EIConnection conn, long deliveryID, long deliveryToReportID, long dashboardID) throws SQLException {
        if (deliveryID > 0) {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM pdf_delivery_extension WHERE report_delivery_id = ?");
            deleteStmt.setLong(1, deliveryID);
            deleteStmt.executeUpdate();
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO pdf_delivery_extension (report_delivery_id, show_header, export_mode, width, height, render_mode) values (?, ?, ?, ?, ?, ?)");
            insertStmt.setLong(1, deliveryID);
            insertStmt.setBoolean(2, showHeader);
            insertStmt.setBoolean(3, displayType);
            insertStmt.setInt(4, width);
            insertStmt.setInt(5, height);
            insertStmt.setInt(6, generateByHTML);
            insertStmt.execute();
            insertStmt.close();
        } else if (deliveryToReportID > 0) {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM pdf_delivery_extension WHERE delivery_to_report_id = ?");
            deleteStmt.setLong(1, deliveryToReportID);
            deleteStmt.executeUpdate();
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO pdf_delivery_extension (delivery_to_report_id, show_header, export_mode, width, height, render_mode) values (?, ?, ?, ?, ?, ?)");
            insertStmt.setLong(1, deliveryToReportID);
            insertStmt.setBoolean(2, showHeader);
            insertStmt.setBoolean(3, displayType);
            insertStmt.setInt(4, width);
            insertStmt.setInt(5, height);
            insertStmt.setInt(6, generateByHTML);
            insertStmt.execute();
            insertStmt.close();
        } else {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM pdf_delivery_extension WHERE delivery_to_dashboard_id = ?");
            deleteStmt.setLong(1, dashboardID);
            deleteStmt.executeUpdate();
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO pdf_delivery_extension (delivery_to_dashboard_id, show_header, export_mode, width, height, render_mode) values (?, ?, ?, ?, ?, ?)");
            insertStmt.setLong(1, dashboardID);
            insertStmt.setBoolean(2, showHeader);
            insertStmt.setBoolean(3, displayType);
            insertStmt.setInt(4, width);
            insertStmt.setInt(5, height);
            insertStmt.setInt(6, generateByHTML);
            insertStmt.execute();
            insertStmt.close();
        }
    }
}
