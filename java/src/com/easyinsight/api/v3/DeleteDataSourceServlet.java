package com.easyinsight.api.v3;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import nu.xom.Document;
import org.hibernate.HibernateException;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;

/**
 * User: jamesboe
 * Date: 2/4/12
 * Time: 1:26 PM
 */
public class DeleteDataSourceServlet extends APIServlet {
    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn, HttpServletRequest request) throws Exception {
        long dataSourceID = Long.parseLong(request.getParameter("dataSourceID"));
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);
        try {
            feedDefinition.delete(conn);
        } catch (HibernateException e) {
            LogClass.error(e);
            PreparedStatement manualDeleteStmt = conn.prepareStatement("DELETE FROM DATA_FEED WHERE DATA_FEED_ID = ?");
            manualDeleteStmt.setLong(1, dataSourceID);
            manualDeleteStmt.executeUpdate();
        }
        return new ResponseInfo(ResponseInfo.ALL_GOOD, "<message>The data source was deleted.</message>");
    }
}
