package com.easyinsight.api.v3;

import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.UserAccountAdminService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 1/20/14
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
@WebServlet(value = "/json/availableTimeZones", asyncSupported = true)
public class AvailableTimeZoneServlet extends JSONServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        setBasicAuth(false);
    }

    @Override
    protected ResponseInfo processGet(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        SecurityUtil.authorizeAccountAdmin();
        JSONObject responseObject = new JSONObject();
        List<String> timezones = new UserAccountAdminService().getTimezones();
        JSONArray jsonArray = new JSONArray();
        JSONObject autoDetect = new JSONObject();
        autoDetect.put("zone_id", "[ Auto Detect ]");
        jsonArray.put(autoDetect);
        for (String timezone : timezones) {
            JSONObject zone = new JSONObject();
            zone.put("zone_id", timezone);
            jsonArray.put(zone);
        }

        responseObject.put("timezones", jsonArray);

        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());
    }

    @Override
    protected ResponseInfo processJSON(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
