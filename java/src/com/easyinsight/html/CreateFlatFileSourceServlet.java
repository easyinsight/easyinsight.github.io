package com.easyinsight.html;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.api.v3.JSONServlet;
import com.easyinsight.api.v3.ResponseInfo;
import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.userupload.FieldUploadInfo;
import com.easyinsight.userupload.FlatFileUploadContext;
import com.easyinsight.userupload.UploadResponse;
import com.easyinsight.userupload.UserUploadService;
import net.minidev.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 4/21/14
 * Time: 1:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateFlatFileSourceServlet extends JSONServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        setBasicAuth(false);
    }

    @Override
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
        /*if(userCreationResponse.isSuccessful()) {
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
        }*/

        return ri;
    }
}
