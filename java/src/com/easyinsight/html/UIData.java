package com.easyinsight.html;

import com.easyinsight.preferences.ApplicationSkin;
import com.easyinsight.preferences.ImageDescriptor;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * User: jamesboe
 * Date: 10/3/12
 * Time: 4:08 PM
 */
public class UIData {
    private ApplicationSkin applicationSkin;
    private String headerStyle;
    private String headerTextStyle;
    private ImageDescriptor headerImageDescriptor;

    public UIData(ApplicationSkin applicationSkin, String headerStyle, String headerTextStyle, ImageDescriptor headerImageDescriptor) {
        this.applicationSkin = applicationSkin;
        this.headerStyle = headerStyle;
        this.headerTextStyle = headerTextStyle;
        this.headerImageDescriptor = headerImageDescriptor;
    }

    public ApplicationSkin getApplicationSkin() {
        return applicationSkin;
    }

    public String getHeaderStyle() {
        return headerStyle;
    }

    public String getHeaderTextStyle() {
        return headerTextStyle;
    }

    public ImageDescriptor getHeaderImageDescriptor() {
        return headerImageDescriptor;
    }

    public String createHeader(String name) {

        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"row-fluid\">\n" +
                "        <div class=\"span12\">\n");
        if (applicationSkin != null && applicationSkin.isReportHeader()) {
            sb.append("            <div style=\""+headerStyle+"\">\n" +
                    "                <div style=\"padding:10px;float:left\">\n" +
                    "                    <div style=\"background-color: #FFFFFF;padding: 5px\">\n");
            if (headerImageDescriptor != null) {
                sb.append("                        <img src=\"/app/reportHeader?imageID="+headerImageDescriptor.getId()+"\" alt=\"Logo\"/>\n");
            }
            sb.append("                    </div>\n" +
                    "                </div>\n");
            sb.append("<div style=\"").append(headerTextStyle).append("\">");
            sb.append(StringEscapeUtils.escapeHtml(name));
            sb.append("</div>\n");
            sb.append("</div>\n");
        } else {
            sb.append("<div style=\"").append(headerTextStyle).append("\">");
            sb.append(StringEscapeUtils.escapeHtml(name));
            sb.append("</div>\n");
        }
        sb.append("</div></div>\n");

        return sb.toString();
    }
}
