package com.easyinsight.html;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.api.v3.ResponseInfo;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.userupload.FieldUploadInfo;
import com.easyinsight.userupload.FlatFileUploadContext;
import com.easyinsight.userupload.UploadResponse;
import com.easyinsight.userupload.UserUploadService;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 4/21/14
 * Time: 1:58 PM
 * To change this template use File | Settings | File Templates.
 */
@WebServlet(value = "/html/connection/createFlatFileSource", asyncSupported = true)
public class CreateFlatFileSourceServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SecurityUtil.populateThreadLocalFromSession(req);
        try {
            InputStream is = req.getInputStream();
            JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, "UTF-8");
            String jsonString = writer.toString();
            Object o = parser.parse(jsonString);
            JSONObject jsonObject = (net.minidev.json.JSONObject) o;
            String fileName;
            String uploadKey = jsonObject.get("upload_key").toString();
            fileName = getFileName(uploadKey);
            String dataSourceName = jsonObject.get("data_source_name").toString();
            FlatFileUploadContext context = new FlatFileUploadContext();
            context.setUploadKey(uploadKey);
            context.setFileName(fileName);
            UploadResponse response = new UserUploadService().analyzeUpload(context);
            List<FieldUploadInfo> infos = response.getInfos();
            List<AnalysisItem> items = infos.stream().map(FieldUploadInfo::getGuessedItem).collect(Collectors.toList());
            UploadResponse creationResponse = new UserUploadService().createDataSource(dataSourceName, context, items, true);
            long dataSourceID = creationResponse.getFeedID();
            EIConnection conn = Database.instance().getConnection();
            try {
                PreparedStatement stmt = conn.prepareStatement("SELECT api_key FROM data_feed WHERE data_feed_id = ?");
                stmt.setLong(1, dataSourceID);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                String urlKey = rs.getString(1);
                ResponseInfo ri;
                org.json.JSONObject jo = new org.json.JSONObject();
                jo.put("url_key", urlKey);
                jo.put("success", true);
                ri = new ResponseInfo(200, jo.toString());
                resp.setContentType("application/json");
                resp.setStatus(ri.getCode());
                resp.getOutputStream().write(ri.getResponseBody().getBytes());
                resp.getOutputStream().flush();
            } finally {
                Database.closeConnection(conn);
            }
        } catch (Exception e) {
            LogClass.error(e);
            sendError(400, "Your request was malformed", resp);
        } finally {
            SecurityUtil.clearThreadLocal();
        }
    }

    protected void sendError(int status, String error, HttpServletResponse resp) throws IOException {
        try {
            resp.setContentType("application/json");
            resp.setStatus(status);
            org.json.JSONObject jo = new org.json.JSONObject();
            jo.put("message", error);
            resp.getOutputStream().write(jo.toString().getBytes());
            resp.getOutputStream().flush();
        } catch (JSONException e) {
            LogClass.error(e);
        }
    }

    private String getFileName(String uploadKey) throws SQLException {
        String fileName;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement uploadStmt = conn.prepareStatement("SELECT UPLOAD_FILE_NAME FROM UPLOAD_BYTES WHERE UPLOAD_KEY = ? AND USER_ID = ?");
            uploadStmt.setString(1, uploadKey);
            uploadStmt.setLong(2, SecurityUtil.getUserID());
            ResultSet uploadRS = uploadStmt.executeQuery();
            uploadRS.next();
            fileName = uploadRS.getString(1);
            uploadStmt.close();
        } finally {
            Database.closeConnection(conn);
        }
        return fileName;
    }

    /*@Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        String uploadKey = jsonObject.get("upload_key").toString();
        PreparedStatement uploadStmt = conn.prepareStatement("SELECT UPLOAD_FILE_NAME FROM UPLOAD_BYTES WHERE UPLOAD_KEY = ? AND USER_ID = ?");
        uploadStmt.setString(1, uploadKey);
        uploadStmt.setLong(2, SecurityUtil.getUserID());
        ResultSet uploadRS = uploadStmt.executeQuery();
        uploadRS.next();
        String fileName = uploadRS.getString(1);
        uploadStmt.close();
        //String fileName = jsonObject.get("file_name").toString();
        String dataSourceName = jsonObject.get("data_source_name").toString();
        UserUploadService service = new UserUploadService();
        FlatFileUploadContext context = new FlatFileUploadContext();
        context.setUploadKey(uploadKey);
        context.setFileName(fileName);
        UploadResponse response = service.analyzeUploadWithConn(context, conn);
        List<FieldUploadInfo> infos = response.getInfos();
        List<AnalysisItem> items = infos.stream().map(FieldUploadInfo::getGuessedItem).collect(Collectors.toList());
        UploadResponse creationResponse = new UserUploadService().createDataSourceWithConn(dataSourceName, context, items, true, conn);
        long dataSourceID = creationResponse.getFeedID();
        PreparedStatement stmt = conn.prepareStatement("SELECT api_key FROM data_feed where data_feed_id = ?");
        stmt.setLong(1, dataSourceID);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        String urlKey = rs.getString(1);

        ResponseInfo ri;
        org.json.JSONObject jo = new org.json.JSONObject();
        jo.put("url_key", urlKey);
        jo.put("success", true);
        ri = new ResponseInfo(200, jo.toString());
        *//*if(userCreationResponse.isSuccessful()) {
            if(!jo.has("success"))
                jo.put("success", true);
            jo.put("user", uto.toJSON(md));
            ri = new ResponseInfo(200, jo.toString());
        } else {
            jo.put("success", false);
            if(!jo.has("error")) {
                jo.put("error", userCreationResponse.getFailureMessage());
            }
            ri = new ResponseInfo(500, jo.toString());
        }*//*

        return ri;
    }*/
}
