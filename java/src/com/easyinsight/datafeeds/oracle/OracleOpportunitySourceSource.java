package com.easyinsight.datafeeds.oracle;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.oracle.client.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;

import javax.xml.ws.BindingProvider;
import java.net.URL;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class OracleOpportunitySourceSource extends OracleBaseSource {
	public OracleOpportunitySourceSource() {
        setFeedName("OpportunitySource");
    }

	public static final String OPTYSRCID = "OptySrcId";
	public static final String OPTYID = "OptyId";
	public static final String SRCNUMBER = "SrcNumber";
	public static final String CONFLICTID = "ConflictId";
	public static final String CREATIONDATE = "CreationDate";
	public static final String CREATEDBY = "CreatedBy";
	public static final String LASTUPDATEDATE = "LastUpdateDate";
	public static final String LASTUPDATEDBY = "LastUpdatedBy";
	public static final String LASTUPDATELOGIN = "LastUpdateLogin";
	public static final String USERLASTUPDATEDATE = "UserLastUpdateDate";

	protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {		fieldBuilder.addField(OPTYSRCID, new AnalysisDimension());
		fieldBuilder.addField(OPTYID, new AnalysisDimension());
		fieldBuilder.addField(SRCNUMBER, new AnalysisDimension());
		fieldBuilder.addField(CONFLICTID, new AnalysisDimension());
		fieldBuilder.addField(CREATIONDATE, new AnalysisDateDimension());
		fieldBuilder.addField(CREATEDBY, new AnalysisDimension());
		fieldBuilder.addField(LASTUPDATEDATE, new AnalysisDateDimension());
		fieldBuilder.addField(LASTUPDATEDBY, new AnalysisDimension());
		fieldBuilder.addField(LASTUPDATELOGIN, new AnalysisDimension());
		fieldBuilder.addField(USERLASTUPDATEDATE, new AnalysisDateDimension());
	}

@Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
	try {
		DataSet dataSet = new DataSet();
		OracleDataSource oracleDataSource = (OracleDataSource) parentDefinition;
		List<com.easyinsight.datafeeds.oracle.client.OpportunitySource> list = oracleDataSource.getOpportunitySource1();
		for (com.easyinsight.datafeeds.oracle.client.OpportunitySource o : list) {
		IRow row = dataSet.createRow();
		row.addValue(keys.get(OPTYSRCID), String.valueOf(o.getOptySrcId()));
		row.addValue(keys.get(OPTYID), String.valueOf(o.getOptyId()));
		row.addValue(keys.get(SRCNUMBER), o.getSrcNumber());
		row.addValue(keys.get(CONFLICTID), String.valueOf(o.getConflictId()));
		row.addValue(keys.get(CREATIONDATE), getDate(o.getCreationDate()));
		row.addValue(keys.get(CREATEDBY), o.getCreatedBy());
		row.addValue(keys.get(LASTUPDATEDATE), getDate(o.getLastUpdateDate()));
		row.addValue(keys.get(LASTUPDATEDBY), o.getLastUpdatedBy());
		row.addValue(keys.get(LASTUPDATELOGIN), o.getLastUpdateLogin().getValue());
		row.addValue(keys.get(USERLASTUPDATEDATE), getDate(o.getUserLastUpdateDate()));
		}

		return dataSet;
	} catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
}

	@Override
    public FeedType getFeedType() {
        return FeedType.ORACLE_OPPORTUNITYSOURCE;
    }

}