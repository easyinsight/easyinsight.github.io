/**
 * EmailList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class EmailList  implements java.io.Serializable {
    /* The Id of the EmailList */
    private int email_list_id;

    /* The name of the EmailList */
    private java.lang.String email_list_name;

    public EmailList() {
    }

    public EmailList(
           int email_list_id,
           java.lang.String email_list_name) {
           this.email_list_id = email_list_id;
           this.email_list_name = email_list_name;
    }


    /**
     * Gets the email_list_id value for this EmailList.
     * 
     * @return email_list_id   * The Id of the EmailList
     */
    public int getEmail_list_id() {
        return email_list_id;
    }


    /**
     * Sets the email_list_id value for this EmailList.
     * 
     * @param email_list_id   * The Id of the EmailList
     */
    public void setEmail_list_id(int email_list_id) {
        this.email_list_id = email_list_id;
    }


    /**
     * Gets the email_list_name value for this EmailList.
     * 
     * @return email_list_name   * The name of the EmailList
     */
    public java.lang.String getEmail_list_name() {
        return email_list_name;
    }


    /**
     * Sets the email_list_name value for this EmailList.
     * 
     * @param email_list_name   * The name of the EmailList
     */
    public void setEmail_list_name(java.lang.String email_list_name) {
        this.email_list_name = email_list_name;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EmailList)) return false;
        EmailList other = (EmailList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.email_list_id == other.getEmail_list_id() &&
            ((this.email_list_name==null && other.getEmail_list_name()==null) || 
             (this.email_list_name!=null &&
              this.email_list_name.equals(other.getEmail_list_name())));
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
        _hashCode += getEmail_list_id();
        if (getEmail_list_name() != null) {
            _hashCode += getEmail_list_name().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EmailList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "EmailList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email_list_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email_list_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email_list_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email_list_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
