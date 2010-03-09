/**
 * ContactImportMapping.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class ContactImportMapping  implements java.io.Serializable {
    /* The heading (i.e. first row in the column) in the CSV file. */
    private java.lang.String csv_header;

    /* The Net-Results Contact Attribute, must exist in the array
     * returned by getContactFields(). */
    private java.lang.String nr_contact_attribute;

    public ContactImportMapping() {
    }

    public ContactImportMapping(
           java.lang.String csv_header,
           java.lang.String nr_contact_attribute) {
           this.csv_header = csv_header;
           this.nr_contact_attribute = nr_contact_attribute;
    }


    /**
     * Gets the csv_header value for this ContactImportMapping.
     * 
     * @return csv_header   * The heading (i.e. first row in the column) in the CSV file.
     */
    public java.lang.String getCsv_header() {
        return csv_header;
    }


    /**
     * Sets the csv_header value for this ContactImportMapping.
     * 
     * @param csv_header   * The heading (i.e. first row in the column) in the CSV file.
     */
    public void setCsv_header(java.lang.String csv_header) {
        this.csv_header = csv_header;
    }


    /**
     * Gets the nr_contact_attribute value for this ContactImportMapping.
     * 
     * @return nr_contact_attribute   * The Net-Results Contact Attribute, must exist in the array
     * returned by getContactFields().
     */
    public java.lang.String getNr_contact_attribute() {
        return nr_contact_attribute;
    }


    /**
     * Sets the nr_contact_attribute value for this ContactImportMapping.
     * 
     * @param nr_contact_attribute   * The Net-Results Contact Attribute, must exist in the array
     * returned by getContactFields().
     */
    public void setNr_contact_attribute(java.lang.String nr_contact_attribute) {
        this.nr_contact_attribute = nr_contact_attribute;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ContactImportMapping)) return false;
        ContactImportMapping other = (ContactImportMapping) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.csv_header==null && other.getCsv_header()==null) || 
             (this.csv_header!=null &&
              this.csv_header.equals(other.getCsv_header()))) &&
            ((this.nr_contact_attribute==null && other.getNr_contact_attribute()==null) || 
             (this.nr_contact_attribute!=null &&
              this.nr_contact_attribute.equals(other.getNr_contact_attribute())));
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
        if (getCsv_header() != null) {
            _hashCode += getCsv_header().hashCode();
        }
        if (getNr_contact_attribute() != null) {
            _hashCode += getNr_contact_attribute().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ContactImportMapping.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "ContactImportMapping"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("csv_header");
        elemField.setXmlName(new javax.xml.namespace.QName("", "csv_header"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nr_contact_attribute");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nr_contact_attribute"));
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
