package com.easyinsight.analysis;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: Nov 1, 2010
 * Time: 9:42:29 AM
 */
public class ReportFault implements Serializable {
    public String toHTML() {
        return "<div class=\"span12\" style=\"text-align:center\" id=\"reportErrorDiv\">\n" +
                "                We're sorry, but something went wrong in retrieving data for this report. We've logged the error on our servers and our engineers will fix it as soon as possible.\n" +
                "            </div>";
    }
}
