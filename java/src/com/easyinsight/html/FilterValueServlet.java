package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
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

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 10/25/13
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class FilterValueServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String filterID = req.getParameter("filterID");
        String query = req.getParameter("q");
        if (filterID != null) {
            if (req.getSession().getAttribute("userID") != null) {
                SecurityUtil.populateThreadLocalFromSession(req);
            }
            int offset = -600;
            try {
                long id = Long.parseLong(filterID);
                EIConnection conn = Database.instance().getConnection();
                Session s = Database.instance().createSession(conn);
                FilterDefinition f = (FilterDefinition) s.createQuery("from FilterDefinition where filterID = ?").setLong(0, id).list().get(0);
                if(f instanceof FilterValueDefinition) {
                    AnalysisItemResultMetadata result = new DataService().getAnalysisItemMetadataForFilter(id, offset);
                    AnalysisDimensionResultMetadata dimensionMetadata = (AnalysisDimensionResultMetadata) result;
                    JSONArray ja = new JSONArray();
                    for(Value v : dimensionMetadata.getValues()) {
                        if(v != null && v.toString().contains(query))
                            ja.add(v.toString());
                    }
                            JSONObject jo = new JSONObject();
//                    if(ja.size() > 100) {
//                        ja = new JSONArray();
//                        jo.put("error", "Too many values, please refine your search.");
//                    }
                    jo.put("values", ja);
                    resp.setContentType("application/json");
                    resp.getOutputStream().write(jo.toString().getBytes());
                    resp.getOutputStream().flush();
                }
            } catch (Exception e) {

            }
        }
    }
}
