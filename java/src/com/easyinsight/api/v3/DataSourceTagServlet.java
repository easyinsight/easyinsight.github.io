package com.easyinsight.api.v3;

import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.tag.Tag;
import com.easyinsight.userupload.UserUploadService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 1/20/14
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataSourceTagServlet extends JSONServlet {


    @Override
    protected ResponseInfo processGet(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        JSONObject responseObject = new JSONObject();
        List<Tag> tags = new UserUploadService().getDataSourceTags(conn);
        ExportMetadata md = ExportService.createExportMetadata(conn);
        JSONArray array;
        array = new JSONArray(tags.stream().map(d -> {
            try {
                return d.toJSON(md);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));


        responseObject.put("tags", array);
        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());
    }

    @Override
    protected ResponseInfo processPost(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        String tagName = String.valueOf(jsonObject.get("name"));
        Tag chosenTag = null;
        UserUploadService uus = new UserUploadService();
        FeedStorage fs = new FeedStorage();
        Collection<Tag> tags = uus.getAllTags(conn).values();
        ExportMetadata md = ExportService.createExportMetadata(conn);
        for(Tag t : tags) {
            if(t.getName().equalsIgnoreCase(tagName)) {
                chosenTag = t;
                break;
            }
        }
        if(chosenTag == null) {
            chosenTag = new Tag();
            chosenTag.setName(tagName);
            chosenTag.setDataSource(true);
            chosenTag = uus.createTag(conn, tags.size(), chosenTag);
        } else {
            chosenTag.setDataSource(true);
            uus.saveTag(conn, -1, chosenTag);
        }


        if(chosenTag != null) {
            uus.tagDataSources(Arrays.asList(fs.dataSourceURLKeyForDataSource(fs.dataSourceIDForDataSource(String.valueOf(request.getParameter("dataSourceID"))), conn)), chosenTag, conn);
        }
        return new ResponseInfo(ResponseInfo.ALL_GOOD, chosenTag.toJSON(md).toString());
    }

    @Override
    protected ResponseInfo processDelete(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        final FeedStorage fs = new FeedStorage();
        Tag t = new Tag(Long.parseLong(String.valueOf(request.getParameter("tagID"))), null, false, false, false);
        UserUploadService uus = new UserUploadService();
        uus.untagDataSource(Arrays.asList(fs.dataSourceURLKeyForDataSource(fs.dataSourceIDForDataSource(String.valueOf(request.getParameter("dataSourceID"))), conn)), t, conn);
        uus.checkTag(t, conn);
        return new ResponseInfo(ResponseInfo.ALL_GOOD, "");
    }

    @Override
    protected ResponseInfo processJSON(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        throw new UnsupportedOperationException();
    }

}
