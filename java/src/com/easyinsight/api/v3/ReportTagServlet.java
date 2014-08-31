package com.easyinsight.api.v3;

import com.easyinsight.analysis.AnalysisService;
import com.easyinsight.analysis.InsightResponse;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
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
public class ReportTagServlet extends JSONServlet {


    @Override
    protected ResponseInfo processGet(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        JSONObject responseObject = new JSONObject();
        List<Tag> tags = new UserUploadService().getReportTags();
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
        Collection<Tag> tags = uus.getAllTags(conn).values();
        ExportMetadata md = ExportService.createExportMetadata(conn);
        for (Tag t : tags) {
            if (t.getName().equalsIgnoreCase(tagName)) {
                chosenTag = t;
                break;
            }
        }
        if (chosenTag == null) {
            chosenTag = new Tag();
            chosenTag.setName(tagName);
            chosenTag.setReport(true);
            chosenTag = uus.createTag(conn, tags.size(), chosenTag);
        } else {
            chosenTag.setReport(true);
            uus.saveTag(conn, -1, chosenTag);
        }

        InsightResponse response = new AnalysisService().openAnalysisIfPossible(String.valueOf(request.getParameter("reportID")));
        EIDescriptor descriptor = response.getInsightDescriptor();
        if (response.getInsightDescriptor() != null) {
            if (chosenTag != null) {
                uus.tagReportsAndDashboards(Arrays.asList(descriptor), chosenTag, conn);
            }
            return new ResponseInfo(ResponseInfo.ALL_GOOD, chosenTag.toJSON(md).toString());

        } else
            return new ResponseInfo(ResponseInfo.UNAUTHORIZED, "");
    }

    @Override
    protected ResponseInfo processDelete(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        final FeedStorage fs = new FeedStorage();
        Tag t = new Tag(Long.parseLong(String.valueOf(request.getParameter("tagID"))), null, false, false, false);
        UserUploadService uus = new UserUploadService();
        InsightResponse response = new AnalysisService().openAnalysisIfPossible(String.valueOf(request.getParameter("reportID")));
        EIDescriptor descriptor = response.getInsightDescriptor();
        if (response.getInsightDescriptor() != null) {
            uus.untagReportsAndDashboards(Arrays.asList(descriptor), t, conn);
            uus.checkReportsTag(t, conn);
            return new ResponseInfo(ResponseInfo.ALL_GOOD, "");
        } else
            return new ResponseInfo(ResponseInfo.UNAUTHORIZED, "");
    }

    @Override
    protected ResponseInfo processJSON(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        throw new UnsupportedOperationException();
    }


}
