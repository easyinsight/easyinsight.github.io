package test;

import com.easyinsight.analysis.ReportCache;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.goals.InstallationSystem;
import com.easyinsight.userupload.CredentialsResponse;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.util.ServiceUtil;
import test.util.TestUtil;

/**
 * User: jamesboe
 * Date: 4/7/11
 * Time: 1:55 PM
 */
public abstract class DataSourceTesting {
    public void setup(String userName) {
        Database.initialize();
        ReportCache.initialize();
        ServiceUtil.initialize();
        if (userName == null) {
            TestUtil.getIndividualTestUser();
        } else {
            TestUtil.login(userName);
        }
    }

    public FeedDefinition create(FeedType feedType) throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            return new InstallationSystem(conn).installConnection(feedType.getType());
        } finally {
            Database.closeConnection(conn);
        }
    }

    public CredentialsResponse validate(FeedDefinition dataSource) throws Exception {
        return new UserUploadService().validateCredentials(dataSource);
    }

    public CredentialsResponse completeInstallation(FeedDefinition dataSource) throws Exception {
        return new UserUploadService().completeInstallation(dataSource);
    }

    protected abstract void configure(FeedDefinition dataSource);

    protected abstract FeedType getType();

    public void testDataSource(String userName) throws Exception {
        setup(userName);
        FeedDefinition dataSource = create(getType());
        configure(dataSource);
        validate(dataSource);
        completeInstallation(dataSource);

    }
}
