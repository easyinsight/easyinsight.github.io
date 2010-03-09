/**
 * NRAPIPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public interface NRAPIPortType extends java.rmi.Remote {

    /**

     */
    public java.math.BigInteger getDefaultRoleId() throws java.rmi.RemoteException;

    /**

     */
    public boolean launchCampaign(java.math.BigInteger campaign_id) throws java.rmi.RemoteException;

    /**

     */
    public java.math.BigInteger associateEmailIdWithCampaignActionGroupId(com.easyinsight.client.netresults.AssociateEmailIdWithCampaignActionGroupIdArgs associateEmailIdWithCampaignActionGroupIdArgs) throws java.rmi.RemoteException;

    /**

     */
    public java.math.BigInteger createRootCampaignActionGroupWithEmailListIdCondition(com.easyinsight.client.netresults.CreateRootCampaignActionGroupWithEmailListIdConditionArgs createRootCampaignActionGroupWithEmailListIdConditionArgs) throws java.rmi.RemoteException;

    /**

     */
    public boolean associateContactIdsWithEmailListIds(java.lang.String[] contact_ids, java.lang.String[] email_list_ids) throws java.rmi.RemoteException;

    /**

     */
    public com.easyinsight.client.netresults.Contact getContactByContactId(java.math.BigInteger contact_id) throws java.rmi.RemoteException;

    /**

     */
    public int[] getContactEmailLists(java.math.BigInteger contact_id) throws java.rmi.RemoteException;

    /**

     */
    public int[] getEmailListContacts(java.math.BigInteger email_list_id) throws java.rmi.RemoteException;

    /**

     */
    public int getContactIdByContactEmailAddress(java.lang.String contact_email_address) throws java.rmi.RemoteException;

    /**

     */
    public java.lang.String[] getContactFields() throws java.rmi.RemoteException;

    /**

     */
    public java.math.BigInteger createCustomField(com.easyinsight.client.netresults.CreateCustomFieldArgs createCustomFieldRequestArgs) throws java.rmi.RemoteException;

    /**

     */
    public com.easyinsight.client.netresults.EmailList[] getEmailLists() throws java.rmi.RemoteException;

    /**

     */
    public java.lang.String createContact(com.easyinsight.client.netresults.CreateContactArgs createContactArgs) throws java.rmi.RemoteException;

    /**

     */
    public java.math.BigInteger createEmailList(com.easyinsight.client.netresults.CreateEmailListArgs createEmailListArgs) throws java.rmi.RemoteException;

    /**

     */
    public java.math.BigInteger submitContactImport(com.easyinsight.client.netresults.SubmitContactImportArgs submitContactImportArgs) throws java.rmi.RemoteException;

    /**

     */
    public java.math.BigInteger submitContactExport(com.easyinsight.client.netresults.SubmitContactExportArgs submitContactExportArgs) throws java.rmi.RemoteException;

    /**

     */
    public com.easyinsight.client.netresults.ContactImportStatus getContactImportStatus(java.math.BigInteger contact_import_id) throws java.rmi.RemoteException;

    /**

     */
    public boolean getContactExportStatus(java.math.BigInteger contact_export_id) throws java.rmi.RemoteException;

    /**

     */
    public com.easyinsight.client.netresults.VisitSummary getContactRecentVisitSummary(java.math.BigInteger contact_id) throws java.rmi.RemoteException;

    /**

     */
    public java.math.BigInteger getContactIdFromVisitorId(java.math.BigInteger visitor_id) throws java.rmi.RemoteException;

    /**

     */
    public java.lang.String createEmail(com.easyinsight.client.netresults.CreateEmailArgs createEmailArgs) throws java.rmi.RemoteException;

    /**

     */
    public java.math.BigInteger createCampaign(com.easyinsight.client.netresults.CreateCampaignArgs createCampaignArgs) throws java.rmi.RemoteException;
}
