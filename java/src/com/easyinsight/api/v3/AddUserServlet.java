package com.easyinsight.api.v3;

import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.FilterValueDefinition;
import com.easyinsight.dashboard.DashboardService;
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

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 3/21/14
 * Time: 12:06 PM
 */
@WebServlet(value = "/json/addUsers", asyncSupported = true)
public class AddUserServlet extends JSONServlet {


    @Override
    protected ResponseInfo processPost(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {

        SecurityUtil.authorizeAccountAdmin();

        List<Persona> personas = new PreferencesService().getPersonas();
        UserTransferObject user = new UserTransferObject();
        user.setEmail(jsonObject.get("email").toString());
        user.setUserName(jsonObject.get("user_name").toString());
        user.setFirstName(jsonObject.get("first_name").toString());
        user.setName(jsonObject.get("last_name").toString());
        if (jsonObject.get("only_show_top_dashboards") != null) {
            user.setOnlyShowTopReports(Boolean.parseBoolean(jsonObject.get("only_show_top_dashboards").toString()));
        }
        if (jsonObject.get("fixed_dashboard") != null) {
            String fixedDashboard = (String) jsonObject.get("fixed_dashboard");
            if (!"".equals(fixedDashboard)) {
                long dashboardID;
                dashboardID = DashboardService.canAccessDashboard(fixedDashboard, conn);
                if (dashboardID == 0) {
                    throw new RuntimeException("Fixed dashboard by ID " + fixedDashboard + " was not found.");
                }
                user.setFixedDashboardID(dashboardID);
            }
        }

        String persona = jsonObject.get("persona").toString();
        List<UserDLS> dlsInfo = new ArrayList<UserDLS>();
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
            user.setPersonaID(personaID);
            JSONObject dlsData = (JSONObject) jsonObject.get("dls");


            UserDLS userDLS = new UserDLS();
            List<UserDLSFilter> userDLSFilters = new ArrayList<UserDLSFilter>();
            dlsInfo.add(userDLS);
            String dataSourceKey = dlsData.get("data_source").toString();
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(new FeedService().openFeedByAPIKey(dataSourceKey).getFeedDescriptor().getId());
            userDLS.setDataSourceID(dataSource.getDataFeedID());
            userDLS.setUserDLSFilterList(userDLSFilters);
            boolean matched = false;
            for (DataSourceDLS dataSourceDLS : personaObject.getDataSourceDLS()) {
                if (dataSourceDLS.getDataSourceID() == dataSource.getDataFeedID()) {
                    userDLS.setDlsID(dataSourceDLS.getDataSourceDLSID());
                    matched = true;
                    List<FilterDefinition> fs = dataSourceDLS.getFilters();
                    JSONArray filters = (JSONArray) dlsData.get("filters");
                    for (int j = 0; j < filters.size(); j++) {
                        JSONObject filterObject = (JSONObject) filters.get(j);
                        String fieldName = filterObject.get("field_name").toString();
                        for (FilterDefinition filterDefinition : fs) {
                            if (filterDefinition.getField().toDisplay().equals(fieldName)) {
                                JSONArray values = (JSONArray) filterObject.get("values");
                                FilterValueDefinition clonee = (FilterValueDefinition) filterDefinition.clone();
                                List<Object> filterValues = new ArrayList<Object>();
                                for (int k = 0; k < values.size(); k++) {
                                    String value = (String) values.get(k);
                                    filterValues.add(value);
                                }
                                clonee.setFilteredValues(filterValues);
                                UserDLSFilter userDLSFilter = new UserDLSFilter();
                                userDLSFilter.setFilterDefinition(clonee);
                                userDLSFilter.setOriginalFilterID(filterDefinition.getFilterID());
                                userDLSFilters.add(userDLSFilter);
                            }
                        }
                    }
                }
            }
        }

        UserCreationResponse userCreationResponse = new UserAccountAdminService().addUserToAccount(user, dlsInfo, true, true);
        if (userCreationResponse.isSuccessful()) {
            return new ResponseInfo(200, "");
        } else {
            JSONObject response = new JSONObject();
            response.put("message", userCreationResponse.getFailureMessage());
            return new ResponseInfo(500, response.toString());
        }
    }

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        throw new UnsupportedOperationException();
    }
}
