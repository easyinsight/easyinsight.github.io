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
                .allowElements("li", "ul", "p", "a", "b", "i")
                .allowAttributes("href").onElements("a").allowStandardUrlProtocols()
                .toFactory();
    }
}
