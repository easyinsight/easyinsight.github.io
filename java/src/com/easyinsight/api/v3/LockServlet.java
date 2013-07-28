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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 7/27/13
 * Time: 5:50 PM
 */
public class LockServlet extends JSONServlet {
    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn, HttpServletRequest request) throws Exception {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        PreparedStatement lockStmt =  conn.prepareStatement("SELECT LOCK_NAME, LOCK_TIME FROM DISTRIBUTED_LOCK");
        JSONArray arr = new JSONArray();
        ResultSet rs = lockStmt.executeQuery();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        while (rs.next()) {
            JSONObject object = new JSONObject();
            arr.put(object);
            object.put("lockName", rs.getString(1));
            Timestamp timestamp = rs.getTimestamp(2);
            if (!rs.wasNull()) {
                object.put("lockTime", sdf.format(new Date(timestamp.getTime())));
            }
        }
        return new ResponseInfo(200, arr.toString());
    }
}
