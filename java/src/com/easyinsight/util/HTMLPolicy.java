package com.easyinsight.util;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

/**
 * User: jamesboe
 * Date: 3/25/14
 * Time: 7:57 AM
 */
public class HTMLPolicy {
    public static PolicyFactory getPolicyFactory() {
        return new HtmlPolicyBuilder()
                .allowElements("li")
                .allowElements("ul")
                .allowElements("p")
                .allowElements("a")
                .allowElements("b")
                .allowElements("i")
                .toFactory();
    }
}
