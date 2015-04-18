package com.easyinsight.datafeeds;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.google.GoogleFeedDefinition;
import com.easyinsight.datafeeds.surveygizmo.SurveyGizmoCompositeSource;
import com.easyinsight.html.RedirectUtil;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.OAuthResponse;
import com.easyinsight.users.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 2/6/14
 * Time: 1:33 PM
 */
public class HTMLConnectionFactory {

    public static final int TYPE_BASIC_AUTH = 1;
    public static final int TYPE_OAUTH = 2;

    private int type;
    private String title;
    private int dataSourceType;
    private String actionSummary;
    private List<HTMLConnectionProperty> properties = new ArrayList<HTMLConnectionProperty>();

    private String name;

    public HTMLConnectionFactory(int dataSourceType) {
        this.dataSourceType = dataSourceType;
        FeedDefinition dataSource = new DataSourceTypeRegistry().createDataSource(new FeedType(dataSourceType));
        name = dataSource.getFeedName();
        title = "Let's create your connection to " + name + "...";
        dataSource.configureFactory(this);
    }

    public String getActionSummary() {
        return actionSummary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HTMLConnectionProperty> getProperties() {
        return properties;
    }

    public int getType() {
        return type;
    }

    public void actionProcess(HttpServletRequest request, HttpServletResponse servletResponse, FeedDefinition dataSource) {
        try {
            for (HTMLConnectionProperty property : properties) {
                String value = request.getParameter(property.getSafeProperty());
                String setter = "set" + Character.toUpperCase(property.getProperty().charAt(0)) + property.getProperty().substring(1);

                if (property.getType() == HTMLConnectionProperty.INTEGER) {
                    Method method = dataSource.getClass().getMethod(setter, int.class);
                    int intValue = 0;
                    try {
                        intValue = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        request.getSession().setAttribute("connectionError", "You'll need to specify a numeric value for " + property.getField() + ".");
                        servletResponse.sendRedirect(RedirectUtil.getURL(request, "/app/html/connections/" + dataSource.getFeedType().getType() + "?error=true"));
                        return;
                    }
                    method.invoke(dataSource, intValue);
                } else if (property.getType() == HTMLConnectionProperty.CHECKBOX) {
                    Method method = dataSource.getClass().getMethod(setter, boolean.class);
                    boolean booleanValue = "on".equals(value);
                    method.invoke(dataSource, booleanValue);
                } else {
                    Method method = dataSource.getClass().getMethod(setter, String.class);
                    method.invoke(dataSource, value);
                }
            }
            if (request.getParameter("pdataSourceName") != null) {
                dataSource.setFeedName(request.getParameter("pdataSourceName"));
            }
            String validation = dataSource.validateCredentials();
            if (validation != null) {
                request.getSession().setAttribute("connectionError", validation);
                servletResponse.sendRedirect(RedirectUtil.getURL(request, "/app/html/connections/"+ dataSource.getFeedType().getType() + "?error=true"));
            } else {
                // launch via ajax
                if (type == TYPE_OAUTH) {

                    // any chance we already have a connection of this type?

                    if (dataSource instanceof SurveyGizmoCompositeSource) {
                        Map<String, String> existingKeys = new HashMap<>();
                        EIConnection conn = Database.instance().getConnection();
                        try {
                            PreparedStatement queryStmt = conn.prepareStatement("SELECT survey_gizmo.token_key, survey_gizmo.secret_key FROM " +
                                    "survey_gizmo, upload_policy_users, user, data_feed WHERE " +
                                    "user.account_id = ? AND user.user_id = upload_policy_users.user_id AND " +
                                    "upload_policy_users.feed_id = survey_gizmo.data_source_id AND " +
                                    "upload_policy_users.feed_id = data_feed.data_feed_id AND " +
                                    "data_feed.visible = ?");
                            queryStmt.setLong(1, SecurityUtil.getAccountID());
                            queryStmt.setBoolean(2, true);
                            ResultSet rs = queryStmt.executeQuery();
                            while (rs.next()) {
                                String tokenKey = rs.getString(1);
                                String tokenSecret = rs.getString(2);
                                if (tokenKey != null && tokenSecret != null) {
                                    existingKeys.put(tokenKey, tokenSecret);
                                }
                            }
                        } finally {
                            Database.closeConnection(conn);
                        }
                        if (existingKeys.size() == 1) {
                            SurveyGizmoCompositeSource surveyGizmoCompositeSource = (SurveyGizmoCompositeSource) dataSource;
                            String key = existingKeys.keySet().iterator().next();
                            surveyGizmoCompositeSource.setSgToken(key);
                            surveyGizmoCompositeSource.setSgSecret(existingKeys.get(key));
                            new FeedStorage().updateDataFeedConfiguration(dataSource);
                            servletResponse.sendRedirect(RedirectUtil.getURL(request, "/app/html/dataSources/" + dataSource.getApiKey() + "/createConnection"));
                        } else {
                            OAuthResponse response = new TokenService().getOAuthResponse(dataSourceType, true, dataSource, TokenService.HTML_SETUP, request.getSession());
                            servletResponse.sendRedirect(response.getRequestToken());
                        }
                    }
                } else {
                    new FeedStorage().updateDataFeedConfiguration(dataSource);
                    servletResponse.sendRedirect(RedirectUtil.getURL(request, "/app/html/dataSources/" + dataSource.getApiKey() + "/createConnection"));
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
        }

    }

    public HTMLConnectionFactory type(int type) {
        this.type = type;
        if (type == TYPE_OAUTH) {
            actionSummary = "You'll need to grant access to Easy Insight to read your data. Click on the Authorize Access button and you'll be redirected to a page for granting access.";
        }
        return this;
    }

    public HTMLConnectionFactory addField(String field, String property) {
        properties.add(new HTMLConnectionProperty(field, property, null, false, HTMLConnectionProperty.STRING, ""));
        return this;
    }

    public HTMLConnectionFactory addFieldWithDefault(String field, String property, String defaultValue) {
        properties.add(new HTMLConnectionProperty(field, property, null, false, HTMLConnectionProperty.STRING, defaultValue));
        return this;
    }

    public HTMLConnectionFactory addField(String field, String property, int type) {
        properties.add(new HTMLConnectionProperty(field, property, null, false, type, ""));
        return this;
    }

    public HTMLConnectionFactory addFieldWithDefault(String field, String property, int type, String defaultValue) {
        properties.add(new HTMLConnectionProperty(field, property, null, false, type, defaultValue));
        return this;
    }

    public HTMLConnectionFactory addPassword(String field, String property, boolean password) {
        properties.add(new HTMLConnectionProperty(field, property, null, password, HTMLConnectionProperty.STRING, ""));
        return this;
    }

    public HTMLConnectionFactory addField(String field, String property, String explanation) {
        properties.add(new HTMLConnectionProperty(field, property, explanation, false, HTMLConnectionProperty.STRING, ""));
        return this;
    }
}
