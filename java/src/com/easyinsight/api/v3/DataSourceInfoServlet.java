package com.easyinsight.api.v3;

import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.userupload.CustomFolder;
import com.easyinsight.userupload.UserUploadService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 1/20/14
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataSourceInfoServlet extends JSONServlet {

    @Override
    protected ResponseInfo processJSON(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        JSONObject responseObject = new JSONObject();

        String dataSourceKey = request.getParameter("dataSourceID");
        long dataSourceID = new FeedStorage().dataSourceIDForDataSource(dataSourceKey);
        DataSourceDescriptor dataSourceDescriptor = new FeedStorage().dataSourceURLKeyForDataSource(dataSourceID);
        ExportMetadata md = ExportService.createExportMetadata(SecurityUtil.getAccountID(), conn, new InsightRequestMetadata());
        JSONArray array;
        List<EIDescriptor> descriptors = new UserUploadService().getFeedAnalysisTreeForDataSource(new DataSourceDescriptor(null, dataSourceID, 0, false, 0));
        array = new JSONArray(descriptors.stream().map(d -> {
            try {
                return d.toJSON(md);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));

        PreparedStatement getFoldersStmt = conn.prepareStatement("SELECT REPORT_FOLDER_ID, FOLDER_NAME, DATA_SOURCE_ID FROM REPORT_FOLDER WHERE DATA_SOURCE_ID = ?");
        getFoldersStmt.setLong(1, dataSourceID);

        ResultSet folderRS = getFoldersStmt.executeQuery();
        JSONObject folders = new JSONObject();
        while (folderRS.next()) {
            long id = folderRS.getLong(1);
            String name = folderRS.getString(2);
            CustomFolder customFolder = new CustomFolder();
            customFolder.setName(name);
            customFolder.setId(id);
            folders.put(String.valueOf(id), customFolder.toJSON(md));
        }

        CustomFolder mainViewFolder = new CustomFolder();
        mainViewFolder.setId(1);
        mainViewFolder.setName("Main Views");
        folders.put("1", mainViewFolder.toJSON(md));

        CustomFolder additionalViewsFolder = new CustomFolder();
        additionalViewsFolder.setId(2);
        additionalViewsFolder.setName("Additional Views");
        folders.put("2", additionalViewsFolder.toJSON(md));

        responseObject.put("folders", folders);



        responseObject.put("data_source", dataSourceDescriptor.toJSON(md));
        responseObject.put("reports", array);
        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());
    }

}
