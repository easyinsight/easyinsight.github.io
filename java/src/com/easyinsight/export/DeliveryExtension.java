package com.easyinsight.export;

import com.easyinsight.database.EIConnection;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: 2/26/13
 * Time: 5:01 PM
 */
public class DeliveryExtension {
    public void save(EIConnection conn, long deliveryID, long deliveryToReportID, long dashboardID) throws SQLException {

    }

    public static DeliveryExtension load(EIConnection conn, long reportDeliveryID, long deliveryToReportID, int format, long dashboardDeliveryID) throws SQLException {
        if (format == ReportDelivery.PDF) {
            PreparedStatement stmt;
            if (reportDeliveryID > 0) {
                stmt = conn.prepareStatement("SELECT show_header, export_mode, width, height, render_mode FROM pdf_delivery_extension WHERE report_delivery_id = ?");
                stmt.setLong(1, reportDeliveryID);
            } else if (dashboardDeliveryID > 0) {
                stmt = conn.prepareStatement("SELECT show_header, export_mode, width, height, render_mode FROM pdf_delivery_extension WHERE delivery_to_dashboard_id = ?");
                stmt.setLong(1, dashboardDeliveryID);
            } else {
                stmt = conn.prepareStatement("SELECT show_header, export_mode, width, height, render_mode FROM pdf_delivery_extension WHERE delivery_to_report_id = ?");
                stmt.setLong(1, deliveryToReportID);
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                PDFDeliveryExtension pdfDeliveryExtension = new PDFDeliveryExtension();
                pdfDeliveryExtension.setShowHeader(rs.getBoolean(1));
                pdfDeliveryExtension.setDisplayType(rs.getBoolean(2));
                pdfDeliveryExtension.setWidth(rs.getInt(3));
                pdfDeliveryExtension.setHeight(rs.getInt(4));
                pdfDeliveryExtension.setGenerateByHTML(rs.getInt(5));
                return pdfDeliveryExtension;
            }
        }
        return null;
    }

    public String toURL() {
        return "";
    }

    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        return new JSONObject();
    }

    public static DeliveryExtension fromJSON(net.minidev.json.JSONObject jsonObject) {
        return new PDFDeliveryExtension(jsonObject);
    }
}
