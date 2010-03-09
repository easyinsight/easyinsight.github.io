/**
 * AssociateEmailIdWithCampaignActionGroupIdArgs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class AssociateEmailIdWithCampaignActionGroupIdArgs  implements java.io.Serializable {
    /* The Id of the Email. */
    private java.math.BigInteger email_id;

    /* The ID of the Campaign Action Group. */
    private java.math.BigInteger campaign_action_group_id;

    public AssociateEmailIdWithCampaignActionGroupIdArgs() {
    }

    public AssociateEmailIdWithCampaignActionGroupIdArgs(
           java.math.BigInteger email_id,
           java.math.BigInteger campaign_action_group_id) {
           this.email_id = email_id;
           this.campaign_action_group_id = campaign_action_group_id;
    }


    /**
     * Gets the email_id value for this AssociateEmailIdWithCampaignActionGroupIdArgs.
     * 
     * @return email_id   * The Id of the Email.
     */
    public java.math.BigInteger getEmail_id() {
        return email_id;
    }


    /**
     * Sets the email_id value for this AssociateEmailIdWithCampaignActionGroupIdArgs.
     * 
     * @param email_id   * The Id of the Email.
     */
    public void setEmail_id(java.math.BigInteger email_id) {
        this.email_id = email_id;
    }


    /**
     * Gets the campaign_action_group_id value for this AssociateEmailIdWithCampaignActionGroupIdArgs.
     * 
     * @return campaign_action_group_id   * The ID of the Campaign Action Group.
     */
    public java.math.BigInteger getCampaign_action_group_id() {
        return campaign_action_group_id;
    }


    /**
     * Sets the campaign_action_group_id value for this AssociateEmailIdWithCampaignActionGroupIdArgs.
     * 
     * @param campaign_action_group_id   * The ID of the Campaign Action Group.
     */
    public void setCampaign_action_group_id(java.math.BigInteger campaign_action_group_id) {
        this.campaign_action_group_id = campaign_action_group_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AssociateEmailIdWithCampaignActionGroupIdArgs)) return false;
        AssociateEmailIdWithCampaignActionGroupIdArgs other = (AssociateEmailIdWithCampaignActionGroupIdArgs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.email_id==null && other.getEmail_id()==null) || 
             (this.email_id!=null &&
              this.email_id.equals(other.getEmail_id()))) &&
            ((this.campaign_action_group_id==null && other.getCampaign_action_group_id()==null) || 
             (this.campaign_action_group_id!=null &&
              this.campaign_action_group_id.equals(other.getCampaign_action_group_id())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getEmail_id() != null) {
            _hashCode += getEmail_id().hashCode();
        }
        if (getCampaign_action_group_id() != null) {
            _hashCode += getCampaign_action_group_id().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AssociateEmailIdWithCampaignActionGroupIdArgs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "associateEmailIdWithCampaignActionGroupIdArgs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaign_action_group_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "campaign_action_group_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
