package com.easyinsight.scheduler;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * User: jamesboe
 * Date: 7/8/13
 * Time: 1:26 PM
 */
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List items = upload.parseRequest(req);
            String uploadKey = req.getParameter("uploadKey");
            byte[] bytes = null;
            for (Object obj : items) {
                FileItem fileItem = (FileItem) obj;

                if (fileItem.isFormField()) {
                } else {
                    bytes = fileItem.get();
                    System.out.println("got " + bytes.length + " bytes");
                }
            }
            ByteArrayOutputStream dest = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(dest));
            zos.putNextEntry(new ZipEntry("data.csv"));
            zos.write(bytes);
            zos.closeEntry();
            zos.close();
            int start = bytes.length;
            bytes = dest.toByteArray();
            System.out.println("compressed from " + start + " to " + bytes.length);
            EIConnection conn = Database.instance().getConnection();
            try {
                PreparedStatement ps = conn.prepareStatement("SELECT UPLOAD_BYTES_ID, USER_ID FROM UPLOAD_BYTES WHERE UPLOAD_KEY = ?");
                ps.setString(1, uploadKey);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    long id = rs.getLong(1);
                    long userID = rs.getLong(2);
                    ps.close();
                    PreparedStatement updateStmt = conn.prepareStatement("UPDATE UPLOAD_BYTES SET BYTES = ? WHERE UPLOAD_BYTES_ID = ?");
                    ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
                    updateStmt.setBinaryStream(1, stream, bytes.length);
                    updateStmt.setLong(2, id);
                    updateStmt.execute();
                    updateStmt.close();
                } else {
                    throw new RuntimeException("No upload key found matching the specified parameter.");
                }
            } finally {
                Database.closeConnection(conn);
            }
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    private void processFormField(FileItem item) {
        String name = item.getFieldName();
        String value = item.getString();
        System.out.println("Item name: Ó + name + " + " value: " + value);
    }
}
