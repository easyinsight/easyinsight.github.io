package com.easyinsight.html;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.api.v3.JSONServlet;
import com.easyinsight.api.v3.ResponseInfo;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedService;
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
public class RefreshFlatFileSourceServlet extends JSONServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        setBasicAuth(false);
    }

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        String uploadKey = jsonObject.get("upload_key").toString();
        String dataSourceURLKey = jsonObject.get("data_source_id").toString();
        int operation = (Integer) jsonObject.get("operation");
        PreparedStatement ps = conn.prepareStatement("SELECT data_feed_id FROM data_feed where API_KEY = ?");
        ps.setString(1, dataSourceURLKey);
        ResultSet rs = ps.executeQuery();
        rs.next();
        long dataSourceID = rs.getLong(1);

        new UserUploadService().updateData(dataSourceID, uploadKey, operation == 2);

        ResponseInfo ri;
        org.json.JSONObject jo = new org.json.JSONObject();
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
