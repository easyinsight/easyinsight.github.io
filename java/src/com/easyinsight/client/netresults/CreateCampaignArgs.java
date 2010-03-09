/**
 * CreateCampaignArgs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class CreateCampaignArgs  implements java.io.Serializable {
    /* The Name of the Campaign. */
    private java.lang.String campaign_name;

    /* The description of the campaign. */
    private java.lang.String campaign_description;

    /* The Campaign's Launch Date. */
    private java.util.Calendar campaign_launch_date;

    /* The Role Id of the Campaign. */
    private java.math.BigInteger role_id;

    public CreateCampaignArgs() {
    }

    public CreateCampaignArgs(
           java.lang.String campaign_name,
           java.lang.String campaign_description,
           java.util.Calendar campaign_launch_date,
           java.math.BigInteger role_id) {
           this.campaign_name = campaign_name;
           this.campaign_description = campaign_description;
           this.campaign_launch_date = campaign_launch_date;
           this.role_id = role_id;
    }


    /**
     * Gets the campaign_name value for this CreateCampaignArgs.
     * 
     * @return campaign_name   * The Name of the Campaign.
     */
    public java.lang.String getCampaign_name() {
        return campaign_name;
    }


    /**
     * Sets the campaign_name value for this CreateCampaignArgs.
     * 
     * @param campaign_name   * The Name of the Campaign.
     */
    public void setCampaign_name(java.lang.String campaign_name) {
        this.campaign_name = campaign_name;
    }


    /**
     * Gets the campaign_description value for this CreateCampaignArgs.
     * 
     * @return campaign_description   * The description of the campaign.
     */
    public java.lang.String getCampaign_description() {
        return campaign_description;
    }


    /**
     * Sets the campaign_description value for this CreateCampaignArgs.
     * 
     * @param campaign_description   * The description of the campaign.
     */
    public void setCampaign_description(java.lang.String campaign_description) {
        this.campaign_description = campaign_description;
    }


    /**
     * Gets the campaign_launch_date value for this CreateCampaignArgs.
     * 
     * @return campaign_launch_date   * The Campaign's Launch Date.
     */
    public java.util.Calendar getCampaign_launch_date() {
        return campaign_launch_date;
    }


    /**
     * Sets the campaign_launch_date value for this CreateCampaignArgs.
     * 
     * @param campaign_launch_date   * The Campaign's Launch Date.
     */
    public void setCampaign_launch_date(java.util.Calendar campaign_launch_date) {
        this.campaign_launch_date = campaign_launch_date;
    }


    /**
     * Gets the role_id value for this CreateCampaignArgs.
     * 
     * @return role_id   * The Role Id of the Campaign.
     */
    public java.math.BigInteger getRole_id() {
        return role_id;
    }


    /**
     * Sets the role_id value for this CreateCampaignArgs.
     * 
     * @param role_id   * The Role Id of the Campaign.
     */
    public void setRole_id(java.math.BigInteger role_id) {
        this.role_id = role_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateCampaignArgs)) return false;
        CreateCampaignArgs other = (CreateCampaignArgs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.campaign_name==null && other.getCampaign_name()==null) || 
             (this.campaign_name!=null &&
              this.campaign_name.equals(other.getCampaign_name()))) &&
            ((this.campaign_description==null && other.getCampaign_description()==null) || 
             (this.campaign_description!=null &&
              this.campaign_description.equals(other.getCampaign_description()))) &&
            ((this.campaign_launch_date==null && other.getCampaign_launch_date()==null) || 
             (this.campaign_launch_date!=null &&
              this.campaign_launch_date.equals(other.getCampaign_launch_date()))) &&
            ((this.role_id==null && other.getRole_id()==null) || 
             (this.role_id!=null &&
              this.role_id.equals(other.getRole_id())));
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
        if (getCampaign_name() != null) {
            _hashCode += getCampaign_name().hashCode();
        }
        if (getCampaign_description() != null) {
            _hashCode += getCampaign_description().hashCode();
        }
        if (getCampaign_launch_date() != null) {
            _hashCode += getCampaign_launch_date().hashCode();
        }
        if (getRole_id() != null) {
            _hashCode += getRole_id().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateCampaignArgs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "createCampaignArgs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaign_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "campaign_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaign_description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "campaign_description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaign_launch_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "campaign_launch_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("role_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "role_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setNillable(true);
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
