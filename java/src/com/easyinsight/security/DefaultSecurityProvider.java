package com.easyinsight.security;

import flex.messaging.FlexContext;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 11:23:10 AM
 */
public class DefaultSecurityProvider implements ISecurityProvider {
    public UserPrincipal getUserPrincipal() {
        if (FlexContext.getFlexSession() == null) {
            return null;
        }
        return (UserPrincipal) FlexContext.getFlexSession().getUserPrincipal();
    }
}
