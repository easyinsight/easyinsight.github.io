package com.easyinsight.api.v3;

import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.FilterValueDefinition;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.preferences.*;
import com.easyinsight.users.UserAccountAdminService;
import com.easyinsight.users.UserTransferObject;
import net.minidev.json.JSONObject;
import org.json.JSONArray;

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
public class AddUserServlet extends JSONServlet {

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        List<Persona> personas = new PreferencesService().getPersonas();
        UserTransferObject user = new UserTransferObject();
        user.setEmail(jsonObject.get("email").toString());
        user.setUserName(jsonObject.get("user_name").toString());
        user.setFirstName(jsonObject.get("first_name").toString());
        user.setName(jsonObject.get("last_name").toString());
        String persona = jsonObject.get("persona").toString();
        List<UserDLS> dlsInfo = new ArrayList<UserDLS>();
        if (!"".equals(persona)) {
            PreparedStatement ps = conn.prepareStatement("SELECT PERSONA_ID FROM PERSONA WHERE PERSONA_NAME = ?");
            ps.setString(1, persona);
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
            JSONArray dlsArray = (JSONArray) jsonObject.get("dls");

            for (int i = 0; i < dlsArray.length(); i++) {
                UserDLS userDLS = new UserDLS();
                List<UserDLSFilter> userDLSFilters = new ArrayList<UserDLSFilter>();
                dlsInfo.add(userDLS);
                JSONObject dlsData = (JSONObject) dlsArray.get(i);
                String dataSourceKey = dlsData.get("data_source").toString();
                FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(new FeedService().openFeedByAPIKey(dataSourceKey).getFeedDescriptor().getId());
                userDLS.setDataSourceID(dataSource.getDataFeedID());
                userDLS.setUserDLSFilterList(userDLSFilters);
                boolean matched = false;
                for (DataSourceDLS dataSourceDLS : personaObject.getDataSourceDLS()) {
                    if (dataSourceDLS.getDataSourceID() == dataSource.getDataFeedID()) {
                        matched = true;
                        List<FilterDefinition> fs = dataSourceDLS.getFilters();
                        JSONArray filters = (JSONArray) dlsData.get("filters");
                        for (int j = 0; j < filters.length(); j++) {
                            JSONObject filterObject = (JSONObject) filters.get(j);
                            String fieldName = filterObject.get("field_name").toString();
                            for (FilterDefinition filterDefinition : fs) {
                                if (filterDefinition.getField().toDisplay().equals(fieldName)) {
                                    JSONArray values = (JSONArray) filterObject.get("values");
                                    FilterValueDefinition clonee = (FilterValueDefinition) filterDefinition.clone();
                                    List<Object> filterValues = new ArrayList<Object>();
                                    for (int k = 0; k < values.length(); k++) {
                                        String value = values.getString(k);
                                        filterValues.add(value);
                                    }
                                    clonee.setFilteredValues(filterValues);
                                    UserDLSFilter userDLSFilter = new UserDLSFilter();
                                    userDLSFilter.setFilterDefinition(clonee);
                                    userDLSFilters.add(userDLSFilter);
                                }
                            }
                        }
                    }
                }
            }
        }

        new UserAccountAdminService().updateUser(user, dlsInfo);
        return new ResponseInfo(200, "");
    }
}
