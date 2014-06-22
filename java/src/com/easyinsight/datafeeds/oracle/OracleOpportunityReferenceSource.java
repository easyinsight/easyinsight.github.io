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

public class OracleOpportunityReferenceSource extends OracleBaseSource {
	public OracleOpportunityReferenceSource() {
        setFeedName("OpportunityReference");
    }

	public static final String OPTYREFERENCEID = "OptyReferenceId";
	public static final String OPTYID = "OptyId";
	public static final String REFERENCEPARTYID = "ReferencePartyId";
	public static final String COMMENTS = "Comments";
	public static final String CONFLICTID = "ConflictId";
	public static final String CREATEDBY = "CreatedBy";
	public static final String CREATIONDATE = "CreationDate";
	public static final String LASTUPDATEDBY = "LastUpdatedBy";
	public static final String LASTUPDATEDATE = "LastUpdateDate";
	public static final String LASTUPDATELOGIN = "LastUpdateLogin";
	public static final String USERLASTUPDATEDATE = "UserLastUpdateDate";
	public static final String STATUSCODE = "StatusCode";
	public static final String REFERENCERANKNUMBER = "ReferenceRankNumber";
	public static final String PARTYNAME1 = "PartyName1";
	public static final String STGID = "StgId";
	public static final String NAME = "Name";
	public static final String NAME1 = "Name1";
	public static final String WINPROB = "WinProb";
	public static final String REVNAMOUNTCURCYCODE = "RevnAmountCurcyCode";
	public static final String EFFECTIVEDATE = "EffectiveDate";
	public static final String TARGETPARTYNAME = "TargetPartyName";
	public static final String TARGETPARTYID = "TargetPartyId";
	public static final String PARTYNUMBER = "PartyNumber";
	public static final String PARTYNAME = "PartyName";
	public static final String PARTYTYPE = "PartyType";
	public static final String EMAILADDRESS = "EmailAddress";
	public static final String DUNSNUMBERC = "DUNSNumberC";
	public static final String COMMENTS1 = "Comments1";
	public static final String PARTYUNIQUENAME = "PartyUniqueName";
	public static final String PARTYUNIQUENAME1 = "PartyUniqueName1";
	public static final String INTCONTACTPARTYID = "IntContactPartyId";
	public static final String PARTYNAME2 = "PartyName2";
	public static final String CUSTOMERCONTACTRELATIONID = "CustomerContactRelationId";
	public static final String SALESACCOUNTID = "SalesAccountId";
	public static final String SALESACCOUNTUNIQUENAME = "SalesAccountUniqueName";

	protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {		fieldBuilder.addField(OPTYREFERENCEID, new AnalysisDimension());
		fieldBuilder.addField(OPTYID, new AnalysisDimension());
		fieldBuilder.addField(REFERENCEPARTYID, new AnalysisDimension());
		fieldBuilder.addField(COMMENTS, new AnalysisDimension());
		fieldBuilder.addField(CONFLICTID, new AnalysisDimension());
		fieldBuilder.addField(CREATEDBY, new AnalysisDimension());
		fieldBuilder.addField(CREATIONDATE, new AnalysisDateDimension());
		fieldBuilder.addField(LASTUPDATEDBY, new AnalysisDimension());
		fieldBuilder.addField(LASTUPDATEDATE, new AnalysisDateDimension());
		fieldBuilder.addField(LASTUPDATELOGIN, new AnalysisDimension());
		fieldBuilder.addField(USERLASTUPDATEDATE, new AnalysisDateDimension());
		fieldBuilder.addField(STATUSCODE, new AnalysisDimension());
		fieldBuilder.addField(REFERENCERANKNUMBER, new AnalysisDimension());
		fieldBuilder.addField(PARTYNAME1, new AnalysisDimension());
		fieldBuilder.addField(STGID, new AnalysisDimension());
		fieldBuilder.addField(NAME, new AnalysisDimension());
		fieldBuilder.addField(NAME1, new AnalysisDimension());
		fieldBuilder.addField(WINPROB, new AnalysisMeasure());
		fieldBuilder.addField(REVNAMOUNTCURCYCODE, new AnalysisDimension());
		fieldBuilder.addField(EFFECTIVEDATE, new AnalysisDateDimension());
		fieldBuilder.addField(TARGETPARTYNAME, new AnalysisDimension());
		fieldBuilder.addField(TARGETPARTYID, new AnalysisDimension());
		fieldBuilder.addField(PARTYNUMBER, new AnalysisDimension());
		fieldBuilder.addField(PARTYNAME, new AnalysisDimension());
		fieldBuilder.addField(PARTYTYPE, new AnalysisDimension());
		fieldBuilder.addField(EMAILADDRESS, new AnalysisDimension());
		fieldBuilder.addField(DUNSNUMBERC, new AnalysisDimension());
		fieldBuilder.addField(COMMENTS1, new AnalysisDimension());
		fieldBuilder.addField(PARTYUNIQUENAME, new AnalysisDimension());
		fieldBuilder.addField(PARTYUNIQUENAME1, new AnalysisDimension());
		fieldBuilder.addField(INTCONTACTPARTYID, new AnalysisDimension());
		fieldBuilder.addField(PARTYNAME2, new AnalysisDimension());
		fieldBuilder.addField(CUSTOMERCONTACTRELATIONID, new AnalysisDimension());
		fieldBuilder.addField(SALESACCOUNTID, new AnalysisDimension());
		fieldBuilder.addField(SALESACCOUNTUNIQUENAME, new AnalysisDimension());
	}

@Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
	try {
		DataSet dataSet = new DataSet();
		OracleDataSource oracleDataSource = (OracleDataSource) parentDefinition;
		List<com.easyinsight.datafeeds.oracle.client.OpportunityReference> list = oracleDataSource.getOpportunityReference();
		for (com.easyinsight.datafeeds.oracle.client.OpportunityReference o : list) {
		IRow row = dataSet.createRow();
		row.addValue(keys.get(OPTYREFERENCEID), String.valueOf(o.getOptyReferenceId()));
		row.addValue(keys.get(OPTYID), String.valueOf(o.getOptyId()));
		row.addValue(keys.get(REFERENCEPARTYID), String.valueOf(o.getReferencePartyId()));
		row.addValue(keys.get(COMMENTS), o.getComments().getValue());
		row.addValue(keys.get(CONFLICTID), String.valueOf(o.getConflictId()));
		row.addValue(keys.get(CREATEDBY), o.getCreatedBy());
		row.addValue(keys.get(CREATIONDATE), getDate(o.getCreationDate()));
		row.addValue(keys.get(LASTUPDATEDBY), o.getLastUpdatedBy());
		row.addValue(keys.get(LASTUPDATEDATE), getDate(o.getLastUpdateDate()));
		row.addValue(keys.get(LASTUPDATELOGIN), o.getLastUpdateLogin().getValue());
		row.addValue(keys.get(USERLASTUPDATEDATE), getDate(o.getUserLastUpdateDate()));
		row.addValue(keys.get(STATUSCODE), o.getStatusCode());
		row.addValue(keys.get(REFERENCERANKNUMBER), o.getReferenceRankNumber().getValue());
		row.addValue(keys.get(PARTYNAME1), o.getPartyName1());
		row.addValue(keys.get(STGID), String.valueOf(o.getStgId()));
		row.addValue(keys.get(NAME), o.getName());
		row.addValue(keys.get(NAME1), o.getName1());
		row.addValue(keys.get(WINPROB), getMeasureValue(o.getWinProb()));
		row.addValue(keys.get(REVNAMOUNTCURCYCODE), o.getRevnAmountCurcyCode());
		row.addValue(keys.get(EFFECTIVEDATE), getDate(o.getEffectiveDate()));
		row.addValue(keys.get(TARGETPARTYNAME), o.getTargetPartyName());
		row.addValue(keys.get(TARGETPARTYID), String.valueOf(o.getTargetPartyId()));
		row.addValue(keys.get(PARTYNUMBER), o.getPartyNumber());
		row.addValue(keys.get(PARTYNAME), o.getPartyName());
		row.addValue(keys.get(PARTYTYPE), o.getPartyType());
		row.addValue(keys.get(EMAILADDRESS), o.getEmailAddress().getValue());
		row.addValue(keys.get(DUNSNUMBERC), o.getDUNSNumberC().getValue());
		row.addValue(keys.get(COMMENTS1), o.getComments1().getValue());
		row.addValue(keys.get(PARTYUNIQUENAME), o.getPartyUniqueName().getValue());
		row.addValue(keys.get(PARTYUNIQUENAME1), o.getPartyUniqueName1().getValue());
		row.addValue(keys.get(INTCONTACTPARTYID), String.valueOf(o.getIntContactPartyId().getValue()));
		row.addValue(keys.get(PARTYNAME2), o.getPartyName2());
		row.addValue(keys.get(CUSTOMERCONTACTRELATIONID), String.valueOf(o.getCustomerContactRelationId().getValue()));
		row.addValue(keys.get(SALESACCOUNTID), String.valueOf(o.getSalesAccountId().getValue()));
		row.addValue(keys.get(SALESACCOUNTUNIQUENAME), o.getSalesAccountUniqueName().getValue());
		}

		return dataSet;
	} catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
}

	@Override
    public FeedType getFeedType() {
        return FeedType.ORACLE_OPPORTUNITYREFERENCE;
    }

}