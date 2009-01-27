package com.easyinsight.datafeeds;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.ColumnSegment;
import com.easyinsight.users.Credentials;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Collection;

/**
 * User: James Boe
 * Date: Apr 26, 2008
 * Time: 8:07:53 PM
 */
public abstract class DynamicFeed extends StaticFeed {

    public void refresh(long accountID, Credentials credentials, String[] columns) {
        
    }
}
