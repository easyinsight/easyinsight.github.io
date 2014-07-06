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

public class OracleOpportunityLeadSource extends OracleBaseSource {
	public OracleOpportunityLeadSource() {
        setFeedName("OpportunityLead");
    }

	public static final String OPTYLEADID = "OptyLeadId";
	public static final String OPTYID = "OptyId";
	public static final String LEADNUMBER = "LeadNumber";
	public static final String CONFLICTID = "ConflictId";
	public static final String CREATIONDATE = "CreationDate";
	public static final String CREATEDBY = "CreatedBy";
	public static final String LASTUPDATEDATE = "LastUpdateDate";
	public static final String LASTUPDATEDBY = "LastUpdatedBy";
	public static final String LASTUPDATELOGIN = "LastUpdateLogin";
	public static final String USERLASTUPDATEDATE = "UserLastUpdateDate";
	public static final String REGISTRATIONNUMBER = "RegistrationNumber";
	public static final String DEALESTIMATEDCLOSEDATE = "DealEstimatedCloseDate";
	public static final String DEALTYPE = "DealType";
	public static final String PRDEALPARTORGPARTYID = "PrDealPartOrgPartyId";
	public static final String PRDEALPARTRESOURCEPARTYID = "PrDealPartResourcePartyId";
	public static final String DEALPARTPROGRAMID = "DealPartProgramId";
	public static final String PARTNERTYPECD = "PartnerTypeCd";
	public static final String DEALEXPIRATIONDATE = "DealExpirationDate";

	protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {		fieldBuilder.addField(OPTYLEADID, new AnalysisDimension());
		fieldBuilder.addField(OPTYID, new AnalysisDimension());
		fieldBuilder.addField(LEADNUMBER, new AnalysisDimension());
		fieldBuilder.addField(CONFLICTID, new AnalysisDimension());
		fieldBuilder.addField(CREATIONDATE, new AnalysisDateDimension(true));
		fieldBuilder.addField(CREATEDBY, new AnalysisDimension());
		fieldBuilder.addField(LASTUPDATEDATE, new AnalysisDateDimension(true));
		fieldBuilder.addField(LASTUPDATEDBY, new AnalysisDimension());
		fieldBuilder.addField(LASTUPDATELOGIN, new AnalysisDimension());
		fieldBuilder.addField(USERLASTUPDATEDATE, new AnalysisDateDimension(true));
		fieldBuilder.addField(REGISTRATIONNUMBER, new AnalysisDimension());
		fieldBuilder.addField(DEALESTIMATEDCLOSEDATE, new AnalysisDateDimension(true));
		fieldBuilder.addField(DEALTYPE, new AnalysisDimension());
		fieldBuilder.addField(PRDEALPARTORGPARTYID, new AnalysisDimension());
		fieldBuilder.addField(PRDEALPARTRESOURCEPARTYID, new AnalysisDimension());
		fieldBuilder.addField(DEALPARTPROGRAMID, new AnalysisDimension());
		fieldBuilder.addField(PARTNERTYPECD, new AnalysisDimension());
		fieldBuilder.addField(DEALEXPIRATIONDATE, new AnalysisDateDimension(true));
	}

@Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
	try {
		DataSet dataSet = new DataSet();
		OracleDataSource oracleDataSource = (OracleDataSource) parentDefinition;
		List<com.easyinsight.datafeeds.oracle.client.OpportunityLead> list = oracleDataSource.getOpportunityLead();
		for (com.easyinsight.datafeeds.oracle.client.OpportunityLead o : list) {
		IRow row = dataSet.createRow();
		row.addValue(keys.get(OPTYLEADID), String.valueOf(o.getOptyLeadId()));
		row.addValue(keys.get(OPTYID), String.valueOf(o.getOptyId()));
		row.addValue(keys.get(LEADNUMBER), o.getLeadNumber());
		row.addValue(keys.get(CONFLICTID), String.valueOf(o.getConflictId()));
		row.addValue(keys.get(CREATIONDATE), getDate(o.getCreationDate()));
		row.addValue(keys.get(CREATEDBY), o.getCreatedBy());
		row.addValue(keys.get(LASTUPDATEDATE), getDate(o.getLastUpdateDate()));
		row.addValue(keys.get(LASTUPDATEDBY), o.getLastUpdatedBy());
		row.addValue(keys.get(LASTUPDATELOGIN), o.getLastUpdateLogin().getValue());
		row.addValue(keys.get(USERLASTUPDATEDATE), getDate(o.getUserLastUpdateDate()));
		row.addValue(keys.get(REGISTRATIONNUMBER), o.getRegistrationNumber().getValue());
		row.addValue(keys.get(DEALESTIMATEDCLOSEDATE), getDate(o.getDealEstimatedCloseDate()));
		row.addValue(keys.get(DEALTYPE), o.getDealType().getValue());
		row.addValue(keys.get(PRDEALPARTORGPARTYID), String.valueOf(o.getPrDealPartOrgPartyId().getValue()));
		row.addValue(keys.get(PRDEALPARTRESOURCEPARTYID), String.valueOf(o.getPrDealPartResourcePartyId().getValue()));
		row.addValue(keys.get(DEALPARTPROGRAMID), String.valueOf(o.getDealPartProgramId().getValue()));
		row.addValue(keys.get(PARTNERTYPECD), o.getPartnerTypeCd().getValue());
		row.addValue(keys.get(DEALEXPIRATIONDATE), getDate(o.getDealExpirationDate()));
		}

		return dataSet;
	} catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
}

	@Override
    public FeedType getFeedType() {
        return FeedType.ORACLE_OPPORTUNITYLEAD;
    }

}