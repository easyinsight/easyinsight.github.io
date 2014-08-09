package com.easyinsight.scheduler;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.easyinsight.analysis.AnalysisService;
import com.easyinsight.analysis.InsightResponse;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.DashboardPDF;
import com.easyinsight.export.ExportService;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.util.RandomTextGenerator;
import flex.messaging.FlexContext;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.util.IOUtils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * User: jamesboe
 * Date: 7/8/13
 * Time: 1:26 PM
 */
public class UploadExportImageServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(req);
        String imageString = wrappedRequest.getParameter("imgBase64");
        imageString = imageString.substring("data:image/png;base64,".length());
        byte[] contentData = imageString.getBytes();
        byte[] decodedData = Base64.decodeBase64(contentData);
        Long id = Long.parseLong(wrappedRequest.getParameter("id"));
        int height = (int) Double.parseDouble(wrappedRequest.getParameter("height"));
        int width = (int) Double.parseDouble(wrappedRequest.getParameter("width"));

        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE image_selenium_trigger SET image_state = ?, image_preferred_width = ?," +
                    "image_preferred_height = ?, image_response = ? WHERE image_selenium_trigger_id = ?");
            updateStmt.setInt(1, DashboardPDF.SUCCESS);
            updateStmt.setInt(2, width);
            updateStmt.setInt(3, height);
            updateStmt.setBytes(4, decodedData);
            updateStmt.setLong(5, id);
            updateStmt.executeUpdate();
            updateStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
