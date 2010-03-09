/**
 * Company.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class Company  implements java.io.Serializable {
    /* The Id of the Company */
    private int company_id;

    /* The Company Name */
    private java.lang.String company_name;

    public Company() {
    }

    public Company(
           int company_id,
           java.lang.String company_name) {
           this.company_id = company_id;
           this.company_name = company_name;
    }


    /**
     * Gets the company_id value for this Company.
     * 
     * @return company_id   * The Id of the Company
     */
    public int getCompany_id() {
        return company_id;
    }


    /**
     * Sets the company_id value for this Company.
     * 
     * @param company_id   * The Id of the Company
     */
    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }


    /**
     * Gets the company_name value for this Company.
     * 
     * @return company_name   * The Company Name
     */
    public java.lang.String getCompany_name() {
        return company_name;
    }


    /**
     * Sets the company_name value for this Company.
     * 
     * @param company_name   * The Company Name
     */
    public void setCompany_name(java.lang.String company_name) {
        this.company_name = company_name;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Company)) return false;
        Company other = (Company) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.company_id == other.getCompany_id() &&
            ((this.company_name==null && other.getCompany_name()==null) || 
             (this.company_name!=null &&
              this.company_name.equals(other.getCompany_name())));
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
        _hashCode += getCompany_id();
        if (getCompany_name() != null) {
            _hashCode += getCompany_name().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Company.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "Company"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("company_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "company_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("company_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "company_name"));
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
