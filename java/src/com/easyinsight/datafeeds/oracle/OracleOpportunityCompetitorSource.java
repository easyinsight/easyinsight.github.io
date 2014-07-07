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

public class OracleOpportunityCompetitorSource extends OracleBaseSource {
	public OracleOpportunityCompetitorSource() {
        setFeedName("OpportunityCompetitor");
    }

	public static final String PARTYNAME = "PartyName";
	public static final String THREATLEVELCODE = "ThreatLevelCode";
	public static final String COMMENTS = "Comments";
	public static final String OPTYCOMPETITORID = "OptyCompetitorId";
	public static final String OPTYID = "OptyId";
	public static final String CMPTPARTYID = "CmptPartyId";
	public static final String PRIMARYFLG = "PrimaryFlg";
	public static final String NAME = "Name";
	public static final String REVNAMOUNTCURCYCODE = "RevnAmountCurcyCode";
	public static final String PARTYNAME1 = "PartyName1";
	public static final String PRIMARYCONTACTPARTYNAME = "PrimaryContactPartyName";
	public static final String NAME1 = "Name1";
	public static final String STGID = "StgId";
	public static final String WINPROB = "WinProb";
	public static final String EFFECTIVEDATE = "EffectiveDate";
	public static final String PARTYUNIQUENAME = "PartyUniqueName";
	public static final String PARTYUNIQUENAME1 = "PartyUniqueName1";
	public static final String SALESACCOUNTID = "SalesAccountId";
	public static final String SALESACCOUNTUNIQUENAME = "SalesAccountUniqueName";
	public static final String CREATEDBY = "CreatedBy";
	public static final String CREATIONDATE = "CreationDate";
	public static final String LASTUPDATEDATE = "LastUpdateDate";
	public static final String LASTUPDATELOGIN = "LastUpdateLogin";
	public static final String LASTUPDATEDBY = "LastUpdatedBy";

	protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {		fieldBuilder.addField(PARTYNAME, new AnalysisDimension());
		fieldBuilder.addField(THREATLEVELCODE, new AnalysisDimension());
		fieldBuilder.addField(COMMENTS, new AnalysisDimension());
		fieldBuilder.addField(OPTYCOMPETITORID, new AnalysisDimension());
		fieldBuilder.addField(OPTYID, new AnalysisDimension());
		fieldBuilder.addField(CMPTPARTYID, new AnalysisDimension());
		fieldBuilder.addField(PRIMARYFLG, new AnalysisDimension());
		fieldBuilder.addField(NAME, new AnalysisDimension());
		fieldBuilder.addField(REVNAMOUNTCURCYCODE, new AnalysisDimension());
		fieldBuilder.addField(PARTYNAME1, new AnalysisDimension());
		fieldBuilder.addField(PRIMARYCONTACTPARTYNAME, new AnalysisDimension());
		fieldBuilder.addField(NAME1, new AnalysisDimension());
		fieldBuilder.addField(STGID, new AnalysisDimension());
		fieldBuilder.addField(WINPROB, new AnalysisMeasure());
		fieldBuilder.addField(EFFECTIVEDATE, new AnalysisDateDimension(true));
		fieldBuilder.addField(PARTYUNIQUENAME, new AnalysisDimension());
		fieldBuilder.addField(PARTYUNIQUENAME1, new AnalysisDimension());
		fieldBuilder.addField(SALESACCOUNTID, new AnalysisDimension());
		fieldBuilder.addField(SALESACCOUNTUNIQUENAME, new AnalysisDimension());
		fieldBuilder.addField(CREATEDBY, new AnalysisDimension());
		fieldBuilder.addField(CREATIONDATE, new AnalysisDateDimension(true));
		fieldBuilder.addField(LASTUPDATEDATE, new AnalysisDateDimension(true));
		fieldBuilder.addField(LASTUPDATELOGIN, new AnalysisDimension());
		fieldBuilder.addField(LASTUPDATEDBY, new AnalysisDimension());
	}

@Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
	try {
		DataSet dataSet = new DataSet();
		OracleDataSource oracleDataSource = (OracleDataSource) parentDefinition;
		List<com.easyinsight.datafeeds.oracle.client.OpportunityCompetitor> list = oracleDataSource.getOpportunityCompetitor2();
		for (com.easyinsight.datafeeds.oracle.client.OpportunityCompetitor o : list) {
		IRow row = dataSet.createRow();
		row.addValue(keys.get(PARTYNAME), o.getPartyName());
		row.addValue(keys.get(THREATLEVELCODE), o.getThreatLevelCode().getValue());
		row.addValue(keys.get(COMMENTS), o.getComments().getValue());
		row.addValue(keys.get(OPTYCOMPETITORID), String.valueOf(o.getOptyCompetitorId()));
		row.addValue(keys.get(OPTYID), String.valueOf(o.getOptyId()));
		row.addValue(keys.get(CMPTPARTYID), String.valueOf(o.getCmptPartyId()));
		row.addValue(keys.get(PRIMARYFLG), o.getPrimaryFlg().getValue());
		row.addValue(keys.get(NAME), o.getName());
		row.addValue(keys.get(REVNAMOUNTCURCYCODE), o.getRevnAmountCurcyCode());
		row.addValue(keys.get(PARTYNAME1), o.getPartyName1());
		row.addValue(keys.get(PRIMARYCONTACTPARTYNAME), o.getPrimaryContactPartyName());
		row.addValue(keys.get(NAME1), o.getName1());
		row.addValue(keys.get(STGID), String.valueOf(o.getStgId()));
		row.addValue(keys.get(WINPROB), getMeasureValue(o.getWinProb()));
		row.addValue(keys.get(EFFECTIVEDATE), getDate(o.getEffectiveDate()));
		row.addValue(keys.get(PARTYUNIQUENAME), o.getPartyUniqueName().getValue());
		row.addValue(keys.get(PARTYUNIQUENAME1), o.getPartyUniqueName1().getValue());
		row.addValue(keys.get(SALESACCOUNTID), String.valueOf(o.getSalesAccountId().getValue()));
		row.addValue(keys.get(SALESACCOUNTUNIQUENAME), o.getSalesAccountUniqueName().getValue());
		row.addValue(keys.get(CREATEDBY), o.getCreatedBy());
		row.addValue(keys.get(CREATIONDATE), getDate(o.getCreationDate()));
		row.addValue(keys.get(LASTUPDATEDATE), getDate(o.getLastUpdateDate()));
		row.addValue(keys.get(LASTUPDATELOGIN), o.getLastUpdateLogin().getValue());
		row.addValue(keys.get(LASTUPDATEDBY), o.getLastUpdatedBy());
		}

		return dataSet;
	} catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
}

	@Override
    public FeedType getFeedType() {
        return FeedType.ORACLE_OPPORTUNITYCOMPETITOR;
    }

}