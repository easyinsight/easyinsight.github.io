package com.easyinsight.security;

import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.users.TokenStorage;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenService;

/**
 * User: jamesboe
 * Date: Aug 25, 2009
 * Time: 7:11:52 PM
 */
public class AuthorizationManager {
    public AuthorizationRequirement authorize(FeedType type, long solutionID) {
        if (type.equals(FeedType.GOOGLE) || type.equals(FeedType.GOOGLE_ANALYTICS)) {
            Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), type.getType());
            if (token == null) {
                return new AuthorizationRequirement(type.getType(), new TokenService().getAuthSubURL(type.getType(), solutionID));
            }
        }
        return null;
    }
}
