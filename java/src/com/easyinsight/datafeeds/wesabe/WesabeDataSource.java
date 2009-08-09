package com.easyinsight.datafeeds.wesabe;

import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.IServerDataSourceDefinition;
import com.easyinsight.datafeeds.CredentialsDefinition;
import com.easyinsight.users.Account;
import com.easyinsight.users.Credentials;
import com.easyinsight.analysis.DataSourceInfo;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.util.*;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;

import org.w3c.dom.Document;

/**
 * User: jboe
 * Date: Jul 16, 2009
 * Time: 9:47:13 AM
 */
public class WesabeDataSource extends CompositeServerDataSource {
    
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> feedTypes = new HashSet<FeedType>();
        feedTypes.add(FeedType.WESABE_ACCOUNTS);
        feedTypes.add(FeedType.WESABE_TRANSACTIONS);
        return feedTypes;
    }

    @Override
    public String validateCredentials(Credentials credentials) {
        try {
            String client_id = "EasyInsight";  // Change this to the name of your application
            String client_version = "1.0";          // Give your application a version number
            String api_version = "1.0.0";        // Document which API version you're using

            Properties sys = System.getProperties();
            String system_name = sys.getProperty("os.name");
            String system_release = sys.getProperty("os.version");

            String userAgent = client_id + "/" + client_version +
                    " (" + system_name + " " + system_release + ")" +
                    " Wesabe-API/" + api_version;
            String credentialString = credentials.getUserName() + ":" + credentials.getPassword();
            String encoding = new sun.misc.BASE64Encoder().encode(credentialString.getBytes());
            URL wesabe = new URL("https://www.wesabe.com/accounts.xml");
            URLConnection connection = wesabe.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            connection.setRequestProperty("User-Agent", userAgent);
            InputStream content = connection.getInputStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.parse(content);
            return null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.WESABE;
    }

    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }

    @Override
    public int getCredentialsDefinition() {
        return CredentialsDefinition.STANDARD_USERNAME_PW;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.INDIVIDUAL;
    }

    @Override
    public boolean isConfigured() {
        return true;
    }

    protected Collection<ChildConnection> getChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.WESABE_ACCOUNTS, FeedType.WESABE_TRANSACTIONS, WesabeBaseSource.ACCOUNT_ID, WesabeBaseSource.ACCOUNT_ID));
    }

    protected IServerDataSourceDefinition createForFeedType(FeedType feedType) {
        if (feedType.equals(FeedType.WESABE_ACCOUNTS)) {
            return new WesabeAccountDataSource();
        } else if (feedType.equals(FeedType.WESABE_TRANSACTIONS)) {
            return new WesabeTransactionDataSource();
        }
        throw new RuntimeException();
    }
}
