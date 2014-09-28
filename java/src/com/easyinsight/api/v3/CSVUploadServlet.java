package com.easyinsight.api.v3;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.util.RandomTextGenerator;
import net.minidev.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * User: jamesboe
 * Date: 7/8/13
 * Time: 1:26 PM
 */
public class CSVUploadServlet extends JSONServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        authProcessor(req, resp, () ->
        Database.useConnection(true, (conn) -> {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            String contentType = null;
            List items = upload.parseRequest(req);
            byte[] bytes = null;
            String fileName = null;
            for (Object obj : items) {
                FileItem fileItem = (FileItem) obj;
                System.out.println("File item " + fileItem.getFieldName() + " - " + fileItem.getName() + " - " + fileItem.getContentType() + " - " + fileItem.getSize());
                fileName = fileItem.getName();
                contentType = fileItem.getContentType();
                if (fileItem.isFormField()) {
                } else if (fileItem.getSize() > 0) {
                    bytes = fileItem.get();
                    System.out.println("got " + bytes.length + " bytes");
                }
            }

            org.json.JSONObject jo = new org.json.JSONObject();
            if(bytes != null && bytes.length > 0 && bytes.length < 1024 * 1024 * 10 && fileName != null && fileName.length() > 0 && isAcceptableUpload(contentType)) {

                ByteArrayOutputStream dest = new ByteArrayOutputStream();

                ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(dest));
                zos.putNextEntry(new ZipEntry("data.csv"));
                zos.write(bytes);
                zos.closeEntry();
                zos.close();
                int start = bytes.length;
                bytes = dest.toByteArray();
                System.out.println("compressed from " + start + " to " + bytes.length);

                String uploadKey = RandomTextGenerator.generateText(25);

                ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
                AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI"));
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(bytes.length);
                s3.putObject(new PutObjectRequest("archival1", uploadKey + ".zip", stream, objectMetadata));

                PreparedStatement updateStmt = conn.prepareStatement("INSERT INTO UPLOAD_BYTES (UPLOAD_SUCCESSFUL, UPLOAD_KEY, UPLOAD_TIME, USER_ID, UPLOAD_FILE_NAME) VALUES (?, ?, ?, ?, ?)");
                updateStmt.setBoolean(1, true);
                updateStmt.setString(2, uploadKey);
                updateStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                updateStmt.setLong(4, SecurityUtil.getUserID());
                updateStmt.setString(5, fileName);
                updateStmt.executeUpdate();
                updateStmt.close();


                jo.put("upload_key", uploadKey);
            }
            ResponseInfo ri = new ResponseInfo(ResponseInfo.ALL_GOOD, jo.toString());
            resp.setContentType("application/json");
            resp.setStatus(ri.getCode());
            resp.getOutputStream().write(ri.getResponseBody().getBytes());
            resp.getOutputStream().flush();
        }));
    }

    private boolean isAcceptableUpload(String contentType) {
        boolean valid = false;
        if ("application/vnd.ms-excel".equals(contentType)) {
            valid = true;
        } else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
            valid = true;
        } else if ("text/plain".equals(contentType)) {
            valid = true;
        }
        return valid;
    }

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        throw new UnsupportedOperationException();
    }
}
