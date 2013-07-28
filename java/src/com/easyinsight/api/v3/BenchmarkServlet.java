package com.easyinsight.api.v3;

import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import nu.xom.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * User: jamesboe
 * Date: 7/27/13
 * Time: 11:33 PM
 */
public class BenchmarkServlet extends JSONServlet {
    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn, HttpServletRequest request) throws Exception {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT COUNT(*), SUM(ELAPSED_TIME), CATEGORY FROM BENCHMARK WHERE BENCHMARK_DATE >= ? GROUP BY CATEGORY");
        int minutes = Integer.parseInt(request.getParameter("minutes"));
        long time = System.currentTimeMillis() - (minutes * 1000 * 60);
        preparedStatement.setTimestamp(1, new Timestamp(time));
        ResultSet rs = preparedStatement.executeQuery();
        JSONArray jsonArray = new JSONArray();
        while (rs.next()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Count", rs.getInt(1));
            jsonObject.put("ElapsedTime", rs.getInt(2));
            jsonObject.put("Category", rs.getString(3));
            jsonArray.put(jsonObject);
        }
        return new ResponseInfo(200, jsonArray.toString());
    }
}
