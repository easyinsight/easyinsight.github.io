package com.easyinsight.datafeeds;

import com.easyinsight.html.RedirectUtil;
import com.easyinsight.logging.LogClass;
import com.easyinsight.users.OAuthResponse;
import com.easyinsight.users.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
                    int intValue = Integer.parseInt(value);
                    method.invoke(dataSource, intValue);
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
                    OAuthResponse response = new TokenService().getOAuthResponse(dataSourceType, true, dataSource, TokenService.HTML_SETUP, request.getSession());
                    servletResponse.sendRedirect(response.getRequestToken());
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
        properties.add(new HTMLConnectionProperty(field, property, null, false, HTMLConnectionProperty.STRING));
        return this;
    }

    public HTMLConnectionFactory addField(String field, String property, int type) {
        properties.add(new HTMLConnectionProperty(field, property, null, false, type));
        return this;
    }

    public HTMLConnectionFactory addPassword(String field, String property, boolean password) {
        properties.add(new HTMLConnectionProperty(field, property, null, password, HTMLConnectionProperty.STRING));
        return this;
    }

    public HTMLConnectionFactory addField(String field, String property, String explanation) {
        properties.add(new HTMLConnectionProperty(field, property, explanation, false, HTMLConnectionProperty.STRING));
        return this;
    }
}
