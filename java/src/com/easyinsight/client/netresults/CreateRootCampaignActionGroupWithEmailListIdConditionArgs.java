/**
 * CreateRootCampaignActionGroupWithEmailListIdConditionArgs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class CreateRootCampaignActionGroupWithEmailListIdConditionArgs  implements java.io.Serializable {
    /* The Name of the Campaign Action Group */
    private java.lang.String campaign_action_group_name;

    /* The ID of the Campaign. */
    private java.math.BigInteger campaign_id;

    /* The ID of the EmailList. */
    private java.math.BigInteger email_list_id;

    public CreateRootCampaignActionGroupWithEmailListIdConditionArgs() {
    }

    public CreateRootCampaignActionGroupWithEmailListIdConditionArgs(
           java.lang.String campaign_action_group_name,
           java.math.BigInteger campaign_id,
           java.math.BigInteger email_list_id) {
           this.campaign_action_group_name = campaign_action_group_name;
           this.campaign_id = campaign_id;
           this.email_list_id = email_list_id;
    }


    /**
     * Gets the campaign_action_group_name value for this CreateRootCampaignActionGroupWithEmailListIdConditionArgs.
     * 
     * @return campaign_action_group_name   * The Name of the Campaign Action Group
     */
    public java.lang.String getCampaign_action_group_name() {
        return campaign_action_group_name;
    }


    /**
     * Sets the campaign_action_group_name value for this CreateRootCampaignActionGroupWithEmailListIdConditionArgs.
     * 
     * @param campaign_action_group_name   * The Name of the Campaign Action Group
     */
    public void setCampaign_action_group_name(java.lang.String campaign_action_group_name) {
        this.campaign_action_group_name = campaign_action_group_name;
    }


    /**
     * Gets the campaign_id value for this CreateRootCampaignActionGroupWithEmailListIdConditionArgs.
     * 
     * @return campaign_id   * The ID of the Campaign.
     */
    public java.math.BigInteger getCampaign_id() {
        return campaign_id;
    }


    /**
     * Sets the campaign_id value for this CreateRootCampaignActionGroupWithEmailListIdConditionArgs.
     * 
     * @param campaign_id   * The ID of the Campaign.
     */
    public void setCampaign_id(java.math.BigInteger campaign_id) {
        this.campaign_id = campaign_id;
    }


    /**
     * Gets the email_list_id value for this CreateRootCampaignActionGroupWithEmailListIdConditionArgs.
     * 
     * @return email_list_id   * The ID of the EmailList.
     */
    public java.math.BigInteger getEmail_list_id() {
        return email_list_id;
    }


    /**
     * Sets the email_list_id value for this CreateRootCampaignActionGroupWithEmailListIdConditionArgs.
     * 
     * @param email_list_id   * The ID of the EmailList.
     */
    public void setEmail_list_id(java.math.BigInteger email_list_id) {
        this.email_list_id = email_list_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateRootCampaignActionGroupWithEmailListIdConditionArgs)) return false;
        CreateRootCampaignActionGroupWithEmailListIdConditionArgs other = (CreateRootCampaignActionGroupWithEmailListIdConditionArgs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.campaign_action_group_name==null && other.getCampaign_action_group_name()==null) || 
             (this.campaign_action_group_name!=null &&
              this.campaign_action_group_name.equals(other.getCampaign_action_group_name()))) &&
            ((this.campaign_id==null && other.getCampaign_id()==null) || 
             (this.campaign_id!=null &&
              this.campaign_id.equals(other.getCampaign_id()))) &&
            ((this.email_list_id==null && other.getEmail_list_id()==null) || 
             (this.email_list_id!=null &&
              this.email_list_id.equals(other.getEmail_list_id())));
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
        if (getCampaign_action_group_name() != null) {
            _hashCode += getCampaign_action_group_name().hashCode();
        }
        if (getCampaign_id() != null) {
            _hashCode += getCampaign_id().hashCode();
        }
        if (getEmail_list_id() != null) {
            _hashCode += getEmail_list_id().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateRootCampaignActionGroupWithEmailListIdConditionArgs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "createRootCampaignActionGroupWithEmailListIdConditionArgs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaign_action_group_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "campaign_action_group_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaign_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "campaign_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email_list_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email_list_id"));
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
