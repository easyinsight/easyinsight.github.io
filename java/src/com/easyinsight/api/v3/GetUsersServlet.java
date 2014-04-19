package com.easyinsight.api.v3;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.FilterValueDefinition;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.preferences.*;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.UserAccountAdminService;
import com.easyinsight.users.UserCreationResponse;
import com.easyinsight.users.UserTransferObject;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 2/19/14
 * Time: 12:58 PM
 */
public class GetUsersServlet extends JSONServlet {

    @Override
    protected ResponseInfo processPost(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        List<Persona> personas = new PreferencesService().getPersonas();
        List<UserTransferObject> users = new ArrayList<UserTransferObject>();
        List<List<UserDLS>> dlsList = new ArrayList<List<UserDLS>>();
        JSONArray userList = (JSONArray) jsonObject.get("users");
        for (int p = 0; p < userList.size(); p++) {
            JSONObject jO = (JSONObject) userList.get(p);
            UserTransferObject user = new UserTransferObject();
            user.setEmail(jO.get("email").toString());
            user.setUserName(jO.get("user_name").toString());
            user.setFirstName(jO.get("first_name").toString());
            user.setName(jO.get("last_name").toString());
            user.setAccountAdmin((Boolean) jO.get("account_admin"));
            user.setAnalyst((Boolean) jO.get("analyst"));
            user.setInitialSetupDone((Boolean) jO.get("initial_setup_done"));
            user.setInvoiceRecipient((Boolean) jO.get("invoice_recipient"));
            user.setTestAccountVisible((Boolean) jO.get("test_account_visible"));
            users.add(user);
            String persona = jO.get("persona").toString();
            List<UserDLS> dlsInfo = new ArrayList<UserDLS>();
            dlsList.add(dlsInfo);
            if (!"".equals(persona)) {
                PreparedStatement ps = conn.prepareStatement("SELECT PERSONA_ID FROM PERSONA WHERE PERSONA_NAME = ? AND ACCOUNT_ID = ?");
                ps.setString(1, persona);
                ps.setLong(2, SecurityUtil.getAccountID());
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new RuntimeException("Could not find persona " + persona);
                }
                long personaID = rs.getLong(1);

                Persona personaObject = null;
                for (Persona persona1 : personas) {
                    if (persona1.getName().equals(persona)) {
                        personaObject = persona1;
                    }
                }

                if (personaObject == null) {
                    throw new RuntimeException("Could not find persona " + persona);
                }
                List<DataSourceDLS> dataSourceDLSList = personaObject.getDataSourceDLS();
                user.setPersonaID(personaID);
                JSONArray dlsArray = (JSONArray) jO.get("dls");

                for (int i = 0; i < dlsArray.size(); i++) {
                    UserDLS userDLS = new UserDLS();
                    List<UserDLSFilter> userDLSFilters = new ArrayList<UserDLSFilter>();
                    dlsInfo.add(userDLS);
                    JSONObject dlsData = (JSONObject) dlsArray.get(i);
                    String dataSourceKey = dlsData.get("data_source").toString();
                    PreparedStatement stmt = conn.prepareStatement("SELECT DATA_FEED_ID FROM DATA_FEED, UPLOAD_POLICY_USERS, USER WHERE FEED_NAME = ? AND " +
                            "DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND " +
                            "UPLOAD_POLICY_USERS.USER_ID = USER.USER_ID AND USER.ACCOUNT_ID = ?");
                    stmt.setString(1, dataSourceKey);
                    stmt.setLong(2, SecurityUtil.getAccountID());
                    ResultSet dsRS = stmt.executeQuery();
                    dsRS.next();
                    long dsID = dsRS.getLong(1);
                    long dlsID = 0;
                    for (DataSourceDLS dataSourceDLS : dataSourceDLSList) {
                        if (dataSourceDLS.getDataSourceID() == dsID) {
                            dlsID = dataSourceDLS.getDataSourceDLSID();
                        }
                    }
                    userDLS.setDataSourceID(dsID);
                    userDLS.setDlsID(dlsID);
                    userDLS.setUserDLSFilterList(userDLSFilters);
                    boolean matched = false;
                    for (DataSourceDLS dataSourceDLS : personaObject.getDataSourceDLS()) {
                        if (dataSourceDLS.getDataSourceID() == dsID) {
                            matched = true;
                            List<FilterDefinition> fs = dataSourceDLS.getFilters();
                            JSONArray filters = (JSONArray) dlsData.get("filters");
                            for (int j = 0; j < filters.size(); j++) {
                                JSONObject filterObject = (JSONObject) filters.get(j);
                                String fieldName = filterObject.get("field").toString();
                                for (FilterDefinition filterDefinition : fs) {
                                    if (filterDefinition.getField().toDisplay().equals(fieldName)) {
                                        JSONArray values = (JSONArray) filterObject.get("values");
                                        FilterValueDefinition clonee = (FilterValueDefinition) filterDefinition.clone();
                                        List<Object> filterValues = new ArrayList<Object>();
                                        for (int k = 0; k < values.size(); k++) {
                                            String value = values.get(k).toString();
                                            filterValues.add(value);
                                        }
                                        clonee.setFilteredValues(filterValues);
                                        UserDLSFilter userDLSFilter = new UserDLSFilter();
                                        userDLSFilter.setOriginalFilterID(filterDefinition.getFilterID());
                                        userDLSFilter.setFilterDefinition(clonee);
                                        userDLSFilters.add(userDLSFilter);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < userList.size(); i++) {
            UserTransferObject userTransferObject = users.get(i);
            List<UserDLS> userDLSList = dlsList.get(i);

            UserCreationResponse response = new UserAccountAdminService().addUserToAccount(userTransferObject, userDLSList, conn, "");
            System.out.println("...");
        }
        /*for (UserTransferObject user : users) {

        }*/

        return new ResponseInfo(200, "");
    }

    @Override
    protected ResponseInfo processGet(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        JSONObject result = new JSONObject();
        JSONArray userList = new JSONArray();
        List<UserTransferObject> users = new UserAccountAdminService().getUsers();
        for (UserTransferObject user : users) {
            JSONObject userObject = new JSONObject();
            userObject.put("email", user.getEmail());
            userObject.put("user_name", user.getEmail());
            userObject.put("first_name", user.getFirstName());
            userObject.put("last_name", user.getName());
            userObject.put("account_admin", user.isAccountAdmin());
            userObject.put("analyst", user.isAnalyst());
            userObject.put("initial_setup_done", user.isInitialSetupDone());
            userObject.put("invoice_recipient", user.isInvoiceRecipient());
            userObject.put("test_account_visible", user.isTestAccountVisible());
            PreparedStatement stmt = conn.prepareStatement("SELECT PERSONA.PERSONA_NAME FROM PERSONA WHERE PERSONA_ID = ?");
            stmt.setLong(1, user.getPersonaID());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userObject.put("persona", rs.getString(1));
                List<UserDLS> dls = new PreferencesService().getUserDLS(user.getUserID(), user.getPersonaID());
                JSONArray dlsInfo = new JSONArray();
                for (UserDLS d : dls) {
                    JSONObject dlsObject = new JSONObject();
                    // api key, not name
                    dlsObject.put("data_source", d.getDataSourceName());

                    JSONArray filters = new JSONArray();

                    for (UserDLSFilter dlsFilter : d.getUserDLSFilterList()) {
                        FilterValueDefinition filter = (FilterValueDefinition) dlsFilter.getFilterDefinition();
                        JSONObject filterObject = new JSONObject();
                        filterObject.put("field", filter.getField().toDisplay());
                        List<Object> filteredValues = filter.getFilteredValues();
                        JSONArray values = new JSONArray();
                        for (Object value : filteredValues) {
                            values.add(value.toString());
                        }
                        filterObject.put("values", values);
                        filters.add(filterObject);
                    }
                    dlsObject.put("filters", filters);

                    dlsInfo.add(dlsObject);
                }
                userObject.put("dls", dlsInfo);
            } else {
                userObject.put("persona", "");
            }
            stmt.close();
            userObject.put("user_id", user.getUserName());

            userList.add(userObject);
        }
        result.put("users", userList);
        ResponseInfo responseInfo = new ResponseInfo(200, result.toString());
        return responseInfo;
    }

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        throw new UnsupportedOperationException();
    }
}
