package com.easyinsight.servlet.snappcloud;

import com.easyinsight.users.User;
import com.easyinsight.users.UserTransferObject;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: 1/15/11
 * Time: 7:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class SnappCloudUserTO extends UserTransferObject {
    public SnappCloudUserTO() {
        super();
    }

    @Override
    public User toUser() {
        User u = super.toUser();
        return u;
    }
}
