package com.easyinsight.jsphelpers;

import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.html.RedirectUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 4/1/14
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class EIHelper {

    public static void sortStuff(List<? extends EIDescriptor> dataSources) {
        Collections.sort(dataSources, new Comparator<EIDescriptor>() {

            public int compare(EIDescriptor eiDescriptor, EIDescriptor eiDescriptor1) {
                String name1 = eiDescriptor.getName() != null ? eiDescriptor.getName() : "";
                String name2 = eiDescriptor1.getName() != null ? eiDescriptor1.getName() : "";
                return name1.compareToIgnoreCase(name2);
            }
        });
    }

    public static void sort(List list) {
        Collections.sort(list);
    }

    public static void buildURL(HttpServletRequest request, StringBuilder sb, String reportName, String urlKey) {
        sb.append("<a href=\"").append(RedirectUtil.getURL(request, "/app/html/report/" + urlKey)).append("\">").append(reportName).append("</a>");
        sb.append(",");
    }
}
