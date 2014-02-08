package com.easyinsight.datafeeds;

import com.easyinsight.html.RedirectUtil;
import com.easyinsight.logging.LogClass;
import com.easyinsight.users.OAuthResponse;
import com.easyinsight.users.TokenService;
import com.easyinsight.userupload.UserUploadService;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
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
    private int dataSourceType;
    private List<Property> properties = new ArrayList<Property>();

    public HTMLConnectionFactory(int dataSourceType) {
        this.dataSourceType = dataSourceType;
        FeedDefinition dataSource = new DataSourceTypeRegistry().createDataSource(new FeedType(dataSourceType));
        dataSource.configureFactory(this);
    }

    private class Property {
        private String field;
        private String property;
        private String safeProperty;
        private String explanation;
        private boolean password;

        private Property(String field, String property, @Nullable String explanation, boolean password) {
            this.field = field;
            this.property = property;
            safeProperty = "p" + property;
            this.explanation = explanation;
            this.password = password;
        }
    }



    public String toConnectionHTML() {
        /*
        <label for="userName" class="promptLabel">
                    User Name or Email
                </label>
                <input type="text" class="form-control" name="userName" id="userName" style="width:100%;font-size:14px;height:28px" autocapitalize="off" autocorrect="off" autoFocus/>
         */
        StringBuilder sb = new StringBuilder();
        for (Property property : properties) {

            sb.append("<label for=\"").append(property.safeProperty).append("\" class=\"promptLabel\">").append(property.field).append("</label>\r\n");
            if (property.password) {
                sb.append("<input type=\"password\" class=\"form-control\" name=\"").append(property.safeProperty).append("\" id=\"").append(property.safeProperty).append("\" ");
            } else {
                sb.append("<input type=\"text\" class=\"form-control\" name=\"").append(property.safeProperty).append("\" id=\"").append(property.safeProperty).append("\" ");
            }
            sb.append("style=\"width:100%;font-size:14px;height:28px\" autocapitalize=\"off\" autocorrect=\"off\"/>");

        }
        return sb.toString();
    }

    public int getType() {
        return type;
    }

    public void actionProcess(HttpServletRequest request, HttpServletResponse servletResponse, FeedDefinition dataSource) {
        try {
            for (Property property : properties) {
                String value = request.getParameter(property.safeProperty);
                String setter = "set" + Character.toUpperCase(property.property.charAt(0)) + property.property.substring(1);
                Method method = dataSource.getClass().getMethod(setter, String.class);
                method.invoke(dataSource, value);
            }
            dataSource.validateCredentials();
            // launch via ajax
            if (type == TYPE_OAUTH) {
                OAuthResponse response = new TokenService().getOAuthResponse(dataSourceType, true, dataSource, TokenService.HTML_SETUP, request.getSession());
                servletResponse.sendRedirect(response.getRequestToken());
            } else {
                new FeedStorage().updateDataFeedConfiguration(dataSource);
                servletResponse.sendRedirect(RedirectUtil.getURL(request, "/app/html/dataSources/ "+ dataSource.getApiKey() + "/createConnection"));
            }
        } catch (Exception e) {
            LogClass.error(e);
        }

    }

    public HTMLConnectionFactory type(int type) {
        this.type = type;
        return this;
    }

    public HTMLConnectionFactory addField(String field, String property) {
        properties.add(new Property(field, property, null, false));
        return this;
    }

    public HTMLConnectionFactory addPassword(String field, String property, boolean password) {
        properties.add(new Property(field, property, null, password));
        return this;
    }

    public HTMLConnectionFactory addField(String field, String property, String explanation) {
        properties.add(new Property(field, property, explanation, false));
        return this;
    }
}
