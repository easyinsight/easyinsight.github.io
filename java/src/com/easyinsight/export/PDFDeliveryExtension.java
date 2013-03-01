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
    private boolean showHeader;
    private boolean displayType;

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

    @Override
    public String toURL() {
        return "&showHeader=" + showHeader;
    }

    @Override
    public void save(EIConnection conn, long deliveryID, long deliveryToReportID) throws SQLException {
        if (deliveryID > 0) {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM pdf_delivery_extension WHERE report_delivery_id = ?");
            deleteStmt.setLong(1, deliveryID);
            deleteStmt.executeUpdate();
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO pdf_delivery_extension (report_delivery_id, show_header, export_mode, width, height) values (?, ?, ?, ?, ?)");
            insertStmt.setLong(1, deliveryID);
            insertStmt.setBoolean(2, showHeader);
            insertStmt.setBoolean(3, displayType);
            insertStmt.setInt(4, 0);
            insertStmt.setInt(5, 0);
            insertStmt.execute();
        } else {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM pdf_delivery_extension WHERE delivery_to_report_id = ?");
            deleteStmt.setLong(1, deliveryToReportID);
            deleteStmt.executeUpdate();
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO pdf_delivery_extension (delivery_to_report_id, show_header, export_mode, width, height) values (?, ?, ?, ?, ?)");
            insertStmt.setLong(1, deliveryToReportID);
            insertStmt.setBoolean(2, showHeader);
            insertStmt.setBoolean(3, displayType);
            insertStmt.setInt(4, 0);
            insertStmt.setInt(5, 0);
            insertStmt.execute();
        }
    }
}
