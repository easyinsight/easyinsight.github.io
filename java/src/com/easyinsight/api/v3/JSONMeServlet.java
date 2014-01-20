package com.easyinsight.api.v3;

import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;
import net.minidev.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 1/20/14
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class JSONMeServlet extends JSONServlet {
    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("username", SecurityUtil.getUserName());
        ResponseInfo r = new ResponseInfo(ResponseInfo.ALL_GOOD, responseJSON.toString());
        return r;
    }
}
