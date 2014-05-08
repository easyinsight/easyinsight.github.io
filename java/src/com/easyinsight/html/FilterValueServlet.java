package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 10/25/13
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class FilterValueServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            JSONObject filterObject = (JSONObject) req.getAttribute("filterObject");
            Long filterID = Long.valueOf((Integer) filterObject.get("id"));
            if (filterID != null) {
                if (req.getSession().getAttribute("userID") != null) {
                    SecurityUtil.populateThreadLocalFromSession(req);
                }
                int offset = -600;
                try {
                    long id = filterID;
                    EIConnection conn = Database.instance().getConnection();
                    try {
                        Session s = Database.instance().createSession(conn);
                        try {
                            JSONArray filters = (JSONArray) filterObject.get("parents");
                            List<FilterDefinition> adjustedFilters = new ArrayList<FilterDefinition>();
                            for (Object o : filters) {
                                JSONObject filterObj = (JSONObject) o;
                                long curID = Long.valueOf((Integer) filterObj.get("id"));
                                FilterDefinition f = (FilterDefinition) s.createQuery("from FilterDefinition where filterID = ?").setLong(0, curID).list().get(0);
                                FilterUtils.adjustFilter("Filter", false, f, filterObj);
                                adjustedFilters.add(f);
                            }

                            FilterDefinition f = (FilterDefinition) s.createQuery("from FilterDefinition where filterID = ?").setLong(0, id).list().get(0);
                            if (f instanceof FilterValueDefinition) {
                                AnalysisItemResultMetadata result = new DataService().getAnalysisItemMetadataForFilter(id, adjustedFilters, offset);
                                AnalysisDimensionResultMetadata dimensionMetadata = (AnalysisDimensionResultMetadata) result;
                                JSONArray ja = new JSONArray();
                                for (String v : dimensionMetadata.getStrings()) {
                                    if (v != null)
                                        ja.add(v);
                                }
                                JSONObject jo = new JSONObject();
                                jo.put("values", ja);
                                resp.setContentType("application/json");
                                resp.getOutputStream().write(jo.toString().getBytes());
                                resp.getOutputStream().flush();
                            }
                        } finally {
                            s.close();
                        }
                    } finally {
                        Database.closeConnection(conn);
                    }
                } catch (Exception e) {
                    LogClass.error(e);
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
        }

    }
}
