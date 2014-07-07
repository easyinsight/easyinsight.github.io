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

public class OracleOpportunityResourceSource extends OracleBaseSource {
	public OracleOpportunityResourceSource() {
        setFeedName("OpportunityResource");
    }

	public static final String OPTYRESOURCEID = "OptyResourceId";
	public static final String OPTYID = "OptyId";
	public static final String RESOURCEID = "ResourceId";
	public static final String PARTYNAME = "PartyName";
	public static final String ACCESSLEVELCODE = "AccessLevelCode";
	public static final String DEALPROTECTEDDATE = "DealProtectedDate";
	public static final String DEALEXPIRATIONDATE = "DealExpirationDate";
	public static final String DEALPROTECTED = "DealProtected";
	public static final String ORGTREECODE = "OrgTreeCode";
	public static final String ORGTREESTRUCTURECODE = "OrgTreeStructureCode";
	public static final String RESOURCEORGID = "ResourceOrgId";
	public static final String MEMBERFUNCTIONCODE = "MemberFunctionCode";
	public static final String ASSIGNMENTTYPE = "AssignmentType";
	public static final String CONFLICTID = "ConflictId";
	public static final String CREATEDBY = "CreatedBy";
	public static final String CREATIONDATE = "CreationDate";
	public static final String LASTUPDATEDBY = "LastUpdatedBy";
	public static final String LASTUPDATEDATE = "LastUpdateDate";
	public static final String LASTUPDATELOGIN = "LastUpdateLogin";
	public static final String USERLASTUPDATEDATE = "UserLastUpdateDate";
	public static final String PERSONFIRSTNAME = "PersonFirstName";
	public static final String PERSONLASTNAME = "PersonLastName";
	public static final String ORGANIZATIONNAME = "OrganizationName";
	public static final String ORGANIZATIONID = "OrganizationId";
	public static final String ASGNTERRITORYVERSIONID = "AsgnTerritoryVersionId";
	public static final String TERRITORYNAME = "TerritoryName";
	public static final String EMAILADDRESS = "EmailAddress";
	public static final String FORMATTEDPHONENUMBER = "FormattedPhoneNumber";
	public static final String PARTNERORGID = "PartnerOrgId";
	public static final String PARTNERPARTYNAME = "PartnerPartyName";
	public static final String ROLENAME = "RoleName";
	public static final String MGRRESOURCEID = "MgrResourceId";
	public static final String OPTYRESOURCENUMBER = "OptyResourceNumber";

	protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {		fieldBuilder.addField(OPTYRESOURCEID, new AnalysisDimension());
		fieldBuilder.addField(OPTYID, new AnalysisDimension());
		fieldBuilder.addField(RESOURCEID, new AnalysisDimension());
		fieldBuilder.addField(PARTYNAME, new AnalysisDimension());
		fieldBuilder.addField(ACCESSLEVELCODE, new AnalysisDimension());
		fieldBuilder.addField(DEALPROTECTEDDATE, new AnalysisDateDimension(true));
		fieldBuilder.addField(DEALEXPIRATIONDATE, new AnalysisDateDimension(true));
		fieldBuilder.addField(DEALPROTECTED, new AnalysisDimension());
		fieldBuilder.addField(ORGTREECODE, new AnalysisDimension());
		fieldBuilder.addField(ORGTREESTRUCTURECODE, new AnalysisDimension());
		fieldBuilder.addField(RESOURCEORGID, new AnalysisDimension());
		fieldBuilder.addField(MEMBERFUNCTIONCODE, new AnalysisDimension());
		fieldBuilder.addField(ASSIGNMENTTYPE, new AnalysisDimension());
		fieldBuilder.addField(CONFLICTID, new AnalysisDimension());
		fieldBuilder.addField(CREATEDBY, new AnalysisDimension());
		fieldBuilder.addField(CREATIONDATE, new AnalysisDateDimension(true));
		fieldBuilder.addField(LASTUPDATEDBY, new AnalysisDimension());
		fieldBuilder.addField(LASTUPDATEDATE, new AnalysisDateDimension(true));
		fieldBuilder.addField(LASTUPDATELOGIN, new AnalysisDimension());
		fieldBuilder.addField(USERLASTUPDATEDATE, new AnalysisDateDimension(true));
		fieldBuilder.addField(PERSONFIRSTNAME, new AnalysisDimension());
		fieldBuilder.addField(PERSONLASTNAME, new AnalysisDimension());
		fieldBuilder.addField(ORGANIZATIONNAME, new AnalysisDimension());
		fieldBuilder.addField(ORGANIZATIONID, new AnalysisDimension());
		fieldBuilder.addField(ASGNTERRITORYVERSIONID, new AnalysisDimension());
		fieldBuilder.addField(TERRITORYNAME, new AnalysisDimension());
		fieldBuilder.addField(EMAILADDRESS, new AnalysisDimension());
		fieldBuilder.addField(FORMATTEDPHONENUMBER, new AnalysisDimension());
		fieldBuilder.addField(PARTNERORGID, new AnalysisDimension());
		fieldBuilder.addField(PARTNERPARTYNAME, new AnalysisDimension());
		fieldBuilder.addField(ROLENAME, new AnalysisDimension());
		fieldBuilder.addField(MGRRESOURCEID, new AnalysisDimension());
		fieldBuilder.addField(OPTYRESOURCENUMBER, new AnalysisDimension());
	}

@Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
	try {
		DataSet dataSet = new DataSet();
		OracleDataSource oracleDataSource = (OracleDataSource) parentDefinition;
		List<com.easyinsight.datafeeds.oracle.client.OpportunityResource> list = oracleDataSource.getOpportunityResource();
		for (com.easyinsight.datafeeds.oracle.client.OpportunityResource o : list) {
		IRow row = dataSet.createRow();
		row.addValue(keys.get(OPTYRESOURCEID), String.valueOf(o.getOptyResourceId()));
		row.addValue(keys.get(OPTYID), String.valueOf(o.getOptyId()));
		row.addValue(keys.get(RESOURCEID), String.valueOf(o.getResourceId()));
		row.addValue(keys.get(PARTYNAME), o.getPartyName());
		row.addValue(keys.get(ACCESSLEVELCODE), o.getAccessLevelCode().getValue());
		row.addValue(keys.get(DEALPROTECTEDDATE), getDate(o.getDealProtectedDate()));
		row.addValue(keys.get(DEALEXPIRATIONDATE), getDate(o.getDealExpirationDate()));
		row.addValue(keys.get(DEALPROTECTED), o.getDealProtected().getValue());
		row.addValue(keys.get(ORGTREECODE), o.getOrgTreeCode().getValue());
		row.addValue(keys.get(ORGTREESTRUCTURECODE), o.getOrgTreeStructureCode().getValue());
		row.addValue(keys.get(RESOURCEORGID), String.valueOf(o.getResourceOrgId().getValue()));
		row.addValue(keys.get(MEMBERFUNCTIONCODE), o.getMemberFunctionCode().getValue());
		row.addValue(keys.get(ASSIGNMENTTYPE), o.getAssignmentType().getValue());
		row.addValue(keys.get(CONFLICTID), String.valueOf(o.getConflictId()));
		row.addValue(keys.get(CREATEDBY), o.getCreatedBy());
		row.addValue(keys.get(CREATIONDATE), getDate(o.getCreationDate()));
		row.addValue(keys.get(LASTUPDATEDBY), o.getLastUpdatedBy());
		row.addValue(keys.get(LASTUPDATEDATE), getDate(o.getLastUpdateDate()));
		row.addValue(keys.get(LASTUPDATELOGIN), o.getLastUpdateLogin().getValue());
		row.addValue(keys.get(USERLASTUPDATEDATE), getDate(o.getUserLastUpdateDate()));
		row.addValue(keys.get(PERSONFIRSTNAME), o.getPersonFirstName().getValue());
		row.addValue(keys.get(PERSONLASTNAME), o.getPersonLastName().getValue());
		row.addValue(keys.get(ORGANIZATIONNAME), o.getOrganizationName());
		row.addValue(keys.get(ORGANIZATIONID), String.valueOf(o.getOrganizationId()));
		row.addValue(keys.get(ASGNTERRITORYVERSIONID), String.valueOf(o.getAsgnTerritoryVersionId().getValue()));
		row.addValue(keys.get(TERRITORYNAME), o.getTerritoryName());
		row.addValue(keys.get(EMAILADDRESS), o.getEmailAddress().getValue());
		row.addValue(keys.get(FORMATTEDPHONENUMBER), o.getFormattedPhoneNumber().getValue());
		row.addValue(keys.get(PARTNERORGID), String.valueOf(o.getPartnerOrgId().getValue()));
		row.addValue(keys.get(PARTNERPARTYNAME), o.getPartnerPartyName());
		row.addValue(keys.get(ROLENAME), o.getRoleName().getValue());
		row.addValue(keys.get(MGRRESOURCEID), String.valueOf(o.getMgrResourceId().getValue()));
		row.addValue(keys.get(OPTYRESOURCENUMBER), o.getOptyResourceNumber());
		}

		return dataSet;
	} catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
}

	@Override
    public FeedType getFeedType() {
        return FeedType.ORACLE_OPPORTUNITYRESOURCE;
    }

}