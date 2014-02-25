package com.easyinsight.datafeeds.surveygizmo;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.datafeeds.wufoo.WufooCompositeSource;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 2/20/14
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class SurveyGizmoFormSource extends CompositeServerDataSource {
    private String formID;

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public String getFormID() {
        return formID;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SURVEYGIZMO_FORM;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM SURVEYGIZMO_FORM_SOURCE WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO SURVEYGIZMO_FORM_SOURCE (FORM_ID, DATA_SOURCE_ID) VALUES (?, ?)");
        insertStmt.setString(1, formID);
        insertStmt.setLong(2, getDataFeedID());
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement getStmt = conn.prepareStatement("SELECT FORM_ID FROM SURVEYGIZMO_FORM_SOURCE WHERE DATA_SOURCE_ID = ?");
        getStmt.setLong(1, getDataFeedID());
        ResultSet rs = getStmt.executeQuery();
        if (rs.next()) {
            formID = rs.getString(1);
        }
        getStmt.close();
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }


    @Override
    protected List<IServerDataSourceDefinition> childDataSources(EIConnection conn) throws Exception {
        List<IServerDataSourceDefinition> defaultChildren = super.childDataSources(conn);
        boolean metadataCreated = false;
        for (CompositeFeedNode existing : getCompositeFeedNodes()) {
            if (existing.getDataSourceType() == FeedType.SURVEYGIZMO_FORM_METADATA.getType()) {
                metadataCreated = true;
            }
        }
        if (!metadataCreated) {
            SurveyGizmoMetadataSource source = new SurveyGizmoMetadataSource();
            newDefinition(source, conn, "", getUploadPolicy());
            CompositeFeedNode node = new CompositeFeedNode();
            node.setDataFeedID(source.getDataFeedID());
            node.setDataSourceType(source.getFeedType().getType());
            getCompositeFeedNodes().add(node);
            defaultChildren.add(source);
        }
        return defaultChildren;
    }

}
