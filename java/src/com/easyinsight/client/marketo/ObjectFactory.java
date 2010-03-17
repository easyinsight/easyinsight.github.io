
package com.easyinsight.client.marketo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.easyinsight.client.marketo package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ParamsListOperation_QNAME = new QName("http://www.marketo.com/mktows/", "paramsListOperation");
    private final static QName _SuccessGetCampaignsForSource_QNAME = new QName("http://www.marketo.com/mktows/", "successGetCampaignsForSource");
    private final static QName _SuccessRequestCampaign_QNAME = new QName("http://www.marketo.com/mktows/", "successRequestCampaign");
    private final static QName _ParamsSyncLead_QNAME = new QName("http://www.marketo.com/mktows/", "paramsSyncLead");
    private final static QName _ParamsGetMultipleLeads_QNAME = new QName("http://www.marketo.com/mktows/", "paramsGetMultipleLeads");
    private final static QName _ParamsGetLeadActivity_QNAME = new QName("http://www.marketo.com/mktows/", "paramsGetLeadActivity");
    private final static QName _SuccessGetLead_QNAME = new QName("http://www.marketo.com/mktows/", "successGetLead");
    private final static QName _ParamsGetLeadChanges_QNAME = new QName("http://www.marketo.com/mktows/", "paramsGetLeadChanges");
    private final static QName _SuccessListOperation_QNAME = new QName("http://www.marketo.com/mktows/", "successListOperation");
    private final static QName _SuccessSyncMultipleLeads_QNAME = new QName("http://www.marketo.com/mktows/", "successSyncMultipleLeads");
    private final static QName _AuthenticationHeader_QNAME = new QName("http://www.marketo.com/mktows/", "AuthenticationHeader");
    private final static QName _SuccessSyncLead_QNAME = new QName("http://www.marketo.com/mktows/", "successSyncLead");
    private final static QName _SuccessGetMultipleLeads_QNAME = new QName("http://www.marketo.com/mktows/", "successGetMultipleLeads");
    private final static QName _SuccessGetLeadChanges_QNAME = new QName("http://www.marketo.com/mktows/", "successGetLeadChanges");
    private final static QName _ParamsGetCampaignsForSource_QNAME = new QName("http://www.marketo.com/mktows/", "paramsGetCampaignsForSource");
    private final static QName _ParamsGetLead_QNAME = new QName("http://www.marketo.com/mktows/", "paramsGetLead");
    private final static QName _SuccessGetLeadActivity_QNAME = new QName("http://www.marketo.com/mktows/", "successGetLeadActivity");
    private final static QName _ParamsRequestCampaign_QNAME = new QName("http://www.marketo.com/mktows/", "paramsRequestCampaign");
    private final static QName _ParamsSyncMultipleLeads_QNAME = new QName("http://www.marketo.com/mktows/", "paramsSyncMultipleLeads");
    private final static QName _AttributeAttrType_QNAME = new QName("", "attrType");
    private final static QName _ResultListOperationStatusList_QNAME = new QName("", "statusList");
    private final static QName _LeadActivityListActivityRecordList_QNAME = new QName("", "activityRecordList");
    private final static QName _ResultSyncLeadLeadRecord_QNAME = new QName("", "leadRecord");
    private final static QName _SyncStatusError_QNAME = new QName("", "error");
    private final static QName _ResultGetMultipleLeadsLeadRecordList_QNAME = new QName("", "leadRecordList");
    private final static QName _ResultGetCampaignsForSourceCampaignRecordList_QNAME = new QName("", "campaignRecordList");
    private final static QName _LeadChangeRecordActivityAttributes_QNAME = new QName("", "activityAttributes");
    private final static QName _LeadChangeRecordMktgAssetName_QNAME = new QName("", "mktgAssetName");
    private final static QName _ParamsSyncMultipleLeadsDedupEnabled_QNAME = new QName("", "dedupEnabled");
    private final static QName _ParamsSyncLeadMarketoCookie_QNAME = new QName("", "marketoCookie");
    private final static QName _ResultGetLeadChangesLeadChangeRecordList_QNAME = new QName("", "leadChangeRecordList");
    private final static QName _ParamsGetLeadActivityBatchSize_QNAME = new QName("", "batchSize");
    private final static QName _ParamsGetLeadActivityActivityFilter_QNAME = new QName("", "activityFilter");
    private final static QName _ParamsGetLeadActivityStartPosition_QNAME = new QName("", "startPosition");
    private final static QName _ParamsGetCampaignsForSourceName_QNAME = new QName("", "name");
    private final static QName _ParamsGetCampaignsForSourceExactName_QNAME = new QName("", "exactName");
    private final static QName _StreamPositionOldestCreatedAt_QNAME = new QName("", "oldestCreatedAt");
    private final static QName _StreamPositionActivityCreatedAt_QNAME = new QName("", "activityCreatedAt");
    private final static QName _StreamPositionLatestCreatedAt_QNAME = new QName("", "latestCreatedAt");
    private final static QName _StreamPositionOffset_QNAME = new QName("", "offset");
    private final static QName _ParamsListOperationStrict_QNAME = new QName("", "strict");
    private final static QName _ActivityRecordForeignSysId_QNAME = new QName("", "foreignSysId");
    private final static QName _ActivityRecordForeignSysOrgId_QNAME = new QName("", "foreignSysOrgId");
    private final static QName _ActivityRecordOrgName_QNAME = new QName("", "orgName");
    private final static QName _ActivityRecordCampaign_QNAME = new QName("", "campaign");
    private final static QName _ActivityRecordPersonName_QNAME = new QName("", "personName");
    private final static QName _VersionedItemDescription_QNAME = new QName("", "description");
    private final static QName _VersionedItemType_QNAME = new QName("", "type");
    private final static QName _ParamsGetMultipleLeadsStreamPosition_QNAME = new QName("", "streamPosition");
    private final static QName _LeadRecordForeignSysType_QNAME = new QName("", "ForeignSysType");
    private final static QName _LeadRecordLeadAttributeList_QNAME = new QName("", "leadAttributeList");
    private final static QName _LeadRecordForeignSysPersonId_QNAME = new QName("", "ForeignSysPersonId");
    private final static QName _LeadRecordEmail_QNAME = new QName("", "Email");
    private final static QName _LeadRecordId_QNAME = new QName("", "Id");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.easyinsight.client.marketo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ParamsGetLead }
     * 
     */
    public ParamsGetLead createParamsGetLead() {
        return new ParamsGetLead();
    }

    /**
     * Create an instance of {@link ResultListOperation }
     * 
     */
    public ResultListOperation createResultListOperation() {
        return new ResultListOperation();
    }

    /**
     * Create an instance of {@link ResultSyncLead }
     * 
     */
    public ResultSyncLead createResultSyncLead() {
        return new ResultSyncLead();
    }

    /**
     * Create an instance of {@link ResultGetCampaignsForSource }
     * 
     */
    public ResultGetCampaignsForSource createResultGetCampaignsForSource() {
        return new ResultGetCampaignsForSource();
    }

    /**
     * Create an instance of {@link ResultGetMultipleLeads }
     * 
     */
    public ResultGetMultipleLeads createResultGetMultipleLeads() {
        return new ResultGetMultipleLeads();
    }

    /**
     * Create an instance of {@link LeadStatus }
     * 
     */
    public LeadStatus createLeadStatus() {
        return new LeadStatus();
    }

    /**
     * Create an instance of {@link ParamsSyncMultipleLeads }
     * 
     */
    public ParamsSyncMultipleLeads createParamsSyncMultipleLeads() {
        return new ParamsSyncMultipleLeads();
    }

    /**
     * Create an instance of {@link ArrayOfAttribute }
     * 
     */
    public ArrayOfAttribute createArrayOfAttribute() {
        return new ArrayOfAttribute();
    }

    /**
     * Create an instance of {@link ArrayOfSyncStatus }
     * 
     */
    public ArrayOfSyncStatus createArrayOfSyncStatus() {
        return new ArrayOfSyncStatus();
    }

    /**
     * Create an instance of {@link ResultGetLeadChanges }
     * 
     */
    public ResultGetLeadChanges createResultGetLeadChanges() {
        return new ResultGetLeadChanges();
    }

    /**
     * Create an instance of {@link ParamsGetCampaignsForSource }
     * 
     */
    public ParamsGetCampaignsForSource createParamsGetCampaignsForSource() {
        return new ParamsGetCampaignsForSource();
    }

    /**
     * Create an instance of {@link StreamPosition }
     * 
     */
    public StreamPosition createStreamPosition() {
        return new StreamPosition();
    }

    /**
     * Create an instance of {@link ArrayOfLeadChangeRecord }
     * 
     */
    public ArrayOfLeadChangeRecord createArrayOfLeadChangeRecord() {
        return new ArrayOfLeadChangeRecord();
    }

    /**
     * Create an instance of {@link SuccessSyncMultipleLeads }
     * 
     */
    public SuccessSyncMultipleLeads createSuccessSyncMultipleLeads() {
        return new SuccessSyncMultipleLeads();
    }

    /**
     * Create an instance of {@link ArrayOfActivityRecord }
     * 
     */
    public ArrayOfActivityRecord createArrayOfActivityRecord() {
        return new ArrayOfActivityRecord();
    }

    /**
     * Create an instance of {@link ParamsGetMultipleLeads }
     * 
     */
    public ParamsGetMultipleLeads createParamsGetMultipleLeads() {
        return new ParamsGetMultipleLeads();
    }

    /**
     * Create an instance of {@link SuccessGetLead }
     * 
     */
    public SuccessGetLead createSuccessGetLead() {
        return new SuccessGetLead();
    }

    /**
     * Create an instance of {@link LeadRecord }
     * 
     */
    public LeadRecord createLeadRecord() {
        return new LeadRecord();
    }

    /**
     * Create an instance of {@link LeadKey }
     * 
     */
    public LeadKey createLeadKey() {
        return new LeadKey();
    }

    /**
     * Create an instance of {@link ActivityTypeFilter }
     * 
     */
    public ActivityTypeFilter createActivityTypeFilter() {
        return new ActivityTypeFilter();
    }

    /**
     * Create an instance of {@link SuccessGetLeadActivity }
     * 
     */
    public SuccessGetLeadActivity createSuccessGetLeadActivity() {
        return new SuccessGetLeadActivity();
    }

    /**
     * Create an instance of {@link ParamsRequestCampaign }
     * 
     */
    public ParamsRequestCampaign createParamsRequestCampaign() {
        return new ParamsRequestCampaign();
    }

    /**
     * Create an instance of {@link ResultRequestCampaign }
     * 
     */
    public ResultRequestCampaign createResultRequestCampaign() {
        return new ResultRequestCampaign();
    }

    /**
     * Create an instance of {@link CampaignRecord }
     * 
     */
    public CampaignRecord createCampaignRecord() {
        return new CampaignRecord();
    }

    /**
     * Create an instance of {@link ArrayOfBase64Binary }
     * 
     */
    public ArrayOfBase64Binary createArrayOfBase64Binary() {
        return new ArrayOfBase64Binary();
    }

    /**
     * Create an instance of {@link ArrayOfVersionedItem }
     * 
     */
    public ArrayOfVersionedItem createArrayOfVersionedItem() {
        return new ArrayOfVersionedItem();
    }

    /**
     * Create an instance of {@link ResultSyncMultipleLeads }
     * 
     */
    public ResultSyncMultipleLeads createResultSyncMultipleLeads() {
        return new ResultSyncMultipleLeads();
    }

    /**
     * Create an instance of {@link SuccessRequestCampaign }
     * 
     */
    public SuccessRequestCampaign createSuccessRequestCampaign() {
        return new SuccessRequestCampaign();
    }

    /**
     * Create an instance of {@link ParamsGetLeadChanges }
     * 
     */
    public ParamsGetLeadChanges createParamsGetLeadChanges() {
        return new ParamsGetLeadChanges();
    }

    /**
     * Create an instance of {@link Attribute }
     * 
     */
    public Attribute createAttribute() {
        return new Attribute();
    }

    /**
     * Create an instance of {@link LeadActivityList }
     * 
     */
    public LeadActivityList createLeadActivityList() {
        return new LeadActivityList();
    }

    /**
     * Create an instance of {@link ParamsSyncLead }
     * 
     */
    public ParamsSyncLead createParamsSyncLead() {
        return new ParamsSyncLead();
    }

    /**
     * Create an instance of {@link ParamsGetLeadActivity }
     * 
     */
    public ParamsGetLeadActivity createParamsGetLeadActivity() {
        return new ParamsGetLeadActivity();
    }

    /**
     * Create an instance of {@link SuccessSyncLead }
     * 
     */
    public SuccessSyncLead createSuccessSyncLead() {
        return new SuccessSyncLead();
    }

    /**
     * Create an instance of {@link ArrayOfLeadKey }
     * 
     */
    public ArrayOfLeadKey createArrayOfLeadKey() {
        return new ArrayOfLeadKey();
    }

    /**
     * Create an instance of {@link ArrayOfLeadRecord }
     * 
     */
    public ArrayOfLeadRecord createArrayOfLeadRecord() {
        return new ArrayOfLeadRecord();
    }

    /**
     * Create an instance of {@link ActivityRecord }
     * 
     */
    public ActivityRecord createActivityRecord() {
        return new ActivityRecord();
    }

    /**
     * Create an instance of {@link VersionedItem }
     * 
     */
    public VersionedItem createVersionedItem() {
        return new VersionedItem();
    }

    /**
     * Create an instance of {@link ArrayOfActivityType }
     * 
     */
    public ArrayOfActivityType createArrayOfActivityType() {
        return new ArrayOfActivityType();
    }

    /**
     * Create an instance of {@link ArrayOfCampaignRecord }
     * 
     */
    public ArrayOfCampaignRecord createArrayOfCampaignRecord() {
        return new ArrayOfCampaignRecord();
    }

    /**
     * Create an instance of {@link ListKey }
     * 
     */
    public ListKey createListKey() {
        return new ListKey();
    }

    /**
     * Create an instance of {@link SyncStatus }
     * 
     */
    public SyncStatus createSyncStatus() {
        return new SyncStatus();
    }

    /**
     * Create an instance of {@link SuccessGetCampaignsForSource }
     * 
     */
    public SuccessGetCampaignsForSource createSuccessGetCampaignsForSource() {
        return new SuccessGetCampaignsForSource();
    }

    /**
     * Create an instance of {@link LeadChangeRecord }
     * 
     */
    public LeadChangeRecord createLeadChangeRecord() {
        return new LeadChangeRecord();
    }

    /**
     * Create an instance of {@link ResultGetLead }
     * 
     */
    public ResultGetLead createResultGetLead() {
        return new ResultGetLead();
    }

    /**
     * Create an instance of {@link ArrayOfLeadStatus }
     * 
     */
    public ArrayOfLeadStatus createArrayOfLeadStatus() {
        return new ArrayOfLeadStatus();
    }

    /**
     * Create an instance of {@link AuthenticationHeaderInfo }
     * 
     */
    public AuthenticationHeaderInfo createAuthenticationHeaderInfo() {
        return new AuthenticationHeaderInfo();
    }

    /**
     * Create an instance of {@link SuccessGetLeadChanges }
     * 
     */
    public SuccessGetLeadChanges createSuccessGetLeadChanges() {
        return new SuccessGetLeadChanges();
    }

    /**
     * Create an instance of {@link ParamsListOperation }
     * 
     */
    public ParamsListOperation createParamsListOperation() {
        return new ParamsListOperation();
    }

    /**
     * Create an instance of {@link SuccessGetMultipleLeads }
     * 
     */
    public SuccessGetMultipleLeads createSuccessGetMultipleLeads() {
        return new SuccessGetMultipleLeads();
    }

    /**
     * Create an instance of {@link SuccessListOperation }
     * 
     */
    public SuccessListOperation createSuccessListOperation() {
        return new SuccessListOperation();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsListOperation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsListOperation")
    public JAXBElement<ParamsListOperation> createParamsListOperation(ParamsListOperation value) {
        return new JAXBElement<ParamsListOperation>(_ParamsListOperation_QNAME, ParamsListOperation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessGetCampaignsForSource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successGetCampaignsForSource")
    public JAXBElement<SuccessGetCampaignsForSource> createSuccessGetCampaignsForSource(SuccessGetCampaignsForSource value) {
        return new JAXBElement<SuccessGetCampaignsForSource>(_SuccessGetCampaignsForSource_QNAME, SuccessGetCampaignsForSource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessRequestCampaign }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successRequestCampaign")
    public JAXBElement<SuccessRequestCampaign> createSuccessRequestCampaign(SuccessRequestCampaign value) {
        return new JAXBElement<SuccessRequestCampaign>(_SuccessRequestCampaign_QNAME, SuccessRequestCampaign.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsSyncLead }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsSyncLead")
    public JAXBElement<ParamsSyncLead> createParamsSyncLead(ParamsSyncLead value) {
        return new JAXBElement<ParamsSyncLead>(_ParamsSyncLead_QNAME, ParamsSyncLead.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsGetMultipleLeads }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsGetMultipleLeads")
    public JAXBElement<ParamsGetMultipleLeads> createParamsGetMultipleLeads(ParamsGetMultipleLeads value) {
        return new JAXBElement<ParamsGetMultipleLeads>(_ParamsGetMultipleLeads_QNAME, ParamsGetMultipleLeads.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsGetLeadActivity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsGetLeadActivity")
    public JAXBElement<ParamsGetLeadActivity> createParamsGetLeadActivity(ParamsGetLeadActivity value) {
        return new JAXBElement<ParamsGetLeadActivity>(_ParamsGetLeadActivity_QNAME, ParamsGetLeadActivity.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessGetLead }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successGetLead")
    public JAXBElement<SuccessGetLead> createSuccessGetLead(SuccessGetLead value) {
        return new JAXBElement<SuccessGetLead>(_SuccessGetLead_QNAME, SuccessGetLead.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsGetLeadChanges }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsGetLeadChanges")
    public JAXBElement<ParamsGetLeadChanges> createParamsGetLeadChanges(ParamsGetLeadChanges value) {
        return new JAXBElement<ParamsGetLeadChanges>(_ParamsGetLeadChanges_QNAME, ParamsGetLeadChanges.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessListOperation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successListOperation")
    public JAXBElement<SuccessListOperation> createSuccessListOperation(SuccessListOperation value) {
        return new JAXBElement<SuccessListOperation>(_SuccessListOperation_QNAME, SuccessListOperation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessSyncMultipleLeads }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successSyncMultipleLeads")
    public JAXBElement<SuccessSyncMultipleLeads> createSuccessSyncMultipleLeads(SuccessSyncMultipleLeads value) {
        return new JAXBElement<SuccessSyncMultipleLeads>(_SuccessSyncMultipleLeads_QNAME, SuccessSyncMultipleLeads.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthenticationHeaderInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "AuthenticationHeader")
    public JAXBElement<AuthenticationHeaderInfo> createAuthenticationHeader(AuthenticationHeaderInfo value) {
        return new JAXBElement<AuthenticationHeaderInfo>(_AuthenticationHeader_QNAME, AuthenticationHeaderInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessSyncLead }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successSyncLead")
    public JAXBElement<SuccessSyncLead> createSuccessSyncLead(SuccessSyncLead value) {
        return new JAXBElement<SuccessSyncLead>(_SuccessSyncLead_QNAME, SuccessSyncLead.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessGetMultipleLeads }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successGetMultipleLeads")
    public JAXBElement<SuccessGetMultipleLeads> createSuccessGetMultipleLeads(SuccessGetMultipleLeads value) {
        return new JAXBElement<SuccessGetMultipleLeads>(_SuccessGetMultipleLeads_QNAME, SuccessGetMultipleLeads.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessGetLeadChanges }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successGetLeadChanges")
    public JAXBElement<SuccessGetLeadChanges> createSuccessGetLeadChanges(SuccessGetLeadChanges value) {
        return new JAXBElement<SuccessGetLeadChanges>(_SuccessGetLeadChanges_QNAME, SuccessGetLeadChanges.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsGetCampaignsForSource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsGetCampaignsForSource")
    public JAXBElement<ParamsGetCampaignsForSource> createParamsGetCampaignsForSource(ParamsGetCampaignsForSource value) {
        return new JAXBElement<ParamsGetCampaignsForSource>(_ParamsGetCampaignsForSource_QNAME, ParamsGetCampaignsForSource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsGetLead }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsGetLead")
    public JAXBElement<ParamsGetLead> createParamsGetLead(ParamsGetLead value) {
        return new JAXBElement<ParamsGetLead>(_ParamsGetLead_QNAME, ParamsGetLead.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessGetLeadActivity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successGetLeadActivity")
    public JAXBElement<SuccessGetLeadActivity> createSuccessGetLeadActivity(SuccessGetLeadActivity value) {
        return new JAXBElement<SuccessGetLeadActivity>(_SuccessGetLeadActivity_QNAME, SuccessGetLeadActivity.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsRequestCampaign }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsRequestCampaign")
    public JAXBElement<ParamsRequestCampaign> createParamsRequestCampaign(ParamsRequestCampaign value) {
        return new JAXBElement<ParamsRequestCampaign>(_ParamsRequestCampaign_QNAME, ParamsRequestCampaign.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsSyncMultipleLeads }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsSyncMultipleLeads")
    public JAXBElement<ParamsSyncMultipleLeads> createParamsSyncMultipleLeads(ParamsSyncMultipleLeads value) {
        return new JAXBElement<ParamsSyncMultipleLeads>(_ParamsSyncMultipleLeads_QNAME, ParamsSyncMultipleLeads.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "attrType", scope = Attribute.class)
    public JAXBElement<String> createAttributeAttrType(String value) {
        return new JAXBElement<String>(_AttributeAttrType_QNAME, String.class, Attribute.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfLeadStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "statusList", scope = ResultListOperation.class)
    public JAXBElement<ArrayOfLeadStatus> createResultListOperationStatusList(ArrayOfLeadStatus value) {
        return new JAXBElement<ArrayOfLeadStatus>(_ResultListOperationStatusList_QNAME, ArrayOfLeadStatus.class, ResultListOperation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfActivityRecord }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "activityRecordList", scope = LeadActivityList.class)
    public JAXBElement<ArrayOfActivityRecord> createLeadActivityListActivityRecordList(ArrayOfActivityRecord value) {
        return new JAXBElement<ArrayOfActivityRecord>(_LeadActivityListActivityRecordList_QNAME, ArrayOfActivityRecord.class, LeadActivityList.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LeadRecord }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "leadRecord", scope = ResultSyncLead.class)
    public JAXBElement<LeadRecord> createResultSyncLeadLeadRecord(LeadRecord value) {
        return new JAXBElement<LeadRecord>(_ResultSyncLeadLeadRecord_QNAME, LeadRecord.class, ResultSyncLead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "error", scope = SyncStatus.class)
    public JAXBElement<String> createSyncStatusError(String value) {
        return new JAXBElement<String>(_SyncStatusError_QNAME, String.class, SyncStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfLeadRecord }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "leadRecordList", scope = ResultGetMultipleLeads.class)
    public JAXBElement<ArrayOfLeadRecord> createResultGetMultipleLeadsLeadRecordList(ArrayOfLeadRecord value) {
        return new JAXBElement<ArrayOfLeadRecord>(_ResultGetMultipleLeadsLeadRecordList_QNAME, ArrayOfLeadRecord.class, ResultGetMultipleLeads.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCampaignRecord }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "campaignRecordList", scope = ResultGetCampaignsForSource.class)
    public JAXBElement<ArrayOfCampaignRecord> createResultGetCampaignsForSourceCampaignRecordList(ArrayOfCampaignRecord value) {
        return new JAXBElement<ArrayOfCampaignRecord>(_ResultGetCampaignsForSourceCampaignRecordList_QNAME, ArrayOfCampaignRecord.class, ResultGetCampaignsForSource.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAttribute }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "activityAttributes", scope = LeadChangeRecord.class)
    public JAXBElement<ArrayOfAttribute> createLeadChangeRecordActivityAttributes(ArrayOfAttribute value) {
        return new JAXBElement<ArrayOfAttribute>(_LeadChangeRecordActivityAttributes_QNAME, ArrayOfAttribute.class, LeadChangeRecord.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "mktgAssetName", scope = LeadChangeRecord.class)
    public JAXBElement<String> createLeadChangeRecordMktgAssetName(String value) {
        return new JAXBElement<String>(_LeadChangeRecordMktgAssetName_QNAME, String.class, LeadChangeRecord.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfLeadRecord }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "leadRecordList", scope = ResultGetLead.class)
    public JAXBElement<ArrayOfLeadRecord> createResultGetLeadLeadRecordList(ArrayOfLeadRecord value) {
        return new JAXBElement<ArrayOfLeadRecord>(_ResultGetMultipleLeadsLeadRecordList_QNAME, ArrayOfLeadRecord.class, ResultGetLead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "dedupEnabled", scope = ParamsSyncMultipleLeads.class)
    public JAXBElement<Boolean> createParamsSyncMultipleLeadsDedupEnabled(Boolean value) {
        return new JAXBElement<Boolean>(_ParamsSyncMultipleLeadsDedupEnabled_QNAME, Boolean.class, ParamsSyncMultipleLeads.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "marketoCookie", scope = ParamsSyncLead.class)
    public JAXBElement<String> createParamsSyncLeadMarketoCookie(String value) {
        return new JAXBElement<String>(_ParamsSyncLeadMarketoCookie_QNAME, String.class, ParamsSyncLead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfLeadChangeRecord }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "leadChangeRecordList", scope = ResultGetLeadChanges.class)
    public JAXBElement<ArrayOfLeadChangeRecord> createResultGetLeadChangesLeadChangeRecordList(ArrayOfLeadChangeRecord value) {
        return new JAXBElement<ArrayOfLeadChangeRecord>(_ResultGetLeadChangesLeadChangeRecordList_QNAME, ArrayOfLeadChangeRecord.class, ResultGetLeadChanges.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "batchSize", scope = ParamsGetLeadActivity.class)
    public JAXBElement<Integer> createParamsGetLeadActivityBatchSize(Integer value) {
        return new JAXBElement<Integer>(_ParamsGetLeadActivityBatchSize_QNAME, Integer.class, ParamsGetLeadActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActivityTypeFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "activityFilter", scope = ParamsGetLeadActivity.class)
    public JAXBElement<ActivityTypeFilter> createParamsGetLeadActivityActivityFilter(ActivityTypeFilter value) {
        return new JAXBElement<ActivityTypeFilter>(_ParamsGetLeadActivityActivityFilter_QNAME, ActivityTypeFilter.class, ParamsGetLeadActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StreamPosition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "startPosition", scope = ParamsGetLeadActivity.class)
    public JAXBElement<StreamPosition> createParamsGetLeadActivityStartPosition(StreamPosition value) {
        return new JAXBElement<StreamPosition>(_ParamsGetLeadActivityStartPosition_QNAME, StreamPosition.class, ParamsGetLeadActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "name", scope = ParamsGetCampaignsForSource.class)
    public JAXBElement<String> createParamsGetCampaignsForSourceName(String value) {
        return new JAXBElement<String>(_ParamsGetCampaignsForSourceName_QNAME, String.class, ParamsGetCampaignsForSource.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "exactName", scope = ParamsGetCampaignsForSource.class)
    public JAXBElement<Boolean> createParamsGetCampaignsForSourceExactName(Boolean value) {
        return new JAXBElement<Boolean>(_ParamsGetCampaignsForSourceExactName_QNAME, Boolean.class, ParamsGetCampaignsForSource.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "oldestCreatedAt", scope = StreamPosition.class)
    public JAXBElement<XMLGregorianCalendar> createStreamPositionOldestCreatedAt(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_StreamPositionOldestCreatedAt_QNAME, XMLGregorianCalendar.class, StreamPosition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "activityCreatedAt", scope = StreamPosition.class)
    public JAXBElement<XMLGregorianCalendar> createStreamPositionActivityCreatedAt(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_StreamPositionActivityCreatedAt_QNAME, XMLGregorianCalendar.class, StreamPosition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "latestCreatedAt", scope = StreamPosition.class)
    public JAXBElement<XMLGregorianCalendar> createStreamPositionLatestCreatedAt(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_StreamPositionLatestCreatedAt_QNAME, XMLGregorianCalendar.class, StreamPosition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "offset", scope = StreamPosition.class)
    public JAXBElement<String> createStreamPositionOffset(String value) {
        return new JAXBElement<String>(_StreamPositionOffset_QNAME, String.class, StreamPosition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "strict", scope = ParamsListOperation.class)
    public JAXBElement<Boolean> createParamsListOperationStrict(Boolean value) {
        return new JAXBElement<Boolean>(_ParamsListOperationStrict_QNAME, Boolean.class, ParamsListOperation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "foreignSysId", scope = ActivityRecord.class)
    public JAXBElement<String> createActivityRecordForeignSysId(String value) {
        return new JAXBElement<String>(_ActivityRecordForeignSysId_QNAME, String.class, ActivityRecord.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAttribute }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "activityAttributes", scope = ActivityRecord.class)
    public JAXBElement<ArrayOfAttribute> createActivityRecordActivityAttributes(ArrayOfAttribute value) {
        return new JAXBElement<ArrayOfAttribute>(_LeadChangeRecordActivityAttributes_QNAME, ArrayOfAttribute.class, ActivityRecord.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "foreignSysOrgId", scope = ActivityRecord.class)
    public JAXBElement<String> createActivityRecordForeignSysOrgId(String value) {
        return new JAXBElement<String>(_ActivityRecordForeignSysOrgId_QNAME, String.class, ActivityRecord.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "orgName", scope = ActivityRecord.class)
    public JAXBElement<String> createActivityRecordOrgName(String value) {
        return new JAXBElement<String>(_ActivityRecordOrgName_QNAME, String.class, ActivityRecord.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "campaign", scope = ActivityRecord.class)
    public JAXBElement<String> createActivityRecordCampaign(String value) {
        return new JAXBElement<String>(_ActivityRecordCampaign_QNAME, String.class, ActivityRecord.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "personName", scope = ActivityRecord.class)
    public JAXBElement<String> createActivityRecordPersonName(String value) {
        return new JAXBElement<String>(_ActivityRecordPersonName_QNAME, String.class, ActivityRecord.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "description", scope = VersionedItem.class)
    public JAXBElement<String> createVersionedItemDescription(String value) {
        return new JAXBElement<String>(_VersionedItemDescription_QNAME, String.class, VersionedItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "type", scope = VersionedItem.class)
    public JAXBElement<String> createVersionedItemType(String value) {
        return new JAXBElement<String>(_VersionedItemType_QNAME, String.class, VersionedItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "streamPosition", scope = ParamsGetMultipleLeads.class)
    public JAXBElement<String> createParamsGetMultipleLeadsStreamPosition(String value) {
        return new JAXBElement<String>(_ParamsGetMultipleLeadsStreamPosition_QNAME, String.class, ParamsGetMultipleLeads.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "batchSize", scope = ParamsGetMultipleLeads.class)
    public JAXBElement<Integer> createParamsGetMultipleLeadsBatchSize(Integer value) {
        return new JAXBElement<Integer>(_ParamsGetLeadActivityBatchSize_QNAME, Integer.class, ParamsGetMultipleLeads.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "batchSize", scope = ParamsGetLeadChanges.class)
    public JAXBElement<Integer> createParamsGetLeadChangesBatchSize(Integer value) {
        return new JAXBElement<Integer>(_ParamsGetLeadActivityBatchSize_QNAME, Integer.class, ParamsGetLeadChanges.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActivityTypeFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "activityFilter", scope = ParamsGetLeadChanges.class)
    public JAXBElement<ActivityTypeFilter> createParamsGetLeadChangesActivityFilter(ActivityTypeFilter value) {
        return new JAXBElement<ActivityTypeFilter>(_ParamsGetLeadActivityActivityFilter_QNAME, ActivityTypeFilter.class, ParamsGetLeadChanges.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ForeignSysType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ForeignSysType", scope = LeadRecord.class)
    public JAXBElement<ForeignSysType> createLeadRecordForeignSysType(ForeignSysType value) {
        return new JAXBElement<ForeignSysType>(_LeadRecordForeignSysType_QNAME, ForeignSysType.class, LeadRecord.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAttribute }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "leadAttributeList", scope = LeadRecord.class)
    public JAXBElement<ArrayOfAttribute> createLeadRecordLeadAttributeList(ArrayOfAttribute value) {
        return new JAXBElement<ArrayOfAttribute>(_LeadRecordLeadAttributeList_QNAME, ArrayOfAttribute.class, LeadRecord.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ForeignSysPersonId", scope = LeadRecord.class)
    public JAXBElement<String> createLeadRecordForeignSysPersonId(String value) {
        return new JAXBElement<String>(_LeadRecordForeignSysPersonId_QNAME, String.class, LeadRecord.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Email", scope = LeadRecord.class)
    public JAXBElement<String> createLeadRecordEmail(String value) {
        return new JAXBElement<String>(_LeadRecordEmail_QNAME, String.class, LeadRecord.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Id", scope = LeadRecord.class)
    public JAXBElement<Integer> createLeadRecordId(Integer value) {
        return new JAXBElement<Integer>(_LeadRecordId_QNAME, Integer.class, LeadRecord.class, value);
    }

}
