package com.easyinsight.api.v3;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.UserThreadMutex;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import nu.xom.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * User: jamesboe
 * Date: 7/27/13
 * Time: 5:50 PM
 */
public class SemaphoreServlet extends JSONServlet {

    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn, HttpServletRequest request) throws Exception {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Map<Long, Semaphore> map = UserThreadMutex.summarize();
        JSONArray arr = new JSONArray();
        for (Map.Entry<Long, Semaphore> entry : map.entrySet()) {
            JSONObject object = new JSONObject();
            object.put("userID", entry.getKey());
            object.put("queueLength", entry.getValue().getQueueLength());
            object.put("availablePermits", entry.getValue().availablePermits());
            arr.put(object);
        }
        return new ResponseInfo(200, arr.toString());
    }
}
