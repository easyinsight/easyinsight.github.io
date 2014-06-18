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

public class OracleOpportunityContactSource extends OracleBaseSource {
	public OracleOpportunityContactSource() {
        setFeedName("OpportunityContact");
    }

	public static final String AFFINITYLVLCD = "AffinityLvlCd";
	public static final String COMMENTS = "Comments";
	public static final String CONFLICTID = "ConflictId";
	public static final String CONTACTEDFLG = "ContactedFlg";
	public static final String CREATEDBY = "CreatedBy";
	public static final String CREATIONDATE = "CreationDate";
	public static final String INFLUENCELVLCD = "InfluenceLvlCd";
	public static final String LASTUPDATEDATE = "LastUpdateDate";
	public static final String LASTUPDATEDBY = "LastUpdatedBy";
	public static final String LASTUPDATELOGIN = "LastUpdateLogin";
	public static final String OPTYCONID = "OptyConId";
	public static final String OPTYID = "OptyId";
	public static final String PERPARTYID = "PERPartyId";
	public static final String RELATIONSHIPID = "RelationshipId";
	public static final String ROLECD = "RoleCd";
	public static final String USERLASTUPDATEDATE = "UserLastUpdateDate";
	public static final String PRIMARYFLG = "PrimaryFlg";
	public static final String RELATIONSHIPCODE = "RelationshipCode";
	public static final String JOBTITLE = "JobTitle";
	public static final String ORGCONTACTID = "OrgContactId";
	public static final String ORGANIZATIONPARTYNAME = "OrganizationPartyName";
	public static final String ORGANIZATIONPARTYID = "OrganizationPartyId";
	public static final String RELATIONSHIPRECID = "RelationshipRecId";
	public static final String SALESAFFINITYCODE = "SalesAffinityCode";
	public static final String SALESBUYINGROLECODE = "SalesBuyingRoleCode";
	public static final String SALESINFLUENCELEVELCODE = "SalesInfluenceLevelCode";
	public static final String PARTYUNIQUENAME = "PartyUniqueName";
	public static final String PARTYNAME = "PartyName";
	public static final String PARTYID = "PartyId";
	public static final String PREFERREDCONTACTMETHOD = "PreferredContactMethod";
	public static final String EMAILADDRESS = "EmailAddress";
	public static final String CONTACTPOINTID = "ContactPointId";
	public static final String CONTACTPOINTTYPE = "ContactPointType";
	public static final String RAWPHONENUMBER = "RawPhoneNumber";
	public static final String FORMATTEDPHONENUMBER = "FormattedPhoneNumber";
	public static final String CONTACTPOINTTYPE1 = "ContactPointType1";
	public static final String FORMATTEDADDRESS = "FormattedAddress";
	public static final String LOCATIONID = "LocationId";
	public static final String FORMATTEDMULTILINEADDRESS = "FormattedMultilineAddress";
	public static final String TARGETPARTYID = "TargetPartyId";
	public static final String SUBJECTID = "SubjectId";
	public static final String OBJECTID = "ObjectId";
	public static final String PERSONCENTRICJOBTITLE = "PersonCentricJobTitle";

	protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {		fieldBuilder.addField(AFFINITYLVLCD, new AnalysisDimension());
		fieldBuilder.addField(COMMENTS, new AnalysisDimension());
		fieldBuilder.addField(CONFLICTID, new AnalysisDimension());
		fieldBuilder.addField(CONTACTEDFLG, new AnalysisDimension());
		fieldBuilder.addField(CREATEDBY, new AnalysisDimension());
		fieldBuilder.addField(CREATIONDATE, new AnalysisDateDimension());
		fieldBuilder.addField(INFLUENCELVLCD, new AnalysisDimension());
		fieldBuilder.addField(LASTUPDATEDATE, new AnalysisDateDimension());
		fieldBuilder.addField(LASTUPDATEDBY, new AnalysisDimension());
		fieldBuilder.addField(LASTUPDATELOGIN, new AnalysisDimension());
		fieldBuilder.addField(OPTYCONID, new AnalysisDimension());
		fieldBuilder.addField(OPTYID, new AnalysisDimension());
		fieldBuilder.addField(PERPARTYID, new AnalysisDimension());
		fieldBuilder.addField(RELATIONSHIPID, new AnalysisDimension());
		fieldBuilder.addField(ROLECD, new AnalysisDimension());
		fieldBuilder.addField(USERLASTUPDATEDATE, new AnalysisDateDimension());
		fieldBuilder.addField(PRIMARYFLG, new AnalysisDimension());
		fieldBuilder.addField(RELATIONSHIPCODE, new AnalysisDimension());
		fieldBuilder.addField(JOBTITLE, new AnalysisDimension());
		fieldBuilder.addField(ORGCONTACTID, new AnalysisDimension());
		fieldBuilder.addField(ORGANIZATIONPARTYNAME, new AnalysisDimension());
		fieldBuilder.addField(ORGANIZATIONPARTYID, new AnalysisDimension());
		fieldBuilder.addField(RELATIONSHIPRECID, new AnalysisDimension());
		fieldBuilder.addField(SALESAFFINITYCODE, new AnalysisDimension());
		fieldBuilder.addField(SALESBUYINGROLECODE, new AnalysisDimension());
		fieldBuilder.addField(SALESINFLUENCELEVELCODE, new AnalysisDimension());
		fieldBuilder.addField(PARTYUNIQUENAME, new AnalysisDimension());
		fieldBuilder.addField(PARTYNAME, new AnalysisDimension());
		fieldBuilder.addField(PARTYID, new AnalysisDimension());
		fieldBuilder.addField(PREFERREDCONTACTMETHOD, new AnalysisDimension());
		fieldBuilder.addField(EMAILADDRESS, new AnalysisDimension());
		fieldBuilder.addField(CONTACTPOINTID, new AnalysisDimension());
		fieldBuilder.addField(CONTACTPOINTTYPE, new AnalysisDimension());
		fieldBuilder.addField(RAWPHONENUMBER, new AnalysisDimension());
		fieldBuilder.addField(FORMATTEDPHONENUMBER, new AnalysisDimension());
		fieldBuilder.addField(CONTACTPOINTTYPE1, new AnalysisDimension());
		fieldBuilder.addField(FORMATTEDADDRESS, new AnalysisDimension());
		fieldBuilder.addField(LOCATIONID, new AnalysisDimension());
		fieldBuilder.addField(FORMATTEDMULTILINEADDRESS, new AnalysisDimension());
		fieldBuilder.addField(TARGETPARTYID, new AnalysisDimension());
		fieldBuilder.addField(SUBJECTID, new AnalysisDimension());
		fieldBuilder.addField(OBJECTID, new AnalysisDimension());
		fieldBuilder.addField(PERSONCENTRICJOBTITLE, new AnalysisDimension());
	}

@Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
	try {
		DataSet dataSet = new DataSet();
		OracleDataSource oracleDataSource = (OracleDataSource) parentDefinition;
		List<com.easyinsight.datafeeds.oracle.client.OpportunityContact> list = oracleDataSource.getOpportunityContact();
		for (com.easyinsight.datafeeds.oracle.client.OpportunityContact o : list) {
		IRow row = dataSet.createRow();
		row.addValue(keys.get(AFFINITYLVLCD), o.getAffinityLvlCd().getValue());
		row.addValue(keys.get(COMMENTS), o.getComments().getValue());
		row.addValue(keys.get(CONFLICTID), String.valueOf(o.getConflictId()));
		row.addValue(keys.get(CONTACTEDFLG), o.getContactedFlg().getValue());
		row.addValue(keys.get(CREATEDBY), o.getCreatedBy());
		row.addValue(keys.get(CREATIONDATE), getDate(o.getCreationDate()));
		row.addValue(keys.get(INFLUENCELVLCD), o.getInfluenceLvlCd().getValue());
		row.addValue(keys.get(LASTUPDATEDATE), getDate(o.getLastUpdateDate()));
		row.addValue(keys.get(LASTUPDATEDBY), o.getLastUpdatedBy());
		row.addValue(keys.get(LASTUPDATELOGIN), o.getLastUpdateLogin().getValue());
		row.addValue(keys.get(OPTYCONID), String.valueOf(o.getOptyConId()));
		row.addValue(keys.get(OPTYID), String.valueOf(o.getOptyId()));
		row.addValue(keys.get(PERPARTYID), String.valueOf(o.getPERPartyId()));
		row.addValue(keys.get(RELATIONSHIPID), String.valueOf(o.getRelationshipId().getValue()));
		row.addValue(keys.get(ROLECD), o.getRoleCd().getValue());
		row.addValue(keys.get(USERLASTUPDATEDATE), getDate(o.getUserLastUpdateDate()));
		row.addValue(keys.get(PRIMARYFLG), o.getPrimaryFlg().getValue());
		row.addValue(keys.get(RELATIONSHIPCODE), o.getRelationshipCode());
		row.addValue(keys.get(JOBTITLE), o.getJobTitle().getValue());
		row.addValue(keys.get(ORGCONTACTID), String.valueOf(o.getOrgContactId()));
		row.addValue(keys.get(ORGANIZATIONPARTYNAME), o.getOrganizationPartyName());
		row.addValue(keys.get(ORGANIZATIONPARTYID), String.valueOf(o.getOrganizationPartyId()));
		row.addValue(keys.get(RELATIONSHIPRECID), String.valueOf(o.getRelationshipRecId()));
		row.addValue(keys.get(SALESAFFINITYCODE), o.getSalesAffinityCode().getValue());
		row.addValue(keys.get(SALESBUYINGROLECODE), o.getSalesBuyingRoleCode().getValue());
		row.addValue(keys.get(SALESINFLUENCELEVELCODE), o.getSalesInfluenceLevelCode().getValue());
		row.addValue(keys.get(PARTYUNIQUENAME), o.getPartyUniqueName().getValue());
		row.addValue(keys.get(PARTYNAME), o.getPartyName());
		row.addValue(keys.get(PARTYID), String.valueOf(o.getPartyId()));
		row.addValue(keys.get(PREFERREDCONTACTMETHOD), o.getPreferredContactMethod().getValue());
		row.addValue(keys.get(EMAILADDRESS), o.getEmailAddress().getValue());
		row.addValue(keys.get(CONTACTPOINTID), String.valueOf(o.getContactPointId()));
		row.addValue(keys.get(CONTACTPOINTTYPE), o.getContactPointType());
		row.addValue(keys.get(RAWPHONENUMBER), o.getRawPhoneNumber().getValue());
		row.addValue(keys.get(FORMATTEDPHONENUMBER), o.getFormattedPhoneNumber().getValue());
		row.addValue(keys.get(CONTACTPOINTTYPE1), o.getContactPointType1());
		row.addValue(keys.get(FORMATTEDADDRESS), o.getFormattedAddress().getValue());
		row.addValue(keys.get(LOCATIONID), String.valueOf(o.getLocationId()));
		row.addValue(keys.get(FORMATTEDMULTILINEADDRESS), o.getFormattedMultilineAddress().getValue());
		row.addValue(keys.get(TARGETPARTYID), String.valueOf(o.getTargetPartyId().getValue()));
		row.addValue(keys.get(SUBJECTID), String.valueOf(o.getSubjectId()));
		row.addValue(keys.get(OBJECTID), String.valueOf(o.getObjectId()));
		row.addValue(keys.get(PERSONCENTRICJOBTITLE), o.getPersonCentricJobTitle().getValue());
		}

		return dataSet;
	} catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
}

	@Override
    public FeedType getFeedType() {
        return FeedType.ORACLE_OPPORTUNITYCONTACT;
    }

}