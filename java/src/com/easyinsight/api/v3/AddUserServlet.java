package com.easyinsight.api.v3;

import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.FilterValueDefinition;
import com.easyinsight.dashboard.DashboardService;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.logging.LogClass;
import com.easyinsight.preferences.*;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.*;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.commons.io.IOUtils;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 3/21/14
 * Time: 12:06 PM
 */
@WebServlet(value = "/json/addUsers", asyncSupported = true)
public class AddUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SecurityUtil.populateThreadLocalFromSession(req);
        if (SecurityUtil.getUserID(false) == 0) {
            UserServiceResponse userResponse = null;
            String authHeader = req.getHeader("Authorization");
            if (authHeader == null) {
                if (req.getHeader("x-requested-with") == null || !"xmlhttprequest".equalsIgnoreCase(req.getHeader("x-requested-with")))
                    resp.addHeader("WWW-Authenticate", "Basic realm=\"Easy Insight\"");
                //sendError(401, "Your credentials were rejected.", resp);
                throw new RuntimeException();
                //return;
            }
            String headerValue = authHeader.split(" ")[1];
            BASE64Decoder decoder = new BASE64Decoder();
            String userPass = new String(decoder.decodeBuffer(headerValue));
            int p = userPass.indexOf(":");

            if (p != -1) {
                String userID = userPass.substring(0, p);
                String password = userPass.substring(p + 1);
                try {
                    userResponse = SecurityUtil.authenticateKeys(userID, password);
                } catch (com.easyinsight.security.SecurityException se) {
                    userResponse = new UserService().authenticate(userID, password, false);
                }
            }
            if (userResponse == null || !userResponse.isSuccessful()) {
                throw new RuntimeException();
            } else {
                SecurityUtil.populateThreadLocal(userResponse.getUserName(), userResponse.getUserID(), userResponse.getAccountID(),
                        userResponse.getAccountType(), userResponse.isAccountAdmin(), userResponse.getFirstDayOfWeek(), userResponse.getPersonaName());
            }
        }
        UserCreationResponse userCreationResponse;
        String userKey;
        String email;
        try {
            InputStream is = req.getInputStream();
            JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, "UTF-8");
            String jsonString = writer.toString();
            Object o = parser.parse(jsonString);
            JSONObject jsonObject = (JSONObject) o;

            SecurityUtil.authorizeAccountAdmin();

            UserTransferObject user;
            List<UserDLS> dlsInfo;
            EIConnection conn = Database.instance().getConnection();
            try {
                List<Persona> personas = new PreferencesService().getPersonas();

                user = new UserTransferObject();
                user.setEmail(jsonObject.get("email").toString());
                email = jsonObject.get("email").toString();
                user.setUserName(jsonObject.get("user_name").toString());
                user.setFirstName(jsonObject.get("first_name").toString());
                user.setName(jsonObject.get("last_name").toString());
                user.setTestAccountVisible(Boolean.parseBoolean(jsonObject.get("can_see_all_reports_and_dashboards").toString()));
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

                dlsInfo = new ArrayList<>();
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

            } catch (Exception e) {
                throw e;
            } finally {
                conn.setAutoCommit(false);
            }

            userCreationResponse = new UserAccountAdminService().addUserToAccount(user, dlsInfo, true, UserAccountAdminService.DEFAULT, true);

            if (userCreationResponse.isSuccessful()) {

                PreparedStatement uStmt = conn.prepareStatement("SELECT USER.USER_KEY FROM USER WHERE USER_ID = ?");
                uStmt.setLong(1, userCreationResponse.getUserID());
                ResultSet rs = uStmt.executeQuery();
                rs.next();
                userKey = rs.getString(1);
                uStmt.close();
            } else {
                userKey = null;
            }

            if (userCreationResponse.isSuccessful()) {
                JSONObject response = new JSONObject();
                response.put("user_key", userKey);
                response.put("email", email);
                resp.setContentType("application/json");
                resp.setStatus(200);
                resp.getOutputStream().write(response.toString().getBytes());
                resp.getOutputStream().flush();
            } else {
                JSONObject response = new JSONObject();
                response.put("message", userCreationResponse.getFailureMessage());
                resp.setContentType("application/json");
                resp.setStatus(500);
                resp.getOutputStream().write(response.toString().getBytes());
                resp.getOutputStream().flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject response = new JSONObject();
            response.put("message", "Something went wrong in server processing.");
            resp.setContentType("application/json");
            resp.setStatus(500);
            resp.getOutputStream().write(response.toString().getBytes());
            resp.getOutputStream().flush();
        } finally {
            SecurityUtil.clearThreadLocal();
        }


    }
}
